package com.trab.myapplication.Net;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WsConnector
{
    public static final String BASE_SERVER = "192.168.1.92:55555/redegen";

    public static HttpURLConnection connectTo (@NonNull String route) throws IOException
    {
        HttpURLConnection con = (HttpURLConnection) new URL(BASE_SERVER + route).openConnection();
        con.setConnectTimeout(10000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("Accept", "text/plain");
        con.addRequestProperty("Content-Type", "application/json");
        return con;
    }

    public static HttpURLConnection get(@NonNull String route) throws IOException
    {
        HttpURLConnection con = connectTo(route);
        con.setRequestMethod("GET");
        con.connect();
        Log.d("net", con.getRequestMethod());
        return con;
    }

    public static String getResponseData (@NonNull HttpURLConnection con) throws IOException
    {
        byte[] buffer = new byte[1024];

        Log.d("net", "" + con.getResponseCode());
        Log.d("net", "" + con.getRequestMethod());
        InputStream in = con.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;

        while ((read = in.read(buffer)) != -1)
            out.write(buffer, 0, read);
        con.disconnect();
        return new String(out.toByteArray(), "UTF-8");
    }

    public static HttpURLConnection post (String route, JSONObject data) throws IOException
    {
        HttpURLConnection con = connectTo(route);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.connect();

        DataOutputStream writer = new DataOutputStream(con.getOutputStream());
        writer.writeBytes(data.toString());
        writer.flush();
        writer.close();

        return con;
    }


    public static HttpURLConnection put (String route, JSONArray data) throws IOException
    {
        HttpURLConnection con = connectTo(route);
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        con.connect();

        DataOutputStream writer = new DataOutputStream(con.getOutputStream());
        writer.writeBytes(data.toString());
        writer.flush();
        writer.close();

        return con;
    }

    public static boolean checkInternetConection()
    {
        try {
            ((HttpURLConnection) new URL(BASE_SERVER).openConnection()).disconnect();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
