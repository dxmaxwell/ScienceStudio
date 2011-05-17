package ca.sciencestudio.rest.service.test;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public abstract class ModelDaoTest {

	protected static final String GID_FACILITY = "CLSI";
	
	protected static final String BASE_URL = "http://localhost:8080/ssrest/model";	
	
	protected static final List<HttpMessageConverter<?>> MESSAGE_CONVERTERS = new ArrayList<HttpMessageConverter<?>>();
	
	protected static final RestTemplate REST_TEMPLATE = new RestTemplate();
	
	static {
		MESSAGE_CONVERTERS.add(new MappingJacksonHttpMessageConverter());
		REST_TEMPLATE.setMessageConverters(MESSAGE_CONVERTERS);
	}
}
