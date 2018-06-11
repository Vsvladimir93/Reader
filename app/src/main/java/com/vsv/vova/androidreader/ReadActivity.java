package com.vsv.vova.androidreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

public class ReadActivity extends AppCompatActivity {

    PDFView pdfView;
    TextView currentPageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Intent intent = getIntent();
        currentPageView = (TextView) findViewById(R.id.currentPageView);





        pdfView = (PDFView) findViewById(R.id.pdfView);

        pdfView.fromUri(Uri.parse(intent.getStringExtra("uri"))).load();

/*Выводить текущую страницу

        Integer currentPage = pdfView.getCurrentPage();

        currentPageView.setText(currentPage.toString());
*/


    }
}
