package nl.kimplusdelta.vca.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.kimplusdelta.vca.BuildConfig;
import nl.kimplusdelta.vca.MyApplication;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.models.exam.Exam;
import nl.kimplusdelta.vca.models.exam.ExamEntry;
import nl.kimplusdelta.vca.utils.SaveUtils;
import nl.kimplusdelta.vca.utils.Utils;
import nl.kimplusdelta.vca.views.ExamAnswerView;
import nl.kimplusdelta.vca.views.TutorialDialog;

public class ExamActivity extends AppCompatActivity implements
        ExamAnswerView.AnswerCallback {

    private static final int MENU_FINISH_ITEM = 1;

    private static final String ANALYTICS_CATEGORY_EXAM = "Exam";
    private static final String ANALYTICS_CATEGORY_EXAM_QUESTION = "Exam Question";

    @Bind(R.id.questionLabel) TextView mQuestionLabel;
    @Bind(R.id.submitButton) Button mSubmitButton;

    @Bind(R.id.progressLabel) TextView mProgressLabel;
    @Bind(R.id.correctScoreLabel) TextView mCorrectScoreLabel;
    @Bind(R.id.incorrectScoreLabel) TextView mInCorrectScoreLabel;
    @Bind(R.id.categoryLabel) TextView mCategoryLabel;
    @Bind(R.id.examTimer) Chronometer mTimer;

    @Bind(R.id.questionImageView) ImageView mQuestionImageView;
    @Bind(R.id.imageHolder) FrameLayout mImageHolder;

    private Exam mExam;

    private List<ExamAnswerView> mAnswerViews;

    private int mCurrentEntry;
    private int mCorrectAnswerIndex;
    private boolean mAnswered;
    private int mCorrectAnswerCounter;
    private int mIncorrectAnswerCounter;

    private boolean mTutorialOpen;

    // Analytics Tracker
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam);
        ButterKnife.bind(this);

        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // prevent screen from sleeping
        SharedPreferences settings = SaveUtils.getPref(this, SaveUtils.SETTINGS);
        if(settings.getBoolean(SettingsActivity.KEY_PREVENT_SLEEP, true)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        mAnswerViews = new ArrayList<>();
        mAnswerViews.add(0, new ExamAnswerView(this, findViewById(R.id.answer1), 0, true));
        mAnswerViews.add(1, new ExamAnswerView(this, findViewById(R.id.answer2), 1, true));
        mAnswerViews.add(2, new ExamAnswerView(this, findViewById(R.id.answer3), 2, true));

        String examType = getIntent().getExtras().getString("type");

        // initialize new / resume old exam
        mExam = Exam.getExam(this, examType);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar != null) {
            if(mExam != null) {
                bar.setTitle(Utils.getExamTypeName(this, mExam.getExamType()));
            }
        }

        // start exam
        startExam();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exam, menu);
        if(BuildConfig.DEBUG) {
            menu.add(Menu.NONE, MENU_FINISH_ITEM, Menu.NONE, "Finish");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_stop) {
            showStopConfirmationDialog();
            return true;
        } else if(id == MENU_FINISH_ITEM) {
            showResults();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        enableTimer(false);

        ((MyApplication)getApplication()).trackScreenTime();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!mTutorialOpen) {
            enableTimer(true);
        }

        ((MyApplication)getApplication()).trackScreen("Exam " + mExam.getExamType());
    }

    private void enableTimer(boolean enabled) {
        if(enabled) {
            mTimer.setBase(SystemClock.elapsedRealtime() - mExam.getTime());
            mTimer.start();
        } else {
            mTimer.stop();
            mExam.setTime((SystemClock.elapsedRealtime() - mTimer.getBase()));
        }
    }

    private void showStopConfirmationDialog() {
        // stop timer
        enableTimer(false);

        // show dialog
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.exit_dialog_confirmation))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        stopExam();
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        // continue timer
                        enableTimer(true);
                    }
                })
                .show();
    }

    private void stopExam() {
        mExam.StopExam();

        sendAnalyticsEvent("Stopped");
    }

    private void startExam() {
        // set the the correct position, for new and resume
        mCurrentEntry = mExam.getCurrentEntryPosition();
        mCorrectAnswerCounter = mExam.getCorrectAnsweredCount();
        mIncorrectAnswerCounter = mExam.getIncorrectAnsweredCount();

        updateScore(null);

        if(mCurrentEntry >= mExam.getEntryCount()) {
            showResults();
        } else {
            initExamEntry(mExam.getEntry(mCurrentEntry));
        }

        if(mExam.isNewExam()) {
            mTutorialOpen = true;
            new TutorialDialog(this, mExam.getExamType(), mExam.getEntryCount(),
                    new TutorialDialog.tutorialListener() {
                @Override
                public void closed() {
                    mTutorialOpen = false;

                    // start timer
                    enableTimer(true);
                }
            }).show();

            sendAnalyticsEvent("New Exam");
        } else {
            sendAnalyticsEvent("Resumed Exam");
        }
    }

    private void initExamEntry(ExamEntry entry) {
        if(entry != null) {
            mQuestionLabel.setText(unescape(entry.question));
            for(int i = 0; i < entry.answers.length; i++) {
                mAnswerViews.get(i).answerLabel.setText(unescape(entry.answers[i]));
                mAnswerViews.get(i).reset();
            }

            // bind correctID to
            mCorrectAnswerIndex = Arrays.asList(entry.answers).indexOf(entry.correctAnswer);

            if(!entry.image.equals("")) {
                mImageHolder.setVisibility(View.VISIBLE);
                mQuestionImageView.setImageDrawable(getImageFromAssets(entry.image));
            } else {
                mImageHolder.setVisibility(View.GONE);
                mQuestionImageView.setImageDrawable(null);
            }

            mCategoryLabel.setText(unescape(entry.category));
        }

        mSubmitButton.setVisibility(View.INVISIBLE);

        mAnswered = false;

        mProgressLabel.setText(getString(R.string.question_number, (mCurrentEntry + 1), mExam.getEntryCount()));
    }

    private void updateScore(Boolean correctAnswer) {
        if(correctAnswer != null) {
            if (correctAnswer) {
                mCorrectAnswerCounter++;
            } else {
                mIncorrectAnswerCounter++;
            }
        }

        // update labels
        mCorrectScoreLabel.setText(String.format("%d", mCorrectAnswerCounter));
        mInCorrectScoreLabel.setText(String.format("%d", mIncorrectAnswerCounter));
    }

    @Override
    public void onClicked(int index) {
        if(!mAnswered) {
            boolean correctAnswer = index == mCorrectAnswerIndex;
            if (correctAnswer) {
                mAnswerViews.get(index).setColor(ContextCompat.getDrawable(this, R.drawable.answer_bg_correct));
            } else {
                mAnswerViews.get(mCorrectAnswerIndex).setColor(ContextCompat.getDrawable(this, R.drawable.answer_bg_correct));
                mAnswerViews.get(index).setColor(ContextCompat.getDrawable(this, R.drawable.answer_bg_wrong));
            }

            // update model
            mExam.setEntryAnswer(mCurrentEntry, correctAnswer, index);

            mAnswerViews.get(index).setAsSelected();

            updateScore(correctAnswer);

            mSubmitButton.setVisibility(View.VISIBLE);
            mAnswered = true;

            if(correctAnswer) {
                sendAnalyticsEvent(String.valueOf(mExam.getEntry(mCurrentEntry).getID()), (long)1);
                sendAnalyticsEvent(mExam.getEntry(mCurrentEntry).category, (long)1);
            } else {
                sendAnalyticsEvent(String.valueOf(mExam.getEntry(mCurrentEntry).getID()), (long)0);
                sendAnalyticsEvent(mExam.getEntry(mCurrentEntry).category, (long)0);
            }
        }
    }


    @OnClick(R.id.submitButton)
    public void nextEntry() {
        mCurrentEntry++;

        if(mCurrentEntry < mExam.getEntryCount()) {
            initExamEntry(mExam.getEntry(mCurrentEntry));
        } else {
            showResults();
        }
    }

    private void showResults() {
        // exam complete
        mExam.saveCompletedExam();

        sendAnalyticsEvent("Finished");

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("exam_id", mExam.exam_db_id);
        intent.putExtra("rate_me", true);
        startActivity(intent);

        finish();
    }

    private Drawable getImageFromAssets(String image) {
        Drawable d = null;
        InputStream ims;
        try {
            // get input stream
            ims = getAssets().open("images/" +image);

            // load image as Drawable
            d = Drawable.createFromStream(ims, null);

            // close stream
            ims.close();
        }
        catch(IOException ex) {
            Log.e("CC", "getImageFromAssets exception: " + ex.getLocalizedMessage());
        }

        return d;
    }

    private String unescape(String s) {
        return s.replaceAll("\\\\n", "\\\n");
    }

    private void sendAnalyticsEvent(String label) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(ExamActivity.ANALYTICS_CATEGORY_EXAM)
                .setAction(mExam.getExamType())
                .setLabel(label)
                .build());
    }

    private void sendAnalyticsEvent(String label, long value) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(ExamActivity.ANALYTICS_CATEGORY_EXAM_QUESTION)
                .setAction(mExam.getExamType())
                .setLabel(label)
                .setValue(value)
                .build());
    }
}
