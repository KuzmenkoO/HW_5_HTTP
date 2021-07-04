package net.ukr.sawkone.command;

public interface Command {
    void process();

    String commandName();

    default boolean canProcess(String command) {
        return commandName().equals(command);
    }
}