package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;

public class FormProductActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etPrice, etCategory, etDescription, etImage;
    private Button btnSave;
    private ProgressBar progressBar;
    private ApiService apiService;
    private Product editProduct;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etTitle = findViewById(R.id.etTitle);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);
        etImage = findViewById(R.id.etImage);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);

        apiService = new ApiService();

        // Verificar si es modo edición
        if (getIntent().hasExtra("product")) {
            editProduct = (Product) getIntent().getSerializableExtra("product");
            isEditMode = true;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Editar Producto");
            }
            fillForm(editProduct);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Producto");
            }
        }

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void fillForm(Product product) {
        if (product.getTitle() != null) {
            etTitle.setText(product.getTitle());
        }
        etPrice.setText(String.valueOf(product.getPrice()));
        if (product.getCategory() != null) {
            etCategory.setText(product.getCategory());
        }
        if (product.getDescription() != null) {
            etDescription.setText(product.getDescription());
        }
        if (product.getImage() != null) {
            etImage.setText(product.getImage());
        }
    }

    private void saveProduct() {
        String title = etTitle.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String image = etImage.getText().toString().trim();

        // Validación: Title, Price y Category son requeridos
        if (title.isEmpty() || priceStr.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Complete los campos requeridos (Título, Precio, Categoría)",
                    Toast.LENGTH_LONG).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                Toast.makeText(this, "El precio debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        product.setCategory(category);
        product.setDescription(description);
        product.setImage(image);
        product.setAvailability("InStock");

        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        // Nota: La API requiere un apiKey para POST/PUT
        // Por ahora usamos null, pero deberías obtener un apiKey de https://fakestores.vercel.app
        String apiKey = null;

        if (isEditMode) {
            apiService.updateProduct(editProduct.getId(), product, apiKey,
                    new ApiService.ApiCallback<Product>() {
                        @Override
                        public void onSuccess(Product result) {
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FormProductActivity.this,
                                        "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                btnSave.setEnabled(true);
                                Toast.makeText(FormProductActivity.this,
                                        "Error al actualizar: " + error, Toast.LENGTH_LONG).show();
                            });
                        }
                    });
        } else {
            apiService.createProduct(product, apiKey, new ApiService.ApiCallback<Product>() {
                @Override
                public void onSuccess(Product result) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FormProductActivity.this,
                                "Producto creado exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnSave.setEnabled(true);

                        // Si el error es por falta de apiKey, informar al usuario
                        if (error.contains("permission") || error.contains("apiKey")) {
                            Toast.makeText(FormProductActivity.this,
                                    "Necesitas un API Key para crear productos. Obtén uno en fakestores.vercel.app",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FormProductActivity.this,
                                    "Error al crear: " + error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}