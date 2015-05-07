package com.global.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

public class NetworkRequests {
	static InputStream is = null; // 输入流
	static JSONObject jObj = null; // Json对象
	static JSONArray jArr = null; // Json数组
	static String json = ""; // Json字符串

	public static String makeHttpRequest(String url, String method,
			List<NameValuePair> params) throws Exception {
		// System.out.println(">>>:" + url + method
		// + new JSONArray(params).toString());
		if (method == "POST") {
			// request method is POST
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("charset", HTTP.UTF_8);
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// httpPost.setEntity(new FileEntity(file, contentType));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} else if (method == "GET") {
			// request method is GET
			HttpClient httpClient = new DefaultHttpClient();
			String paramString = URLEncodedUtils.format(params, "utf-8");
			url += "?" + paramString;
			HttpGet httpGet = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		json = sb.toString();
		// System.out.println("<<<:" + json);
		return json;
	}

}
