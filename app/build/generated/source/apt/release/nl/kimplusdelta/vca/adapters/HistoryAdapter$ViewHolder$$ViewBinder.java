// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HistoryAdapter$ViewHolder$$ViewBinder<T extends nl.kimplusdelta.vca.adapters.HistoryAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165285, "field 'mImageviewIcon'");
    target.mImageviewIcon = finder.castView(view, 2131165285, "field 'mImageviewIcon'");
    view = finder.findRequiredView(source, 2131165419, "field 'typeLabel'");
    target.typeLabel = finder.castView(view, 2131165419, "field 'typeLabel'");
    view = finder.findRequiredView(source, 2131165343, "field 'scoreLabel'");
    target.scoreLabel = finder.castView(view, 2131165343, "field 'scoreLabel'");
    view = finder.findRequiredView(source, 2131165380, "field 'successLabel'");
    target.successLabel = finder.castView(view, 2131165380, "field 'successLabel'");
    view = finder.findRequiredView(source, 2131165411, "field 'timeLabel'");
    target.timeLabel = finder.castView(view, 2131165411, "field 'timeLabel'");
    view = finder.findRequiredView(source, 2131165293, "field 'dateLabel'");
    target.dateLabel = finder.castView(view, 2131165293, "field 'dateLabel'");
    view = finder.findRequiredView(source, 2131165323, "field 'progressBar'");
    target.progressBar = finder.castView(view, 2131165323, "field 'progressBar'");
  }

  @Override public void unbind(T target) {
    target.mImageviewIcon = null;
    target.typeLabel = null;
    target.scoreLabel = null;
    target.successLabel = null;
    target.timeLabel = null;
    target.dateLabel = null;
    target.progressBar = null;
  }
}
