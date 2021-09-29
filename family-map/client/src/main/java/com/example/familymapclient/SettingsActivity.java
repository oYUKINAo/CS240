package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private DataCache cache;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cache = DataCache.getInstance();

        Switch lifeStoryLineSwitch = findViewById(R.id.LifeStoryLinesSwitch);
        lifeStoryLineSwitch.setChecked(cache.hasLifeStoryLines());
        lifeStoryLineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setLifeStoryLines(isChecked);
            }
        });

        Switch familyTreeLinesSwitch = findViewById(R.id.FamilyTreeLinesSwitch);
        familyTreeLinesSwitch.setChecked(cache.hasFamilyTreeLines());
        familyTreeLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setFamilyTreeLines(isChecked);
            }
        });

        Switch spouseLineSwitch = findViewById(R.id.SpouseLineSwitch);
        spouseLineSwitch.setChecked(cache.hasSpouseLine());
        spouseLineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setSpouseLine(isChecked);
            }
        });

        Switch dadSideSwitch = findViewById(R.id.DadSideSwitch);
        dadSideSwitch.setChecked(cache.isPaternal());
        dadSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setPaternal(isChecked);
            }
        });

        Switch momSideSwitch = findViewById(R.id.MomSideSwitch);
        momSideSwitch.setChecked(cache.isMaternal());
        momSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setMaternal(isChecked);
            }
        });

        Switch maleEventsSwitch = findViewById(R.id.MaleEventsSwitch);
        maleEventsSwitch.setChecked(cache.isMale());
        maleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setMale(isChecked);
            }
        });

        Switch femaleEventsSwitch = findViewById(R.id.FemaleEventsSwitch);
        femaleEventsSwitch.setChecked(cache.isFemale());
        femaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setFemale(isChecked);
            }
        });

        TextView logoutTextView = findViewById(R.id.LogoutTextView);
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cache.setLoggedIn(false);
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
