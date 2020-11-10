package com.example.enjelaundry.views.akun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.enjelaundry.R;
import com.example.enjelaundry.koneksi.koneksi;
import com.example.enjelaundry.views.menu.Admin;
import com.example.enjelaundry.views.menu.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText _txtUser, _txtPass;
    Button _btnLogin;
    Spinner _Spinner;
    String status, text;
    String item, id_penyuci;
    String username;
    String pid,pnama,palamat,php,pakses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mlogin);
        _txtPass = (EditText) findViewById(R.id.txtPassword);
        _txtUser = (EditText) findViewById(R.id.txtUser);
        _btnLogin = (Button) findViewById(R.id.btnLogin);
        _Spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usertype, R.layout.item_spinner);
        _Spinner.setAdapter(adapter);

        _Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 item = String.valueOf(parent.getItemAtPosition(position));
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        bacaPreferensi();
        if (!(pid.equals("0"))) {
            if(pakses.equals("Admin")){
                startActivity(new Intent(getApplicationContext(), Admin.class));
                finish();
            }else{
                startActivity(new Intent(getApplicationContext(), Users.class));
                finish();
            }

        }


        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 username = _txtUser.getText().toString();
                String katasandi = _txtPass.getText().toString();
//                Toast.makeText(LoginActivity.this, "" + text, Toast.LENGTH_SHORT).show();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Login.this, "Masukkan Nama Pengguna", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(katasandi)) {
                    Toast.makeText(Login.this, "Masukkan Kata Sandi", Toast.LENGTH_SHORT).show();
                    return;
                } else if (katasandi.length() < 4) {
                    Toast.makeText(Login.this, "Kata Sandi Harus 8 digit", Toast.LENGTH_SHORT).show();
                    return;
                }

                login(username, katasandi);

//                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void login(final String email, final String katasandi) {
        ProgressDialog dialog = new ProgressDialog(Login.this);
        dialog.setMessage("please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                koneksi.mitra, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.contains("1")) {

                    try {
                        JSONObject jsonObject;
                        jsonObject = new JSONObject(response);
                        JSONArray result = jsonObject.getJSONArray("users");
                        JSONObject c = result.getJSONObject(0);
                        if(item.equals("admin")){
                            startActivity(new Intent(getApplicationContext(), Admin.class));
                        }else{
                            startActivity(new Intent(getApplicationContext(), Users.class));
                        }
                        SharedPreferences pref = getSharedPreferences("akun", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("id", c.getString("id"));
                        editor.putString("nama", c.getString("nama"));
                        editor.putString("alamat", c.getString("alamat"));
//                        editor.putString("hp", c.getString("hp"));
                        editor.putString("akses", c.getString("akses"));
                        editor.commit();
                        Log.d("COBA", response);
                        finish();
                    } catch (JSONException e) {
                        Log.d("COBA", response);
                        Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Gagal Masuk ! ", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(Login.this, "Silahkan Cek Kembali Koneksi Internet Anda", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("nama_penyuci", email);
                map.put("password", katasandi);
                map.put("hak_akses", item);

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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


