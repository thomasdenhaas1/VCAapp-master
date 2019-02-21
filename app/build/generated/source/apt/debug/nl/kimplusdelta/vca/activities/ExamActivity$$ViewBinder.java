// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ExamActivity$$ViewBinder<T extends nl.kimplusdelta.vca.activities.ExamActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165331, "field 'mQuestionLabel'");
    target.mQuestionLabel = finder.castView(view, 2131165331, "field 'mQuestionLabel'");
    view = finder.findRequiredView(source, 2131165378, "field 'mSubmitButton' and method 'nextEntry'");
    target.mSubmitButton = finder.castView(view, 2131165378, "field 'mSubmitButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.nextEntry();
        }
      });
    view = finder.findRequiredView(source, 2131165324, "field 'mProgressLabel'");
    target.mProgressLabel = finder.castView(view, 2131165324, "field 'mProgressLabel'");
    view = finder.findRequiredView(source, 2131165248, "field 'mCorrectScoreLabel'");
    target.mCorrectScoreLabel = finder.castView(view, 2131165248, "field 'mCorrectScoreLabel'");
    view = finder.findRequiredView(source, 2131165286, "field 'mInCorrectScoreLabel'");
    target.mInCorrectScoreLabel = finder.castView(view, 2131165286, "field 'mInCorrectScoreLabel'");
    view = finder.findRequiredView(source, 2131165228, "field 'mCategoryLabel'");
    target.mCategoryLabel = finder.castView(view, 2131165228, "field 'mCategoryLabel'");
    view = finder.findRequiredView(source, 2131165260, "field 'mTimer'");
    target.mTimer = finder.castView(view, 2131165260, "field 'mTimer'");
    view = finder.findRequiredView(source, 2131165330, "field 'mQuestionImageView'");
    target.mQuestionImageView = finder.castView(view, 2131165330, "field 'mQuestionImageView'");
    view = finder.findRequiredView(source, 2131165273, "field 'mImageHolder'");
    target.mImageHolder = finder.castView(view, 2131165273, "field 'mImageHolder'");
  }

  @Override public void unbind(T target) {
    target.mQuestionLabel = null;
    target.mSubmitButton = null;
    target.mProgressLabel = null;
    target.mCorrectScoreLabel = null;
    target.mInCorrectScoreLabel = null;
    target.mCategoryLabel = null;
    target.mTimer = null;
    target.mQuestionImageView = null;
    target.mImageHolder = null;
  }
}
