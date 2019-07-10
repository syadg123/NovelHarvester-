package top.unclez.ui.model;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import top.unclez.ui.util.BMUtil;
import top.unclez.ui.view.MAlert;
import top.unclez.utils.ObjUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MyBookMark implements Initializable {
    static List<String> data=new ArrayList<>();
    static int linenum=4;
    @FXML
    GridPane grid;
    @FXML
    ScrollPane mainPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPane();
        grid.prefHeightProperty().bind(mainPane.heightProperty());
        grid.prefWidthProperty().bind(mainPane.widthProperty());
    }
    public void initPane(){
        grid.getChildren().clear();
        data.clear();
        data=BMUtil.resetData();
        grid.getStyleClass().add("bookmark");
        int size=data.size();
        for(int i=0;i<size;i++){
                StackPane c=createPane(i%linenum,i/linenum,"/image/bookImg.jpg",data.get(i));
                GridPane.setHgrow(c, Priority.ALWAYS);
                grid.add(c,i%linenum,i/linenum);
            }
    }
    private StackPane createPane(int row,int col,String imagePath,String text){
        int index=row+col*4;
        StackPane stackPane=new StackPane();
        if((row+col)%2==0){
            stackPane.getStyleClass().add("stackpaneA");
        }else {
            stackPane.getStyleClass().add("stackpaneB");
        }
        ImageView imageView=new ImageView(imagePath);
        Label label=new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(40);
        label.setMaxHeight(50);
        label.setTextFill(Color.BLACK);
        label.setFont(new Font("Cambria",15));
        stackPane.getChildren().addAll(imageView,label);
        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                BMUtil.SetMouseEvent(event,index,data);
                if(event.getButton() == MouseButton.SECONDARY){
                    MAlert alert=new MAlert();
                    alert.setText("确定删除吗？");
                    alert.setTitle("删除 "+data.get(index));
                    alert.setMessageImg("/image/delect.jpg");
                    alert.start(new Stage());
                    if(alert.isOK()){
                        ObjUtil.delReaderConfig(data.get(index)+".conf");
                        initPane();
                        System.out.println("删除了");
                    }
                }
            }
        });
        return  stackPane;
    }
}
