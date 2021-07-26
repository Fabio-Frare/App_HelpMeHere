package br.com.udesc.helpmehere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Cadastro extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView reciclerView;
    private EditText edtTextNome, edtTextTelefone, edtTextCidade;
    private UsuarioDAO dao;
    private Button btRegistrarCadastro;
    public TextView textNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.bt_menu);
        reciclerView = findViewById(R.id.recycler_view);

        // campos de cadastro de usuário
        edtTextNome = findViewById(R.id.edtTextNome);
        edtTextTelefone = findViewById(R.id.edtTextTelefone);
        edtTextCidade = findViewById(R.id.edtTextCidade);
        btRegistrarCadastro = findViewById(R.id.btRegistrarCadastro);
        textNome = findViewById(R.id.textNome);
        dao = new UsuarioDAO(this);

        reciclerView.setLayoutManager( new LinearLayoutManager(this));
        reciclerView.setAdapter(new MainAdapter(this, MainActivity.arrayList));

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

       

    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }

    public void registrar(View view) {
        Usuario usuario = new Usuario();
        usuario.setNome(edtTextNome.getText().toString());
        usuario.setTelefone(edtTextTelefone.getText().toString());
        usuario.setCidade(edtTextCidade.getText().toString());
//        Log.d("Nome: ", usuario.getNome());
        Long idUsuario = dao.inserir(usuario);
        usuario.setId(idUsuario);
        atualizarUsuario(usuario);
//        Log.d("Id: ", Integer.toString(usuario.getId()));
        Toast.makeText(this, "Usuário "+ usuario.getId() + " cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
    }

    public void atualizarUsuario(Usuario usuario) {
        // buscar usuário via banco sqlite
        String nome = dao.buscarUsuario();
        Log.i("Nome (cadastro): ", nome);
        textNome.setText(nome);
        voltarMainActivity();
    }

    public void voltarMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}