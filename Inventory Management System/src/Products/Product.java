package Products;

public class Product {

    //TODO: add a parameter for top sellers
    //TODO: also add a parameter to update stock when it gets sold? Maybe.

    private String sku;
    private String name;
    private String description;
    private String category;
    private double price;
    private int stock;

    public Product(String sku, String name, String description, String category, double price, int stock) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int newStock) {
        this.stock = newStock;
    }

    @Override
    public String toString(){
        return String.format("Product: %s\nSKU: %s\nDescription: %s\nCategory: %s\nPrice: $%.2f\nStock: %d\n", name, sku, description, category, price, stock);
    }

}