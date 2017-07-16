package com.pdm.alvaro.museumcomments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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

public class verComentariosUsuarioActivity extends AppCompatActivity {

    private RecyclerView lista;
    private ImageButton volver, salir;
    private ArrayList<Comentario> comentarios;
    private ComentarioAdapter adapter;
    private String URL = "http://comentariosgr.esy.es/comentarios_usuario.php";
    private RequestQueue requestQueue;
    private String id_usuario, nombre;
    private ProgressDialog barra_progreso;
    private RecyclerView.LayoutManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_comentarios_usuario);


        lista = (RecyclerView) findViewById(R.id.lista2);

        volver = (ImageButton) findViewById(R.id.volver2);
        salir = (ImageButton) findViewById(R.id.exitbutton);
        comentarios = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        id_usuario = getIntent().getStringExtra("idUsuario");
        nombre = getIntent().getStringExtra("nombreUsuario");

        cargarDatosUsuario();

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

    }

    private  void cargarDatosUsuario(){

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arrayinput = new JSONArray(response);
                            for (int i =0; i<arrayinput.length(); i++){
                                JSONObject jsonObject = arrayinput.getJSONObject(i);
                                String nombre = jsonObject.getString("nombre");
                                String tema = jsonObject.getString("tema");
                                String comentario = jsonObject.getString("comentario");
                                String fecha = jsonObject.getString("fecha");
                                comentarios.add(new Comentario(nombre, tema, comentario,fecha));
                            }
                            adapter = new ComentarioAdapter(comentarios,getApplicationContext());
                            lista.setAdapter(adapter);
                            lm = new LinearLayoutManager(getApplicationContext());
                            lista.setLayoutManager(lm);
                            barra_progreso.dismiss();;
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
                        Intent intent = new Intent(getApplicationContext(), PanelActivity.class);
                        intent.putExtra("nombreUsuario",nombre);
                        intent.putExtra("idUsuario",id_usuario);
                        finish();
                        startActivity(intent);

                    }
                })  {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_usuario", id_usuario);
                return params;
            }

        };
        requestQueue.add(jsonObjectRequest);
        barra_progreso = new ProgressDialog(this);
        barra_progreso.setMessage("Cargando Datos ...");
        barra_progreso.show();
    }

    private class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> {
        private List<Comentario> items;
        private Context context;

        public class ComentarioViewHolder extends RecyclerView.ViewHolder {
            // Campos respectivos de un item

            public TextView nombre, tema, comentario, fecha;


            public ComentarioViewHolder(View v) {
                super(v);
                nombre = (TextView) v.findViewById(R.id.nombreText);
                tema = (TextView) v.findViewById(R.id.temaText);
                comentario = (TextView) v.findViewById(R.id.comentarioText);
                fecha = (TextView) v.findViewById(R.id.fechaText);
            }
        }

        public ComentarioAdapter(List<Comentario> items, Context context) {
            this.items = items;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public ComentarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
            return new ComentarioViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ComentarioViewHolder viewHolder, int i) {
            viewHolder.nombre.setText(items.get(i).getNombre());
            viewHolder.tema.setText(items.get(i).getTema());
            viewHolder.comentario.setText(items.get(i).getComentario());
            viewHolder.fecha.setText(items.get(i).getFecha());
        }

    }
}
