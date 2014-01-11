package edu.brandeis.cs.python.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @brief Invoke Python file and run with input and return output.
 * @author shicq@cs.brandeis.edu
 *
 */
public class PythonRunner {
	
	public static final String CONF_FILE = "lapps.conf";
	
	
	public static final String ESCAPE_MARK_QUOTATION = "\"";
	public static final String ESCAPE_MARK_SINGLE_QUOTATION = "'";	
	
	
	// the name of default section
	public static final String CONF_SECTION_DEFAULT = "default";
	// set the pickle saving/loading directory
	public static final String CONF_PICKLE_PATH = "PickleHome";
	// set the interface Python cmd function
	public static final String CONF_PYTHON_FILE = "PythonInterface";
	
	// load pickle file into memory.
	public static final String CONF_PYTHON_SECTION_LOADS = "Loads";
	// set global variable need in the function
	public static final String CONF_PYTHON_SECTION_GLOBALS = "Globals";
	// running required sections before this function
	public static final String CONF_PYTHON_SECTION_REQUIRES = "Requires";
	// the Python file has the target function
	public static final String CONF_PYTHON_SECTION_FILE = "PythonFile";
	// the target function name
	public static final String CONF_PYTHON_SECTION_FUNC = "Method";
	// the arguments need for this function
	public static final String CONF_PYTHON_SECTION_ARGS = "Args";
	// the return reference
	public static final String CONF_PYTHON_SECTION_RETURN = "Return";
	// dump needed data into pickle file.
	public static final String CONF_PYTHON_SECTION_DUMPS = "Dumps";

	public static final String CONF_PYTHON_SECTION_GLOBALS_SEPARATOR = ",";
	public static final String CONF_PYTHON_SECTION_GLOBALS_SETATTR_SEPARATOR = ":";
	public static final String CONF_PYTHON_SECTION_REQUIRES_SEPARATOR = ",";
	public static final String CONF_PYTHON_SECTION_LOADS_SEPARATOR = ",";
	public static final String CONF_PYTHON_SECTION_LOADS_PICKLE_SEPARATOR = ":";
	
	public static final String NULL = "";
	
	protected static final Logger logger = LoggerFactory
			.getLogger(PythonRunner.class);
	
	private String confPath = null;
	
	public PythonRunner() throws PythonRunnerException {
		this(CONF_FILE);
	}
	
	public PythonRunner(String confPath) throws PythonRunnerException {
		logger.info("PythonRunner(): Creating PythonRunner ...");
		init(confPath);
		logger.info("PythonRunner(): Creating PythonRunner !");
	}

//	private Properties prop = new Properties();
	
	private JIniFile prop = null;


	protected void init(String confPath) throws PythonRunnerException {
		this.confPath = confPath;
		File file = FileLoadUtil.locate(this.confPath);
		if (file == null) {
			logger.error("init(): fail to open \"" + this.confPath + "\".");
			throw new PythonRunnerException("init(): fail to open \""
					+ this.confPath + "\".");
		}

		try {
			prop = new JIniFile(file);
		} catch (Exception e) {
			logger.error("init(): fail to load \"" + this.confPath + "\".");
			throw new PythonRunnerException("init(): fail to load \""
					+ this.confPath + "\".");
		}

		logger.info("init(): load " + this.confPath + ".");
	}
	
	/**
	 * 
	 * @param pythonFile python file
	 * @param params 
	 * @see runPython(String pythonFile, String params)
	 * @return
	 */
	public static String runPython(String pythonFile, String params) throws PythonRunnerException {
		return runPython(pythonFile, new String [] {params});		
	}
	
	
	/**
	 * @brief run python file and return result throw IOException.
	 * @param pythonFile 
	 * @param parameter array
	 * @return
	 */
	public static String runPython(String pythonFile, String ... arrParams)  throws PythonRunnerException  {
		StringBuffer result = new StringBuffer();
        String[] callAndArgs = new String[arrParams.length + 2];
        callAndArgs[0] = "python";
        callAndArgs[1] = pythonFile;
        System.out.print("python "+ pythonFile);
        for (int i = 0; i < arrParams.length; i++) {
        	callAndArgs[2 + i] = arrParams[i];
        	System.out.print(" " + escapePyParam(arrParams[i]));
        }        
        System.out.println();
        
        try{
	        Process p = Runtime.getRuntime().exec(callAndArgs);
	        BufferedReader stdInput =
	            new BufferedReader(new InputStreamReader(p.getInputStream()));
	        BufferedReader stdError =
	            new BufferedReader(new InputStreamReader(p.getErrorStream()));        
	        String st = null;
	        while ((st = stdInput.readLine()) != null) {
	            result.append(st);
	        }
	        while ((st = stdError.readLine()) != null) {
	            System.err.println(st);            
	        }       
	        p.waitFor();
	        p.destroy();
        }catch(Exception e){
        	e.printStackTrace();
        	throw new PythonRunnerException(e);
        }
        // RETURN THE RESULT RATHER THAN PRINTING IT
        System.out.println("runPython=" + result.toString());
        return result.toString();
	}
	
//	/**
//	 * Run Python section, with string arguments 
//	 * @param args
//	 * @return
//	 * @throws PythonRunnerException
//	 */
//	public String runPythonSection(String [] args) throws PythonRunnerException {
//		File confFile = FileLoadUtil.locate(confPath);
//		try {
//			if (FileLoadUtil.needUpdate(confFile)){
//				init(confFile.getAbsolutePath());
//			}
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			throw new PythonRunnerException(e);
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new PythonRunnerException(e);
//		}
//		
//		String pythonFileConf = getDefProperty(CONF_PYTHON_FILE);		
//		String pythonFile = FileLoadUtil.locate(pythonFileConf).getAbsolutePath();
//		return runPython(pythonFile, args);
//	}
	
	public String getDefProperty(String key){
		return getSectionProperty(CONF_SECTION_DEFAULT, key);
	}
	
	public String getSectionProperty(String section, String key){		
		String value = prop.ReadString(section, key, "");
//		System.out.println("section="+section+" key="+key+" value="+value);
		return value;
	}

	
	public String runPythonSection(String section, String ... arrParams) throws PythonRunnerException {
		File confFile = FileLoadUtil.locate(confPath);
		try {
			if (FileLoadUtil.needUpdate(confFile)){
				init(confFile.getAbsolutePath());
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// read target python file
		String module = getSectionProperty(section, CONF_PYTHON_SECTION_FILE);
		module = FileLoadUtil.locate(module).getAbsolutePath();
		
		// read parameter configurations
		String args = getSectionProperty(section, CONF_PYTHON_SECTION_ARGS);
		
		// read python command interface
		String pythonFileConf = getDefProperty(CONF_PYTHON_FILE);		
		String pythonFile = FileLoadUtil.locate(pythonFileConf).getAbsolutePath();
		
		
//		System.out.println("runPythonSection():module=" + module);
		
		// parameters replacements.
		for(int i = 0; i < arrParams.length; i++){
			args = args.replace("%%"+(i+1), escapePyParam(arrParams[i]));
			args = args.replace("&&"+(i+1), arrParams[i]);
		}
		
		return runPython(pythonFile, "-i", section, module, args);
	}
	
	
	public String runPythonPyro4Holder() throws PythonRunnerException{
		String pythonFileConf = getDefProperty(CONF_PYTHON_FILE);		
		String pythonFile = FileLoadUtil.locate(pythonFileConf).getAbsolutePath();
		return runPython(pythonFile, "-o");
	}
	
	public Object runPythonSectionPyro4(String section, Object ... arrParams) throws PythonRunnerException {
		// provide function running sockets key
		String key = section + ":" + System.currentTimeMillis();
		try {
			// create a new connection for pyro4holder.
			Pyro4Holder holder = new Pyro4Holder();
			// push all the parameters into sockets 
			holder.put(key, arrParams);
			String pythonFileConf = getDefProperty(CONF_PYTHON_FILE);		
			String pythonFile = FileLoadUtil.locate(pythonFileConf).getAbsolutePath();
			// runPythonSection
			runPython(pythonFile, "-p", section, key);
			// read the return value from the sockets.
			Object obj = holder.get(key);
			return obj;
		} catch(IOException e) {
			throw new PythonRunnerException("Holder Initialization Exception:", e);
		}
	}

	
	public static final String escapePyParam(String arg){
		arg = arg.trim();
		if (!arg.contains(ESCAPE_MARK_QUOTATION)){
			return ESCAPE_MARK_QUOTATION + arg + ESCAPE_MARK_QUOTATION;
		}
		else if (!arg.contains(ESCAPE_MARK_SINGLE_QUOTATION)){
			return ESCAPE_MARK_SINGLE_QUOTATION + arg + ESCAPE_MARK_SINGLE_QUOTATION;
		}
		else {
			return arg;
		}
	}
	
} 
