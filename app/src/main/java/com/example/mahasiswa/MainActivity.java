package com.example.mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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

    PrefManager PrefManager;
    String accessToken;
    BaseAPIService mApiService;
    RecyclerView rvlistkelas;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    JSONArray ujians;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefManager = new PrefManager(this);
        if (PrefManager.getToken().equals("")){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            accessToken = PrefManager.getToken();
            mApiService = UtilsAPI.getAPIService();
            requestListUjian();
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
        if(item.getItemId() == R.id.logout){
            PrefManager.saveToken("");
            Toast.makeText(MainActivity.this, "Berhasil logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public  void requestListUjian(){
        mApiService.kelasRequset(accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        ujians = jsonRESULTS.getJSONArray("ujians");
                        rvlistkelas = findViewById(R.id.rvlistkelas);
                        rvlistkelas.setHasFixedSize(true);
                        rvlistkelas.setLayoutManager(layoutManager);
                        mAdapter = new ListKelasAdapter(ujians, new ListKelasAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(JSONObject item){
                                Intent intent = new Intent(MainActivity.this, BarcodeActivity.class);
                                intent.putExtra("data", item.toString());
                                startActivity(intent);
                            }
                        });
//                        Toast.makeText(ListUjianActivity.this, ujians.toString(), Toast.LENGTH_LONG).show();
                        rvlistkelas.setAdapter(mAdapter);
                    } catch (JSONException | IOException e){
                        Toast.makeText(MainActivity.this, "Your Session Expired", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        PrefManager.saveToken("");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Server unreachable :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
