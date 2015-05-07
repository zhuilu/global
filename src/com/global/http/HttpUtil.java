package com.global.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.global.User;
import com.global.alert.AlertWaitService;
import com.global.app.SysApplicationImpl;
import com.global.file.FileUtil;
import com.global.log.CLog;
import com.global.string.StringUtil;
import com.global.thread.HandlerThreadNew;
import com.global.toast.Toaster;
import com.global.R;

/**
 * Top通用操作封装
 */
public class HttpUtil {

	private static String baseURL = "http://" + "HJConstantUtil.httpAndPort"
			+ "/HJDATA/mobile/";

	public HttpUtil() {

	}

	/**
	 * 组合url
	 * 
	 * @return HttpClient
	 */
	public String composeUrl() {
		// String ip = SysApplicationImpl.getSharedPref("server_ip");
		// ip = ip.equals("empty") ? HJConstantUtil.httpAndPort : ip;
		// baseURL = "http://" + ip + "/HJDATA/mobile/";
		return baseURL;
	}

	/**
	 * @param operaType
	 * @param param
	 * @param res
	 *            回调函数
	 */
	public void invokeRequest(final Activity activity, final String operaType,
			final String title, Map<String, String> param, final HJResponse res) {
		if (!checkToken(activity)) {
			invokeError(res, "");
			AlertWaitService.getInstance().forceEndAlertWait();
			return;
		}

		res.setDescribe(operaType);
		baseURL = composeUrl();

		StringBuilder tempurl = new StringBuilder(baseURL);
		tempurl.append(operaType + ".vshtml");

		Set<String> keys = param.keySet();
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {
			String temp = iter.next();
			tempurl.append("&" + temp + "=" + param.get(temp));
		}

		final String url = tempurl.toString().replaceFirst("&", "?");
		if (!StringUtil.isNullOrEmpty(title)) {
			AlertWaitService.getInstance().startWait(title);
		}
		writeSendUrl(operaType, url);
		HandlerThreadNew.newThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (SysApplicationImpl.getInstance().isTest()) {
						forTest(activity, res, operaType, title);
					} else {
						sendHttpclientRequest(activity, res, operaType, title,
								url);

					}
				} catch (Exception e) {
					Toaster.getInstance().displayToast(e.getMessage());
					AlertWaitService.getInstance().forceEndAlertWait();
					invokeError(res, e);
					if (SysApplicationImpl.getInstance().isDebug()) {
						e.printStackTrace();
					}
					return;
				}
			}
		});

	}

	private void writeSendUrl(String operaType, String url) {
		if (SysApplicationImpl.getInstance().isDebug()) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy年MM月dd日HH时mm分ss秒SSS");
			String tempTime = format.format(new Date());
			String fileName = operaType + "_" + tempTime + "_send";
			File tempDir = new File(FileUtil.ROOT_DIE, "log");
			if (!tempDir.exists()) {
				tempDir.mkdir();
			}
			File tempFile = new File(tempDir, fileName);

			try {
				FileUtil.byteToFile(url.getBytes(), tempFile.getPath());
			} catch (Exception e) {
				AlertWaitService.getInstance().forceEndAlertWait();
				if (SysApplicationImpl.getInstance().isDebug()) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean checkToken(Activity activity) {
		if (User.getToken()[0] == null
				|| User.getToken()[0].trim().length() < 0) {
			return false;
		}
		return true;
	}

	public void invokeRequest(final Activity activity, final String operaType,
			final File fielUpload, final HJResponse response) {

		StringBuilder tempurl = new StringBuilder(composeUrl());
		tempurl.append(operaType + ".vshtml?operatorid="
				+ User.getOperatorid()[0] + "&token=" + User.getToken()[0]);
		final String url = tempurl.toString();
		CLog.i("url=" + url);
		writeSendUrl(operaType, url);
		HandlerThreadNew.newThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (SysApplicationImpl.getInstance().isTest()) {
						forTest(activity, response, operaType, "正在上传");
					} else {
						fileUpload(activity, operaType, fielUpload, response,
								url, "正在上传");
					}

				} catch (Exception e) {
					if (SysApplicationImpl.getInstance().isDebug()) {
						e.printStackTrace();
					}
					AlertWaitService.getInstance().forceEndAlertWait();
					invokeError(response, e);
					Toaster.getInstance().displayToast(e.getMessage());
				}
			}
		});

	}

	public void sendHttpclientRequest(final Activity activity,
			final HJResponse response, final String operaType,
			final String title, final String url) throws Exception {
		HttpResponse httpResponse = null;
		String content = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity s = new StringEntity(url, HTTP.UTF_8);
			s.setContentType("text/xml charset=utf-8");
			httpPost.setEntity(s);

			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			httpResponse = httpclient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new Exception(httpResponse.getStatusLine().toString());
			}

			String charset = EntityUtils.getContentCharSet(httpResponse
					.getEntity());// 获得的是utf-8
			content = EntityUtils.toString(httpResponse.getEntity(), charset);
			CLog.i(response.getDescribe() + ">>>_" + operaType + "____>>>"
					+ content);
		} catch (Exception e) {
			throw new Exception("请检查网络");
		}
		try {
			createSendReuest(operaType, content);
			parseJsonResult(activity, response, content, title);
		} catch (Exception e) {
			throw new Exception(e.getMessage());

		}

	}

	/**
	 * 读取流
	 * 
	 * @param is
	 *            InputStream
	 * @return 字节数组
	 * @throws Exception
	 *             Exception
	 */
	public String readStream(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"), 1024 * 1024 * 5);
		String temp = "";
		StringBuffer resultBuilder = new StringBuffer("");
		while ((temp = reader.readLine()) != null) {
			resultBuilder.append(temp);
			CLog.i(">>>>>>" + temp);
		}

		reader.close();
		is.close();
		CLog.i(resultBuilder.toString());
		return resultBuilder.toString();
	}

	private void createSendReuest(String operaType, String content) {
		if (SysApplicationImpl.getInstance().isDebug()) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy年MM月dd日HH时mm分ss秒SSS");
			String tempTime = format.format(new Date());
			String fileName = operaType + "_" + tempTime + "_Receive";
			File tempDir = new File(FileUtil.ROOT_DIE, "log");
			if (!tempDir.exists()) {
				tempDir.mkdir();
			}
			File tempFile = new File(tempDir, fileName);

			try {
				FileUtil.byteToFile(content.getBytes(), tempFile.getPath());
			} catch (Exception e) {
				AlertWaitService.getInstance().forceEndAlertWait();
				if (SysApplicationImpl.getInstance().isDebug()) {
					e.printStackTrace();
				}
			}
		}
	}

	public void parseJsonResult(final Activity activity,
			final HJResponse hjResponse, String mContent, String title)
			throws Exception {
		if (!StringUtil.isNullOrEmpty(title)) {
			AlertWaitService.getInstance().forceEndAlertWait();
		}
		try {
			JSONObject result = new JSONObject(mContent);
			hjResponse.setHead(result.getJSONObject("head"));
			hjResponse.setResult(result.getJSONObject("head").getString(
					"result"));
			hjResponse.setErrorCode(result.getJSONObject("head").getString(
					"errorCode"));
			hjResponse.setErrorMessage(result.getJSONObject("head").getString(
					"errorMessage"));
			hjResponse.setBody(result.getJSONObject("body"));
			hjResponse.setSign(result.getString("sign"));
		} catch (Exception e) {
			throw new Exception("数据格式错误");
		}
		if (!hjResponse.getResult().equals("success")) {
			if (hjResponse.getErrorCode().equals("E000106")) {
				SysApplicationImpl.jumpToLogin(activity);
				throw new Exception("登录超时");
			}
			throw new Exception(hjResponse.getErrorMessage());
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				hjResponse.response(hjResponse);
			}
		});

	}

	public void forTest(Activity context, HJResponse rosponse,
			String operatorType, String title) {

		BufferedInputStream buff;
		try {
			buff = new BufferedInputStream(context.getResources().getAssets()
					.open(operatorType + ".vshtml"));

			byte[] temp = new byte[9048];
			int i = 0;
			String content = "";
			while ((i = buff.read(temp)) != -1) {
				content = content + new String(temp, 0, i);
			}
			parseJsonResult(context, rosponse, content, title);
		} catch (Exception e) {
			if (SysApplicationImpl.getInstance().isDebug()) {
				e.printStackTrace();
			}
			Toaster.getInstance().displayToast(e.getMessage());
		}
	}

	public void fileUpload(final Activity activity, final String operaType,
			File fielUpload, final HJResponse response, String url,
			final String title) throws Exception {

		try {
			final byte[] b;
			AlertWaitService.getInstance().startWait(title, "0");
			try {
				URL fileUrl = new URL(url);
				HttpURLConnection con = (HttpURLConnection) fileUrl
						.openConnection();
				con.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
				con.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;

				// 使用struts2必须设置Content-type才可以，否则action中的request获取不到值。但是servlet不需要设置
				con.setRequestProperty("Content-type",
						"application/x-java-serialized-object");
				con.setRequestMethod("POST");
				con.setUseCaches(false);
				con.setInstanceFollowRedirects(true);

				byte[] buffer = new byte[102400];
				CLog.i("buffer.length==" + buffer.length);
				con.setChunkedStreamingMode(buffer.length);
				con.connect();
				OutputStream out = con.getOutputStream();
				FileInputStream fin = new FileInputStream(fielUpload);
				long progress = 0;
				CLog.i("fielUpload==" + fielUpload.getPath());
				int length = 0;
				while ((length = fin.read(buffer)) != -1) {
					out.write(buffer, 0, length);
					out.flush();
					progress = progress + length;
					AlertWaitService.getInstance().updateProgress(
							String.valueOf(progress),
							String.valueOf((int) fielUpload.length()));
				}
				System.gc();
				progress = 0;

				out.close();
				InputStream in = con.getInputStream();
				b = new byte[in.available()];
				in.read(b);
				con.disconnect();
				FileUtil.closeStream(in);
				FileUtil.closeStream(fin);
			} catch (Exception e) {
				if (SysApplicationImpl.getInstance().isDebug()) {
					e.printStackTrace();
				}
				throw new Exception("请检查网络");
			}

			parseJsonResult(activity, response, new String(b), title);

			if (fielUpload.exists()
					&& !SysApplicationImpl.getInstance().isDebug()) {
				fielUpload.delete();
			}

		} catch (Exception e) {
			if (SysApplicationImpl.getInstance().isDebug()) {
				e.printStackTrace();
			}
			throw new Exception(e.getMessage());
		}

	}

	public void invokeError(HJResponse response, String obj) {
		response.onError(obj);
	}

	public void invokeError(HJResponse response, Throwable obj) {
		if (obj.getMessage() != null && !obj.getMessage().equals("")) {
			response.onError(obj.getMessage());
		} else {
			response.onError("Error");
		}
	}

	public static boolean isNetworkAvailable(Context con) {
		boolean result = false;
		ConnectivityManager cm = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo netinfo = cm.getActiveNetworkInfo();
			if (netinfo != null && netinfo.isConnected()) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		if (!result) {
			AlertWaitService.getInstance().forceEndAlertWait();
			Toaster.getInstance().displayToast("请检查网络");
		}
		return result;
	}
}
