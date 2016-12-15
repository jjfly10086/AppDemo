package com.demo.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.ModelMap;

import com.demo.bean.UserBean;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtils {
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * objectתjson
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String parseObj2Json(Object obj) throws IOException{
		StringWriter sw = new StringWriter();
		JsonGenerator generator = new JsonFactory().createGenerator(sw);
		objectMapper.writeValue(generator, obj);
		generator.close();
		return sw.toString();
	}

	/**
	 * jsonתobject
	 * @param json
	 * @param cls
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object parseJson2Obj(String json,Class cls) throws JsonParseException, JsonMappingException, IOException{
		Object obj = objectMapper.readValue(json, cls);
		return obj;
	}
	public static void main(String[] args) throws IOException {
		ModelMap model = new ModelMap();
		model.addAttribute("ssss", "ssss");
		List<UserBean> list = new ArrayList<UserBean>();
		UserBean user = new UserBean();
		user.setId("1");
		user.setUserName("aaa");
		user.setUserPass("1232");
		list.add(user);
		model.addAttribute("list", list);
		System.out.println(parseObj2Json(model));
	}
}
