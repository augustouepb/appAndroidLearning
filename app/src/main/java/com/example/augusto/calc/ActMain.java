package com.example.augusto.calc;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import com.example.augusto.calc.database.BdTesteOpenHelper;
import com.example.augusto.calc.dominio.entidades.Cliente;
import com.example.augusto.calc.dominio.repositorio.ClienteRepositorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.listaClientes)
    RecyclerView listaClientes;
    @BindView(R.id.refresh_list)
    SwipeRefreshLayout refresh_list;

//    private RecyclerView listaClientes;
    private SQLiteDatabase connect;
    private BdTesteOpenHelper bdTesteOpenHelper;
    private ConstraintLayout constraintLayout;

    private ClienteAdapter clienteAdapter;
    private ClienteRepositorio clienteRepositorio;
    private LayoutAnimationController controller = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main);

        refresh_list.setOnRefreshListener(this);
        refresh_list.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener( (view) -> {

//            Intent it = new Intent(ActMain.this, CadastraCliente.class);
            Intent it = new Intent(ActMain.this, MainActivity.class);
            //startforresult abaixo serve para atualizar a lista, precisa sobreescrever o metodo
            //onActivityResult para poder funcionar (não esquecer)
            startActivityForResult(it, 0);

        });

        //Métodos para utilizar okhttp
        OkHttpClient client = new OkHttpClient();
        String url = "https://reqres.in/api/users?page=2";

        //cria requisição com a url criada acima
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String resposta = response.body().string();

                    ActMain.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            
                            System.out.println("################### \n");
                            System.out.println(resposta);
                            System.out.println("################### \n");
                        }
                    });
                }
            }
        });

//        listaClientes = (RecyclerView) findViewById(R.id.listaClientes);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        //Cria conexao com o banco
        getConnection();
        //metodos default para a recyclerview funcionar
        listaClientes.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listaClientes.setLayoutManager(linearLayoutManager);

        //pega os dados que estão no banco
        clienteRepositorio = new ClienteRepositorio(connect);
        List<Cliente> dados = new ArrayList<Cliente>();
        dados = clienteRepositorio.getAll();

        //cria o adaptador com os dados do banco
        clienteAdapter = new ClienteAdapter(dados);
        // informa a lista o adaptador com os dados do banco
        listaClientes.setAdapter(clienteAdapter);

        /*runAnimation(listaClientes,0);
        listaClientes.setLayoutAnimation(controller);
        listaClientes.scheduleLayoutAnimation();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        runAnimation(listaClientes,0);
        listaClientes.setLayoutAnimation(controller);
        listaClientes.scheduleLayoutAnimation();
    }

    public void runAnimation(RecyclerView recyclerView, int type){
        Context context = recyclerView.getContext();

        if(type == 0) {
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        }

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

    //Este método serve para atualizar a lista assim que algum dado for cadastrado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        List<Cliente> dados = new ArrayList<Cliente>();
        dados = clienteRepositorio.getAll();

        //cria o adaptador com os dados do banco
        clienteAdapter = new ClienteAdapter(dados);
        // informa a lista o adaptador com os dados do banco
        listaClientes.setAdapter(clienteAdapter);

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh_list.setRefreshing(false);
            }
        }, 5000);

    }
}
