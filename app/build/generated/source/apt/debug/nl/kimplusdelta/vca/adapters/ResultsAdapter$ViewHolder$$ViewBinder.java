// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ResultsAdapter$ViewHolder$$ViewBinder<T extends nl.kimplusdelta.vca.adapters.ResultsAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165228, "field 'categoryLabel'");
    target.categoryLabel = finder.castView(view, 2131165228, "field 'categoryLabel'");
    view = finder.findRequiredView(source, 2131165343, "field 'scoreLabel'");
    target.scoreLabel = finder.castView(view, 2131165343, "field 'scoreLabel'");
    view = finder.findRequiredView(source, 2131165247, "field 'correctLabel'");
    target.correctLabel = finder.castView(view, 2131165247, "field 'correctLabel'");
    view = finder.findRequiredView(source, 2131165418, "field 'totalLabel'");
    target.totalLabel = finder.castView(view, 2131165418, "field 'totalLabel'");
    view = finder.findRequiredView(source, 2131165323, "field 'progressBar'");
    target.progressBar = finder.castView(view, 2131165323, "field 'progressBar'");
  }

  @Override public void unbind(T target) {
    target.categoryLabel = null;
    target.scoreLabel = null;
    target.correctLabel = null;
    target.totalLabel = null;
    target.progressBar = null;
  }
}
