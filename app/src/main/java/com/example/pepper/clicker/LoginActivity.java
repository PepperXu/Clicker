package com.example.pepper.clicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText accountText;
    EditText passwordText;
    Button submitButton;
    TextView txtResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountText = (EditText) findViewById(R.id.accountName);
        passwordText = (EditText) findViewById(R.id.password);
        submitButton = (Button) findViewById(R.id.submit);
        txtResponse = (TextView) findViewById(R.id.txtResponseId);
    }

    public void checkPassword(View view){
        String accountName = accountText.getText().toString();
        String password = passwordText.getText().toString();
        boolean hasUserName = accountName != null && ((accountName = accountName.trim()).length() > 0);
        boolean hasPassword = password != null && ((password = password.trim()).length() > 0);
        if(!hasUserName) {
            android.widget.Toast.makeText(getApplicationContext(), "Please enter account name. ", Toast.LENGTH_LONG).show();
            return;
        }
        if(!hasPassword){
            android.widget.Toast.makeText(getApplicationContext(), "Please enter password. ", Toast.LENGTH_LONG).show();
            return;
        }

        submitButton.setEnabled(false);  // Disable the button
        new HttpTask().execute("http://10.27.164.99:9999/clicker/login?accountname="+accountName+"&password="+password); // Send HTTP request
        Toast.makeText(this, "Sent", Toast.LENGTH_LONG).show(); // Toast a message

    }

    private class HttpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strURLs) {
            URL url = null;
            HttpURLConnection conn = null;
            //ByteArrayOutputStream os = new ByteArrayOutputStream();
            //byte[] buf = new byte[4096];
            try {
                url = new URL(strURLs[0]);
                conn = (HttpURLConnection) url.openConnection();
                // Get the HTTP response code (e.g., 200 for "OK", 404 for "Not found")
                // and pass a string description in result to onPostExecute(String result)
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {  // 200
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return "OK (" + responseCode + ")";
                } else if(responseCode == HttpURLConnection.HTTP_PRECON_FAILED){
                    //InputStream es = conn.getErrorStream();
                    //int ret = 0;
                    //while((ret = es.read(buf)) > 0){
                    //    os.write(buf, 0, ret);
                    //}
//
                    //es.close();
                    submitButton.setEnabled(true);
                    return "Wrong name or password.";
                }
                else {
                    submitButton.setEnabled(true);
                    return "Fail (" + responseCode + ")";

                }
            } catch (IOException e) {
                submitButton.setEnabled(true);
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // Displays the result of the AsyncTask.
        // The String result is passed from doInBackground().
        @Override
        protected void onPostExecute(String result) {
            txtResponse.setText(result);  // put it on TextView
        }
    }
}
