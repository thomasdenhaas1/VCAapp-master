package nl.kimplusdelta.vca.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.models.adapterItems.ExamQuestionItem;

public class QuestionAdapter extends ArrayAdapter<ExamQuestionItem> {

    private final Context mContext;
    private final List<ExamQuestionItem> mExamItems;

    public QuestionAdapter(Context context, List<ExamQuestionItem> categories) {
        super(context, R.layout.question_list_item, categories);

        this.mContext = context;
        this.mExamItems = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.question_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        ExamQuestionItem item = getItem(position);

        // question
        holder.questionLabel.setText(item.question);

        // answer
        holder.answerLabel.setText(item.answer);
        holder.answerLabel.setTextColor(item.correct ?
                ContextCompat.getColor(mContext, R.color.correct_answer) :
                ContextCompat.getColor(mContext, R.color.wrong_answer));

        return convertView;
    }

    @Override
    public ExamQuestionItem getItem(int position) {
        return mExamItems.get(position);
    }

    static class ViewHolder {
        @Bind(R.id.questionLabel) TextView questionLabel;
        @Bind(R.id.answerLabel) TextView answerLabel;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
