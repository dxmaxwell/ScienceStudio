/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractScanConverter class.
 *     
 */
package ca.sciencestudio.data.converter;

import java.util.Date;

import ca.sciencestudio.model.utilities.ScanParameters;

/**
 * @author maxweld
 *
 */
public abstract class AbstractScanConverter extends AbstractConverter {

	private String scanName = null;
	private Date scanEndDate = null;
	private Date scanStartDate = null;
	private String scanDataUrl = null;
	private ScanParameters scanParams = null;

	public AbstractScanConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		super(fromFormat, toFormat, forceUpdate);
	}
	
	public String getScanName() {
		return scanName;
	}
	public void setScanName(String scanName) {
		this.scanName = scanName;
	}
	
	public Date getScanEndDate() {
		return scanEndDate;
	}
	public void setScanEndDate(Date scanEndDate) {
		this.scanEndDate = scanEndDate;
	}
	
	public Date getScanStartDate() {
		return scanStartDate;
	}
	public void setScanStartDate(Date scanStartDate) {
		this.scanStartDate = scanStartDate;
	}
	
	public String getScanDataUrl() {
		return scanDataUrl;
	}
	public void setScanDataUrl(String scanDataUrl) {
		this.scanDataUrl = scanDataUrl;
	}
	
	public ScanParameters getScanParams() {
		return scanParams;
	}
	public void setScanParams(ScanParameters scanParams) {
		this.scanParams = scanParams;
	}
}
