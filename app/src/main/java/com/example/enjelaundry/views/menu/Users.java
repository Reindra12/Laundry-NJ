package com.example.enjelaundry.views.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.enjelaundry.views.akun.Login;
import com.example.enjelaundry.R;
import com.example.enjelaundry.koneksi.koneksi;
import com.example.enjelaundry.views.layanan.Layanan;
import com.example.enjelaundry.views.penyuci.Penyuci;
import com.example.enjelaundry.views.santri.Santri;
import com.example.enjelaundry.views.transaksi.Transaksi;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users extends AppCompatActivity implements View.OnClickListener {
    BarChart chart;
    List<String> list = new ArrayList<>();
    String pid, pnama, palamat, php, pakses;
    TextView tnama, talamat, thp, ttotal, tsudah, tbelum,tmenunggu,ttolak;
    LinearLayout btransaksi, briwayat, bkeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muser);
        chart = findViewById(R.id.barchart);
        chart.setDrawValueAboveBar(true);
        chart.setPinchZoom(false);
        bacaPreferensi();
        tnama = findViewById(R.id.tnama);
        talamat = findViewById(R.id.talamat);
        thp = findViewById(R.id.thp);
        ttotal = findViewById(R.id.ttotal);
        tsudah = findViewById(R.id.tsudah);
        tbelum = findViewById(R.id.tbelum);
        tmenunggu = findViewById(R.id.tmenunggu);
        ttolak = findViewById(R.id.ttolak);
//
        btransaksi = findViewById(R.id.btransaksi);
        briwayat = findViewById(R.id.briwayat);
        bkeluar = findViewById(R.id.bkeluar);
        tnama.setText(pnama);
        talamat.setText(palamat);
        thp.setText(php);
//
        btransaksi.setOnClickListener(this);
        briwayat.setOnClickListener(this);
        bkeluar.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tampil();
        grafik();
    }

    private void bacaPreferensi() {
        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
        pid = pref.getString("id", "0");
        pnama = pref.getString("nama", "0");
        palamat = pref.getString("alamat", "0");
        pakses = pref.getString("akses", "0");
        php = pref.getString("hp", "0");
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        SharedPreferences pref = getSharedPreferences("status", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        switch (v.getId()) {
            case R.id.btransaksi:
                intent = new Intent(Users.this, Transaksi.class);
                editor.putString("status", "0");
                editor.putString("user", "1");
                editor.commit();
                startActivity(intent);
                break;
            case R.id.briwayat:
                intent = new Intent(Users.this, Transaksi.class);
                editor.putString("status", "1");
                editor.putString("user", "1");
                editor.commit();
                startActivity(intent);
                break;
            case R.id.bkeluar:
                startActivity(new Intent(getApplicationContext(), Login.class));
                SharedPreferences pref1 = getSharedPreferences("akun", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.putString("id", "0");
                editor1.putString("nama", "0");
                editor1.putString("alamat", "0");
                editor1.putString("hp", "0");
                editor1.putString("akses", "0");
                editor1.commit();
                finish();
                break;
        }
    }


    private void tampil() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.home,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CEKRESPONSE", response);
                        if (response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("home");
                                JSONObject c = result.getJSONObject(0);
                                ttotal.setText(c.getString("total"));
                                tbelum.setText(c.getString("proses"));
                                tsudah.setText(c.getString("sudah"));
                                tmenunggu.setText(c.getString("konfirm"));
                                ttolak.setText(c.getString("tolak"));
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
                        Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", pid);
                params.put("akses", pakses);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void grafik() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.grafik,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CEKRESPONSE", response);
                        if (response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("grafik");
                                ArrayList NoOfEmp = new ArrayList();
                                for (int i = 0; i < result.length(); i++) {//PR
                                    JSONObject c = result.getJSONObject(i);
                                    int nilai = Integer.parseInt(c.getString("total"));
                                    NoOfEmp.add(new BarEntry(nilai, i));
                                }
                                ArrayList year = new ArrayList();

                                year.add("Januari");
                                year.add("Februari");
                                year.add("Maret");
                                year.add("April");
                                year.add("Mei");
                                year.add("Juni");
                                year.add("Juli");
                                year.add("Agustus");
                                year.add("September");
                                year.add("Oktober");
                                year.add("November");
                                year.add("Desember");
                                BarDataSet bardataset = new BarDataSet(NoOfEmp, "Bulan");
                                chart.animateY(5000);
                                BarData data = new BarData(year, bardataset);
                                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                                chart.setData(data);
                                chart.getLegend().setEnabled(false);
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
                        Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", pid);
                params.put("akses", pakses);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}