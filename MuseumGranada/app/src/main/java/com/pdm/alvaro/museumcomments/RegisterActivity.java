package com.pdm.alvaro.museumcomments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import java.util.HashMap;
import java.util.Map;

import org.json.*;

public class RegisterActivity extends AppCompatActivity {

    private Button registrar;
    private TextInputLayout user, ema, pass;
    private String URL = "http://comentariosgr.esy.es/alta_usuario.php";
    private RequestQueue requestQueue;
    private ProgressDialog barra_progreso;
    private ImageButton volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = (TextInputLayout) findViewById(R.id.input_nombre);
        ema = (TextInputLayout) findViewById(R.id.input_email);
        pass = (TextInputLayout) findViewById(R.id.input_pass);
        registrar = (Button) findViewById(R.id.buttonRegister);
        volver = (ImageButton) findViewById(R.id.botonVolver);

        requestQueue = Volley.newRequestQueue(this);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    private void registrarUsuario(){
        final String username = user.getEditText().getText().toString().trim();
        final String email = ema.getEditText().getText().toString().trim();
        final String password = pass.getEditText().getText().toString().trim();

        if (username.equals("") || email.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(), "Algún campo está vacio", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            finish();
            startActivity(intent);
        } else {

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            barra_progreso.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                String m = jsonObject.getString("mensaje");
                                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                finish();
                                startActivity(intent);
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
                    params.put("nombre", username);
                    params.put("correo", email);
                    params.put("password", password);
                    return params;
                }
            };

            requestQueue.add(jsonObjectRequest);
            barra_progreso = new ProgressDialog(this);
            barra_progreso.setMessage("Registrando ...");
            barra_progreso.show();


        }
    }


}







