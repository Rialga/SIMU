package com.example.dosen.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BaseAPIService {

    // Login
    @FormUrlEncoded
    @POST("api/login/dosen")
    Call<ResponseBody> loginRequest(@Field("username") String username,
                                    @Field("password") String password);

    // List Kelas Mahasiswa
    @POST("api/kartu-ujian/kelasdosen")
    Call<ResponseBody> kelasRequset (@Header("Authorization") String Token);

    // List Nama Mahasiswa
    @POST("api/kartu-ujian/mhskelas/{kelas_id}")
    Call<ResponseBody> mahasiswaRequest (@Header("Authorization") String token,
                                         @Path("kelas_id") String kelas_id);
}


