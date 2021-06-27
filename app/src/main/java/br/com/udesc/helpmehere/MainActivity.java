package br.com.udesc.helpmehere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Decalrando as variáveis
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView reciclerView;
    static ArrayList<String> arrayList = new ArrayList<>();
    MainAdapter adapter;
    Button btBombeiros;

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializando as variáveis
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.bt_menu);
        reciclerView = findViewById(R.id.recycler_view);
        btBombeiros = findViewById(R.id.btBombeiros);

        // Limpando a lista
        arrayList.clear();

        // Adicionando os itens de menu na lista
        arrayList.add("Home");
        arrayList.add("Cadastro");
        arrayList.add("Orientações");
        arrayList.add("Saiba mais");
        arrayList.add("Sair");

        // Inicializando Adapter
        adapter = new MainAdapter(this, arrayList);
        reciclerView.setLayoutManager(new LinearLayoutManager(this));

        reciclerView.setAdapter(adapter);

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
        closeDrawer(drawerLayout);
    }
}