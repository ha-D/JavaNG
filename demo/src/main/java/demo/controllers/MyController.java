package demo.controllers;

import javang.JavaNGController;
import javang.Scope;

public class MyController extends JavaNGController {

    @Scope
    public String foo() {
        return "HELLO";
    }
}
