package br.com.udesc.helpmehere;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UsuarioDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;

    public UsuarioDAO(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public long inserir(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.clear();
        values.put("nome", usuario.getNome());
        values.put("telefone", usuario.getTelefone());
        values.put("cidade", usuario.getCidade());
        long result =  banco.insert("usuario", null, values);
        return  result;
    }

}
