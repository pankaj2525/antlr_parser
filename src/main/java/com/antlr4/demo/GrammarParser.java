package com.antlr4.demo;


import static com.antlr4.config.ConfigurationUtils.loadConfiguration;
import static com.antlr4.util.AntlrUtil.compileJavaFiles;
import static com.antlr4.util.AntlrUtil.generateJavaFiles;
import static com.antlr4.util.AntlrUtil.generateOuput;

public class GrammarParser {
	public static void main(String[] args) throws Exception {
		loadConfiguration(args);
		generateJavaFiles();
		compileJavaFiles();
		generateOuput();
	}

}
