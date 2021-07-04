package net.ukr.sawkone.command;

import net.ukr.sawkone.model.entity.PetEntity;
import net.ukr.sawkone.service.CheckEnteredData;
import net.ukr.sawkone.util.HttpUtil;
import net.ukr.sawkone.view.View;

import java.io.IOException;

public class Pet implements Command {
    private View view;
    private CheckEnteredData check;
    private HttpUtil httpUtil;

    public Pet(View view, CheckEnteredData check, HttpUtil httpUtil) {
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
                    1 - Create pet;
                    2 - Update an existing pet;
                    3 - Finds Pets by status;
                    4 - Find pet by ID;
                    5 - Update pet by id;
                    6 - Delete pet by id;
                    7 - Uploads an image pet by id;
                    8 - Exit the pet menu""");
            int numberCommand = 0;
            try {
                numberCommand = Integer.parseInt(view.read());
            } catch (Exception e) {
                view.write("Wrong command is entered");
            }
            switch (numberCommand) {
                case 1 -> {
                    PetEntity petCreate = check.getPetEntity();
                    try {
                        view.write(httpUtil.createPet(petCreate).toString());
                    } catch (Exception e) {
                        view.write("No pet created");
                    }
                }
                case 2 -> {
                    PetEntity petUpdate = check.getPetEntity();
                    try {
                        PetEntity petEntity = httpUtil.updatePet(petUpdate);
                        if (petEntity.getId() != 0) {
                            view.write(petEntity.toString());
                        } else view.write("The pet with such id is absent");
                    } catch (Exception e) {
                        view.write("Pets are not updated");
                    }
                }
                case 3 -> {
                    try {
                        view.write(httpUtil.findByStatus(check.orGivenPetStatus("Enter status: available, pending, sold"))
                                .toString());
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                case 4 -> {
                    try {
                        PetEntity petEntity = httpUtil.getPetById(check.orNumberLong("Enter id to search for a pet"));
                        if (petEntity.getId() != 0) {
                            view.write(petEntity.toString());
                        } else view.write("Pet with such id not found try yet");
                    } catch (Exception e) {
                        view.write("The pet not found");
                    }
                }
                case 5 -> {
                    try {
                        long id = check.orNumberLong("Enter pet id to update");
                        view.write(httpUtil.updatePetById(id,
                                check.orLineIsEmpty("Enter a new pet name"),
                                check.orGivenPetStatus("Enter pet status"))
                                .toString());
                    } catch (Exception e) {
                        view.write("You are update a non-existent object");
                    }
                }
                case 6 -> {
                    try {
                        view.write(httpUtil.deleteById(check.orNumberLong("Enter id for delete"))
                                .toString());
                    } catch (Exception e) {
                        view.write("You are deleting a non-existent object");
                    }
                }
                case 7 -> {
                    try {
                        view.write(httpUtil.uploadImage(check.orNumberLong("Enter id pet"),
                                check.orLineIsEmpty("Enter meta data"),
                                check.orFileIsImage("Enter url image")).toString());
                    } catch (Exception e) {
                        view.write("Error file not loaded");
                    }
                }
                case 8 -> isNotExit = false;
                default -> view.write("Select another command");
            }
        }
    }

    @Override
    public String commandName() {
        return "pet";
    }
}