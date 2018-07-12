package com.example.augusto.calc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Classe criada para criar o banco de dados
public class BdTesteOpenHelper extends SQLiteOpenHelper {
    public BdTesteOpenHelper(Context context) {
        super(context, "bdTeste", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Ao ser instanciada ela executa o sql por meio do db.execSQL
        //O método estático da class ScriptDLL retorna a estrutura de uma tabela em sql
        db.execSQL(ScriptDLL.getCreateTableClient());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
