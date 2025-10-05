import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ManajemenCustomer extends Application {

    private TextField tfId, tfNama, tfEmail, tfTelepon;
    private Button btnTambah, btnEdit, btnHapus;
    private TableView<Customer> tableView;

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

        // Tabel (belum ada kolom)
        tableView = new TableView<>();

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

// Model Customer
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
