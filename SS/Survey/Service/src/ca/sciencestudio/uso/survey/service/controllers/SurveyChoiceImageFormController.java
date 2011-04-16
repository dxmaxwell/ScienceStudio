package ca.sciencestudio.uso.survey.service.controllers;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import ca.sciencestudio.uso.survey.model.SurveyChoice;
import ca.sciencestudio.uso.survey.model.dao.SurveyChoiceDAO;

public class SurveyChoiceImageFormController extends SimpleFormController {
	
	private SurveyChoiceDAO surveyChoiceDAO;
	private String directoryPath;
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		
		MultipartHttpServletRequest multipartReq = (MultipartHttpServletRequest) request;
		
		MultipartFile file = multipartReq.getFile("file");
		
		InputStream is = null;
		OutputStream os = null;
		
		if (file.getSize() > 0) {
			is = file.getInputStream();
			os = new FileOutputStream(directoryPath + file.getOriginalFilename().replace(" ", "_"));
			
			int readBytes = 0;
			byte[] buffer = new byte[8192];
			
			while ((readBytes = is.read(buffer, 0 , 8192))!=-1){
				os.write(buffer, 0, readBytes);
			}
			
			os.close();
			is.close();
		}
		
		String surveyChoiceIdParam = request.getParameter("id");
		int surveyChoiceId = 0;
		try {
			if (surveyChoiceIdParam != null) {
				surveyChoiceId = Integer.parseInt(surveyChoiceIdParam);
			}
		} catch (Exception e) {
			surveyChoiceId = 0;
		}
		
		SurveyChoice surveyChoice = surveyChoiceDAO.getChoiceById(surveyChoiceId);
		int surveyQuestionId = surveyChoice.getQuestionId();
		return new ModelAndView("redirect:surveyChoice?surveyChoiceId=" + surveyChoiceId + "&surveyQuestionId=" + surveyQuestionId + "&activeTab=image");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String choiceIdStr = request.getParameter("surveyChoiceId");
		int choiceId = 0;
		
		try {
			if (choiceIdStr != null) {
				choiceId = Integer.parseInt(choiceIdStr);
			}
		} catch (Exception e) {
			choiceId = 0;
		}
		
		SurveyChoice surveyChoice = null;
		
		if (choiceId != 0) {
			surveyChoice = surveyChoiceDAO.getChoiceById(choiceId);
		}
		
		return surveyChoice;
	}
	
	protected Map<Object,Object> referenceData(HttpServletRequest request) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		ServletContext ctx = request.getSession().getServletContext();
		Resource resource = new FileSystemResource(ctx.getRealPath(directoryPath));
		String[] files = resource.getFile().list();
		
		List<String> filenames = new ArrayList<String>();
		
		filenames.add("");
		for (String s : files) {
			if (s.endsWith(".jpg") || s.endsWith(".gif") || s.endsWith(".bmp") || s.endsWith(".png") || s.endsWith(".tif"))
				filenames.add(s);
		}
		
		Collections.sort(filenames, String.CASE_INSENSITIVE_ORDER);
		
		map.put("filenames", filenames);
		
		map.put("directoryPath", directoryPath);
		
		return map;
	}

	public void setSurveyChoiceDAO(SurveyChoiceDAO surveyChoiceDAO) {
		this.surveyChoiceDAO = surveyChoiceDAO;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
}
