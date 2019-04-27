package com.example.kbrent.myapplication.HTTP;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import android.util.Log;

import org.json.JSONObject;

public class HttpClient {

    public static String postUrl(URL url, JSONObject object) {

        try {

            if(object == null) {
                Log.e("HTTP", "null");
            }

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // Post request body output stream
            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(object.toString().getBytes());
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();
                return responseBodyData;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public static String getUrl(URL url, String token) {
        try {

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            String basicAuth = token;
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                Log.e("HTTP", "success");
                InputStream responseBody = connection.getInputStream();


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();
                return responseBodyData;
            }
        }
        catch (Exception e) {
            Log.e("HTTP", e.getMessage() + e.toString());
            return "";
        }

        return "";
    }

}
