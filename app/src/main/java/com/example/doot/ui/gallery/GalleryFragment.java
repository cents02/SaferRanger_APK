package com.example.doot.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.doot.R;

import org.json.JSONObject;
import org.json.JSONTokener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        sendpost();
        return root;

    }
    private void sendpost() {
        Thread thread = new Thread(new Runnable() {

            public void run() {

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://192.168.137.13:5005/ballsget")
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    Log.i("HAHA", response.toString());
                    Log.i("HAHA",response.message());
                    Log.i("HAHA",response.body().string());

                } catch (Exception ex) {
                    Log.e("DEATH", ex.toString());
                }
            }
        });
        thread.start();
    }

}