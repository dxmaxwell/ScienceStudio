/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 * 
 * Description:
 *    StatisticDeviceEventDispatcherImpl class.
 *    
 */
package ca.sciencestudio.device.control.event.dispatcher.impl;

import java.util.List;
import java.util.LinkedList;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.dispatcher.impl.DefaultDeviceEventDispatcherImpl;

/**
 * @author maxweld
 *
 */
public class StatisticDeviceEventDispatcherImpl extends DefaultDeviceEventDispatcherImpl {

	private static final long STATISTICS_SAMPLE_TIME = 2000L;
	
	private DispatcherStatsThread dispatcherStatsThread = new DispatcherStatsThread();
	
	private LinkedList<Long> arrivalTimeRecord = new LinkedList<Long>();
	private LinkedList<Long> dispatchTimeRecord = new LinkedList<Long>();
	private long previousAbsoluteArrivalTime = getSystemMicroTime();
	private long dispatchTimeStart = 0L;
	
	public StatisticDeviceEventDispatcherImpl() {
		dispatcherStatsThread.start();
	}
	
	@Override
	public void dispatchEvent(List<DeviceEventListener> deviceEventListeners, DeviceEvent deviceEvent) {
		recordArrivalTime();
		super.dispatchEvent(deviceEventListeners, deviceEvent);
	}
	
	@Override
	protected void doDispatchEvent(DeviceEvent deviceEvent, List<DeviceEventListener> deviceEventListeners) {		
		recordDispatchTimeStart();
		super.doDispatchEvent(deviceEvent, deviceEventListeners);
		recordDispatchTimeEnd();
	}

	protected synchronized void recordArrivalTime() {
		long absoluteArrivalTime = getSystemMicroTime();
		long arrivalTime = absoluteArrivalTime - previousAbsoluteArrivalTime;
		if(arrivalTime > 0L) {					
			arrivalTimeRecord.addLast(arrivalTime);
		}
		previousAbsoluteArrivalTime = absoluteArrivalTime;
	}
	
	protected synchronized void recordDispatchTimeStart() {
		dispatchTimeStart = getSystemMicroTime();
	}
	
	protected synchronized void recordDispatchTimeEnd() {
		long dispatchTime = getSystemMicroTime() - dispatchTimeStart;
		if(dispatchTime > 0L) {
			dispatchTimeRecord.addLast(dispatchTime);
		}
	}
	
	protected synchronized double[] computeStatisticsForArrivalTime() {
		double [] statistics = computeStatistics(arrivalTimeRecord);
		arrivalTimeRecord.clear();
		return statistics;
	}
	
	protected synchronized double[] computeStatisticsForDispatchTime() {
		double [] statistics = computeStatistics(dispatchTimeRecord);
		dispatchTimeRecord.clear();
		return statistics;
	}
	
	protected double[] computeStatistics(List<Long> xList) {
		
		long sum = 0;
		long sum2 = 0;
		
		for(long x : xList) {
			sum += x;
			sum2 += (x * x);
		}
		
		double mean = Double.NaN;
		double rms = Double.NaN;
		double n = xList.size();
		
		if(n > 0) {
			/* convert micro-seconds to milli-seconds */ 
			mean = (sum / n) / 1000.0;
			rms = Math.sqrt(((n*sum2) - (sum*sum))/(n*n)) / 1000.0;
		} 
		
		return new double[] { n, mean, rms };
	}

	protected long getSystemMicroTime() {
		return System.nanoTime() / 1000L;
	}
	
	private class DispatcherStatsThread extends Thread {

		public DispatcherStatsThread() {
			setPriority(Thread.MIN_PRIORITY);
			setDaemon(true);
		}
			
		@Override
		public void run() {
			while(true) {
				
				double sampleTime = STATISTICS_SAMPLE_TIME / 1000.0;
				
				double[] arrivalTimeStats = computeStatisticsForArrivalTime();
				double arrivalRate = arrivalTimeStats[0] / sampleTime;				
				log.info(String.format("Device events arrived: %.1f per sec. Mean/RMS arrival time: %.3f / %.3f msec.", arrivalRate, arrivalTimeStats[1], arrivalTimeStats[2]));
				
				double[] dispatchTimeStats = computeStatisticsForDispatchTime();
				double dispatchRate = dispatchTimeStats[0] / sampleTime;
				log.info(String.format("Device events dispatched: %.1f per sec. Mean/RMS dispatch time: %.3f / %.3f msec.", dispatchRate, dispatchTimeStats[1], dispatchTimeStats[2]));
				
				try {
					sleep(STATISTICS_SAMPLE_TIME);
				}
				catch(InterruptedException e) {
					log.warn("Dispatcher statistics thread has been interupted.");
					break;
				}
			}
		}
		
	}
}
