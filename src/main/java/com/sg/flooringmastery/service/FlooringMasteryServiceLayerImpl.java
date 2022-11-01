/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryAuditDao;
import com.sg.flooringmastery.dao.FlooringMasteryOrdersDao;
import com.sg.flooringmastery.dao.FlooringMasteryProductsDao;
import com.sg.flooringmastery.dao.FlooringMasteryTaxDao;
import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.model.Products;
import com.sg.flooringmastery.model.Taxes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {
    
//    private FlooringMasteryOrdersDao daoOrder = new FlooringMasteryOrdersDaoFileImpl();
//    private FlooringMasteryProductsDao daoProduct = new FlooringMasteryProductsDaoFileImpl();
//    private FlooringMasteryTaxDao daoTax = new FlooringMasteryTaxDaoFileImpl();
//    private FlooringMasteryAuditDao daoAudit = new FlooringMasteryAuditDaoFileImpl();
//    
    private FlooringMasteryOrdersDao daoOrder;
    private FlooringMasteryProductsDao daoProduct;
    private FlooringMasteryTaxDao daoTax;
    private FlooringMasteryAuditDao daoAudit;
    
    public FlooringMasteryServiceLayerImpl(FlooringMasteryOrdersDao daoOrder,
            FlooringMasteryProductsDao daoProduct,
            FlooringMasteryTaxDao daoTax,
            FlooringMasteryAuditDao daoAudit) {
        
        this.daoOrder = daoOrder;
        this.daoProduct = daoProduct;
        this.daoTax = daoTax;
        this.daoAudit = daoAudit;
    }

    // Order Service Layer Code
    @Override
    public List<LocalDate> getAllOrderDates() throws PersistenceException {
        return daoOrder.getAllOrderDates();
    }

    @Override
    public List<Orders> getAllOrders(LocalDate date) throws PersistenceException, DoesNotExistException {
        return daoOrder.getAllOrders(date);
    }

    @Override
    public Orders getOrder(List<Orders> orderList, int orderNumber) throws PersistenceException, DoesNotExistException {
        return daoOrder.getOrder(orderList, orderNumber);
    }

    @Override
    public List<Orders> addOrder(LocalDate futureOrderdate, Orders order) throws PersistenceException, FutureDateException, DataValidationException {
        List<Orders> addedOrderlist = daoOrder.addOrder(futureOrderdate, order);
        daoAudit.writeAuditOrderLine("ADDED", futureOrderdate, order);
        return addedOrderlist;
    }

    @Override
    public List<Orders> editOrder(LocalDate date, List<Orders> orderList, Orders editedOrder) throws PersistenceException, DataValidationException {
        List<Orders> editedOrderlist = daoOrder.editOrder(date, orderList, editedOrder);
        daoAudit.writeAuditOrderLine("EDITED", date, editedOrder);
        return editedOrderlist;
    }

    @Override
    public List<Orders> removeOrder(LocalDate date, List<Orders> orderList, Orders order) throws PersistenceException {
        List<Orders> removedOrderlist = daoOrder.removeOrder(date, orderList, order);
        daoAudit.writeAuditOrderLine("REMOVED", date, order);
        return removedOrderlist;
    }
    
    @Override
    public void exportData() throws PersistenceException {
        daoOrder.exportData();
    }
    
    // Product Service Layer Code
    @Override
    public List<Products> getAllProducts() throws PersistenceException {
        return daoProduct.getAllProducts();
    }

    @Override
    public void createProduct(Products product) throws DuplicateEntryException, DataValidationException, PersistenceException {
        String thisProductType = product.getProductType();
        
        if(daoProduct.getProduct(thisProductType) != null) {
            throw new DuplicateEntryException("Could not create product. "
                    + thisProductType + " already exists.");
        }
        
        this.validateProductData(product);
        daoProduct.addProduct(thisProductType, product);
        daoAudit.writeAuditProductLine("ADDED", thisProductType);
    }

    @Override
    public void updateProduct(Products product) throws PersistenceException, DataValidationException {
        String thisProductType = product.getProductType();
     
        this.validateProductData(product);
        daoProduct.updateProduct(thisProductType, product);
        daoAudit.writeAuditProductLine("UPDATED", thisProductType);
        
    }

    @Override
    public Products removeProduct(String productType) throws PersistenceException {
        Products removedProduct = daoProduct.removeProduct(productType);
        daoAudit.writeAuditProductLine("REMOVED", productType);
        return removedProduct;
    }

    @Override
    public Products getProduct(String productType) throws PersistenceException, DoesNotExistException {
        Products checkProduct = daoProduct.getProduct(productType);
        
        if(productType == "" | productType.trim().length() == 0) {
            throw new DoesNotExistException("Invalid input. Please try again.");
        } else if(checkProduct == null) {
            throw new DoesNotExistException("Product: " + productType.toUpperCase() + " does not exist in database.");
        } else {
            return checkProduct;
        }
    }

    // Tax Service Layer Code
    @Override
    public List<Taxes> getAllStateTaxes() throws PersistenceException {
        return daoTax.getAllStateTaxes();
    }

    @Override
    public void createStateTax(Taxes tax) throws DuplicateEntryException, DataValidationException, PersistenceException {
        String thisStateInitials = tax.getStateInitials();
        String thisStateName = tax.getStateName();
        
        if(daoTax.getStateTax(thisStateInitials) != null) {
            throw new DuplicateEntryException("Could not add this state. " + thisStateInitials + " already exists.");
        }
        
        this.validateTaxData(tax);
        daoTax.addStateTax(thisStateInitials, tax);
        daoAudit.writeAuditTaxLine("ADDED", thisStateInitials, thisStateName);
    }

    @Override
    public void updateStateTax(Taxes tax) throws PersistenceException, DataValidationException {
        String thisStateInitials = tax.getStateInitials();
        
        this.validateTaxData(tax);
        daoTax.updateStateTax(thisStateInitials, tax);
        daoAudit.writeAuditTaxLine("UPDATED", thisStateInitials, tax.getStateName());
    }

    @Override
    public Taxes removeStateTax(String stateInitials) throws PersistenceException {
        Taxes removedStateTax = daoTax.removeStateTax(stateInitials);
        daoAudit.writeAuditTaxLine("REMOVED", stateInitials, removedStateTax.getStateName());
        return removedStateTax;
    }

    @Override
    public Taxes getStateTax(String stateInitials) throws PersistenceException, DoesNotExistException {
       Taxes checkStateTax = daoTax.getStateTax(stateInitials);
       
       if(stateInitials == "" | stateInitials.trim().length() == 0) {
           throw new DoesNotExistException("Invalid input. Please try again.");
       } else if (checkStateTax == null) {
           throw new DoesNotExistException("State Tax: " + stateInitials.toUpperCase() + " does not exist in database.");
       } else {
           return checkStateTax;
       }
    }
    
    // Support Code and Function
    private void validateProductData(Products product) throws DataValidationException {
        if(product.getProductType() == null
                || product.getProductType().trim().length() == 0) {
            throw new DataValidationException("All fields required. Please try again.");
        }
        
        if(product.getCostPerSquareFoot().compareTo(BigDecimal.ZERO) <= 0
                || product.getLaborCostPerSquareFoot().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DataValidationException("Prices must be greater than $0.00. Please try again.");
        }
    }
    
    private void validateTaxData(Taxes tax) throws DataValidationException {
        if(checkInteger(tax.getStateInitials())
                || checkInteger(tax.getStateName())) {
            throw new DataValidationException("STATE INITIALS and NAME must be NON-NUMERICAL. Please try again.");
        }
        
        if(tax.getStateInitials() == null
                || tax.getStateInitials().trim().length() == 0
                || tax.getStateName() == null
                || tax.getStateName().trim().length() == 0) {
            throw new DataValidationException("All fields required. Please try again.");
        }
            
        if(tax.getTaxRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DataValidationException("Tax rate percentages must be greater than %0.00. Please try again.");
        }
    }
    
    private boolean checkInteger(String phrase) throws DataValidationException {
        boolean isNumber = true;
        
        try {
            int stringToInt = Integer.parseInt(phrase);
            isNumber = true;
        } catch(Exception e) {
            isNumber = false;
        }
        
        return isNumber;
    }
}
