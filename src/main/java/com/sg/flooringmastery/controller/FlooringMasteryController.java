/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.service.PersistenceException;
import com.sg.flooringmastery.model.Products;
import com.sg.flooringmastery.model.Taxes;
import com.sg.flooringmastery.service.DataValidationException;
import com.sg.flooringmastery.service.DoesNotExistException;
import com.sg.flooringmastery.service.DuplicateEntryException;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.service.FutureDateException;
import com.sg.flooringmastery.view.FlooringMasteryView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryController {
    private final String VERIFICATION_CODE = "aug2021JAVA";
//    private FlooringMasteryOrdersDao daoOrder = new FlooringMasteryOrdersDaoFileImpl();
//    private FlooringMasteryProductsDao daoProduct = new FlooringMasteryProductsDaoFileImpl();
//    private FlooringMasteryTaxDao daoTax = new FlooringMasteryTaxDaoFileImpl();
//    
//    private UserIO io = new UserIOConsoleImpl();
    
    private final FlooringMasteryServiceLayer service;
    private final FlooringMasteryView view;
    
    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }
    
    // CODE FOR UI MENU
    public void runUI() {
        boolean runOn = true;
        int menuSelect = 0;
        
        try {
        while(runOn) {    
            menuSelect = view.printUIAndGetSelect();
            
            switch(menuSelect) {
                case 1:
                    this.displayOrders();
                    break;
                case 2:
                    this.createOrder();
                    break;
                case 3:
                    this.editOrder();
                    break;
                case 4:
                    this.removeOrder();
                    break;
                case 5:
                    this.exportData();
                    break;
                case 6:
                    runOn = false;
                    break;
                case 7:
                    this.verifyTSGCorpAccess();
//                    this.runTSGDatabase();
                    break;
                default:
                    view.displayBannerUnknownCommand();
            }
        }
        view.displayExitMessage("EXITING FINAL EXAM: FLOORING PROGRAM !!! THANK YOU");
        System.exit(0);
        } catch(PersistenceException | DoesNotExistException | DataValidationException |
                FutureDateException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    private void displayOrders() throws PersistenceException, DoesNotExistException {
        view.displayBannerDisplayAllOrderDates();
        List<LocalDate> orderdateList = service.getAllOrderDates();
        if(orderdateList.isEmpty()) {
            view.displayErrorMessage("There are no orders available.");
            view.waitForEnter("Please hit ENTER to continue.");
        } else {
            view.displayOrderDateList(orderdateList);
            LocalDate chosenDate = view.getOrderDateTo(orderdateList, "DISPLAY");
            if(chosenDate == null) {
                view.displayErrorMessage("There are no orders available.");
                view.waitForEnter("Please hit ENTER to continue.");
            } else {
                List<Orders> orderList = service.getAllOrders(chosenDate);
                view.displayBannerAllOrders(chosenDate);
                view.displayAllOrdersForDate(orderList);
                view.waitForEnter("\nPlease hit ENTER to continue.");
            }
        }
    }
    
    private void createOrder() throws PersistenceException, FutureDateException, DataValidationException {
        String msgToConfirm ="Is this correct? (y / n)";
        String option = "ORDER";
        List<Taxes> statetaxList = service.getAllStateTaxes();
        List<Products> productList = service.getAllProducts();
        
        view.displayBannerAddOrder();
        LocalDate orderFutureDate = view.getNewOrderDate();
        Orders newOrder = view.getNewOrderUserInfo(statetaxList, productList);
        boolean confirmOrder = false;
        confirmOrder = view.displayNewOrderInfoAndConfirm(newOrder, orderFutureDate, msgToConfirm, option);
        
        if(confirmOrder == true) {
            service.addOrder(orderFutureDate, newOrder);
            view.displayBannerAddOrderSuccess();
        } else {
            view.printBanner("Order was not added.");
            view.waitForEnter("Please hit ENTER to continue.");
        }
    }

    private void editOrder() throws PersistenceException, DoesNotExistException, DataValidationException {
        List<Taxes> statetaxList = service.getAllStateTaxes();
        List<Products> productList = service.getAllProducts();
        
        view.displayBannerEditOrder();
        List<LocalDate> orderdateList = service.getAllOrderDates();
        if(orderdateList.isEmpty()) {
            view.displayErrorMessage("There are no orders avialable.");
            view.waitForEnter("Please hit ENTER to continue.");
        } else {
            view.displayOrderDateList(orderdateList);
            LocalDate chosenDate = view.getOrderDateTo(orderdateList, "EDIT");
            if(chosenDate == null) {
                view.displayErrorMessage("There are no orders available.");
                view.waitForEnter("Please hit ENTER to continue.");
            } else {
                List<Orders> orderList = service.getAllOrders(chosenDate);
                view.displayBannerAllOrders(chosenDate);
                view.displayAllOrdersForDate(orderList);
                int orderNumber = view.getOrderNumber(orderList, "EDIT");
                Orders orderToEdit = service.getOrder(orderList, orderNumber);
                Orders editedOrder = view.getEditOrderInfo(orderNumber, orderToEdit, statetaxList, productList);
                Orders orderToPass = view.displayEditInfoAndConfirm(orderToEdit, editedOrder, chosenDate);
                service.editOrder(chosenDate, orderList, orderToPass);
                view.printBanner("Process complete.");
                view.waitForEnter("Please hit ENTER to continue.");
            }
        }
    }
    
    private void removeOrder() throws PersistenceException, DoesNotExistException {
        List<Taxes> statetaxList = service.getAllStateTaxes();
        List<Products> productList = service.getAllProducts();
        
        view.displayBannerRemoveOrder();
        List<LocalDate> orderdateList = service.getAllOrderDates();
        if(orderdateList.isEmpty()) {
            view.displayErrorMessage("There are no orders available.");
            view.waitForEnter("Please hit ENTER to continue.");
        } else {
            view.displayOrderDateList(orderdateList);
            LocalDate chosenDate = view.getOrderDateTo(orderdateList, "REMOVE");
            if(chosenDate == null) {
                view.displayErrorMessage("There are no orders available.");
                view.waitForEnter("Please hit ENTER to continue.");
            } else {
                List<Orders> orderList = service.getAllOrders(chosenDate);
                view.displayBannerAllOrders(chosenDate);
                view.displayAllOrdersForDate(orderList);
                int orderNumber = view.getOrderNumber(orderList, "REMOVE");
                Orders orderToRemove = service.getOrder(orderList, orderNumber);
                boolean toRemove = false;
                toRemove = view.displayRemoveInfoAndConfirm(orderToRemove, chosenDate);
                
                if(toRemove == true) {
                    service.removeOrder(chosenDate, orderList, orderToRemove);
                    view.printBanner("Order was successfully removed.");
                } else {
                    view.printBanner("Order was NOT removed.");
                    view.waitForEnter("Please hit ENTER to continue.");
                }
            }
        }
    }
    
        
    private void exportData() throws PersistenceException {
        view.displayBannerExportData();
        service.exportData();
        view.displayResultExportData();
        view.waitForEnter("Please hit ENTER to continue.");
    }
    
        private void verifyTSGCorpAccess() throws PersistenceException{
        boolean contactAdmin = view.confirmContactAdmin();
        view.displayResultContactAdmin(contactAdmin);
        if(contactAdmin) {
            String verifyAccess = view.getVerificationCode();
            if(verifyAccess.equals(VERIFICATION_CODE)) {
                view.displayBannerAccessTSGCorpMenuSuccess();
                this.runTSGDatabase();
            } else {
                view.displayBannerAccessTSGCorpMenuFail();
            }
        } else {
            this.runUI();
        }
    }
    
    // CODE FOR DATABASE MENU
    private void runTSGDatabase() throws PersistenceException {
        boolean runOn = true;
        int menuSelect = 0;
        
        while(runOn) {
            menuSelect = view.printTSGDatabaseAndGetSelect();
        
            switch(menuSelect) {
                case 1:
                    this.runTSGProductMenu();
                    break;
                case 2:
                    this.runTSGTaxMenu();
                    break;
                case 3:
                    runOn = false;
                    break;
                case 4:
                    this.runUI();
                    break;
                default:
                    view.displayBannerUnknownCommand();
            }
        }
        view.displayExitMessage("EXITING TSG DATABASE MENU !!! THANK YOU");
        System.exit(0);
    }
    
    
    // CODE FOR PRODUCT MENU
    private void runTSGProductMenu() {
        boolean runOn = true;
        int menuSelect = 0;
        
        try {
            while(runOn) {
                List<Products> productList = service.getAllProducts();
                view.displayBannerTSGProductWelcome();
                if(productList.size() < 1) {
                    this.displayNoProduct();
                } else {
                    this.displayAllProducts();
                    menuSelect = view.printTSGProductAndGetSelect();
                    switch(menuSelect) {
                        case 1: 
                            this.createProduct();
                            break;
                        case 2:
                            this.removeProduct();
                            break;
                        case 3:
                            this.updateProduct();
                            break;
                        case 4:
                            this.runTSGTaxMenu();
                            break;
                        case 5:
                            runOn = false;
                            break;
                        case 6:
                            this.runUI();
                            break;
                        default:
                            view.displayBannerUnknownCommand();
                    }
                }
            }
            view.displayExitMessage("EXITING TSG PRODUCT MENU !!! THANK YOU");
            System.exit(0);
        } catch(PersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    private void displayNoProduct()  throws PersistenceException {
        view.displayBannerNoProducts();
        this.createProduct();
    }
    private void displayAllProducts() throws PersistenceException {
        List<Products> productList = service.getAllProducts();
        view.displayProductList(productList);
    }
    
    private void createProduct() throws PersistenceException  {
        view.displayBannerAddProduct();
        boolean hasErrors = false;
        
        do {
            Products newProduct = view.getNewProductInfo();
            try {
                service.createProduct(newProduct);
                view.displayBannerAddProductSuccess(newProduct.getProductType());
                hasErrors = false;
            } catch(DuplicateEntryException | DataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while(hasErrors);
    }
    
    private void updateProduct() throws PersistenceException  {
        view.displayBannerUpdateProduct();
        
        boolean hasErrors = false;
        
        do {
            try {
                this.displayAllProducts();
                String productType = view.getProductTypeTo("UPDATE: ");
                Products chosenProduct = service.getProduct(productType);
                Products updatingProduct = view.getUpdateProductInfo(chosenProduct);
                service.updateProduct(updatingProduct);
                view.displayResultUpdateProduct(updatingProduct);
                hasErrors = false;
            } catch (Exception e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while(hasErrors);
    }
    
    private void removeProduct() throws PersistenceException  {
        view.displayBannerRemoveProduct();
        this.displayAllProducts();
        String productType = view.getProductTypeTo("REMOVE: ");
        
        try {
            Products chosenProduct = service.getProduct(productType);
            String userConfirm = view.getRemoveProductConfirmation(chosenProduct);

            if(userConfirm.equalsIgnoreCase("y")) {
                Products removedProduct = service.removeProduct(productType);
                view.displayResultRemoveProduct(removedProduct); 
            } else if(userConfirm.equalsIgnoreCase("n")) {
                view.waitForEnter("Please hit ENTER to continue.");
            } else {
                view.waitForEnter("Something went wrong. Please hit ENTER to continue.");
            }
        } catch(Exception e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    // CODE FOR TAX MENU
    private void runTSGTaxMenu() {
        boolean runOn = true;
        int menuSelect = 0;
        
        try {
            while(runOn) {
                List<Taxes> stateTaxList = service.getAllStateTaxes();
                view.displayBannerTSGTaxWelcome();
                if(stateTaxList.size() < 1) {
                    this.displayNoTaxes();
                } else {
                    this.displayAllStateTaxes();
                    menuSelect = view.printTSGTaxAndGetSelect();
                    switch(menuSelect) {
                        case 1:
                            this.createStateTax();
                            break;
                        case 2:
                            this.removeStateTax();
                            break;
                        case 3:
                            this.updateStateTax();
                            break;
                        case 4: 
                            this.runTSGProductMenu();
                            break;
                        case 5:
                            runOn = false;
                            break;
                        case 6:
                            this.runUI();
                            break;
                        default:
                            view.displayBannerUnknownCommand();   
                        }
                }
            }
            view.displayExitMessage("EXITING TSG CORP TAX MENU !!! THANK YOU");
            System.exit(0);  
        } catch(PersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
 
    private void displayNoTaxes() throws PersistenceException {
        view.displayBannerNoTaxes();
        this.createStateTax();
    }
    
    private void displayAllStateTaxes() throws PersistenceException {
        List<Taxes> stateTaxList = service.getAllStateTaxes();
        view.displayTaxList(stateTaxList);
    }
    
    private void createStateTax() throws PersistenceException {
        view.displayBannerAddStateTax();
        boolean hasErrors = false;
        
        do {
            Taxes newStateTax = view.getNewStateTaxInfo();
            try {
                service.createStateTax(newStateTax);
                view.displayBannerAddStateTaxSuccess(newStateTax.getStateInitials(), newStateTax.getStateName());
                hasErrors = false;
            } catch(DuplicateEntryException | DataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while(hasErrors);
    }
    
    private void updateStateTax() throws PersistenceException {
        view.displayBannerUpdateTax();
        boolean hasErrors = false;
        
        do {
            try {
                this.displayAllStateTaxes();
                String stateInitials = view.getStateInitialTo("UPDATE: ");
                Taxes chosenStateTax = service.getStateTax(stateInitials);
                Taxes updatingStateTax = view.getUpdateStateTaxInfo(chosenStateTax);
                service.updateStateTax(updatingStateTax);
                view.displayResultUpdateTax(updatingStateTax);
                hasErrors = false;
            } catch (Exception e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while(hasErrors);
    }

    private void removeStateTax() throws PersistenceException {
        view.displayBannerRemoveTax();
        this.displayAllStateTaxes();
        String stateInitial = view.getStateInitialTo("REMOVE: ");
        
        try {
            Taxes chosenStateTax = service.getStateTax(stateInitial);
            String userConfirm = view.getRemoveTaxConfirmation(chosenStateTax);
            
            if(userConfirm.equalsIgnoreCase("y")) {
                Taxes removedStateTax = service.removeStateTax(stateInitial);
                view.displayResultRemoveTax(removedStateTax);
            } else if(userConfirm.equalsIgnoreCase("n")) {
                view.waitForEnter("Please hit ENTER to continue.");
            } else {
                view.waitForEnter("Something went wrong. Please hit ENTER to continue.");
            }
        } catch(Exception e) {
            view.displayErrorMessage(e.getMessage());
        }
        
    }

}
