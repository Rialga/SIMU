package com.example.dosen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.AdapterHolder>{

    private JSONArray dataarrays;
    private OnItemClickListener kliks;

    public MahasiswaAdapter(JSONArray data, OnItemClickListener listener){
        dataarrays = data;
        kliks = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(JSONObject item);
    }

    public class AdapterHolder extends RecyclerView.ViewHolder{

        private TextView nama;
        private TextView nim;

        public AdapterHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            nim = itemView.findViewById(R.id.nim);
        }
        public void bind(final JSONObject item,final MahasiswaAdapter.OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
    @NonNull
    @Override
    public MahasiswaAdapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.mahasiswa_item,parent,false);
        return new AdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaAdapter.AdapterHolder holder, int position) {
        try {
            holder.nama.setText(dataarrays.getJSONObject(position).getString("nama"));
            holder.nim.setText(dataarrays.getJSONObject(position).getString("nim"));
            holder.bind(dataarrays.getJSONObject(position),kliks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataarrays.length();
    }

}
