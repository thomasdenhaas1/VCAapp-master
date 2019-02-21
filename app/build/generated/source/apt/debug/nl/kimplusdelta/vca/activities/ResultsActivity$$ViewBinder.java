// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ResultsActivity$$ViewBinder<T extends nl.kimplusdelta.vca.activities.ResultsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165303, "field 'mListView'");
    target.mListView = finder.castView(view, 2131165303, "field 'mListView'");
    view = finder.findRequiredView(source, 2131165340, "field 'mResultLabel'");
    target.mResultLabel = finder.castView(view, 2131165340, "field 'mResultLabel'");
    view = finder.findRequiredView(source, 2131165343, "field 'mScoreLabel'");
    target.mScoreLabel = finder.castView(view, 2131165343, "field 'mScoreLabel'");
    view = finder.findRequiredView(source, 2131165321, "field 'mPercentageLabel'");
    target.mPercentageLabel = finder.castView(view, 2131165321, "field 'mPercentageLabel'");
    view = finder.findRequiredView(source, 2131165339, "field 'mMissingScoreLabel'");
    target.mMissingScoreLabel = finder.castView(view, 2131165339, "field 'mMissingScoreLabel'");
    view = finder.findRequiredView(source, 2131165411, "field 'mTimeLabel'");
    target.mTimeLabel = finder.castView(view, 2131165411, "field 'mTimeLabel'");
    view = finder.findRequiredView(source, 2131165359, "method 'onShareClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onShareClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165333, "method 'onRateClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onRateClicked();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mListView = null;
    target.mResultLabel = null;
    target.mScoreLabel = null;
    target.mPercentageLabel = null;
    target.mMissingScoreLabel = null;
    target.mTimeLabel = null;
  }
}
