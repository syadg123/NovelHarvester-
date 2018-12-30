package top.unclez.ui.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {
    public static URL getFXMLResource(String fxmlName){
        String path="/fxml/"+fxmlName;
        URL url= ResourceLoader.class.getResource(path);
        //System.out.println(url.getPath());
        return url;
    }
    public static URL getImage(String icon){
        return ResourceLoader.class.getResource("/image/"+icon);
    }

}
