package javang;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JavaNGBridge {
    private WebEngine mWebEngine;
    private JSObject mWindow;

    public JavaNGBridge(WebEngine webEngine) {
        mWebEngine = webEngine;
        initialize();
    }

    private void initialize() {
        JSObject window = (JSObject) mWebEngine.executeScript("window");
        mWindow = window;
        window.setMember("javangBridge", this);
        mWebEngine.executeScript("javangHook();");
    }

    public MyModel foo() {
        return new MyModel();
    }
    public void registerController(String ctrlClassName, JSObject scope, JSObject functionContainer) {
        Class ctrlClass = null;
        try {
            ctrlClass = ClassLoader.getSystemClassLoader().loadClass(ctrlClassName);

            Object ctrlInstance = ctrlClass.newInstance();
            for (Method method : ctrlClass.getDeclaredMethods()) {
                Scope scopeAnnotation  = method.getAnnotation(Scope.class);
                if (scopeAnnotation != null) {
                    String memberName = (scopeAnnotation.value().isEmpty() ? method.getName() : scopeAnnotation.value());
                    functionContainer.setMember(memberName, new FunctionWrapper(ctrlInstance, method));
                }
            }

            if (ctrlInstance instanceof JavaNGController) {
                ((JavaNGController)ctrlInstance).setScope(scope);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class FunctionWrapper {
        private Method mMethod;
        private Object mInstance;

        public FunctionWrapper(Object instance, Method method) {
            mInstance = instance;
            mMethod = method;
        }

        public Object invoke(Object arg) throws InvocationTargetException, IllegalAccessException {
            Object returnVal = mMethod.invoke(mInstance);
            return returnVal;
        }
    }
}
