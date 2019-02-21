package nl.kimplusdelta.vca.activities;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nl.kimplusdelta.vca.MyApplication;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.db.VcaDbHelper;
import nl.kimplusdelta.vca.models.exam.ExamEntry;
import nl.kimplusdelta.vca.views.ExamAnswerView;

// TODO: 17/11/15 combine with exam activity?
public class ExamReviewActivity extends AppCompatActivity implements ExamAnswerView.AnswerCallback {

    @Bind(R.id.topContainer) LinearLayout mTopContainer;

    @Bind(R.id.categoryLabel) TextView mCategoryLabel;
    @Bind(R.id.questionLabel) TextView mQuestionLabel;
    @Bind(R.id.submitButton) Button mSubmitButton;

    @Bind(R.id.questionImageView) ImageView mQuestionImageView;
    @Bind(R.id.imageHolder) FrameLayout mImageHolder;

    private List<ExamAnswerView> mAnswerViews;
    private int mCorrectAnswerIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam);

        ButterKnife.bind(this);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        mAnswerViews = new ArrayList<>();
        mAnswerViews.add(0, new ExamAnswerView(this, findViewById(R.id.answer1), 0, false));
        mAnswerViews.add(1, new ExamAnswerView(this, findViewById(R.id.answer2), 1, false));
        mAnswerViews.add(2, new ExamAnswerView(this, findViewById(R.id.answer3), 2, false));

        final long question_id = getIntent().getExtras().getLong("question_id");
        final String answer = getIntent().getExtras().getString("question_answer");

        SQLiteDatabase db = VcaDbHelper.getInstance(this).getWritableDatabase();
        initExamEntry(new ExamEntry(this, db, question_id, null), answer);
        db.close();

        mTopContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((MyApplication)getApplication()).trackScreen("Exam Review");
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((MyApplication)getApplication()).trackScreenTime();
    }

    private void initExamEntry(ExamEntry entry, String answer) {
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

            // set answer
            onClicked(Arrays.asList(entry.answers).indexOf(answer));
        }
        mSubmitButton.setVisibility(View.GONE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String unescape(String s) {
        return s.replaceAll("\\\\n", "\\\n");
    }

    @Override
    public void onClicked(int index) {
        boolean correctAnswer = index == mCorrectAnswerIndex;
        if (correctAnswer) {
            mAnswerViews.get(index).setColor(ContextCompat.getDrawable(this, R.drawable.answer_bg_correct));
        } else {
            mAnswerViews.get(mCorrectAnswerIndex).setColor(ContextCompat.getDrawable(this, R.drawable.answer_bg_correct));
            mAnswerViews.get(index).setColor(ContextCompat.getDrawable(this, R.drawable.answer_bg_wrong));
        }

        mAnswerViews.get(index).setAsSelected();
    }
}
