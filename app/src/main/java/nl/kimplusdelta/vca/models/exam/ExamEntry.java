package nl.kimplusdelta.vca.models.exam;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import nl.kimplusdelta.vca.db.VcaContract;
import nl.kimplusdelta.vca.db.VcaDbHelper;
import nl.kimplusdelta.vca.utils.Utils;

public class ExamEntry {
    private static final String KEY_ID = "index";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWERS = "answers";
    private static final String KEY_CORRECT_ANSWERS = "correct answer";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_LITE = "lite";
    private static final String KEY_B = "b";
    private static final String KEY_VOL = "vol";
    private static final String KEY_VIL = "vil";

    private int db_id;

    public String category;
    public String question;
    public String[] answers;
    public String correctAnswer;
    public String image; // find correct type
    public Boolean answeredCorrectly;
    private String givenAnswer;
    private Boolean typeLite;
    private Boolean typeB;
    private Boolean typeVOL;
    private Boolean typeVIL;

    public ExamEntry(Context context, SQLiteDatabase db, long question_id, Boolean answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;

        // find the correct item
        Cursor cursor = db.query(VcaContract.Question.TABLE_NAME,
                getCursorColumns(), VcaContract.Question._ID + "=" + question_id,
                null, null, null, null);

        // init
        if(cursor.moveToFirst()) {
            initByCursor(context, cursor);
        }

        cursor.close();
    }

    public void setAnswer(Context context, long exam_id, Boolean answeredCorrectly, int answer_index) {
        this.answeredCorrectly = answeredCorrectly;
        this.givenAnswer = answers[answer_index];

        saveAnswerToDB(context, exam_id);
    }

    private void initByCursor(Context context, Cursor cursor) {
        this.db_id = cursor.getInt(cursor.getColumnIndex(VcaContract.Question._ID));

        this.category = encrypt(context, cursor.getString(cursor.getColumnIndex(VcaContract.Question.CATEGORY)));
        this.question = encrypt(context, cursor.getString(cursor.getColumnIndex(VcaContract.Question.QUESTION)));
        this.correctAnswer = encrypt(context, cursor.getString(cursor.getColumnIndex(VcaContract.Question.CORRECT)));
        this.image = encrypt(context, cursor.getString(cursor.getColumnIndex(VcaContract.Question.IMAGE)));

        answers = new String[3];
        this.answers[0] = encrypt(context, cursor.getString(cursor.getColumnIndex(VcaContract.Question.ANSWER_ONE)));
        this.answers[1] = encrypt(context, cursor.getString(cursor.getColumnIndex(VcaContract.Question.ANSWER_TWO)));
        this.answers[2] = encrypt(context, cursor.getString(cursor.getColumnIndex(VcaContract.Question.ANSWER_THREE)));

        this.typeLite = cursor.getInt(cursor.getColumnIndex(VcaContract.Question.TYPE_LITE)) == 1;
        this.typeB = cursor.getInt(cursor.getColumnIndex(VcaContract.Question.TYPE_B)) == 1;
        this.typeVOL = cursor.getInt(cursor.getColumnIndex(VcaContract.Question.TYPE_VOL)) == 1;
        this.typeVIL = cursor.getInt(cursor.getColumnIndex(VcaContract.Question.TYPE_VIL)) == 1;

        //shuffle answers
        shuffleArray(answers);
    }

    private ExamEntry(JSONObject data) throws JSONException {
        init(data);
    }

    private void init(JSONObject data) throws JSONException {
        db_id = data.getInt(KEY_ID);
        question = data.getString(KEY_QUESTION);
        category = data.getString(KEY_CATEGORY);

        JSONArray answerArr = data.getJSONArray(KEY_ANSWERS);
        answers = new String[answerArr.length()];
        for(int i = 0; i < answerArr.length(); i++) {
            answers[i] = answerArr.getString(i);
        }

        correctAnswer = data.getString(KEY_CORRECT_ANSWERS);
        image = data.getString(KEY_IMAGE);
        answeredCorrectly = null;

        typeLite = !data.getString(KEY_LITE).equals("");
        typeB = !data.getString(KEY_B).equals("");
        typeVOL = !data.getString(KEY_VOL).equals("");
        typeVIL = !data.getString(KEY_VIL).equals("");
    }

    private void shuffleArray(String[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);

            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private void saveAnswerToDB(Context context, long exam_id) {
        SQLiteDatabase db = VcaDbHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VcaContract.ActiveQuestion.STATE, answeredCorrectly ? 2 : 1);
        values.put(VcaContract.ActiveQuestion.ANSWER, givenAnswer);

        db.update(VcaContract.ActiveQuestion.TABLE_NAME, values,
                VcaContract.ActiveQuestion.QUESTION_ID + "=" + db_id + " AND " +
                        VcaContract.ActiveQuestion.EXAM_ID + "=" + exam_id, null);
        db.close();
    }

    public int getID() {
        return db_id;
    }

    private static String[] getCursorColumns() {
        return new String[]{
                VcaContract.Question._ID,
                VcaContract.Question.CATEGORY,
                VcaContract.Question.QUESTION,
                VcaContract.Question.CORRECT,
                VcaContract.Question.IMAGE,
                VcaContract.Question.ANSWER_ONE,
                VcaContract.Question.ANSWER_TWO,
                VcaContract.Question.ANSWER_THREE,
                VcaContract.Question.TYPE_LITE,
                VcaContract.Question.TYPE_B,
                VcaContract.Question.TYPE_VOL,
                VcaContract.Question.TYPE_VIL
        };
    }

    public static void writeToDB(Context context, SQLiteDatabase db, JSONObject data) {
        try {
            ExamEntry entry = new ExamEntry(data);

            ContentValues values = new ContentValues();
            values.put(VcaContract.Question._ID, entry.db_id);

            values.put(VcaContract.Question.CATEGORY, encrypt(context, entry.category));
            values.put(VcaContract.Question.QUESTION, encrypt(context, entry.question));
            values.put(VcaContract.Question.CORRECT, encrypt(context, entry.correctAnswer));
            values.put(VcaContract.Question.ANSWER_ONE, encrypt(context, entry.answers[0]));
            values.put(VcaContract.Question.ANSWER_TWO, encrypt(context, entry.answers[1]));
            values.put(VcaContract.Question.ANSWER_THREE, encrypt(context, entry.answers[2]));
            values.put(VcaContract.Question.IMAGE, encrypt(context, entry.image));

            values.put(VcaContract.Question.TYPE_LITE, entry.typeLite);
            values.put(VcaContract.Question.TYPE_B, entry.typeB);
            values.put(VcaContract.Question.TYPE_VOL, entry.typeVOL);
            values.put(VcaContract.Question.TYPE_VIL, entry.typeVIL);

            db.insert(VcaContract.Question.TABLE_NAME, null, values);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String encrypt(Context context, String data) {
        return Utils.getDecryptedData(context, data);
    }
}