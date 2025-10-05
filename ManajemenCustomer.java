import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;

public class ManajemenCustomer extends Application {

    private TextField tfId, tfNama, tfEmail, tfTelepon;
    private Button btnTambah, btnEdit, btnHapus, btnSimpan;
    private TableView<Customer> tableView;
    private ObservableList<Customer> data;
    private final String FILE_NAME = "customers.csv";

    @Override
    public void start(Stage primaryStage) {
        Label lblTitle = new Label("📋 Manajemen Customer");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

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

        btnTambah = new Button("Tambah");
        btnEdit = new Button("Edit");
        btnHapus = new Button("Hapus");
        btnSimpan = new Button("Simpan ke File");

        HBox tombol = new HBox(10, btnTambah, btnEdit, btnHapus, btnSimpan);
        tombol.setPadding(new Insets(10));

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

        data = FXCollections.observableArrayList();
        bacaDariFile();
        tableView.setItems(data);

        btnTambah.setOnAction(e -> {
            if (tfId.getText().isEmpty() || tfNama.getText().isEmpty() ||
                tfEmail.getText().isEmpty() || tfTelepon.getText().isEmpty()) {
                showAlert("Error", "Semua kolom harus diisi!");
                return;
            }

            Customer c = new Customer(tfId.getText(), tfNama.getText(), tfEmail.getText(), tfTelepon.getText());
            data.add(c);
            clearForm();
        });

        tableView.setOnMouseClicked(e -> {
            Customer selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tfId.setText(selected.getId());
                tfNama.setText(selected.getNama());
                tfEmail.setText(selected.getEmail());
                tfTelepon.setText(selected.getTelepon());
            }
        });

        btnEdit.setOnAction(e -> {
            Customer selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Pilih data yang ingin diedit!");
                return;
            }

            selected.setId(tfId.getText());
            selected.setNama(tfNama.getText());
            selected.setEmail(tfEmail.getText());
            selected.setTelepon(tfTelepon.getText());

            tableView.refresh();
            clearForm();
        });

        btnHapus.setOnAction(e -> {
            Customer selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Pilih data yang ingin dihapus!");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi Hapus");
            confirm.setHeaderText(null);
            confirm.setContentText("Apakah kamu yakin ingin menghapus data " + selected.getNama() + "?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    data.remove(selected);
                    clearForm();
                }
            });
        });

        btnSimpan.setOnAction(e -> simpanKeFile());

        VBox root = new VBox(10, lblTitle, form, tombol, tableView);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 800, 450);
        primaryStage.setTitle("Manajemen Customer - Dengan Penyimpanan File");
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
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
            showAlert("Sukses", "Data berhasil disimpan ke file!");
        } catch (IOException e) {
            showAlert("Error", "Gagal menyimpan data ke file!");
        }
    }

    private void bacaDariFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4) {
                    data.add(new Customer(p[0], p[1], p[2], p[3]));
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Gagal membaca data dari file!");
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
