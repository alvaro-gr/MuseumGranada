package com.pdm.alvaro.museumcomments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelActivity extends AppCompatActivity {

    private Button  miscomentarios, crear, sugerencia;
    private ImageButton salir;
    public String id_usuario, nombre;
    private TextView mensaje;
    private String URL = "http://comentariosgr.esy.es/temas.php";
    private ArrayList<Tema> temas;
    private TemaAdapter adapter;
    private RecyclerView lista;
    private RecyclerView.LayoutManager lm;
    private ProgressDialog barra_progreso;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);


        lista = (RecyclerView) findViewById(R.id.recyler);

        sugerencia = (Button) findViewById(R.id.sugerenciaButton);
        miscomentarios = (Button) findViewById(R.id.verbutton);
        salir = (ImageButton) findViewById(R.id.exitbutton);
        mensaje = (TextView) findViewById(R.id.welcomeText);
        requestQueue = Volley.newRequestQueue(this);

        id_usuario = getIntent().getStringExtra("idUsuario");
        nombre = getIntent().getStringExtra("nombreUsuario");
        temas = new ArrayList<>();

        mensaje.setText("Hola "+nombre+"!");

        cargarDatos();

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


        miscomentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), verComentariosUsuarioActivity.class);
                intent.putExtra("idUsuario",id_usuario);
                intent.putExtra("nombreUsuario",nombre);
                startActivity(intent);
                finish();
            }
        });

        sugerencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SugerenciaActivity.class);
                intent.putExtra("idUsuario",id_usuario);
                intent.putExtra("nombreUsuario",nombre);
                startActivity(intent);
                finish();
            }
        });

    }

    private  void cargarDatos(){

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arrayinput = new JSONArray(response);
                            for (int i =0; i<arrayinput.length(); i++){
                                JSONObject jsonObject = arrayinput.getJSONObject(i);
                                String n = jsonObject.getString("nombre");
                                Integer c = jsonObject.getInt("codigo");
                                temas.add(new Tema(c, n));
                            }
                            adapter = new TemaAdapter(temas,getApplicationContext());
                            lista.setAdapter(adapter);
                            lm = new LinearLayoutManager(getApplicationContext());
                            lista.setLayoutManager(lm);
                            barra_progreso.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barra_progreso.dismiss();
                        Toast.makeText(getApplicationContext(), "No Se ha podido cargar los temas", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), PanelActivity.class);
                        intent.putExtra("nombreUsuario",nombre);
                        intent.putExtra("idUsuario",id_usuario);
                        finish();
                        startActivity(intent);

                    }
                });
        requestQueue.add(jsonObjectRequest);
        barra_progreso = new ProgressDialog(this);
        barra_progreso.setMessage("Cargando Datos ...");
        barra_progreso.show();
    }

    private class TemaAdapter extends RecyclerView.Adapter<TemaAdapter.TemaViewHolder> {
        private List<Tema> items;
        private Context context;

        public class TemaViewHolder extends RecyclerView.ViewHolder {
            // Campos respectivos de un item

            public TextView nombre_tema, cod;
            public Button opinar;

            public TemaViewHolder(View v) {
                super(v);
                nombre_tema = (TextView) v.findViewById(R.id.texttema);
                cod = (TextView) v.findViewById(R.id.codigo);
                opinar = (Button) v.findViewById(R.id.opinar);
            }
        }

        public TemaAdapter(List<Tema> items, Context context) {
            this.items = items;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public TemaAdapter.TemaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(context).inflate(R.layout.tema, viewGroup, false);
            return new TemaAdapter.TemaViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TemaAdapter.TemaViewHolder viewHolder, final int i) {
            viewHolder.nombre_tema.setText(items.get(i).getNombre());

            viewHolder.opinar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, crearComentarioActivity.class);
                    intent.putExtra("codigo_tema",Integer.toString(items.get(i).getCodigo()));
                    intent.putExtra("nombre_tema",items.get(i).getNombre());
                    intent.putExtra("nombreUsuario",nombre);
                    intent.putExtra("idUsuario",id_usuario);
                    finish();
                    startActivity(intent);
                }
            });

        }
    }
}
