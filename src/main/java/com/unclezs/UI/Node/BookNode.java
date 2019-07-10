package com.unclezs.UI.Node;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import com.unclezs.Model.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.logging.Level;

/*
 *书架书节点
 *@author unclezs.com
 *@date 2019.06.22 09:54
 */
public class BookNode extends VBox {
    private Book book;
    private ImageView img;
    private Label label;
    public BookNode(Book book){
        this.book=book;
        //设边距
        this.setPrefWidth(120);
        this.setPrefHeight(160);
        this.setMaxWidth(120);
        this.setMaxHeight(160);
        this.setPadding(new Insets(25,25,30,40));
        //设置图片
        boolean b = book.getImg().startsWith("http");
        Image image = new Image(b?"":"file:" + book.getImg(),false);
        if(image.isError()){
            image=new Image(getClass().getResourceAsStream("/images/搜索页/没有封面.png"));
        }
        img = new ImageView(image);
        img.setFitWidth(120);
        img.setFitHeight(160);
        //设置标签
        label=new Label(book.getName());
        label.setPadding(new Insets(10,0,0,0));
        label.setFont(new Font(15));
        label.setPrefWidth(120);
        label.setAlignment(Pos.CENTER);
        //设置选中变色
        this.setOnMouseMoved(e->{
            this.setStyle("-fx-background-color: #c2c2c2");
        });
        this.setOnMouseExited(e->{
            this.setStyle("");
        });
        this.getChildren().addAll(img,label);
    }

    public Book getBook() {
        return book;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
