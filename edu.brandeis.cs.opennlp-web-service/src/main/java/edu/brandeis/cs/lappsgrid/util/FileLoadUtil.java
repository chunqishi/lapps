package edu.brandeis.cs.lappsgrid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

import org.anc.resource.ResourceLoader;

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

	public static File locate(String resource) {
		File resFile;
		try {
			resFile = new File(getClassLoader().getResource(resource).toURI());
			// System.out.println(checksumSHA1(resFile));
			// System.out.println(resFile);
		} catch (Exception e) {
			resFile = new File(resource);
			e.printStackTrace();
		}
		return resFile;
	}

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
			loader = FileLoadUtil.class.getClassLoader();
		}
		return loader;
	}

}
