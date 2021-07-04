package net.ukr.sawkone;

import net.ukr.sawkone.controller.Controller;
import net.ukr.sawkone.view.Console;
import net.ukr.sawkone.view.View;

public class Application {

    public static void main(String[] args) {
        View view = new Console();
        Controller controller = new Controller(view);
        controller.run();
    }
}