package Products;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class Inventory {
    private Map<String, Inventory> inventories = new HashMap<>();
    private String name;
    private LinkedHashMap<String, Product> products;

    public Inventory() {
        this.products = new LinkedHashMap<>();
    }

    public Inventory(String name) {
        this.name = name;
        this.products = new LinkedHashMap<>();
    }

    public void createNewInventory(String name) {
        inventories.put(name, new Inventory(name));
    }

    public Inventory getInventory(String name) {
        return inventories.get(name);
    }

    public Map<String, Inventory> getInventories() {
        return inventories;
    }

    public void addProduct(Product product) {
        if (products.containsKey(product.getSku())) {
            Product existingProduct = products.get(product.getSku());
            existingProduct.setStock(existingProduct.getStock() + product.getStock());
        } else {
            products.put(product.getSku(), product);
        }
    }

    public void addProductByFoot(ProductByFoot product) {
        products.put(product.getSku(), product);
    }

    public void removeProduct(String sku) {
        products.remove(sku);
    }

    public Product getProduct(String sku) {
        return products.get(sku);
    }

    public LinkedHashMap<String,Product> getProducts() {
        return products;
    }

    public LinkedHashMap<String, Product> getProductsSortedByPriceAscending() {
        return products.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingDouble(Product::getPrice)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public LinkedHashMap<String, Product> getProductsSortedByPriceDescending() {
        return products.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingDouble(Product::getPrice).reversed()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public LinkedHashMap<String, Product> getProductsSortedByStockAscending() {
        return products.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Product::getStock)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public LinkedHashMap<String, Product> getProductsSortedByStockDescending() {
        return products.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Product::getStock).reversed()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    //Using Apache Commons Text library(Levenshtein Distance) to calculate the similarity between the product name and the search query
    public LinkedHashMap<String, Product> searchProductsByName(String name) {
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        return products.values().stream()
                .filter(product -> levenshtein.apply(product.getName().toLowerCase(), name.toLowerCase()) <= 2)
                .sorted(Comparator.comparingInt(product -> levenshtein.apply(product.getName().toLowerCase(), name.toLowerCase())))
                .collect(Collectors.toMap(
                        Product::getSku,
                        product -> product,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    //also using apache commons text to correct if categories are misspelled while searching.
    public LinkedHashMap<String, Product> searchProductsByCategory(String category) {
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        return products.values().stream()
                .filter(product -> levenshtein.apply(product.getCategory().toLowerCase(), category.toLowerCase()) <= 2)
                .sorted(Comparator.comparingInt(product -> levenshtein.apply(product.getCategory().toLowerCase(), category.toLowerCase())))
                .collect(Collectors.toMap(
                        Product::getSku,
                        product -> product,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Product searchProductBySku(String sku) {
        return products.get(sku);
    }

    public void printInventory() {
        for (Product product : products.values()) {
            System.out.println(product);
        }
    }
}
