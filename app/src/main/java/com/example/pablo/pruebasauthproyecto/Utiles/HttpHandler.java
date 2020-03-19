package com.example.pablo.pruebasauthproyecto.Utiles;

/**
 * Created by Pablo on 21/03/2018.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {

    public HttpHandler() {
    }

    public String conexionHttp (String reqUrl) {
        String respuesta = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            respuesta = convertirStreamString(in);
        } catch (MalformedURLException e) {
            Log.d("DEPURACION", "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.d("DEPURACION", "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.d("DEPURACION", "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.d("DEPURACION", "Exception: " + e.getMessage());
        }
        return respuesta;
    }

    private String convertirStreamString (InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
