/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     EpicsAlarmStatus enum.
 */
package ca.sciencestudio.device.control.component.epics;

/**
 * @author chabotd
 *
 */
public enum EpicsAlarmStatus {
	NO_ALARM,
	READ_ALARM,
	WRITE_ALARM,
	HIHI_ALARM,
	HIGH_ALARM,
	LOLO_ALARM,
	LOW_ALARM,
	STATE_ALARM,
	COS_ALARM,
	COMM_ALARM,
	TIMEOUT_ALARM,
	HW_LIMIT_ALARM,
	CALC_ALARM,
	SCAN_ALARM,
	LINK_ALARM,
	SOFT_ALARM,
	BAD_SUB_ALARM,
	UDF_ALARM,
	DISABLE_ALARM,
	SIMM_ALARM,
	READ_ACCESS_ALARM,
	WRITE_ACCESS_ALARM;
}
