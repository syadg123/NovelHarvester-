package top.unclez.ui.util;

import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import top.unclez.bean.GlobalValue;
import top.unclez.ui.model.Book_Control;
import top.unclez.ui.model.MyBookMark;
import top.unclez.ui.view.MAlert;
import top.unclez.ui.view.Reader;
import top.unclez.utils.ObjUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class BMUtil {
    public static void SetMouseEvent(MouseEvent event, int index, List<String> data){
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
            GlobalValue.config.setNovelname(data.get(index));
            GlobalValue.readstage=new Stage();
            Reader reader=new Reader();
            reader.start(GlobalValue.readstage);
            GlobalValue.stage.close();
        }
    }
    public static List<String> resetData(){
        List<String> data=new ArrayList<>();
        File book=new File("conf/");
        File[] res=book.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname.getName().endsWith("conf"))
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
        }
        return data;
    }
}
