package com.example.satyarth.encryptedqrcode;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by satyarth on 10/20/17.
 */

public class APICommunication {


    public static final String addUrl="http://192.168.43.62:8080/qr_api/user";
    public static final String fetchUrl="http://192.168.43.62:8080/qr_api/getuser";



    public void addUsername(String username, String publicKey)throws Exception{
        if(searchUsername(username).equals("")){
            String myData = "?username="+ URLEncoder.encode(username, "UTF-8") + "&publicKey=" + URLEncoder.encode(publicKey, "UTF-8");
            URL fetchEndPoint = new URL(addUrl + myData);
            HttpURLConnection fetchConnection = (HttpURLConnection) fetchEndPoint.openConnection();
            if(fetchConnection.getResponseCode() == 200) {
            }
            else{
                throw new Exception("unable to register");
            }
        }
        else{
            throw new Exception("already registered");
        }

    }

    //returns publicKey if available else outputs empty string . throws exception on connection error

    public String searchUsername(String username) throws IOException, JSONException {
        String output = "";

        String myData = "?username="+ URLEncoder.encode(username, "UTF-8");
        URL fetchEndPoint = new URL(fetchUrl + myData);

        HttpURLConnection fetchConnection = (HttpURLConnection) fetchEndPoint.openConnection();

        System.out.println(fetchConnection.toString());

        if(fetchConnection.getResponseCode() == 200){
            InputStream responseBody = fetchConnection.getInputStream();
            InputStreamReader responseBodyReader =
                    new InputStreamReader(responseBody, "UTF-8");

            JsonReader jsonReader = new JsonReader(responseBodyReader);
            try {
                jsonReader.beginObject();
            }
            catch (Exception e){
                //returns empty output
                return output;
            }
            while(jsonReader.hasNext()){
                if(jsonReader.nextName().equals("publicKey")){
                    output = jsonReader.nextString();
                }
                else{
                    //you should always skip value or expected String error
                    jsonReader.skipValue();
                }
            }

        }
        else{
            return "error";
        }
        return output;

    }
}
