package com.opentok.android.demo.opentoksamples;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.opentok.android.demo.multiparty.MySession;
import com.opentok.android.demo.opentokhelloworld.R;

public class MultipartyActivity extends Activity {

	private static final String LOGTAG = "demo-subclassing";
	private MySession mSession;
	EditText mMessageEditText;

	private boolean resumeHasRun = false;

	private NotificationCompat.Builder mNotifyBuilder;
	NotificationManager mNotificationManager;
	private int notificationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.room);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		mSession = new MySession(this);

		mMessageEditText = (EditText) findViewById(R.id.message);

		ViewGroup preview = (ViewGroup) findViewById(R.id.preview);
		mSession.setPreviewView(preview);

		mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        ViewPager playersView = (ViewPager)findViewById(R.id.pager);
        mSession.setPlayersViewContainer(playersView);
        mSession.setMessageView((TextView)findViewById(R.id.messageView), (ScrollView)findViewById(R.id.scroller));
        mSession.connect();
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

    @Override
    public void onPause() {
    	super.onPause();
    		     
        if ( mSession != null ){
        	mSession.onPause();
        }
        
        mNotifyBuilder = new NotificationCompat.Builder(this)
        .setContentTitle(this.getTitle())
        .setContentText(getResources().getString(R.string.notification))
        .setSmallIcon(R.drawable.ic_launcher).setOngoing(true);
        
		Intent notificationIntent = new Intent(this, MultipartyActivity.class);
	    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	    
	    mNotifyBuilder.setContentIntent(intent);
	    notificationId = (int) System.currentTimeMillis();
        mNotificationManager.notify(
        		notificationId,
                mNotifyBuilder.build());
    }

    @Override
    public void onResume() {
       super.onResume();
       
       if (!resumeHasRun) {
           resumeHasRun = true;
           return;
       }
       else {
    	   if ( mSession != null ){
    		   mSession.onResume();
           }
       }
       mNotificationManager.cancel(notificationId);
    }

    @Override
   	public void onBackPressed() {  		
        if (mSession != null) {
        	mSession.disconnect();
        }
        super.onBackPressed();
   	}
    
    @Override
	public void onStop() {
	    super.onStop();
	
	    if (isFinishing()) {
	    	mNotificationManager.cancel(notificationId);
	        if (mSession != null) {
	            mSession.disconnect();
	        }
	    }
	}

	public void onClickSend(View v) {
    	Log.i(LOGTAG, "Sending a chat message");
    	mSession.sendChatMessage(mMessageEditText.getText().toString());
        mMessageEditText.setText("");
    }
}
