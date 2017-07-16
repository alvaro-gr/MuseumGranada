package com.pdm.alvaro.museumcomments;

/**
 * Created by alvaro on 11/04/17.
 */

public class Comentario {

    private  String nombre, tema, comentario, fecha;

    public Comentario(String nombre, String tema, String comentario, String fecha){
        this.nombre = nombre;
        this.tema = tema;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setTema(String tema){
        this.tema = tema;
    }

    public void setComentario(String comentario){
        this.comentario = comentario;
    }

    public String getNombre(){
        return  this.nombre;
    }

    public String getTema() {
        return this.tema;
    }

    public String getComentario() {
        return this.comentario;
    }
}
