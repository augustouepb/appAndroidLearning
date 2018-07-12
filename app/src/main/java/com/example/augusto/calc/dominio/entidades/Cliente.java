package com.example.augusto.calc.dominio.entidades;

import java.io.Serializable;

public class Cliente implements Serializable{
    private int codigo;
    private String nome;
    private String sobreNome;

    public Cliente(){
        codigo = 0;
    }

    public Cliente(int codigo, String nome, String sobreNome) {
        this.codigo = codigo;
        this.nome = nome;
        this.sobreNome = sobreNome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }
}
