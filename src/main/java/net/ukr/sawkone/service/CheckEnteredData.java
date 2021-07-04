package net.ukr.sawkone.service;

import net.ukr.sawkone.model.entity.*;
import net.ukr.sawkone.view.View;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckEnteredData {
    private final String errorMessage = "Error. The data entered is incorrect";
    private View view;

    public CheckEnteredData(View view) {
        this.view = view;
    }

    public Long orNumberLong(String message) {
        boolean isNotNumber = true;
        long number = 0;
        while (isNotNumber) {
            try {
                view.write(message);
                number = Long.parseLong(view.read());
                if (number > 0) {
                    isNotNumber = false;
                }
            } catch (Exception e) {
                view.write(errorMessage);
            }
        }
        return number;
    }

    public int orNumberInt(String message) {
        boolean isNotNumber = true;
        int number = 0;
        while (isNotNumber) {
            try {
                view.write(message);
                number = Integer.parseInt(view.read());
                if (number > 0) {
                    isNotNumber = false;
                }
            } catch (Exception e) {
                view.write(errorMessage);
            }
        }
        return number;
    }

    public String orLineIsEmpty(String message) {
        boolean isNotString = true;
        String result = "";
        while (isNotString) {
            try {
                view.write(message);
                result = view.read();
                if (!result.isEmpty()) {
                    isNotString = false;
                }
            } catch (Exception e) {
                view.write(errorMessage);
            }
        }
        return result;
    }

    public PetStatus orGivenPetStatus(String message) {
        boolean isNotPetStatus = true;
        PetStatus petStatus = PetStatus.sold;
        while (isNotPetStatus) {
            view.write(message);
            try {
                petStatus = PetStatus.valueOf(view.read().toLowerCase());
                isNotPetStatus = false;
            } catch (Throwable throwable) {
                view.write(errorMessage);
            }
        }
        return petStatus;
    }

    public OrderStatus orGivenOrderStatus(String message) {
        boolean isNotOrderStatus = true;
        OrderStatus orderStatus = OrderStatus.approved;
        while (isNotOrderStatus) {
            view.write(message);
            try {
                orderStatus = OrderStatus.valueOf(view.read().toLowerCase());
                isNotOrderStatus = false;
            } catch (Throwable throwable) {
                view.write(errorMessage);
            }
        }
        return orderStatus;
    }

    public PetEntity getPetEntity() {
        PetEntity petCreate = new PetEntity();
        petCreate.setId(orNumberLong("Enter id pet"));
        petCreate.setName(orLineIsEmpty("Enter pet's name"));
        petCreate.setCategory(createCategory("Enter the name of the pet category"));
        petCreate.setTags(createTags("Enter tags"));
        petCreate.setPhotoUrls(createPhotoUrls("Enter photo url"));
        petCreate.setStatus(orGivenPetStatus("Enter status: available, pending, sold"));
        return petCreate;
    }

    public List<Tags> createTags(String message) {
        List<Tags> tags = new ArrayList<>();
        long count = 1;
        boolean enterClose = true;
        view.write(message);
        while (enterClose) {
            String text = orLineIsEmpty("Enter 'yes' if you want to add a tag or 'no' to continue");
            if (text.equalsIgnoreCase("yes")) {
                Tags create = new Tags(count++, orLineIsEmpty("enter tags"));
                tags.add(create);
            } else if (text.equalsIgnoreCase("no")) {
                enterClose = false;
            } else {
                view.write("I do not understand you enter 'yes' or 'no'");
            }
        }
        return tags;
    }

    public List<String> createPhotoUrls(String message) {
        List<String> photoUrls = new ArrayList<>();
        boolean enterClose = true;
        view.write(message);
        while (enterClose) {
            String text = orLineIsEmpty("Enter 'yes' if you want to add a photo or 'no' to continue");
            if (text.equalsIgnoreCase("yes")) {
                photoUrls.add(orLineIsEmpty("Enter url"));
            } else if (text.equalsIgnoreCase("no")) {
                enterClose = false;
            } else {
                view.write("I do not understand you enter 'yes' or 'no'");
            }
        }
        return photoUrls;
    }

    public Category createCategory(String message) {
        boolean enterClose = true;
        long count = 1;
        Category category = null;
        while (enterClose) {
            category = new Category(count++, orLineIsEmpty(message));
            enterClose = false;
        }
        return category;
    }

    public File orFileIsImage(String message) {
        File file = null;
        boolean isFieldBlank = true;
        while (isFieldBlank) {
            try {
                file = new File(orLineIsEmpty(message));
                if (ImageIO.read(file) != null) {
                    isFieldBlank = false;
                }
            } catch (Exception ex) {
                view.write(errorMessage);
            }
        }
        return file;
    }

    public boolean trueOrFalse(String message) {
        while (true) {
            String result = orLineIsEmpty(message).toLowerCase();
            if (result.equals("true")) {
                return true;
            } else if (result.equals("false")) {
                return false;
            }
            view.write(errorMessage);
        }
    }

    public long idForOrderSearch(String message) {
        long id = 0;
        boolean isGoodNumber = true;
        while (isGoodNumber) {
            id = orNumberLong(message);
            if (id > 0 && id <= 10) {
                isGoodNumber = false;
            } else view.write(errorMessage);
        }
        return id;
    }

    public UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(orNumberLong("Enter id"));
        userEntity.setUserName(orLineIsEmpty("Enter username").toLowerCase());
        userEntity.setFirstName(orLineIsEmpty("Enter first name"));
        userEntity.setLastName(orLineIsEmpty("Enter last name"));
        userEntity.setEmail(orLineIsEmpty("Enter email"));
        userEntity.setPhone(orLineIsEmpty("Enter phone"));
        userEntity.setPassword(orLineIsEmpty("Enter password"));
        userEntity.setUserStatus(orNumberInt("Enter user status"));
        return userEntity;
    }

    public List<UserEntity> createListUser(String message) {
        List<UserEntity> userEntityList = new ArrayList<>();
        boolean enterClose = true;
        view.write(message);
        while (enterClose) {
            String text = orLineIsEmpty("Enter 'yes' if you want to add a user or 'no' to continue");
            if (text.equalsIgnoreCase("yes")) {
                UserEntity create = getUserEntity();
                userEntityList.add(create);
            } else if (text.equalsIgnoreCase("no")) {
                enterClose = false;
            } else {
                view.write("I do not understand you enter 'yes' or 'no'");
            }
        }
        if (userEntityList.isEmpty()) {
            view.write("You have not created any users");
        }
        return userEntityList;
    }

    public UserEntity getUpdateUser(String username, UserEntity user) {
        user.setId(orNumberLong("Enter id"));
        user.setUserName(username);
        user.setFirstName(orLineIsEmpty("Enter first name"));
        user.setLastName(orLineIsEmpty("Enter last name"));
        user.setEmail(orLineIsEmpty("Enter email"));
        user.setPhone(orLineIsEmpty("Enter phone"));
        user.setPassword(orLineIsEmpty("Enter password"));
        user.setUserStatus(orNumberInt("Enter user status"));
        return user;
    }

    public void getApiResponse(ApiResponse response, String yes, String no) {
        if (response.getCode() == 200) {
            view.write(response.toString() + "\n" + yes);
        } else view.write(no);
    }
}