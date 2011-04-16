/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFRecord class.
 *     
 */
package ca.sciencestudio.data.standard;

/**
 * @author maxweld
 *
 */
public abstract class StdUnits {

	public static final String METRIC_PREFIX_FEMTO = "f";
	public static final String METRIC_PREFIX_PICO = "p";
	public static final String METRIC_PREFIX_NANO = "n";
	public static final String METRIC_PREFIX_MICRO = "u";  //µ - Causes problem in CDF file.
	public static final String METRIC_PREFIX_MILLI = "m";
	public static final String METRIC_PREFIX_CENTI = "c";
	public static final String METRIC_PREFIX_DECI = "d";
	public static final String METRIC_PREFIX_DEKA = "da";
	public static final String METRIC_PREFIX_HECTO = "h";
	public static final String METRIC_PREFIX_KILO = "k";
	public static final String METRIC_PREFIX_MEGA = "M";
	public static final String METRIC_PREFIX_GIGA = "G";
	public static final String METRIC_PREFIX_TERA = "T";
	public static final String METRIC_PREFIX_PETA = "P";

	public static final String METER = "m"; 
	public static final String MILLI_METER = METRIC_PREFIX_MILLI + METER;
	
	public static final String EVOLT = "eV";
	public static final String KILO_EVOLT = METRIC_PREFIX_KILO + EVOLT;
	public static final String GIGA_EVOLT = METRIC_PREFIX_GIGA + EVOLT;
	
	public static final String AMPERE = "A";
	public static final String MILLI_AMPERE = METRIC_PREFIX_MILLI + AMPERE;
	
	public static final String SECOND = "s";
	public static final String MILLI_SECOND = METRIC_PREFIX_MILLI + SECOND;
	public static final String MICRO_SECOND = METRIC_PREFIX_MICRO + SECOND;
	
	public static final String COUNT = "count";
	public static final String PERCENT = "percent";
	
	public static final String UNKNOWN = "UNKNOWN";
}
