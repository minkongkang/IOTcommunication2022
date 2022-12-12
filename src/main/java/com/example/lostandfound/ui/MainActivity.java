package com.example.lostandfound.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lostandfound.R;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "AndroidAPITest";
    EditText thingShadowURL, getLogsURL;
    //String listThingsURL,thingShadowURL, getLogsURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 관리자 버튼
        Button listLogsBtn = findViewById(R.id.listLogsBtn);
        listLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                //intent.putExtra("admin",1 );
                intent.putExtra("name","admin");
                startActivity(intent);
            }
        });

        // 사용자버튼
        Button listUserLogsBtn = findViewById(R.id.listUserLogsBtn);
        listUserLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,GalleryActivity.class);
                //intent.putExtra("user",0 );
                intent.putExtra("name","user");
                //intent.putExtra("https://r9eh795567.execute-api.ap-northeast-2.amazonaws.com/lostItems", getLogsURL);
                //Log.i(TAG,getLogsURL);
                startActivity(intent);
            }
        });
    }
}