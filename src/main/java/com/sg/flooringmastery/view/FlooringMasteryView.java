/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.view;

import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.model.Products;
import com.sg.flooringmastery.model.Taxes;
import com.sg.flooringmastery.service.DoesNotExistException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryView {
    
    private UserIO io;
    
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
    
    public static final String PRODUCT_COL_FORM = "%-15s%23s%29s\n";
    public static final String TAX_COL_FORM = "%-17s%-25s%11s\n";
    public static final String ORDERDATE_COL_FORM = "%-18s%18s\n";
    public static final String ORDERS_COL_FORM = "%-15s%-31s%15s\n";
    
    // UI Menu and Option Code
    public int printUIAndGetSelect() {
        io.print("=================================================");
        io.print("FINAL EXAM: FLOORING PROGRAM by DARYL DEL ROSARIO");
        io.print("=================================================");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");
        io.print("===========================");
        io.print("7. ACCESS TSG CORP DATABASE");
        io.print("===========================");
        
        return io.readInt("Please choose an option from above.", 1, 7);
    }
    
    // DISPLAYING AN ORDER: REQUIREMENTS - CODE and FUNCTION
    public void displayHeaderOrderDates() {
        System.out.printf(ORDERDATE_COL_FORM, "Order Date Code", "Date");
        io.print("=".repeat(36));
    }

    public void displayColumnOrderDates(String dateCode, String fullDate) {
        System.out.printf(ORDERDATE_COL_FORM, dateCode, fullDate);
    }
    
    public void displayHeaderOrders() {
        System.out.printf(ORDERS_COL_FORM, "Order Number", "Customer Name", "Total Cost");
        io.print("=".repeat(61));
    }
    
    public void displayColumnOrders(String orderNumber, String customerName, String totalCost) {
        System.out.printf(ORDERS_COL_FORM, orderNumber, customerName, totalCost);
    }
    
    // 1. Ask user for a date and display orders
    // 2. If no orders exist for that date - display error message and return to main menu
    public void displayBannerDisplayAllOrderDates() {
        this.displayBanner("DISPLAYING AVAILABLE ORDER DATES");
    }
    
    public void displayOrderDateList(List<LocalDate> orderdateList) {
        try {
            this.displayHeaderOrderDates();
            for(LocalDate o : orderdateList) {
                String dateCode = o.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                String fullDate = o.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
                this.displayColumnOrderDates(dateCode, fullDate);
            }
        } catch(Exception e) {
            this.displayErrorMessage("No orders available.");
        }
    }
    
    public LocalDate getOrderDateTo(List<LocalDate> orderdateList, String option) {
        LocalDate dateCodeLD = null;
        LocalDate returnCode = null;
        boolean hasErrors = false;
        
        do{
        try {
            io.print("");
            String dateCodeStr = io.readString("Please enter Order Date Code to " + option + " [MM-DD-YYYY]: ");
            dateCodeLD = this.convertDateCodeToLocalDate(dateCodeStr);
            
                for(LocalDate o : orderdateList) {
                    if(dateCodeLD.equals(o)) {
                        returnCode = dateCodeLD;
                        return returnCode;
                    }
                }
            return null;

            } catch(Exception e) {
                this.displayErrorMessage("Invalid format. Please try again.");
                hasErrors = true;
        } 
        }while (hasErrors);
        
        return returnCode;
    }
    
    public void displayBannerAllOrders(LocalDate date) {
        String dateFull = this.convertLocalDateToFull(date);
        String dateFormat = this.convertLocalDateToExample(date);
        this.displayBanner("DISPLAYING ORDERS FOR: " + dateFull.toUpperCase() + " [" + dateFormat + "]");
    }
    
    public void displayAllOrdersForDate(List<Orders> orderList) {
        this.displayHeaderOrders();
        for(Orders o : orderList) {
            String orderNumber = String.valueOf(o.getOrderNumber());
            String customerName = o.getCustomerName();
            String totalCost = "$ " + o.getTotal().toString();
            
            this.displayColumnOrders(orderNumber, customerName, totalCost);
        }
    }
    
    // ADDING AN ORDER: REQUIREMENTS - CODE and FUNCTION
    // 1. Order Date - must be in the future
    public LocalDate getNewOrderDate() {
        String msg = "Must be a Future Date [MM-DD-YYYY]: ";
        String option = "ADD";
        LocalDate orderFutureDate = this.getFutureDateTo(option, msg);
        return orderFutureDate;
    }
    
    // 2. Customer Name - may not be blank, allowed [a-z][0-9][.][,]
    public String getNewOrderCustomerName() {
        String customerName = "";
        String allowedChar = "^[\\w .,]+";
        Boolean hasErrors = false;
        
        do {
            this.displayBanner("Please enter Customer Name: ");
            String userCustomerName = io.readString("Characters allowed [a-z A-Z][0-9][.][,] ");
            if(!userCustomerName.matches(allowedChar)
                    || userCustomerName == null
                    || userCustomerName.trim().length() == 0){
                this.displayErrorMessage("Invalid input. Please try again.");
                hasErrors = true;
            } else {
                customerName = userCustomerName;
                hasErrors = false;
            }
        } while(hasErrors);
        return customerName;
    }
    
    // 3. State Initials - constructor statetaxList via Controller
    public Taxes getNewOrderStateTax(List<Taxes> statetaxList) {
        // Create list of available stateInitials
        List<String> stateInitialsList = statetaxList.stream()
                .map((s) -> s.getStateInitials())
                .collect(Collectors.toList());

        // Get userStateInitials and verify in list
        String stateInitials = "";
        Boolean hasErrors = false;
        do {
            this.displayBanner("These are the current available states we sell to: ");
            this.displayTaxList(statetaxList);
            String userStateInitials = this.getStateInitialTo("BUY FROM: ");
            if(stateInitialsList.contains(userStateInitials)) {
                stateInitials = userStateInitials;
                hasErrors = false;
            } else {
                this.displayErrorMessage("Invalid State Initials. Please try again.");
                hasErrors = true;
            }
        } while(hasErrors);
        
        // Return chosen state tax
        Taxes chosenStatetax = new Taxes();
        for(Taxes t : statetaxList) {
            if(stateInitials.equals(t.getStateInitials())) {
                chosenStatetax = t;
            }
        }
        
        return chosenStatetax;
    }
    
    // 4. Product type - constructor productList via Controller
    public Products getNewOrderProduct(List<Products> productList) {
        // Create list of available product types
        List<String> productTypeList = productList.stream()
                .map((p) -> p.getProductType())
                .collect(Collectors.toList());
        
        // Get userProdutType and verify in list
        String productType = "";
        Boolean hasErrors = false;
        do {
            this.displayBanner("These are the current products we have available: ");
            this.displayProductList(productList);
            String userProductType = this.getProductTypeTo("BUY: ");
            if(productTypeList.contains(userProductType)) {
                productType = userProductType;
                hasErrors = false;
            } else {
                this.displayErrorMessage("Invalid Product Type. Please try again.");
                hasErrors = true;
            }
        } while(hasErrors);
        
        // Return chosen product
        Products chosenProduct = new Products();
        for(Products p : productList) {
            if(productType.equals(p.getProductType())) {
                chosenProduct = p;
            }
        }
        
        return chosenProduct;
    }
    
    // 5. Area - must be positive and greater than 100 square foot
    public BigDecimal getNewOrderArea() {
        
        BigDecimal area = new BigDecimal("0");
        Boolean hasErrors = false;
        do {
            this.displayBanner("Please enter Area of material required: ");
            BigDecimal userArea = io.readBigDecimal("Must be a minimum of 100 sq ft.");
            if(userArea.compareTo(new BigDecimal("100")) >= 0) {
                area = userArea.setScale(2, RoundingMode.HALF_UP);
                hasErrors = false;
            } else {
                this.displayErrorMessage("Not a valid area input. Please try again.");
                hasErrors = true;
            }
        } while(hasErrors);
        
        return area;
    }
    
    public void displayBannerAddOrder() {
        this.displayBanner("ADDING A NEW ORDER");
    }
    
    public Orders getNewOrderUserInfo(List<Taxes> statetaxList, List<Products> productList) {
        String customerName = this.getNewOrderCustomerName();
        Taxes chosenStateTax = this.getNewOrderStateTax(statetaxList);
        Products chosenProduct = this.getNewOrderProduct(productList);
        BigDecimal area = this.getNewOrderArea();
        
        Orders newOrder = new Orders(customerName, chosenStateTax, chosenProduct, area);
        return newOrder;
    }
    
    public boolean displayNewOrderInfoAndConfirm(Orders newOrder, LocalDate orderFutureDate, String msgToConfirm, String option) {
        boolean toConfirm = false;
        
        this.displayBanner("PLEASE REVIEW YOUR " + option + " and CONFIRM BELOW");
        io.print("Customer Name: " + newOrder.getCustomerName());
        io.print("Purchasing State: " + newOrder.getStateInitials() + " - " + newOrder.getStateName());
        io.print("Product Purchased: " + newOrder.getProductType());
        io.print("Area of Material: " + newOrder.getArea() + " square feet");
        io.print("");
        io.print("Material Cost: $" + newOrder.getMaterialCost() + " @ $" + newOrder.getCostPerSquareFoot() + " cost per square feet");
        io.print("Labor Cost: $" + newOrder.getLaborCost() + " @ $" + newOrder.getLaborCostPerSquareFoot() + " labor cost per square feet");
        io.print("Net Cost: $" + newOrder.getMaterialCost().add(newOrder.getLaborCost()));
        io.print("");
        io.print("Tax: $" + newOrder.getTax() + " @ " + newOrder.getTaxRate() + "% state tax rate");
        io.print("Total Cost: $" + newOrder.getTotal());
        
        String fullDate = this.convertLocalDateToFull(orderFutureDate);
        String exampleDate = this.convertLocalDateToExample(orderFutureDate);
        
        boolean hasErrors = false;
        this.displayBanner("This order will be processed on " + fullDate + " [" + exampleDate + "]");
        
        do {
            String userChoice = io.readString(msgToConfirm);
            if(userChoice.equalsIgnoreCase("y")) {
                toConfirm = true;
                return toConfirm;
            } else if(userChoice.equalsIgnoreCase("n")) {
                toConfirm = false;
                return toConfirm;
            } else {
                this.displayErrorMessage("Invalid input. Please try again.");
                hasErrors = true;
            }
        } while(hasErrors);
        
        return toConfirm;
    }
    
    public void displayBannerAddOrderSuccess() {
        this.displayBanner("Order successfully created.");
        io.readString("Please hit ENTER to continue.");
    }
    
    // EDITING AN ORDER: REQUIREMENTS - CODE and FUNCTION
    public void displayBannerEditOrder() {
        this.displayBanner("EDITING AN ORDER");
    }
    
    // 1. Ask user for date and order number
    public int getOrderNumber(List<Orders> orderList, String option) {
        int customerNumber = 0;
        List<Integer> orderIds = orderList.stream()
                .map((id) -> id.getOrderNumber())
                .collect(Collectors.toList());
        boolean hasErrors = false;
        
        do {
            int userChoice = io.readInt("\nPlease enter Order Number to " + option);
            if(orderIds.contains(userChoice)) {
                customerNumber = userChoice;
                return customerNumber;
            } else {
                this.displayErrorMessage("That Order Number does not exist in database. Please try again.");
                hasErrors = true;
            }
        } while(hasErrors);
        
        return customerNumber;
    }
    
    // 2. If order exists for that date - ask user for each piece but display existing data
    // 3. If user enters something new - replace data
    // 3a. If user hits ENTER without entering data - leave existing data in place
    // 4. Only certain data allowed to change: customerName, state, productType, area
    // 5. Order must be RE-CALCULATED 
    // 5a. orderDate MAY NOT BE CHANGED
    public Orders getEditOrderInfo(int orderNumber, Orders orderToEdit, List<Taxes> taxList, List<Products> productList) {
        String oldCustomerName = orderToEdit.getCustomerName();
        String oldStateInitials = orderToEdit.getStateInitials();
        String oldStateName = orderToEdit.getStateName();
        String oldProductType = orderToEdit.getProductType();
        BigDecimal oldArea = orderToEdit.getArea();
        
        Taxes editedStatetax = new Taxes();
        Products editedProduct = new Products();
        
        String editCustomerName = "";
        String editStateInitials = "";
        String editStateName = "";
        String editProductType = "";
        BigDecimal editArea = new BigDecimal("0");
        
        editCustomerName = this.getEditCustomerName(oldCustomerName);
        
        editedStatetax = this.getEditStateTax(taxList, oldStateInitials, oldStateName);
        editStateInitials = editedStatetax.getStateInitials();
        editStateName = editedStatetax.getStateName();
        
        editedProduct = this.getEditProduct(productList, oldProductType);
        editProductType = editedProduct.getProductType();
        
        editArea =this.getEditArea(oldArea);
        
        Orders editedOrder = new Orders(editCustomerName, editedStatetax, editedProduct, editArea);
        editedOrder.setOrderNumber(orderNumber);

        return editedOrder;
    }
    
    // 6. After query - display new order info and prompt for saving
    // 6a. If yes - replace data, return menu. If no - DO NOT SAVE, return menu
    public Orders displayEditInfoAndConfirm(Orders oldOrder, Orders editedOrder, LocalDate chosenDate) {
        String msgToConfirm = "Save this update? (y / n)";
        String option = "UPDATES";
        boolean toSave = false;
        
        toSave = this.displayNewOrderInfoAndConfirm(editedOrder, chosenDate, msgToConfirm, option);
        if(toSave) {
            this.displayBanner("SAVING UPDATED ORDER");
            io.print("--- loading ---");
            return editedOrder;
        } else {
            this.displayBanner("NO CHANGES WILL BE MADE");
            io.print("--- deleting temporary files ---");
            return oldOrder;
        }
    }
    
    // SUPPORTING FUNCTION FOR EDIT
    // GETTING INFO for customerName, stateInitials - stateName, productType, area
    public String getEditCustomerName(String oldCustomerName) {
        String editedCustomerName = "";
        String allowedChar = "^[\\w .,]+";
        boolean hasErrors = false;
        
        do {
            this.displayBanner("ENTER CUSTOMER NAME [CURRENT: " + oldCustomerName + "]");
            editedCustomerName = io.readString("Leave blank and hit ENTER to keep current: ");
            if(editedCustomerName == null
                    || editedCustomerName.trim().length() == 0){
                editedCustomerName = oldCustomerName;
                return editedCustomerName;
            } else if(!editedCustomerName.matches(allowedChar)) {
                this.displayErrorMessage("Invalid characters. Must only be [a-z A-Z][0-9][.][,]. Please try again.");
                hasErrors = true;
            } else {
                return editedCustomerName;
            }
        } while(hasErrors);
        return editedCustomerName;
    }
    
    public Taxes getEditStateTax(List<Taxes> statetaxList, String oldStateInitials, String oldStateName) {
        List<String> stateInitialsList = statetaxList.stream()
                .map((s) -> s.getStateInitials())
                .collect(Collectors.toList());
        
        String userStateInitials = "";
        String checkStateInitials = "";
        String editedStateInitials = "";
        boolean hasErrors = false;
        
        do {
            this.displayBanner("UPDATING ORDER: STATE TAX ");
            this.displayTaxList(statetaxList);
            this.displayBanner("ENTER STATE INITIAL [CURRENT: " + oldStateInitials + " - " + oldStateName + "]");
            userStateInitials = io.readString("Leave blank and hit ENTER to keep current: ");
            checkStateInitials = this.allCaps(userStateInitials);
            
            if(checkStateInitials == null
                    || checkStateInitials.trim().length() == 0) {
                editedStateInitials = oldStateInitials;
                hasErrors = false;
            } else if(stateInitialsList.contains(checkStateInitials)) {
                editedStateInitials = checkStateInitials;
                hasErrors = false;
            } else {
                this.displayErrorMessage("Invalid State Initials. Please try again.");
                hasErrors = true;
            }
        } while(hasErrors);
        
        Taxes editedStateTax = new Taxes();
        for(Taxes t : statetaxList) {
            if(t.getStateInitials().equals(editedStateInitials)) {
                editedStateTax = t;
            }
        }
        return editedStateTax;
    }
    
    public Products getEditProduct(List<Products> productList, String oldProductType) {
        List<String> productTypeList = productList.stream()
                .map((p) -> p.getProductType())
                .collect(Collectors.toList());
        
        String userProductType = "";
        String checkProductType = "";
        String editedProductType = "";
        boolean hasErrors = false;
        
        do {
            this.displayBanner("UPDATING ORDER: PRODUCT");
            this.displayProductList(productList);
            this.displayBanner("ENTER PRODUCT TYPE [CURRENT: " + oldProductType + "]");
            userProductType = io.readString("Leave blank and hit ENTER to keep current: ");
            checkProductType = this.capitalizeThis(userProductType);
            if(checkProductType == null
                    || checkProductType.trim().length() == 0) {
                editedProductType = oldProductType;
                hasErrors = false;
            } else if(productTypeList.contains(checkProductType)) {
                editedProductType = checkProductType;
                hasErrors = false;
            } else {
                this.displayErrorMessage("Invalid Product Type. Please try again.");
                hasErrors = true;
            }
        } while(hasErrors);
        
        Products editedProduct = new Products();
        for(Products p : productList) {
            if(p.getProductType().equals(editedProductType)) {
                editedProduct = p;
            }
        }
        return editedProduct;
    }
    
    public BigDecimal getEditArea(BigDecimal oldArea) {
        BigDecimal editedArea = new BigDecimal("0");
        BigDecimal checkAreaBD = new BigDecimal("0");
        String checkArea = "";
        boolean hasErrors = false;
        
        do {
            this.displayBanner("ENTER AREA OF MATERIAL REQUIRED [CURRENT: " + oldArea + " square feet]");
            checkArea = io.readString("Leave blank and hit ENTER to keep current: ");
            if(checkArea == null
                    || checkArea.trim().length() == 0) {
                editedArea = oldArea;
                return editedArea;
            }
               
            try {
                checkAreaBD = new BigDecimal(checkArea);
                if(checkAreaBD.compareTo(new BigDecimal("100")) >= 0) {
                    editedArea = checkAreaBD.setScale(2, RoundingMode.HALF_UP);
                    return editedArea;
                } else {
                    this.displayErrorMessage("Must be a minimum of 100 sq ft. Please try again.");
                    hasErrors = true;
                }

            } catch (Exception e) {
                this.displayErrorMessage("Not a valid area input. Please try again.");
                hasErrors = true;
            }
        } while(hasErrors); 
        return editedArea;
    }
    
    // REMOVING AN ORDER: REQUIREMENTS - CODE and FUNCTION
    public void displayBannerRemoveOrder() {
        this.displayBanner("REMOVING AN ORDER");
    }
    // 1. Ask for date and order number
    // Task above will be re-using this.getOrderDate() and this.getOrderNumber() in controller
    
    // 2. If exists - display order information
    // 2a. Prompt to confirm
    // 3. If yes - remove from list
    public boolean displayRemoveInfoAndConfirm(Orders orderToRemove, LocalDate chosenDate) {
        String msgToConfirm = "Remove this order? (y / n)";
        String option = "ORDER TO REMOVE";
        boolean toRemove = false;
        
        toRemove = this.displayNewOrderInfoAndConfirm(orderToRemove, chosenDate, msgToConfirm, option);
        if(toRemove) {
            this.displayBanner("REMOVING SELECTED ORDER");
            io.print("--- loading ---");
            return true;
        } else {
            this.displayBanner("NO CHANGES WILL BE MADE");
            io.print("--- restoring files ---");
            return false;
        }
    }
    
    
    // EXPORTING AN ORDER: REQUIREMENTS - CODE and FUNCTION
    public void displayBannerExportData() {
        this.displayBanner("EXPORTING DATA");
    }
    
    public void displayResultExportData() {
        this.printMessage("Data fully backed up and exported to Backup/dataexport.txt");
    }
    
    // ACCESSING TSG DATABASE: CODE and FUNCTIOn
    public boolean confirmContactAdmin() {
        boolean contactAdmin = false;
        boolean hasErrors = false;
        String userChoice = "";
        
        io.print("*");
        io.print("**");
        io.print("***");
        this.displayBanner("ADMIN ACCESS REQUIRED");
        
        do {
            userChoice = io.readString("Would you like to contact admin (y / n)");
            if(userChoice.equalsIgnoreCase("y")) {
                contactAdmin = true;
                return contactAdmin;
            } else if(userChoice.equalsIgnoreCase("n")) {
                contactAdmin = false;
                return contactAdmin;
            } else {
                io.print("Invalid input. Please try again.");
                hasErrors = true;
            }
        } while(hasErrors);
        
        return contactAdmin;
    }
    
    public boolean displayResultContactAdmin(boolean contactAdmin) {
        if(contactAdmin) {
            io.print("**");
            io.print("*");
            this.displayBanner("CONTACTING ADMIN");
            return true;
        } else {
            io.print("***");
            io.print("**");
            io.print("*");
            this.displayBanner("RETURNING TO MAIN MENU");
            return false;
        }
    }
    
        public String getVerificationCode() {
        io.print("Hello, this is Floor Master Admin.");
        io.print("Verification code is required and is with Daryl del Rosario.");
        return io.readString("Please ask and enter the code now: ");
    }
    
    public void displayBannerAccessTSGCorpMenuSuccess() {
        io.print("*");
        this.displayBanner("ACCESSING TSG CORP DATABASE");
        io.readString("Please hit ENTER to continue.");
        io.print("*");
    }
    
    public void displayBannerAccessTSGCorpMenuFail() {
        io.print("*");
        this.displayBanner("INCORRECT VERIFCATION CODE: ACCESS DENIED");
        io.print("*");
        io.print("Thank you, try again next time.");
        io.readString("Please hit ENTER to return to menu.");
    }
    
    // TSG Database Menu and Option Code
    public int printTSGDatabaseAndGetSelect() {
        io.print("=====================================");
        io.print("TSG CORP DATABASE: PRODUCTS AND TAXES");
        io.print("=====================================");
        io.print("1. Access TSG Corp Products");
        io.print("2. Access TSG Corp Taxes");
        io.print("3. Quit");
        io.print("================================");
        io.print("4. RETURN TO FLOORING PROGRAM UI");
        io.print("================================");
        
        return io.readInt("Please choose an option from above.", 1, 4);
    }
    
    // TSG Product Menu and Option Code
    public void displayBannerTSGProductWelcome() {
        this.displayBanner("TSG CORP: PRODUCT MENU");
    }
    
    public int printTSGProductAndGetSelect() {
        io.print("");
        io.print("1. Add Product");
        io.print("2. Remove Product");
        io.print("3. Update Product Prices");
        io.print("4. Access TSG Corp Taxes");
        io.print("5. Quit");
        io.print("================================");
        io.print("6. RETURN TO FLOORING PROGRAM UI");
        io.print("================================");
        
        return io.readInt("Please choose an option from above.", 1, 6);
    }
    
    public void displayBannerNoProducts() {
        io.print("There are no products in the database !!! Please add a product.");
    }
    
    public void displayHeaderProduct() {
        System.out.printf(PRODUCT_COL_FORM, "Product Type", "Cost Per Square Foot", "Labor Cost Per Square Foot");
        io.print("=".repeat(67));
    }
    
    public void displayColumnProduct(String productType, String cost, String laborCost) {
        System.out.printf(PRODUCT_COL_FORM, productType, cost, laborCost);
    }
    
    public void displayProductList(List<Products> productList) {
        this.displayHeaderProduct();
        for(Products currentProduct : productList) {
            String productType = currentProduct.getProductType();
            String cost = "$ " + currentProduct.getCostPerSquareFoot().toString();
            String laborCost = "$ " + currentProduct.getLaborCostPerSquareFoot().toString();
            
            this.displayColumnProduct(productType, cost, laborCost);
        }
    }
    
    public void displayBannerAddProduct() {
        this.displayBanner("ADDING NEW PRODUCT");
    }
    
    public Products getNewProductInfo() {
        String userProductType = io.readString("Please enter Product Type to ADD: ");
        String productType = this.capitalizeThis(userProductType);
        BigDecimal cost = io.readBigDecimal("Please enter Cost Per Square Foot: ");
        BigDecimal labor = io.readBigDecimal("Please enter Labor Cost Per Square Foot: ");
        
        Products newProduct = new Products(productType, cost, labor);
        return newProduct;
    }
    
    public void displayBannerAddProductSuccess(String productType) {
        this.displayBanner(productType.toUpperCase() + " SUCCESSFULLY ADDED");
        io.readString("Please hit ENTER to continue.");
    }
    
    public void displayBannerUpdateProduct() {
        this.displayBanner("UPDATING PRODUCT");
    }
    
    public Products getUpdateProductInfo(Products product) throws DoesNotExistException {
        String productType = product.getProductType();
        String oldCost = "$ " + product.getCostPerSquareFoot().toString();
        String oldLaborCost = "$ " + product.getLaborCostPerSquareFoot().toString();

        this.displayBanner("UPDATING: " + productType.toUpperCase());

        io.print("CURRENT COST PER SQUARE FOOT: " + oldCost);
        BigDecimal userCost = io.readBigDecimal("Enter Cost Per Square Foot: ");
        BigDecimal newCost = new BigDecimal("0");
        if(userCost == null) {
            newCost = product.getCostPerSquareFoot();
        } else {
            newCost = userCost;
        }

        io.print("");
        io.print("CURRENT LABOR COST PER SQUARE FOOT: " + oldLaborCost);
        BigDecimal userLaborCost = io.readBigDecimal("Enter Labor Cost Per Square Foot: ");
        BigDecimal newLaborCost = new BigDecimal("0");
        if(userLaborCost == null) {
            newLaborCost = product.getLaborCostPerSquareFoot();
        } else {
            newLaborCost = userLaborCost;
        }

        Products updatedProduct = new Products(productType, newCost, newLaborCost);
        return updatedProduct;

    }
    
    public void displayResultUpdateProduct(Products productRecord) throws DoesNotExistException {
        if(productRecord != null) {
            this.displayBanner(productRecord.getProductType().toUpperCase() + " SUCCESSFULLY UPDATED.");
        } else {
            throw new DoesNotExistException(this.returnBanner("PRODUCT DOES NOT EXIST IN DATABASE"));
        }
        io.readString("Please hit ENTER to continue.");
    }
    
    public void displayBannerRemoveProduct() {
        this.displayBanner("REMOVING PRODUCT");
    }
    
    public String getRemoveProductConfirmation(Products product) {
        String productType = product.getProductType();
        String userChoice = "x";
        
        do {
            this.displayBanner("ARE YOU SURE YOU WANT TO REMOVE: " + productType + "?");
            userChoice = io.readString("Please confirm (y / n): ");
            if(userChoice.equalsIgnoreCase("y")) {
                this.displayBanner("--- loading ---");
                userChoice = "y";
                return userChoice;
            } else if(userChoice.equalsIgnoreCase("n")) {
                this.displayBanner(productType + " HAS NOT BEEN REMOVED.");
                userChoice = "n";
                return userChoice;
            } else {
                io.print("Not a valid option. Try again.");
                userChoice = "x";
            }
        } while (userChoice.equalsIgnoreCase("x"));
        
        return userChoice;
    }
    
    public void displayResultRemoveProduct(Products productRecord) {
        if(productRecord != null) {
            io.print(productRecord.getProductType() + " WAS SUCCESSFULLY REMOVED");
        } else {
            this.displayBanner("PRODUCT DOES NOT EXIST IN DATABASE");
        }
        io.readString("Please hit ENTER to continue.");
    }
    
    public String getProductTypeTo(String option) {
        io.print("");
        String userInput = io.readString("Please enter Product Type to " + option);
        String inDatabase = this.capitalizeThis(userInput);
        return inDatabase;
    }
    
    // TSG Tax Menu and Option Code
    public void displayBannerTSGTaxWelcome() {
        this.displayBanner("TSG CORP: TAX MENU");
    }
    
    public int printTSGTaxAndGetSelect() {
        io.print("");
        io.print("1. Add State Tax");
        io.print("2. Remove State Tax");
        io.print("3. Update State Tax Rate");
        io.print("4. Access TSG Corp Products");
        io.print("5. Quit");
        io.print("================================");
        io.print("6. RETURN TO FLOORING PROGRAM UI");
        io.print("================================");
        
        return io.readInt("Please choose an option from above.", 1, 6);
    }
      
    public void displayBannerNoTaxes() {
        io.print("There are no taxes in the database !!! Please add a state tax.");
    }

    public void displayHeaderTaxes() {
        System.out.printf(TAX_COL_FORM, "State Initial", "State Name", "Tax Rate");
        io.print("=".repeat(53));
    }

    public void displayColumnTaxes(String stateInitial, String stateName, String taxRate) {
        System.out.printf(TAX_COL_FORM, stateInitial, stateName, taxRate);
    }

    public void displayTaxList(List<Taxes> stateTaxList) {
        this.displayHeaderTaxes();
        for(Taxes currentStateTax : stateTaxList) {
            String stateInitial = currentStateTax.getStateInitials();
            String stateName = currentStateTax.getStateName();
            String taxRate = currentStateTax.getTaxRate().toString() + " %";
            
            this.displayColumnTaxes(stateInitial, stateName, taxRate);
        }
    }
    
    public void displayBannerAddStateTax() {
        this.displayBanner("ADDING NEW STATE TAX");
    }
    
    public Taxes getNewStateTaxInfo() {   
        String userStateInitial = io.readString("Please enter the STATE INITIALS to ADD: ");
        String stateInitial = this.allCaps(userStateInitial);
        String userStateName = io.readString("Please enter the STATE NAME for " + stateInitial + ": ");
        String stateName = this.capitalizeThis(userStateName);
        BigDecimal taxRate = io.readBigDecimal("Please enter the Tax Rate: ");
        
        Taxes newStateTax = new Taxes(stateInitial, stateName, taxRate);
        return newStateTax;
    }

    public void displayBannerAddStateTaxSuccess(String stateInitial, String stateName) {
        this.displayBanner(stateInitial + " - " + stateName.toUpperCase() + " SUCCESSFULLY ADDED");
        io.readString("Please hit ENTER to continue.");
    }
    
    public void displayBannerUpdateTax() {
        this.displayBanner("UPDATING STATE TAX");
    }
    
    public Taxes getUpdateStateTaxInfo(Taxes stateTax) throws DoesNotExistException {
        String stateInitials = stateTax.getStateInitials();
        String stateName = stateTax.getStateName();
        String oldTaxRate = stateTax.getTaxRate().toString();
        
        this.displayBanner("UPDATING: " + stateInitials + " - " + stateName);
        
        io.print("Current TAX RATE is: " + oldTaxRate + " % ");
        BigDecimal userTaxRate = io.readBigDecimal("Enter TAX RATE: ");
        BigDecimal newTaxRate = new BigDecimal("0");
        if(userTaxRate == null) {
            newTaxRate = new BigDecimal(oldTaxRate);
        } else {
            newTaxRate = userTaxRate;
        }
        
        Taxes updatedStateTax = new Taxes(stateInitials, stateName, newTaxRate);
        return updatedStateTax;
    }

    public void displayResultUpdateTax(Taxes taxRecord) throws DoesNotExistException {
        if(taxRecord != null) {
            this.displayBanner(taxRecord.getStateInitials() + " - " + taxRecord.getStateName() + " SUCCESSFULLY UPDATED.");
        } else {
            throw new DoesNotExistException(this.returnBanner("STATE TAX DOES NOT EXIST IN DATABASE"));
        }
        io.readString("Please hit ENTER to continue.");
    }
    public void displayBannerRemoveTax() {
        this.displayBanner("REMOVING STATE TAX");
    }
    
    public String getRemoveTaxConfirmation(Taxes stateTax) {
        String stateInitial = stateTax.getStateInitials();
        String stateName = stateTax.getStateName();
        String userChoice = "x";
        
        do {
            this.displayBanner("ARE YOU SURE YOU WANT TO REMOVE: " + stateInitial + " - " + stateName + "?");
            userChoice = io.readString("Please confirm (y / n): ");
            if(userChoice.equalsIgnoreCase("y")) {
                this.displayBanner("--- loading ---");
                userChoice = "y";
                return userChoice;
            } else if(userChoice.equalsIgnoreCase("n")) {
                this.displayBanner(stateInitial + " - " + stateName + " HAS NOT BEEN REMOVED.");
                userChoice = "n";
                return userChoice;
            } else {
                io.print("Not a valid option. Try again.");
                userChoice = "x";
            }
        } while (userChoice.equalsIgnoreCase("x"));
        
        return userChoice;
    }

    public void displayResultRemoveTax(Taxes taxRecord) {
        if(taxRecord != null) {
            io.print(taxRecord.getStateInitials() + " - " + taxRecord.getStateName() + " WAS SUCCESSFULLY REMOVED");
        } else {
            this.displayBanner("STATE TAX DOES NOT EXIST IN DATABASE");
        }
        io.readString("Please hit ENTER to continue.");
    }
    
    public String getStateInitialTo(String option) {
        io.print("");
        String userInput = io.readString("Please enter State Initial to " + option);
        String inDataBase = this.allCaps(userInput);
        return inDataBase;
    }
    
    // Supporting Function and Code
    public void displayBannerUnknownCommand() {
        this.displayBanner("!!! UNKNOWN COMMAND !!!");
    }
    
    public void displayExitMessage(String msg) {
        int stringLength = msg.length();
        io.print("=".repeat(stringLength));
        io.print(msg);
        io.print("=".repeat(stringLength));
    }
    
    public void displayErrorMessage(String msg) {
        this.displayBanner("--- ERROR ---");
        io.print(msg);
    }
    
    public void waitForEnter(String msg) {
        io.readString(msg);
    }
    
    public void printBanner(String msg) {
        this.displayBanner(msg);
    }
    
    public void printMessage(String msg) {
        io.print(msg);
    }
    
    private void displayBanner(String msg) {
        int stringLength = msg.length();
        io.print("=".repeat(stringLength));
        io.print(msg);
        io.print("=".repeat(stringLength));
    }
    
    private String returnBanner(String msg) {
        int stringLength = msg.length();
        
        String border = "=".repeat(stringLength);
        String banner = border + "\n" + msg + "\n" + border;
        
        return banner;
    }
    
    private String capitalizeThis(String phrase) {
        String forceLow = phrase.toLowerCase();
        String output = WordUtils.capitalize(forceLow);
        return output;
    }
    
    private String allCaps(String phrase) {
        return phrase.toUpperCase();
    }
    
    private LocalDate getFutureDateTo(String option, String msg) {
        LocalDate futureDateLD = null;
        LocalDate rightNow = LocalDate.now();
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        String rightNowFormatFull = this.convertLocalDateToFull(rightNow);
        String rightNowInputFormat = rightNow.format(inputFormat);
        
        Boolean hasErrors = false;
        
        do {
            try {
                io.print("Today is " + rightNowFormatFull + " [" + rightNowInputFormat + "]");
                this.displayBanner("Please enter Order Date to " + option);
                String futureDate = io.readString(msg);
                futureDateLD = LocalDate.parse(futureDate, inputFormat);
                    if(futureDateLD.isAfter(LocalDate.now())) {
                        return futureDateLD;
                    } else {
                        this.displayErrorMessage("Date must be in the future. Please try again.");
                        io.print("");
                        hasErrors = true;
                    }
            } catch(Exception e) {
                this.displayErrorMessage("Invalid format. Please try again.");
                io.print("");
                hasErrors = true;
            }
        } while(hasErrors);
        
        return futureDateLD;
    }
    
    private String convertLocalDateToExample(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String exampleFormat = date.format(formatter);
        return exampleFormat;
    }
    
    private LocalDate convertDateCodeToLocalDate(String dateCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate date = LocalDate.parse(dateCode, formatter);
        return date;
    }
    
    private String convertLocalDateToMed(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        String medFormat = date.format(formatter);
        return medFormat;
    }
    private String convertLocalDateToFull(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        String fullFormat = date.format(formatter);
        return fullFormat;
    }
    
    private String convertLocalDateToOrderTxt(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String orderTxt = date.format(formatter);
        return orderTxt;
    }
}
