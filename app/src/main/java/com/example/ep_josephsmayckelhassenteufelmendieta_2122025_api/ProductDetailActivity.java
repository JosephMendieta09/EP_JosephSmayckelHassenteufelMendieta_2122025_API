package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.R;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.ApiService;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.Product;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName, productCategory, productPrice, productDescription;
    private Button btnEdit, btnDelete;
    private ApiService apiService;
    private Product currentProduct;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.product_detail_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        productImage = findViewById(R.id.detailProductImage);
        productName = findViewById(R.id.detailProductName);
        productCategory = findViewById(R.id.detailProductCategory);
        productPrice = findViewById(R.id.detailProductPrice);
        productDescription = findViewById(R.id.detailProductDescription);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        apiService = new ApiService();
        productId = getIntent().getStringExtra("PRODUCT_ID");

        loadProductDetails();

        btnEdit.setOnClickListener(v -> editProduct());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadProductDetails() {
        apiService.getProductById(productId, new ApiService.ApiCallback<Product>() {
            @Override
            public void onSuccess(Product result) {
                currentProduct = result;
                displayProduct(result);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ProductDetailActivity.this,
                        "Error al cargar el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProduct(Product product) {
        productName.setText(product.getTitle());
        productCategory.setText(product.getCategory());
        productPrice.setText(String.format("$%.2f", product.getPrice()));
        productDescription.setText(product.getDescription());

        Glide.with(this)
                .load(product.getImage())
                .placeholder(R.color.divider)
                .error(R.color.divider)
                .into(productImage);
    }

    private void editProduct() {
        Intent intent = new Intent(this, AddEditProductActivity.class);
        intent.putExtra("PRODUCT_ID", currentProduct.getId());
        intent.putExtra("PRODUCT_TITLE", currentProduct.getTitle());
        intent.putExtra("PRODUCT_PRICE", currentProduct.getPrice());
        intent.putExtra("PRODUCT_CATEGORY", currentProduct.getCategory());
        intent.putExtra("PRODUCT_DESCRIPTION", currentProduct.getDescription());
        intent.putExtra("PRODUCT_IMAGE", currentProduct.getImage());
        startActivity(intent);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este producto?")
                .setPositiveButton("Eliminar", (dialog, which) -> deleteProduct())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteProduct() {
        apiService.deleteProduct(productId, new ApiService.ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Toast.makeText(ProductDetailActivity.this,
                        "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ProductDetailActivity.this,
                        "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
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