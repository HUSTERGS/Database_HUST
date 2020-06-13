package net.samge.view.controller;

/**
 * Sample Skeleton for 'flightInfoItem.fxml' Controller Class
 */

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.samge.dbController.OrderController;
import net.samge.dbController.PlaneInfoController;
import net.samge.model.PlaneInfo;
import net.samge.utils.Toast;

public class FlightInfoListItem {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="companyName"
    private Text companyName; // Value injected by FXMLLoader

    @FXML // fx:id="leaveTime"
    private Text leaveTime; // Value injected by FXMLLoader

    @FXML // fx:id="arriveTime"
    private Text arriveTime; // Value injected by FXMLLoader

    @FXML // fx:id="cost"
    private Text cost; // Value injected by FXMLLoader

    @FXML // fx:id="orderButton"
    private JFXButton orderButton; // Value injected by FXMLLoader

    @FXML
    private HBox hbox;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

    public HBox getBox() {
        return this.hbox;
    }

    public FlightInfoListItem(PlaneInfo info, FlightInfoViewController superController, Stage superStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/flightInfoItem.fxml"));
        loader.setController(this);

        try {
            loader.load();
            companyName.setText(info.getCompany());
            leaveTime.setText(new SimpleDateFormat("HH:mm").format(info.getSTime()));
            arriveTime.setText(new SimpleDateFormat("HH:mm").format(info.getATime()));
            cost.setText(Long.toString(info.getCost()));

            if (PlaneInfoController.isPlaneFull(info)) {
                // 如果航班已经满了
                orderButton.setDisable(true);
                orderButton.setText("人数已满");
            } else {
                orderButton.setOnMouseClicked(e -> {
                    // 如果被点击
                    if (superController.currentUser == null) {
                        // 如果主页面并没有登录，那么就进行登录操作
                        superController.loginButton.fire();
                        System.out.println("未登录用户尝试订购");
                        Toast.makeText(superStage, "请先进行登录", 3500, 500, 500);
                    } else {
                        // 如果主页已经登陆了，那么就进行相应的插入操作，登记用户订购机票情况
//                        Toast.makeText();
                        if (OrderController.order(superController.currentUser.getUid(), info.getPid())) {
                            Toast.makeText(superStage, "订购成功", 3500, 500, 500);
                            superController.updateList(PlaneInfoController.getPlaneInfos(
                                    DateTimeFormatter.ofPattern("yyyy/MM/dd").format(superController.leaveDate.getValue()),
                                    superController.leaveCityList.getValue().toString(),
                                    superController.returnCityList.getValue().toString()));
                        } else {

                        }

                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
