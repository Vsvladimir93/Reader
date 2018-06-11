package com.vsv.vova.androidreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnFind;
    public static final int READ_REQUEST_CODE = 42;
    private static Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFind = (Button) findViewById(R.id.button);
        btnFind.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData ){
        if(requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK){
            uri = null;
            if(resultData != null){
                uri = resultData.getData();
                Intent intent = new Intent(this, ReadActivity.class);
                intent.putExtra("uri", uri.toString());
                startActivity(intent);
            }
        }
    }

}
