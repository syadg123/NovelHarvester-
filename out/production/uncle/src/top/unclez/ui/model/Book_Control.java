package top.unclez.ui.model;

import com.jfoenix.controls.JFXDialog;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import top.unclez.bean.GlobalValue;
import top.unclez.ui.util.BMUtil;
import top.unclez.ui.view.MAlert;
import top.unclez.ui.view.Reader;
import top.unclez.utils.ObjUtil;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Book_Control implements Initializable {
    @FXML
    ListView<String> list;
    ObservableList<String> data= FXCollections.observableArrayList();
    List<String> listdata=new ArrayList<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = list.getSelectionModel().getSelectedIndex();
                BMUtil.SetMouseEvent(event,index,listdata);
                if(event.getButton() == MouseButton.SECONDARY){
                    MAlert alert=new MAlert();
                    alert.setText("确认删除吗？");
                    alert.setTitle("删除 "+data.get(index));
                    alert.setMessageImg("/image/delect.jpg");
                    alert.start(new Stage());
                    if(alert.isOK()){
                        ObjUtil.delReaderConfig(data.get(index)+".conf");
                        init();
                        System.out.println("删除了");
                    }
                }
            }
        });
    }
    public void init(){
        data.clear();
        listdata= BMUtil.resetData();
        for(String s:listdata)
            data.add(s);
        list.setItems(data);
    }
}
