package nl.kimplusdelta.vca.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import nl.kimplusdelta.vca.billing.IabHelper;
import nl.kimplusdelta.vca.billing.IabResult;
import nl.kimplusdelta.vca.billing.Inventory;
import nl.kimplusdelta.vca.billing.Purchase;
import nl.kimplusdelta.vca.interfaces.OnBillingListener;

public class BillingActivity extends AppCompatActivity {
    protected static final String SKU_VCA_B = "vca_b";
    protected static final String SKU_VCA_VOL = "vca_vol";
    protected static final String SKU_VCU_VIL = "vcu_vil";

    // (arbitrary) request code for the purchase flow
    private static final int RC_REQUEST = 10001;

    // The helper object
    private IabHelper mHelper;

    protected Inventory mInventory;

    protected OnBillingListener mBillingListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBilling();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mHelper == null) return;

        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void initBilling() {
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt4VRL+QkmomarWUTtT0NQ5cQlUsRSTeVeiLbKIfRq+GEmqt3kw0AdPx5/tjRZgJkt7ZP4rucsQoMOisHPezGV7l2nLLA+bqcw9/5iE3PZsi26uDnbZpFMwKEAHl46kBLtMTaA7K21Zf5LHBoK49MhNF75M9RgjmOUanGWQ+4UOiI0dmqtlyYYO63aFe86VDB5yMkXQr37UWUC9fGt4aPKNodyZS11js41MOrbCLnVkMRHIPPueGbVGvKLEFPFF9Er2wT022iFQc5psKBbgQ+sV2BQpvs6rzvBKF6wIINv6qNYjvDO1Kh8HSauD+r2DtDWKk8hH/K1oIRs9CjI+K3vQIDAQAB";
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d("CC", "Unsuccesfull billing init: " + result.getMessage());
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    protected void showIAP(String sku) {
        if(!sku.equals("")) {
            mHelper.launchPurchaseFlow(this, sku, RC_REQUEST,
                    mPurchaseFinishedListener, "");
        }
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    private final IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) {
                Log.d("CC", "helper is null");
                return;
            }

            // Is it a failure?
            if (result.isFailure()) {
                Log.d("CC", "Result: " + result.isFailure() + " - " + result.getMessage() + " - " + result.getResponse());
                return;
            }

            mInventory = inventory;

            if(mBillingListener != null) {
                mBillingListener.onQueryInventoryFinished();
            }
        }
    };

    // Callback for when a purchase is finished
    private final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            // if we were disposed of in the meantime, quit.
            if (mHelper == null) {
                Log.d("CC", "helper is null");
                return;
            }

            if(mBillingListener != null) {
                mBillingListener.onIabPurchaseFinished(result);
            }

            if (result.isFailure()) {
                Log.d("CC", "Result: " + result.isFailure() + " - " + result.getMessage() + " - " + result.getResponse());
                return;
            }

            mHelper.queryInventoryAsync(mGotInventoryListener);
        }
    };
}
