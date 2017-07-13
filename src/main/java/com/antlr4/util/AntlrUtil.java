package com.antlr4.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.antlr4.config.ConfigurationUtils;
import com.antlr4.demo.GrammarParser;




public final class AntlrUtil {
	protected static String JAR_PATH;
	static {
		try {
			JAR_PATH = "/home/synerzip/workspace/antlr_parser/antlr_parser/target/classes:/usr/local/lib/antlr-4.7-complete.jar";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getJarPath() throws Exception {
		return getJarFile().toString();
	}

	private static File getJarFile() throws FileNotFoundException {

		String path = GrammarParser.class.getResource(GrammarParser.class.getSimpleName() + ".class").getFile();
		System.out.println("getJarFile path :" + path);
		if (path.startsWith("/")) {
			throw new FileNotFoundException("This is not a jar file: \n" + path);
		}
		path = path.substring(path.indexOf(":") + 1, path.lastIndexOf('!'));
		System.out.println("path : " + path);
		return new File(path);
	}

	/**
	 * This operation generates java files from the grammar files
	 * 
	 * @throws Exception
	 */
public static final String TEMP_FILE = "temp.txt";
	/**
	 * This operation gernates output in form of tree give a input file
	 * 
	 * @throws Exception
	 */
	public static void generateOuput() throws Exception {
		for (int j = 0 ; j < ConfigurationUtils.myPojo.getGrammars().length;j++ )
		{
		try {
			List<String> fileNames = getInputFiles(ConfigurationUtils.myPojo.getGrammars()[j].getProperties().getInputFolder());
			String ruleName = ConfigurationUtils.myPojo.getGrammars()[j].getProperties().getStartRule();
			String grammarName = ConfigurationUtils.myPojo.getGrammars()[j].getName();
			List<String> commandListArgs = new ArrayList<String>();
			PrintWriter outputWriter = null;
			BufferedReader inputBufferedReader = null;
			for (int i = 0; i < fileNames.size(); i++) {
				outputWriter = new PrintWriter(ConfigurationUtils.myPojo.getGrammars()[j].getProperties().getOuputFolder() + File.separator +fileNames.get(i) + "_output.txt");
				outputWriter.print("");
				outputWriter.close();
				Process proc = null;
				Runtime rt = null;
				
				InputStream inputStream = null;
				InputStreamReader inputStreamReader = null;
				BufferedReader bufferedReader = null;
				
				inputBufferedReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(ConfigurationUtils.myPojo.getGrammars()[j].getProperties().getInputFolder() + File.separator + fileNames.get(i))));
				String inputLine;
				Boolean validInput = true;
				String errorString = null;
				while ((inputLine = inputBufferedReader.readLine()) != null) {
					rt = Runtime.getRuntime();
					PrintWriter tempFile = new PrintWriter(TEMP_FILE);
					tempFile.print("");
					tempFile.print(inputLine);
					tempFile.close();
					commandListArgs = new ArrayList<String>();
					commandListArgs.add("java");
					commandListArgs.add("-cp");
					commandListArgs.add(JAR_PATH);
					commandListArgs.add("org.antlr.v4.gui.TestRig");
					commandListArgs.add(grammarName);
					commandListArgs.add(ruleName);
					commandListArgs.add(TEMP_FILE);
					commandListArgs.add("-tree");
					proc = rt.exec(commandListArgs.toArray(new String[commandListArgs.size()]));
					int exitVal = proc.waitFor();
					System.out.println("Process exitValue: " + exitVal);
					int len;
					validInput = true;
					if ((len = proc.getErrorStream().available()) > 0) {
						byte[] buf = new byte[len];
						proc.getErrorStream().read(buf);
						validInput = false;
						errorString = new String(buf);
						System.err.println("Command error:\t\"" + new String(buf) + "\"");
					}
					inputStream = proc.getInputStream();
					inputStreamReader = new InputStreamReader(inputStream);
					bufferedReader = new BufferedReader(inputStreamReader);

					String outputLine;
					while ((outputLine = bufferedReader.readLine()) != null) {
						writeOutput(inputLine,outputLine, fileNames.get(i),validInput,errorString,j);
					}
					bufferedReader.close();
					inputStreamReader.close();
					inputStream.close();
				}

				inputBufferedReader.close();
			}

		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
		}
	}

	private static void writeOutput(String inputLine,String outputLine, String fileName,Boolean validInput,String errorString,int j) {

		try {
			Files.write(Paths.get(ConfigurationUtils.myPojo.getGrammars()[j].getProperties().getOuputFolder() + File.separator + fileName + "_output.txt"),
					("Input : " + inputLine + "\n").getBytes(), StandardOpenOption.APPEND);
			Files.write(Paths.get(ConfigurationUtils.myPojo.getGrammars()[j].getProperties().getOuputFolder() + File.separator + fileName + "_output.txt"),
					("Valid Input : " + validInput + "\n").getBytes(), StandardOpenOption.APPEND);
			if(!validInput){
				Files.write(Paths.get(ConfigurationUtils.myPojo.getGrammars()[j].getProperties().getOuputFolder() + File.separator + fileName + "_output.txt"),
						("Output : " + errorString + "\n\n").getBytes(), StandardOpenOption.APPEND);
			}else{
			Files.write(Paths.get(ConfigurationUtils.myPojo.getGrammars()[j].getProperties().getOuputFolder() + File.separator + fileName + "_output.txt"),
					("Output : " + outputLine + "\n\n\n").getBytes(), StandardOpenOption.APPEND);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("output: " + outputLine + "\n\n");

	}

	private static List<String> getInputFiles(String sDir) {
		ArrayList<String> fileNames = new ArrayList<String>();
		try {
			Files.find(Paths.get(sDir), 999, (p, bfa) -> bfa.isRegularFile()).forEach(f -> {
				if (f.getFileName().toString().endsWith(".txt")) {
					String fileName = f.getFileName().toString();
					fileNames.add(fileName);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileNames;
	}

	/**
	 * This operation compiles the java files and stores the output in grammar
	 * file location
	 * 
	 * @throws Exception
	 */

}
