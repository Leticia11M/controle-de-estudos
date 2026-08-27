package br.edu.utfpr.leticiasantos.controledeestudos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ConteudoDao {

    @Insert
    long inserir(Conteudo conteudo);

    @Update
    void atualizar(Conteudo conteudo);

    @Delete
    void excluir(Conteudo conteudo);

    @Query("SELECT * FROM conteudos ORDER BY titulo")
    List<Conteudo> listarPorTitulo();

    @Query("SELECT * FROM conteudos ORDER BY disciplina")
    List<Conteudo> listarPorDisciplina();

    @Query("SELECT * FROM conteudos WHERE id = :id")
    Conteudo buscarPorId(int id);

    @Query("SELECT * FROM conteudos WHERE disciplinaId = :disciplinaId ORDER BY titulo")
    List<Conteudo> listarPorDisciplinaId(int disciplinaId);
}