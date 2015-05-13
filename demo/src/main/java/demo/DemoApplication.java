package demo;

import javafx.event.EventHandler;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javang.JavaNGWindow;

import java.lang.String;

public class DemoApplication {

    public static void main(String[] args) {
        String index = DemoApplication.class.getClassLoader().getResource("index.html").toString();
//        index = "http://localhost:9000/";
//        index = "http://google.com";
        new JavaNGWindow(index, 800, 600, new JavaNGWindow.StateListener() {
            @Override
            public void onStageInit(JavaNGWindow window) {
                window.getWebEngine().onAlertProperty().set(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(WebEvent<String> event) {
                        System.out.println("ALERT");
                    }
                });

                window.getWebEngine().onErrorProperty().set(new EventHandler<WebErrorEvent>() {
                    @Override
                    public void handle(WebErrorEvent event) {
                        System.err.println(event.getMessage());
                    }
                });

            }

            @Override
            public void onReady(JavaNGWindow window) {
                System.out.println("READY");
            }
        });

        System.out.println("HERE");
    }

}
