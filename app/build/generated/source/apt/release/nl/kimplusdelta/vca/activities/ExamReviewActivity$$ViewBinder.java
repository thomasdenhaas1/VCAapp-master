// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ExamReviewActivity$$ViewBinder<T extends nl.kimplusdelta.vca.activities.ExamReviewActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165416, "field 'mTopContainer'");
    target.mTopContainer = finder.castView(view, 2131165416, "field 'mTopContainer'");
    view = finder.findRequiredView(source, 2131165228, "field 'mCategoryLabel'");
    target.mCategoryLabel = finder.castView(view, 2131165228, "field 'mCategoryLabel'");
    view = finder.findRequiredView(source, 2131165331, "field 'mQuestionLabel'");
    target.mQuestionLabel = finder.castView(view, 2131165331, "field 'mQuestionLabel'");
    view = finder.findRequiredView(source, 2131165378, "field 'mSubmitButton'");
    target.mSubmitButton = finder.castView(view, 2131165378, "field 'mSubmitButton'");
    view = finder.findRequiredView(source, 2131165330, "field 'mQuestionImageView'");
    target.mQuestionImageView = finder.castView(view, 2131165330, "field 'mQuestionImageView'");
    view = finder.findRequiredView(source, 2131165273, "field 'mImageHolder'");
    target.mImageHolder = finder.castView(view, 2131165273, "field 'mImageHolder'");
  }

  @Override public void unbind(T target) {
    target.mTopContainer = null;
    target.mCategoryLabel = null;
    target.mQuestionLabel = null;
    target.mSubmitButton = null;
    target.mQuestionImageView = null;
    target.mImageHolder = null;
  }
}
