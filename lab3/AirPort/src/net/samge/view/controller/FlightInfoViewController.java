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
import javafx.stage.Stage;
import javafx.util.Callback;
import net.samge.dbController.PlaneInfoController;
import net.samge.dbController.UserController;
import net.samge.model.PlaneInfo;
import net.samge.model.User;
import net.samge.utils.Toast;

import javax.sound.sampled.Line;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Sample Skeleton for 'FlightInfo.fxml' Controller Class
 */
public class FlightInfoViewController {

    public JFXListView<String> flightInfoList;

    public JFXDatePicker leaveDate;

    public Stage primaryStage;

    public JFXComboBox<String> leaveCityList;

    public JFXComboBox<String> returnCityList;

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


    public User currentUser;


    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
//        userNodeList.setSpacing(10);

//        System.out.println(userNodeList.getChildren().size());
        userNodeList.setSpacing(10);

        leaveCityList.getItems().addAll(PlaneInfoController.getAllLeavingCities());
        returnCityList.getItems().addAll(PlaneInfoController.getAllArrivingCities());


        // 尝试加入联想搜索功能
//        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
//        autoCompletePopup.getSuggestions().addAll(leaveCityList.getItems());
//
//        autoCompletePopup.setSelectionHandler(event -> {
//            leaveCityList.setValue(event.getObject());
//        });
//

        // 点击登录按钮会弹出一个新的窗口，用于用户登录或者注册
        loginButton.setOnAction(e -> {
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
//            System.out.println(userNodeList.isExpanded());
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


        // 设置航班表
        flightInfoList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView listView) {
                return new InfoCell();
            }
        });


        // 当没有选择日期和城市的时候禁用搜索按钮
        searchButton.disableProperty().bind((leaveDate.valueProperty().isNotNull()
                .and(leaveCityList.valueProperty().isNotNull())
                .and(returnCityList.valueProperty().isNotNull())).not());

        searchButton.setOnMouseClicked(e -> {
//            System.out.println(leaveDate.valueProperty().isNotNull());
//            System.out.println(leaveCityList.selectionModelProperty().isNotNull());
//            System.out.println(returnCityList.selectionModelProperty().isNotNull());
            updateList(PlaneInfoController.getPlaneInfos(
                    DateTimeFormatter.ofPattern("yyyy/MM/dd").format(leaveDate.getValue()),
                    leaveCityList.getValue().toString(),
                    returnCityList.getValue().toString()));
        });

//        updateList(PlaneInfoController.getPlaneInfos("2020/06/11", "上海", "武汉"));
    }


    /**
     * 用于在登录子组件中调用，设置登录用户
     *
     * @param currentUser
     */
    public void setCurrentUser(User currentUser) {
        if (loginStage != null) {
            Toast.makeText(primaryStage, "登录成功", 3500, 500, 500);
        }
        this.currentUser = currentUser;
        // 登录后需要更改用户点击后的list
        // 去掉开始的登录以及退出按钮
        userNodeList.getChildren().remove(1, 3);
        // 用户名
        userNodeList.addAnimatedNode(new Label("用户:" + currentUser.getUsername()));
        JFXButton userCenterButton = new JFXButton("个人中心");
        JFXButton logoutButton = new JFXButton("退出登录");
        userNodeList.addAnimatedNode(userCenterButton);
        userNodeList.addAnimatedNode(logoutButton);

        userCenterButton.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UserCenter.fxml"));
                Parent newRoot = loader.load();
                UserCenterController controller = loader.getController();
                controller.setCurrentUser(currentUser);
                controller.updateList(UserController.getAllOrdersOfUser(currentUser));
                controller.updateNoticeList();
                Stage stage = (Stage) main.getScene().getWindow();
                stage.setScene(new Scene(newRoot));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // 退出登录按钮点击事件
        logoutButton.setOnAction(e -> {
            userNodeList.getChildren().remove(1, 4);
            userNodeList.addAnimatedNode(this.loginButton);
            userNodeList.addAnimatedNode(this.exitButton);
            userNodeList.setSpacing(10);
            this.currentUser = null;
        });

        userNodeList.setSpacing(10);
        if (loginStage != null) {
            loginStage.close();
        }

    }



    static class InfoCell extends ListCell<String> {
        public static Stage primaryStage;
        public static ArrayList<PlaneInfo> list;
        public static FlightInfoViewController superController;
        @Override
        public void updateItem(String string, boolean empty) {
            super.updateItem(string, empty);
            if (string != null) {
                FlightInfoListItem item = new FlightInfoListItem(list.get(Integer.parseInt(string)), superController, primaryStage);
                setGraphic(item.getBox());
            }
        }
    }

    public void updateList(ArrayList<PlaneInfo> list) {
        InfoCell.primaryStage = primaryStage;
        InfoCell.list = list;
        InfoCell.superController = this;
        flightInfoList.getItems().clear();
        for (int i = 0; i < InfoCell.list.size(); i++) {
            flightInfoList.getItems().add(Integer.toString(i));
        }
    }


}
