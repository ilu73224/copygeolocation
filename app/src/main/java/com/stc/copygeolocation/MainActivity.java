package com.stc.copygeolocation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = (TextView)this.findViewById(R.id.tv);
        Intent intent = getIntent();
        String action = intent.getAction();
        if(0 == action.compareTo("android.intent.action.MAIN")){
            t.setText( intent.toString() );
        }else {
            Uri u = intent.getData();
            String scheme = u.getScheme();
            if(0 == scheme.compareTo("google.navigation")){
                String q = u.getSchemeSpecificPart();
                String location = q.substring(q.indexOf("q=")+2, q.indexOf("&"));
                t.setText(location);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("location",location);
                clipboard.setPrimaryClip(clip);
            }else if(0 == scheme.compareTo("line")){
                String q = u.getSchemeSpecificPart();
                //https://pkget.com/?25.0564439002806&lng=121.5483734035&g=2
                String lat = q.substring(q.indexOf("lat=")+4, q.indexOf("&lng="));
                String lng = q.substring(q.indexOf("lng=")+4, q.indexOf("&g="));
                String location = String.format("%s,%s", lat, lng);
                t.setText(location);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("location",location);
                clipboard.setPrimaryClip(clip);
            }else{
                t.setText( intent.toString() );
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finishAndRemoveTask();
            }
        }, 1500);
    }
}
