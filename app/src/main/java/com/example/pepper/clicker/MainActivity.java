package com.example.pepper.clicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    ToggleButton[] btnSelect = new ToggleButton[4];
    Button btnSubmit;
    TextView txtResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtResponse = (TextView) findViewById(R.id.txtResponseId);

        BtnListener listener = new BtnListener();

        btnSelect[0] = (ToggleButton) findViewById(R.id.btnSeclectA);
        btnSelect[1] = (ToggleButton) findViewById(R.id.btnSeclectB);
        btnSelect[2] = (ToggleButton) findViewById(R.id.btnSeclectC);
        btnSelect[3] = (ToggleButton) findViewById(R.id.btnSeclectD);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(listener);

    }

    private class BtnListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String urlString = "http://10.27.164.99:9999/clicker/select?questionnum=8";
            int count = 0;

            for (ToggleButton b:btnSelect) {
                if(b.isChecked()) {
                    urlString += "&choice=" + b.getTextOn().toString();
                    count++;
                }
            }

            if(count==0){
                android.widget.Toast.makeText(getApplicationContext(), "Please select at least one answer.", Toast.LENGTH_LONG).show();
            } else {
                for (ToggleButton b:btnSelect){
                    b.setEnabled(false);
                }
                btnSubmit.setEnabled(false);
                new HttpTask().execute(urlString);
                android.widget.Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();
            }




        }
    }

    private class HttpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strURLs) {
            URL url = null;
            HttpURLConnection conn = null;
            try {
                url = new URL(strURLs[0]);
                conn = (HttpURLConnection) url.openConnection();
                // Get the HTTP response code (e.g., 200 for "OK", 404 for "Not found")
                // and pass a string description in result to onPostExecute(String result)
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {  // 200
                    return "OK (" + responseCode + ")";
                } else {
                    return "Fail (" + responseCode + ")";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // Displays the result of the AsyncTask.
        // The String result is passed from doInBackground().
        @Override
        protected void onPostExecute(String result) {
            txtResponse.setText(result);
        }
    }

}
