package com.example.satyarth.encryptedqrcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private final String name = "Settings";
    private TextView username;
    private TextView public_key;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(name);

        username = (TextView) findViewById(R.id.username);
        public_key = (TextView) findViewById(R.id.public_key);

        preferenceManager = new PreferenceManager(this);

        username.setText(preferenceManager.pref.getString(PreferenceManager.USER_NAME, "error"));
        public_key.setText(preferenceManager.pref.getString(PreferenceManager.PUBLIC_KEY, "error"));

    }
}
