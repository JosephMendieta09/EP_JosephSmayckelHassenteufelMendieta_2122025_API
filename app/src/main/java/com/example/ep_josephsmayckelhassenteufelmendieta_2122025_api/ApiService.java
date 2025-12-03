package com.example.ep_josephsmayckelhassenteufelmendieta_2122025_api;

import android.util.Log;
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
    private static final String BASE_URL = "https://fakestores.onrender.com/api";
    private static final String TAG = "ApiService";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public void getProducts(ApiCallback<List<Product>> callback) {
        executor.execute(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(BASE_URL + "/products");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(50000);
                conn.setReadTimeout(50000);

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "GET Products - Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    Log.d(TAG, "Response: " + response.toString());
                    List<Product> products = parseProducts(response.toString());
                    callback.onSuccess(products);
                } else {
                    callback.onError("Error code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting products", e);
                callback.onError(e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        });
    }

    public void createProduct(Product product, String apiKey, ApiCallback<Product> callback) {
        executor.execute(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(BASE_URL + "/products");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                if (apiKey != null && !apiKey.isEmpty()) {
                    conn.setRequestProperty("apiKey", apiKey);
                }
                conn.setDoOutput(true);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                JSONObject jsonProduct = new JSONObject();
                jsonProduct.put("title", product.getTitle());
                jsonProduct.put("price", product.getPrice());
                jsonProduct.put("category", product.getCategory());
                if (product.getDescription() != null && !product.getDescription().isEmpty()) {
                    jsonProduct.put("description", product.getDescription());
                }
                if (product.getImage() != null && !product.getImage().isEmpty()) {
                    jsonProduct.put("image", product.getImage());
                }
                if (product.getAvailability() != null) {
                    jsonProduct.put("availability", product.getAvailability());
                }

                Log.d(TAG, "Creating product: " + jsonProduct.toString());

                OutputStream os = conn.getOutputStream();
                os.write(jsonProduct.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "POST Product - Response Code: " + responseCode);

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

                    Product newProduct = parseProduct(new JSONObject(response.toString()));
                    callback.onSuccess(newProduct);
                } else {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getErrorStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    Log.e(TAG, "Error response: " + response.toString());
                    callback.onError("Error code: " + responseCode + " - " + response.toString());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error creating product", e);
                callback.onError(e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        });
    }

    public void updateProduct(String id, Product product, String apiKey, ApiCallback<Product> callback) {
        executor.execute(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(BASE_URL + "/products/" + id);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                if (apiKey != null && !apiKey.isEmpty()) {
                    conn.setRequestProperty("apiKey", apiKey);
                }
                conn.setDoOutput(true);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                JSONObject jsonProduct = new JSONObject();
                jsonProduct.put("title", product.getTitle());
                jsonProduct.put("price", product.getPrice());
                jsonProduct.put("category", product.getCategory());
                if (product.getDescription() != null && !product.getDescription().isEmpty()) {
                    jsonProduct.put("description", product.getDescription());
                }
                if (product.getImage() != null && !product.getImage().isEmpty()) {
                    jsonProduct.put("image", product.getImage());
                }
                if (product.getAvailability() != null) {
                    jsonProduct.put("availability", product.getAvailability());
                }

                OutputStream os = conn.getOutputStream();
                os.write(jsonProduct.toString().getBytes("UTF-8"));
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

                    Product updatedProduct = parseProduct(new JSONObject(response.toString()));
                    callback.onSuccess(updatedProduct);
                } else {
                    callback.onError("Error code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error updating product", e);
                callback.onError(e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        });
    }

    public void deleteProduct(String id, String apiKey, ApiCallback<Void> callback) {
        executor.execute(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(BASE_URL + "/products/" + id);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Accept", "application/json");
                if (apiKey != null && !apiKey.isEmpty()) {
                    conn.setRequestProperty("apiKey", apiKey);
                }
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK ||
                        responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Error code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error deleting product", e);
                callback.onError(e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        });
    }

    private List<Product> parseProducts(String json) throws Exception {
        List<Product> products = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            products.add(parseProduct(jsonObject));
        }

        return products;
    }

    private Product parseProduct(JSONObject json) throws Exception {
        Product product = new Product();

        if (json.has("id")) {
            product.setId(json.getString("id"));
        }
        if (json.has("title")) {
            product.setTitle(json.getString("title"));
        }
        if (json.has("price")) {
            product.setPrice(json.getDouble("price"));
        }
        if (json.has("description")) {
            product.setDescription(json.getString("description"));
        }
        if (json.has("category")) {
            product.setCategory(json.getString("category"));
        }
        if (json.has("image")) {
            product.setImage(json.getString("image"));
        }
        if (json.has("availability")) {
            product.setAvailability(json.getString("availability"));
        }

        return product;
    }
}