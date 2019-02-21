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

import java.util.ArrayList;

import butterknife.ButterKnife;
import nl.kimplusdelta.vca.MyApplication;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.adapters.HistoryAdapter;
import nl.kimplusdelta.vca.db.VcaContract;
import nl.kimplusdelta.vca.db.VcaDbHelper;
import nl.kimplusdelta.vca.models.adapterItems.ExamHistoryItem;
import nl.kimplusdelta.vca.models.exam.Exam;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        ButterKnife.bind(this);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        final ArrayList<ExamHistoryItem> listValues = new ArrayList<>();

        SQLiteDatabase db = VcaDbHelper.getInstance(this).getWritableDatabase();
        Cursor cursor = db.query(VcaContract.Exam.TABLE_NAME,
                new String[]{
                        VcaContract.Exam._ID,
                        VcaContract.Exam.DATE,
                        VcaContract.Exam.TIME,
                        VcaContract.Exam.QUESTION_COUNT,
                        VcaContract.Exam.TYPE,
                        VcaContract.Exam.SCORE
                },
                VcaContract.Exam.STATUS + "=" + Exam.STATUS_COMPLETED,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            int iId = cursor.getColumnIndex(VcaContract.Exam._ID);
            int iScore = cursor.getColumnIndex(VcaContract.Exam.SCORE);
            int iDate = cursor.getColumnIndex(VcaContract.Exam.DATE);
            int iTime = cursor.getColumnIndex(VcaContract.Exam.TIME);
            int iType = cursor.getColumnIndex(VcaContract.Exam.TYPE);
            int iQuestionCount = cursor.getColumnIndex(VcaContract.Exam.QUESTION_COUNT);

            while (!cursor.isAfterLast()) {
                ExamHistoryItem item = new ExamHistoryItem();
                item.total = cursor.getInt(iQuestionCount);
                item.correct = cursor.getInt(iScore);
                item.date = cursor.getLong(iDate);
                item.time = cursor.getLong(iTime);
                item.item_id = cursor.getLong(iId);
                item.type = cursor.getString(iType);

                // add list for adapter ( insert )
                listValues.add(0, item);

                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        // set values
        final HistoryAdapter adapter = new HistoryAdapter(this, listValues);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // empty view
        listView.setEmptyView(findViewById(R.id.emptyView));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryActivity.this, ResultsActivity.class);
                intent.putExtra("exam_id", adapter.getItem(position).item_id);
                intent.putExtra("rate_me", false);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((MyApplication)getApplication()).trackScreen("History");
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
