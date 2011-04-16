/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractSimulationDevice class.
 *     
 */
package ca.sciencestudio.device.control.simulation;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import ca.sciencestudio.device.control.simulation.SimulationDevice;
import ca.sciencestudio.device.control.simulation.SimulationEvent;
import ca.sciencestudio.device.control.simulation.SimulationEventListener;

/**
 * @author maxweld
 *
 */
public abstract class AbstractSimulationDevice implements SimulationDevice {

	private static SimulationEventPublisherThread simulationEventPublisherThread = new SimulationEventPublisherThread();
	
	protected String id;
	protected String name;
	protected boolean publishing = false;
	protected Logger log = Logger.getLogger(getClass());
	protected List<SimulationEventListener> eventListeners = new ArrayList<SimulationEventListener>(); 
	
	public AbstractSimulationDevice(String id) {
		this.id = id;
	}
		
	public boolean isPublishing() {
		return publishing;
	}

	public void setPublishing(boolean publishing) {
		this.publishing = publishing;
	}

	public void startPublishing() {
		setPublishing(true);
	}
	
	public void stopPublishing() {
		setPublishing(false);
	}
	
	public void addEventListener(SimulationEventListener eventListener) {	
		if(!eventListeners.contains(eventListener)) { 
			eventListeners.add(eventListener);
		}
	}
	
	public void removeEventListener(SimulationEventListener eventListener) {
		eventListeners.remove(eventListener);
	}
	
	protected void publishEvent(final SimulationEvent event) {	
		asynchronousPublishEvent(event);
	}
	
	protected void asynchronousPublishEvent(final SimulationEvent event) {	
		if(publishing) {
			simulationEventPublisherThread.asynchronousPublishEvent(eventListeners, event);
		}
	}
	
	protected void synchronousPublishEvent(final SimulationEvent event) {
		if(publishing) {
			for(SimulationEventListener sel : eventListeners) {
				sel.handleEvent(event);
			}
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Object> getMapFromObject(Object obj) {
		try {
			return (Map<String,Object>)obj;
		}
		catch(ClassCastException e) {
			log.warn("Object is not the expected class: Map<String,Object>.");
			return new HashMap<String,Object>();
		}
	}
		
	private static class SimulationEventPublisherThread extends Thread {
		
		private LinkedBlockingQueue<SimulationEventPublisherQueueItem> SimulationEventPublisherQueue = 
					new LinkedBlockingQueue<SimulationEventPublisherQueueItem>(1000);
		
		private final Logger log = Logger.getLogger(getClass());
		
		public SimulationEventPublisherThread() {
			setPriority(Thread.MIN_PRIORITY);
			setDaemon(true);
			start();
		}

		@Override
		public void run() {
			while(true) {
				try {
					SimulationEventPublisherQueueItem simulationEventPublisherQueueItem = SimulationEventPublisherQueue.take();
					List<SimulationEventListener> simulationEventListeners = simulationEventPublisherQueueItem.getSimulationEventListeners();
					SimulationEvent simulationEvent = simulationEventPublisherQueueItem.getSimulationEvent();
					for(SimulationEventListener simulationEventListener : simulationEventListeners) {
						simulationEventListener.handleEvent(simulationEvent);
					}
				}
				catch(InterruptedException e) {
					log.warn("SimulationEventPublisherThread has been interrupted.");
					return;
				}
			}
		}
		
		public void asynchronousPublishEvent(List<SimulationEventListener> simulationEventListeners, SimulationEvent simulationEvent) {
			
			SimulationEventPublisherQueueItem  deviceEventPublisherQueueItem = 
				new SimulationEventPublisherQueueItem(simulationEventListeners, simulationEvent);
			
			boolean accepted = SimulationEventPublisherQueue.offer(deviceEventPublisherQueueItem);
			
			if(!accepted) {
				log.warn("SimulationEventPublisherQueue is full, dropping device event.");
			}
		}
		
		private class SimulationEventPublisherQueueItem {
			
			private List<SimulationEventListener> simulationEventListeners;
			private SimulationEvent simulationEvent;
			
			public SimulationEventPublisherQueueItem(List<SimulationEventListener> simulationEventListeners, SimulationEvent simulationEvent) {
				this.simulationEventListeners = simulationEventListeners;
				this.simulationEvent = simulationEvent;
			}

			public List<SimulationEventListener> getSimulationEventListeners() {
				return simulationEventListeners;
			}
			public SimulationEvent getSimulationEvent() {
				return simulationEvent;
			}
		}
		
	}	
}
