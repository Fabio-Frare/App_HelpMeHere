package br.com.udesc.helpmehere;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {

    // Decalrando as variáveis
    DrawerLayout drawerLayout;
    ImageView btMenu;
    RecyclerView reciclerView;
    static ArrayList<String> arrayList = new ArrayList<>();
    MainAdapter adapter;
    Button btBombeiros;
    ImageView imgFoto;
    String sFoto;
    TextView txtBase64;

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificando se o usuário permitiu o uso da câmera
        verificarPermissaoCamera();

        // Inicializando as variáveis
        drawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.bt_menu);
        reciclerView = findViewById(R.id.recycler_view);
        btBombeiros = findViewById(R.id.btBombeiros);
        imgFoto = findViewById(R.id.idFoto);
        txtBase64 = findViewById(R.id.txtBase64);

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

        btBombeiros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    // Verificando se o usuário permitiu o uso da câmera
    private void verificarPermissaoCamera() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new  String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    private void tirarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    // Pegando o arquivo da foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap foto = (Bitmap) data.getExtras().get("data");
            imgFoto.setImageBitmap(foto);
            tranformarFotoBase64(foto);
        }
    }

    private void tranformarFotoBase64(Bitmap foto) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        sFoto = Base64.getEncoder().encodeToString(bytes);
        txtBase64.setText(sFoto);
//        System.out.println("==============================================");
//        System.out.println(sFoto);
//        System.out.println("==================================================");
    }



}