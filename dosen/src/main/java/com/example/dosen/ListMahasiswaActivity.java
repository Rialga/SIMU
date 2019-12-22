package com.example.dosen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dosen.API.BaseAPIService;
import com.example.dosen.API.PrefManager;
import com.example.dosen.API.UtilsAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListMahasiswaActivity extends AppCompatActivity {

    TextView matkul,namakelas;
    RecyclerView rvMahasiswa;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    PrefManager PrefManager;
    BaseAPIService baseAPIService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefManager = new PrefManager (this);
        if (PrefManager.getToken().equals("")){
            logout();
        }
        else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_mahasiswa);

            try {
                Intent intent = getIntent();
                JSONObject detailAwas = new JSONObject(Objects.requireNonNull(intent.getStringExtra("data")));
                fill(detailAwas);

                ListMahasiswa(PrefManager.getToken(),detailAwas.getInt("kelas_id"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
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

    private void fill(JSONObject detailAwas) {
        matkul = findViewById(R.id.mmatkul);
        namakelas = findViewById(R.id.nm_kelas);
        baseAPIService = UtilsAPI.getAPIService();

        try {
            matkul.setText(detailAwas.getString("nama_mk"));
            namakelas.setText(detailAwas.getString("nama_kelas"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ListMahasiswa(String token, int kelas_id) {
        baseAPIService.mahasiswaRequest(token,String.valueOf(kelas_id)).enqueue(new Callback<ResponseBody>(){

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        rvMahasiswa = findViewById(R.id.rvlistmahasiswa);
                        rvMahasiswa.setHasFixedSize(true);
                        rvMahasiswa.setLayoutManager(layoutManager);

                        adapter = new MahasiswaAdapter(jsonObject.getJSONArray("mahasiswas"),
                                new MahasiswaAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(JSONObject item) {
                                        Toast.makeText(ListMahasiswaActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        rvMahasiswa.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ListMahasiswaActivity.this, "Gagal Ambil Data :(", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ListMahasiswaActivity.this, "Error Koneksi :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Scan(View v){
        Intent intent = new Intent(ListMahasiswaActivity.this, ScanActivity.class);
        startActivity(intent);

    }

    public void logout(){
        Toast.makeText(ListMahasiswaActivity.this, "Babay.", Toast.LENGTH_SHORT).show();
        PrefManager.saveToken("");
        Intent intent = new Intent(ListMahasiswaActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}
