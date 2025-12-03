package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnProducts = findViewById(R.id.btnProducts);
        Button btnAbout = findViewById(R.id.btnAbout);
        Button btnExit = findViewById(R.id.btnExit);

        btnProducts.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ProductListActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            // Mostrar información de la app
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Acerca de FakeStores");
            builder.setMessage("FakeStores CRUD App\n\nVersión 1.0\n\nAplicación de gestión de productos usando la API FakeStores.\n\nDesarrollado con Java y Android Studio.");
            builder.setPositiveButton("OK", null);
            builder.show();
        });

        btnExit.setOnClickListener(v -> finish());
    }
}