package com.picsarttraining.searchimages.http;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Arsen on 04.04.2016.
 */
public class RequestUtils {
    public static void requestImages(String query, int page, final ImagesResponseHandler imagesResponseHandler) {
        new AsyncTask<String, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(String... params) {

                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Connection", "close");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    String output;
                    while ((output = reader.readLine()) != null) {
                        if(output.contains("\"link\": \"")){
                            String link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
                            imageUrls.add(link);
                        }
                    }
                    conn.disconnect();
                    return imageUrls;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> imageUrls) {
                imagesResponseHandler.onResponse(imageUrls);
            }
        }.execute(GoogleImagesRequest.getUrlFor(query, page));
    }

    public interface ImagesResponseHandler {
        void onResponse(ArrayList<String> imageUrls);
    }
}
