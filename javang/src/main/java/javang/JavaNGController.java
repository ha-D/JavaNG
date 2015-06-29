package javang;


import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class JavaNGController {
    protected JSObject scope;
    protected WebEngine mWebEngine;

    public void setScope(JSObject scope) {
        this.scope = scope;
    }

    public JSObject getScope() {
        return scope;
    }

    public void setWebEngine(WebEngine webEngine) {
        mWebEngine = webEngine;
    }

    protected void changePage(String page) {
        mWebEngine.executeScript("changePage('" + page + "')");
    }
}
