import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ManajemenCustomer extends Application {

    private TextField tfId, tfNama, tfEmail, tfTelepon;
    private Button btnTambah, btnEdit, btnHapus;
    private TableView<Customer> tableView;
    private ObservableList<Customer> data;

    @Override
    public void start(Stage primaryStage) {
        Label lblTitle = new Label("Manajemen Customer");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Form Input
        tfId = new TextField();
        tfNama = new TextField();
        tfEmail = new TextField();
        tfTelepon = new TextField();

        tfId.setPromptText("ID Customer");
        tfNama.setPromptText("Nama Customer");
        tfEmail.setPromptText("Email");
        tfTelepon.setPromptText("Nomor Telepon");

        HBox form = new HBox(10, tfId, tfNama, tfEmail, tfTelepon);
        form.setPadding(new Insets(10));

        // Tombol
        btnTambah = new Button("Tambah");
        btnEdit = new Button("Edit");
        btnHapus = new Button("Hapus");

        HBox tombol = new HBox(10, btnTambah, btnEdit, btnHapus);
        tombol.setPadding(new Insets(10));

        // === TABLEVIEW ===
        tableView = new TableView<>();
        TableColumn<Customer, String> colId = new TableColumn<>("ID");
        TableColumn<Customer, String> colNama = new TableColumn<>("Nama");
        TableColumn<Customer, String> colEmail = new TableColumn<>("Email");
        TableColumn<Customer, String> colTelepon = new TableColumn<>("Telepon");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));

        tableView.getColumns().addAll(colId, colNama, colEmail, colTelepon);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // === DATA AWAL ===
        data = FXCollections.observableArrayList(
                new Customer("C001", "Andi", "andi@gmail.com", "08123456789"),
                new Customer("C002", "Budi", "budi@gmail.com", "08234567890"),
                new Customer("C003", "Citra", "citra@gmail.com", "08345678901")
        );
        tableView.setItems(data);

        VBox root = new VBox(10, lblTitle, form, tombol, tableView);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setTitle("Manajemen Customer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// === MODEL CUSTOMER ===
class Customer {
    private String id;
    private String nama;
    private String email;
    private String telepon;

    public Customer(String id, String nama, String email, String telepon) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.telepon = telepon;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getTelepon() { return telepon; }
}
