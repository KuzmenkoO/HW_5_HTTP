package net.ukr.sawkone.controller;

import net.ukr.sawkone.command.*;
import net.ukr.sawkone.service.CheckEnteredData;
import net.ukr.sawkone.util.HttpUtil;
import net.ukr.sawkone.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    private View view;
    private List<Command> commands;

    public Controller(View view) {
        CheckEnteredData checkEnteredData = new CheckEnteredData(view);
        HttpUtil httpUtil = new HttpUtil();
        this.view = view;
        this.commands = new ArrayList<>(Arrays.asList(
                new Help(view),
                new Pet(view, checkEnteredData, httpUtil),
                new Store(view, checkEnteredData, httpUtil),
                new User(view, checkEnteredData, httpUtil)
        ));
    }

    public void run() {
        view.write("Welcome to the Pet store");
        doCommand();
    }

    private void doCommand() {
        boolean isNotExit = true;
        while (isNotExit) {
            view.write("Please enter a command or help to retrieve command list");
            String inputCommand = view.read();
            for (Command command : commands) {
                if (command.canProcess(inputCommand)) {
                    command.process();
                    break;
                } else if (inputCommand.equalsIgnoreCase("exit")) {
                    view.write("Good Bye!");
                    isNotExit = false;
                    break;
                }
            }
        }
    }
}