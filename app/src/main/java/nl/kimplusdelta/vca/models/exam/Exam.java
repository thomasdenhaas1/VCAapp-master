package nl.kimplusdelta.vca.models.exam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.kimplusdelta.vca.db.VcaContract;
import nl.kimplusdelta.vca.db.VcaDbHelper;
import nl.kimplusdelta.vca.models.adapterItems.ExamResultItem;
import nl.kimplusdelta.vca.utils.QuestionAlgorithm2;
import nl.kimplusdelta.vca.utils.Utils;

public class Exam {

    private static final Integer STATUS_TERMINATED = 0;
    private static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_COMPLETED = 2;

    public static final String KEY_LITE = "Lite";
    public static final String KEY_BASIC = "Basis";
    public static final String KEY_VOL = "Vol";
    public static final String KEY_VIL = "Vil";

    private static final Integer LITE_ENTRIES = 20;
    private static final Integer B_ENTRIES = 40;
    private static final Integer VOL_ENTRIES = 70;
    private static final Integer VIL_ENTRIES = 70;

    public long exam_db_id;

    private final Context mContext;
    private boolean mNewExam;
    private long mTime;
    private long mExamCompleteDate;
    private String mExamType;

    private final List<ExamEntry> mEntries;
    private int mCurrentEntryPosition;

    private Exam(Context context, long exam_db_id) {
        this.mContext = context;
        this.exam_db_id = exam_db_id;

        this.mEntries = new ArrayList<>();
        this.mCurrentEntryPosition = 0;
        this.mTime = 0;
    }

    public static Exam getExam(Context context, String examType) {
        try {
            SQLiteDatabase db = VcaDbHelper.getInstance(context).getReadableDatabase();
            Cursor cursor = db.query(VcaContract.Exam.TABLE_NAME,
                    new String[]{VcaContract.Exam._ID, VcaContract.Exam.STATUS, VcaContract.Exam.TYPE},
                    VcaContract.Exam.STATUS + "=?" +" AND " + VcaContract.Exam.TYPE + "=?",
                    new String[]{ String.valueOf(STATUS_ACTIVE), examType }, null, null, null);

            if (cursor.moveToFirst()) {
                long exam_db_id = cursor.getInt(cursor.getColumnIndex(VcaContract.Exam._ID));

                cursor.close();
                db.close();

                return getExistingExam(context, exam_db_id);
            } else {
                cursor.close();
                db.close();

                // create a new DB
                return getNewExam(context, examType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setEntryAnswer(int index, Boolean answeredCorrectly, int answer_index) {
        getEntry(index).setAnswer(mContext, exam_db_id, answeredCorrectly, answer_index);
    }

    public ExamEntry getEntry(int position) {
        if (mEntries.size() > position) {
            mCurrentEntryPosition = position;
            return mEntries.get(position);
        }
        return null;
    }

    public int getEntryCount() {
        return mEntries.size();
    }

    public int getCorrectAnsweredCount() {
        int counter = 0;

        for (ExamEntry entry : mEntries) {
            if (entry != null && entry.answeredCorrectly != null) {
                if (entry.answeredCorrectly) {
                    counter++;
                }
            }
        }

        return counter;
    }

    public int getIncorrectAnsweredCount() {
        int counter = 0;

        for (ExamEntry entry : mEntries) {
            if (entry != null && entry.answeredCorrectly != null) {
                if (!entry.answeredCorrectly) {
                    counter++;
                }
            }
        }

        return counter;
    }

    public int getCurrentEntryPosition() {
        return mCurrentEntryPosition;
    }

    private void setCurrentEntryPosition(int position) {
        mCurrentEntryPosition = position;
    }

    public long getTime() {
        return mTime;
    }

    public boolean isNewExam() {
        return mNewExam;
    }

    public void setTime(long time) {
        this.mTime = time;

        SQLiteDatabase db = VcaDbHelper.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VcaContract.Exam.TIME, time);
        db.update(VcaContract.Exam.TABLE_NAME, values, VcaContract.Exam._ID + "=" + exam_db_id, null);
        db.close();
    }

    public String getExamType() {
        return mExamType;
    }

    public void StopExam() {
        SQLiteDatabase db = VcaDbHelper.getInstance(mContext).getWritableDatabase();

        // delete unanswered questions
        db.delete(VcaContract.ActiveQuestion.TABLE_NAME,
                VcaContract.ActiveQuestion.EXAM_ID + "=?" + " AND " +
                        VcaContract.ActiveQuestion.STATE + "=?",
                new String[]{String.valueOf(exam_db_id), "0"});

        // update exam
        ContentValues values = new ContentValues();
        values.put(VcaContract.Exam.STATUS, STATUS_TERMINATED);
        values.put(VcaContract.Exam.DATE, System.currentTimeMillis());
        db.update(VcaContract.Exam.TABLE_NAME, values, VcaContract.Exam._ID + "=" + exam_db_id, null);

        // reset id
        exam_db_id = -1;
        db.close();
    }

    public void saveCompletedExam() {
        SQLiteDatabase db = VcaDbHelper.getInstance(mContext).getWritableDatabase();

        // update exam
        ContentValues values = new ContentValues();
        values.put(VcaContract.Exam.STATUS, STATUS_COMPLETED);
        values.put(VcaContract.Exam.SCORE, getCorrectAnsweredCount());

        mExamCompleteDate = System.currentTimeMillis();
        values.put(VcaContract.Exam.DATE, mExamCompleteDate);

        db.update(VcaContract.Exam.TABLE_NAME, values, VcaContract.Exam._ID + "=" + exam_db_id, null);

        db.close();
    }

    public List<ExamResultItem> getResults() {
        Map<String, ExamResultItem> results = new HashMap<>();

        // seed map
        for (ExamEntry entry : mEntries) {
            if (entry.answeredCorrectly == null) {
                continue;
            }

            if (!results.containsKey(entry.category)) {
                results.put(entry.category, new ExamResultItem());
            }

            results.get(entry.category).correct += entry.answeredCorrectly ? 1 : 0;
            results.get(entry.category).total++;
        }

        // TODO: 02/09/15 improve this function, create one loop out of it
        // create a return list
        List<ExamResultItem> returnList = new ArrayList<>();
        Iterator it = results.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            ExamResultItem resultItem = (ExamResultItem) pair.getValue();
            resultItem.category = (String) pair.getKey();
            returnList.add(resultItem);

            it.remove(); // avoids a ConcurrentModificationException
        }

        return returnList;
    }

    private void addEntry(ExamEntry entry) {
        this.mEntries.add(entry);
    }

    public static void init(Context context) {
        try {
            JSONObject content = new JSONObject(Utils.loadDataFromAsset(context));
            JSONArray jsonData = content.getJSONArray("content");
            saveQuestions(context, jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static Exam getNewExam(Context context, String examType) {
        SQLiteDatabase db = VcaDbHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VcaContract.Exam.STATUS, STATUS_ACTIVE); // active
        values.put(VcaContract.Exam.QUESTION_COUNT, getExamEntryCount(examType));
        values.put(VcaContract.Exam.TYPE, examType);
        long exam_db_id = db.insert(VcaContract.Exam.TABLE_NAME, null, values);

        Exam exam = new Exam(context, exam_db_id);
        exam.mNewExam = true;
        exam.mExamType = examType;

        List<Integer> questionIds = QuestionAlgorithm2.getQuestions(context, db, examType, getExamEntryCount(examType));

        db.beginTransaction();
        for (Integer questionID : questionIds) {
            values = new ContentValues();
            values.put(VcaContract.ActiveQuestion.EXAM_ID, exam_db_id);
            values.put(VcaContract.ActiveQuestion.QUESTION_ID, questionID);
            values.put(VcaContract.ActiveQuestion.TYPE, examType);
            db.insert(VcaContract.ActiveQuestion.TABLE_NAME, null, values);

            // save entry
            exam.addEntry(new ExamEntry(context, db, questionID, null));
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        return exam;
    }

    public static Exam getExistingExam(Context context, long exam_id) {
        Exam exam = new Exam(context, exam_id);
        exam.mNewExam = false;

        SQLiteDatabase db = VcaDbHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(VcaContract.ActiveQuestion.TABLE_NAME,
                new String[]{
                        VcaContract.ActiveQuestion._ID,
                        VcaContract.ActiveQuestion.EXAM_ID,
                        VcaContract.ActiveQuestion.QUESTION_ID,
                        VcaContract.ActiveQuestion.STATE
                },
                VcaContract.ActiveQuestion.EXAM_ID + "=" + exam_id,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Boolean answered = null;
                int answer = cursor.getInt(cursor.getColumnIndex(VcaContract.ActiveQuestion.STATE));
                if (answer != 0) {
                    answered = answer != 1; // 2 = correctly answered
                }

                ExamEntry entry = new ExamEntry(context, db,
                        cursor.getInt(cursor.getColumnIndex(VcaContract.ActiveQuestion.QUESTION_ID)),
                        answered);

                // save entry
                exam.addEntry(entry);

                if (entry.answeredCorrectly != null) {
                    exam.setCurrentEntryPosition(exam.getCurrentEntryPosition() + 1);
                }

                cursor.moveToNext();
            }
        }
        cursor.close();

        // retrieve exam time
        cursor = db.query(VcaContract.Exam.TABLE_NAME,
                new String[]{
                        VcaContract.Exam._ID, VcaContract.Exam.TIME,
                        VcaContract.Exam.TYPE, VcaContract.Exam.DATE
                },
                VcaContract.Exam._ID + "=" + exam_id,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            exam.mTime = cursor.getLong(cursor.getColumnIndex(VcaContract.Exam.TIME));
            exam.mExamType = cursor.getString(cursor.getColumnIndex(VcaContract.Exam.TYPE));
            exam.mExamCompleteDate = cursor.getLong(cursor.getColumnIndex(VcaContract.Exam.DATE));
        }
        cursor.close();
        db.close();

        return exam;
    }

    private static void saveQuestions(Context context, JSONArray arr) throws JSONException {
        if (arr == null || arr.length() == 0) {
            return;
        }

        SQLiteDatabase db = VcaDbHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(VcaContract.Question.TABLE_NAME, new String[]{VcaContract.Question._ID},
                null, null, null, null, null);

        if(cursor.getCount() == 0) {
            db.beginTransaction();
            for (int i = 0; i < arr.length(); i++) {
                ExamEntry.writeToDB(context, db, arr.getJSONObject(i));
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }

        cursor.close();
        db.close();
    }

    public static int getExamEntryCount(String type) {
        switch (type) {
            case KEY_BASIC:
                return B_ENTRIES;
            case KEY_VOL:
                return VOL_ENTRIES;
            case KEY_VIL:
                return VIL_ENTRIES;
            case KEY_LITE:
                return LITE_ENTRIES;
        }
        return 0;
    }
}
