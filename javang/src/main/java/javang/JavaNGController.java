package javang;


import netscape.javascript.JSObject;

public class JavaNGController {
    protected JSObject scope;

    public void setScope(JSObject scope) {
        this.scope = scope;
    }

    public JSObject getScope() {
        return scope;
    }
}
