package nl.kimplusdelta.vca.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.models.exam.Exam;

public class Utils {

    public static String loadDataFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            int b = is.read(buffer);
            is.close();

            if (b == 0) {
                Log.d("CC", "Couldn't read all bytes");
            }

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return getDecryptedData(context, json);
    }

    public static String getDecryptedData(Context context, String data) {
        char[] k = Utils.getEK(context);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            int xor = data.charAt(i) ^ k[i % k.length];
            out.append((char) xor);
        }
        String Text = out.toString();
//        writeLogToFile(Text, "VCCTEXT");
        return out.toString();
    }

    private static char[] getEK(Context context) {
        String[] p = context.getPackageName().split("\\.");

        char[] k = new char[6];
        char[] c = new char[k.length * 2];

        for (int i = 0; i < k.length / 2; i++) {
            k[i] = p[i].charAt(i);
            k[i + (k.length / 2)] = p[i].charAt(1);


        }
        for (int i = 0; i < k.length; i++) {
            c[i] = k[i];
            c[k.length + i] = k[k.length - 1 - i];
        }


        return c;
    }


    public static void writeLogToFile(String e, String filename) {
        try {
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //File folder = new File(SDCardRoot, "PMD");
            if (!SDCardRoot.exists())
                SDCardRoot.mkdirs();

            filename = filename + ".txt";

            BufferedWriter bos = new BufferedWriter(new FileWriter(SDCardRoot.getAbsolutePath() + "/" + filename));
            bos.write(e);
            bos.flush();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getTimeInFormat(long time) {
        return String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }

    public static String getDateInFormat(Context context, long ms) {
        DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        String date = df.format(new Date(ms));
        String localizedTime = android.text.format.DateFormat
                .getTimeFormat(context).format(ms);
        return context.getString(R.string.exam_result_date, date, localizedTime);
    }

    public static String getExamTypeName(Context context, String examType) {
        switch (examType) {
            case Exam.KEY_LITE:
                return context.getString(R.string.version_vca_lite);
            case Exam.KEY_BASIC:
                return context.getString(R.string.version_vca_b);
            case Exam.KEY_VOL:
                return context.getString(R.string.version_vca_vol);
            case Exam.KEY_VIL:
                return context.getString(R.string.version_vcu_vil);
        }
        return "";
    }
}
