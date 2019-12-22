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

public class ListKelasAdapter extends RecyclerView.Adapter<ListKelasAdapter.AdapterViewholder> {

    private JSONArray kelas;
    public OnItemClickListener onItemClickListener;

    public ListKelasAdapter(JSONArray data, OnItemClickListener listener) {
        kelas = data;
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public AdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kelas_item, parent, false);
        return new AdapterViewholder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewholder holder, int position) {
        try {
            holder.nama_mk.setText(kelas.getJSONObject(position).getString("nama_mk"));
            holder.ruangan.setText(kelas.getJSONObject(position).getString("ruang"));
            holder.mulai.setText(kelas.getJSONObject(position).getString("mulai"));
            holder.selesai.setText(kelas.getJSONObject(position).getString("selesai"));
            holder.tanggal.setText(kelas.getJSONObject(position).getString("tanggal"));
            holder.bind(kelas.getJSONObject(position), onItemClickListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return kelas.length();
    }

    public class AdapterViewholder extends RecyclerView.ViewHolder {
        private TextView nama_mk, ruangan, mulai, selesai, tanggal;

        public AdapterViewholder(@NonNull View itemView) {
            super(itemView);
            nama_mk = itemView.findViewById(R.id.nama_mk);
            ruangan = itemView.findViewById(R.id.ruangan);
            mulai = itemView.findViewById(R.id.mulai);
            selesai = itemView.findViewById(R.id.selesai);
            tanggal = itemView.findViewById(R.id.tanggal);
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
