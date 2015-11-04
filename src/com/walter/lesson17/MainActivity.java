package com.walter.lesson17;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText edtNames, edtQty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		edtNames = (EditText) findViewById(R.id.editText1);
		edtQty = (EditText) findViewById(R.id.editText2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void save(View v) {
		String names = edtNames.getText().toString().trim();
		String qty = edtQty.getText().toString().trim();
		if (names.length() > 7 && qty.length() > 0) {
			Integer quantity = Integer.parseInt(qty);
			Database db = new Database(this);
			db.save(names, quantity);
			Toast.makeText(this, "Count " + db.count(), Toast.LENGTH_SHORT).show();
			edtQty.setText("");
			edtNames.setText("");
		} else 
		{
			Toast.makeText(this, "Invalid Values", Toast.LENGTH_SHORT).show();
		}

	}

	public void sync(View v) {
		String url="http://10.0.2.2/lesson17/sync.php";
		final Database db=new Database(this);
		ArrayList<Product> array=db.fetch();
		Gson gson=new Gson();
		String data = gson.toJson(array);
		Log.d("DATA", data);
		RequestParams params=new RequestParams();
		params.put("json", data);
		AsyncHttpClient client=new AsyncHttpClient();
		client.post(url, params,new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] response) {
				String resp = new String(response);
				Log.d("RESPONSE", resp);
				try {
					JSONArray jarray= new JSONArray(resp);
					for (int i = 0; i < jarray.length(); i++) 
					{
					  String uid= jarray.getJSONObject(i).getString("uid");
					  String status= jarray.getJSONObject(i).getString("status");
					  db.update(uid, status);
					}
				}
				catch (JSONException e) 
				{
				 Log.e("JSON", "Error with Json String");
				}
				
				
			}			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                			
			}
		});
          
	}
	
	
	
	
	
	

}
