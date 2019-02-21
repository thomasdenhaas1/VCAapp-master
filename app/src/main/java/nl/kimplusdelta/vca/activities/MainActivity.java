package nl.kimplusdelta.vca.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.kimplusdelta.vca.MyApplication;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.activities.base.BillingActivity;
import nl.kimplusdelta.vca.billing.IabResult;
import nl.kimplusdelta.vca.interfaces.OnBillingListener;
import nl.kimplusdelta.vca.models.exam.Exam;

public class MainActivity extends BillingActivity implements OnBillingListener {

    @Bind(R.id.liteButton) LinearLayout mLiteButton;
    @Bind(R.id.bButton) LinearLayout mBasisButton;
    @Bind(R.id.volButton) LinearLayout mVOLButton;
    @Bind(R.id.vilButton) LinearLayout mVILButton;

    @Bind(R.id.bLabel) TextView mBasisExamLabel;
    @Bind(R.id.bLabelDescription) TextView mBasisExamDescriptionLabel;

    @Bind(R.id.volLabel) TextView mVOLExamLabel;
    @Bind(R.id.volLabelDescription) TextView mVolExamDescriptionLabel;

    @Bind(R.id.vilLabel) TextView mVILExamLabel;
    @Bind(R.id.vilLabelDescription) TextView mVilExamDescriptionLabel;

    // Analytics Tracker
    private Tracker mTracker;
    private String selectedSku;
    public int value;
    public int value1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.setTitle(R.string.main_title);
        }



        // assign callback
        this.mBillingListener = this;

        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mBasisExamLabel.setText(getString(R.string.main_menu_exam_normal,
                getString(R.string.version_vca_b)));
        mVOLExamLabel.setText(getString(R.string.main_menu_exam_normal,
                getString(R.string.version_vca_vol)));
        mVILExamLabel.setText(getString(R.string.main_menu_exam_normal,
                getString(R.string.version_vcu_vil)));


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             value = extras.getInt("key");
            //The key argument here must match that used in the other activity
        }

        SharedPreferences sp = getSharedPreferences("key1", Activity.MODE_PRIVATE);
            value1 = sp.getInt("key2", -1);

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
        ((MyApplication)getApplication()).trackScreen("Main");
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
        ((MyApplication)getApplication()).trackScreenTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {

            if (value == 51)
            {
                int login = 51;
                Intent in = new Intent(MainActivity.this, SettingsActivity.class);
                in.putExtra("login", login);
                startActivity(in);

            }

            else    {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }
            return true;
        } else if(id == R.id.action_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_faq) {
            Intent intent = new Intent(this, FAQActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.liteButton)
    public void onLiteExamClicked() {
        launchExam(Exam.KEY_LITE);
    }

    @OnClick(R.id.bButton)
    public void onBExamClicked() {
        startExam(Exam.KEY_BASIC);
    }

    @OnClick(R.id.volButton)
    public void onVOLExamClicked() {
        startExam(Exam.KEY_VOL);
    }

    @OnClick(R.id.vilButton)
    public void onVILExamClicked() {
        startExam(Exam.KEY_VIL);
    }

    private void startExam(String examType) {

        if(mInventory == null ) {
            return;
        }

        String sku = typeToSku(examType);
        if(sku == null || mInventory.getPurchase(sku) != null || value ==  51 || value1 == 51 ) {

            launchExam(examType);
        } else {
            // storing SKU
            selectedSku = sku;

            showIAP(sku);
        }
    }

    private void launchExam(String examType) {
        Intent intent = new Intent(this, ExamActivity.class);
        intent.putExtra("type", examType);
        startActivity(intent);
    }

    private String typeToSku(String type) {
        switch (type) {
            case Exam.KEY_BASIC:
                return SKU_VCA_B;
            case Exam.KEY_VOL:
                return SKU_VCA_VOL;
            case Exam.KEY_VIL:
                return SKU_VCU_VIL;
        }
        return null;
    }

    private void updateButtons() {
        if(mInventory.getPurchase(typeToSku(Exam.KEY_BASIC)) == null) {
            mBasisExamLabel.setText(getString(R.string.main_menu_buy_exam,
                    getString(R.string.version_vca_b)));
            mBasisExamDescriptionLabel.setText(getString(R.string.main_menu_buy_description_exam, 1000));
        } else {
            mBasisExamLabel.setText(getString(R.string.main_menu_exam_normal,
                    getString(R.string.version_vca_b)));
            mBasisExamDescriptionLabel.setText(getString(R.string.more_exams_description));
        }

        if(mInventory.getPurchase(typeToSku(Exam.KEY_VOL)) == null) {
            mVOLExamLabel.setText(getString(R.string.main_menu_buy_exam,
                    getString(R.string.version_vca_vol)));
            mVolExamDescriptionLabel.setText(getString(R.string.main_menu_buy_description_exam, 1400));
        } else {
            mVOLExamLabel.setText(getString(R.string.main_menu_exam_normal,
                    getString(R.string.version_vca_vol)));
            mVolExamDescriptionLabel.setText(getString(R.string.more_exams_description));
        }

        if(mInventory.getPurchase(typeToSku(Exam.KEY_VIL)) == null) {
            mVILExamLabel.setText(getString(R.string.main_menu_buy_exam,
                    getString(R.string.version_vcu_vil)));
            mVilExamDescriptionLabel.setText(getString(R.string.main_menu_buy_description_exam, 1400));
        } else {
            mVILExamLabel.setText(getString(R.string.main_menu_exam_normal,
                    getString(R.string.version_vcu_vil)));
            mVilExamDescriptionLabel.setText(getString(R.string.more_exams_description));
        }
    }

    private void showThankYouDialog() {
        // new screen
        ((MyApplication)getApplication()).trackScreen("IAP Thanks");

        // show dialog
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.iap_thanks_title))
                .setMessage(getString(R.string.iap_thanks_message))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        // hide screen
                        ((MyApplication) getApplication()).trackScreenTime();
                    }
                })
                .show();

    }

    private void sendAnalyticsEvent(String label) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Billing")
                .setAction(selectedSku)
                .setLabel(label)
                .build());
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        Toast.makeText(getApplicationContext(), "Klik op de home button om de app af te sluiten!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIabPurchaseFinished(IabResult result) {
        if(result.isSuccess()) {
            AdWordsConversionReporter.reportWithConversionId(this.getApplicationContext(),
                    "951878169", "DdcoCIiZ8mMQmYTyxQM", "8.60", true);

            sendAnalyticsEvent("Bought");

            showThankYouDialog();
        } else {
            sendAnalyticsEvent("Canceled");
        }
    }

    @Override
    public void onQueryInventoryFinished() {
        updateButtons();
    }
}
