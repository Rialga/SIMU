package com.example.mahasiswa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.AdapterViewholder>{

    private JSONArray datakelas;
    public OnItemClickListener aksi;

    public KelasAdapter(JSONArray data, OnItemClickListener listener) {
        datakelas = data;
        aksi = listener;
    }

    @NonNull
    @Override
    public AdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_kelas, parent, false);
        return new AdapterViewholder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewholder holder, int position) {
        try {
            holder.nama_mk.setText(datakelas.getJSONObject(position).getString("nama_mk"));
            holder.ruang_ujian.setText(datakelas.getJSONObject(position).getString("ruangan"));
            holder.mulai.setText(datakelas.getJSONObject(position).getString("mulai"));
            holder.selesai.setText(datakelas.getJSONObject(position).getString("selesai"));
            holder.tanggal_ujian.setText(datakelas.getJSONObject(position).getString("tanggal"));
            holder.bind(datakelas.getJSONObject(position), aksi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return datakelas.length();
    }

    public class AdapterViewholder extends RecyclerView.ViewHolder {
        private TextView nama_mk, ruang_ujian, mulai, selesai, tanggal_ujian;

        public AdapterViewholder(@NonNull View itemView) {
            super(itemView);
            nama_mk = itemView.findViewById(R.id.nama_mk);
            ruang_ujian = itemView.findViewById(R.id.ruangan);
            mulai = itemView.findViewById(R.id.mulai);
            selesai = itemView.findViewById(R.id.selesai);
            tanggal_ujian = itemView.findViewById(R.id.tanggal);
        }

        public void bind(final JSONObject item, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        listener.onItemClick(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(JSONObject item) throws JSONException;
    }
}
