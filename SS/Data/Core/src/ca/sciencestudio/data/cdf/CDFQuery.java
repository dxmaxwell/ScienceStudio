/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFQuery class.
 *     
 */
package ca.sciencestudio.data.cdf;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import gsfc.nssdc.cdf.Attribute;
import gsfc.nssdc.cdf.CDF;
import gsfc.nssdc.cdf.CDFException;
import gsfc.nssdc.cdf.Entry;
import gsfc.nssdc.cdf.Variable;

import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.cdf.selector.CDFSelector;
import ca.sciencestudio.data.standard.StdCategories;
import ca.sciencestudio.data.standard.StdConverter;
import ca.sciencestudio.data.standard.StdScanParams;
import ca.sciencestudio.data.standard.category.UniqueCategory;
import ca.sciencestudio.data.support.CDFQueryException;
import ca.sciencestudio.data.util.CategoryUtils;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public class CDFQuery implements StdScanParams, StdConverter, StdCategories {

	private CDF cdf;
	private boolean standard;
	private Collection<UniqueCategory<?>> categories;
	
	@SuppressWarnings("deprecation")
	protected static File getCdfFileFromScan(Scan scan) throws CDFQueryException {
		Parameters scanParams = scan.getParameters();
		String dataFileBase = scanParams.get(PARAM_KEY_DATA_FILE_BASE);
		
		if(dataFileBase == null) {
			String cdfFileName = scanParams.get(PARAM_KEY_CDF_FILE_NAME);
			if(cdfFileName != null) {
				String cdfSuffixRegex = Pattern.quote(FILE_NAME_SUFFIX_CDF) + "\\Z";
				dataFileBase = cdfFileName.replaceAll(cdfSuffixRegex, ""); 
			}
		}
		
		if(dataFileBase == null) {
			String dafFileName = scanParams.get(PARAM_KEY_DAF_FILE_NAME);
			if(dafFileName != null) {
				String dafSuffixRegex = Pattern.quote(FILE_NAME_SUFFIX_DAF_DATA) + "\\Z";
				dataFileBase = dafFileName.replaceAll(dafSuffixRegex, "");
			}
		}
		
		if(dataFileBase == null) {
			throw new CDFQueryException("Cannot get CDF file base name from scan.");
		}
		
		return new File(scan.getDataUrl(), dataFileBase + FILE_NAME_SUFFIX_CDF);
	}
	
	public CDFQuery(Scan scan) throws CDFQueryException {
		this(getCdfFileFromScan(scan));
	}
	
	public CDFQuery(File cdfFile) throws CDFQueryException {
		this(cdfFile.getAbsolutePath());
	}
	
	public CDFQuery(String cdfFilePath) throws CDFQueryException {
		try {
			cdf = CDF.open(cdfFilePath, CDF.READONLYon);
		}
		catch(CDFException e) {
			throw new CDFQueryException("Cannot open CDF file: " + cdfFilePath, e);
		}
		
		try {
			Attribute categoryRegistryAttribute = getAttributeByName(CATEGORY_REGISTRY);
			
			Set<String> categoryRegistryEntries = new HashSet<String>(); 
			for(Object entry : categoryRegistryAttribute.getEntries()) {
				if(entry instanceof Entry) {
					categoryRegistryEntries.add(((Entry)entry).getData().toString());
				}
			}
			
			categories = CategoryUtils.findCategories(categoryRegistryEntries);
			standard = true;
		}
		catch (CDFQueryException e) {
			categories = Collections.emptySet();
			standard = false;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if(cdf != null) {
			cdf.close();
		}
		super.finalize();
	}
	
	public Attribute getAttributeByName(String name) throws CDFQueryException {
		try {
			return cdf.getAttribute(name);
		}
		catch(CDFException e) {
			throw new CDFQueryException("Cannot get attribute with name, " + name + ", in CDF file.", e);
		}
	}
	
	public Attribute getAttributeByID(long id) throws CDFQueryException {
		try {
			return cdf.getAttribute(id);
		}
		catch(CDFException e) {
			throw new CDFQueryException("Cannot get attribute with ID, " + id + ", in CDF file.", e);
		}
	}
	
	
	public Variable getVariableByName(String name) throws CDFQueryException {
		try {
			return cdf.getVariable(name);
		}
		catch(CDFException e) {
			throw new CDFQueryException("Cannot get variable with name, " + name + ", in CDF file.", e);
		}
	}
	
	public Variable getVariableByID(long id) throws CDFQueryException {
		try {
			return cdf.getVariable(id);
		}
		catch(CDFException e) {
			throw new CDFQueryException("Cannot get variable with ID, " + id + ", in CDF file.", e);
		}
	}
	
	public CDFAttributeRecord getAttributeRecordByName(String name) throws CDFQueryException {
		return new CDFAttributeRecord(getAttributeByName(name));	
	}
	
	public CDFAttributeRecord getAttributeRecordByID(long id) throws CDFQueryException {
		return new CDFAttributeRecord(getAttributeByID(id));
	}
	
	public CDFEvent getEventFromVariableNames(String[] cdfVarNames) throws CDFQueryException {
		List<Variable> cdfVariables = new ArrayList<Variable>();
		for(String cdfVarName : cdfVarNames) {	
			cdfVariables.add(getVariableByName(cdfVarName));
		}
		return new CDFEvent(cdfVariables);
	}
	
	public CDFEvent getEventFromVariableIDs(long[] cdfVarIDs) throws CDFQueryException {
		List<Variable> cdfVariables = new ArrayList<Variable>();
		for(long cdfVarID : cdfVarIDs) {	
			cdfVariables.add(getVariableByID(cdfVarID));
		}
		return new CDFEvent(cdfVariables);
	}
	
	public List<CDFRecord> queryRecordsByNames(String[] cdfVarNames) throws CDFQueryException {
		return queryRecordsByNames(cdfVarNames, null);
	}
	
	public List<CDFRecord> queryRecordsByIDs(long[] cdfVarIDs) throws CDFQueryException {
		return queryRecordsByIDs(cdfVarIDs, null);
	}
	
	public List<CDFRecord> queryRecords(CDFEvent cdfEvent) throws CDFQueryException {
		return queryRecords(cdfEvent, null);	
	}
	
	public List<CDFRecord> queryRecordsByNames(String[] cdfVarNames, CDFSelector cdfSelector) throws CDFQueryException {
		return queryRecords(getEventFromVariableNames(cdfVarNames), cdfSelector);
	}
	
	public List<CDFRecord> queryRecordsByIDs(long[] cdfVarIDs, CDFSelector cdfSelector) throws CDFQueryException {
		return queryRecords(getEventFromVariableIDs(cdfVarIDs), cdfSelector);
	}
	
	@SuppressWarnings("unchecked")
	public List<CDFRecord> queryRecords(CDFEvent cdfEvent, CDFSelector cdfSelector) throws CDFQueryException {
		try {
			long numberOfRecords = cdfEvent.getMinimumNumberOfRecords();
		
			CDFRecord cdfRecord;
			Vector<Object> cdfValues;
			List<CDFRecord> cdfRecords = new ArrayList<CDFRecord>();
			
			for(long i = 0L; i < numberOfRecords; i++) {
				cdfValues = cdf.getRecord(i, cdfEvent.getVariableIDs());		
				cdfRecord = new CDFRecord(cdfEvent, cdfValues);
				if((cdfSelector == null) || cdfSelector.select(cdfRecord)) {
					cdfRecords.add(cdfRecord);
				}
			}
			
			return cdfRecords;
		}
		catch(CDFException e) {
			throw new CDFQueryException("Cannot get record for variables, " + cdfEvent.getVariableNames() + ", in CDF file.", e);
		}
	}

	public boolean isStandard() {
		return standard;
	}

	public Collection<UniqueCategory<?>> getCategories() {
		return categories;
	}
}
