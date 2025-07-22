package Products;

public class ProductByFoot extends Product {
    private double length;

    public ProductByFoot(String sku, String name, String description, String category, double price, double stock) {
        super(sku, name, description, category, price, (int) stock);
        this.length = stock;
    }

    public double getLength() {
        return length;
    }

    @Override
    public String toString(){
        return String.format("Product: %s\nSKU: %s\nDescription: %s\nPrice: $%.2f\nStock (feet): %.2f", getName(), getSku(), getDescription(), getPrice(), length);
    }
}
