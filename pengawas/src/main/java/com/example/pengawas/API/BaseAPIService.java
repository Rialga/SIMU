package com.example.pengawas.API;

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
    @POST("api/login/admin")
    Call<ResponseBody> loginRequest(@Field("username") String username,
                                    @Field("password") String password);


    // List Kelas Mahasiswa
    @POST("api/kartu-ujian/kelasadmin")
    Call<ResponseBody> kelasRequset (@Header("Authorization") String Token);

    // List Nama Mahasiswa
    @POST("api/kartu-ujian/mhsruang/{kelas_id}/{ruangan_id}")
    Call<ResponseBody> mahasiswaRequest (@Header("Authorization") String token,
                                      @Path("kelas_id") String kelas_id,
                                      @Path("ruangan_id")String ruangan_id);
}
