package com.example.satyarth.encryptedqrcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import org.spongycastle.util.encoders.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class WelcomeScreen extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private Button loginButton;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(this);
        if(preferenceManager.isFirstTimeLaunch()){
            launchHomeScreen();
            finish();
        }
        setContentView(R.layout.activity_welcome_screen);

        username = (EditText)findViewById(R.id.username);

        // if first time logged in generates key
        generateButtonListener();

    }


    private void launchHomeScreen(){
        Intent generatorIntent = new Intent(WelcomeScreen.this, MainUi.class);
        startActivity(generatorIntent);
        finish();
    }


    private void generateButtonListener() {
        loginButton = (Button) findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = username.getText().toString();
                if(uname == ""){
                    return;
                }
                else{
                    try {
                        //using elleptic curve cipher

                        KeyPair keyPair = ECCipher.generateKeyPair();
                        String publicKey = new String(Base64.encode(keyPair.getPublic().getEncoded()));
                        String privateKey = new String(Base64.encode(keyPair.getPrivate().getEncoded()));
                        preferenceManager.setFirstTimeLauch(uname, privateKey, publicKey);


                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    }

                }

                launchHomeScreen();

            }
        });

    }
}
