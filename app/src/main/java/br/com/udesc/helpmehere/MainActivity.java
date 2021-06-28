package br.com.udesc.helpmehere;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

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
    FusedLocationProviderClient localClient;

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
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
        localClient = LocationServices.getFusedLocationProviderClient(this);

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

    @Override
    protected void onResume() {
        super.onResume();
        // Verifica se o usuario tem e se está atualizado o Google Plasy Services
        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        switch (errorCode) {
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_DISABLED:
                Log.d("Teste", "Google Play Services não instalado ou desatualizado.");
                GoogleApiAvailability.getInstance().getErrorDialog(this, errorCode, 0, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).show();
                break;
            case ConnectionResult.SUCCESS:
                Log.d("Teste", "Google Play Services está atualizado.");
                break;
        }
        // verifica se o usuário deu permissão para localização.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        localClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    Log.i("Location", location.getLatitude() + " " + location.getLongitude());
                } else {
                    Log.i("Location", "A localização está nula.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Falha ao encontrar a localização.");
            }
        });
        // Buscar localização mais precisa
        LocationRequest locationRequest = LocationRequest.create();
        // controlar gasto de bateria
        locationRequest.setInterval(2 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                        locationSettingsResponse.getLocationSettingsStates().isGpsPresent();
                        Log.i("Teste", locationSettingsResponse
                                .getLocationSettingsStates()
                                .isNetworkLocationPresent() + "");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        if(e instanceof ResolvableApiException) {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            try {
                                resolvable.startResolutionForResult(MainActivity.this, 10);
                            } catch (IntentSender.SendIntentException e1) {
                            }
                        }
                    }
                });
        // Atualizar localização com frequencia definida
        final LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null) {
                    Log.i("Teste2", "Local é nullo");
                    return;
                }
                for (Location location: locationResult.getLocations()) {
                    Log.i("Teste2", location.getLatitude() + "");
                }
            }
            @Override
            public void onLocationAvailability( LocationAvailability locationAvailability) {
                Log.i("Teste", locationAvailability.isLocationAvailable() + "");
            }
        };
        localClient.requestLocationUpdates(locationRequest, locationCallback, null);
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
//        txtBase64.setText(sFoto);
//        System.out.println("==============================================");
//        System.out.println(sFoto);
//        System.out.println("==================================================");
    }



}