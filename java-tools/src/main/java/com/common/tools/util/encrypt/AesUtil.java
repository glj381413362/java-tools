package com.common.tools.util.encrypt;

import com.common.tools.util.PluginConstants;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * <p>
 * AES加密工具类
 * AES已经变成目前对称加密中最流行算法之一；AES可以使用128、192、和256位密钥，并且用128位分组加密和解密数据
 * </p>
 *
 * @author gongliangjun 2019/12/28 5:51 PM
 */
public class AesUtil {

	public static final String AES_KEY = "AES";

	/**
	 * des加密
	 *
	 * @param str 待加密对象
	 * @param key 密钥 长度为8的倍数
	 * @return
	 */
	public static byte[] encrypt(String str, String key) {
		byte[] bytes = null;

		try {
			Cipher cipher = init(Cipher.ENCRYPT_MODE, key);
			if (cipher == null) {
				return new byte[]{};
			}
			bytes = cipher.doFinal(str.getBytes(PluginConstants.UTF8));
		} catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return bytes;
	}

	/**
	 * des解密
	 *
	 * @param bytes 待解密对象
	 * @param key   密钥 长度为8的倍数
	 * @return
	 */
	public static String decrypt(byte[] bytes, String key) {
		String str = null;
		try {
			Cipher cipher = init(Cipher.DECRYPT_MODE, key);
			if (cipher == null) {
				return null;
			}
			str = new String(cipher.doFinal(bytes), PluginConstants.UTF8);
		} catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return str;
	}


	private static Cipher init(int mode, String key) {
		Cipher cipher = null;
		try {
			KeyGenerator kGen = KeyGenerator.getInstance(AES_KEY);

			kGen.init(128, new SecureRandom(key.getBytes()));

			cipher = Cipher.getInstance(AES_KEY);
			cipher.init(mode, new SecretKeySpec(kGen.generateKey().getEncoded(), AES_KEY));

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return cipher;
	}
}
