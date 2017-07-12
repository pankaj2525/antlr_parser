package com.antlr4.demo;

public class Properties {
	private String inputFolder;

	private String ouputFolder;

	public String getGrammarFolder() {
		return grammarFolder;
	}

	public void setGrammarFolder(String grammerFolder) {
		this.grammarFolder = grammerFolder;
	}

	private String startRule;
	
	private String grammarFolder;

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public String getOuputFolder() {
		return ouputFolder;
	}

	public void setOuputFolder(String ouputFolder) {
		this.ouputFolder = ouputFolder;
	}

	public String getStartRule() {
		return startRule;
	}

	public void setStartRule(String startRule) {
		this.startRule = startRule;
	}

	@Override
	public String toString() {
		return "ClassPojo [inputFolder = " + inputFolder + ", ouputFolder = " + ouputFolder + ", startRule = "
				+ startRule + "]";
	}
}
