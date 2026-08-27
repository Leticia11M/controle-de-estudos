package br.edu.utfpr.leticiasantos.controledeestudos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "disciplinas")
public class Disciplina {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;

    private String professor;

    public Disciplina(String nome, String professor) {
        this.nome = nome;
        this.professor = professor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }



    public String toString() {
        return nome;
    }
}