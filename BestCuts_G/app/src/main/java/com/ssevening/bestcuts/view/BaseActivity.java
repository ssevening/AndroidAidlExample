package com.ssevening.bestcuts.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ssevening.bestcuts.object.Track;

public class BaseActivity extends AppCompatActivity implements Track {
    private static FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(getPageName(), null);
    }

    @Override
    public String getPageName() {
        return null;
    }
}
