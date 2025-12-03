package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.R;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.ApiService;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.Product;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditProductActivity extends AppCompatActivity {
    private TextInputEditText editTextName, editTextPrice, editTextCategory,
            editTextDescription, editTextImageUrl;
    private Button btnSave;
    private ApiService apiService;
    private boolean isEditMode = false;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        btnSave = findViewById(R.id.btnSave);

        apiService = new ApiService();

        // Verificar si es modo edición
        if (getIntent().hasExtra("PRODUCT_ID")) {
            isEditMode = true;
            productId = getIntent().getStringExtra("PRODUCT_ID");
            loadProductData();

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.edit_product_title);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.add_product_title);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void loadProductData() {
        editTextName.setText(getIntent().getStringExtra("PRODUCT_TITLE"));
        editTextPrice.setText(String.valueOf(getIntent().getDoubleExtra("PRODUCT_PRICE", 0)));
        editTextCategory.setText(getIntent().getStringExtra("PRODUCT_CATEGORY"));
        editTextDescription.setText(getIntent().getStringExtra("PRODUCT_DESCRIPTION"));
        editTextImageUrl.setText(getIntent().getStringExtra("PRODUCT_IMAGE"));
    }

    private void saveProduct() {
        String name = editTextName.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String imageUrl = editTextImageUrl.getText().toString().trim();

        // Validaciones
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("El nombre es requerido");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(priceStr)) {
            editTextPrice.setError("El precio es requerido");
            editTextPrice.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(category)) {
            editTextCategory.setError("La categoría es requerida");
            editTextCategory.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            editTextPrice.setError("Precio inválido");
            editTextPrice.requestFocus();
            return;
        }

        // Si no hay imagen, usar una por defecto
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = "https://via.placeholder.com/300";
        }

        Product product = new Product();
        product.setTitle(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setDescription(description);
        product.setImage(imageUrl);

        if (isEditMode) {
            updateProduct(product);
        } else {
            createProduct(product);
        }
    }

    private void createProduct(Product product) {
        btnSave.setEnabled(false);

        apiService.createProduct(product, new ApiService.ApiCallback<Product>() {
            @Override
            public void onSuccess(Product result) {
                btnSave.setEnabled(true);
                Toast.makeText(AddEditProductActivity.this,
                        "Producto creado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error) {
                btnSave.setEnabled(true);
                Toast.makeText(AddEditProductActivity.this,
                        "Error al crear el producto: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduct(Product product) {
        btnSave.setEnabled(false);

        apiService.updateProduct(productId, product, new ApiService.ApiCallback<Product>() {
            @Override
            public void onSuccess(Product result) {
                btnSave.setEnabled(true);
                Toast.makeText(AddEditProductActivity.this,
                        "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error) {
                btnSave.setEnabled(true);
                Toast.makeText(AddEditProductActivity.this,
                        "Error al actualizar el producto: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (apiService != null) {
            apiService.shutdown();
        }
    }
}
