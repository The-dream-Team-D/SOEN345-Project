package com.example.popin.logic.service;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.popin.BuildConfig;

public class EmailServicer {
    public static void sendEmail(String toEmail, String subject, String message) {

        String apiKey = BuildConfig.SENDGRID_API_KEY;
        String fromEmail = "thedreamteamd94@gmail.com";

        new Thread(() -> {
            try {

                URL url = new URL("https://api.sendgrid.com/v3/mail/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                conn.setRequestProperty("Authorization", "Bearer " + apiKey);
                conn.setRequestProperty("Content-Type", "application/json");

                // Build JSON body
                JSONObject json = new JSONObject();

                JSONArray personalizations = new JSONArray();
                JSONObject personalization = new JSONObject();

                JSONArray toArray = new JSONArray();
                JSONObject toObj = new JSONObject();
                toObj.put("email", toEmail);
                toArray.put(toObj);

                personalization.put("to", toArray);
                personalization.put("subject", subject);
                personalizations.put(personalization);

                JSONObject from = new JSONObject();
                from.put("email", fromEmail);

                JSONArray contentArray = new JSONArray();
                JSONObject content = new JSONObject();
                content.put("type", "text/html");
                content.put("value", "<p>" + message + "</p>");
                contentArray.put(content);

                json.put("personalizations", personalizations);
                json.put("from", from);
                json.put("content", contentArray);

                // Send request
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d("EMAIL", "Response Code: " + responseCode);

            } catch (Exception e) {
                Log.e("EMAIL", "Error sending email", e);
            }
        }).start();
    }
}
