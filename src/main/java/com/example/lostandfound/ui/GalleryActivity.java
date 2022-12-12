package com.example.lostandfound.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lostandfound.R;
import com.example.lostandfound.ui.apicall.GetDBItem;
import com.example.lostandfound.ui.apicall.GetDBItems;

public class GalleryActivity extends AppCompatActivity {
    String getLogsURL;
    //int name;
    String name;

    //    private TextView textView_Date1;
//    private TextView textView_Date2;
    final static String TAG = "AndroidAPITest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //Intent intent = getIntent();
        getLogsURL ="https://r9eh795567.execute-api.ap-northeast-2.amazonaws.com/lostItems" ;
        //intent.getStringExtra("getLogsURL");
        Log.i(TAG, "getLogsURL="+getLogsURL);

        Intent intent = getIntent();
        //name = intent.getIntExtra("name",-1);
        name = intent.getStringExtra("name");
        Log.i("n", "name="+name);
        //

        Button start = findViewById(R.id.log_start_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetDBItem(GalleryActivity.this,getLogsURL,name).execute();
                //new GetDBItems(GalleryActivity.this,getLogsURL).execute();

            }
        });
    }
}