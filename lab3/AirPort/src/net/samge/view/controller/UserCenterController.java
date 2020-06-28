package net.samge.view.controller;



import com.jfoenix.controls.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.samge.dbController.OrderController;
import net.samge.dbController.PlaneInfoController;
import net.samge.dbController.UserController;
import net.samge.model.*;
import net.samge.utils.IdcardValidatorUtil;
import net.samge.utils.Toast;

public class UserCenterController {

    // 当前用户
    public User currentUser;
    public Text currentUserText;
    public AnchorPane main;
    @FXML
    private JFXListView<Notification> notificationList;
    public Text totalPrice;
    public TableView<PriceItem> priceTable;
    public JFXTextField phoneNumberTextField;
    public JFXTextField IDCardNumTextField;
    public JFXTextField userNameTextFiled;
    public JFXButton modifyButton;
    public JFXButton resetButton;
    public JFXPasswordField passwordTextField;
    public JFXTextField emailTextField;


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

    public Stage noticeStage;
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
            Notification notice = notificationList.getItems().get(notificationList.getSelectionModel().getSelectedIndex());
            System.out.println(notice);
            try {
                if (this.noticeStage == null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/NotificationPanel.fxml"));
                    Parent target = loader.load();
                    this.noticeController = loader.getController();
                    this.noticeController.setParentController(this);
                    this.noticeController.setNotice(notice);
                    System.out.println("设置后" + this.noticeController.notice);
                    this.noticeController.getReady();
                    System.out.println("getReady后" + this.noticeController.notice);
                    Scene loginScene = new Scene(target);
                    this.noticeStage = new Stage();
                    this.noticeStage.setScene(loginScene);
                    this.noticeStage.show();
                    this.noticeStage = null;
                } else {
                    noticeStage.show();
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        // 邮箱正则
        RegexValidator emailValidator = new RegexValidator();
        emailValidator.setRegexPattern("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        emailValidator.setMessage("邮箱格式不正确!");
        emailTextField.getValidators().add(emailValidator);

        emailTextField.focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            if (!t1) {
                emailTextField.validate();
            }
        }));

        // 手机号正则
        RegexValidator phoneNumberValidator = new RegexValidator();
        phoneNumberValidator.setRegexPattern("^(\\+86)?1[356789]\\d{9}$");
        phoneNumberValidator.setMessage("手机号格式不正确!");
        phoneNumberTextField.getValidators().add(phoneNumberValidator);

        phoneNumberTextField.focusedProperty().addListener(((observableValue, aBoolean, t1) -> {
            if (!t1) {
                phoneNumberTextField.validate();
            }
        }));
        // 重置按钮
        resetButton.setOnMouseClicked(e -> {
            setCurrentUser(currentUser);
        });


        modifyButton.setOnMouseClicked(e -> {
            // 手机号不为空，但是不通过检验，直接退出
            if (!phoneNumberTextField.getText().trim().isEmpty() && !phoneNumberTextField.validate()) {
                return;
            }
            // 邮箱不为空，但是不通过检验，则直接退出
            if (!emailTextField.getText().trim().isEmpty() && !emailTextField.validate()) {
                return ;
            }
            // 身份证不为空，但是不通过检验，直接退出
            if (!IDCardNumTextField.getText().trim().isEmpty() && !IdcardValidatorUtil.isValidatedAllIdcard(IDCardNumTextField.getText())) {
                return ;
            }

            currentUser.setEmail(emailTextField.getText());
            currentUser.setUsername(userNameTextFiled.getText());
            currentUser.setPhoneNum(phoneNumberTextField.getText());
            currentUser.setPassword(passwordTextField.getText());
            currentUser.setIdCardNum(IDCardNumTextField.getText());

            if (!UserController.updateUserInfo(currentUser)) {
                Toast.makeText((Stage) main.getScene().getWindow(), "更新信息失败", 3500, 500, 500);
            } else {
                Toast.makeText((Stage) main.getScene().getWindow(), "更新信息成功", 3500, 500, 500);
            }
            setCurrentUser(UserController.getUser(currentUser.getUid()));
        });
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        currentUserText.setText("用户 " + user.getUsername());
        userNameTextFiled.setText(user.getUsername());
        phoneNumberTextField.setText(user.getPhoneNum());
        IDCardNumTextField.setText(user.getIdCardNum());
        emailTextField.setText(user.getEmail());
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

    public void updateTable() {
        ((TableColumn) priceTable.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<PriceItem, String>("pid"));
        ((TableColumn) priceTable.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<PriceItem, String>("c1"));
        ((TableColumn) priceTable.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<PriceItem, String>("c2"));
        ((TableColumn) priceTable.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<PriceItem, String>("date"));
        ((TableColumn) priceTable.getColumns().get(4)).setCellValueFactory(new PropertyValueFactory<PriceItem, String>("cost"));
        priceTable.getItems().addAll(OrderController.getAllBillingRecord(currentUser.getUid()));
        long temp = 0;
        for (PriceItem item : priceTable.getItems()) {
            temp += item.getCost();
        }
        totalPrice.setText("总价格" + temp);
    }
}
