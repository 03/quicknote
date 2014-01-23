package com.wind.quicknote.helper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

public class MappingJacksonJsonpView extends MappingJacksonJsonView {

	/**
	 * Default content type. Override as a bean property.
	 */
	public static final String DEFAULT_CONTENT_TYPE = "application/javascript";

    @Override
    public String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    @Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if("GET".equals(request.getMethod().toUpperCase())) {
            @SuppressWarnings("unchecked")
            Map<String, String[]> params = request.getParameterMap();

            if(params.containsKey("jsoncallback")) {
                response.getOutputStream().write(new String(params.get("jsoncallback")[0] + "(").getBytes());
                super.render(model, request, response);
                response.getOutputStream().write(new String(");").getBytes());
                response.setContentType("application/javascript");
            }

            else {
                super.render(model, request, response);
            }
        }

        else {
            super.render(model, request, response);
        }
    }
    
	public String convertToJsonString(Object objects)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Writer strWriter = new StringWriter();
		mapper.writeValue(strWriter, objects);
		return strWriter.toString();
	}
    
}