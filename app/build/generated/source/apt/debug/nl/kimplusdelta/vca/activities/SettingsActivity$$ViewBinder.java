// Generated code from Butter Knife. Do not modify!
package nl.kimplusdelta.vca.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingsActivity$$ViewBinder<T extends nl.kimplusdelta.vca.activities.SettingsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165367, "field 'mSleepCheckbox'");
    target.mSleepCheckbox = finder.castView(view, 2131165367, "field 'mSleepCheckbox'");
    view = finder.findRequiredView(source, 2131165368, "method 'onSleepClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onSleepClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165334, "method 'onRateMeClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onRateMeClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165362, "method 'onShareClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onShareClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165242, "method 'onContactClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onContactClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165253, "method 'onDeleteHistoryClicked'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onDeleteHistoryClicked();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mSleepCheckbox = null;
  }
}
