package com.unclezs.UI.Utils;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import sun.nio.cs.ext.MS874;

/*
 *@author unclezs.com
 *@date 2019.07.03 14:07
 */
public class AlertUtil {
    public static Alert getAlert(String title,String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(DataManager.mainStage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.getDialogPane().setStyle("-fx-graphic: url('images/1.png');");
        alert.setTitle(title);
        return alert;
    }
}
