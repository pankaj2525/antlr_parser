import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigurationUtils {
	public static final Properties configProperties = new Properties();
	public static final String GRAMMAR_FOLDER = "antlr.grammar.folder";
	public static final String INPUT_FOLDER = "antlr.input.folder";
	public static final String OUTPUT_FOLDER = "antlr.output.folder";
	public static final String GRAMMAR_RULE_NAME = "antlr.rule.name";
	public static final String GRAMMAR_NAME = "antlr.grammar.name";

	/**
	 * This operation reads input file for configuration parameters and creates
	 * a configuration
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static final void loadConfiguration(String[] args) throws Exception {
		String file = null;

		for (int i = 0; i < (args.length - 1); i++) {
			if ("--configpath".equals(args[i])) {
				if (args[i + 1] != null && new File(args[i + 1].trim()).isFile()) {
					file = args[i + 1];

				}
				break;
			}
		}
		if (file == null || file.trim() == "") {

			throw new Exception(
					"specify --configpath as command line argument followed by path to configuration file..\n e.g --configpath /home/filename.properties");
		}
		InputStream inputStream = null;

		try {

			inputStream = new FileInputStream(file);

			// load a properties file
			configProperties.load(inputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * This operation returns configuration for key passed
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getProperty(String key) throws Exception {
		String value = configProperties.getProperty(key);
		if (value == null || value.trim() == "") {
			throw new Exception("key : " + key + " not found");
		}
		return value;
	}
}
