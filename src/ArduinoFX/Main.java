package ArduinoFX;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ArduinoGUI.fxml"));
        primaryStage.setTitle("Arduino FX");
        primaryStage.setScene(new Scene(root, 300, 349));
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    }

