package br.edu.utfpr.leticiasantos.controledeestudos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

public class ConteudoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Conteudo> listaConteudos;

    public ConteudoAdapter(Context context, ArrayList<Conteudo> listaConteudos) {
        this.context = context;
        this.listaConteudos = listaConteudos;
    }


    public int getCount() {
        return listaConteudos.size();
    }


    public Object getItem(int position) {
        return listaConteudos.get(position);
    }


    public long getItemId(int position) {
        return listaConteudos.get(position).getId();
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_conteudo,
                    parent,
                    false
            );
        }

        TextView textViewTitulo = view.findViewById(R.id.textViewTitulo);
        TextView textViewDisciplina = view.findViewById(R.id.textViewDisciplina);
        TextView textViewDificuldade = view.findViewById(R.id.textViewDificuldade);
        TextView textViewStatus = view.findViewById(R.id.textViewStatus);
        TextView textViewData = view.findViewById(R.id.textViewData);

        Conteudo conteudo = listaConteudos.get(position);

        textViewTitulo.setText(conteudo.getTitulo());
        textViewDisciplina.setText("Disciplina: " + conteudo.getDisciplina());
        textViewDificuldade.setText("Dificuldade: " + conteudo.getDificuldade());
        textViewStatus.setText("Status: " + conteudo.getStatus());

        if (conteudo.getDataEstudo() != null) {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofLocalizedDate(FormatStyle.SHORT)
                    .withLocale(Locale.getDefault());

            textViewData.setText("Data: " + conteudo.getDataEstudo().format(formatter));
        } else {
            textViewData.setText("Data: -");
        }

        return view;
    }
}