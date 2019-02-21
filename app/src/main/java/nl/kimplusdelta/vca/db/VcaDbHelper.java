package nl.kimplusdelta.vca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VcaDbHelper extends SQLiteOpenHelper {

    private static VcaDbHelper mInstance;

    private VcaDbHelper(Context context) {
        super(context, VcaContract.DATABASE_NAME, null, VcaContract.DATABASE_VERSION);
    }

    public static VcaDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VcaDbHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VcaContract.Exam.CREATE_TABLE);
        db.execSQL(VcaContract.Question.CREATE_TABLE);
        db.execSQL(VcaContract.ActiveQuestion.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(VcaContract.Question.DELETE_TABLE);
        db.execSQL(VcaContract.Question.CREATE_TABLE);
    }

    public void clearDB() {
        // get db
        SQLiteDatabase db = getWritableDatabase();

        // delete exam history
        db.execSQL(VcaContract.Exam.DELETE_TABLE);
        db.execSQL(VcaContract.Exam.CREATE_TABLE);

        // delete question history
        db.execSQL(VcaContract.ActiveQuestion.DELETE_TABLE);
        db.execSQL(VcaContract.ActiveQuestion.CREATE_TABLE);

        // close db
        db.close();
    }
}
