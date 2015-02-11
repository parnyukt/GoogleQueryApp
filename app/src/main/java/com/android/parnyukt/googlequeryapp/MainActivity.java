package com.android.parnyukt.googlequeryapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.parnyukt.googlequeryapp.utils.Streams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private static String TAG = MainActivity.class.getSimpleName();

    private static String SERVER_URL = "http://ajax.googleapis.com/ajax/services/search/news";
    private static Integer PAGE_SIZE = 6;
//    http://ajax.googleapis.com/ajax/services/search/news?v=1.0&q="харьков"&rsz=6&start=1

    private EditText editTextInput;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInput = (EditText) findViewById(R.id.editTextInput);

    }

    public void onSearchClick(View v)
    {
        String query = "android";
        query = editTextInput.getText().toString();

        new GoogleSearchTask().execute(query);

//        try {
//            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//            String term = editTextInput.getText().toString();
//            intent.putExtra(SearchManager.QUERY, term);
//            startActivity(intent);
//        } catch (Exception e) {
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GoogleSearchTask extends AsyncTask<String, Integer, JSONObject> {

        String searchResult = "";
        String searchUrl = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
        String searchQuery;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... query) {
//            return makeAndroidRequest(query[0]);
            return makeHttpRequest(query[0]);
        }


        @Override
        protected void onPostExecute(final JSONObject data) {
            Log.i("Response", data.toString());
        }

        @Override
        protected void onCancelled() {
        }

        private JSONObject makeAndroidRequest(String query){
            final String url = getUrl(query, 1);

            final HttpClient client = new DefaultHttpClient();

            final HttpGet getRequest = new HttpGet(url);

            try {
                HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("Request", "Error " + statusCode);
                    return null;
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();
                        String responseData = Streams.copyAndClose(inputStream, new ByteArrayOutputStream()).toString();
                        return new JSONObject(responseData);
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                // Could provide a more explicit error message for IOException or IllegalStateException
                getRequest.abort();
                Log.w("Request", "Error " + url);
            }

            return null;

        }

        private JSONObject makeHttpRequest(String query) {
            final String urlStr = getUrl(query, 1);
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urlStr);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                String responseData = Streams.read(in);
                return new JSONObject(responseData);

            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());

            } finally {
                urlConnection.disconnect();

            }
            return null;
        }

        private String getUrl(String query, Integer page){
            JSONObject params = new JSONObject();
//            params.put("v", "1.0");
//            params.put("rsz", PAGE_SIZE);
//            params.put("start", 1);

            return SERVER_URL + "?" + "v=1.0&q=харьков&rsz=6&start=1";
        }
    }
}
