package com.example.enjelaundry.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.enjelaundry.R;

import java.util.ArrayList;
import java.util.HashMap;


public class AdpSantri extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;


    public AdpSantri(Activity context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_santri, viewGroup, false);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;

        HashMap<String, String> brg = new HashMap<String, String>();
        brg = data.get(position);
        myHolder.tnama.setText(brg.get("nama"));
        myHolder.twil.setText(brg.get("wil"));
        myHolder.tnis.setText(brg.get("nis"));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tnama, tnis, twil;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            tnama = itemView.findViewById(R.id.tnama);
            tnis = itemView.findViewById(R.id.tnis);
            twil = itemView.findViewById(R.id.twil);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getPosition();
//            mtransaksi brg = data.get(index);
        }
    }


}