package net.ukr.sawkone.command;

import net.ukr.sawkone.view.View;

public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public void process() {
        view.write("help - show a list of commands");
        view.write("pet - show menu pet");
        view.write("store - show menu store");
        view.write("user - show menu user");
        view.write("exit - exit from an application");
    }

    @Override
    public String commandName() {
        return "help";
    }
}