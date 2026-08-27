package br.edu.utfpr.leticiasantos.controledeestudos;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SobreActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Sobre");
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}