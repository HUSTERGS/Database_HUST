/**
 * Sample Skeleton for 'planeDetail.fxml' Controller Class
 */

package net.samge.view.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.samge.model.AdminItem;
import net.samge.model.PlaneOrderInfoItem;

public class PlaneDetail {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="table"
    private TableView<PlaneOrderInfoItem> table; // Value injected by FXMLLoader

    private AdminPanelController superController;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        table.getItems().add(new PlaneOrderInfoItem(
                "default",
                "420281200010020032",
                "1",
                1
        ));
        table.getItems().add(new PlaneOrderInfoItem(
                "Samuel",
                "420281200111020047",
                "2",
                0
        ));
        ((TableColumn) table.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<PlaneOrderInfoItem, String>("userName"));
        ((TableColumn) table.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<PlaneOrderInfoItem, String>("ID"));
        ((TableColumn) table.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<PlaneOrderInfoItem, String>("seatNo"));
        ((TableColumn) table.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<PlaneOrderInfoItem, String>("payed"));
    }

    public void setSuperController(AdminPanelController controller) {
        superController =  controller;
    }

    public void setPid(long pid) {

    }
}
