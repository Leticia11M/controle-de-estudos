package br.edu.utfpr.leticiasantos.controledeestudos;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class Conversores {

    @TypeConverter
    public static String localDateParaString(LocalDate data) {

        if (data == null) {
            return null;
        }

        return data.toString();
    }

    @TypeConverter
    public static LocalDate stringParaLocalDate(String valor) {

        if (valor == null) {
            return null;
        }

        return LocalDate.parse(valor);
    }
}