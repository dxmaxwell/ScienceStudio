/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanDataMapXYController class.
 *
 */
package ca.sciencestudio.data.service.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.cdf.CDFQuery;
import ca.sciencestudio.data.standard.category.MapXY10UniqueCategory;
import ca.sciencestudio.data.support.CDFQueryException;
import ca.sciencestudio.data.support.RecordFormatException;
import ca.sciencestudio.data.util.CategoryUtils;

import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
@RequestMapping("/scan/{scanId}/data/mapxy")
public class ScanDataMapXYController extends AbstractScanDataController {
	
	@RequestMapping("/I.{format}")
	public String getIndexesI(@PathVariable int scanId, @PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		CDFQuery cdfQuery = getCDFQueryWithSecurityCheck(scanId, errors);
		if(errors.hasErrors()) {
			return responseView;
		}
		
		MapXY10UniqueCategory<?> mapXY10 = CategoryUtils.getFirstCategory(cdfQuery.getCategories(), MapXY10);
		if(mapXY10 == null) {
			errors.reject("cdfquery.notmapxy", "CDF file does not contain MapXY data.");
			return responseView;
		}
		
		String[] cdfVarNames = new String[] { mapXY10.I() };
		
		List<CDFRecord> cdfRecords;
		try {
			cdfRecords = cdfQuery.queryRecordsByNames(cdfVarNames);
		}
		catch(CDFQueryException e) {
			String msg = "Exception while executing CDF query. (ScanId:" + scanId + ")";
			errors.reject("cdfquery.error", msg);
			logger.warn(msg, e);
			return responseView;
		}
		
		if(cdfRecords.isEmpty()) {
			String msg = "CDF query result does not contain any records. (ScanId:" + scanId + ")";
			errors.reject("cdfquery.error", msg);
			logger.warn(msg);
			return responseView;
		}
		
		Set<Integer> indexSet = new HashSet<Integer>(cdfRecords.size());
		
		try {
			for(CDFRecord cdfRecord : cdfRecords) {
				indexSet.add(cdfRecord.getIntByName(mapXY10.I()));
			}
		}
		catch(RecordFormatException e) {
			String msg = "Exception while reading CDF query result. (ScanId:" + scanId + ")";
			errors.reject("cdfquery.error", msg);
			logger.warn(msg, e);
			return responseView;
		}
		
		List<Integer> indexList = new ArrayList<Integer>(indexSet);
		Collections.sort(indexList);
		
		Collection<int[]> indexes = new ArrayList<int[]>(indexList.size());
		for(int index : indexList) {
			indexes.add(new int[] { index });
		}
		
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("I", indexes);
		
		model.put("response", response);
		return responseView;
	}
	
	@RequestMapping(value = "/J.{format}", method = RequestMethod.GET)
	public String getIndexesJ(@PathVariable int scanId, @PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		CDFQuery cdfQuery = getCDFQueryWithSecurityCheck(scanId, errors);
		if(errors.hasErrors()) {
			return responseView;
		}
		
		MapXY10UniqueCategory<?> mapXY10 = CategoryUtils.getFirstCategory(cdfQuery.getCategories(), MapXY10);
		if(mapXY10 == null) {
			errors.reject("cdfquery.notmapxy", "CDF file does not contain MapXY data.");
			return responseView;
		}
		
		String[] cdfVarNames = new String[] { mapXY10.J() };
		
		List<CDFRecord> cdfRecords;
		try {
			cdfRecords = cdfQuery.queryRecordsByNames(cdfVarNames);
		}
		catch(CDFQueryException e) {
			String msg = "Exception while executing CDF query. (ScanId:" + scanId + ")";
			errors.reject("cdfquery.error", msg);
			logger.warn(msg, e);
			return responseView;
		}
		
		if(cdfRecords.isEmpty()) {
			String msg = "CDF query result does not contain any records. (ScanId:" + scanId + ")";
			errors.reject("cdfquery.error", msg);
			logger.warn(msg);
			return responseView;
		}
		
		Set<Integer> indexSet = new HashSet<Integer>(cdfRecords.size());
		
		try {
			for(CDFRecord cdfRecord : cdfRecords) {
				indexSet.add(cdfRecord.getIntByName(mapXY10.J()));
			}
		}
		catch(RecordFormatException e) {
			String msg = "Exception while reading CDF query result. (ScanId:" + scanId + ")";
			errors.reject("cdfquery.error", msg);
			logger.warn(msg, e);
			return responseView;
		}
		
		List<Integer> indexList = new ArrayList<Integer>(indexSet);
		Collections.sort(indexList);
		
		Collection<int[]> indexes = new ArrayList<int[]>(indexList.size());
		for(int index : indexList) {
			indexes.add(new int[] { index });
		}
		
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("J", indexes);
		
		model.put("response", response);
		return responseView;
	}

}
