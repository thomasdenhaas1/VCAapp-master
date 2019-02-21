package nl.kimplusdelta.vca.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.kimplusdelta.vca.MyApplication;
import nl.kimplusdelta.vca.R;

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        ButterKnife.bind(this);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((MyApplication)getApplication()).trackScreen("FAQ");
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((MyApplication)getApplication()).trackScreenTime();
    }

    @OnClick(R.id.websiteButton)
    public void onLiteExamClicked() {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vca-examen-app.nl"));
            startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
