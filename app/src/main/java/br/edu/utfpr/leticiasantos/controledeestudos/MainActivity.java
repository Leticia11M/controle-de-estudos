package br.edu.utfpr.leticiasantos.controledeestudos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText editTextConteudo;
    EditText editTextObservacao;
    RadioGroup radioGroupStatus;
    CheckBox checkBoxRevisao;
    Spinner spinnerDisciplina;
    Spinner spinnerDificuldade;
    Button buttonDataEstudo;

    int posicaoEdicao = -1;
    int idEdicao = 0;

    ArrayList<Disciplina> listaDisciplinas;
    String[] dificuldades;

    AppDatabase database;
    SharedPreferences preferences;

    LocalDate dataSelecionada;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextConteudo = findViewById(R.id.editTextConteudo);
        editTextObservacao = findViewById(R.id.editTextObservacao);
        radioGroupStatus = findViewById(R.id.radioGroupStatus);
        checkBoxRevisao = findViewById(R.id.checkBoxRevisao);
        spinnerDisciplina = findViewById(R.id.spinnerDisciplina);
        spinnerDificuldade = findViewById(R.id.spinnerDificuldade);
        buttonDataEstudo = findViewById(R.id.buttonDataEstudo);

        preferences = getSharedPreferences("configuracoes", MODE_PRIVATE);
        database = AppDatabase.getDatabase(this);

        dataSelecionada = LocalDate.now();
        buttonDataEstudo.setText(dataSelecionada.toString());

        buttonDataEstudo.setOnClickListener(v -> abrirDatePicker());

        carregarSpinnerDisciplinas();
        carregarSpinnerDificuldades();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        receberDadosParaEdicao();
    }

    private void abrirDatePicker() {
        Calendar calendario = Calendar.getInstance();

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        MainActivity.this,
                        (view, year, month, dayOfMonth) -> {
                            dataSelecionada = LocalDate.of(year, month + 1, dayOfMonth);
                            buttonDataEstudo.setText(dataSelecionada.toString());
                        },
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)
                );

        datePickerDialog.show();
    }

    private void carregarSpinnerDisciplinas() {
        listaDisciplinas = new ArrayList<>();
        listaDisciplinas.addAll(database.disciplinaDao().listarTodas());

        ArrayAdapter<Disciplina> adapterDisciplinas = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaDisciplinas
        );

        adapterDisciplinas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisciplina.setAdapter(adapterDisciplinas);
    }

    private void carregarSpinnerDificuldades() {
        dificuldades = getResources().getStringArray(R.array.dificuldades);

        ArrayAdapter<String> adapterDificuldades = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dificuldades
        );

        adapterDificuldades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDificuldade.setAdapter(adapterDificuldades);
    }

    private void receberDadosParaEdicao() {
        Intent intent = getIntent();

        boolean modoEdicao = intent.getBooleanExtra("modoEdicao", false);

        if (modoEdicao) {
            posicaoEdicao = intent.getIntExtra("posicao", -1);
            idEdicao = intent.getIntExtra("id", 0);

            String titulo = intent.getStringExtra("titulo");
            String disciplina = intent.getStringExtra("disciplina");
            String dificuldade = intent.getStringExtra("dificuldade");
            String status = intent.getStringExtra("status");
            String observacao = intent.getStringExtra("observacao");
            String dataEstudo = intent.getStringExtra("dataEstudo");

            editTextConteudo.setText(titulo);

            if (observacao != null) {
                editTextObservacao.setText(observacao);
            }

            if (dataEstudo != null) {
                dataSelecionada = LocalDate.parse(dataEstudo);
                buttonDataEstudo.setText(dataSelecionada.toString());
            }

            selecionarDisciplinaNoSpinner(disciplina);
            selecionarSpinnerString(spinnerDificuldade, dificuldades, dificuldade);

            if (status != null && status.contains(getString(R.string.status_estudado))) {
                radioGroupStatus.check(R.id.radioEstudado);
            } else {
                radioGroupStatus.check(R.id.radioPendente);
            }

            if (status != null && status.contains(getString(R.string.revisar))) {
                checkBoxRevisao.setChecked(true);
            }

            setTitle(getString(R.string.editar_conteudo));
        } else {
            setTitle(getString(R.string.cadastrar_conteudo));
        }
    }

    private void selecionarDisciplinaNoSpinner(String nomeDisciplina) {
        if (nomeDisciplina == null || listaDisciplinas == null) {
            return;
        }

        for (int i = 0; i < listaDisciplinas.size(); i++) {
            if (listaDisciplinas.get(i).getNome().equals(nomeDisciplina)) {
                spinnerDisciplina.setSelection(i);
                return;
            }
        }
    }

    private void selecionarSpinnerString(Spinner spinner, String[] vetor, String valor) {
        if (valor == null) {
            return;
        }

        for (int i = 0; i < vetor.length; i++) {
            if (vetor[i].equals(valor)) {
                spinner.setSelection(i);
                return;
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastro, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuSalvar) {
            salvarConteudo();
            return true;
        }

        if (item.getItemId() == R.id.menuLimpar) {
            limparCampos();
            return true;
        }

        if (item.getItemId() == R.id.menuSugerirTitulo) {
            String ultimoTitulo = preferences.getString("ultimoTitulo", "");

            if (!ultimoTitulo.isEmpty()) {
                editTextConteudo.setText(ultimoTitulo);
                editTextConteudo.setSelection(editTextConteudo.getText().length());
                Toast.makeText(this, getString(R.string.titulo_sugerido), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.nenhum_titulo_salvo), Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void limparCampos() {
        editTextConteudo.setText("");
        editTextObservacao.setText("");
        radioGroupStatus.clearCheck();
        checkBoxRevisao.setChecked(false);

        if (!listaDisciplinas.isEmpty()) {
            spinnerDisciplina.setSelection(0);
        }

        spinnerDificuldade.setSelection(0);

        dataSelecionada = LocalDate.now();
        buttonDataEstudo.setText(dataSelecionada.toString());

        editTextConteudo.requestFocus();

        Toast.makeText(this, getString(R.string.campos_limpos), Toast.LENGTH_SHORT).show();
    }

    private void salvarConteudo() {
        String titulo = editTextConteudo.getText().toString().trim();
        String observacao = editTextObservacao.getText().toString().trim();

        if (titulo.isEmpty()) {
            Toast.makeText(this, getString(R.string.digite_nome_conteudo), Toast.LENGTH_SHORT).show();
            editTextConteudo.requestFocus();
            return;
        }

        if (observacao.isEmpty()) {
            Toast.makeText(this, getString(R.string.digite_observacao), Toast.LENGTH_SHORT).show();
            editTextObservacao.requestFocus();
            return;
        }

        int radioSelecionado = radioGroupStatus.getCheckedRadioButtonId();

        if (radioSelecionado == -1) {
            Toast.makeText(this, getString(R.string.selecione_status), Toast.LENGTH_SHORT).show();
            return;
        }

        if (listaDisciplinas.isEmpty()) {
            Toast.makeText(this, "Cadastre uma disciplina primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

        Disciplina disciplinaSelecionada = (Disciplina) spinnerDisciplina.getSelectedItem();

        String disciplina = disciplinaSelecionada.getNome();
        int disciplinaId = disciplinaSelecionada.getId();

        RadioButton radioButton = findViewById(radioSelecionado);
        String status = radioButton.getText().toString();

        String dificuldade = spinnerDificuldade.getSelectedItem().toString();

        if (dificuldade.equals(getString(R.string.selecione))) {
            Toast.makeText(this, getString(R.string.selecione_dificuldade), Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ultimoTitulo", titulo);
        editor.apply();

        boolean precisaRevisao = checkBoxRevisao.isChecked();

        if (precisaRevisao) {
            status = status + " | " + getString(R.string.revisar);
        }

        Intent intent = new Intent();

        intent.putExtra("titulo", titulo);
        intent.putExtra("disciplina", disciplina);
        intent.putExtra("disciplinaId", disciplinaId);
        intent.putExtra("dificuldade", dificuldade);
        intent.putExtra("status", status);
        intent.putExtra("observacao", observacao);
        intent.putExtra("revisar", precisaRevisao);
        intent.putExtra("dataEstudo", dataSelecionada.toString());
        intent.putExtra("posicao", posicaoEdicao);
        intent.putExtra("id", idEdicao);

        setResult(RESULT_OK, intent);
        finish();
    }


    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }
}