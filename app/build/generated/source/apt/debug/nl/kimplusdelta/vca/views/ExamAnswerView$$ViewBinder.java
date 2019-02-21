// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.views;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ExamAnswerView$$ViewBinder<T extends nl.kimplusdelta.vca.views.ExamAnswerView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165215, "field 'answerLabel'");
    target.answerLabel = finder.castView(view, 2131165215, "field 'answerLabel'");
    view = finder.findRequiredView(source, 2131165290, "field 'indicatorLabel'");
    target.indicatorLabel = finder.castView(view, 2131165290, "field 'indicatorLabel'");
    view = finder.findRequiredView(source, 2131165288, "field 'indicatorColorLayout'");
    target.indicatorColorLayout = finder.castView(view, 2131165288, "field 'indicatorColorLayout'");
  }

  @Override public void unbind(T target) {
    target.answerLabel = null;
    target.indicatorLabel = null;
    target.indicatorColorLayout = null;
  }
}
