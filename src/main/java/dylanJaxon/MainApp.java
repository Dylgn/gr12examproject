package dylanJaxon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class MainApp extends Application {
    private static Stage stage;
    public static player you = new player();

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        stage = s;
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image("icon.png"));
        setRoot("menu", "");
    }

    static void setRoot(String fxml) throws IOException {
        setRoot(fxml, stage.getTitle());
    }

    static void setRoot(String fxml, String title) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setSize(int height, int width) {
        stage.setHeight(height);
        stage.setWidth(width);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
