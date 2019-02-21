package nl.kimplusdelta.vca.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nl.kimplusdelta.vca.R;
import nl.kimplusdelta.vca.models.adapterItems.ExamResultItem;

public class ResultsAdapter extends ArrayAdapter<ExamResultItem> {

    private final Context mContext;
    private final List<ExamResultItem> mExamItems;

    public ResultsAdapter(Context context, List<ExamResultItem> categories) {
        super(context, R.layout.results_list_item, categories);

        this.mContext = context;
        this.mExamItems = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.results_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        ExamResultItem item = getItem(position);

        holder.categoryLabel.setText(item.category);
        holder.correctLabel.setText(String.format("%d", item.correct));
        holder.totalLabel.setText(String.format("%d", item.total));

        int score = Math.round(((float)item.correct / (float)item.total) * 100.0f);
        holder.scoreLabel.setText(String.format("%d%%", score));

        holder.progressBar.setProgress(score);

        return convertView;
    }

    @Override
    public ExamResultItem getItem(int position) {
        return mExamItems.get(position);
    }

    static class ViewHolder {
        @Bind(R.id.categoryLabel) TextView categoryLabel;
        @Bind(R.id.scoreLabel) TextView scoreLabel;
        @Bind(R.id.correctLabel) TextView correctLabel;
        @Bind(R.id.totalLabel) TextView totalLabel;
        @Bind(R.id.progressBar) ProgressBar progressBar;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
