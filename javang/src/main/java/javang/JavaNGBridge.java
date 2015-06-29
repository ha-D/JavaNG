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

    public void registerController(String ctrlClassName, JSObject scope, JSObject functionContainer) {
        Class ctrlClass = null;
        try {
            ctrlClass = ClassLoader.getSystemClassLoader().loadClass(ctrlClassName);

            Object ctrlInstance = ctrlClass.newInstance();
            for (Method method : ctrlClass.getDeclaredMethods()) {
                Scope scopeAnnotation  = method.getAnnotation(Scope.class);
                if (scopeAnnotation != null) {
                    String memberName = (scopeAnnotation.value().isEmpty() ? method.getName() : scopeAnnotation.value());
                    functionContainer.setMember(memberName, FunctionWrapperFactory.build(ctrlInstance, method));
                }
            }

            if (ctrlInstance instanceof JavaNGController) {
                ((JavaNGController)ctrlInstance).setScope(scope);
                ((JavaNGController)ctrlInstance).setWebEngine(mWebEngine);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class FunctionWrapperFactory {
        public static FunctionWrapper build(Object ctrlInstance, Method method) {
            switch (method.getParameterCount()) {
                case 0:
                    return new NoArgFunctionWrapper(ctrlInstance, method);
                case 1:
                    return new SingleArgFunctionWrapper(ctrlInstance, method);
                case 2:
                    return new DoubleArgFunctionWrapper(ctrlInstance, method);
                case 3:
                    return new TripleArgFunctionWrapper(ctrlInstance, method);
            }
            throw new TooManyArgumentsException("Too many arguments defined for method " + method.getName());
        }

        private static class TooManyArgumentsException extends RuntimeException {
            public TooManyArgumentsException(String message) {
                super(message);
            }
        }
    }

    public static class FunctionWrapper {
        protected Method mMethod;
        protected Object mInstance;

        public FunctionWrapper(Object instance, Method method) {
            mInstance = instance;
            mMethod = method;
        }

        public Object invoke(Object arg) throws InvocationTargetException, IllegalAccessException {
            System.out.println("Invoking function " + mMethod.getName());
            Object returnVal = mMethod.invoke(mInstance);
            return returnVal;
        }
    }

    public static class NoArgFunctionWrapper extends FunctionWrapper {
        public NoArgFunctionWrapper(Object instance, Method method) {
            super(instance, method);
        }

        public Object invoke() throws InvocationTargetException, IllegalAccessException {
            return mMethod.invoke(mInstance);
        }
    }

    public static class SingleArgFunctionWrapper extends FunctionWrapper {
        public SingleArgFunctionWrapper(Object instance, Method method) {
            super(instance, method);
        }

        public Object invoke(Object arg) throws InvocationTargetException, IllegalAccessException {
            return mMethod.invoke(mInstance, arg);
        }
    }

    public static class DoubleArgFunctionWrapper extends FunctionWrapper {
        public DoubleArgFunctionWrapper(Object instance, Method method) {
            super(instance, method);
        }

        public Object invoke(Object arg1, Object arg2) throws InvocationTargetException, IllegalAccessException {
            return mMethod.invoke(mInstance, arg1, arg2);
        }
    }

    public static class TripleArgFunctionWrapper extends FunctionWrapper {
        public TripleArgFunctionWrapper(Object instance, Method method) {
            super(instance, method);
        }

        public Object invoke(Object arg1, Object arg2, Object arg3) throws InvocationTargetException, IllegalAccessException {
            return mMethod.invoke(mInstance, arg1, arg2, arg3);
        }
    }
}
