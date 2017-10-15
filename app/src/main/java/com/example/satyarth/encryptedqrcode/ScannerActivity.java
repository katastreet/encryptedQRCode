package com.example.satyarth.encryptedqrcode;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.spongycastle.util.encoders.Base64;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ScannerActivity extends AppCompatActivity {
    private final String name = "QR Scanner";
    private Button scanBtn;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        getSupportActionBar().setTitle(name);

        scanBtn = (Button) findViewById(R.id.scan_button);
        resultTextView = (TextView) findViewById(R.id.result);

        final Activity activity = this;

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultTextView.setText("Data Not scanned");
                IntentIntegrator qrCodeIntegrator = new IntentIntegrator(activity);
                qrCodeIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                qrCodeIntegrator.setPrompt("Scan a QR Code");
                qrCodeIntegrator.setCameraId(0); // use specific camera of the device
                qrCodeIntegrator.setBeepEnabled(false);
                qrCodeIntegrator.setBarcodeImageEnabled(false);
                qrCodeIntegrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult qrScanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(qrScanResult != null){
            if(qrScanResult.getContents() ==  null){
                Toast.makeText(this, "Scanning Cancelled", Toast.LENGTH_LONG).show();
            }
            else if(!qrScanResult.getContents().contains("qr://")){
                resultTextView.setText(qrScanResult.getContents());
            }
            else{
                String result = qrScanResult.getContents();
                    result = result.substring(5);
                    byte[] decodedResult = Base64.decode(result.getBytes());

                    ECCipher ecCipher = new ECCipher();

                    PreferenceManager preferenceManager = new PreferenceManager(ScannerActivity.this);

                    String privateKey = preferenceManager.pref.getString(PreferenceManager.PRIVATE_KEY, "error");

                    try {
                        PrivateKey privateKeySpec = ECCipher.genPrivateKeyFromString(privateKey);
                        String message = new String(ecCipher.decrypt(privateKeySpec, decodedResult));
                        result = message;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ScannerActivity.this, "private key error reinstall app", Toast.LENGTH_SHORT).show();
                    }
                    resultTextView.setText(result);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
