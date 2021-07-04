package net.ukr.sawkone.command;

import net.ukr.sawkone.model.entity.ApiResponse;
import net.ukr.sawkone.model.entity.OrderEntity;
import net.ukr.sawkone.service.CheckEnteredData;
import net.ukr.sawkone.util.HttpUtil;
import net.ukr.sawkone.view.View;

import java.io.IOException;
import java.time.LocalDate;

public class Store implements Command {
    private View view;
    private CheckEnteredData check;
    private HttpUtil httpUtil;

    public Store(View view, CheckEnteredData check, HttpUtil httpUtil) {
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
                    1 - Create order;
                    2 - Delete order by id;
                    3 - Returns pet inventories by status;
                    4 - Find order by id
                    5 - Exit the store menu""");
            int numberCommand = 0;
            try {
                numberCommand = Integer.parseInt(view.read());
            } catch (Exception e) {
                view.write("Wrong command is entered");
            }
            switch (numberCommand) {
                case 1 -> {
                    try {
                        OrderEntity orderCreate = new OrderEntity();
                        orderCreate.setId(check.orNumberLong("Enter id"));
                        orderCreate.setPetId(check.orNumberLong("Enter pet id"));
                        orderCreate.setShipDate(LocalDate.now().toString());
                        orderCreate.setStatus(check.orGivenOrderStatus("Enter status: placed, approved, delivered"));
                        orderCreate.setQuantity(check.orNumberInt("Enter quantity"));
                        orderCreate.setComplete(check.trueOrFalse("Enter complete: true or false"));
                        view.write(httpUtil.createOrder(orderCreate).toString());
                    } catch (Exception e) {
                        view.write("Order not created");
                    }
                }
                case 2 -> {
                    try {
                        ApiResponse apiResponse = httpUtil.deleteOrderById(check.orNumberLong("Enter id"));
                        check.getApiResponse(apiResponse, "Order delete", "The order not found");
                    } catch (Exception e) {
                        view.write("Order not deleted");
                    }
                }
                case 3 -> {
                    try {
                        view.write(httpUtil.mapOfStatusCodesToQuantities().toString());
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                case 4 -> {
                    try {
                        OrderEntity orderEntity = httpUtil.getOrderById(check.idForOrderSearch("Enter id from 1 to 10"));
                        if (orderEntity.getId() != 0) {
                            view.write(orderEntity.toString());
                        } else view.write("Order with such id not found try yet");
                    } catch (Exception e) {
                        view.write("The order not found");
                    }
                }
                case 5 -> isNotExit = false;
                default -> view.write("select another command");
            }
        }
    }

    @Override
    public String commandName() {
        return "store";
    }
}