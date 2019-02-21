package nl.kimplusdelta.vca.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.models.adapterItems.ExamHistoryItem;
import nl.kimplusdelta.vca.models.exam.Exam;
import nl.kimplusdelta.vca.utils.Utils;

public class HistoryAdapter extends ArrayAdapter<ExamHistoryItem> {

    private final Context mContext;
    private final List<ExamHistoryItem> mExamItems;

    public HistoryAdapter(Context context, List<ExamHistoryItem> categories) {
        super(context, R.layout.history_list_item, categories);

        this.mContext = context;
        this.mExamItems = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.history_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        ExamHistoryItem item = getItem(position);

        // type
        holder.typeLabel.setText(Utils.getExamTypeName(mContext, item.type));

        if(item.type.equalsIgnoreCase(Exam.KEY_LITE)){

            holder.mImageviewIcon.setImageResource(R.drawable.icon_white_greats);
            holder.mImageviewIcon.setBackgroundResource(R.drawable.circle_gratis);
            holder.typeLabel.setTextColor(ContextCompat.getColor(mContext,R.color.color_gratis));

        }else if(item.type.equalsIgnoreCase(Exam.KEY_BASIC)){
            holder.mImageviewIcon.setImageResource(R.drawable.icon_white_basis);
            holder.mImageviewIcon.setBackgroundResource(R.drawable.circle_basic);
            holder.typeLabel.setTextColor(ContextCompat.getColor(mContext,R.color.color_basic));

        }else if(item.type.equalsIgnoreCase(Exam.KEY_VIL)){
            holder.mImageviewIcon.setImageResource(R.drawable.icon_white_vil);
            holder.mImageviewIcon.setBackgroundResource(R.drawable.circle_vil);
            holder.typeLabel.setTextColor(ContextCompat.getColor(mContext,R.color.color_vil));

        }else if(item.type.equalsIgnoreCase(Exam.KEY_VOL)){
            holder.mImageviewIcon.setImageResource(R.drawable.icon_white_vol);
            holder.mImageviewIcon.setBackgroundResource(R.drawable.circle_vol);
            holder.typeLabel.setTextColor(ContextCompat.getColor(mContext,R.color.color_vol));
        }

        // score
        holder.scoreLabel.setText(mContext.getString(R.string.exam_score_format, item.correct, item.total));

        // result
        boolean passed = (item.correct >= (item.total * 0.7f));
        holder.successLabel.setText(passed ? mContext.getString(R.string.exam_history_successful) : mContext.getString(R.string.exam_history_failed));
        holder.successLabel.setTextColor(passed ? ContextCompat.getColor(mContext, R.color.correct_answer) : ContextCompat.getColor(mContext, R.color.wrong_answer));

        // date
        holder.dateLabel.setText(Utils.getDateInFormat(mContext, item.date));

        // time
        holder.timeLabel.setText(Utils.getTimeInFormat(item.time));

        // progress bar
        int score = Math.round(((float)item.correct / (float)item.total) * 100.0f);
        holder.progressBar.setProgress(score);

        return convertView;
    }

    @Override
    public ExamHistoryItem getItem(int position) {
        return mExamItems.get(position);
    }

    static class ViewHolder {
        @Bind(R.id.imageview_icon) ImageView mImageviewIcon;
        @Bind(R.id.typeLabel) TextView typeLabel;
        @Bind(R.id.scoreLabel) TextView scoreLabel;
        @Bind(R.id.successLabel) TextView successLabel;
        @Bind(R.id.timeLabel) TextView timeLabel;
        @Bind(R.id.infoLabel) TextView dateLabel;
        @Bind(R.id.progressBar) ProgressBar progressBar;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


