package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.R;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.ProductAdapter;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.ApiService;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ProgressBar progressBar;
    private TextView errorText;
    private ApiService apiService;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.product_list_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList, this::onProductClick);
        recyclerView.setAdapter(adapter);

        apiService = new ApiService();
        loadProducts();
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        apiService.getAllProducts(new ApiService.ApiCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                productList.clear();
                productList.addAll(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Error: " + error);
                Toast.makeText(ProductListActivity.this,
                        "Error al cargar productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", product.getId());
        startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}