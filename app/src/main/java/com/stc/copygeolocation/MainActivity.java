package com.stc.copygeolocation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.regex.Matcher;

import static android.util.Patterns.WEB_URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "copygeolocation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = (TextView)this.findViewById(R.id.tv);
        Intent intent = getIntent();
        String action = intent.getAction();
        if(0 == action.compareTo("android.intent.action.MAIN")){
            t.setText( intent.toString() );
        } else {
            boolean bGetlocation = false;
            Uri u = intent.getData();
            Log.d(TAG, "u = " + u);
            String scheme = u.getScheme();
            String q = u.getSchemeSpecificPart();
            Log.d(TAG, "q = " + q);
            String location = "";
            if(0 == scheme.compareTo("google.navigation")){
                location = q.substring(q.indexOf("q=")+2, q.indexOf("&"));
                bGetlocation = true;
            }else if(0 == scheme.compareTo("line")){
                Matcher m = WEB_URL.matcher(q);
                boolean result = m.find();
                if(result) {
                    String url=m.group(1);
                    Uri uu = Uri.parse(url);
                    location = String.format("%s,%s", uu.getQueryParameter("lat"), uu.getQueryParameter("lng"));
                    bGetlocation = true;
                }else{
                    t.setText(q);
                }
            }else{
                t.setText( intent.toString() );
            }
            if(bGetlocation) {
                Log.d(TAG, "location = " + location);
                t.setText(location);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("location",location);
                clipboard.setPrimaryClip(clip);
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
