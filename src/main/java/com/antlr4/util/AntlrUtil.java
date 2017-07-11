package com.antlr4.util;

import static com.antlr4.config.ConfigurationUtils.GRAMMAR_FOLDER;
import static com.antlr4.config.ConfigurationUtils.GRAMMAR_NAME;
import static com.antlr4.config.ConfigurationUtils.GRAMMAR_RULE_NAME;
import static com.antlr4.config.ConfigurationUtils.INPUT_FOLDER;
import static com.antlr4.config.ConfigurationUtils.OUTPUT_FOLDER;
import static com.antlr4.config.ConfigurationUtils.getProperty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.Tool;
import org.antlr.v4.tool.ErrorType;

import com.antlr4.demo.GrammarParser;




public final class AntlrUtil {
	protected static String JAR_PATH;
	static {
		try {
			JAR_PATH = getJarPath();
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
	public static void generateJavaFiles() throws Exception {
		String[] toolInputArgs = getToolInputArgs();
		Tool antlr = new Tool(toolInputArgs);
		if (toolInputArgs.length == 0) {
			antlr.help();
			antlr.exit(0);
		}

		try {
			antlr.processGrammarsOnCommandLine();
		} finally {
			if (antlr.log) {
				try {
					String logname = antlr.logMgr.save();
					System.out.println("wrote " + logname);
				} catch (IOException ioe) {
					antlr.errMgr.toolError(ErrorType.INTERNAL_ERROR, ioe);
				}
			}
		}
	}
public static final String TEMP_FILE = "temp.txt";
	/**
	 * This operation gernates output in form of tree give a input file
	 * 
	 * @throws Exception
	 */
	public static void generateOuput() throws Exception {
		
		try {
			List<String> fileNames = getInputFiles(getProperty(INPUT_FOLDER));
			String ruleName = getProperty(GRAMMAR_RULE_NAME);
			String grammarName = getProperty(GRAMMAR_NAME);
			List<String> commandListArgs = new ArrayList<String>();
			PrintWriter outputWriter = null;
			BufferedReader inputBufferedReader = null;
			for (int i = 0; i < fileNames.size(); i++) {
				outputWriter = new PrintWriter(getProperty(OUTPUT_FOLDER) + File.separator +fileNames.get(i) + "_output.txt");
				outputWriter.print("");
				outputWriter.close();
				Process proc = null;
				Runtime rt = null;
				
				InputStream inputStream = null;
				InputStreamReader inputStreamReader = null;
				BufferedReader bufferedReader = null;
				
				inputBufferedReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(getProperty(INPUT_FOLDER) + File.separator + fileNames.get(i))));
				String line1;
				while ((line1 = inputBufferedReader.readLine()) != null) {
					rt = Runtime.getRuntime();
					PrintWriter tempFile = new PrintWriter(TEMP_FILE);
					tempFile.print("");
					tempFile.print(line1);
					tempFile.close();
					commandListArgs = new ArrayList<String>();
					commandListArgs.add("java");
					commandListArgs.add("-cp");
					commandListArgs.add(JAR_PATH + File.pathSeparator + getProperty(GRAMMAR_FOLDER) + File.separator);
					commandListArgs.add("org.antlr.v4.gui.TestRig");
					commandListArgs.add(grammarName);
					commandListArgs.add(ruleName);
					commandListArgs.add(TEMP_FILE);
					commandListArgs.add("-tree");
					proc = rt.exec(commandListArgs.toArray(new String[commandListArgs.size()]));
					int exitVal = proc.waitFor();
					System.out.println("Process exitValue: " + exitVal);
					int len;
					if ((len = proc.getErrorStream().available()) > 0) {
						byte[] buf = new byte[len];
						proc.getErrorStream().read(buf);
						System.err.println("Command error:\t\"" + new String(buf) + "\"");
					}
					inputStream = proc.getInputStream();
					inputStreamReader = new InputStreamReader(inputStream);
					bufferedReader = new BufferedReader(inputStreamReader);

					String line;
					while ((line = bufferedReader.readLine()) != null) {
						writeOutput(line, fileNames.get(i));
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

	private static void writeOutput(String line, String fileName) {

		try {
			Files.write(Paths.get(getProperty(OUTPUT_FOLDER) + File.separator +fileName + "_output.txt"), ("Output : " + line + "\n\n\n").getBytes(),
					StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("output: " + line + "\n\n");

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
	public static void compileJavaFiles() throws Exception {
		List<String> javaFiles = getJavaFilesForCompilation();
		try {
			Runtime rt = Runtime.getRuntime();
			List<String> cmd = new ArrayList<String>();
			cmd.add("javac");
			cmd.add("-cp");
			cmd.add(JAR_PATH);
			for (String file : javaFiles) {
				cmd.add(file.trim());

			}
			System.out.println("compileJavaFiles : "+cmd);
			Process proc = rt.exec(cmd.toArray(new String[cmd.size()]));
			int exitVal = proc.waitFor();
			System.out.println("Process exitValue: " + exitVal);
			int len;
			if ((len = proc.getErrorStream().available()) > 0) {
				byte[] buf = new byte[len];
				proc.getErrorStream().read(buf);
				System.err.println("Command error:\t\"" + new String(buf) + "\"");
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}

	}

	private static List<String> getJavaFilesForCompilation() throws Exception {
		List<String> javaFiles = new ArrayList<String>();
		Files.find(Paths.get(getProperty(GRAMMAR_FOLDER)), 999, (p, bfa) -> bfa.isRegularFile()).forEach(f -> {

			String path = f.toAbsolutePath().toString();

			String fileName = f.getFileName().toString();
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			if ("java".equals(extension)) {
				System.out.println("java file : " + path);
				javaFiles.add(path);
			}
		});
		return javaFiles;
	}

	private static String[] getToolInputArgs() throws Exception {
		String grammarPath = getProperty(GRAMMAR_FOLDER);
		List<String> arguments = new ArrayList<>();
		arguments.add("-o");
		arguments.add(grammarPath);

		Files.find(Paths.get(grammarPath), 999, (p, bfa) -> bfa.isRegularFile()).forEach(f -> {

			String path = f.toAbsolutePath().toString();
			System.out.println(path);
			String fileName = f.getFileName().toString();
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			if ("g4".equals(extension)) {
				arguments.add(path);
			}
		});
		int argLength = 0;
		String[] input = new String[arguments.size()];
		for (String item : arguments) {
			input[argLength++] = item;
		}

		return input;
	}
}
