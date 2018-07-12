package com.example.augusto.calc;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.augusto.calc.database.BdTesteOpenHelper;
import com.example.augusto.calc.dominio.entidades.Cliente;
import com.example.augusto.calc.dominio.repositorio.ClienteRepositorio;

public class CadastraCliente extends AppCompatActivity {

    private EditText textNome;
    private EditText textSobreNome;
    private BdTesteOpenHelper bdTesteOpenHelper;
    private ClienteRepositorio clienteRepositorio;
    private ConstraintLayout constraintLayout;
    private SQLiteDatabase connect;
    private Cliente cliente;
    private FloatingActionButton botCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //habilita a seta de voltar, é necessário colocar o metodo de fechar lá no metodo
        //onoptionitemselected
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textNome      = (EditText) findViewById(R.id.textNome);
        textSobreNome = (EditText) findViewById(R.id.textSobreNome);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //este trecho faz a transição de telas, neste caso a tela a ser aberta é ActMain.class
                Intent it = new Intent(CadastraCliente.this, ActMain.class);
                startActivity(it);

            }
        });

        botCadastrar = (FloatingActionButton) findViewById(R.id.botCadastrar);
        botCadastrar.setOnClickListener( (view) -> {
            confirm();
        });
        verificaParametro();
    }

    private void verificaParametro(){
        //metodo para pegar o objeto extra que foi passado na classe ClienteAdapter
        Bundle bundle = getIntent().getExtras();
        cliente = new Cliente();
        //O nome informado para o cliente extra foi "CLIENTE", aqui testa se o extra passado foi
        // um cliente
        if(bundle != null && bundle.containsKey("CLIENTE")){

            cliente = (Cliente)bundle.getSerializable("CLIENTE");
            System.out.println("CLIENTE RECEBIDO: "+cliente.getCodigo());
            textNome.setText(cliente.getNome());
            textSobreNome.setText(cliente.getSobreNome());

        }

    }

    //seta o menu dessa atividade com o menu_act_cad_cliente que foi criado dentro da pasta menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_cliente, menu);
        getConnection();

        return super.onCreateOptionsMenu(menu);
    }

    public boolean validation (){
        boolean resultado = false;

        String nome = textNome.getText().toString();
        String sobreNome = textSobreNome.getText().toString();
        cliente.setNome(nome);
        cliente.setSobreNome(sobreNome);

        if (resultado = testEmptyField(nome)){
            textNome.requestFocus();
        }else if (resultado = testEmptyField(sobreNome)){
            textSobreNome.requestFocus();
        }

        if(resultado){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Aviso");
            dialog.setMessage("Há algum campo inválido!");
            dialog.setNeutralButton("Ok",null);
            dialog.show();

        }
        return resultado;
    }

    public boolean testEmptyField(String data){

        boolean result = (TextUtils.isEmpty(data) || data.trim().isEmpty());
        return result;
    }

    //Aplica eventos aos itens dos menus, os itens são selecinados com o R.id.nomeItem
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clienteRepositorio = new ClienteRepositorio(connect);
        int id = item.getItemId();

        switch(id){

            case android.R.id.home:
                finish();
                break;

            case R.id.action_ok:
                System.out.println("Botao ok pressionado");
                confirm();
//                Toast.makeText(this, "Botao ok pressionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu_excluir:

                if(cliente != null) {
                    System.out.println("CODIGO A SER EXCLUIDO: " + cliente.getCodigo());
                    clienteRepositorio.excluir(cliente.getCodigo());
                    finish();
                    break;
                }else{
                    finish();
                    break;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getConnection(){
        try{

            bdTesteOpenHelper = new BdTesteOpenHelper(this);
            connect = bdTesteOpenHelper.getWritableDatabase();

        }catch(SQLException e){
            AlertDialog.Builder dialogEx = new AlertDialog.Builder(this);
            dialogEx.setTitle("Error");
            dialogEx.setMessage(e.getMessage());
            dialogEx.setNeutralButton("Ok",null);
            dialogEx.show();
            e.printStackTrace();
        }
    }

    private void confirm(){
        clienteRepositorio = new ClienteRepositorio(connect);
        if(!validation()){
            try {
                //Esse if testa se o cliente é novo(codigo 0 que é default), caso o codigo não seja
                //0 é porque o cliente ja existe e apenas deseja altera-lo
                if(cliente.getCodigo() == 0) {
                    System.out.println("Metodo inserir, codigo: "+cliente.getCodigo());
                    clienteRepositorio.inserir(cliente);
                    finish();
                }else{
                    System.out.println("Metodo alterar, codigo: "+cliente.getCodigo());
                    clienteRepositorio.alterar(cliente);
                    finish();
                }
            }catch(SQLException e){
                AlertDialog.Builder dialogEx = new AlertDialog.Builder(this);
                dialogEx.setTitle("Error");
                dialogEx.setMessage(e.getMessage());
                dialogEx.setNeutralButton("Ok",null);
                dialogEx.show();
                e.printStackTrace();
            }

        }

    }

}
