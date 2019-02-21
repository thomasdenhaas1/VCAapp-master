// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class QuestionListActivity$$ViewBinder<T extends nl.kimplusdelta.vca.activities.QuestionListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165228, "field 'mCategoryLabel'");
    target.mCategoryLabel = finder.castView(view, 2131165228, "field 'mCategoryLabel'");
  }

  @Override public void unbind(T target) {
    target.mCategoryLabel = null;
  }
}
