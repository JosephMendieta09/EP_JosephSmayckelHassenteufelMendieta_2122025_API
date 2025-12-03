package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.os.Handler;
import android.os.Looper;
import com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiService {
    private static final String BASE_URL = "https://fakestoreapi.com";
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public ApiService() {
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    // GET - Obtener todos los productos
    public void getAllProducts(ApiCallback<List<Product>> callback) {
        executorService.execute(() -> {
            try {
                URL url = new URL(BASE_URL + "/products");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    List<Product> products = parseProductList(response.toString());
                    mainHandler.post(() -> callback.onSuccess(products));
                } else {
                    mainHandler.post(() -> callback.onError("Error: " + responseCode));
                }
                conn.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    // GET - Obtener un producto por ID
    public void getProductById(String id, ApiCallback<Product> callback) {
        executorService.execute(() -> {
            try {
                URL url = new URL(BASE_URL + "/products/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    Product product = parseProduct(response.toString());
                    mainHandler.post(() -> callback.onSuccess(product));
                } else {
                    mainHandler.post(() -> callback.onError("Error: " + responseCode));
                }
                conn.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    // POST - Crear un nuevo producto
    public void createProduct(Product product, ApiCallback<Product> callback) {
        executorService.execute(() -> {
            try {
                URL url = new URL(BASE_URL + "/products");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("title", product.getTitle());
                jsonBody.put("price", product.getPrice());
                jsonBody.put("description", product.getDescription());
                jsonBody.put("category", product.getCategory());
                jsonBody.put("image", product.getImage());

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK ||
                        responseCode == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    Product createdProduct = parseProduct(response.toString());
                    mainHandler.post(() -> callback.onSuccess(createdProduct));
                } else {
                    mainHandler.post(() -> callback.onError("Error: " + responseCode));
                }
                conn.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    // PUT - Actualizar un producto
    public void updateProduct(String id, Product product, ApiCallback<Product> callback) {
        executorService.execute(() -> {
            try {
                URL url = new URL(BASE_URL + "/products/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("title", product.getTitle());
                jsonBody.put("price", product.getPrice());
                jsonBody.put("description", product.getDescription());
                jsonBody.put("category", product.getCategory());
                jsonBody.put("image", product.getImage());

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    Product updatedProduct = parseProduct(response.toString());
                    mainHandler.post(() -> callback.onSuccess(updatedProduct));
                } else {
                    mainHandler.post(() -> callback.onError("Error: " + responseCode));
                }
                conn.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    // DELETE - Eliminar un producto
    public void deleteProduct(String id, ApiCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                URL url = new URL(BASE_URL + "/products/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    mainHandler.post(() -> callback.onSuccess(true));
                } else {
                    mainHandler.post(() -> callback.onError("Error: " + responseCode));
                }
                conn.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    // Helper: Parsear lista de productos
    private List<Product> parseProductList(String jsonString) throws Exception {
        List<Product> products = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Product product = new Product();
            product.setId(String.valueOf(jsonObject.getInt("id")));
            product.setTitle(jsonObject.getString("title"));
            product.setPrice(jsonObject.getDouble("price"));
            product.setDescription(jsonObject.getString("description"));
            product.setCategory(jsonObject.getString("category"));
            product.setImage(jsonObject.getString("image"));
            products.add(product);
        }
        return products;
    }

    // Helper: Parsear un producto
    private Product parseProduct(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        Product product = new Product();
        product.setId(String.valueOf(jsonObject.getInt("id")));
        product.setTitle(jsonObject.getString("title"));
        product.setPrice(jsonObject.getDouble("price"));
        product.setDescription(jsonObject.getString("description"));
        product.setCategory(jsonObject.getString("category"));
        product.setImage(jsonObject.getString("image"));
        return product;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
