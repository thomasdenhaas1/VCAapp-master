package nl.kimplusdelta.vca.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import nl.kimplusdelta.vca.R;

public class ExamAnswerView {

    @Bind(R.id.answerLabel) public TextView answerLabel;
    @Bind(R.id.indicatorLabel) public TextView indicatorLabel;
    @Bind(R.id.indicatorColor) public FrameLayout indicatorColorLayout;

    private final RelativeLayout answerHolder;

    private final AnswerCallback answerCallback;
    private final Context mContext;

    private final int defaultIndicatorTextColor;

    private final int mIndex;

    public interface AnswerCallback {
        void onClicked(int index);
    }

    public ExamAnswerView(Context context, View v, int index, boolean clickable) {
        ButterKnife.bind(this, v);

        defaultIndicatorTextColor = indicatorLabel.getTextColors().getDefaultColor();

        mContext = context;
        mIndex = index;
        answerCallback = (AnswerCallback)context;

        indicatorLabel.setText(getIndicatorText(index));

        answerHolder = (RelativeLayout)v;

        if(clickable) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answerCallback.onClicked(mIndex);
                }
            });
        }
    }

    public void setAsSelected() {
        indicatorColorLayout.setVisibility(View.VISIBLE);
    }

    public void setColor(Drawable indicatorColor) {
        this.answerHolder.setBackground(indicatorColor);

        int color = ContextCompat.getColor(mContext, android.R.color.white);
        this.indicatorLabel.setTextColor(color);
        this.answerLabel.setTextColor(color);
    }

    public void reset() {
        this.answerHolder.setBackground(ContextCompat.getDrawable(mContext, R.drawable.floating_box));
        this.indicatorLabel.setTextColor(defaultIndicatorTextColor);
        this.answerLabel.setTextColor(defaultIndicatorTextColor);

        indicatorColorLayout.setVisibility(View.INVISIBLE);
    }

    private String getIndicatorText(int index) {
        switch(index) {
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
        }

        return "";
    }
}
