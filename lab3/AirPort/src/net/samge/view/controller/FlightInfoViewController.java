package net.samge.view.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXNodesList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



/**
 * Sample Skeleton for 'FlightInfo.fxml' Controller Class
 */
public class FlightInfoViewController {


    public JFXButton loginButton;
    public JFXButton exitButton;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="downPane"
    private AnchorPane main; // Value injected by FXMLLoader

    @FXML
    private JFXNodesList userNodeList;


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
//        userNodeList.setSpacing(10);
        Label a = new Label("hhh");
        Label b = new Label("bbbb");
//        System.out.println(userNodeList.getChildren().size());
        userNodeList.setSpacing(10);

        loginButton.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
                Parent newRoot = loader.load();
                main.getChildren().setAll(newRoot);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}
