package nl.kimplusdelta.vca.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import nl.kimplusdelta.vca.MyApplication;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.adapters.QuestionAdapter;
import nl.kimplusdelta.vca.db.VcaContract;
import nl.kimplusdelta.vca.db.VcaDbHelper;
import nl.kimplusdelta.vca.models.adapterItems.ExamQuestionItem;
import nl.kimplusdelta.vca.utils.Utils;

public class QuestionListActivity extends AppCompatActivity {

    @Bind(R.id.categoryLabel) TextView mCategoryLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question_list);

        ButterKnife.bind(this);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        final long exam_id = getIntent().getExtras().getLong("exam_id");
        final String exam_category = getIntent().getExtras().getString("exam_category");

        mCategoryLabel.setText(exam_category);

        final ArrayList<ExamQuestionItem> listValues = new ArrayList<>();
        SQLiteDatabase db = VcaDbHelper.getInstance(this).getWritableDatabase();
        Cursor aqCursor = db.query(VcaContract.ActiveQuestion.TABLE_NAME,
                new String[]{
                        VcaContract.ActiveQuestion.EXAM_ID,
                        VcaContract.ActiveQuestion.QUESTION_ID,
                        VcaContract.ActiveQuestion.STATE,
                        VcaContract.ActiveQuestion.ANSWER
                },
                VcaContract.ActiveQuestion.EXAM_ID + "=" + exam_id,
                null, null, null, null);

        if (aqCursor.moveToFirst()) {
            int iQuestionId = aqCursor.getColumnIndex(VcaContract.ActiveQuestion.QUESTION_ID);
            int iAnswer = aqCursor.getColumnIndex(VcaContract.ActiveQuestion.ANSWER);
            int iState = aqCursor.getColumnIndex(VcaContract.ActiveQuestion.STATE);

            while (!aqCursor.isAfterLast()) {
                Cursor questionCursor = db.query(VcaContract.Question.TABLE_NAME,
                        new String[]{
                                VcaContract.Question._ID,
                                VcaContract.Question.CATEGORY,
                                VcaContract.Question.QUESTION
                        },
                        VcaContract.Question._ID + "=? AND " + VcaContract.Question.CATEGORY + "=?",
                        new String[] { aqCursor.getString(iQuestionId), Utils.getDecryptedData(this, exam_category) }, null, null, null);


                if(questionCursor.moveToFirst()) {
                    String answer = aqCursor.getString(iAnswer);
                    // prevent crashes
                    if(answer != null && !answer.equals("")) {
                        ExamQuestionItem item = new ExamQuestionItem();
                        item.db_item = aqCursor.getLong(iQuestionId);
                        item.answer = aqCursor.getString(iAnswer);
                        item.correct = (aqCursor.getInt(iState) == 2);
                        item.question = Utils.getDecryptedData(this, questionCursor.getString(
                                questionCursor.getColumnIndex(VcaContract.Question.QUESTION)));

                        listValues.add(item);
                    }
                }
                questionCursor.close();

                aqCursor.moveToNext();
            }
        }
        aqCursor.close();
        db.close();

        ListView listView = (ListView) findViewById(R.id.listView);
        QuestionAdapter adapter = new QuestionAdapter(this, listValues);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QuestionListActivity.this, ExamReviewActivity.class);
                ExamQuestionItem item = listValues.get(position);
                intent.putExtra("question_id", item.db_item);
                intent.putExtra("question_answer", item.answer);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((MyApplication)getApplication()).trackScreen("Question List");
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((MyApplication)getApplication()).trackScreenTime();
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
}
