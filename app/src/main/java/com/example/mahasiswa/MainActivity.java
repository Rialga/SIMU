package com.example.mahasiswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mahasiswa.API.BaseAPIService;
import com.example.mahasiswa.API.PrefManager;
import com.example.mahasiswa.API.UtilsAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    BaseAPIService baseApiService;
    com.example.mahasiswa.API.PrefManager PrefManager;
    RecyclerView rvlistkelas;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    JSONArray kelasdosen;
    TextView titleListKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefManager = new PrefManager(this);

        if (PrefManager.getToken().equals("")){
            logout();
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            baseApiService = UtilsAPI.getAPIService();
            titleListKelas = findViewById(R.id.judul);
            String token = PrefManager.getToken();

            getClass(token);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.logout){
            PrefManager.saveToken("");
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getClass(String token){
        baseApiService.kelasRequset(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        kelasdosen = jsonObject.getJSONArray("ujians");
                        rvlistkelas = findViewById(R.id.rvlistkelas);
                        rvlistkelas.setHasFixedSize(true);
                        rvlistkelas.setLayoutManager(layoutManager);

                        adapter = new KelasAdapter(kelasdosen, new KelasAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(JSONObject item) {
                                Intent intent = new Intent(MainActivity.this, BarcodeActivity.class);
                                intent.putExtra("data", item.toString());
                                startActivity(intent);
                            }
                        });
                        rvlistkelas.setAdapter(adapter);
                    } catch (JSONException | IOException e) {
                        logout();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(MainActivity.this, "Failed to get data :(", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error with connection :(", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout(){
        Toast.makeText(MainActivity.this, "Your session expired. You are now logged out.", Toast.LENGTH_SHORT).show();
        PrefManager.saveToken("");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivity(intent);
        finish();
    }

}