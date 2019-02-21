// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends nl.kimplusdelta.vca.activities.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165305, "field 'mLiteButton' and method 'onLiteExamClicked'");
    target.mLiteButton = finder.castView(view, 2131165305, "field 'mLiteButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onLiteExamClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165217, "field 'mBasisButton' and method 'onBExamClicked'");
    target.mBasisButton = finder.castView(view, 2131165217, "field 'mBasisButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onBExamClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165428, "field 'mVOLButton' and method 'onVOLExamClicked'");
    target.mVOLButton = finder.castView(view, 2131165428, "field 'mVOLButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onVOLExamClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165424, "field 'mVILButton' and method 'onVILExamClicked'");
    target.mVILButton = finder.castView(view, 2131165424, "field 'mVILButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onVILExamClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165219, "field 'mBasisExamLabel'");
    target.mBasisExamLabel = finder.castView(view, 2131165219, "field 'mBasisExamLabel'");
    view = finder.findRequiredView(source, 2131165220, "field 'mBasisExamDescriptionLabel'");
    target.mBasisExamDescriptionLabel = finder.castView(view, 2131165220, "field 'mBasisExamDescriptionLabel'");
    view = finder.findRequiredView(source, 2131165430, "field 'mVOLExamLabel'");
    target.mVOLExamLabel = finder.castView(view, 2131165430, "field 'mVOLExamLabel'");
    view = finder.findRequiredView(source, 2131165431, "field 'mVolExamDescriptionLabel'");
    target.mVolExamDescriptionLabel = finder.castView(view, 2131165431, "field 'mVolExamDescriptionLabel'");
    view = finder.findRequiredView(source, 2131165426, "field 'mVILExamLabel'");
    target.mVILExamLabel = finder.castView(view, 2131165426, "field 'mVILExamLabel'");
    view = finder.findRequiredView(source, 2131165427, "field 'mVilExamDescriptionLabel'");
    target.mVilExamDescriptionLabel = finder.castView(view, 2131165427, "field 'mVilExamDescriptionLabel'");
  }

  @Override public void unbind(T target) {
    target.mLiteButton = null;
    target.mBasisButton = null;
    target.mVOLButton = null;
    target.mVILButton = null;
    target.mBasisExamLabel = null;
    target.mBasisExamDescriptionLabel = null;
    target.mVOLExamLabel = null;
    target.mVolExamDescriptionLabel = null;
    target.mVILExamLabel = null;
    target.mVilExamDescriptionLabel = null;
  }
}
