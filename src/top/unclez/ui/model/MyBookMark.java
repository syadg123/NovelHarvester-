package top.unclez.ui.model;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class BookMark2 {
    public Pane get(){
        GridPane pane= new GridPane();
        pane.setGridLinesVisible(true);
        pane.setHgap(10);
        pane.setVgap(10);
        for(int i=0;i<10;i++)
            for (int j=0;j<10;j++){
                StackPane stackPane=new StackPane();
                ImageView imageView=new ImageView("/image/bokktest.jpg");
                stackPane.getChildren().addAll(imageView,new Label("完美世界"));
                GridPane.setHgrow(stackPane, Priority.ALWAYS);
                GridPane.setVgrow(stackPane,Priority.ALWAYS);
                pane.add(stackPane,i,j);
            }
        return pane;
    }
}
