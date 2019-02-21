package nl.kimplusdelta.vca.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.models.exam.Exam;

public class TutorialDialog {

    private final PopupWindow mPopupWindow;
    private final View mLayout;

    private final tutorialListener mListener;
    public interface tutorialListener {
        void closed();
    }

    public TutorialDialog(Context context, String examType, int entryCount, tutorialListener listener) {
        this.mListener = listener;
        this.mLayout = View.inflate(context, R.layout.tutorial_dialog, null);
        this.mPopupWindow = new PopupWindow(mLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(
                context.getResources(),
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        ));

        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        init(context, examType, entryCount);

        // simple click button
        mPopupWindow.getContentView().findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mListener.closed();
            }
        });
    }

    public void show() {
        mLayout.post(showRunnable);
    }

    private void init(Context context, String examType, int entryCount) {
        TextView content = (TextView) mLayout.findViewById(R.id.contentLabel);
        int questionCount = Exam.getExamEntryCount(examType);
        int minCount = (int)Math.ceil((float)questionCount * 0.7f);

        // get correct plural
        String plurals = context.getResources().getQuantityString(R.plurals.proefexamenvragen, entryCount, entryCount);

        // get correct text
        if(examType.equals(Exam.KEY_LITE)) {
            // set text
            content.setText(context.getString(R.string.tutorial_text_lite, plurals));
        } else {
            // set text
            content.setText(context.getString(R.string.tutorial_text_pro, plurals,
                    getTranslatedExamType(context, examType),
                    minCount, questionCount));
        }
    }

    private String getTranslatedExamType(Context context, String examType) {
        switch(examType) {
            case Exam.KEY_BASIC: return context.getString(R.string.version_vca_b);
            case Exam.KEY_VOL: return context.getString(R.string.version_vca_vol);
            case Exam.KEY_VIL: return context.getString(R.string.version_vcu_vil);
        }
        return context.getString(R.string.version_vca_lite);
    }

    private final Runnable showRunnable = new Runnable() {
        @Override
        public void run() {
            mPopupWindow.showAtLocation(mLayout, Gravity.CENTER, 0, 0);
        }
    };
}
