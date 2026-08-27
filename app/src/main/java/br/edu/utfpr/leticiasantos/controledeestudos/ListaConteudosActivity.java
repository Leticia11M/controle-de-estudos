package br.edu.utfpr.leticiasantos.controledeestudos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;

public class ListaConteudosActivity extends AppCompatActivity {

    private ListView listViewConteudos;
    private ArrayList<Conteudo> listaConteudos;
    private ConteudoAdapter adapter;

    private ActivityResultLauncher<Intent> launcherCadastro;

    private int posicaoSelecionada = -1;
    private ActionMode actionMode;

    private AppDatabase database;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_conteudos);

        listViewConteudos = findViewById(R.id.listViewConteudos);

        database = AppDatabase.getDatabase(this);

        listaConteudos = new ArrayList<>();
        adapter = new ConteudoAdapter(this, listaConteudos);
        listViewConteudos.setAdapter(adapter);

        launcherCadastro = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Intent data = result.getData();

                        String titulo = data.getStringExtra("titulo");
                        String disciplina = data.getStringExtra("disciplina");
                        int disciplinaId = data.getIntExtra("disciplinaId", 0);
                        String dificuldade = data.getStringExtra("dificuldade");
                        String status = data.getStringExtra("status");
                        String observacao = data.getStringExtra("observacao");
                        boolean revisar = data.getBooleanExtra("revisar", false);
                        String dataEstudoTexto = data.getStringExtra("dataEstudo");

                        int posicao = data.getIntExtra("posicao", -1);
                        int idConteudo = data.getIntExtra("id", 0);

                        LocalDate dataEstudo;

                        if (dataEstudoTexto != null) {
                            dataEstudo = LocalDate.parse(dataEstudoTexto);
                        } else {
                            dataEstudo = LocalDate.now();
                        }

                        Conteudo conteudo = new Conteudo(
                                titulo,
                                disciplina,
                                dificuldade,
                                status,
                                observacao,
                                revisar,
                                dataEstudo,
                                disciplinaId
                        );

                        if (posicao == -1) {
                            database.conteudoDao().inserir(conteudo);
                            Toast.makeText(this, "Conteúdo adicionado", Toast.LENGTH_SHORT).show();
                        } else {
                            conteudo.setId(idConteudo);
                            database.conteudoDao().atualizar(conteudo);
                            Toast.makeText(this, "Conteúdo editado", Toast.LENGTH_SHORT).show();
                        }

                        carregarConteudos();
                    }
                }
        );

        listViewConteudos.setOnItemClickListener((parent, view, position, id) -> {
            Conteudo conteudo = listaConteudos.get(position);
            Toast.makeText(this, "Clicou: " + conteudo.getTitulo(), Toast.LENGTH_SHORT).show();
        });

        listViewConteudos.setOnItemLongClickListener((parent, view, position, id) -> {

            posicaoSelecionada = position;

            if (actionMode != null) {
                return true;
            }

            actionMode = startActionMode(actionModeCallback);
            view.setActivated(true);

            return true;
        });

        carregarConteudos();
    }


    protected void onResume() {
        super.onResume();
        carregarConteudos();
    }

    private void carregarConteudos() {
        SharedPreferences prefs = getSharedPreferences("configuracoes", MODE_PRIVATE);
        String ordenacao = prefs.getString("ordenacao", "titulo");

        listaConteudos.clear();

        if (ordenacao.equals("disciplina")) {
            listaConteudos.addAll(database.conteudoDao().listarPorDisciplina());
        } else {
            listaConteudos.addAll(database.conteudoDao().listarPorTitulo());
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuAdicionar) {
            Intent intent = new Intent(this, MainActivity.class);
            launcherCadastro.launch(intent);
            return true;
        }

        if (item.getItemId() == R.id.menuDisciplinas) {
            Intent intent = new Intent(this, DisciplinaActivity.class);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.menuConfiguracoes) {
            Intent intent = new Intent(this, ConfiguracoesActivity.class);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.menuSobre) {
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {


        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("");
            mode.getMenuInflater().inflate(R.menu.menu_contextual, menu);
            return true;
        }


        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if (posicaoSelecionada < 0 || posicaoSelecionada >= listaConteudos.size()) {
                mode.finish();
                return false;
            }

            Conteudo conteudo = listaConteudos.get(posicaoSelecionada);

            if (item.getItemId() == R.id.menuEditar) {

                Intent intent = new Intent(ListaConteudosActivity.this, MainActivity.class);

                intent.putExtra("modoEdicao", true);
                intent.putExtra("posicao", posicaoSelecionada);
                intent.putExtra("id", conteudo.getId());
                intent.putExtra("titulo", conteudo.getTitulo());
                intent.putExtra("disciplina", conteudo.getDisciplina());
                intent.putExtra("disciplinaId", conteudo.getDisciplinaId());
                intent.putExtra("dificuldade", conteudo.getDificuldade());
                intent.putExtra("status", conteudo.getStatus());
                intent.putExtra("observacao", conteudo.getObservacao());
                intent.putExtra("revisar", conteudo.isRevisar());

                if (conteudo.getDataEstudo() != null) {
                    intent.putExtra("dataEstudo", conteudo.getDataEstudo().toString());
                }

                launcherCadastro.launch(intent);

                mode.finish();
                return true;
            }

            if (item.getItemId() == R.id.menuExcluir) {
                confirmarExclusao(conteudo);
                mode.finish();
                return true;
            }

            return false;
        }


        public void onDestroyActionMode(ActionMode mode) {

            actionMode = null;

            if (posicaoSelecionada >= 0) {

                int firstVisible = listViewConteudos.getFirstVisiblePosition();
                int childIndex = posicaoSelecionada - firstVisible;

                if (childIndex >= 0 &&
                        childIndex < listViewConteudos.getChildCount()) {

                    listViewConteudos
                            .getChildAt(childIndex)
                            .setActivated(false);
                }
            }

            posicaoSelecionada = -1;
        }
    };

    private void confirmarExclusao(Conteudo conteudo) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir conteúdo")
                .setMessage("Deseja realmente excluir este conteúdo?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    database.conteudoDao().excluir(conteudo);
                    carregarConteudos();

                    Toast.makeText(
                            ListaConteudosActivity.this,
                            "Conteúdo excluído",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .setNegativeButton("Não", null)
                .show();
    }
}