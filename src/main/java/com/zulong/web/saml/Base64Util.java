package com.zulong.web.saml;

import org.apache.commons.codec.binary.Base64;

/**
 * 说明�?
 * @author xiayisan 
 * @version 创建时间: 2015�?�?6�?下午4:38:39
 *
 */
public class Base64Util
{
	/**
	 * Base64编码
	 * @param bytes 待编码二进制数据 
	 * @return String
	 */
	public static String encode(byte[] bytes) throws Exception
	{
		return new String(Base64.encodeBase64(bytes));
	}
	
	/**
	 * Base64 解码，返回byte数组
	 * @param base64 待解码数�?
	 * @return byte[]
	 */
	public static byte[] decode(String base64) throws Exception
	{
		return Base64.decodeBase64(base64.getBytes());
	}
}
