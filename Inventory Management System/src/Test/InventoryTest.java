package Test;

import org.junit.jupiter.api.Test;
import Products.*;

import java.util.LinkedHashMap;

public class InventoryTest {
    @Test
    public void testInventory_SearchByName_Misspelled() {
        System.out.println("Testing Inventory Search by name with misspelled name");
        Inventory inventory = generateInventory();

        LinkedHashMap<String, Product> test = inventory.searchProductsByName("Mircowave");

        System.out.println("Search Word: Mircowave");
        System.out.print("Results: ");

        test.forEach((sku, product) -> System.out.println(product));
        assert test.size() == 1;
        assert test.containsKey("1234");
        assert test.containsValue(inventory.getProduct("1234"));
    }

    @Test
    public void testInventory_SearchByCategory() {
        System.out.println("Testing Inventory Search by Category");
        Inventory inventory = generateInventory();

        LinkedHashMap<String, Product> test = inventory.searchProductsByCategory("Appliance");

        System.out.println("Search Word: Appliance");
        System.out.print("Results: ");

        test.forEach((sku, product) -> System.out.println(product));
        assert test.size() == 3;
        assert test.containsKey("1234");
        assert test.containsKey("5678");
        assert test.containsKey("91011");
    }


    public Inventory generateInventory() {
        Inventory inventory = new Inventory();
        inventory.addProduct(new Product("1234", "Microwave", "This is a Microwave", "Appliance", 10.00, 100));
        inventory.addProduct(new Product("5678", "Refrigerator", "This is a Refrigerator", "Appliance", 10.00, 200));
        inventory.addProduct(new Product("91011", "Washing Machine", "This is a Washing Machine", "Appliance", 10.00, 300));
        return inventory;
    }

}
