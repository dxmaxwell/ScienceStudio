/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFTypesUtility class.
 *     
 */
package ca.sciencestudio.data.cdf;

import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;

import gsfc.nssdc.cdf.CDF;
import gsfc.nssdc.cdf.CDFException;
import gsfc.nssdc.cdf.util.Epoch;
import gsfc.nssdc.cdf.util.Epoch16;
import ca.sciencestudio.data.util.TypesUtility;
import ca.sciencestudio.data.support.TypesException;
import ca.sciencestudio.data.support.CDFTypesException;

/**
 * @author maxweld
 *
 */
public class CDFTypesUtility extends TypesUtility {

	public static final String CDF_EPOCH_TIME_ZONE_ID = "GMT";
	
	public static double getCDFEpoch() throws CDFException {
		return getCDFEpoch(new Date());
	}
	
	public static double getCDFEpoch(long millis) throws CDFException {
		return getCDFEpoch(new Date(millis));
	}
	
	public static double getCDFEpoch(Date date) throws CDFException {
		TimeZone tz = TimeZone.getTimeZone(CDF_EPOCH_TIME_ZONE_ID);
		Calendar calendar = Calendar.getInstance(tz);
		calendar.setTime(date);
		long year = calendar.get(Calendar.YEAR);
		long month = calendar.get(Calendar.MONTH) + 1;
		long day = calendar.get(Calendar.DAY_OF_MONTH);
		long hour = calendar.get(Calendar.HOUR_OF_DAY);
		long minute = calendar.get(Calendar.MINUTE);
		long second = calendar.get(Calendar.SECOND);
		long msecond = calendar.get(Calendar.MILLISECOND);
		return Epoch.compute(year, month, day, hour, minute, second, msecond);
	}
	
	public static double[] getCDFEpoch16() throws CDFException {
		return getCDFEpoch16(new Date(), 0L, 0L, 0L);
	}
	
	public static double[] getCDFEpoch16(long millis, long micros, long nanos, long picos) throws CDFException {
		return getCDFEpoch16(new Date(millis), micros, nanos, picos);
	}
	
	public static double[] getCDFEpoch16(Date date, long micros, long nanos, long picos) throws CDFException {
		TimeZone tz = TimeZone.getTimeZone(CDF_EPOCH_TIME_ZONE_ID);
		Calendar calendar = Calendar.getInstance(tz);
		calendar.setTime(date);
		double[] epoch16 = new double[2];
		long year = calendar.get(Calendar.YEAR);
		long month = calendar.get(Calendar.MONTH) + 1;
		long day = calendar.get(Calendar.DAY_OF_MONTH);
		long hour = calendar.get(Calendar.HOUR_OF_DAY);
		long minute = calendar.get(Calendar.MINUTE);
		long second = calendar.get(Calendar.SECOND);
		long millis = calendar.get(Calendar.MILLISECOND);
		Epoch16.compute(year, month, day, hour, minute, second, millis, micros, nanos, picos, epoch16);
		return epoch16;
	}
	
	public static Object getCDFValue(long cdfType, Object value) throws TypesException {
		
		switch ((int)cdfType) {
			
			case (int)CDF.CDF_BYTE:
			case (int)CDF.CDF_INT1:
				return TypesUtility.getByteValue(value); 
				
			case (int)CDF.CDF_UINT1:
			case (int)CDF.CDF_INT2:
				return TypesUtility.getShortValue(value);
			
			case (int)CDF.CDF_UINT2:
			case (int)CDF.CDF_INT4:
				return TypesUtility.getIntValue(value);
			
			case (int)CDF.CDF_UINT4:
				return TypesUtility.getLongValue(value);
			
			case (int)CDF.CDF_FLOAT:
			case (int)CDF.CDF_REAL4:
				return TypesUtility.getFloatValue(value);
			
			case (int)CDF.CDF_DOUBLE:
			case (int)CDF.CDF_REAL8:
				return TypesUtility.getDoubleValue(value);
			
			case (int)CDF.CDF_CHAR:
			case (int)CDF.CDF_UCHAR:
				return TypesUtility.getStringValue(value);
				
			default:
				throw new CDFTypesException("CDF type (" + cdfType + ") is not supported.");
		}
	}
}
