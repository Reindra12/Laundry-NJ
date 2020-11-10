package com.example.enjelaundry.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.enjelaundry.R;
import com.example.enjelaundry.koneksi.koneksi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class AdpPenyuci extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;


    public AdpPenyuci(Activity context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tukangcuci, viewGroup, false);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;

        HashMap<String, String> brg = new HashMap<String, String>();
        brg = data.get(position);
        myHolder.tnama.setText(brg.get("nama"));
        myHolder.talamat.setText(brg.get("alamat"));
        myHolder.tkontak.setText(brg.get("hp"));
        Picasso.with(context).load(koneksi.gambar+brg.get("foto"))
                .error(R.drawable.ic_cuci1)
                .placeholder(R.drawable.ic_cuci1)
                .into(myHolder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tnama, talamat, tkontak;
        ImageView img;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            tnama = itemView.findViewById(R.id.tnama);
            talamat = itemView.findViewById(R.id.talamat);
            tkontak = itemView.findViewById(R.id.tkontak);
            img = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getPosition();
//            mtransaksi brg = data.get(index);
        }
    }


}