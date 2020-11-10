package com.example.enjelaundry.views.layanan;

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
import com.example.enjelaundry.adapter.AdpLayanan;
import com.example.enjelaundry.koneksi.koneksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Layanan extends AppCompatActivity {
    RecyclerView rv_penyuci;
    RecyclerView.Adapter adapter;
    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    SwipeRefreshLayout swipe;
    SearchView scari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mjenis);
        rv_penyuci = (RecyclerView) findViewById(R.id.vtamp);
        swipe = findViewById(R.id.swipe);
        scari = findViewById(R.id.scari);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tampil("");
            }
        });

        if (scari.isSubmitButtonEnabled()) {

        } else {
            tampil("");
        }

        scari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tampil(query);
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
                tampil("");
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


    private void tampil(String cari) {
        swipe.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.layanan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipe.setRefreshing(false);
                        Log.d("CEKRESPONSE", response);
                        if (response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("layanan");
                                tampil.clear();
                                for (int i = 0; i < result.length(); i++) {//PR
                                    JSONObject c = result.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", c.getString("id"));
                                    map.put("nama", c.getString("nama"));
                                    map.put("harga", c.getString("harga"));
                                    map.put("satuan", c.getString("satuan"));
                                    tampil.add(map);
                                }

                                adapter = new AdpLayanan(Layanan.this, tampil);
                                rv_penyuci.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                rv_penyuci.setAdapter(adapter);
                            } catch (JSONException e) {
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
                        //berfungsi memberi tahu jika ada kesalahan pada server atau koneksi aplikasi.
                        Toast.makeText(Layanan.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cari", cari);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}