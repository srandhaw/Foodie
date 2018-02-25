package edu.purdue.srandhaw.foodie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView wv = (WebView)findViewById(R.id.wv);
        String url = getIntent().getStringExtra("url");
        wv.loadUrl(url);
    }
}
