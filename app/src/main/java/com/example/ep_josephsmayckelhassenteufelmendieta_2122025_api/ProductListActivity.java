package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private static final String TAG = "ProductListActivity";
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ProgressBar progressBar;
    private List<Product> productList;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Productos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        apiService = new ApiService();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, FormProductActivity.class);
            startActivity(intent);
        });

        // Cargar productos
        loadProducts();
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        Log.d(TAG, "Loading products...");

        apiService.getProducts(new ApiService.ApiCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Products loaded: " + products.size());
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    productList.clear();
                    productList.addAll(products);
                    adapter.notifyDataSetChanged();

                    if (products.isEmpty()) {
                        Toast.makeText(ProductListActivity.this,
                                "No hay productos disponibles", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Error loading products: " + error);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(ProductListActivity.this,
                            "Error al cargar productos: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Product product) {
        Intent intent = new Intent(this, FormProductActivity.class);
        intent.putExtra("product", product);
        intent.putExtra("mode", "edit");
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Está seguro de eliminar este producto?")
                .setPositiveButton("Eliminar", (dialog, which) -> deleteProduct(product))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteProduct(Product product) {
        progressBar.setVisibility(View.VISIBLE);

        // Nota: La API requiere un apiKey para DELETE
        // Por ahora usamos null, pero deberías obtener un apiKey de https://fakestores.vercel.app
        apiService.deleteProduct(product.getId(), null, new ApiService.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProductListActivity.this,
                            "Producto eliminado", Toast.LENGTH_SHORT).show();
                    loadProducts();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProductListActivity.this,
                            "Error al eliminar: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}