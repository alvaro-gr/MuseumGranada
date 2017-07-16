package com.pdm.alvaro.museumcomments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextInputLayout ema, pass;
    private String URL = "http://comentariosgr.esy.es/consultar_usuario.php";
    private RequestQueue requestQueue;
    private ProgressDialog barra_progreso;
    private TextView registrar;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ema = (TextInputLayout) findViewById(R.id.input_email);
        pass = (TextInputLayout) findViewById(R.id.input_pass);
        login = (Button) findViewById(R.id.buttonlogin);
        registrar = (TextView) findViewById(R.id.textviewRegistrar);
        cardView = (CardView) findViewById(R.id.card);

        requestQueue = Volley.newRequestQueue(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "http://www.cajagranadafundacion.es/museo/conoceelmuseo.html";
                Intent intent = null;
                intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });



    }

    private void login(){
        final String email = ema.getEditText().getText().toString().trim();
        final String password = pass.getEditText().getText().toString().trim();

        if (email.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(), "Algún campo está vacio", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            finish();
            startActivity(intent);
        } else {

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            barra_progreso.dismiss();
                            try {
                                JSONObject jsonObject= new JSONObject(response.toString());
                                String m = jsonObject.getString("mensaje");
                                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
                                if (m.equals("Bienvenido")){
                                    String id_usuario = jsonObject.getString("id");
                                    String nombre = jsonObject.getString("nombre");
                                    Intent intent = new Intent(getApplicationContext(), PanelActivity.class);
                                    intent.putExtra("idUsuario",id_usuario);
                                    intent.putExtra("nombreUsuario",nombre);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            barra_progreso.dismiss();
                            Toast.makeText(getApplicationContext(), "No hay conexion", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }

            };

            requestQueue.add(jsonObjectRequest);
            barra_progreso = new ProgressDialog(this);
            barra_progreso.setMessage("Iniciando Sesion ...");
            barra_progreso.show();
        }
    }


}
