package br.edu.utfpr.leticiasantos.controledeestudos;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(
        tableName = "conteudos",
        foreignKeys = @ForeignKey(
                entity = Disciplina.class,
                parentColumns = "id",
                childColumns = "disciplinaId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("disciplinaId")}
)
public class Conteudo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titulo;
    private String disciplina;
    private String dificuldade;
    private String status;
    private String observacao;
    private boolean revisar;
    private LocalDate dataEstudo;
    private int disciplinaId;

    public Conteudo(String titulo,
                    String disciplina,
                    String dificuldade,
                    String status,
                    String observacao,
                    boolean revisar,
                    LocalDate dataEstudo,
                    int disciplinaId) {

        this.titulo = titulo;
        this.disciplina = disciplina;
        this.dificuldade = dificuldade;
        this.status = status;
        this.observacao = observacao;
        this.revisar = revisar;
        this.dataEstudo = dataEstudo;
        this.disciplinaId = disciplinaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }


    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }


    public boolean isRevisar() {
        return revisar;
    }

    public void setRevisar(boolean revisar) {
        this.revisar = revisar;
    }


    public LocalDate getDataEstudo() {
        return dataEstudo;
    }

    public void setDataEstudo(LocalDate dataEstudo) {
        this.dataEstudo = dataEstudo;
    }


    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }
}