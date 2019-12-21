package com.example.mahasiswa.API;

public class UtilsAPI {

  //Link
    public static final String BASE_URL_API = "http://central.si.fti.unand.ac.id/ujian/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseAPIService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseAPIService.class);
    }
}
