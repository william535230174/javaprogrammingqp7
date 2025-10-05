import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManajemenCustomer extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("Manajemen Customer");
        VBox root = new VBox(label);
        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Manajemen Customer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
