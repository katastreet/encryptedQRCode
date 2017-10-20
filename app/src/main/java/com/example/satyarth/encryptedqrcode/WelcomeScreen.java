package com.example.satyarth.encryptedqrcode;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                        //using elliptic curve cipher

                        KeyPair keyPair = ECCipher.generateKeyPair();
                        String publicKey = new String(Base64.encode(keyPair.getPublic().getEncoded()));
                        String privateKey = new String(Base64.encode(keyPair.getPrivate().getEncoded()));

                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute(uname, publicKey,privateKey);


                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String username;
        private String publicKey;
        private String privateKey;
        private Exception asyncException;

        @Override
        protected String doInBackground(String... strings) {
            APICommunication apiCommunication = new APICommunication();
            username = strings[0];
            publicKey = strings[1];
            privateKey = strings[2];
            try {
                apiCommunication.addUsername(username, publicKey);
            } catch (Exception e) {
                asyncException = e;
                e.printStackTrace();
            }
//            try {
//                String output = apiCommunication.searchUsername("Ram");
//                if(output.equals("")){
//                    System.out.println("unregistered");
//                }
//                else{
//                    System.out.println(output);
//                }
//            } catch (Exception e) {
//                System.out.println("unable to fetch");
//                e.printStackTrace();
//            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            if(asyncException != null){
                Toast.makeText(WelcomeScreen.this, asyncException.getMessage(),Toast.LENGTH_SHORT).show();
                return;
            }
            if (s.equals("success")) {
                preferenceManager.setFirstTimeLauch(username, privateKey, publicKey);
                launchHomeScreen();
            }

            }
        }
    }

