package net.samge.view.controller;

/**
 * Sample Skeleton for 'sample.fxml' Controller Class
 */

/**
 * Sample Skeleton for 'Login.fxml' Controller Class
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.validation.RegexValidator;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class LoginViewController {

    public Pane container;

    @FXML
    public JFXSpinner spinner;

    @FXML
    private AnchorPane main;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="loginButton"
    private JFXButton loginButton; // Value injected by FXMLLoader

    @FXML // fx:id="email"
    private JFXTextField email; // Value injected by FXMLLoader

    @FXML // fx:id="password"
    private JFXPasswordField password; // Value injected by FXMLLoader

    @FXML // fx:id="forgetPassword"
    private Text forgetPassword; // Value injected by FXMLLoader

    @FXML // fx:id="registerButton"
    private JFXButton registerButton; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        // 注册按钮点击事件,页面滑动效果
        registerButton.setOnMouseClicked(event -> {
            email.setDisable(true);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Register.fxml"));
                Parent newRoot = loader.load();
                RegisterViewController controller = loader.getController();
                Pane view2 = controller.container;
                Pane view1 = container;
                AnchorPane root = main;
                root.getChildren().add(newRoot);
                double width = root.getWidth();
                KeyFrame start = new KeyFrame(Duration.ZERO,
                        new KeyValue(view2.translateXProperty(), width),
                        new KeyValue(view1.translateXProperty(), 0));
                KeyFrame end = new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(view2.translateXProperty(), 0),
                        new KeyValue(view1.translateXProperty(), -width));
                Timeline slide = new Timeline(start, end);
                slide.setOnFinished(e -> root.getChildren().remove(view1));
                slide.play();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // 邮箱输入格式检查
        RegexValidator validator = new RegexValidator();
        validator.setRegexPattern("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        validator.setMessage("邮箱格式不正确!");
        email.getValidators().add(validator);
        email.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    email.validate();
                }
            }
        });

        // 当邮箱或者密码为空的时候,禁用登录按钮
        loginButton.disableProperty().bind(email.textProperty().isNotEmpty().and(password.textProperty().isNotEmpty()).not());


        loginButton.setOnMouseClicked(e -> {

        });

    }

}
