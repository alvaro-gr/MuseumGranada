package com.pdm.alvaro.museumcomments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class SugerenciaActivity extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    public String id_usuario, nombre;
    private EditText sugerencia;
    private ImageButton salir, volver;
    private String tema;
    private String URL = "http://comentariosgr.esy.es/crear_sugerencia.php";
    private ProgressDialog barra_progreso;
    private RequestQueue requestQueue;
    private Button enviar_sugerencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerencia);

        requestQueue = Volley.newRequestQueue(this);

        id_usuario = getIntent().getStringExtra("idUsuario");
        nombre = getIntent().getStringExtra("nombreUsuario");

        salir = (ImageButton) findViewById(R.id.exitButton);
        volver = (ImageButton) findViewById(R.id.returnButton);
        sugerencia = (EditText) findViewById(R.id.etSugerencia);
        enviar_sugerencia = (Button) findViewById(R.id.sugerenciaButton);

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter =ArrayAdapter.createFromResource(this,R.array.opciones_museo, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 tema = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PanelActivity.class);
                intent.putExtra("nombreUsuario",nombre);
                intent.putExtra("idUsuario",id_usuario);
                startActivity(intent);
                finish();
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        enviar_sugerencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarSugerencia();
            }
        });

    }

    private void insertarSugerencia(){


        if(sugerencia.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Algún campo está vacio", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), SugerenciaActivity.class);
            intent.putExtra("idUsuario",id_usuario);
            intent.putExtra("nombreUsuario",nombre);
            finish();
            startActivity(intent);
        } else{

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            barra_progreso.dismiss();
                            try {
                                JSONObject jsonObject= new JSONObject(response.toString());
                                String m = jsonObject.getString("mensaje");
                                Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), PanelActivity.class);
                                intent.putExtra("nombreUsuario",nombre);
                                intent.putExtra("idUsuario",id_usuario);
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
                            Toast.makeText(getApplicationContext(), "Ha habido un error", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), crearComentarioActivity.class);
                            intent.putExtra("nombreUsuario",nombre);
                            intent.putExtra("idUsuario",id_usuario);
                            finish();
                            startActivity(intent);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_usuario", id_usuario);
                    params.put("tema", tema);
                    params.put("sugerencia", sugerencia.getText().toString());
                    return params;
                }
            };
            requestQueue.add(jsonObjectRequest);
            barra_progreso = new ProgressDialog(this);
            barra_progreso.setMessage("Creando ...");
            barra_progreso.show();
        }

    }


}
