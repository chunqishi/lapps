package edu.brandeis.cs.python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @brief Invoke Python file and run with input and return output.
 * @author shicq@cs.brandeis.edu
 *
 */
public class PythonRunner {
	/**
	 * 
	 * @param pythonFile python file
	 * @param arrParams parameter array
	 * @see runPython(String pythonFile, String params)
	 * @return
	 */
	public static String runPython(String pythonFile, String[] arrParams) throws IOException {
		StringBuffer params = new StringBuffer();
		for (String param: arrParams) {
			params.append(param).append(" ");
		}
		return runPython(pythonFile, params.substring(0, params.length() - 1));		
	}
	
	
	/**
	 * @brief run python file and return result throw IOException.
	 * @param pythonFile 
	 * @param params parameters as commandline input.
	 * @return
	 */
	public static String runPython(String pythonFile, String params) throws IOException {
		StringBuffer result = new StringBuffer();
        String[] callAndArgs = { "python", pythonFile, params};
        Process p = Runtime.getRuntime().exec(callAndArgs);
        BufferedReader stdInput =
            new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError =
            new BufferedReader(new InputStreamReader(p.getErrorStream()));
        p.destroy();
        String st = null;
        while ((st = stdInput.readLine()) != null) {
            result.append(st);
        }
        while ((st = stdError.readLine()) != null) {
            System.err.println(st);            
        }        
        // RETURN THE RESULT RATHER THAN PRINTING IT
        System.out.println(result.toString());
        return result.toString();
	}
	
} 