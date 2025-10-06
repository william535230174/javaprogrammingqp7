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

    private Stage primaryStage;
    private ObservableList <Customer> customerData;
    private TableView <Customer> tableCustomer;

    public static void main (String [] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) {
        this.primaryStage = primaryStage;
        customerData = FXCollections.observableArrayList();
        showLoginPage();
    }

    private void showLoginPage() {
        Label lblUser = new Label ("Username:");
        TextField tfUser = new TextField();
        Label lblPass = new Label ("Password:");
        PasswordField tfPass = new PasswordField();
        Button btnLogin = new Button ("Login");
        Label lblMsg = new Label();

        VBox loginPane = new VBox (10, lblUser, tfUser, lblPass, tfPass, btnLogin, lblMsg);
        loginPane.setPadding (new Insets(20));

        btnLogin.setOnAction (_ -> {
            if (tfUser.getText().equals("admin") && tfPass.getText().equals("123")) {
                showMenuUtama();
            } else {
                lblMsg.setText("Login gagal!");
                lblMsg.setStyle("-fx-text-fill:red;");
            }
        });

        primaryStage.setScene(new Scene(loginPane, 300, 200));
        primaryStage.setTitle("Login Aplikasi Gudang");
        primaryStage.show();
    }

    private void showMenuUtama() {
        Label lblTitle = new Label("Menu Utama");
        Button btnBarang = new Button("Manajemen Barang");
        Button btnSupplier = new Button("Manajemen Supplier");
        Button btnCustomer = new Button("Manajemen Customer");

        btnBarang.setOnAction(_ -> new Alert(Alert.AlertType.INFORMATION, "Manajemen Tidak Tersedia!").showAndWait()); 
        
        btnSupplier.setOnAction (_ -> new Alert (Alert.AlertType.INFORMATION, "Manajemen Tidak Tersedia!").showAndWait()); 

        btnCustomer.setOnAction (_ -> showManajemenCustomer()); 

        VBox vbox = new VBox(15, lblTitle, btnBarang, btnSupplier, btnCustomer);
        vbox.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(vbox, 400, 250));
        primaryStage.setTitle("Menu Utama");
        primaryStage.show();
    }

    @SuppressWarnings("unchecked")
    private void showManajemenCustomer() {
        Label lblTitle = new Label("Manajemen Customer");

        tableCustomer = new TableView<>();
        TableColumn<Customer, String> colId = new TableColumn<>("Id");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Customer, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Customer, String> colTelepon = new TableColumn<>("Telepon");
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("telepon"));

        tableCustomer.getColumns().addAll(colId, colNama, colEmail, colTelepon);
        tableCustomer.setItems(customerData);

        Button btnTambah = new Button("Tambah Customer");
        Button btnEdit = new Button("Edit Customer");
        Button btnHapus = new Button("Hapus Customer");

        btnTambah.setOnAction(_ -> showTambahCustomerWindow());
        btnEdit.setOnAction(_ -> editCustomer());
        btnHapus.setOnAction(_ -> hapusCustomer());

        HBox aksi = new HBox(10, btnTambah, btnEdit, btnHapus);
        VBox vbox = new VBox(10, lblTitle, tableCustomer, aksi);
        vbox.setPadding(new Insets(15));

        primaryStage.setScene(new Scene(vbox, 600, 400));
        primaryStage.setTitle("Manajemen Customer");
        primaryStage.show();
    }

    private void showTambahCustomerWindow() {
        Stage stage = new Stage();
        TextField tfId = new TextField();
        tfId.setPromptText("ID Customer");

        TextField tfNama = new TextField();
        tfNama.setPromptText("Nama Customer");

        TextField tfEmail = new TextField();
        tfEmail.setPromptText("Email");

        TextField tfTelepon = new TextField();
        tfTelepon.setPromptText("Nomor Telepon");

        Button btnSimpan = new Button("Simpan");
        Label lblMsg = new Label();

        btnSimpan.setOnAction(_ -> {
            String id = tfId.getText().trim();
            String nama = tfNama.getText().trim();
            String email = tfEmail.getText().trim();
            String telp = tfTelepon.getText().trim();

            if (id.isEmpty() || nama.isEmpty() || email.isEmpty()) {
                lblMsg.setText("Mandatory field harus diisi");
                lblMsg.setStyle("-fx-text-fill:red;");
                return;
            }

            if (!email.contains("@")) {
                lblMsg.setText("Email tidak valid");
                lblMsg.setStyle("-fx-text-fill:red;");
                return;
            }

            for (Customer c : customerData) {
                if (c.getId().equals(id)) {
                    lblMsg.setText("ID customer sudah ada");
                    lblMsg.setStyle("-fx-text-fill:red;");
                    return;
                }
            }

            customerData.add(new Customer(id, nama, email, telp));
            lblMsg.setText("Customer ditambahkan");
            lblMsg.setStyle("-fx-text-fill:green;");
            stage.close();
        });

        VBox vbox = new VBox(10, tfId, tfNama, tfEmail, tfTelepon, btnSimpan, lblMsg);
        vbox.setPadding(new Insets(15));
        stage.setScene(new Scene(vbox, 300, 250));
        stage.setTitle("Tambah Customer");
        stage.show();
    }

    private void editCustomer() {
        Customer selected = tableCustomer.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Pilih data untuk diedit!").showAndWait();
            return;
        }

        TextField tfNama = new TextField(selected.getNama());
        TextField tfEmail = new TextField(selected.getEmail());
        TextField tfTelepon = new TextField(selected.getTelepon());

        tfNama.setPromptText("Nama");
        tfEmail.setPromptText("Email");
        tfTelepon.setPromptText("Telepon");
        Button btnUpdate = new Button("Update");

        Stage stage = new Stage();
        btnUpdate.setOnAction(_ -> {
            if (tfNama.getText().isEmpty() || tfEmail.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Mandatory field harus diisi").showAndWait();
                return;
            }
            if (!tfEmail.getText().contains("@")) {
                new Alert(Alert.AlertType.ERROR, "Email tidak valid").showAndWait();
                return;
            }
            selected.setNama(tfNama.getText());
            selected.setEmail(tfEmail.getText());
            selected.setTelepon(tfTelepon.getText());
            tableCustomer.refresh();
            stage.close();
        });

        VBox vbox = new VBox(10, new Label("ID: " + selected.getId()), tfNama, tfEmail, tfTelepon, btnUpdate);

        vbox.setPadding(new Insets(15));
        stage.setScene(new Scene(vbox, 300, 220));
        stage.setTitle("Edit Customer");
        stage.show();
    }

    private void hapusCustomer() {
        Customer selected = tableCustomer.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Pilih data untuk dihapus!").showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin hapus data?", ButtonType.YES, ButtonType.NO);

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                customerData.remove(selected);
            }
        });
    }

    public static class Customer {
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
        public void setNama(String nama) { this.nama = nama; }
        public void setEmail(String email) { this.email = email; }
        public void setTelepon(String telepon) { this.telepon = telepon; }
    }
}
