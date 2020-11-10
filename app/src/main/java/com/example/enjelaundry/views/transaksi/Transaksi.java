package com.example.enjelaundry.views.transaksi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.example.enjelaundry.adapter.AdpTrans;
import com.example.enjelaundry.koneksi.koneksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Transaksi extends AppCompatActivity {
    public static RecyclerView recyclerView;
    public static  RecyclerView.Adapter transaksiAdapter;
    public static  ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    String status,user;
    public static SwipeRefreshLayout swipe;
    public static SearchView scari;
    String pid,pnama,palamat,php,pakses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mtransaksi);
        cekstatus();
        bacaPreferensi();
        recyclerView = findViewById(R.id.vtamp);
        swipe = findViewById(R.id.swipe);
        scari = findViewById(R.id.scari);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                transaksi("");
            }
        });

        if (scari.isSubmitButtonEnabled()){

        }else{
            transaksi("");
        }

        scari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                transaksi(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        scari.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                transaksi("");
                return false;
            }
        });
        LinearLayout bback = findViewById(R.id.bback);
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        transaksi("");
    }

    public void transaksi(String cari){
        swipe.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.transaksi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipe.setRefreshing(false);
                        Log.d("CEKRESPONSE", response);
                        if (response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("transaksi");
                                tampil.clear();
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
                                    tampil.add(map);
                                }
                                transaksiAdapter = new AdpTrans(Transaksi.this, tampil);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(transaksiAdapter);

                            } catch (JSONException e) {
                                swipe.setRefreshing(false);
                                Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipe.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
                        //berfungsi memberi tahu jika Tr kesalahan pada server atau koneksi aplikasi.
                        Toast.makeText(Transaksi.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void cekstatus(){
        SharedPreferences pref = getSharedPreferences("status", MODE_PRIVATE);
        status = pref.getString("status", "0");
        user = pref.getString("user", "0");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        pid = pref.getString("id", "0");
        pnama = pref.getString("nama", "0");
        palamat = pref.getString("alamat", "0");
        pakses = pref.getString("akses", "0");
        php = pref.getString("hp", "0");
    }
}