// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class QuestionAdapter$ViewHolder$$ViewBinder<T extends nl.kimplusdelta.vca.adapters.QuestionAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165331, "field 'questionLabel'");
    target.questionLabel = finder.castView(view, 2131165331, "field 'questionLabel'");
    view = finder.findRequiredView(source, 2131165215, "field 'answerLabel'");
    target.answerLabel = finder.castView(view, 2131165215, "field 'answerLabel'");
  }

  @Override public void unbind(T target) {
    target.questionLabel = null;
    target.answerLabel = null;
  }
}
