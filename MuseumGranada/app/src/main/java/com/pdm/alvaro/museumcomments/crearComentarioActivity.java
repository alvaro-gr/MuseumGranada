package com.pdm.alvaro.museumcomments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
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

public class crearComentarioActivity extends AppCompatActivity {

    private Button crear;
    private ImageButton volver, salir;
    private TextView tema;
    private EditText comentario;
    private RatingBar puntuacion;
    private String id_usuario, nombre, codigo_tema, nombre_tema;
    private String URL = "http://comentariosgr.esy.es/insertar_comentario.php";
    private RequestQueue requestQueue;
    private ProgressDialog barra_progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_comentario);

        salir = (ImageButton) findViewById(R.id.exitbutton);
        volver = (ImageButton) findViewById(R.id.returnButton);
        crear = (Button) findViewById(R.id.crearbutton);
        tema = (TextView) findViewById(R.id.editTema);
        comentario = (EditText) findViewById(R.id.editComentario);
        puntuacion = (RatingBar) findViewById(R.id.ratingBar);

        id_usuario = getIntent().getStringExtra("idUsuario");
        nombre = getIntent().getStringExtra("nombreUsuario");
        codigo_tema = getIntent().getStringExtra("codigo_tema");
        nombre_tema = getIntent().getStringExtra("nombre_tema");

        tema.setText(nombre_tema);

        requestQueue = Volley.newRequestQueue(this);

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

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarComentario();
            }
        });
    }

    private void insertarComentario(){

        final String c = comentario.getText().toString().trim();

        if(c.equals("")) {
            Toast.makeText(getApplicationContext(), "Algún campo está vacio", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), crearComentarioActivity.class);
            intent.putExtra("nombreUsuario", nombre);
            intent.putExtra("nombre_tema", nombre_tema);
            finish();
            startActivity(intent);
        } else if (c.length() > 250){
            Toast.makeText(getApplicationContext(), "El comentario no puede tener más de 250 caracteres", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), crearComentarioActivity.class);
            intent.putExtra("nombreUsuario", nombre);
            intent.putExtra("nombre_tema", nombre_tema);
            finish();
            startActivity(intent);
        }else{

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
                            intent.putExtra("nombreUsuario", nombre);
                            intent.putExtra("nombre_tema", nombre_tema);
                            finish();
                            startActivity(intent);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_usuario", id_usuario);
                    params.put("id_tema", codigo_tema);
                    params.put("comentario", comentario.getText().toString().trim());
                    params.put("puntuacion", String.valueOf(puntuacion.getRating()));
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
