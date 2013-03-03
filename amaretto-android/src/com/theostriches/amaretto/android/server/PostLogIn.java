package com.theostriches.amaretto.android.server;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.theostriches.amaretto.android.model.User;
import com.theostriches.amaretto.android.util.Constant;
import com.theostriches.amaretto.android.util.Log;

/**
 * @author Antonio Prada <toniprada@gmail.com>
 * 
 * 
 */
public class PostLogIn extends Thread {

	public static final int CODE_OK = 303;
	public static final int CODE_BADAUTH = 320;
	public static final int CODE_ERROR = 351;

	private Handler mHandler;
	private String user;
	private String passHasH;
	private boolean newUser;

	private boolean done = false;

	public PostLogIn(Handler handler, String user, String passHash, boolean newUser) {
		this.mHandler = handler;
		this.user  = user;
		this.passHasH = passHash;
		this.newUser =  newUser;
	}

	@Override
	public void run() {
		try {			
			Map<String, String> data = new HashMap<String, String>();
			data.put("username", user);
			data.put("password", passHasH);
			if (newUser) {
				int code = HttpRequest.post(Constant.SERVER_URL + "/api/user").form(data).code();
				if (code == 200) {
					User u = new User(user, passHasH);
					mHandler.sendMessage(Message.obtain(mHandler, CODE_OK, u));
				} else {
					sendMessage(CODE_ERROR);
				}
			} else {
				int code = HttpRequest.post(Constant.SERVER_URL + "/login").form(data).code();
				if (code == 200) {
					User u = new User(user, passHasH);
					mHandler.sendMessage(Message.obtain(mHandler, CODE_OK, u));
				} else if (code == 215) {
					sendMessage(CODE_BADAUTH);
				} else {
					sendMessage(CODE_ERROR);
				}
			}
			
//			
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpPost httppost = new HttpPost(Constant.SERVER_URL + "/new_order.php");
//			// Add your data
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//			nameValuePairs.add(new BasicNameValuePair("id_table", "" + order.getTable().getId_table()));
//			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			// Execute HTTP Post Request
//			String jsonProducts = gson.toJson(order); 
//			Log.w("PostNewOrder JSON:" + jsonProducts);
//			StringEntity sEntity = new StringEntity(jsonProducts, "UTF-8");
//			httppost.setEntity(sEntity);
//			HttpResponse response = httpclient.execute(httppost);
//			String responseString = EntityUtils.toString(response.getEntity());
//			Log.w("PostNewOrder RESPONSE:"+ responseString);
//			ResponseREST responseRest = gson.fromJson(responseString, ResponseREST.class);
//			mHandler.sendMessage(Message.obtain(mHandler, CODE_OK, responseRest));
//		} catch (MalformedURLException mfe) {
//			Log.e("Malformed url exception: " + mfe.getMessage());
//			sendMessage(CODE_ERROR);
//		} catch (IOException ie) {
//			Log.e("No connection?: " + ie.getMessage());
//			sendMessage(CODE_ERROR);
		} catch (Exception e) {
			Log.e("other exception, maybe encrypt: " + e.getMessage());
			sendMessage(CODE_ERROR);
		}
	}

	private void sendMessage(int code) {
		if (!done) {
			done = true;
			mHandler.sendEmptyMessage(code);
		}
	}

}
