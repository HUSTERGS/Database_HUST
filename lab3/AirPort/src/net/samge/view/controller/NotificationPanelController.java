package net.samge.view.controller;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import net.samge.dbController.NotificationController;
import net.samge.model.Notification;

public class NotificationPanelController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="main"
    private AnchorPane main; // Value injected by FXMLLoader

    @FXML // fx:id="notificationText"
    private TextArea notificationText; // Value injected by FXMLLoader

    @FXML // fx:id="comfirmButton"
    private JFXButton comfirmButton; // Value injected by FXMLLoader

    public Notification notice;

    public UserCenterController superController;
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

    public void setNotice(Notification notice) {
        this.notice = notice;
    }

    public void setParentController(UserCenterController controller) {
        this.superController = controller;
    }

    public void getReady() {
        this.notificationText.setText(
                notice.getInfo().getSStation() + "--------" + notice.getInfo().getAStation() + "\n\n" +
                "起飞时间: \n" + notice.getInfo().getSTime() + "\n\n" +
                "预计到达时间: \n" + notice.getInfo().getATime() + "\n\n" +
                "费用: " + notice.getInfo().getCost());
        if (notice.getOrder().getCanceled() != 0 || notice.getReceived() != 0) {
            // 如果已经取消了或者已经缴费,则禁用按钮
            comfirmButton.setDisable(true);
            comfirmButton.setText("缴费成功");
        } else {
            comfirmButton.setOnMouseClicked(e -> {
                // 进行插入以及账单处理
                NotificationController.noticeUser(this.notice);
                superController.UserComfirm();
            });
        }
    }
}
