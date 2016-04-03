package com.picsarttraining.searchimages;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.picsarttraining.searchimages.http.RequestUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private View searchButton;
    private ListView listView;
    private ImagesAdapter imagesAdapter;
    private DisplayMetrics metrics;
    private View progressBar;
    private boolean isRequesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        editText = (EditText) findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.list_view);
        progressBar = findViewById(R.id.image_loading);
        imagesAdapter = new ImagesAdapter(this);
        listView.setAdapter(imagesAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImages(editText.getText().toString(), 0, true);
                searchButton.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                imagesAdapter.resetWatchCount();
            }
        });

        imagesAdapter.setOnlastElementReachedListener(new ImagesAdapter.LastElementReachedListener() {
            @Override
            public void reach(int whichTime) {
                if (!isRequesting)
                    requestImages(editText.getText().toString(), whichTime, false);
            }
        });
    }

    private void requestImages(final String query, int page, final boolean isResetResults) {
        isRequesting = true;
        RequestUtils.requestImages(query, page, new RequestUtils.ImagesResponseHandler() {
            @Override
            public void onResponse(ArrayList<String> imageStringUrls) {
                if (imageStringUrls == null) {
                    Toast.makeText(MainActivity.this, "Something goes Wrong", Toast.LENGTH_LONG).show();
                    searchButton.setClickable(true);
                    progressBar.setVisibility(View.GONE);
                    isRequesting = false;
                    return;
                }

                ArrayList<URL> imageUrls = new ArrayList<>();
                for (String link : imageStringUrls) {
                    try {
                        imageUrls.add(new URL(link));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

                BitmapUtils.loadImagesFromUrls(MainActivity.this, imageUrls, metrics.widthPixels, new BitmapUtils.BitmapsLoadCallback() {
                    @Override
                    public void onBitmapLoad(ArrayList<Bitmap> bitmaps) {
                        if (isResetResults)
                            imagesAdapter.setImages(bitmaps);
                        else
                            imagesAdapter.addImages(bitmaps);
                        searchButton.setClickable(true);
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        isRequesting = false;
                    }
                });
            }
        });
    }
}
