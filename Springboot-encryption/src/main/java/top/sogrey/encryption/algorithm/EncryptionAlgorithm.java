package top.sogrey.encryption.algorithm;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class EncryptionAlgorithm {
	/**
	 * 生成对称加密算法的密钥
	 * 
	 * @param keySize
	 * @return
	 */
	public static SecretKey generateKey(int keySize) {
		KeyGenerator keyGenerator;
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(keySize);
			return keyGenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// ignore
			return null;
		}
	}

	/**
	 * 生成非对称密钥对
	 *
	 * @param keySize 密钥大小
	 * @param random  指定随机来源，默认使用 JCAUtil.getSecureRandom()
	 * @return 非对称密钥对
	 * @throws NoSuchAlgorithmException NoSuchAlgorithm
	 */
	public static PPKeys genKeysRSA(int keySize, SecureRandom random) throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		if (null != random) {
			generator.initialize(keySize, random);
		} else {
			generator.initialize(keySize);
		}
		KeyPair pair = generator.generateKeyPair();
		PPKeys keys = new PPKeys();
		PublicKey publicKey = pair.getPublic();
		PrivateKey privateKey = pair.getPrivate();
		keys.setPublicKey(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
		keys.setPrivatekey(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
		return keys;
	}

	public static void diffieHellman() throws Exception {
		AlgorithmParameterGenerator dhParams = AlgorithmParameterGenerator.getInstance("DH");
		dhParams.init(1024);
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
		keyGen.initialize(dhParams.generateParameters().getParameterSpec(DHParameterSpec.class), new SecureRandom());

		KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
		KeyPair alicePair = keyGen.generateKeyPair();
		KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DH");
		KeyPair bobPair = keyGen.generateKeyPair();

		aliceKeyAgree.init(alicePair.getPrivate());
		bobKeyAgree.init(bobPair.getPrivate());

		aliceKeyAgree.doPhase(bobPair.getPublic(), true);
		bobKeyAgree.doPhase(alicePair.getPublic(), true);

		boolean agree = Base64.getEncoder().encodeToString(aliceKeyAgree.generateSecret())
				.equals(Base64.getEncoder().encodeToString(bobKeyAgree.generateSecret()));
		System.out.println(agree);
	}

	public static String md5(String content) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(content.getBytes(StandardCharsets.UTF_8));
			return Hex.encodeHexString(bytes);
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/*
	 * SHA系列 安全散列算法（Secure Hash
	 * Algorithm，缩写为SHA）是一个加密散列函数家族，是FIPS(美国联邦信息处理标准)所认证的安全散列算法。能计算出一个数字消息所对应到的，
	 * 长度固定的字符串（又称消息摘要）的算法。且若输入的消息不同，它们对应到不同字符串的机率很高。
	 * 
	 * 它们分别包含 SHA-0、SHA-1、SHA-2、SHA-3，其中 SHA-0、SHA-1 输出长度是160位，SHA-2 包含
	 * SHA-224、SHA-256、SHA-384、SHA-512、SHA-512/224、SHA-512/256，我们平时常用 SHA-256 。
	 */
	public static String sha256(String content) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] bytes = digest.digest(content.getBytes(StandardCharsets.UTF_8));
			return Hex.encodeHexString(bytes);
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/*
	 * DES DES 是对称加密算法领域中的典型算法，因为密钥默认长度为56 bit， 所以密码长度需要大于 8 byte，DESKeySpec 取前 8
	 * byte 进行密钥制作。
	 */
	public static String encryptDES(byte[] content, String password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
			return Base64.getEncoder().encodeToString(cipher.doFinal(content));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decryptDES(String content, String password) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
		return new String(cipher.doFinal(Base64.getDecoder().decode(content)));
	}

	/*
	 * 3DES 3DES（即Triple DES）。是DES算法的加强，它使用3条56位的密钥对数据进行三次加密。
	 * 它以DES为基本模块，通过组合分组方法设计出分组加密算法。比起最初的DES，3DES更为安全。 密钥默认长度 168 bit， 密码需要大于24
	 * byte，IV 是 8 byte 的随机数字和字母数组。
	 */

	public static String encrypt3DESECB(String content, String key, String iv) {
		try {
			IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
			DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(StandardCharsets.UTF_8));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey secretkey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretkey, ivSpec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt3DESECB(String content, String key, String iv) {
		try {
			IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
			DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(StandardCharsets.UTF_8));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey secretkey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretkey, ivSpec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(content)), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * AES AES 高级数据加密标准，能够有效抵御已知的针对DES算法的所有攻击，默认密钥长度为128 bit，还可以供选择 192 bit，256
	 * bit。AES-128 AES-192 AES-256
	 * 
	 * 默认 AES-128 ，使用 PBEKeySpec 生成固定大小的密钥。
	 */

	public static String encryptAES128(String plainText, String password, String salt) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
		// AES-128 密钥长度为128bit
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 1000, 128);
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		AlgorithmParameters params = cipher.getParameters();
		IvParameterSpec iv = params.getParameterSpec(IvParameterSpec.class);

		cipher.init(Cipher.ENCRYPT_MODE, secret, iv);
		byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

		String encodedText = Base64.getEncoder().encodeToString(encryptedTextBytes);
		String encodedIV = Base64.getEncoder().encodeToString(iv.getIV());
		String encodedSalt = Base64.getEncoder().encodeToString(saltBytes);
		return encodedSalt + "." + encodedIV + "." + encodedText;
	}

	public static String decryptAES128(String encryptedText, String password) throws Exception {
		String[] fields = encryptedText.split("\\.");
		byte[] saltBytes = Base64.getDecoder().decode(fields[0]);
		byte[] ivBytes = Base64.getDecoder().decode(fields[1]);
		byte[] encryptedTextBytes = Base64.getDecoder().decode(fields[2]);

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 1000, 128);

		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
		byte[] decryptedTextBytes;
		try {
			decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
			return new String(decryptedTextBytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 非对称加密算法
	 * 非对称加密使用一对密钥，公钥用作加密，私钥则用作解密。关于密钥大小，截至2020年，公开已知的最大RSA密钥是破解的是829位的RSA-250，
	 * 建议至少使用 2048 位密钥。
	 */
	public static String encrypt(byte[] publicKey, String plainText) {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			PublicKey publicKeySecret = kf.generatePublic(keySpec);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKeySecret);
			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
			return new String(Base64.getEncoder().encode(encryptedBytes));
		} catch (Exception e) {
			System.err.println("Rsa encrypt error :" + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(byte[] privateKey, String encryptedText) {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			PrivateKey privateKeySecret = kf.generatePrivate(keySpec);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKeySecret);
			return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)), StandardCharsets.UTF_8);
		} catch (Exception e) {
			System.err.println("Rsa decrypt error :" + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
