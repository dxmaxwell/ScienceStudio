/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanDataMCAController class.
 *
 */
package ca.sciencestudio.data.service.controllers;

import gsfc.nssdc.cdf.Variable;

import java.util.List;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.data.cdf.CDFQuery;
import ca.sciencestudio.data.cdf.CDFRecord;
import ca.sciencestudio.data.cdf.selector.CDFSelector;
import ca.sciencestudio.data.cdf.selector.CDFSelectorBuilder;
import ca.sciencestudio.data.service.controllers.AbstractScanDataController;

import ca.sciencestudio.data.standard.category.MCA10UniqueCategory;
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
public class ScanDataMCAController extends AbstractScanDataController {

	public static final double DEFAULT_MIN_ENERGY = 0.0;
	public static final double DEFAULT_MAX_ENERGY = 1.0;

	@ResponseBody
	@RequestMapping("/scan/{scanGid}/data/mca/spectrum*")
	public FormResponseMap getSpectrum(@RequestParam String I, @RequestParam String J, @PathVariable String scanGid) {
		
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

		MCA10UniqueCategory<?> mca10 = CategoryUtils.getFirstCategory(cdfQuery.getCategories(), MCA10);
		if(mca10 == null) {
			return new FormResponseMap(false, "CDF file does not contain MCA data.");
		}
		
		CDFSelector iSelector;
		try {
			iSelector = CDFSelectorBuilder.EQ(mapXY10.I(), Integer.parseInt(I));
		}
		catch(NumberFormatException e) {
			return new FormResponseMap(false, "Scan data point index invalid (I).");
		}
		
		CDFSelector jSelector;
		try {
			jSelector = CDFSelectorBuilder.EQ(mapXY10.J(), Integer.parseInt(J));
		}
		catch(NumberFormatException e) {
			return new FormResponseMap(false, "Scan data point index invalid (J).");
		}
		
		Variable mca10Spectrum = null;
		try {
			mca10Spectrum = cdfQuery.getVariableByName(mca10.SumSpectrum());
		}
		catch(CDFQueryException e) {
			try {
				mca10Spectrum = cdfQuery.getVariableByName(mca10.Spectrum(1));
			}
			catch(CDFQueryException ex) {
				String msg = "Scan data CDF file does not contain a spectrum (Scan: " + scanGid + ").";
				FormResponseMap response = new FormResponseMap(false, msg);
				logger.warn(msg, e);
				return response;
			}
		}
		
		List<CDFRecord> cdfRecords;
		try {
			String[] cdfVarNames = new String[] { mapXY10.I(), mapXY10.J(), mca10Spectrum.getName() };
			cdfRecords = cdfQuery.queryRecordsByNames(cdfVarNames, CDFSelectorBuilder.AND(iSelector, jSelector));
		}
		catch(CDFQueryException e) {
			String msg = "Exception while executing CDF query. (ScanGid:" + scanGid + ")";
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
	
		int[] spectrum;
		try {
			spectrum = cdfRecords.get(0).getIntArrayByName(mca10Spectrum.getName());
		}
		catch(RecordFormatException e) {
			String msg = "CDF query result invalid record format. (Scan:" + scanGid + ")";
			FormResponseMap response = new FormResponseMap(false, msg);
			logger.warn(msg, e);
			return response;
		}
		
		double maxEnergy;
		try {
			maxEnergy = cdfQuery.getAttributeRecordByName(mca10.MaxEnergy()).getDoubleByID(0);
		}
		catch(Exception e) {
			maxEnergy = DEFAULT_MAX_ENERGY;
		}
		
		double minEnergy;
		try {
			minEnergy = cdfQuery.getAttributeRecordByName(mca10.MinEnergy()).getDoubleByID(0);
		}
		catch(Exception e) {
			minEnergy = DEFAULT_MIN_ENERGY;
		}
		
		FormResponseMap response = new FormResponseMap(true);
		response.put("spectrum", spectrum);
		response.put("minEnergy", minEnergy);
		response.put("maxEnergy", maxEnergy);
		return response;
	}
}
