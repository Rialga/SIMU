package com.example.mahasiswa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mahasiswa.API.BaseAPIService;
import com.example.mahasiswa.API.UtilsAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button btnLogin;
    ProgressDialog loading;
    Context Context;
    BaseAPIService ApiService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Context = this;
        ApiService = UtilsAPI.getAPIService(); // mengambil yanga da pada folder API
        initComponents();
    }

    private void initComponents() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(Context, null, "Harap Tunggu...", true, false);
                requestLogin();
            }

            private void requestLogin(){
                ApiService.loginRequest(username.getText().toString(), password.getText().toString())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    loading.dismiss();
                                    try {
                                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                        if (jsonRESULTS.getString("error").equals("false")){
                                            // Jika login berhasil maka data nama yang ada di response API
                                            // akan diparsing ke activity selanjutnya.
                                            Toast.makeText(Context, "BERHASIL LOGIN", Toast.LENGTH_SHORT).show();
                                            String nama = jsonRESULTS.getJSONObject("username").getString("nama");
                                            Intent intent = new Intent(Context, MainActivity.class);
                                            intent.putExtra("result_nama", nama);
                                            startActivity(intent);
                                        } else {
                                            // Jika login gagal
                                            String error_message = jsonRESULTS.getString("error_msg");
                                            Toast.makeText(Context, error_message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    loading.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Unable to reach server ", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        });
            }
        });

    }
}

