package com.antlr4.config;

import java.io.File;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.antlr4.demo.Pojo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public final class ConfigurationUtils {
	public static Pojo myPojo;

	/**
	 * This operation reads input file for configuration parameters and creates
	 * a configuration
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static final void loadConfiguration(String[] args) throws Exception {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			String fileName = "config/config.yaml";
			ClassLoader classLoader = new Pojo().getClass().getClassLoader();
			File file = new File(classLoader.getResource(fileName).getFile());
			myPojo = mapper.readValue(file, Pojo.class);
			System.out.println(myPojo.getGrammars()[0].getName());
			System.out.println(ReflectionToStringBuilder.toString(myPojo, ToStringStyle.MULTI_LINE_STYLE));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
