package com.opentok.android.demo.opentoksamples;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.opentok.android.demo.opentokhelloworld.ContactActivity;
import com.opentok.android.demo.opentokhelloworld.MapActivity;
import com.opentok.android.demo.opentokhelloworld.R;

/**
 * Quiosco Application built using OpenTok Android SDK. 
 */
public class OpenTokSamples extends Activity implements OnClickListener {
	Button chat_button;
	TextView session_text;
	TextView token_text;
	  //URL to get JSON Array
	  private static String url = "http://intense-waters-5600.herokuapp.com/connection";
	  //JSON Node Names
	  private static final String TAG_CON = "con";
	  private static final String TAG_SESSION = "session";
	  private static final String TAG_TOKEN = "token"; 

	  JSONArray connArray = null;
	private static final String LOGTAG = "demo-opentok-sdk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);
		
		/* Initialize all views */
		
		chat_button =  (Button) findViewById(R.id.chat_button);
		chat_button.setOnClickListener((android.view.View.OnClickListener) this);




        // Disable screen dimming
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  
    }
	
	
	/* Method to control button interactions */
	public void onClick(View view){
		switch (view.getId()){
		case R.id.chat_button:
			//new JSONParse().execute();
			startVideoActivity();
			break;

		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume(); 
    }
   
    /**
     * Starts the Hello-World app with UI. See OpenTokUI.java
     */
    public void startVideoActivity() {

        Log.i(LOGTAG, "starting video chat activity");

        Intent intent = new Intent(OpenTokSamples.this, UIActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    

    /**
     * Starts the Hello-World app using subclassing. See
     * OpenTokVideoSubclassing.java
     */
    public void startTextAndVideoActivity() {

        Log.i(LOGTAG, "starting text and video activity");

        Intent intent = new Intent(OpenTokSamples.this,
                MultipartyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    
    /**
     * Starts the Hello-World app with UI. See OpenTokUI.java
     */
    public void startMapActivity() {

        Log.i(LOGTAG, "starting map activity");

        Intent intent = new Intent(OpenTokSamples.this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    
    public void startContactActivity(){
    	
    	Intent intent = new Intent(OpenTokSamples.this, ContactActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    	
    }
   private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
       @Override
         protected void onPreExecute() {
             super.onPreExecute();

             pDialog = new ProgressDialog(OpenTokSamples.this);
             pDialog.setMessage("Getting Data ...");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
             pDialog.show();
       }
       @Override
         protected JSONObject doInBackground(String... args) {
         JSONParser jParser = new JSONParser();
         // Getting JSON from URL
         JSONObject json = jParser.getJSONFromUrl(url);
         return json;
       }
        @Override
          protected void onPostExecute(JSONObject json) {
          pDialog.dismiss();
          try {
              Log.i("test", "works up to here");

             // Getting JSON Array
             connArray = json.getJSONArray(TAG_CON);
             JSONObject c = connArray.getJSONObject(0);
             // Storing  JSON item in a Variable
             String session_data = c.getString(TAG_SESSION);
             String token_data = c.getString(TAG_TOKEN);
             //String name = c.getString(TAG_NAME);
             //String email = c.getString(TAG_EMAIL);
             //Set JSON Data in TextView
             //Set session and token text for 
             session_text.setText(session_data);
             token_text.setText(token_data);
             //name1.setText(name);
             //email1.setText(email);
         } catch (JSONException e) {
           e.printStackTrace();
         }
        }
     }
   
   
}


