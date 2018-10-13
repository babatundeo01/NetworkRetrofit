package com.example.consultants.networkretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.consultants.networkretrofit.api.RandomAPI;
import com.example.consultants.networkretrofit.models.RandomResponse;
import com.example.consultants.networkretrofit.models.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://randomuser.me/";
    private static final String TAG = "MainActivity_TAG";

    private Retrofit client;
    private RandomAPI randomAPI;

    private TextView textView, textViewPhone, textViewEmail, textViewDob;
    private ImageView imageView;
    private String name, phone, email, dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initViews();

        Log.d(TAG, "onCreate: Thread "
                + Thread.currentThread().getName());

        client = prepareRetrofitClient();
        randomAPI = client.create(RandomAPI.class);

        randomAPI.getRandomUser().enqueue(new Callback<RandomResponse>() {
            @Override
            public void onResponse(Call<RandomResponse> call, Response<RandomResponse> response) {
                if(response.isSuccessful()) {
                    RandomResponse randomUser = response.body();
                    if(randomUser!=null) {
                        List<Result> res = randomUser.getResults();
                        displayRandom(res);
                        Log.d(TAG, "onResponse: seed " +
                                res.size());
                        Log.d(TAG, "onResponse: Thread " +
                                Thread.currentThread().getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<RandomResponse> call, Throwable t) {

                Log.e(TAG, "onResponse: Error " + t);
            }
        });
    }

    private void initViews() {
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewDob = findViewById(R.id.textViewDob);
    }

    private void displayRandom(List<Result> res) {
        name = res.get(0).getName().getFirst().toUpperCase() + " " + res.get(0).getName().getLast().toUpperCase();
        phone = res.get(0).getPhone();
        email = res.get(0).getEmail();
        dob = String.valueOf(res.get(0).getDob().getAge());
        textView.append(name);
        textViewPhone.append(phone);
        textViewEmail.append(email);
        textViewDob.append(dob);
        Picasso.get().load(res.get(0).getPicture().getLarge()).into(imageView);
    }

    private Retrofit prepareRetrofitClient() {
        Retrofit client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return client;
    }
}
