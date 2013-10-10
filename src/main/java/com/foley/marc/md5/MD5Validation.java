package com.foley.marc.md5;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class MD5Validation {

	public static void main(String[] args) throws Exception
	{
		Path _origFile = Paths.get("c:\\tmp\\np-globalregcam-orig.war");
		Path _compareFile = Paths.get("c:\\tmp\\np-globalregcam.war");
		if (getMD5(_origFile).equals(getMD5(_compareFile)))
		{
			System.out.println("Files matched!!!!");
		}
		else
		{
			System.out.println("Files didn't match");
		}
	}

	private static String getMD5(Path path) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		try (InputStream is = Files.newInputStream(path)) {
			DigestInputStream dis = new DigestInputStream(is, md);
			/* Read stream to EOF as normal... */
		}
		byte[] digest = md.digest();
		String _md5Value = new String(Base64.encodeBase64(digest));
		System.out.println(_md5Value);
		return _md5Value;
	}
}
