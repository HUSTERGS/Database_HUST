package net.samge.view.controller;

/**
 * Sample Skeleton for 'UserCenter.fxml' Controller Class
 */


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXNodesList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.samge.dbController.OrderController;
import net.samge.dbController.PlaneInfoController;
import net.samge.dbController.UserController;
import net.samge.model.*;
import net.samge.utils.Toast;

public class UserCenterController {

    // 当前用户
    public User currentUser;
    public Text currentUserText;
    public AnchorPane main;
    public JFXListView<Notification> notificationList;


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="orderedList"
    private JFXListView<String> orderedList; // Value injected by FXMLLoader

    @FXML // fx:id="notifications"
    private Tab notifications; // Value injected by FXMLLoader

    @FXML // fx:id="userNodeList"
    private JFXNodesList userNodeList; // Value injected by FXMLLoader

    @FXML // fx:id="userAvatar"
    private ImageView userAvatar; // Value injected by FXMLLoader

    @FXML // fx:id="changeAvatar"
    private JFXButton changeAvatar; // Value injected by FXMLLoader

    @FXML // fx:id="logoutButton"
    private JFXButton logoutButton; // Value injected by FXMLLoader

    @FXML // fx:id="returnButton"
    private JFXButton returnButton; // Value injected by FXMLLoader

    private Stage noticeStage;
    private NotificationPanelController noticeController;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        returnButton.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/FlightInfo.fxml"));
                Parent newRoot = loader.load();
                FlightInfoViewController controller = loader.getController();
                controller.primaryStage = (Stage) main.getScene().getWindow();;
                controller.setCurrentUser(currentUser);
                main.getChildren().setAll(newRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // 添加已订航班列表
        orderedList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView listView) {
                return new UserCenterController.InfoCell();
            }
        });

        logoutButton.setOnMouseClicked(event->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/FlightInfo.fxml"));
                Parent newRoot = loader.load();
                FlightInfoViewController controller = loader.getController();
                controller.primaryStage = (Stage) main.getScene().getWindow();;
//                controller.setCurrentUser(currentUser);
                main.getChildren().setAll(newRoot);
                Toast.makeText(controller.primaryStage, "退出成功", 3500, 500, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // 用户点击时,弹出通知窗口

        notificationList.setOnMouseClicked(e -> {
            Notification notice = notificationList.getSelectionModel().getSelectedItem();
            try {
                if (this.noticeStage == null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/NotificationPanel.fxml"));
                    Parent target = loader.load();
                    this.noticeController = loader.getController();
                    this.noticeController.setParentController(this);
                    this.noticeController.setNotice(notice);
                    this.noticeController.getReady();
                    Scene loginScene = new Scene(target);
                    this.noticeStage = new Stage();
                    this.noticeStage.setScene(loginScene);
                    this.noticeStage.show();
                } else {
                    noticeStage.show();
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        currentUserText.setText("用户 " + user.getUsername());
//        updateList(UserController.getAllOrdersOfUser(currentUser));
    }

    public void UserComfirm() {
        // 关闭页面
        noticeStage.close();
        updateNoticeList();
    }

    static class InfoCell extends ListCell<String> {
//        public static Stage primaryStage;
        public static ArrayList<UserOrder> list;
        public static UserCenterController superController;
        @Override
        public void updateItem(String string, boolean empty) {
            super.updateItem(string, empty);
            if (string != null) {
                UserOrderItem item = new UserOrderItem(list.get(Integer.parseInt(string)), superController);
                setGraphic(item.getBox());
            }
        }
    }


    public void updateList(ArrayList<UserOrder> list) {
//        UserCenterController.InfoCell.primaryStage = (Stage) main.getScene().getWindow();
        UserCenterController.InfoCell.list = list;
        UserCenterController.InfoCell.superController = this;
        orderedList.getItems().clear();
        for (int i = 0; i < UserCenterController.InfoCell.list.size(); i++) {
            orderedList.getItems().add(Integer.toString(i));
        }
    }

    public void updateNoticeList() {
        ArrayList<Notification> notices = UserController.getAllNotifications(currentUser);
        notificationList.getItems().clear();
        for (Notification notice : notices) {
            Order order = OrderController.getOrder(notice.getOid());
            PlaneInfo planeInfo = PlaneInfoController.getPlaneInfo(order.getPid());
            notice.setInfo(planeInfo);
            notice.setOrder(order);
            notificationList.getItems().add(notice);
        }
    }
}
