package com.ucm.design1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Security;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class ModifiedHttpClient {
	private static final int TIMEOUT = 10000;

	
	public static String calculateMD5Hash(String input)
	{
		Security.addProvider(new BouncyCastleProvider());
		MD5Digest md5 = new MD5Digest();
		md5.update(input.getBytes(), 0, input.getBytes().length);
		byte[] digest = new byte[md5.getDigestSize()];
		md5.doFinal(digest, 0);
		return new String(Hex.encode(digest));
		
		
	}
	
	public static String sendRequestAndGetResponse(String requestVar,
			String requestValue, String url) {
		try {

			// Toast.makeText(this, request,Toast.LENGTH_LONG).show();

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
			// httpParams.setParameter("json", request);
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost httpRequest = new HttpPost(url);
			HttpEntity sentity = new ByteArrayEntity(
					requestValue.getBytes("UTF8"));
			httpRequest.setEntity(sentity);
			httpRequest.setHeader(requestVar, requestValue);

			HttpResponse httpResponse = client.execute(httpRequest);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				InputStream inStream = entity.getContent();
				String result = convertStreamToString(inStream);
				// Log.i("Read from server", result);
				// Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				return result;
			}

		} catch (Throwable t) {
			return t.toString();

		}
		return "";
	}
	
	public static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	public static int getIDForNewItem(String name,String number)
	{
		//have to implement
		return 0;
	}

}
