package com.bithack.apparatus;

import android.os.AsyncTask;
import com.bithack.apparatus.PublishDialog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/* loaded from: classes.dex */
public class Community {

    protected class FetchTask extends AsyncTask<String, String, String> {
        protected FetchTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(String... params) throws IllegalStateException {
            String result = "";
            try {
                HttpClient client = PublishDialog.HttpUtils.getNewHttpClient();
                HttpGet req = new HttpGet("http://apparatus-web.voxelmanip.se/internal/fetch.php?t=" + params[0] + "&m=" + params[1] + "&s=" + params[2]);

                HttpResponse res = client.execute(req);
                InputStream in = res.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder str = new StringBuilder();
                while (true) {
                    String data = reader.readLine();
                    if (data != null) {
                        str.append(data);
                    } else {
                        in.close();
                        result = str.toString();
                        return result;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String result) {
            result.trim();
        }
    }
}
