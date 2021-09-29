package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import model.Event;
import model.Person;

public class EventActivity extends AppCompatActivity {
    private static final String EVENT_ID = "eventID";

    private static Event event;
    private static Person person;

    private GoogleMapsFragment fragment;

    private DataCache cache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra(EVENT_ID);
        event = cache.getEvent(eventID);
        cache.setDisplayingEvent(event);

        String personID = event.getPersonID();
        person = cache.getPerson(personID);
        cache.setDisplayingPerson(person);

        FragmentManager fm = this.getSupportFragmentManager();
        fragment = new GoogleMapsFragment();
        fm.beginTransaction().add(R.id.mapFrameLayout, fragment).commit();

//        fragment = (GoogleMapsFragment) fm.findFragmentById(R.id.mapFrameLayout);
//        if (fragment == null) {
//            fragment = new GoogleMapsFragment();
//            Bundle args = new Bundle();
//            fragment.setArguments(args);
//            fm.beginTransaction().replace(R.id.mapFrameLayout, fragment).commit();
//        }
    }
}
