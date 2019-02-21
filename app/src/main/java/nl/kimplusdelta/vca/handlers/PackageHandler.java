package nl.kimplusdelta.vca.handlers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class PackageHandler {

    private final Activity mActivity;

    public PackageHandler(Activity activity) {
        this.mActivity = activity;
    }

    public void openApp(String appPackageName) {
        try {
            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
