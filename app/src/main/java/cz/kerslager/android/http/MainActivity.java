package cz.kerslager.android.http;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

// https://www.tutorialspoint.com/android/android_network_connection.htm

public class MainActivity extends AppCompatActivity {

    private TextView vystup;
    private final String GETURL = "https://pluto.pslib.cz/test.txt";
    private String prijato="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vystup = (TextView) findViewById(R.id.textViewVystup);
    }

    public void onClick(View v) {
        // TODO: zjistit, zda je síť dostupná
        //vystup.setText(null);
        vystup.append("kliknuto"+"\n");
        download(GETURL);
        vystup.append(prijato);
        prijato = "";
    }

    private void download(String geturl) {
        final String url = geturl;

        Thread t = new Thread() {
            public void run() {
                InputStream in = null;
                try {
                    in = openHttpConnection(url);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String data;
                    while ((data = reader.readLine()) != null) {
                        prijato += data + "\n";
                    }
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;
        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();
            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an HTTP URL");
            }
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
}
