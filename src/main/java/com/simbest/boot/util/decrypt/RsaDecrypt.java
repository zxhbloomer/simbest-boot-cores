package com.simbest.boot.util.decrypt;

import org.springframework.util.ResourceUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

/**
 * <strong>Title</strong> : Decrypt.java<br>
 * <strong>Description</strong> : RSA解密，临时方案<br>
 * <strong>Create on</strong> : 2018/01/19<br>
 * <strong>Modify on</strong> : 2018/01/19<br>
 * <strong>Copyright (C) ___ Ltd.</strong><br>
 *
 * @author baimengqi baimengqi@simbest.com.cn
 * @version v0.0.1
 */
public class RsaDecrypt {

	private static RSAPrivateKey privateKey;

	// 读取私钥文件
	private static RSAPrivateKey getPrivateKey() {
		String filePath = "certificate/rsa/pkcs8_private_key.pem";
		try {
			// 读取密钥文件
			filePath = ResourceUtils.getFile("classpath:" + filePath).getPath();
			List<String> allLines = Files.readAllLines(Paths.get(filePath));
			// 删除第一行和最后一行
			allLines.remove(0);
			allLines.remove(allLines.size() - 1);
			// 转换为字符串
			String s = String.join("", allLines.toArray(new String[allLines.size()]));
			// Base64解码
			byte[] buffer = Base64.getDecoder().decode(s);

			// 生成解码器
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ThreadLocal<Cipher> cipherTL = ThreadLocal.withInitial(() -> {
		try {
			// Android的默认RSA是RSA/None/NoPadding，这个可以通过使用下面的方式来使用
			//	Cipher.getInstance("RSA/None/NoPadding", new BouncyCastleProvider());
			return Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return null;
	});

	/**
	 * 解密以base64编码的二进制信息
	 *
	 * @param base64String 字符串
	 *
	 * @return 解密信息
	 */
	public static String decryptWithBase64(String base64String) {
		// 解码加密串
		byte[] base64 = Base64.getMimeDecoder().decode(base64String);

		return decrypt(base64);
	}

	/**
	 * 解密二进制信息
	 *
	 * @param base64 字节数组
	 *
	 * @return 解密信息
	 */
	public static String decrypt(byte[] base64) {
		if (privateKey == null) {
			privateKey = getPrivateKey();
		}

		try {
			Cipher cipher = cipherTL.get();
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			// 解码加密串
			byte[] output = cipher.doFinal(base64);
			String string = new String(output);
			// log.debug("Decrypt with: " + string);

			return string + "";
		} catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

}
