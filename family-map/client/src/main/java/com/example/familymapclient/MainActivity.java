package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.familymapclient.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private GoogleMapsFragment mapFragment;

    private LoginFragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void launchLoginFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        loginFragment = (LoginFragment) fm.findFragmentById(R.id.loginFrameLayout);
        if (loginFragment == null) {
            loginFragment = createLoginFragment();
            fm.beginTransaction().add(R.id.loginFrameLayout, loginFragment).commit();
        }
    }

    private GoogleMapsFragment createMapFragment() {
        GoogleMapsFragment fragment = new GoogleMapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void launchMapFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = (GoogleMapsFragment) fm.findFragmentById(R.id.mapFrameLayout);
        if (mapFragment == null) {
            mapFragment = createMapFragment();
            fm.beginTransaction().replace(R.id.loginFrameLayout, mapFragment).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataCache cache = DataCache.getInstance();
        if (cache.isLoggedIn()) {
            launchMapFragment();
        }
        else {
            launchLoginFragment();
        }

        Iconify.with(new FontAwesomeModule());
    }
}