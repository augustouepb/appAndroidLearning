package com.example.augusto.calc.database;

//Classe criada para retornar, em string, a estrutura das tabelas do banco
public class ScriptDLL {

    public ScriptDLL(){

    }

    public static String getCreateTableClient(){
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE IF NOT EXISTS CLIENTE (");
        sql.append("    CODIGO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sql.append("    NOME VARCHAR (250) NOT NULL DEFAULT (''), ");
        sql.append("    SOBRENOME VARCHAR (250) NOT NULL DEFAULT ('') ) ");

        return sql.toString();
    }
}
