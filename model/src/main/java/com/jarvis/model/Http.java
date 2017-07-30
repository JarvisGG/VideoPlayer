package com.jarvis.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Http {
    public static final String CHARSET = "UTF-8";

    public static String get(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        try {
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                return response.toString();
            } else {
                throw new IOException("Network Error - response code: " + con.getResponseCode());
            }
        } finally {
            con.disconnect();
        }
    }

    public static String get(String baseUrl, String key, String value) throws IOException {
        return get(baseUrl + "?" + concatKeyValue(key, value));
    }

    public static String get(String baseUrl, int suffix) throws IOException {
        Log.e("Http1 -----> ", baseUrl + suffix);

        return get(baseUrl + suffix);
    }

    public static String get(String baseUrl, String suffix) throws IOException {
        Log.e("Http2 -----> ", baseUrl + encodeString(suffix));
        return get(baseUrl + encodeString(suffix));
    }

    private static String concatKeyValue(String key, String value) {
        return encodeString(key) + "=" + encodeString(value).replace("+", "%20");
    }

    private static String encodeString(String str) {
        try {
            return URLEncoder.encode(str, CHARSET);
        } catch (UnsupportedEncodingException ignored) {
            return "";
        }
    }
}
