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
	
	
	public static final String PYRO4_RET_HEADER = "RETURN_";
	
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
        System.out.print("runPython(): python "+ pythonFile);
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
        System.out.println("runPython(): result=" + result.toString());
        return result.toString();
	}
	
	public String getDefProperty(String key){
		return getSectionProperty(CONF_SECTION_DEFAULT, key);
	}
	
	public String getSectionProperty(String section, String key){		
		String value = prop.ReadString(section, key, "");
//		System.out.println("section="+section+" key="+key+" value="+value);
		return value;
	}
	
	
	public void updateConf() throws PythonRunnerException{
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
	}

	

	
	public String startPyro4Holder() throws PythonRunnerException{
		String pythonFileConf = getDefProperty(CONF_PYTHON_FILE);		
		String pythonFile = FileLoadUtil.locate(pythonFileConf).getAbsolutePath();
		return runPython(pythonFile, "-o", "start");
	}

	public String stopPyro4Holder() throws PythonRunnerException{
		String pythonFileConf = getDefProperty(CONF_PYTHON_FILE);		
		String pythonFile = FileLoadUtil.locate(pythonFileConf).getAbsolutePath();
		return runPython(pythonFile, "-o", "stop");
	}

	/**
	 * Parameters can be objects. 
	 * 3. TYPE MAPPINGS
	 *<p>   ---------------------
	 *<p>   			
	 *<p>   Pyrolite does the following type mappings:
	 *<p>   			
	 *<p>   PYTHON    ---->     JAVA		[[[[ RETURN VALUES ]]]
	 *<p>   ------              ----
	 *<p>   None                null
	 *<p>   bool                boolean
	 *<p>   int                 int
	 *<p>   long                long or BigInteger  (depending on size)
	 *<p>   string              String
	 *<p>   unicode             String
	 *<p>   complex             net.razorvine.pickle.objects.ComplexNumber
	 *<p>   datetime.date       java.util.Calendar
	 *<p>   datetime.datetime   java.util.Calendar
	 *<p>   datetime.time       java.util.Calendar
	 *<p>   datetime.timedelta  net.razorvine.pickle.objects.TimeDelta
	 *<p>   float               double   (float isn't used) 
	 *<p>   array.array         array of appropriate primitive type (char, int, short, long, float, double)
	 *<p>   list                java.util.List<Object>
	 *<p>   tuple               Object[]
	 *<p>   set                 java.util.Set
	 *<p>   dict                java.util.Map
	 *<p>   bytes               byte[]
	 *<p>   bytearray           byte[]
	 *<p>   decimal             BigDecimal    
	 *<p>   custom class        Map<String, Object>  (dict with class attributes including its name in "__class__")
	 *<p>   Pyro4.core.URI      net.razorvine.pyro.PyroURI
	 *<p>   Pyro4.core.Proxy    net.razorvine.pyro.PyroProxy
	 *<p>   Pyro4.errors.*      net.razorvine.pyro.PyroException
	 *<p>   Pyro4.utils.flame.FlameBuiltin     net.razorvine.pyro.FlameBuiltin 
	 *<p>   Pyro4.utils.flame.FlameModule      net.razorvine.pyro.FlameModule 
	 *<p>   Pyro4.utils.flame.RemoteInteractiveConsole    net.razorvine.pyro.FlameRemoteConsole 
	 *<p>   
	 *<p>   The unpickler simply returns an Object. Because Java is a statically typed
	 *<p>   language you will have to cast that to the appropriate type. Refer to this
	 *<p>   table to see what you can expect to receive.
	 *<p>                       
	 *<p>   
	 *<p>   JAVA     ---->      PYTHON  		[[[[ PARAMETERS ]]]
	 *<p>   -----               ------
	 *<p>   null                None
	 *<p>   boolean             bool
	 *<p>   byte                int
	 *<p>   char                str/unicode (length 1)
	 *<p>   String              str/unicode
	 *<p>   double              float
	 *<p>   float               float
	 *<p>   int                 int
	 *<p>   short               int
	 *<p>   BigDecimal          decimal
	 *<p>   BigInteger          long
	 *<p>   any array           array if elements are primitive type (else tuple)
	 *<p>   Object[]            tuple (cannot contain self-references)
	 *<p>   byte[]              bytearray
	 *<p>   java.util.Date      datetime.datetime
	 *<p>   java.util.Calendar  datetime.datetime
	 *<p>   Enum                the enum value as string
	 *<p>   java.util.Set       set
	 *<p>   Map, Hashtable      dict
	 *<p>   Vector, Collection  list
	 *<p>   Serializable        treated as a JavaBean, see below.
	 *<p>   JavaBean            dict of the bean's public properties + __class__ for the bean's type.
	 *<p>   net.razorvine.pyro.PyroURI      Pyro4.core.URI
	 *<p>   net.razorvine.pyro.PyroProxy    cannot be pickled.
	 * @param section
	 * @param arrParams
	 * @return
	 * @throws PythonRunnerException
	 */
	
	public Object runPythonSectionPyro(String section, Object ... arrParams) throws PythonRunnerException {
		updateConf();
		// provide function running sockets key
		String key = section + ":" + System.currentTimeMillis();
		try {
			// create a new connection for pyro4holder.
			Pyro4Holder holder = new Pyro4Holder(this);
			// push all the parameters into sockets 
			holder.put(key, arrParams);
			String pythonFileConf = getDefProperty(CONF_PYTHON_FILE);
			String pythonFile = FileLoadUtil.locate(pythonFileConf).getAbsolutePath();
			// runPythonSection
			runPython(pythonFile, "-p", section, key);
			// read the return value from the sockets.
			Object obj = holder.get(PYRO4_RET_HEADER + key);
			return obj;
		} catch(IOException e) {
			throw new PythonRunnerException("Holder Initialization Exception:", e);
		}
	}
	
	public Object runPythonSection(String section, Object ... arrParams) throws PythonRunnerException {
		return runPythonSectionPyro(section, arrParams);
	}
	
	/**
	 * <p> We would like to use Pyro4 interface, which could get the function returned result 
	 * instead of  the printed result in the python. 
	 * @deprecated Please Use {@code runPythonSectionPyro}
	 * @param section
	 * @param arrParams
	 * @return
	 * @throws PythonRunnerException
	 */
	
	@Deprecated
	public String runPythonSection(String section, String ... arrParams) throws PythonRunnerException {
		updateConf(); 
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
