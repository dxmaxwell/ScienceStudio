package ca.sciencestudio.rest.service.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.Scan;
import ca.sciencestudio.model.dao.ScanDAO;
import ca.sciencestudio.model.dao.rest.RestScanDAO;
import ca.sciencestudio.model.validators.ScanValidator;

public abstract class ScanDaoTest extends ModelDaoTest {

	private static final DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	public static void main(String[] args) throws Exception {

		RestScanDAO restScanDAO = new RestScanDAO();
		restScanDAO.setValidator(new ScanValidator());
		restScanDAO.setRestTemplate(REST_TEMPLATE);
		restScanDAO.setBaseUrl(BASE_URL);
		
		ScanDAO scanDAO = (ScanDAO)restScanDAO;
		
		List<Scan> scans = scanDAO.getAll();
		for(Scan s : scans) {
			System.out.println(s);
		}
		System.out.println();
		
		Scan scan = new Scan();
		scan.setName("Rest Test Scan");
		scan.setDataUrl("/mnt/scstudio/test/scan1");
		scan.setParameters("key1=value1");
		scan.setStartDate(DATE_FORMATER.parse("2011-03-17 08:15:24"));
		scan.setEndDate(DATE_FORMATER.parse("2011-04-21 16:23:45"));
		scan.setExperimentId(2);
		Errors errors = scanDAO.add(scan);
		
		if(errors.hasErrors()) {
			System.out.println("Errors while adding Scan: " + errors);
			return;
		}
		
		System.out.println("Add Scan: " + scan);
		
		Scan scan2 = scanDAO.get(scan.getGid());
		if(!scan.equals(scan2)) {
			System.out.println("Error while getting Scan with GID: " + scan.getGid());
			return;
		}
		
		System.out.println("Get Project: " + scan);

		scan.setName("Rest Test Scan (Updated)");
		scan.setDataUrl("/mnt/scstudio/beta/scan1");
		scan.setParameters("key1=value2");
		scan.setStartDate(DATE_FORMATER.parse("2011-02-15 08:29:14"));
		scan.setEndDate(DATE_FORMATER.parse("2011-05-02 12:26:19"));
		scan.setExperimentId(3);
		errors = scanDAO.edit(scan);
		
		if(errors.hasErrors()) {
			System.out.println("Errors while editting Scan: " + errors);
			return;
		}
	
		System.out.println("Edit Scan: " + scan);
		
		scan2 = scanDAO.get(scan.getGid());
		if(!scan.equals(scan2)) {
			System.out.println("Error while getting Scan with GID: " + scan.getGid());
			return;
		}
		
		System.out.println("Get Project (again): " + scan);
		
		boolean success = scanDAO.remove(scan.getGid());
		if(!success) {
			System.out.println("Error while removing Scan with GID: " + scan.getGid());
			return;
		}
		System.out.println();
		
		scans = scanDAO.getAll();
		for(Scan s : scans) {
			System.out.println(s);
		}
	}

}
