package nl.kimplusdelta.vca.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import nl.kimplusdelta.vca.db.VcaContract;
import nl.kimplusdelta.vca.models.exam.Exam;

public class QuestionAlgorithm2 {

    private static final int MIN_QUESTIONS = 2;

    public static List<Integer> getQuestions(Context context, SQLiteDatabase db, String examType, int numberOfQuestions) {
        int minQuestions = MIN_QUESTIONS;
        int maxQuestions = minQuestions + Math.round(numberOfQuestions / 25);

        // loop through all questions
        Cursor questionCursor = db.query(VcaContract.Question.TABLE_NAME,
                new String[]{
                        VcaContract.Question._ID,
                        VcaContract.Question.CATEGORY,
                        VcaContract.Question.TYPE_B,
                        VcaContract.Question.TYPE_VOL,
                        VcaContract.Question.TYPE_VIL
                },
                getExamSelection(examType), null, null, null, null);

        HashMap<String, List<Integer>> result = new HashMap<>();

        int iQuestionId = questionCursor.getColumnIndex(VcaContract.Question._ID);
        int iCategory = questionCursor.getColumnIndex(VcaContract.Question.CATEGORY);

        if (questionCursor.moveToFirst()) {
            do {
                int questionId = questionCursor.getInt(iQuestionId);
                String category = Utils.getDecryptedData(context, questionCursor.getString(iCategory));
                if(!result.containsKey(category)) {
                    result.put(category, new ArrayList<Integer>());
                }
                result.get(category).add(questionId);
            } while (questionCursor.moveToNext());
        }
        questionCursor.close();

        List<Integer> questionSet = new ArrayList<>();
        do {
            // shuffle map
            List<Map.Entry<String, List<Integer>>> list = new ArrayList<>(result.entrySet());
            Collections.shuffle(list);

            for (Map.Entry<String, List<Integer>> pair : list) {
                List<Integer> questionIds = pair.getValue();

                Collections.shuffle(questionIds, new Random(System.nanoTime()));

                int max = Math.min(getRandomMax(Math.min(minQuestions, questionIds.size()), maxQuestions), questionIds.size());
                for (int i = 0; i < max; i++) {
                    if(!questionSet.contains(questionIds.get(i))) {
                        questionSet.add(questionIds.get(i));
                    }

                    if (questionSet.size() == numberOfQuestions) {
                        Collections.shuffle(questionSet, new Random(System.nanoTime()));
                        return questionSet;
                    }
                }
            }
        } while(questionSet.size() != numberOfQuestions);

        Collections.shuffle(questionSet, new Random(System.nanoTime()));
        return questionSet;
    }

    private static int getRandomMax(int min, int max) {
        Random rand = new Random();
        return min + rand.nextInt((max - min) + 1);
    }

    private static String getExamSelection(String examType) {
        switch (examType) {
            case Exam.KEY_LITE:
                return VcaContract.Question.TYPE_LITE + "=1";
            case Exam.KEY_BASIC:
                return VcaContract.Question.TYPE_B + "=1";
            case Exam.KEY_VOL:
                return VcaContract.Question.TYPE_VOL + "=1";
            case Exam.KEY_VIL:
                return VcaContract.Question.TYPE_VIL + "=1";
        }
        return null;
    }
}
