package br.com.udesc.helpmehere;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UsuarioDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;

    public UsuarioDAO(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public long inserir(Usuario usuario) {
        try {

            ContentValues values = new ContentValues();
            values.clear();
            values.put("nome", usuario.getNome());
            values.put("telefone", usuario.getTelefone());
            values.put("cidade", usuario.getCidade());
            long idUsuario =  banco.insert("usuario", null, values);
            return  idUsuario;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if( banco != null) {
                banco.close();
            }
        }
        return 0;
    }

    public String buscarUsuario() {
        SQLiteDatabase banco = null;
        Cursor cursor;
        String nome = null;
        String query = "SELECT nome FROM usuario WHERE id = 1";

        try {
            banco = this.conexao.getReadableDatabase();
            cursor = banco.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            Log.i("Banco", "Encontrou op primeiro registro");
            Usuario usuario = new Usuario();
            usuario.setNome(cursor.getString(0));
            nome = usuario.getNome();
        }

        }catch (Exception e) {
            e.printStackTrace();
            Log.i("Erro", "erro na consulta do usuário.");
        }finally {
            if( banco != null) {
                banco.close();
            }
        }
        return nome;
    }

}
