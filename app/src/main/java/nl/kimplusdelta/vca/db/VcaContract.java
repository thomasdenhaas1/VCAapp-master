package nl.kimplusdelta.vca.db;

import android.provider.BaseColumns;

public final class VcaContract {
    public static final int DATABASE_VERSION = 5;

    public static final String DATABASE_NAME = "vca.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DEFAULT_ZERO = " DEFAULT 0";
    private static final String COMMA_SEP = ", ";

    private VcaContract() {}

    public static abstract class Exam implements BaseColumns {
        public static final String TABLE_NAME = "exam";

        public static final String QUESTION_COUNT = "question_count";
        public static final String TIME = "time";
        public static final String STATUS = "status"; // 0 is terminated, 1 is active, 2 is completed
        public static final String SCORE = "score";
        public static final String TYPE = "type";
        public static final String DATE = "date";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                QUESTION_COUNT + INTEGER_TYPE + COMMA_SEP +
                TIME + INTEGER_TYPE + DEFAULT_ZERO + COMMA_SEP +
                STATUS + INTEGER_TYPE + DEFAULT_ZERO + COMMA_SEP +
                SCORE + INTEGER_TYPE + DEFAULT_ZERO + COMMA_SEP +
                TYPE + TEXT_TYPE + COMMA_SEP +
                DATE + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Question implements BaseColumns {
        public static final String TABLE_NAME = "question";

        public static final String CATEGORY = "category";
        public static final String QUESTION = "question";
        public static final String CORRECT = "correct";
        public static final String ANSWER_ONE = "answer_1";
        public static final String ANSWER_TWO = "answer_2";
        public static final String ANSWER_THREE = "answer_3";
        public static final String IMAGE = "image";
        public static final String TYPE_LITE = "lite";
        public static final String TYPE_B = "b";
        public static final String TYPE_VOL = "vol";
        public static final String TYPE_VIL = "vil";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY " + COMMA_SEP +
                CATEGORY + TEXT_TYPE + COMMA_SEP +
                QUESTION + TEXT_TYPE + COMMA_SEP +
                CORRECT + TEXT_TYPE + COMMA_SEP +
                ANSWER_ONE + TEXT_TYPE + COMMA_SEP +
                ANSWER_TWO + TEXT_TYPE + COMMA_SEP +
                ANSWER_THREE + TEXT_TYPE + COMMA_SEP +
                IMAGE + TEXT_TYPE + COMMA_SEP +
                TYPE_LITE + INTEGER_TYPE + DEFAULT_ZERO + COMMA_SEP +
                TYPE_B + INTEGER_TYPE + DEFAULT_ZERO + COMMA_SEP +
                TYPE_VOL + INTEGER_TYPE + DEFAULT_ZERO + COMMA_SEP +
                TYPE_VIL + INTEGER_TYPE + DEFAULT_ZERO + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class ActiveQuestion implements BaseColumns {
        public static final String TABLE_NAME = "active_question";

        public static final String EXAM_ID = "exam_id";
        public static final String QUESTION_ID = "question_id";
        public static final String STATE = "state";
        public static final String TYPE = "type";
        public static final String ANSWER = "answer";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                EXAM_ID + INTEGER_TYPE + COMMA_SEP +
                QUESTION_ID + INTEGER_TYPE + COMMA_SEP +
                STATE + INTEGER_TYPE + DEFAULT_ZERO + COMMA_SEP +
                TYPE + TEXT_TYPE + COMMA_SEP +
                ANSWER + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
