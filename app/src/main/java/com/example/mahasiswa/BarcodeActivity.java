package com.example.mahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class BarcodeActivity extends AppCompatActivity {

    JSONObject data;
    Bitmap bitmap;
    ImageView qrImage;
    TextView mataKuliah,namauser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        qrImage = findViewById(R.id.qrcode);
        mataKuliah = findViewById(R.id.matakuliahujian);
        namauser = findViewById(R.id.nama);
        Intent intent = getIntent();
        try {
            data = new JSONObject(Objects.requireNonNull(intent.getStringExtra("data")));
            String passcode = data.getString("passcode");
            String matkul = data.getString("nama_mk");
            String nama = data.getString("nama");
            QRGEncoder qrgEncoder = new QRGEncoder(passcode, null, QRGContents.Type.TEXT, 1200);
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
            mataKuliah.setText(matkul);
            namauser.setText(nama);
        } catch (JSONException | WriterException e) {
            e.printStackTrace();
        }

    }
}
