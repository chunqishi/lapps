package edu.brandeis.cs.python.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

import org.anc.resource.ResourceLoader;
/**
 * Provide self needed file loading related functions.
 * @author shicq@cs.brandeis.edu
 *
 */
public class FileLoadUtil {

	public static final String checksumMD5(File file)
			throws NoSuchAlgorithmException, IOException {
		return checksum(file, "MD5");
	}

	public static final String checksumSHA1(File file)
			throws NoSuchAlgorithmException, IOException {
		return checksum(file, "SHA1");
	}

	private static final ConcurrentHashMap<String, String> fileHashMap = new ConcurrentHashMap<String, String>(
			256);
/**
 * Find resources from class path.  Java class loader provides getResources function.  
 * Using it, we can locate the uri of the resource.    
 * @param resource
 * @return
 */
	public static File locate(String resource) {
		File resFile = null;
		try {
			URL resURL = getClassLoader().getResource(resource);
			if (resURL != null)
				resFile = new File(resURL.toURI());
			// System.out.println(checksumSHA1(resFile));
//			System.out.println(resource);
//			System.out.println(resFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resFile == null)
			resFile = new File(resource);
		return resFile;
	}

/**
 * We use a hash map memory to hold loaded files' check sum. 
 * If the check sum is changed, we return True. 
 * Here we use MD5 check sum, since its speed is acceptable for normal size files.	
 * @param file
 * @return
 * @throws NoSuchAlgorithmException
 * @throws IOException
 */
	public static final boolean needUpdate(File file)
			throws NoSuchAlgorithmException, IOException {
		String path = file.getCanonicalPath();
		String lastChechsum = fileHashMap.get(path);
//		System.out.println(path +" last:" +lastChechsum);
		String currChechsum = checksumMD5(file);
//		System.out.println(path +" curr:" +currChechsum);
		fileHashMap.put(path, currChechsum);
		return !currChechsum.equals(lastChechsum);
	}

/**
 * Provide check sum function, which read all the bits and calculate the  digested string. 	
 * @param file
 * @param dgst
 * @return
 * @throws NoSuchAlgorithmException
 * @throws IOException
 */
	public static final String checksum(File file, String dgst)
			throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance(dgst);
		FileInputStream fis = new FileInputStream(file);
		byte[] dataBytes = new byte[1024];

		int nread = 0;
		try {
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			;
		} finally {
			fis.close();
		}
		byte[] mdbytes = md.digest();

		// convert the byte to hex format
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		long start = System.currentTimeMillis();
		long last = System.currentTimeMillis() - start;
//		System.out.println(sb + " " + last + " " + file);
		return sb.toString();
	}

	public static final ClassLoader getClassLoader() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = ResourceLoader.class.getClassLoader();
		}
		return loader;
	}

}
