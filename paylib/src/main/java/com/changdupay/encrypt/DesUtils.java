package com.changdupay.encrypt;


import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;

import com.changdupay.protocol.base.PayConst;

/**
* SecretUtils {3DES加密解密的工具类 }
* @author William
* @date 2013-04-19
*/
public class DesUtils {

	//定义加密算法，有DES、DESede(即3DES)、Blowfish
	private static final String Algorithm = "DESede"; 
	private static final String PASSWORD_CRYPT_KEY = PayConst.APPKEY;

	/**
	* 加密方法
	* @param src 源数据的字节数组
	* @return 
	*/
	public static byte[] encryptMode(byte[] src, String key)
	{
		try
		{
			SecretKey deskey = new SecretKeySpec(build3DesKey(TextUtils.isEmpty(key) ? PASSWORD_CRYPT_KEY : key), Algorithm); //生成密钥
			Cipher c1 = Cipher.getInstance(Algorithm); //实例化负责加密/解密的Cipher工具类
			c1.init(Cipher.ENCRYPT_MODE, deskey); //初始化为加密模式
			return c1.doFinal(src);
		} 
		catch (java.security.NoSuchAlgorithmException e1)
		{
			e1.printStackTrace();
		} 
		catch (javax.crypto.NoSuchPaddingException e2) 
		{
			e2.printStackTrace();
		} 
		catch (Exception e3)
		{
			e3.printStackTrace();
		}
		return null;
	}
	
	/**
	* 解密函数
	* @param src 密文的字节数组
	* @return
	*/
	public static byte[] decryptMode(byte[] src, String key) 
	{ 
		try 
		{
			SecretKey deskey = new SecretKeySpec(build3DesKey(TextUtils.isEmpty(key) ? PASSWORD_CRYPT_KEY : key), Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey); //初始化为解密模式
			return c1.doFinal(src);
		} 
		catch (java.security.NoSuchAlgorithmException e1)
		{
			e1.printStackTrace();
		} 
		catch (javax.crypto.NoSuchPaddingException e2)
		{
			e2.printStackTrace();
		} 
		catch (Exception e3)
		{
			e3.printStackTrace();
		}
		return null;
	}

	/*
	* 根据字符串生成密钥字节数组 
	* @param keyStr 密钥字符串
	* @return 
	* @throws UnsupportedEncodingException
	*/
	public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException
	{
		byte[] key = new byte[24]; //声明一个24位的字节数组，默认里面都是0
		byte[] temp = keyStr.getBytes("UTF-8"); //将字符串转成字节数组

		/*
		* 执行数组拷贝
		* System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
		*/
		if(key.length > temp.length)
		{
			//如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, temp.length);
		}
		else
		{
			//如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, key.length);
		}
		return key;
	} 
	

    // 默认3Des向量
    private static byte[] Default3DesIV = { 0x01, 0x23, 0x45, 0x67, 0x3A, 0x45, 0x41, 0x7C  };
	/**
     * CBC加密
     * @param key 密钥
     * @param keyiv IV
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(byte[] keyiv, byte[] data, byte[] key)
            throws Exception {
    	keyiv = GetDefaultIVFromKey(key);
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);

        return bOut;
    }
    
    /**
     * CBC解密
     * @param key 密钥
     * @param keyiv IV
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);

        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] bOut = cipher.doFinal(data);

        return bOut;

    }
    
  /// <summary>
    /// 取密钥前8字节作为向量
    /// </summary>
    /// <param name="key"></param>
    /// <returns></returns>
    public static byte[] GetDefaultIVFromKey(byte[] key)
    {
        byte[] iv = new byte[8];
        if (key.length < 8)
            iv = Default3DesIV;
        else
        {
            for (int i = 0; i < 8; i++)
            {
                //iv[i] = key[key.Length - 8 + i]; // 后8位
                iv[i] = key[i]; // 前8位
            }
        }
        return iv;
    }
    
	//转换成十六进制字符串 
	public static String byte2hex(byte[] b)
	{ 
		String hs=""; 
		String stmp=""; 
		for (int n=0;n<b.length;n++) 
		{ 
			stmp=(Integer.toHexString(b[n] & 0XFF));
			if (stmp.length()==1) 
			{
				hs=hs+"0"+stmp; 
			}
			else 
			{
				hs=hs+stmp; 
			}
			if (n<b.length-1) 
			{
				//hs=hs+":"; 
			}
		} 
		return hs.toUpperCase(); 
	}
}