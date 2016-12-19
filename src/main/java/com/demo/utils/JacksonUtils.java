package com.demo.utils;

import java.io.IOException;
import java.io.StringWriter;

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
		
	}
}
