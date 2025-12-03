package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.R;

public class MainActivity extends AppCompatActivity {
    private CardView cardProducts, cardAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardProducts = findViewById(R.id.cardProducts);
        cardAddProduct = findViewById(R.id.cardAddProduct);

        cardProducts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
            startActivity(intent);
        });

        cardAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditProductActivity.class);
            startActivity(intent);
        });
    }
}