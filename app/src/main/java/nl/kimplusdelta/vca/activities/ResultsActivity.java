package nl.kimplusdelta.vca.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.kimplusdelta.vca.MyApplication;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.adapters.ResultsAdapter;
import nl.kimplusdelta.vca.handlers.PackageHandler;
import nl.kimplusdelta.vca.models.adapterItems.ExamResultItem;
import nl.kimplusdelta.vca.models.exam.Exam;
import nl.kimplusdelta.vca.utils.Utils;

public class ResultsActivity extends AppCompatActivity {

    @Bind(R.id.listView) ListView mListView;
    @Bind(R.id.resultLabel) TextView mResultLabel;
    @Bind(R.id.scoreLabel) TextView mScoreLabel;
    @Bind(R.id.pctLabel) TextView mPercentageLabel;
    @Bind(R.id.requiredPctLabel) TextView mMissingScoreLabel;
    @Bind(R.id.timeLabel) TextView mTimeLabel;

    private Exam mExam;

    private PackageHandler mPackageHandler;

    private boolean showRateMe;

    interface RateMeCB {
        void onRateMeClicked(boolean doAction);
    }

    // Analytics Tracker
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam_results);
        ButterKnife.bind(this);

        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        showRateMe = getIntent().getExtras().getBoolean("rate_me");

        final long exam_id = getIntent().getExtras().getLong("exam_id");
        mExam = Exam.getExistingExam(this, exam_id);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle(getResultTitle());
        }

        initScoreHeader();

        // set list form
        final List<ExamResultItem> results = mExam.getResults();
        ResultsAdapter adapter = new ResultsAdapter(this, results);
        mListView.setAdapter(adapter);

        mPackageHandler = new PackageHandler(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ResultsActivity.this, QuestionListActivity.class);
                intent.putExtra("exam_id", mExam.exam_db_id);
                intent.putExtra("exam_category", results.get(position).category);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((MyApplication)getApplication()).trackScreen("Results");
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((MyApplication)getApplication()).trackScreenTime();
    }

    private void initScoreHeader() {
        int correct = mExam.getCorrectAnsweredCount();
        int total = mExam.getEntryCount();
        int pct = Math.round(((float) correct / (float) total) * 100.0f);

        // set head label
        mResultLabel.setText((correct >= (total * 0.65f)) ? getString(R.string.exam_successful) : getString(R.string.exam_failed));

        // set total score
        mScoreLabel.setText(getString(R.string.exam_score_format, correct, total));
        mPercentageLabel.setText(String.format("%d%%", pct));

        // set missing score
        if(pct >= 65) {
            mMissingScoreLabel.setVisibility(View.INVISIBLE);

            sendAnalyticsEvent("Succeeded");
        } else {
            int missing = Math.round(total * 0.65f) - correct;
            String points = getResources().getQuantityString(R.plurals.punten, missing, missing);
            mMissingScoreLabel.setText(getString(R.string.required_score_result, points));

            sendAnalyticsEvent("Failed");
        }

        sendAnalyticsEvent((long) pct);

        // set time
        mTimeLabel.setText(Utils.getTimeInFormat(mExam.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!checkRateMe()) {
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getResultTitle() {
        return getString(R.string.title_activity_exam_results) + " " + Utils.getExamTypeName(this, mExam.getExamType());
    }

    @Override
    public void onBackPressed() {
        if (!checkRateMe()) {
            super.onBackPressed();
        }
    }

    private boolean checkRateMe() {
        if(showRateMe) {
            showRateMeDialog(new RateMeCB() {
                @Override
                public void onRateMeClicked(boolean doAction) {
                    if (doAction && showRateMe) {
                        finish();
                    }
                    showRateMe = false;
                }
            });
        }

        return showRateMe;
    }

    @OnClick(R.id.shareButton)
    public void onShareClicked() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hallo, ik wil graag deze app delen. http://www.vca-examen-app.nl/");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("InfoBox")
                .setAction("Share")
                .setLabel("Results")
                .build());
    }

    @OnClick(R.id.rateMeButton)
    public void onRateClicked() {
        mPackageHandler.openApp(getBaseContext().getPackageName());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("InfoBox")
                .setAction("RateMe")
                .setLabel("Results")
                .build());
    }

    private void showRateMeDialog(final RateMeCB cb) {
        SharedPreferences prefs = getSharedPreferences("rateme", MODE_PRIVATE);
        if(prefs.contains("dontshowagain")) {
            if(prefs.getBoolean("dontshowagain", false)) {
                cb.onRateMeClicked(true);
                return;
            }
        }

        final SharedPreferences.Editor editor = prefs.edit();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.rate_me_dialog, null);
        builder.setView(v);

        final AlertDialog alert = builder.create();

        v.findViewById(R.id.yesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.apply();
                }

                mPackageHandler.openApp(getBaseContext().getPackageName());
                alert.dismiss();

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("InfoBox")
                        .setAction("RateMe")
                        .setLabel("Yes")
                        .build());

                // no action
                cb.onRateMeClicked(false);
            }
        });

        v.findViewById(R.id.noButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.apply();
                }

                alert.dismiss();

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("InfoBox")
                        .setAction("RateMe")
                        .setLabel("No")
                        .build());
            }
        });

        v.findViewById(R.id.laterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("InfoBox")
                        .setAction("RateMe")
                        .setLabel("Later")
                        .build());
            }
        });

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cb.onRateMeClicked(true);
            }
        });

        alert.show();
    }

    private void sendAnalyticsEvent(String label) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Exam")
                .setAction(mExam.getExamType())
                .setLabel(label)
                .build());
    }

    private void sendAnalyticsEvent(Long value) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Exam")
                .setAction(mExam.getExamType())
                .setLabel("Score percentage")
                .setValue(value)
                .build());
    }
}
