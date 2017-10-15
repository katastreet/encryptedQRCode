package com.example.satyarth.encryptedqrcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainUi extends AppCompatActivity {
    private Button scanButton;
    private Button generateButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);

        scannerButtonListener();
        generateButtonListener();
        settingsButtonListener();


    }

    private void scannerButtonListener() {
        scanButton = (Button) findViewById(R.id.scan_qr);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scannerIntent = new Intent(MainUi.this, ScannerActivity.class);
                startActivity(scannerIntent);

            }
        });
    }

    private void generateButtonListener() {
        generateButton = (Button) findViewById(R.id.generate_qr);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generatorIntent = new Intent(MainUi.this, GeneratorActivity.class);
                startActivity(generatorIntent);

            }
        });

    }

    private void settingsButtonListener() {
        settingsButton = (Button) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainUi.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
    }
}
