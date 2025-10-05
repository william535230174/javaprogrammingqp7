import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;

public class ManajemenCustomer extends Application {

    private TextField tfId, tfNama, tfEmail, tfTelepon, tfCari;
    private Button btnTambah, btnEdit, btnHapus, btnSimpan;
    private TableView<Customer> tableView;
    private ObservableList<Customer> data;
    private FilteredList<Customer> filteredData;
    private final String FILE_NAME = "customers.csv";

    @Override
    public void start(Stage primaryStage) {
        Label lblTitle = new Label("📋 Manajemen Customer");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        tfId = new TextField();
        tfNama = new TextField();
        tfEmail = new TextField();
        tfTelepon = new TextField();
        tfCari = new TextField();

        tfId.setPromptText("ID");
        tfNama.setPromptText("Nama");
        tfEmail.setPromptText("Email");
        tfTelepon.setPromptText("Telepon");
        tfCari.setPromptText("Cari nama/email...");

        HBox form = new HBox(10, tfId, tfNama, tfEmail, tfTelepon);
        form.setPadding(new Insets(10));

        btnTambah = new Button("Tambah");
        btnEdit = new Button("Edit");
        btnHapus = new Button("Hapus");
        btnSimpan = new Button("Simpan ke File");

        HBox tombol = new HBox(10, btnTambah, btnEdit, btnHapus, btnSimpan);
        tombol.setPadding(new Insets(10));

        TableColumn<Customer, String> colId = new TableColumn<>("ID");
        TableColumn<Customer, String> colNama = new TableColumn<>("Nama");
        TableColumn<Customer, String> colEmail = new TableColumn<>("Email");
        TableColumn<Customer, String> colTelepon = new TableColumn<>("Telepon");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));

        tableView = new TableView<>();
        tableView.getColumns().addAll(colId, colNama, colEmail, colTelepon);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        data = FXCollections.observableArrayList();
        bacaDariFile();
        filteredData = new FilteredList<>(data, p -> true);
        tableView.setItems(filteredData);

        tfCari.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(customer -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return customer.getNama().toLowerCase().contains(lower)
                        || customer.getEmail().toLowerCase().contains(lower);
            });
        });

        btnTambah.setOnAction(e -> {
            if (tfId.getText().isEmpty() || tfNama.getText().isEmpty()
                    || tfEmail.getText().isEmpty() || tfTelepon.getText().isEmpty()) {
                showAlert("Error", "Semua kolom harus diisi!");
                return;
            }
            data.add(new Customer(tfId.getText(), tfNama.getText(), tfEmail.getText(), tfTelepon.getText()));
            clearForm();
        });

        tableView.setOnMouseClicked(e -> {
            Customer c = tableView.getSelectionModel().getSelectedItem();
            if (c != null) {
                tfId.setText(c.getId());
                tfNama.setText(c.getNama());
                tfEmail.setText(c.getEmail());
                tfTelepon.setText(c.getTelepon());
            }
        });

        btnEdit.setOnAction(e -> {
            Customer c = tableView.getSelectionModel().getSelectedItem();
            if (c == null) {
                showAlert("Error", "Pilih data yang ingin diedit!");
                return;
            }
            c.setId(tfId.getText());
            c.setNama(tfNama.getText());
            c.setEmail(tfEmail.getText());
            c.setTelepon(tfTelepon.getText());
            tableView.refresh();
            clearForm();
        });

        btnHapus.setOnAction(e -> {
            Customer c = tableView.getSelectionModel().getSelectedItem();
            if (c == null) {
                showAlert("Error", "Pilih data yang ingin dihapus!");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi");
            confirm.setHeaderText(null);
            confirm.setContentText("Hapus " + c.getNama() + "?");
            confirm.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    data.remove(c);
                    clearForm();
                }
            });
        });

        btnSimpan.setOnAction(e -> simpanKeFile());

        VBox root = new VBox(10, lblTitle, form, tombol, tfCari, tableView);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 800, 480);
        primaryStage.setTitle("Manajemen Customer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void clearForm() {
        tfId.clear();
        tfNama.clear();
        tfEmail.clear();
        tfTelepon.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void simpanKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Customer c : data) {
                writer.write(c.getId() + "," + c.getNama() + "," + c.getEmail() + "," + c.getTelepon());
                writer.newLine();
            }
            showAlert("Sukses", "Data berhasil disimpan!");
        } catch (IOException e) {
            showAlert("Error", "Gagal menyimpan data.");
        }
    }

    private void bacaDariFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4)
                    data.add(new Customer(p[0], p[1], p[2], p[3]));
            }
        } catch (IOException e) {
            showAlert("Error", "Gagal membaca data.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

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

    public void setId(String id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setEmail(String email) { this.email = email; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
}
