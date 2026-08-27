package br.edu.utfpr.leticiasantos.controledeestudos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {Disciplina.class, Conteudo.class},
        version = 1,
        exportSchema = true
)
@TypeConverters({Conversores.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;

    public abstract DisciplinaDao disciplinaDao();

    public abstract ConteudoDao conteudoDao();

    public static AppDatabase getDatabase(Context context) {

        if (instancia == null) {
            instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "controle_estudos.db"
                    )
                    .allowMainThreadQueries()
                    .build();
        }

        return instancia;
    }
}