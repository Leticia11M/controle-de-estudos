package br.edu.utfpr.leticiasantos.controledeestudos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DisciplinaDao {

    @Insert
    long inserir(Disciplina disciplina);

    @Update
    void atualizar(Disciplina disciplina);

    @Delete
    void excluir(Disciplina disciplina);

    @Query("SELECT * FROM disciplinas ORDER BY nome")
    List<Disciplina> listarTodas();

    @Query("SELECT * FROM disciplinas WHERE id = :id")
    Disciplina buscarPorId(int id);
}