package top.unclez.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import top.unclez.Utils.FileUtil;
import top.unclez.bean.GlobalValue;
import top.unclez.ui.view.BookMark;
import top.unclez.ui.view.Reader;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Book_Control implements Initializable {
    @FXML
    ListView<String> list;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> data= FXCollections.observableArrayList();
        File book=new File("bookRead/");
        File[] res=book.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname.getName().endsWith("index"))
                  return true;
                return false;
            }
        });
        if(res!=null){
            for (File f:res){
                data.add(f.getName().split("\\.")[0]);
            }
        }
        if(data.size()==0){
            data.add("还没有书，先去去搜索阅读吧");
            list.setItems(data);
            return;
        }
        list.setItems(data);
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                    int index = list.getSelectionModel().getSelectedIndex();
                    GlobalValue.config.setNovelname(data.get(index));
                    GlobalValue.readstage=new Stage();
                    Reader reader=new Reader();
                    reader.start(GlobalValue.readstage);
                    GlobalValue.bookstage.close();
                    GlobalValue.stage.close();
                }
            }
        });
    }
}
