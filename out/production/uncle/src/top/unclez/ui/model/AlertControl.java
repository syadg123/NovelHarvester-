package top.unclez.ui.model;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertControl implements Initializable {
    @FXML
    JFXButton ok;
    @FXML
    JFXButton no;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
