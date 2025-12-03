package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalle del Producto");
        }

        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            ImageView imageView = findViewById(R.id.detailImage);
            TextView tvTitle = findViewById(R.id.detailTitle);
            TextView tvPrice = findViewById(R.id.detailPrice);
            TextView tvCategory = findViewById(R.id.detailCategory);
            TextView tvDescription = findViewById(R.id.detailDescription);
            TextView tvAvailability = findViewById(R.id.detailAvailability);

            tvTitle.setText(product.getTitle() != null ? product.getTitle() : "Sin título");
            tvPrice.setText(String.format(Locale.US, "$%.2f", product.getPrice()));
            tvCategory.setText(product.getCategory() != null ? product.getCategory() : "Sin categoría");
            tvDescription.setText(product.getDescription() != null ? product.getDescription() : "Sin descripción");

            String availability = product.getAvailability() != null ? product.getAvailability() : "InStock";
            tvAvailability.setText("Estado: " + availability);

            // Cargar imagen con Glide
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                Glide.with(this)
                        .load(product.getImage())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .centerCrop()
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.placeholder_image);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}