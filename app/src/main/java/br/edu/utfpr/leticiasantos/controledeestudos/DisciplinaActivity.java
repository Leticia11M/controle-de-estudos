package br.edu.utfpr.leticiasantos.controledeestudos;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisciplinaActivity extends AppCompatActivity {

    private EditText editTextNomeDisciplina;
    private EditText editTextProfessor;
    private Button buttonSalvarDisciplina;
    private ListView listViewDisciplinas;

    private AppDatabase database;
    private ArrayList<Disciplina> listaDisciplinas;
    private ArrayAdapter<Disciplina> adapter;

    private Disciplina disciplinaSelecionada = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);

        editTextNomeDisciplina = findViewById(R.id.editTextNomeDisciplina);
        editTextProfessor = findViewById(R.id.editTextProfessor);
        buttonSalvarDisciplina = findViewById(R.id.buttonSalvarDisciplina);
        listViewDisciplinas = findViewById(R.id.listViewDisciplinas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = AppDatabase.getDatabase(this);

        listaDisciplinas = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaDisciplinas
        );

        listViewDisciplinas.setAdapter(adapter);

        carregarDisciplinas();

        buttonSalvarDisciplina.setOnClickListener(v -> salvarDisciplina());

        listViewDisciplinas.setOnItemClickListener((parent, view, position, id) -> {
            disciplinaSelecionada = listaDisciplinas.get(position);

            editTextNomeDisciplina.setText(disciplinaSelecionada.getNome());
            editTextProfessor.setText(disciplinaSelecionada.getProfessor());

            Toast.makeText(this, "Editando disciplina", Toast.LENGTH_SHORT).show();
        });

        listViewDisciplinas.setOnItemLongClickListener((parent, view, position, id) -> {
            Disciplina disciplina = listaDisciplinas.get(position);
            confirmarExclusao(disciplina);
            return true;
        });
    }

    private void salvarDisciplina() {
        String nome = editTextNomeDisciplina.getText().toString().trim();
        String professor = editTextProfessor.getText().toString().trim();

        if (nome.isEmpty()) {
            Toast.makeText(this, "Digite o nome da disciplina", Toast.LENGTH_SHORT).show();
            editTextNomeDisciplina.requestFocus();
            return;
        }

        if (professor.isEmpty()) {
            Toast.makeText(this, "Digite o nome do professor", Toast.LENGTH_SHORT).show();
            editTextProfessor.requestFocus();
            return;
        }

        if (disciplinaSelecionada == null) {
            Disciplina disciplina = new Disciplina(nome, professor);
            database.disciplinaDao().inserir(disciplina);

            Toast.makeText(this, "Disciplina cadastrada", Toast.LENGTH_SHORT).show();

        } else {
            disciplinaSelecionada.setNome(nome);
            disciplinaSelecionada.setProfessor(professor);

            database.disciplinaDao().atualizar(disciplinaSelecionada);

            Toast.makeText(this, "Disciplina atualizada", Toast.LENGTH_SHORT).show();

            disciplinaSelecionada = null;
        }

        limparCampos();
        carregarDisciplinas();
    }

    private void carregarDisciplinas() {
        listaDisciplinas.clear();
        listaDisciplinas.addAll(database.disciplinaDao().listarTodas());
        adapter.notifyDataSetChanged();
    }

    private void confirmarExclusao(Disciplina disciplina) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir disciplina")
                .setMessage("Deseja realmente excluir esta disciplina?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    database.disciplinaDao().excluir(disciplina);
                    carregarDisciplinas();
                    limparCampos();

                    Toast.makeText(this, "Disciplina excluída", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void limparCampos() {
        editTextNomeDisciplina.setText("");
        editTextProfessor.setText("");
        editTextNomeDisciplina.requestFocus();
    }


    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}