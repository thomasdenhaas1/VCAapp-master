package nl.kimplusdelta.vca.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nl.kimplusdelta.vca.MyApplication;
import nl.kimplusdelta.vca.R;

public class SplashActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();
    private boolean starting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        }, 3000);

//        findViewById(R.id.contentHolder).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication)getApplication()).trackScreen("Splash");

    }

    @Override
    protected void onPause() {
        super.onPause();

        ((MyApplication)getApplication()).trackScreenTime();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void startApp() {
        if(starting) {
            return;
        }

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();

        starting = false;
    }
}
