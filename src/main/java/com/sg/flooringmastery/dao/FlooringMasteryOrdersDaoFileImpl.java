/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.service.DataValidationException;
import com.sg.flooringmastery.service.DoesNotExistException;
import com.sg.flooringmastery.service.FutureDateException;
import com.sg.flooringmastery.service.PersistenceException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryOrdersDaoFileImpl implements FlooringMasteryOrdersDao {
    private final String ORDERS_FOLDER;
    private final String ORDERS_FILE;
    private final String EXPORTFILE_PATH = "Backup/dataexport.txt";
    
    public FlooringMasteryOrdersDaoFileImpl() {
        ORDERS_FOLDER = "Orders";
        ORDERS_FILE = "Orders/Orders_";
    }
    
    public FlooringMasteryOrdersDaoFileImpl(String orderFolderFile, String orderTextFile) {
        ORDERS_FOLDER = orderFolderFile;
        ORDERS_FILE = orderTextFile;
    }
    
    public static final String DELIMITER = "::";
    public static final String HEADER = "Order Number" + DELIMITER
            + "Customer Name" + DELIMITER
            + "State Initials" + DELIMITER
            + "State Name" + DELIMITER
            + "Tax Rate" + DELIMITER
            + "Product Type" + DELIMITER
            + "Area" + DELIMITER
            + "Cost Per Square Foot" + DELIMITER
            + "Labor Cost Per Square Foot" + DELIMITER
            + "Material Cost" + DELIMITER
            + "Labor Cost" + DELIMITER
            + "Tax" + DELIMITER
            + "Total";
    
    private Map<LocalDate, List<Orders>> localDateOrderList = new HashMap<>();
    
    @Override
    public List<LocalDate> getAllOrderDates() throws PersistenceException {
        this.loadOrdersFile();
        List<LocalDate> allDates = new ArrayList<>(localDateOrderList.keySet());
        List<LocalDate> availableDates = new ArrayList<>();
        for(LocalDate thisDate : allDates) {
            if(localDateOrderList.get(thisDate).isEmpty()) {
                continue;
            } else {
                availableDates.add(thisDate);
            }
        }
        return availableDates;
    }
    
    @Override
    public List<Orders> getAllOrders(LocalDate date) throws PersistenceException, DoesNotExistException {
        List<Orders> ordersForDate = localDateOrderList.get(date);
        return ordersForDate;
    }

    @Override
    public Orders getOrder(List<Orders> orderList, int orderNumber) throws PersistenceException, DoesNotExistException {
        Orders chosenOrder = new Orders();
        for(Orders o : orderList) {
            if(o.getOrderNumber() == orderNumber) {
                chosenOrder = o;
            }
        }
        return chosenOrder;
    }

    @Override
    public List<Orders> addOrder(LocalDate orderFuturedate, Orders order) throws PersistenceException, FutureDateException, DataValidationException {
        this.loadOrdersFile();
        List<Orders> newOrderList = localDateOrderList.get(orderFuturedate);
        if(newOrderList == null) {
            order.setOrderNumber(1);
            newOrderList = new ArrayList<>();
            localDateOrderList.put(orderFuturedate, newOrderList);
            newOrderList.add(order);
            this.writeOrdersFile();
            return newOrderList;
        } else {
            int orderNumber = 1;
            for(Orders o : newOrderList) {
                int thisOrderNumber = o.getOrderNumber();
                if(thisOrderNumber >= orderNumber) {
                    orderNumber = thisOrderNumber + 1;
                }
            }
            order.setOrderNumber(orderNumber);
            newOrderList.add(order);
            this.writeOrdersFile();
            return newOrderList;
        }
    }

    @Override
    public List<Orders> editOrder(LocalDate date, List<Orders> orderList, Orders editedOrder) throws PersistenceException, DataValidationException {
        int orderIndex = 0;
        int orderNumber = editedOrder.getOrderNumber();

        for(Orders o : orderList) {
            if(o.getOrderNumber() == orderNumber) {
                orderIndex = orderList.indexOf(o);
            }
        }
        
        orderList.set(orderIndex, editedOrder);
        this.writeOrdersFile();
        return orderList;
    }

    @Override
    public List<Orders> removeOrder(LocalDate date, List<Orders> orderList, Orders orderToRemove) throws PersistenceException {
        orderList.remove(orderToRemove);
        this.writeOrdersFile();
        return orderList;
    }
    
    @Override
    public void exportData() throws PersistenceException {
        // 1. Read all Orders/Orders_ file
        // 2. Write into Backup/dataexport.txt file
        Scanner sc;
        PrintWriter out;
        File ordersFolder = new File(ORDERS_FOLDER);
        File[] ordersFileList = ordersFolder.listFiles();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        
        LocalDateTime rightNow = LocalDateTime.now();
        String dateStamp = rightNow.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        String timeStamp = rightNow.format(DateTimeFormatter.ofPattern("HH.mm.ss"));
        String dateTimeStamp = dateStamp + " @ " + timeStamp;
        
        try {
            out = new PrintWriter(new FileWriter(EXPORTFILE_PATH));
        } catch(IOException e) {
            throw new PersistenceException("Could not write to Backup/dataexport.txt", e);
        }
        
        out.println("Last backed up : " + dateTimeStamp);
        List<LocalDate> availableDates = this.getAllOrderDates();
        
        if(availableDates.isEmpty()) {
            out.println("There are no orders available to back up at this time.");
            out.flush();
            out.close();
        } else {
            for(File o : ordersFileList) {
                String ORDERFILE_PATH = o.getPath();
                String[] getOrderDate = ORDERFILE_PATH.split("_");
                getOrderDate = getOrderDate[1].split("\\.");
                String dateInString = getOrderDate[0];

                LocalDate orderDate = LocalDate.parse(dateInString, formatter);
                String orderDateFull = orderDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));

                try{
                    sc = new Scanner(new BufferedReader(new FileReader(ORDERFILE_PATH)));
                } catch(FileNotFoundException e) {
                    throw new PersistenceException("Could not load order file into memory.", e);
                }

                sc.nextLine();
                if(sc.hasNextLine()) {
                    out.println();
                    out.println("Orders for " + orderDateFull);
                    out.println(HEADER);
                } else {
                    continue;
                }

                while (sc.hasNextLine()) {
                    String thisOrder = sc.nextLine();
                    out.println(thisOrder);
                    out.flush();
                }
            }
            out.close();
        }
    }
    
    private Orders unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMITER);
        
        int orderNumber = Integer.parseInt(orderTokens[0]);
        String customerName = orderTokens[1];
        String stateInitials = orderTokens[2];
        String stateName = orderTokens[3];
        BigDecimal taxRate = new BigDecimal(orderTokens[4]).setScale(2, RoundingMode.HALF_UP);
        String productType = orderTokens[5];
        BigDecimal area = new BigDecimal(orderTokens[6]).setScale(2, RoundingMode.HALF_UP);
        BigDecimal costPerSquareFoot = new BigDecimal(orderTokens[7]).setScale(2, RoundingMode.HALF_UP);
        BigDecimal laborCostPerSquareFoot = new BigDecimal(orderTokens[8]).setScale(2, RoundingMode.HALF_UP);
        
        Orders orderFromFile = new Orders(orderNumber, customerName, stateInitials, stateName, taxRate, productType, area, costPerSquareFoot, laborCostPerSquareFoot);
        return orderFromFile;
    }
    
    private void loadOrdersFile() throws PersistenceException {
        Scanner sc;
        File folder = new File(ORDERS_FOLDER);
        File[] listOfFiles = folder.listFiles();
        
        for(File file : listOfFiles) {
            String THIS_ORDER_PATH = file.getPath();
            
            String[] getLocalDate = THIS_ORDER_PATH.split("_");
            getLocalDate = getLocalDate[1].split("\\.");
            String dateInString = getLocalDate[0];
            
            try {
                sc = new Scanner(new BufferedReader(new FileReader(THIS_ORDER_PATH)));
            } catch(FileNotFoundException e) {
                throw new PersistenceException("Could not load order data into memory.", e);
            }
            
            String currentLine;
            Orders currentOrder;
            List<Orders> orderList = new ArrayList<>();
            
            try {
                sc.nextLine();
                while(sc.hasNextLine()) {
                    currentLine = sc.nextLine();
                    currentOrder = this.unmarshallOrder(currentLine);
                    orderList.add(currentOrder);
                }
                LocalDate dateOfOrders = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern("MMddyyyy"));
                localDateOrderList.put(dateOfOrders, orderList);
                sc.close();
            } catch(Exception e) {
                while(sc.hasNextLine()) {
                currentLine = sc.nextLine();
                currentOrder = this.unmarshallOrder(currentLine);
                orderList.add(currentOrder);
            }
            
            LocalDate dateOfOrders = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern("MMddyyyy"));
            localDateOrderList.put(dateOfOrders, orderList);
            sc.close();
            }
        }
    }
    
    private String marshallOrder(Orders order) {
        String orderAsText = String.valueOf(order.getOrderNumber()) + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getStateInitials() + DELIMITER;
        orderAsText += order.getStateName() + DELIMITER;
        orderAsText += order.getTaxRate().toString() + DELIMITER;
        orderAsText += order.getProductType() + DELIMITER;
        orderAsText += order.getArea().toString() + DELIMITER;
        orderAsText += order.getCostPerSquareFoot().toString() + DELIMITER;
        orderAsText += order.getLaborCostPerSquareFoot().toString() + DELIMITER;
        orderAsText += order.getMaterialCost().toString() + DELIMITER;
        orderAsText += order.getLaborCost().toString() + DELIMITER;
        orderAsText += order.getTax().toString() + DELIMITER;
        orderAsText += order.getTotal().toString();
        
        return orderAsText;
    }
    
    private void writeOrdersFile() throws PersistenceException {
        PrintWriter out;
        List<LocalDate> localdateList = new ArrayList(localDateOrderList.keySet());
        
        String PATH = this.ORDERS_FILE;
        for(LocalDate thisDate : localdateList) {
            String extension = thisDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
            String thisFilePath = PATH + extension;
            
            try {
                out = new PrintWriter(new FileWriter(thisFilePath));
            } catch(IOException e) {
                throw new PersistenceException("Could not save order data.", e);
            }
            
            out.println(this.HEADER);
            String orderAsText;
            List<Orders> orderList = localDateOrderList.get(thisDate);

            for(Orders currentOrder : orderList) {
                orderAsText = marshallOrder(currentOrder);
                out.println(orderAsText);
                out.flush();
            }
            out.close();
        }
    }
}

