package com.example.pengawas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pengawas.API.BaseAPIService;
import com.example.pengawas.API.PrefManager;
import com.example.pengawas.API.UtilsAPI;

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
    android.content.Context Context;
    BaseAPIService ApiService;
    com.example.pengawas.API.PrefManager PrefManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefManager = new PrefManager(this);
        super.onCreate(savedInstanceState);


        if (PrefManager.getToken().equals("")){
            setContentView(R.layout.activity_login);
            Context = this;
            ApiService = UtilsAPI.getAPIService(); // meng-init yang ada di package apihelper
            initComponents();
        }else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void initComponents() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(Context, null, "Harap Tunggu...", true, false);
                requestLogin();
            }

            public void requestLogin(){
                ApiService.loginRequest(username.getText().toString(), password.getText().toString())
                        .enqueue(new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(Call<ResponseBody> callback, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    try {
                                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                        String token = "Bearer "+jsonRESULTS.getString("access_token");
                                        PrefManager.saveToken(token);
                                        loading.dismiss();
                                        Intent intent = new Intent(Context, MainActivity.class);
                                        startActivity(intent);
//
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                else {

                                    Toast.makeText(Context, "Username atau passwor salah" + username, Toast.LENGTH_SHORT).show();
                                    Log.d("onResponse", "onResponse: " + response.message());
                                    loading.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("debug", "onFailure: ERROR > " + t.toString());
                                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        });
            }
        });

    }
}
