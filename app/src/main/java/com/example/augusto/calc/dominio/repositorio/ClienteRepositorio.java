package com.example.augusto.calc.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.augusto.calc.dominio.entidades.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepositorio {

    private SQLiteDatabase connection;

    public ClienteRepositorio(SQLiteDatabase connection){
        this.connection = connection;
    }

    public void inserir(Cliente cliente){

        //O objeto contentvalue serve para transformar o objeto java em um tipo que insere no banco
        // basta passar os dados do objeto java para o objeto contentvalue
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", cliente.getNome());
        contentValues.put("SOBRENOME", cliente.getSobreNome()   );
        System.out.println("Nome informado: "+contentValues.get("NOME"));
        //este metodo insere na tabela cliente, os espaços em branco completa com null e os dados
        connection.insertOrThrow("CLIENTE", null, contentValues);
    }

    public void excluir(int codigo){

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);

        //este metodo exclui da tabela cliente, onde o código for "?", a interrogação é substituida
        //pelo array "parametros
        System.out.println("Vou excluir na tabela cliente o codigo: "+parametros[0]);
        connection.delete("CLIENTE","CODIGO = ?",parametros);

    }

    public void alterar(Cliente cliente){

        //O objeto contentvalue serve para transformar o objeto java em um tipo que insere no banco
        // basta passar os dados do objeto java para o objeto contentvalue
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", cliente.getNome());
        contentValues.put("SOBRENOME", cliente.getSobreNome()   );

        //Salva o código do cliente na posição 0 do array para ser colocado no "where" da consulta
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(cliente.getCodigo());

        //este metodo altera na tabela cliente, a "?" do where é substituida pelo array "parametros"
        connection.update("CLIENTE",contentValues, "CODIGO = ?", parametros);

    }

    public List<Cliente> getAll(){

        List<Cliente> clientes = new ArrayList<Cliente>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CODIGO,NOME,SOBRENOME ");
        sql.append(" FROM CLIENTE ");

        Cursor result = connection.rawQuery(sql.toString(),null);

        if(result.getCount() >0) {

            result.moveToFirst();

            do {
                Cliente cli = new Cliente();

                int codigo = result.getInt(result.getColumnIndexOrThrow("CODIGO"));
                String nome = result.getString(result.getColumnIndexOrThrow("NOME"));
                String sobreNome = result.getString(result.getColumnIndexOrThrow("SOBRENOME"));
                cli.setCodigo(codigo);
                cli.setNome(nome);
                cli.setSobreNome(sobreNome);

                clientes.add(cli);

            }while(result.moveToNext());
        }

        return clientes;

    }

    public Cliente buscarCliente(int codigo){

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CODIGO,NOME,SOBRENOME ");
        sql.append(" FROM CLIENTE ");
        sql.append(" WHERE CODIGO = ?");


        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);

        Cursor result = connection.rawQuery(sql.toString(),parametros);

        if(result.getCount() > 0) {

            String nome = result.getString(result.getColumnIndexOrThrow("NOME"));
            String sobreNome = result.getString(result.getColumnIndexOrThrow("SOBRENOME"));
            Cliente cliente = new Cliente(codigo, nome, sobreNome);

            return cliente;
        }
        return null;
    }

}
