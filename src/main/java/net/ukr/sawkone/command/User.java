package net.ukr.sawkone.command;

import net.ukr.sawkone.model.entity.ApiResponse;
import net.ukr.sawkone.model.entity.UserEntity;
import net.ukr.sawkone.service.CheckEnteredData;
import net.ukr.sawkone.util.HttpUtil;
import net.ukr.sawkone.view.View;

public class User implements Command {
    private View view;
    private CheckEnteredData check;
    private HttpUtil httpUtil;

    public User(View view, CheckEnteredData check, HttpUtil httpUtil) {
        this.view = view;
        this.check = check;
        this.httpUtil = httpUtil;
    }

    @Override
    public void process() {
        boolean isNotExit = true;
        while (isNotExit) {
            view.write("""
                    Enter number command:
                    1 - Create user;
                    2 - Create list user;
                    3 - Find user by name;
                    4 - Update user by name;
                    5 - Delete user by name;
                    6 - exit the user menu""");
            int numberCommand = 0;
            try {
                numberCommand = Integer.parseInt(view.read());
            } catch (Exception e) {
                view.write("Wrong command is entered");
            }
            switch (numberCommand) {
                case 1 -> {
                    UserEntity userEntity = check.getUserEntity();
                    try {
                        check.getApiResponse(httpUtil.createUser(userEntity), "User created", "User not created");
                    } catch (Exception e) {
                        view.write("User not created");
                    }
                }
                case 2 -> {
                    try {
                        view.write(httpUtil.createWithListUsers(check.createListUser("Enter users to add to the list")).toString());
                    } catch (Exception e) {
                        view.write("Users not created");
                    }
                }
                case 3 -> {
                    try {
                        UserEntity userEntity = httpUtil.getUserByName(check.orLineIsEmpty("Enter username").toLowerCase());
                        if (userEntity.getId() != 0) {
                            view.write(userEntity.toString());
                        } else view.write("User with such username not found try yet");
                    } catch (Exception e) {
                        view.write("The user not found");
                    }
                }
                case 4 -> {
                    try {
                        String username = check.orLineIsEmpty("Enter username").toLowerCase();
                        UserEntity user = httpUtil.getUserByName(username);
                        user = check.getUpdateUser(username, user);
                        check.getApiResponse(httpUtil.updateUserByName(username, user),
                                "User update",
                                "The user has not been updated");
                    } catch (Exception e) {
                        view.write("The user with this name is missing, the user has not been updated");
                    }
                }
                case 5 -> {
                    try {
                        ApiResponse apiResponse = httpUtil.deleteUserByName(check.orLineIsEmpty("Enter username").toLowerCase());
                        check.getApiResponse(apiResponse, "User delete", "User not deleted");
                    } catch (Exception e) {
                        view.write("User not deleted");
                    }
                }
                case 6 -> isNotExit = false;
                default -> view.write("Select another command");
            }
        }
    }

    @Override
    public String commandName() {
        return "user";
    }
}