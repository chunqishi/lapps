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
	 * @param params 
	 * @see runPython(String pythonFile, String params)
	 * @return
	 */
	public static String runPython(String pythonFile, String params) throws IOException {
		return runPython(pythonFile, new String [] {params});		
	}
	
	
	/**
	 * @brief run python file and return result throw IOException.
	 * @param pythonFile 
	 * @param parameter array
	 * @return
	 */
	public static String runPython(String pythonFile, String[] arrParams) throws IOException {
		StringBuffer result = new StringBuffer();
        String[] callAndArgs = new String[arrParams.length + 2];
        callAndArgs[0] = "python";
        callAndArgs[1] = pythonFile;
        for (int i = 0; i < arrParams.length; i++) {
        	callAndArgs[2 + i] = arrParams[i];
        }
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
        p.destroy();
        // RETURN THE RESULT RATHER THAN PRINTING IT
        System.out.println(result.toString());
        return result.toString();
	}
	
} 