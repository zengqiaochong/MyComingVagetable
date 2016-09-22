package com.caomei.comingvagetable.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.bean.vegedata.VegeCartBean;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import de.greenrobot.event.EventBus;

/** 
 * 
 */
public class NetUtil {

	private static Context context = null;

	private HttpClient client = null;
	private HttpGet request = null;
	private HttpResponse response = null;

	public static int TIME_OUT = 6000;

	public static int NINETON_TIME_OUT = 6000;

	public static int THINKPAD_TIME_OUT = 6000;

	private AccessNetState netState = AccessNetState.Success;

	private Object lock = new Object();

	public static NetUtil getInstance(Context mContext) {
		context = mContext;
		return new NetUtil();
	}

	/**
	 * 
	 * @param httpURL
	 * @param timeout
	 * @return
	 */
	public AccessNetResultBean getDataFromNetByGet(String httpURL, int timeout) {
		httpURL = httpURL.replace("https", "http");
		AccessNetResultBean bean = new AccessNetResultBean();

		/**
		 * HttpClient
		 */
		client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, timeout); //
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				timeout); // ��ȡ��ʱ

		request = new HttpGet(httpURL);
		request.addHeader("Accept-Encoding", "gzip");
		response = null;

		Reader responseReader = null;
		BufferedReader reader = null;

		Thread httpThread = new Thread() {
			@Override
			public void run() {
				try {
					response = client.execute(request);
				} catch (ConnectTimeoutException e) {
					e.printStackTrace();
					netState = AccessNetState.ConnectTimeoutException;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					netState = AccessNetState.ClientProtocolException;
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("NetUtils:---------->IOException:"
							+ e.toString());
				} catch (Exception e) {
					e.printStackTrace();
					Log.v("XGService", e.getMessage(), e.getCause());
					netState = AccessNetState.Exception;
				}
			}
		};
		httpThread.start();

		try {
			int sleepTime = 0;
			while ((sleepTime + 50 < timeout + 50) && response == null) {
				Thread.sleep(50);
				sleepTime += 50;
			}
			if (response == null && netState == AccessNetState.Success) {
				httpThread.interrupt();
				netState = AccessNetState.ConnectTimeoutException;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			if (response != null) {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() == 200) {
					String results = getJsonStringFromGZIP(response);
					bean.setResult(results);
					netState = AccessNetState.Success;

				} else {
					netState = AccessNetState.ErrorResponse;
				}
			}
		} catch (Exception e) {
			netState = AccessNetState.Exception;
		} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (responseReader != null) {
					responseReader.close();
					responseReader = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		bean.setState(netState);
		return bean;
	}

	/**
	 * 封装的能够解析各种返回类型的网络请求方法
	 * 
	 * @param url
	 * @param successCode
	 * @param errorCode
	 */

	public <T> void requestData(final String url, int successCode,
			int errorCode, T bean) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(context)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						bean = new Gson().fromJson(bean.getResult(),
								bean.getClass());
					} catch (Exception ex) {

					}
				}
			}
		}).start();
	}

	public AccessNetResultBean getDataFromNetByHttpsGet(String httpURL,
			int timeout) {

		AccessNetResultBean bean = new AccessNetResultBean();
		/**
		 * HttpClient
		 */
		client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				timeout);

		request = new HttpGet(httpURL);
		request.addHeader("Accept-Encoding", "gzip");
		response = null;

		Reader responseReader = null;
		BufferedReader reader = null;

		Thread httpThread = new Thread() {

			@Override
			public void run() {
				try {
					response = client.execute(request);
				} catch (ConnectTimeoutException e) {
					e.printStackTrace();
					netState = AccessNetState.ConnectTimeoutException;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					netState = AccessNetState.ClientProtocolException;
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("NetUtils:---------->IOException:"
							+ e.toString());
				} catch (Exception e) {
					e.printStackTrace();
					Log.v("XGService", e.getMessage(), e.getCause());
					netState = AccessNetState.Exception;
				}
			}

		};
		httpThread.start();

		try {
			int sleepTime = 0;
			while ((sleepTime + 50 < timeout + 50) && response == null) {
				Thread.sleep(50);
				sleepTime += 50;
			}
			if (response == null && netState == AccessNetState.Success) {
				httpThread.interrupt();
				netState = AccessNetState.ConnectTimeoutException;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			if (response != null) {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() == 200) {
					String results = getJsonStringFromGZIP(response);
					bean.setResult(results);
					netState = AccessNetState.Success;

				} else {
					netState = AccessNetState.ErrorResponse;
				}
			}
		} catch (Exception e) {
			netState = AccessNetState.Exception;
		} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (responseReader != null) {
					responseReader.close();
					responseReader = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		bean.setState(netState);
		return bean;
	}

	public AccessNetResultBean getDataFromNetByGet(String httpURL) {

		return getDataFromNetByGet(httpURL, TIME_OUT);
	}

	public String getDataFromNetByPost(String httpURL,
			ArrayList<NameValuePair> params) {
		return getDataFromNetByPost(httpURL, params, TIME_OUT);
	}

	public String getDataFromNetByPost(String httpURL,
			ArrayList<NameValuePair> params, int time_out) {
		HttpPost httpPost = new HttpPost(httpURL);
		HttpResponse httpResponse = null;
		String result = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				result = EntityUtils.toString(httpResponse.getEntity());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static final long STATE_DOWNLOAD_FILE_SUCCESS = 1;
	public static final long STATE_DOWNLOAD_FILE_ERR = 2;

	private static InputStream inStream = null;
	private static FileOutputStream fs = null;

	private static boolean isDownFileCancel = false;

	public static long downloadNet(String urlString, String fileDir,
			String fileName) {
		int byteread = 0;
		inStream = null;
		fs = null;
		isDownFileCancel = false;
		try {
			URL url = new URL(urlString);
			File file = new File(fileDir);
			if (!file.exists()) {
				// file.mkdir();
				file.mkdirs();
			}
			String filePath = fileDir + File.separator + fileName;
			System.out.println("file path :" + filePath);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(2 * 60 * 1000);
			conn.setReadTimeout(2 * 60 * 1000);
			inStream = conn.getInputStream();
			File filetem = new File(filePath);
			if (!filetem.exists()) {
				filetem.createNewFile();
			}
			fs = new FileOutputStream(filePath);

			if (!isDownFileCancel) {
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
					fs.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return STATE_DOWNLOAD_FILE_ERR;
		} finally {
			try {
				if (fs != null) {
					fs.close();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (Exception e2) {
			}
		}
		return STATE_DOWNLOAD_FILE_SUCCESS;
	}

	public static boolean shutdownNet() {
		try {
			isDownFileCancel = true;
			if (fs != null) {
				fs.close();
				fs = null;
			}
			if (inStream != null) {
				inStream.close();
				inStream = null;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * ״̬
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean getNetworkState(Context mContext) {

		ConnectivityManager conManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null) {
			if (!networkInfo.isAvailable()) {
				Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
			}
			return networkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean getNetworkState_00(Context mContext) {

		try {
			ConnectivityManager connectivity = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {

					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * ���WIFI״̬
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiNetworkInfo.isConnected();
	}

	private int getShort(byte[] data) {
		return (data[0] << 8) | data[1] & 0xFF;
	}

	private String getJsonStringFromGZIP(HttpResponse response) {
		String jsonString = null;
		try {
			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			bis.mark(2);
			// ȡǰ�����ֽ�
			byte[] header = new byte[2];
			int result = bis.read(header);
			// reset����������ʼλ��
			bis.reset();
			// �ж��Ƿ���GZIP��ʽ
			int headerData = getShort(header);
			// Gzip �� ��ǰ�����ֽ��� 0x1f8b
			if (result != -1 && headerData == 0x1f8b) {
				Log.i("ss", " use GZIPInputStream  ");
				is = new GZIPInputStream(bis);
			} else {
				Log.i("ss", " not use GZIPInputStream");
				is = bis;
			}
			InputStreamReader reader = new InputStreamReader(is, "utf-8");
			char[] data = new char[100];
			int readSize;
			StringBuffer sb = new StringBuffer();
			while ((readSize = reader.read(data)) > 0) {
				sb.append(data, 0, readSize);
			}
			jsonString = sb.toString();
			bis.close();
			reader.close();
		} catch (Exception e) {
			Log.i("ss", e.toString(), e);
		}

		return jsonString;
	}

	public static void initWebView(Context context, WebView webView) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		WebSettings settings = webView.getSettings();
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setDomStorageEnabled(true);
		settings.setAppCacheEnabled(true);
		settings.setAppCachePath(context.getDatabasePath("cache-webview")
				.getAbsolutePath());
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(false);
		settings.setSupportZoom(false);
		settings.setSupportMultipleWindows(true);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
		webView.getSettings().setUserAgentString("User-Agent");
		webView.setWebChromeClient(new WebChromeClient() {
		});
	}

	public static String requestHTTPSPage(Context context, String mUrl) {
		InputStream ins = null;
		String result = "";
		try {
			ins = context.getAssets().open("my.key"); // ���ص�֤��ŵ���Ŀ�е�assetsĿ¼��
			CertificateFactory cerFactory = CertificateFactory
					.getInstance("X.509");
			Certificate cer = cerFactory.generateCertificate(ins);
			KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);

			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
			Scheme sch = new Scheme("https", socketFactory, 443);
			HttpClient mHttpClient = new DefaultHttpClient();
			mHttpClient.getConnectionManager().getSchemeRegistry()
					.register(sch);

			BufferedReader reader = null;
			try {
				HttpGet request = new HttpGet();
				request.setURI(new URI(mUrl));
				HttpResponse response = mHttpClient.execute(request);
				if (response.getStatusLine().getStatusCode() != 200) {
					request.abort();
					return result;
				}

				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				result = buffer.toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void getCartData(final Context mContext) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_GET_CART_VEGE, 0, 10000, 1, ShareUtil.getInstance(mContext).getUserId());
				Log.e("url", "获取购物车数据  " + url);
				AccessNetResultBean bean = NetUtil.getInstance(mContext).getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						VegeCartBean vcBean = new Gson().fromJson(bean.getResult(), VegeCartBean.class);
						EventBus.getDefault().post(new EventMsg(OpCodes.CART_DATA_GET_DONE, vcBean));
					} catch (Exception ex) {
						EventBus.getDefault().post(new EventMsg(OpCodes.CART_DATA_GET_ERROR, ex.getMessage()));
					}
				} else {
					EventBus.getDefault().post(new EventMsg(OpCodes.CART_DATA_GET_ERROR, "获取购物车数据失败"));
				}
			}
		}).start();
	}

	public static void getD2eliverFee(final Context mContext) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = CommonAPI.BASE_URL
						+ String.format(CommonAPI.URL_GET_CONFIG,
								"DELIVER_FEE", ShareUtil.getInstance(mContext)
										.getUserId());
				Log.e("url", "获取配送费  " + url);
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						String res = bean.getResult();
						EventBus.getDefault().post(
								new EventMsg(OpCodes.DEVEL_FEE_GET_DONE, res));
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.DEVEL_FEE_GET_ERROR, ex
										.getMessage()));
					}
				} else {
					EventBus.getDefault()
							.post(new EventMsg(OpCodes.DEVEL_FEE_GET_ERROR,
									"获取配送费失败"));
				}
			}
		}).start();
	}

	/**
	 * 获取配置项的统用接口
	 * 
	 * @param mContext
	 * @param key
	 */
	public static void getCommonConfig(final Context mContext, final String key) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = CommonAPI.BASE_URL
						+ String.format(CommonAPI.URL_GET_CONFIG, key,
								ShareUtil.getInstance(mContext).getUserId());
				Log.e("url", "获取配置的通用接口  " + url);
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						String res = bean.getResult();
						EventBus.getDefault().post(
								new EventMsg(OpCodes.DEVEL_FEE_GET_DONE, res));
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.DEVEL_FEE_GET_ERROR, ex
										.getMessage()));
					}
				} else {
					EventBus.getDefault()
							.post(new EventMsg(OpCodes.DEVEL_FEE_GET_ERROR,
									"获取配送费失败"));
				}
			}
		}).start();
	}
	public static final String BOUNDARY = "--my_boundary--";
	public static void writeStringParams(Map<String, String> textParams,
			DataOutputStream ds) throws Exception {

		Set<String> keySet = textParams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String value = textParams.get(name);
			ds.writeBytes("--" + BOUNDARY + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name
					+ "\"\r\n");
			ds.writeBytes("\r\n");
			value = value + "\r\n";
			ds.write(value.getBytes());

		}

	}
	/** 
	 * @param fileparams
	 * @param ds
	 * @throws Exception
	 */
	public static void writeFileParams(Map<String, File> fileparams, 
			DataOutputStream ds) throws Exception {
		Set<String> keySet = fileparams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			File value = fileparams.get(name);
			ds.writeBytes("--" + BOUNDARY + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\""
					+ URLEncoder.encode(value.getName(), "UTF-8") + "\"\r\n");
			ds.writeBytes("Content-Type:application/octet-stream \r\n");
			ds.writeBytes("\r\n");
			ds.write(getBytes(value));
			ds.writeBytes("\r\n");
		}
	}
	private static byte[] getBytes(File f) throws Exception {
		FileInputStream in = new FileInputStream(f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int n;
		while ((n = in.read(b)) != -1) {
			out.write(b, 0, n);
		}
		in.close();
		return out.toByteArray();
	}
	
	public static void paramsEnd(DataOutputStream ds) throws Exception {
		ds.writeBytes("--" + BOUNDARY + "--" + "\r\n");
		ds.writeBytes("\r\n");
	}
}