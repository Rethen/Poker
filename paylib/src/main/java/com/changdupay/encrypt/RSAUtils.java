package com.changdupay.encrypt;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
public class RSAUtils {
	/** *//**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    /** *//**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";    
    /** *//**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    
    /** *//**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;    
    /** *//**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    public static final String SIGN_ALGORITHMS  ="SHA1WithRSA";
   

	/** *//**
	* <p>
	* 数字签名
	* </p>
	* 
	* @param content 已加密数据
	* @param pprivateKey 公钥(BASE64编码)
	* @param sign 数字签名
	* 
	* @return
	* @throws Exception
	* 
	*/
	public static String sign(String content, String privateKey) 
	{
		String charset = "utf-8";		  
		try 
		{	
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));			 
			KeyFactory keyf = KeyFactory.getInstance(KEY_ALGORITHM);		 
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);		   
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes());
			byte[] signed = signature.sign();      
			return Base64.encode(signed); //返回结果base64				 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		return null;
	}

	/** *//**
	* <p>
	* 校验数字签名
	* </p>
	* 
	* @param data 已加密数据
	* @param publicKey 公钥(BASE64编码)
	* @param sign 数字签名
	* 
	* @return
	* @throws Exception
	* 
	*/
	public static boolean doCheck(String content, String sign, String publicKey) 
	{
		String charset = "utf-8";
		try 
		{
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			byte[] encodedKey = Base64.decode(publicKey);	 
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes(charset));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return false;
	}

	//*************************************为测试加入函数**********************************************************/
	/** *//**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> genKeyPair() throws Exception
	{
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance( KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();    
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/** *//**
	 
	 * <p>
	 * 获取私钥
	 * </p>
	 * 
	 * @param keyMap 密钥对
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64.encode(key.getEncoded());
	}

	/** *//**
	 * <p>
	 * 获取公钥
	 * </p>
	 * 
	 * @param keyMap 密钥对
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64.encode(key.getEncoded());
	}

	/** *//**
	 * <p>
	 * 私钥加密
	 * </p>
	 * 
	 * @param data 源数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key)   
			throws Exception {   
		// 对密钥解密   
		byte[] keyBytes =  Base64.decode(key);   

		// 取得私钥   
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);   
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);   
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);   
		//    RSAPrivateCrtKey prk = (RSAPrivateCrtKey)privateKey;

		// 对数据加密   
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());   
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);   
		byte[] tmp=cipher.doFinal(data); 
		System.out.println("["+ByteToHex(tmp)+"]"); 	     
		return tmp;   
	}   

	/** *//**
	 * <p>
	 * 公钥解密
	 * </p>
	 * 
	 * @param encryptedData 已加密数据
	 * @param publicKey 公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key)   
			throws Exception {   
		// 对密钥解密   
		byte[] keyBytes = Base64.decode(key); 
		// 取得公钥   
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);   
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);   
		Key publicKey = keyFactory.generatePublic(x509KeySpec); 
		// 对数据解密   
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());   
		cipher.init(Cipher.DECRYPT_MODE, publicKey);   

		return cipher.doFinal(data);   
	}   



	/** *//** 
	 * 加密<br> 
	 * 用公钥加密 
	 *   
	 * @param data 
	 * @param key 
	 * @return 
	 * @throws Exception 
	 */ 
	public static byte[] encryptByPublicKey(byte[] data, String key)   
			throws Exception {   
		// 对公钥解密   
		byte[] keyBytes =Base64.decode(key);   
		// 取得公钥   
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);   
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);   
		Key publicKey = keyFactory.generatePublic(x509KeySpec); 
		// 对数据加密       
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		//Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm()); 
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
		byte[] output = cipher.doFinal(data);
		//  String enhex= ByteToHex(output );        
		// System.out.println(" "+ output+"]");
		return output; 
	}   

	/** *//** 
	 * 加密<br> 
	 * 用公钥加密 
	 *   
	 * @param data 
	 * @param key 
	 * @return 
	 * @throws Exception 
	 */ 
	public static byte[] encryptByModule(byte[] data, String str_modulus)   
			throws Exception {   
		byte[] ary_exponent = { 1, 0, 1 };
		byte[] ary_modulus = hexStringToBytes(str_modulus);
		BigInteger big_exponent = new BigInteger(1,ary_exponent);  
        BigInteger big_modulus = new BigInteger(1,ary_modulus);  
        RSAPublicKeySpec keyspec=new RSAPublicKeySpec(big_modulus,big_exponent);  
        KeyFactory keyfac=KeyFactory.getInstance("RSA");  
        PublicKey publicKey=keyfac.generatePublic(keyspec); 
		// 对数据加密       
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		//Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm()); 
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
		byte[] output = cipher.doFinal(data);
		//  String enhex= ByteToHex(output );        
		// System.out.println(" "+ output+"]");
		return output; 
	}  
	
	//私钥解密
	public static byte[] decryptByPrivateKey(byte[] data, String key)   
			throws Exception {   
		// 对密钥解密   
		// 对密钥解密   
		byte[] keyBytes =Base64.decode(key);   

		// 取得私钥   
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);   
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);   
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);   

		// 对数据解密  
		// Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");        
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		//Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());   
		cipher.init(Cipher.DECRYPT_MODE, privateKey);   

		return cipher.doFinal(data);   
	}   
	
	static  public String Encrypt(String strSrc,String encName)
	{          
		//parameter strSrc is a string will be encrypted,    
		//parameter encName is the algorithm name will be used.  
		//encName dafault to "MD5"  
		MessageDigest md=null;
		String strDes=null; 
		byte[] bt=strSrc.getBytes();  
		try 
		{   
			if (encName==null||encName.equals(""))
			{  
				encName="MD5";   
			} 
			md=MessageDigest.getInstance(encName); 
			md.update(bt); 
			String str="sh1bit:";
			
			System.out.println(str);
			byte[] shabt=md.digest();
			strDes=ByteToHex( shabt); 
			for (int i=0;i<shabt.length;i++)
			{
				str+=shabt[i]+",";  			
			}
			System.out.print(str);
			//to HexString  
		}  
		catch (NoSuchAlgorithmException e)
		{   
			System.out.println("Invalid algorithm.");  
			return null;  
		} 
		return strDes; 
	}

	//btye转换hex函数
	public  static String ByteToHex(byte[] byteArray) 
	{
		StringBuffer StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++)
		{
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) 
			{
				StrBuff.append("0").append(
				Integer.toHexString(0xFF & byteArray[i]));
			} else 
			{
				StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return StrBuff.toString();
	}

	private static byte charToByte(char c) 
	{   
		return (byte) "0123456789ABCDEF".indexOf(c);   
	} 
	 
	public static String bytesToHexString(byte[] src)
	{   
		StringBuilder stringBuilder = new StringBuilder("");   
		if (src == null || src.length <= 0)
		{   
			return null;   
		}   
		for (int i = 0; i < src.length; i++) 
		{   
			int v = src[i] & 0xFF;   
			String hv = Integer.toHexString(v);   
			if (hv.length() < 2) 
			{   
				stringBuilder.append(0);   
			}   
			stringBuilder.append(hv);   
		}        
		return stringBuilder.toString();   
	}  

	public static byte[] hexStringToBytes(String hexString) 
	{   
		if (hexString == null || hexString.equals(""))
		{   
			return null;   
		}   
		hexString = hexString.toUpperCase();   
		int length = hexString.length() / 2;   
		char[] hexChars = hexString.toCharArray();   
		byte[] d = new byte[length];   
		for (int i = 0; i < length; i++)
		{   
			int pos = i * 2;   
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
		}   
		return d;   
	}
}
