/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.service;

import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.model.Products;
import com.sg.flooringmastery.model.Taxes;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public interface FlooringMasteryServiceLayer {

    // Service Layer for Order Dao
    List<LocalDate> getAllOrderDates() throws PersistenceException;
    List<Orders> getAllOrders(LocalDate date) throws PersistenceException, DoesNotExistException;
    Orders getOrder(List<Orders> orderList, int orderNumber) throws PersistenceException, DoesNotExistException;
    List<Orders> addOrder(LocalDate futureOrderdate, Orders order) throws PersistenceException, FutureDateException, DataValidationException;
    List<Orders> editOrder(LocalDate date, List<Orders> orderList, Orders editedOrder) throws PersistenceException, DataValidationException;
    List<Orders> removeOrder(LocalDate date, List<Orders> orderList, Orders order) throws PersistenceException;
    void exportData() throws PersistenceException;
    
    // Service Layer for Product Dao
    List<Products> getAllProducts() throws PersistenceException;
    void createProduct(Products product) throws DuplicateEntryException, DataValidationException, PersistenceException;
    void updateProduct(Products product) throws PersistenceException, DataValidationException;
    Products removeProduct(String productType) throws PersistenceException;
    Products getProduct(String productType) throws PersistenceException, DoesNotExistException;
    
    // Service Layer for Tax Dao
    List<Taxes> getAllStateTaxes() throws PersistenceException;
    void createStateTax(Taxes tax) throws DuplicateEntryException, DataValidationException, PersistenceException;
    void updateStateTax(Taxes tax) throws PersistenceException, DataValidationException;
    Taxes removeStateTax(String stateInitials) throws PersistenceException;
    Taxes getStateTax(String stateInitials) throws PersistenceException, DoesNotExistException;
    
    
    
}
