package com.ebapps.keyrecord;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class RecordActivity extends Activity {

	RecordView  recordView;
	DrawRecordThread mThread;
	Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record);
        
        // get handles to the RecordView from XML and the Tutorial thread.
        recordView = (RecordView)findViewById(R.id.record_view);
        mThread = recordView.getThread();
        
        mThread.setRecordState(DrawRecordThread.STATE_RUNNING);

        // look up the happy shiny button
        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new OnClickListener() {           

        	  @Override
        	  public void onClick(View v) 
        	  {
        		  // TODO: Launch Intent to send data via text message
        		  
        		  
        		  // TODO: Launch Intent to send data to other apps
        	  }    
        	});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record, menu);
        return true;
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mThread.setRecordState(DrawRecordThread.STATE_STOPPED);

    }

    @Override
    protected void onResume() {
        mThread.setRecordState(DrawRecordThread.STATE_RUNNING);
        super.onResume();
    
    }
    
}
