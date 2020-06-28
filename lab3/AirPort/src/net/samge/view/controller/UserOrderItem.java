package net.samge.view.controller;

/**
 * Sample Skeleton for 'userOrderItem.fxml' Controller Class
 */

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.samge.dbController.OrderController;
import net.samge.dbController.PlaneInfoController;
import net.samge.dbController.UserController;
import net.samge.model.PlaneInfo;
import net.samge.model.UserOrder;
import net.samge.utils.Toast;

public class UserOrderItem {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="hbox"
    private HBox hbox; // Value injected by FXMLLoader

    @FXML // fx:id="companyName"
    private Text companyName; // Value injected by FXMLLoader

    @FXML // fx:id="flightDate"
    private Text flightDate; // Value injected by FXMLLoader

    @FXML // fx:id="startTime"
    private Text startTime; // Value injected by FXMLLoader

    @FXML // fx:id="startCity"
    private Text startCity; // Value injected by FXMLLoader

    @FXML // fx:id="endTime"
    private Text endTime; // Value injected by FXMLLoader

    @FXML // fx:id="endCity"
    private Text endCity; // Value injected by FXMLLoader

    @FXML // fx:id="cost"
    private Text cost; // Value injected by FXMLLoader

    @FXML // fx:id="orderButton"
    private JFXButton orderButton; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

    public HBox getBox() {
        return this.hbox;
    }

    public UserOrderItem(UserOrder userOrder, UserCenterController superController) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/userOrderItem.fxml"));
        loader.setController(this);
        try {
            loader.load();
            companyName.setText(userOrder.info.getCompany());
            startTime.setText(new SimpleDateFormat("HH:mm").format(userOrder.info.getSTime()));
            endTime.setText(new SimpleDateFormat("HH:mm").format(userOrder.info.getATime()));
            cost.setText(Long.toString(userOrder.info.getCost()) + "￥");
            flightDate.setText(new SimpleDateFormat("yyyy/MM/dd").format(userOrder.info.getSTime()));
            startCity.setText(userOrder.info.getSStation());
            endCity.setText(userOrder.info.getAStation());
            if (userOrder.order.getCanceled() == 0) {
                // 如果没有取消
                orderButton.setText("退   订");
                if (userOrder.info.getATime().before(new Timestamp(System.currentTimeMillis()))) {
                    // 如果时间已经超过了当前时间,那么则不能退票
                    orderButton.setDisable(true);
                } else {
                    orderButton.setOnMouseClicked(e -> {
                        UserController.cancelOrder(userOrder.order);
                        superController.updateList(UserController.getAllOrdersOfUser(userOrder.user));
                        Toast.makeText((Stage) superController.main.getScene().getWindow(), "退订成功", 3500, 500, 500);
                    });
                }
            } else {
                orderButton.setText("已退订");
                orderButton.setDisable(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
