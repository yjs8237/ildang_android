package com.jscompany.ildang.fcmPush;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class RegistrationIntentService extends IntentService{

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
