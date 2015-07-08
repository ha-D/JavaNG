package javang;

import javafx.scene.web.WebEngine;

import java.util.ArrayList;
import java.util.List;

public class JavaNGMenuItem {
    private JavaNGMenuHandler mClickCallback;
    private String mText;

    public JavaNGMenuItem(String text, JavaNGMenuHandler callback) {
        mText = text;
        mClickCallback = callback;
    }

    public JavaNGMenuItem(String text) {
        this(text, null);
    }

    public void setOnClickCallback(JavaNGMenuHandler callback) {
        this.mClickCallback = callback;
    }

    public void onClick(WebEngine webEngine) {
        if (mClickCallback != null) {
            mClickCallback.setWebEngine(webEngine);
            mClickCallback.onMenuClick();
        }
    }

    public String getText() {
        return mText;
    }

    public void setText(String text){
        mText = text;
    }
}
