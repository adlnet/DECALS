package com.eduworks.resolver.security;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverEncrypt extends Resolver
{
	@Override
	public Object resolve(Context c, Map<String,String[]> parameters, Map<String,InputStream> dataStreams) throws JSONException
	{
		if (has(KEY))
			resolveAChild(c,parameters, dataStreams, KEY);

		resolveAChild(c,parameters, dataStreams, VALUE);

	    try
		{
	    	return encrypt(getAsString("salt",parameters),parameters);
		}
		catch (GeneralSecurityException gse)
		{
			throw new RuntimeException(gse);
		}
	}
	/* STATIC MEMBERS */

	protected static final char DELIM = '|';

	protected static final String KEY	= "key";
	protected static final String VALUE	= "value";
	protected static final String SALT	= "levr";

	protected static final int BYTES = 16;

	/** Converts value from encode encryption to the original byte array. */
	private static byte[] decode(String string)
	{
		if (string == null) return null;

		final char[] encoded = string.toCharArray();
		final byte[] decoded = new byte[encoded.length];

		final StringBuilder next = new StringBuilder(4);
		int index = 0;

		for (char c : encoded)
			if (c != DELIM)
				next.append(c);
			else
			{
				decoded[index++] = Byte.parseByte(next.toString());
				next.setLength(0);
			}

		decoded[index++] = Byte.parseByte(next.toString());

		final byte[] trimmed = new byte[index];

		System.arraycopy(decoded, 0, trimmed, 0, index);

		return trimmed;

	}

	public static String decrypt(String encrypted, String key,String salt) throws GeneralSecurityException
	{
		if (encrypted == null || encrypted.isEmpty()) return null;

		return new String(getCipher(false, key,salt).doFinal(decode(encrypted)));
	}

	/** Converts encrypted bytes to a consistent format not affected by url encoding. */
	private static String encode(byte[] bytes)
	{
		if (bytes == null) return null;

		// Longest encoded byte value is five chars in length: -128|
		final StringBuilder encoded = new StringBuilder(bytes.length * 5);

		for (final byte b : bytes)
			encoded.append(Byte.toString(b)).append(DELIM);

		return encoded.substring(0, encoded.length() - 1);
	}

	public static String encrypt(String decrypted, String key,String salt) throws GeneralSecurityException
	{
		if (decrypted == null || decrypted.isEmpty()) return null;

		return encode(getCipher(true, key,salt).doFinal(decrypted.getBytes()));
	}

	private static Cipher getCipher(boolean encrypt, String key,String salt) throws GeneralSecurityException
	{
		final String algorithm = "AES";

		final SecretKeySpec skeySpec = new SecretKeySpec(getKeyBytes(key,salt), algorithm);
		final Cipher cipher = Cipher.getInstance(algorithm);

	    if (encrypt)
	    	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	    else
	    	cipher.init(Cipher.DECRYPT_MODE, skeySpec);

	    return cipher;
	}

	/** Generates an unpredictable, but consistent byte sequence for consistent decoding. */
	private static byte[] getKeyBytes()
	{
		final byte[] key = new byte[BYTES];

		for (int i = 0; i < key.length; i++)
			key[i] = (i % 2 == 0)
				? (byte) (Byte.MAX_VALUE - (i % 7 * 10))
				: (byte) (Byte.MIN_VALUE + (i % 7 * 10));

		return key;
	}

	/** Generates an unpredictable, but consistent byte sequence from key. */
	private static byte[] getKeyBytes(String key,String salt) throws GeneralSecurityException
	{
		if (key == null || key.isEmpty()) return getKeyBytes();

		final byte[] randomized = new byte[BYTES];

		final String salted = getSaltedKey(key,salt);

		new Random(salted.hashCode()).nextBytes(randomized);

		return randomized;
	}

	/** Salts a key with a constant value that is relative to the key itself. */
	private static String getSaltedKey(String key,String salt) throws GeneralSecurityException
	{
		if (key == null || key.isEmpty()) key = SALT;

		final StringBuilder salted = new StringBuilder(key.length() + 36);

		salted.append(key);

		while (salted.length() < 32)
			salted.append(SALT);

		return salted.substring(0, 32);
	}


	/* INSTANCE METHODS */

	public String decrypt(String salt,Map<String,String[]> parameters) throws GeneralSecurityException, JSONException
	{
    	return decrypt(getAsString(VALUE, parameters), optAsString(KEY, parameters),salt);
	}

	public String encrypt(String salt,Map<String,String[]> parameters) throws GeneralSecurityException, JSONException
	{
    	return encrypt(getAsString(VALUE, parameters), optAsString(KEY, parameters),salt);
	}

	@Override
	public String getDescription()
	{
		return "Encrypts 'value' using 'key'";
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("key","String","value","String");
	}

}
