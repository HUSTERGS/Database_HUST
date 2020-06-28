package net.samge.view.controller;


import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.samge.dbController.PlaneInfoController;
import net.samge.model.AdminItem;

public class AdminPanelController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="main"
    private AnchorPane main; // Value injected by FXMLLoader

    @FXML // fx:id="returnButton"
    private JFXButton returnButton; // Value injected by FXMLLoader

    @FXML // fx:id="infoTable"
    private TableView<AdminItem> infoTable; // Value injected by FXMLLoader

    private PlaneDetail detailPanelController;
    private Stage detailStage;
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        returnButton.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/FlightInfo.fxml"));
                Parent target = loader.load();
                main.getChildren().setAll(target);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        ((TableColumn) infoTable.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<AdminItem, String>("pid"));
        ((TableColumn) infoTable.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<AdminItem, String>("c1"));
        ((TableColumn) infoTable.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<AdminItem, String>("c2"));
        ((TableColumn) infoTable.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<AdminItem, String>("date"));
        ((TableColumn) infoTable.getColumns().get(5)).setCellValueFactory(new PropertyValueFactory<AdminItem, String>("orderRate"));
        ((TableColumn) infoTable.getColumns().get(4)).setCellValueFactory(new PropertyValueFactory<AdminItem, String>("seatRate"));
        infoTable.getItems().addAll(PlaneInfoController.getAllInfo());

        // 当点击的时候，创建一个新的页面
        infoTable.setOnMouseClicked(e -> {
            if (this.detailPanelController != null) {
                detailStage.show();
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/planeDetail.fxml"));
            try {
                Parent target = loader.load();
                this.detailPanelController = loader.getController();
                this.detailPanelController.setPid(infoTable.getSelectionModel().getSelectedItem().getPid());
                Scene loginScene = new Scene(target);
                detailStage = new Stage();
                detailStage.setScene(loginScene);
                detailStage.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });


    }
}

