import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import Products.*;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.LinkedHashMap;

//TODO: Login page needs a new logo. Besides that make the inventory page look better.
//TODO: When adding a new product, the user should be able to enter the SKU, name, description, category, price, and stock.
//TODO: Add edit product button, user will be able to edit a product, like change the stock or info etc.
//TODO: Make the description column bigger so that a proper description can be shown. Maybe make the Products clickable, and when you click on one it shows the full description.
public class InventoryApp extends Application {
    private Stage primaryStage;
    private Scene optionsScene;
    private Scene inventoryScene;
    private Inventory inventory;

    private ObservableList<Product> currentSearchResults;
    private TableView<Product> tableView;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Inventory Management System");

        inventory = new Inventory();
        HashMap<String, String> userDatabase = new HashMap<>();

        optionsScene = createOptionsScene();
        Scene loginScene = new LoginPage(primaryStage, optionsScene, userDatabase).createLoginScene();
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private Scene createOptionsScene() {
        VBox optionsBox = new VBox();
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setSpacing(20);

        Label welcomeLabel = new Label("Welcome to the Inventory Management System");
        welcomeLabel.getStyleClass().add("welcome-label");

        Button newInventoryButton = new Button("Create New Inventory");
        newInventoryButton.getStyleClass().add("welcome-button");
        newInventoryButton.setOnAction(e -> showNewInventoryPage());

        Button editInventoryButton = new Button("Edit Existing Inventory");
        editInventoryButton.getStyleClass().add("welcome-button");
        if (inventory.getInventories().isEmpty()) {
            editInventoryButton.setDisable(true);
        }
        editInventoryButton.setOnAction(e -> showEditInventoryPage());

        Button viewInventoryButton = new Button("View Existing Inventory");
        viewInventoryButton.getStyleClass().add("options-button");
        if (inventory.getInventories().isEmpty()) {
            viewInventoryButton.setDisable(true);
        }
        viewInventoryButton.setOnAction(e -> showViewInventoryPage());

        optionsBox.getChildren().addAll(welcomeLabel, newInventoryButton, editInventoryButton, viewInventoryButton);
        Scene scene = new Scene(optionsBox, 800, 600);
        scene.getStylesheets().add("styles.css");
        return scene;
    }

    private void showNewInventoryPage() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Inventory");
        dialog.setHeaderText("Enter the name of the new inventory");
        dialog.setContentText("Inventory Name:");

        dialog.showAndWait().ifPresent(name -> {
            inventory.createNewInventory(name);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inventory Created Successfully");
            alert.setHeaderText(null);
            alert.setContentText("Inventory '" + name + "' has been created.");
            alert.showAndWait();
            primaryStage.setScene(createOptionsScene());
        });
    }

    private void showEditInventoryPage() {
        if (inventory.getInventories().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Inventories Exist");
            alert.setContentText("Please create an inventory first.");
            alert.showAndWait();
            return;
        }
        BorderPane editInventoryPane = new BorderPane();
        VBox editInventoryBox = new VBox();
        editInventoryBox.setAlignment(Pos.CENTER);
        editInventoryBox.setSpacing(20);

        Label editInventoryLabel = new Label("Edit Existing Inventory");
        editInventoryLabel.getStyleClass().add("welcome-label");

        ListView<String> inventoryListView = new ListView<>();
        inventoryListView.setItems(FXCollections.observableArrayList(inventory.getInventories().keySet()));

        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(e -> {
            String selectedInventory = inventoryListView.getSelectionModel().getSelectedItem();
            if (selectedInventory != null) {
                showInventoryEditPage(selectedInventory);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Inventory Selected");
                alert.setContentText("Please select an inventory to edit.");
                alert.showAndWait();
            }
        });

        Button backButton = new Button("\u2190");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> primaryStage.setScene(createOptionsScene()));

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.TOP_LEFT);
        editInventoryPane.setTop(backButtonBox);

        editInventoryBox.getChildren().addAll(editInventoryLabel, inventoryListView, editButton);
        editInventoryPane.setCenter(editInventoryBox);

        Scene scene = new Scene(editInventoryPane, 800, 600);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
    }
    //this method is called by the showEditInventoryPage. it brings up the add and remove product buttons.
    private void showInventoryEditPage(String inventoryName) {
        BorderPane inventoryEditPane = new BorderPane();
        VBox inventoryEditBox = new VBox();
        inventoryEditBox.setAlignment(Pos.CENTER);
        inventoryEditBox.setSpacing(20);

        Label inventoryEditLabel = new Label("Edit Inventory: " + inventoryName);
        inventoryEditLabel.getStyleClass().add("welcome-label");

        Button addProductButton = new Button("Add Product");
        addProductButton.getStyleClass().add("edit-button");
        addProductButton.setOnAction(e -> showAddProductPage(inventoryName));

        Button removeProductButton = new Button("Remove Product");
        removeProductButton.getStyleClass().add("edit-button");
        removeProductButton.setOnAction(e -> showRemoveProductPage(inventoryName));

        Button backButton = new Button("\u2190");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> showEditInventoryPage());

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.TOP_LEFT);
        inventoryEditPane.setTop(backButtonBox);

        inventoryEditBox.getChildren().addAll(inventoryEditLabel, addProductButton, removeProductButton);
        inventoryEditPane.setCenter(inventoryEditBox);

        Scene scene = new Scene(inventoryEditPane, 800, 600);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
    }
    //method that allows a user to add a product to an inventory
    private void showAddProductPage(String inventoryName) {
        BorderPane addProductPane = new BorderPane();
        VBox addProductBox = new VBox();
        addProductBox.setAlignment(Pos.CENTER);
        addProductBox.setSpacing(20);

        Label addProductLabel = new Label("Add Product to Inventory: " + inventoryName);
        addProductLabel.getStyleClass().add("welcome-label");

        TextField skuField = new TextField();
        skuField.setPromptText("SKU");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField stockField = new TextField();
        stockField.setPromptText("Stock");

        Button addButton = new Button("Add Product");
        addButton.getStyleClass().add("edit-button");
        addButton.setOnAction(e -> {
            String sku = skuField.getText();
            String name = nameField.getText();
            String description = descriptionField.getText();
            String category = categoryField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            inventory.getInventory(inventoryName).addProduct(new Product(sku, name, description, category, price, stock));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Added Successfully");
            alert.setHeaderText(null);
            alert.setContentText("Product '" + name + "' has been added to inventory '" + inventoryName + "'.");
            alert.showAndWait();
        });

        Button backButton = new Button("\u2190");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> showInventoryEditPage(inventoryName));

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.TOP_LEFT);
        addProductPane.setTop(backButtonBox);

        addProductBox.getChildren().addAll(addProductLabel, skuField, nameField, descriptionField, categoryField, priceField, stockField, addButton);
        addProductPane.setCenter(addProductBox);

        Scene scene = new Scene(addProductPane, 800, 600);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
    }
    //method that allows the user to remove a product from the inventory
    private void showRemoveProductPage(String inventoryName) {
        BorderPane removeProductPane = new BorderPane();
        VBox removeProductBox = new VBox();
        removeProductBox.setAlignment(Pos.CENTER);
        removeProductBox.setSpacing(20);

        Label removeProductLabel = new Label("Remove Product from Inventory: " + inventoryName);
        removeProductLabel.getStyleClass().add("welcome-label");

        ListView<String> productListView = new ListView<>();
        productListView.setItems(FXCollections.observableArrayList(inventory.getInventory(inventoryName).getProducts().keySet()));

        Button removeButton = new Button("Remove Product");
        removeButton.getStyleClass().add("edit-button");
        removeButton.setOnAction(e -> {
            String selectedProduct = productListView.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                inventory.getInventory(inventoryName).removeProduct(selectedProduct);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Product Removed Successfully");
                alert.setHeaderText(null);
                alert.setContentText("Product '" + selectedProduct + "' has been removed from inventory '" + inventoryName + "'.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Product Selected");
                alert.setContentText("Please select a product to remove.");
                alert.showAndWait();
            }
        });

        Button backButton = new Button("\u2190");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> showInventoryEditPage(inventoryName));

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.TOP_LEFT);
        removeProductPane.setTop(backButtonBox);

        removeProductBox.getChildren().addAll(removeProductLabel, productListView, removeButton);
        removeProductPane.setCenter(removeProductBox);

        Scene scene = new Scene(removeProductPane, 800, 600);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
    }
    //method that allows the user to search for a product in the inventory
    private void showViewInventoryPage() {
        if (inventory.getInventories().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Inventories Exist");
            alert.setContentText("Please create an inventory first.");
            alert.showAndWait();
            return;
        }

        BorderPane viewInventoryPane = new BorderPane();
        VBox viewInventoryBox = new VBox();
        viewInventoryBox.setAlignment(Pos.CENTER);
        viewInventoryBox.setSpacing(20);

        Label viewInventoryLabel = new Label("View Inventory");
        viewInventoryLabel.getStyleClass().add("welcome-label");

        ListView<String> inventoryListView = new ListView<>();
        inventoryListView.setItems(FXCollections.observableArrayList(inventory.getInventories().keySet()));

        Button viewButton = new Button("View");
        viewButton.getStyleClass().add("view-button");
        viewButton.setOnAction(e -> {
            String selectedInventory = inventoryListView.getSelectionModel().getSelectedItem();
            if (selectedInventory != null) {
                showSearchPage(selectedInventory);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("No Inventory Selected");
                    alert.setContentText("Please select an inventory to view.");
                    alert.showAndWait();
                }
            });

        Button backButton = new Button("\u2190");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> primaryStage.setScene(createOptionsScene()));

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.TOP_LEFT);
        viewInventoryPane.setTop(backButtonBox);

        viewInventoryBox.getChildren().addAll(viewInventoryLabel, inventoryListView, viewButton);
        viewInventoryPane.setCenter(viewInventoryBox);

        Scene scene = new Scene(viewInventoryPane, 800, 600);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
        }

    private void showSearchPage(String inventoryName) {
        Inventory selectedInventory = inventory.getInventory(inventoryName);

        BorderPane searchPane = new BorderPane();
        VBox searchBox = new VBox();
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(20);

        Label searchLabel = new Label("Search Inventory: " + inventoryName);
        searchLabel.getStyleClass().add("welcome-label");

        HBox searchControls = createSearchBox(selectedInventory);
        tableView = createTableView();
        tableView.setItems(FXCollections.observableArrayList(selectedInventory.getProducts().values()));

        Button backButton = new Button("\u2190");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> showViewInventoryPage());

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.TOP_LEFT);
        searchPane.setTop(backButtonBox);

        searchBox.getChildren().addAll(searchLabel, searchControls, tableView);
        searchPane.setCenter(searchBox);

        Scene scene = new Scene(searchPane, 800, 600);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
    }


    private HBox createSearchBox(Inventory inventory) {
        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(10);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by SKU, Name, or Category");
        searchField.setPrefWidth(200);

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("search-button");

        ComboBox<String> filters = new ComboBox<>();
        filters.getItems().addAll("All", "Sort by Price Ascending", "Sort by Price Descending", "Sort by Stock Ascending", "Sort by Stock Descending");
        filters.setValue("All");

        searchBox.getChildren().addAll(searchField, searchButton, filters);
        HBox.setHgrow(searchField, Priority.NEVER);

        searchButton.setOnAction(e -> {
            String search = searchField.getText().toLowerCase();
            currentSearchResults = FXCollections.observableArrayList(performSearch(inventory, search).values());
            tableView.setItems(currentSearchResults);
            tableView.setVisible(!currentSearchResults.isEmpty());
        });

        filters.setOnAction(e -> {
            String selectedFilter = filters.getValue();
            LinkedHashMap<String, Product> filteredResults = applyFilter(inventory, selectedFilter);
            tableView.setItems(FXCollections.observableArrayList(filteredResults.values()));
            tableView.setVisible(!filteredResults.isEmpty());
        });

        return searchBox;
    }

    private TableView<Product> createTableView() {
        TableView<Product> tableView = new TableView<>();
        tableView.setVisible(false);

        TableColumn<Product, String> skuColumn = new TableColumn<>("SKU");
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tableView.getColumns().addAll(skuColumn, nameColumn, descriptionColumn, categoryColumn, priceColumn, stockColumn);
        tableView.setFixedCellSize(25);
        tableView.setMaxHeight(250);
        VBox.setVgrow(tableView, Priority.NEVER);

        return tableView;
    }

    private LinkedHashMap<String, Product> applyFilter(Inventory inventory, String selectedFilter) {
        LinkedHashMap<String, Product> filteredResults = new LinkedHashMap<>(currentSearchResults.stream().collect(LinkedHashMap::new, (map, item) -> map.put(item.getSku(), item), LinkedHashMap::putAll));

        switch (selectedFilter) {
            case "Sort by Price Ascending":
                return inventory.getProductsSortedByPriceAscending();
            case "Sort by Price Descending":
                return inventory.getProductsSortedByPriceDescending();
            case "Sort by Stock Ascending":
                return inventory.getProductsSortedByStockAscending();
            case "Sort by Stock Descending":
                return inventory.getProductsSortedByStockDescending();
            default:
                return filteredResults;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // helper method that checks to see what search the user is trying to do
    private LinkedHashMap<String, Product> performSearch(Inventory inventory, String search) {
        LinkedHashMap<String, Product> searchResults = new LinkedHashMap<>();
        if (search.isEmpty()) {
            searchResults.putAll(inventory.getProducts());
        } else {
            Product productBySku = inventory.searchProductBySku(search);
            if (productBySku != null) {
                searchResults.put(search, productBySku);
            } else {
                LinkedHashMap<String, Product> productsByCategory = inventory.searchProductsByCategory(search);
                if (!productsByCategory.isEmpty()) {
                    searchResults.putAll(productsByCategory);
                } else {
                    searchResults.putAll(inventory.searchProductsByName(search));
                }
            }
        }
        return searchResults;
    }
}