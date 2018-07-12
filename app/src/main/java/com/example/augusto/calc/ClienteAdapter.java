package com.example.augusto.calc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.augusto.calc.dominio.entidades.Cliente;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

//classe utilizada para povoar a recycleview e o holder é para controlar o front da lista
public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolderCliente> {

    private static List<Cliente> dados;

    public ClienteAdapter(List<Cliente> dados){
        this.dados = dados;
        for(int i=0;i<dados.size();i++){
            System.out.println("Codigo: "+dados.get(i).getCodigo());
        }
    }

    //metodo que cria a referencia do adaptador com o layout criado anteriormente(linha_pessoa.xml)
    @NonNull
    @Override
    public ViewHolderCliente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.linha_pessoa, parent, false);

        ViewHolderCliente viewHolderCliente = new ViewHolderCliente(view, parent.getContext());

        return viewHolderCliente;
    }

    //Método responsãvel por pegar os dados e povoar a lista
    @Override
    public void onBindViewHolder(@NonNull ViewHolderCliente holder, int position) {

        if(dados != null && dados.size()>0) {

            Cliente cli = dados.get(position);

            holder.textNome.setText(cli.getNome());
            holder.textSobreNome.setText(cli.getSobreNome());

        }
    }

    public static List<Cliente> getDados(){
        return dados;
    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public static class ViewHolderCliente extends RecyclerView.ViewHolder{

        public TextView textNome;
        public TextView textSobreNome;
        public static View itemView;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public ViewHolderCliente(View itemView, final Context context) {
            super(itemView);

            this.itemView = itemView;
            textNome      = (TextView) itemView.findViewById(R.id.textNome);
            textSobreNome = (TextView) itemView.findViewById(R.id.textSobreNome);

            itemView.setOnClickListener((view) ->{
                //Este if é para tirar a seleção e o fundo do item caso ele esteja selecionado
                if(itemView.isSelected()){
                    itemView.setSelected(false);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    return ;
                }else {
                    if (ClienteAdapter.getDados().size() > 0) {
                        //o metodo getlayoutposition pega a posicao do item que foi clicado na lista
                        // dados.get(getlayoutposition) pega o objeto que foi clicado e salva em cli
                        Cliente cli = dados.get(getLayoutPosition());
//                    Toast.makeText(context,"Nome: "+ cli.getNome(),Toast.LENGTH_SHORT).show();

                        Intent it = new Intent(context, CadastraCliente.class);
                        //o metodo putExtra passa o objeto cli como parametro extra
                        it.putExtra("CLIENTE", cli);
                        ((AppCompatActivity) context).startActivityForResult(it, 0);
                    }
                }

//
            });

            itemView.setOnLongClickListener((View view) -> {
                //Este if é para controlar os itens que estão selecionados
                if(!itemView.isSelected()) {
                    itemView.setSelected(true);
                    view.setBackgroundColor(Color.CYAN);
                    Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show();
                    return true;
                }else{
                    itemView.setSelected(false);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    return true;
                }
            });
        }

    }

}
