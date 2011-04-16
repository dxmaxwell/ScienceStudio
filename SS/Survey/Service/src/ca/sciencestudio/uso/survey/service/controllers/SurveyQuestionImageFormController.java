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

import ca.sciencestudio.uso.survey.model.SurveyQuestion;
import ca.sciencestudio.uso.survey.model.dao.SurveyQuestionDAO;


public class SurveyQuestionImageFormController extends SimpleFormController {
	
	private SurveyQuestionDAO surveyQuestionDAO;
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
		
		String surveyQuestionIdParam = request.getParameter("id");
		int surveyQuestionId = 0;
		try {
			if (surveyQuestionIdParam != null) {
				surveyQuestionId = Integer.parseInt(surveyQuestionIdParam);
			}
		} catch (Exception e) {
			surveyQuestionId = 0;
		}
		
		SurveyQuestion surveyQuestion = surveyQuestionDAO.getQuestionById(surveyQuestionId);
		int surveySectionId = surveyQuestion.getSectionId();
		return new ModelAndView("redirect:surveyQuestion?surveyQuestionId=" + surveyQuestionId + "&surveySectionId=" + surveySectionId + "&activeTab=image");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String questionIdStr = request.getParameter("surveyQuestionId");
		int questionId = 0;
		
		try {
			if (questionIdStr != null) {
				questionId = Integer.parseInt(questionIdStr);
			}
		} catch (Exception e) {
			questionId = 0;
		}
		
		SurveyQuestion surveyQuestion = null;
		
		if (questionId != 0) {
			surveyQuestion = surveyQuestionDAO.getQuestionById(questionId);
		}
		
		return surveyQuestion;
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

	public void setSurveyQuestionDAO(SurveyQuestionDAO surveyQuestionDAO) {
		this.surveyQuestionDAO = surveyQuestionDAO;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
}
