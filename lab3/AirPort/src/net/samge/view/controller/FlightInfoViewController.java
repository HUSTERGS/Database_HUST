package net.samge.view.controller;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.samge.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



/**
 * Sample Skeleton for 'FlightInfo.fxml' Controller Class
 */
public class FlightInfoViewController {

    public JFXListView<String> flightInfoList;

    public JFXDatePicker leaveDate;

    public JFXDatePicker returnDate;

    public JFXComboBox leaveCityList;

    public JFXComboBox returnCityList;

    public JFXButton searchButton;

    @FXML
    private ImageView userAvatar;
    /**
     * 用于保证登录窗口的唯一性
     */
    private Stage loginStage;
    private LoginViewController childController;

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


    private User currentUser;


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
//        userNodeList.setSpacing(10);

//        System.out.println(userNodeList.getChildren().size());
        userNodeList.setSpacing(10);


        // 点击登录按钮会弹出一个新的窗口，用于用户登录或者注册
        loginButton.setOnMouseClicked(e -> {
            // 收起列表
            userNodeList.animateList(false);
            try {
                if (this.loginStage == null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
                    Parent target = loader.load();
                    this.childController = loader.getController();
                    this.childController.setParentController(this);
                    Scene loginScene = new Scene(target);
                    loginStage = new Stage();
                    loginStage.setScene(loginScene);
                    loginStage.show();
                } else {
                    loginStage.show();
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });


        this.userNodeList.setOnMouseClicked(e -> {
//            userNodeList.isExpanded();
            System.out.println(userNodeList.isExpanded());
        });

//        this.userAvatar.focusedProperty().addListener((e, b1, b2) -> {
//            if (!b2) {
//                Event.fireEvent(userNodeList, new MouseEvent(MouseEvent.MOUSE_CLICKED,
//                        userNodeList.getLayoutX(), userNodeList.getLayoutY(),
//                        userNodeList.getLayoutX(), userNodeList.getLayoutY(), MouseButton.PRIMARY, 1,
//                true, true, true, true, true, true, true, true, true, true, null));
////                System.out.println(e);
//            }
//            System.out.println(e);
//        });

        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");
        flightInfoList.getItems().add("aaa");

        flightInfoList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView listView) {
                return new InfoCell();
            }
        });

    }


    /**
     * 用于在子组件中调用，设置登录用户
     * @param currentUser
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        // 登录后需要更改用户点击后的list
        // 去掉开始的登录以及退出按钮
        userNodeList.getChildren().remove(1,3);
        // 用户名
        userNodeList.addAnimatedNode(new Label("用户:" + currentUser.getUsername()));
        JFXButton userCenterButton = new JFXButton("个人中心");
        JFXButton logoutButton = new JFXButton("退出登录");
        userNodeList.addAnimatedNode(userCenterButton);
        userNodeList.addAnimatedNode(logoutButton);

        userCenterButton.setOnMouseClicked(e -> {

        });

        // 退出登录按钮点击事件
        logoutButton.setOnMouseClicked(e -> {
            userNodeList.getChildren().remove(1, 4);
            userNodeList.addAnimatedNode(this.loginButton);
            userNodeList.addAnimatedNode(this.exitButton);
            userNodeList.setSpacing(10);
        });

        userNodeList.setSpacing(10);
        loginStage.close();
    }


    static class InfoCell extends ListCell<String> {
        @Override
        public void updateItem(String string, boolean empty) {
            super.updateItem(string, empty);
            if (string != null) {
                FlightInfoListItem item = new FlightInfoListItem();
                setGraphic(item.getBox());
            }
        }
    }

}
