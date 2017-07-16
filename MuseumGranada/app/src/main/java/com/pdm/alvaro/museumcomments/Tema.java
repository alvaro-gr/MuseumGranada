package com.pdm.alvaro.museumcomments;

/**
 * Created by alvaro on 21/04/17.
 */

public class Tema {
    private Integer codigo;
    private String nombre;

    public Tema(Integer cod, String n){
        this.codigo = cod;
        this.nombre = n;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


}
