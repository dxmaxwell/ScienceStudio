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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.cdf.CDFQuery;
import ca.sciencestudio.data.standard.category.MapXY10UniqueCategory;
import ca.sciencestudio.data.support.CDFQueryException;
import ca.sciencestudio.data.support.RecordFormatException;
import ca.sciencestudio.data.util.CategoryUtils;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanDataMapXYController extends AbstractScanDataController {
	
	@ResponseBody
	@RequestMapping(value = "/scan/{scanGid}/data/mapxy/I*", method = RequestMethod.GET)
	public FormResponseMap getIndexesI(@PathVariable String scanGid) {

		CDFQuery cdfQuery;
		try {
			cdfQuery = getCDFQueryByScanGid(scanGid);
		}
		catch(Exception e) {
			return new FormResponseMap(false, e.getMessage());
		}
		
		MapXY10UniqueCategory<?> mapXY10 = CategoryUtils.getFirstCategory(cdfQuery.getCategories(), MapXY10);
		if(mapXY10 == null) {
			return new FormResponseMap(false, "CDF file does not contain MapXY data.");
		}
		
		String[] cdfVarNames = new String[] { mapXY10.I() };
		
		List<CDFRecord> cdfRecords;
		try {
			cdfRecords = cdfQuery.queryRecordsByNames(cdfVarNames);
		}
		catch(CDFQueryException e) {
			String msg = "Exception while executing CDF query. (Scan:" + scanGid + ")";
			FormResponseMap response = new FormResponseMap(false, msg);
			logger.warn(msg, e);
			return response;
		}
		
		if(cdfRecords.isEmpty()) {
			String msg = "CDF query result does not contain any records. (Scan:" + scanGid + ")";
			FormResponseMap response = new FormResponseMap(false, msg);
			logger.warn(msg);
			return response;
		}
		
		Set<Integer> indexSet = new HashSet<Integer>(cdfRecords.size());
		
		try {
			for(CDFRecord cdfRecord : cdfRecords) {
				indexSet.add(cdfRecord.getIntByName(mapXY10.I()));
			}
		}
		catch(RecordFormatException e) {
			String msg = "Exception while reading CDF query result. (Scan:" + scanGid + ")";
			FormResponseMap response = new FormResponseMap(false, msg);
			logger.warn(msg, e);
			return response;
		}
		
		List<Integer> indexList = new ArrayList<Integer>(indexSet);
		Collections.sort(indexList);
		
		Collection<int[]> indexes = new ArrayList<int[]>(indexList.size());
		for(int index : indexList) {
			indexes.add(new int[] { index });
		}
		
		FormResponseMap response = new FormResponseMap(true);
		response.put("I", indexes);
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/scan/{scanGid}/data/mapxy/J*", method = RequestMethod.GET)
	public FormResponseMap getIndexesJ(@PathVariable String scanGid) {
		
		CDFQuery cdfQuery;
		try {
			cdfQuery = getCDFQueryByScanGid(scanGid);
		}
		catch(Exception e) {
			return new FormResponseMap(false, e.getMessage());
		}
		
		MapXY10UniqueCategory<?> mapXY10 = CategoryUtils.getFirstCategory(cdfQuery.getCategories(), MapXY10);
		if(mapXY10 == null) {
			return new FormResponseMap(false, "CDF file does not contain MapXY data.");
		}
		
		String[] cdfVarNames = new String[] { mapXY10.J() };
		
		List<CDFRecord> cdfRecords;
		try {
			cdfRecords = cdfQuery.queryRecordsByNames(cdfVarNames);
		}
		catch(CDFQueryException e) {
			String msg = "Exception while executing CDF query. (Scan:" + scanGid + ")";
			FormResponseMap response = new FormResponseMap(false, msg);
			logger.warn(msg, e);
			return response;
		}
		
		if(cdfRecords.isEmpty()) {
			String msg = "CDF query result does not contain any records. (Scan:" + scanGid + ")";
			FormResponseMap response = new FormResponseMap(false, msg);
			logger.warn(msg);
			return response;
		}
		
		Set<Integer> indexSet = new HashSet<Integer>(cdfRecords.size());
		
		try {
			for(CDFRecord cdfRecord : cdfRecords) {
				indexSet.add(cdfRecord.getIntByName(mapXY10.J()));
			}
		}
		catch(RecordFormatException e) {
			String msg = "Exception while reading CDF query result. (Scan:" + scanGid + ")";
			FormResponseMap response = new FormResponseMap(false, msg);
			logger.warn(msg, e);
			return response;
		}
		
		List<Integer> indexList = new ArrayList<Integer>(indexSet);
		Collections.sort(indexList);
		
		Collection<int[]> indexes = new ArrayList<int[]>(indexList.size());
		for(int index : indexList) {
			indexes.add(new int[] { index });
		}
		
		FormResponseMap response = new FormResponseMap(true);
		response.put("J", indexes);
		return response;
	}
}
