package nl.kimplusdelta.vca.interfaces;

import nl.kimplusdelta.vca.billing.IabResult;

public interface OnBillingListener {
    void onIabPurchaseFinished(IabResult result);
    void onQueryInventoryFinished();
}
