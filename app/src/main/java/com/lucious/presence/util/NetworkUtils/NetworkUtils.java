package com.lucious.presence.util.NetworkUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.lucious.presence.BaseApplication;
import com.lucious.presence.R;
import com.lucious.presence.config.Config;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by ztx on 2017/2/2.
 */

public class NetworkUtils {
    private static AsyncHttpClient client;
    private static AsyncHttpClient clientUpload;

    public static synchronized AsyncHttpClient getAsyncHttpClientInstance() {
        if(client == null) {
            client = getHttpsAsyncHttpClient();
            client.addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
        }
        return client;
    }

    public static synchronized AsyncHttpClient getAsyncHttpClientUploadInstance() {
        if(clientUpload == null) {
            clientUpload = getHttpsAsyncHttpClient();
        }
        return clientUpload;
    }

    private static AsyncHttpClient getHttpsAsyncHttpClient() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(10000);
        client.setTimeout(10000);
        client.setResponseTimeout(10000);
        client.setMaxRetriesAndTimeout(5,10000);

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null,null);
            SSLSocketFactoryEx sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException | CertificateException | UnrecoverableKeyException | IOException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static String getHttpsJsonObject(Context context, String httpsUrl) {
        String total = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream cert = context.getResources().openRawResource(R.raw.server);
            InputStream caInput = new BufferedInputStream(cert);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null,null);
            keyStore.setCertificateEntry("ca",ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,tmf.getTrustManagers(),null);

            URL url = new URL(httpsUrl);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization","Bearer " + BaseApplication.token);

            HashMap<String,String> postDataParams = new HashMap<>();
            postDataParams.put("rs", Config.RS);
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();

            int responseCode=urlConnection.getResponseCode();
            if(responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while((line=br.readLine()) != null) {
                    total+=line;
                }
            } else {
                total="";
            }

        } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException e) {
            e.printStackTrace();
        }
        return total;
    }

    private static String getPostDataString(HashMap<String,String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String,String> entry : params.entrySet()) {
            if(first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        return result.toString();
    }

    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if(infos != null) {
                for(NetworkInfo ni : infos) {
                    if(ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
}
