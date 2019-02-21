package nl.kimplusdelta.vca.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.kimplusdelta.vca.MyApplication;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.db.VcaDbHelper;
import nl.kimplusdelta.vca.handlers.PackageHandler;
import nl.kimplusdelta.vca.utils.SaveUtils;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREVENT_SLEEP = "preventSleep";

    @Bind(R.id.sleepCheckBox)
    CheckBox mSleepCheckbox;

    private PackageHandler mPackageHandler;

    // Analytics Tracker
    private Tracker mTracker;
    public int login;
    public int login1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            login = extras.getInt("login");
            //The key argument here must match that used in the other activity
        }

        SharedPreferences sp = getSharedPreferences("key1", Activity.MODE_PRIVATE);
        login1 = sp.getInt("key2", -1);

        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        final Button logout_button = (Button) findViewById(R.id.logout_user);
        final Button login_button = (Button) findViewById(R.id.login_user);

        if (login1 == 51) {
            logout_button.setVisibility(View.GONE);
            login_button.setVisibility(View.GONE);
        } else {
            logout_button.setVisibility(View.GONE);
            login_button.setVisibility(View.VISIBLE);
        }


        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        mPackageHandler = new PackageHandler(this);

        mSleepCheckbox.setChecked(getSleepCheckState());
        mSleepCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSleepCheckState();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((MyApplication) getApplication()).trackScreen("Settings");
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((MyApplication) getApplication()).trackScreenTime();
    }

    private void setSleepCheckState() {
        SharedPreferences settings = SaveUtils.getPref(this, SaveUtils.SETTINGS);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(KEY_PREVENT_SLEEP, mSleepCheckbox.isChecked());
        editor.apply();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("InfoBox")
                .setAction("Sleep")
                .setLabel(mSleepCheckbox.isChecked() ? "Uit" : "Aan")
                .build());
    }

    private boolean getSleepCheckState() {
        SharedPreferences settings = SaveUtils.getPref(this, SaveUtils.SETTINGS);
        return settings.getBoolean(KEY_PREVENT_SLEEP, true); // default true
    }

    @OnClick(R.id.sleep_button)
    public void onSleepClicked() {
        mSleepCheckbox.setChecked(!mSleepCheckbox.isChecked());
        setSleepCheckState();
    }

    @OnClick(R.id.rate_me_button)
    public void onRateMeClicked() {
        mPackageHandler.openApp(getBaseContext().getPackageName());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("InfoBox")
                .setAction("RateMe")
                .setLabel("Settings")
                .build());
    }

    @OnClick(R.id.share_button)
    public void onShareClicked() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hallo, ik wil graag deze app delen. http://www.vca-examen-app.nl/");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("InfoBox")
                .setAction("Share")
                .setLabel("Settings")
                .build());
    }

    @OnClick(R.id.contact_button)
    public void onContactClicked() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", getString(R.string.action_about_send_email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.action_about_send_subject));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.action_about_send_chooser_title)));
    }

    @OnClick(R.id.delete_history_button)
    public void onDeleteHistoryClicked() {

        SharedPreferences preferences = getSharedPreferences("key1", 0);
        preferences.edit().remove("key2").commit();

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_data_dialog_confirmation))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        VcaDbHelper.getInstance(getApplicationContext()).clearDB();

                        dialog.dismiss();
                        Intent intent = new Intent(SettingsActivity.this, SplashActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);



                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    public void LogOutFunction(View v) {
        
        final ProgressDialog dlg = new ProgressDialog(SettingsActivity.this);
        dlg.setTitle("Een ogenblik geduld..");
        dlg.setMessage("Logt uit...");
        dlg.show();


        // logging out of Parse
        ParseUser.logOut();

        alertDisplayer("Gelukt", "U bent uitgelogd.");
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

    private void alertDisplayer(String title,String message){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingsActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog ok = builder.create();
        ok.show();
    }

    public void LogInFunction(View v)
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
