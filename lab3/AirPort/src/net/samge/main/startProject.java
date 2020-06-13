package net.samge.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.samge.view.controller.FlightInfoViewController;

public class startProject extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/fxml/FlightInfo.fxml"));
        Parent root = loader.load();
        FlightInfoViewController controller = loader.getController();
        controller.primaryStage = primaryStage;
        primaryStage.setTitle("航班预定系统");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
