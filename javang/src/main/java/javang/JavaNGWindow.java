package javang;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class JavaNGWindow {
    private static int DEFAULT_WIDTH = 800;
    private static int DEFAULT_HEIGHT = 600;

    private int mWidth;
    private int mHeight;
    private String mIndexURL;
    private Stage mStage;
    private WebEngine mWebEngine;
    private JavaNGMenu mMenu;
    private JavaNGBridge mNgBridge;

    private StateListener mStateListener;

    public JavaNGWindow(String indexURL, int width, int height, JavaNGMenu menuBar, StateListener stateListener) {
        mWidth = width;
        mHeight = height;
        mIndexURL = indexURL;
        mStateListener = stateListener;
        mMenu = menuBar;
        initialize();
    }

    public JavaNGWindow(String indexURL, int width, int height, StateListener stateListener) {
        this(indexURL, width, height, null, stateListener);
    }

    public JavaNGWindow(String indexURL) {
        this(indexURL, DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
    }

    public JavaNGWindow(String indexURL, int width, int height) {
        this(indexURL, width, height, null);
    }

    public JavaNGWindow(String indexURL, StateListener stateListener) {
        this(indexURL, DEFAULT_WIDTH, DEFAULT_HEIGHT, stateListener);
    }

    private void initialize() {
        if (JavaNGApplication.isCreated()) {
            Stage stage = new Stage();
            initStage(stage);
        } else {
            JavaNGApplication.launchApplication(new JavaNGApplication.ApplicationReadyCallback() {
                @Override
                public void onReady(JavaNGApplication application, Stage primaryStage) {
                    initStage(primaryStage);
                }
            });
        }
    }

    private void initStage(Stage stage) {
        mStage = stage;
        VBox vbox = new VBox();
        StackPane root = new StackPane();

        MenuBar menuBar = createMenu();
        if (menuBar != null) {
            vbox.getChildren().add(menuBar);
        }
        vbox.getChildren().add(root);
        WebView webView = createWebView();
        root.getChildren().add(webView);
//        webView.setPrefHeight(800);
        stage.setScene(new Scene(vbox, mWidth, mHeight));
        stage.show();

        mWebEngine = webView.getEngine();
        if (mStateListener != null) {
            mStateListener.onStageInit(this);
        }

        mWebEngine.load(mIndexURL);

        mWebEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue.equals(Worker.State.SUCCEEDED)) {

                    initBridge();
                    if (mStateListener != null) {
                        mStateListener.onReady(JavaNGWindow.this);
                    }
                }
            }
        });

//        String s =
//                "تنظیمات مدیر"
//                ;
//        mStage.setTitle(s);
    }

    private MenuItem createMenuItem(JavaNGMenuItem item) {
        if (item instanceof JavaNGMenu) {
            Menu menuItem = new Menu(item.getText());
            for (JavaNGMenuItem child : ((JavaNGMenu) item).getItems()) {
                menuItem.getItems().add(createMenuItem(child));
            }
            return menuItem;
        } else {
            MenuItem menuItem = new MenuItem(item.getText());
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    item.onClick(mWebEngine);
                }
            });
            return menuItem;
        }
    }

    private MenuBar createMenu() {
        if (mMenu == null) {
            return null;
        }

        MenuBar menuBar = new MenuBar();
        menuBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        for (JavaNGMenuItem item : mMenu.getItems()) {
            MenuItem menu = createMenuItem(item);
            menuBar.getMenus().add((Menu)menu);
        }

        return menuBar;
    }

    private WebView createWebView() {
        final WebView webView = new WebView();
        webView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.isControlDown() && event.getCode() == KeyCode.I) {
                    System.out.println("Loading firebug");
                    mWebEngine.executeScript("Firebug.chrome.toggle();");
                } else if (event.isControlDown() && event.getCode() == KeyCode.R) {
                    System.out.println("Reloading stage");
                    mWebEngine.reload();
                }
            }
        });

        webView.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

            }
        });

        return webView;
    }

    private void initBridge() {
        mNgBridge = new JavaNGBridge(mWebEngine);
    }

    public WebEngine getWebEngine() {
        return mWebEngine;
    }

    public Stage getStage() {
        return mStage;
    }

    public interface StateListener {
        void onStageInit(JavaNGWindow window);

        void onReady(JavaNGWindow window);
    }

}
