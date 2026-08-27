package br.edu.utfpr.leticiasantos.controledeestudos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracoesActivity extends AppCompatActivity {

    private RadioButton radioTitulo;
    private RadioButton radioDisciplina;
    private Button buttonSalvarConfig;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        radioTitulo = findViewById(R.id.radioTitulo);
        radioDisciplina = findViewById(R.id.radioDisciplina);
        buttonSalvarConfig = findViewById(R.id.buttonSalvarConfig);

        SharedPreferences prefs = getSharedPreferences("configuracoes", MODE_PRIVATE);

        String ordenacao = prefs.getString("ordenacao", "titulo");

        if (ordenacao.equals("disciplina")) {
            radioDisciplina.setChecked(true);
        } else {
            radioTitulo.setChecked(true);
        }

        buttonSalvarConfig.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();

            if (radioDisciplina.isChecked()) {
                editor.putString("ordenacao", "disciplina");
            } else {
                editor.putString("ordenacao", "titulo");
            }

            editor.apply();

            Toast.makeText(this, "Configuração salva", Toast.LENGTH_SHORT).show();

            finish();
        });
    }
}