package com.example.enjelaundry.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.enjelaundry.R;
import com.example.enjelaundry.koneksi.koneksi;
import com.example.enjelaundry.views.transaksi.Transaksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AdpTrans extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;


    RecyclerView recyclerView;
    RecyclerView.Adapter transaksiAdapter;
    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    SwipeRefreshLayout swipe;
    Dialog dialog;
    View dialogView;
    String pid,pnama,palamat,php,pakses;
    LinearLayout bsetuju,btolak,bselesai;
    String status,user;
    public AdpTrans(Activity context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_trans, viewGroup, false);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;

        HashMap<String, String> brg = new HashMap<String, String>();
        brg = data.get(position);
        myHolder.tambil.setText(brg.get("ambil"));
        myHolder.ttgl.setText(brg.get("tgl"));
        myHolder.tbulan.setText(brg.get("bulan"));
        myHolder.ttahun.setText(brg.get("tahun"));
        myHolder.tnama.setText(brg.get("nama"));
        myHolder.tstatus.setText(brg.get("status"));
        myHolder.twil.setText(brg.get("wil"));
        myHolder.tpencuci.setText(brg.get("pencuci"));
        myHolder.tkontak.setText(brg.get("hp"));
        myHolder.ttotal.setText(brg.get("total"));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tnama, tstatus, twil, tpencuci, tkontak, ttotal,ttgl,ttahun,tbulan,tambil;
        LinearLayout lin;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            tnama = itemView.findViewById(R.id.tnama);
            tstatus = itemView.findViewById(R.id.tstatus);
            twil = itemView.findViewById(R.id.twil);
            tpencuci = itemView.findViewById(R.id.tpencuci);
            tkontak = itemView.findViewById(R.id.tkontak);
            ttotal = itemView.findViewById(R.id.ttotal);
            lin = itemView.findViewById(R.id.lin);
            ttgl = itemView.findViewById(R.id.ttgl);
            ttahun = itemView.findViewById(R.id.ttahun);
            tbulan = itemView.findViewById(R.id.tbln);
            tambil = itemView.findViewById(R.id.tambil);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getPosition();
            HashMap<String, String> brg = new HashMap<String, String>();
            brg = data.get(index);
            showDiag(R.style.DialogAnimation_2,brg.get("id"),brg.get("status"));
        }
    }

    public void showDiag(int animationSource, String id, String status) {
        dialogView = View.inflate(context, R.layout.mdetail, null);
        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        swipe = dialog.findViewById(R.id.swipe);
        recyclerView = dialog.findViewById(R.id.vtamp);
        bselesai = dialog.findViewById(R.id.bselesai);
        bsetuju = dialog.findViewById(R.id.bsetuju);
        btolak = dialog.findViewById(R.id.btolak);
        LinearLayout lin = dialog.findViewById(R.id.linsetuju);
        bacaPreferensi();
        cekstatus();
        if (pakses.equals("Admin")){
            lin.setVisibility(View.GONE);
        }else{
            if (status.equals("Menunggu Konfirmasi")){
                lin.setVisibility(View.VISIBLE);
            }else if (status.equals("Proses")){
                lin.setVisibility(View.VISIBLE);
                bsetuju.setVisibility(View.GONE);
                btolak.setVisibility(View.GONE);
            }else{
                lin.setVisibility(View.GONE);
            }
        }

        bsetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upstatus(id,"4");
            }
        });
        btolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upstatus(id,"5");
            }
        });
        bselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upstatus(id,"6");
            }
        });

        LinearLayout bback = dialog.findViewById(R.id.bback);
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        transaksi(id);
        dialog.getWindow().getAttributes().windowAnimations = animationSource;
        dialog.show();
    }

    public void transaksi(String id){
        swipe.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.detail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipe.setRefreshing(false);
                        Log.d("CEKRESPONSE", response);
                        if (response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("detail");
                                tampil.clear();
                                for (int i = 0; i < result.length(); i++) {//PR
                                    JSONObject c = result.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("nama", c.getString("nama"));
                                    map.put("total", c.getString("total"));
                                    map.put("jumlah", c.getString("jumlah"));
                                    tampil.add(map);
                                }
                                transaksiAdapter = new AdpDetail(context, tampil);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                recyclerView.setAdapter(transaksiAdapter);

                            } catch (JSONException e) {
                                swipe.setRefreshing(false);
                                Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipe.setRefreshing(false);
                        Toast.makeText(context, String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void upstatus(String id,String status){
        swipe.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.updatestatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipe.setRefreshing(false);
                        Log.d("CEKRESPONSE", response);
                        if (response.contains("1")) {
                            dialog.dismiss();
                            viewtransaksi("");
                            Toast.makeText(context, "Status Diperbarui", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipe.setRefreshing(false);
                        Toast.makeText(context, String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_transaksi", id);
                params.put("id_status", status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void viewtransaksi(String cari){
        Transaksi.swipe.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.transaksi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Transaksi.swipe.setRefreshing(false);
                        Log.d("CEKRESPONSE", response);
                        if (response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("transaksi");
                                Transaksi.tampil.clear();
                                for (int i = 0; i < result.length(); i++) {//PR
                                    JSONObject c = result.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", c.getString("id"));
                                    map.put("nama", c.getString("nama"));
                                    map.put("nota", c.getString("nota"));
                                    map.put("wil", c.getString("wil"));
                                    map.put("status", c.getString("status"));
                                    map.put("pencuci", c.getString("pencuci"));
                                    map.put("hp", c.getString("hp"));
                                    map.put("total", c.getString("total"));
                                    map.put("tahun", c.getString("tahun"));
                                    map.put("bulan", c.getString("bulan"));
                                    map.put("tgl", c.getString("tgl"));
                                    map.put("ambil", c.getString("ambil"));
                                    Transaksi.tampil.add(map);
                                }
                                Transaksi.transaksiAdapter = new AdpTrans(context, Transaksi.tampil);
                                Transaksi.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                Transaksi.recyclerView.setAdapter(Transaksi.transaksiAdapter);

                            } catch (JSONException e) {
                                Transaksi.swipe.setRefreshing(false);
                                Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Transaksi.swipe.setRefreshing(false);
                        Toast.makeText(context, String.valueOf(error), Toast.LENGTH_SHORT).show();
                        //berfungsi memberi tahu jika Tr kesalahan pada server atau koneksi aplikasi.
                        Toast.makeText(context, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
                params.put("cari", cari);
                params.put("akses", pakses);
                params.put("id", pid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private void bacaPreferensi() {
        SharedPreferences pref = context.getSharedPreferences("akun", context.MODE_PRIVATE);
        pid = pref.getString("id", "0");
        pnama = pref.getString("nama", "0");
        palamat = pref.getString("alamat", "0");
        pakses = pref.getString("akses", "0");
        php = pref.getString("hp", "0");
    }

    private void cekstatus(){
        SharedPreferences pref = context.getSharedPreferences("status", context.MODE_PRIVATE);
        status = pref.getString("status", "0");
        user = pref.getString("user", "0");
    }

}