package com.example.popin.logic.service;

import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import com.example.popin.BuildConfig;

public class SMServicer {
    private static final String UTF_8 = "UTF-8";

    private SMServicer() {
    }

    public static void sendSMS(String toPhone, String messageText) {

        new Thread(() -> {
            try {
                String ACCOUNT_SID = BuildConfig.TWILIO_ACCOUNT_SID;
                String AUTH_TOKEN = BuildConfig.TWILIO_AUTH_TOKEN;
                String FROM = BuildConfig.TWILIO_PHONE_NUMBER;

                String urlStr = "https://api.twilio.com/2010-04-01/Accounts/"
                        + ACCOUNT_SID + "/Messages.json";

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String auth = ACCOUNT_SID + ":" + AUTH_TOKEN;
                String encodedAuth = android.util.Base64.encodeToString(
                        auth.getBytes(), android.util.Base64.NO_WRAP);

                conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = "To=" + URLEncoder.encode(toPhone, UTF_8) +
                        "&From=" + URLEncoder.encode(FROM, UTF_8) +
                        "&Body=" + URLEncoder.encode(messageText, UTF_8);

                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d("SMS", "Response: " + responseCode);

            } catch (Exception e) {
                Log.e("SMS", "Error", e);
            }
        }).start();
    }

}

