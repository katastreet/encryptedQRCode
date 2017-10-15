package com.example.satyarth.encryptedqrcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.mockito.internal.util.collections.ArrayUtils;
import org.spongycastle.util.encoders.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.security.PublicKey;

import javax.crypto.SecretKey;

public class GeneratorActivity extends AppCompatActivity {
    private static final String TAG = "permission";
    private final String name = "QR Generator";
    private EditText generatorText;
    private Button generateButton;
    private ImageView qrImageView;
    private EditText publicKey;
    public int noPubKey = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        getSupportActionBar().setTitle(name);


        generatorText = (EditText) findViewById(R.id.inputText);
        generateButton = (Button)findViewById(R.id.generate);
        qrImageView = (ImageView) findViewById(R.id.qr_image);
        publicKey = (EditText) findViewById(R.id.publicKey);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noPubKey = 0;

                String message = generatorText.getText().toString().trim();
                String pubKey=publicKey.getText().toString();




                try{
                    if(!pubKey.matches(""))
                    {
                        ECCipher ecCipher = new ECCipher();

                        PreferenceManager preferenceManager = new PreferenceManager(GeneratorActivity.this);

                        String publicKey = preferenceManager.pref.getString(PreferenceManager.PUBLIC_KEY, "error");

                        try {
                            PublicKey publicKeySpec = ECCipher.genPublicKeyFromString(pubKey);
                            message = new String(Base64.encode(ecCipher.encrypt(publicKeySpec, message)));
                            noPubKey = 1;
                        }
                        catch(Exception e){
                            Toast.makeText(GeneratorActivity.this, "invalid pub key", Toast.LENGTH_SHORT).show();
                            throw e;
                        }
                    }
                    else{
                        Toast.makeText(GeneratorActivity.this, "empty pub key", Toast.LENGTH_SHORT).show();
                    }
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                    String finalMessage ="";
                    if(noPubKey != 0){
                        finalMessage = "qr://" + message;
                    }
                    else{
                        finalMessage = message;
                    }

                    BitMatrix bitMatrix = multiFormatWriter.encode(finalMessage, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    qrImageView.setImageBitmap(bitmap);

                }
                catch(WriterException e){
                    e.printStackTrace();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        qrImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrImageView.setDrawingCacheEnabled(true);

                Bitmap bitmap = qrImageView.getDrawingCache();
                File root = Environment.getExternalStorageDirectory();
                File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                verifyStoragePermissions(GeneratorActivity.this);
                try {
                    cachePath.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(cachePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
//                    Toast.makeText(GeneratorActivity.this,"permission error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
                startActivity(Intent.createChooser(share, "Share via"));
            }
        });


    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
