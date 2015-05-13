package javang;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaNGApplication extends Application {
    private static JavaNGApplication sInstance;
    private static ApplicationReadyCallback sReadyCallback;

    public static boolean isCreated() {
        return sInstance != null;
    }

    public static JavaNGApplication getInstance() {
        return sInstance;
    }

    public static void launchApplication(ApplicationReadyCallback callback) {
        sReadyCallback = callback;
        launch(JavaNGApplication.class);
    }

    public JavaNGApplication() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        sInstance = this;
        if (sReadyCallback != null) {
            sReadyCallback.onReady(this, primaryStage);
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public interface ApplicationReadyCallback {
        void onReady(JavaNGApplication application, Stage primaryStage);
    }
}
