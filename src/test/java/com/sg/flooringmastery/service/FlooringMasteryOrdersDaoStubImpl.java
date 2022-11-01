/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryOrdersDao;
import com.sg.flooringmastery.model.Orders;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryOrdersDaoStubImpl implements FlooringMasteryOrdersDao {

    public Orders orderOne;
    
    public FlooringMasteryOrdersDaoStubImpl() {
        int orderNumberOne = 1;
        String customerNameOne = "One";
        String stateInitialsOne = "TX";
        String stateNameOne = "Texas";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        String productTypeOne = "Laminate";
        BigDecimal areaOne = new BigDecimal("111");
        BigDecimal costPerSquareFootOne = new BigDecimal("1.11");
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("11.1");
        
        orderOne = new Orders(orderNumberOne, customerNameOne, stateInitialsOne, stateNameOne, 
                taxRateOne, productTypeOne, areaOne, 
                costPerSquareFootOne, laborCostPerSquareFootOne);
    }
    
    public FlooringMasteryOrdersDaoStubImpl(Orders testOrder) {
        this.orderOne = testOrder;
    }
    
    @Override
    public List<LocalDate> getAllOrderDates() throws PersistenceException{
        List<LocalDate> datesList = new ArrayList<>();
        datesList.add(LocalDate.now());
        return datesList;
    }

    @Override
    public List<Orders> getAllOrders(LocalDate date) throws PersistenceException, DoesNotExistException {
        List<Orders> allOrders = new ArrayList<>();
        if(date.equals(LocalDate.now())) {
            allOrders.add(this.orderOne);
            return allOrders;
        } else {
            throw new DoesNotExistException("");
        }
    }

    @Override
    public Orders getOrder(List<Orders> orderList, int orderNumber) throws PersistenceException, DoesNotExistException {
        if(orderNumber == orderOne.getOrderNumber()) {
            return this.orderOne;
        } else if (orderNumber != orderOne.getOrderNumber()){
            throw new DoesNotExistException("");
        } else {
            return null;
        }
    }

    @Override
    public List<Orders> addOrder(LocalDate futureOrderdate, Orders order) throws PersistenceException, FutureDateException, DataValidationException {
        List<Orders> addedOrderList = new ArrayList<>();
        if(futureOrderdate.isEqual(LocalDate.now()) || futureOrderdate.isBefore(LocalDate.now())){
            throw new FutureDateException("");
        } else if(order.getCustomerName().equalsIgnoreCase("One!")){
            throw new DataValidationException("");
        } else if(order.equals(this.orderOne)) {
            addedOrderList.add(order);
            return addedOrderList;
        } else {
            return null;
        }
    }

    @Override
    public List<Orders> editOrder(LocalDate date, List<Orders> orderList, Orders editedOrder) throws PersistenceException, DataValidationException {
        List<Orders> editedOrderList = new ArrayList<>();
        if(editedOrder.getCustomerName().equalsIgnoreCase("One!")) {
            throw new DataValidationException("");
        } else if(editedOrder.equals(this.orderOne)) {
            editedOrderList.add(editedOrder);
            return editedOrderList;
        } else {
            return null;
        }
    }

    @Override
    public List<Orders> removeOrder(LocalDate date, List<Orders> orderList, Orders order) throws PersistenceException {
        List<Orders> currentOrderlist = new ArrayList<>();
        currentOrderlist.add(this.orderOne);
        
        if(order.equals(this.orderOne)) {
            currentOrderlist.remove(order);
            return currentOrderlist;
        } else if(!order.equals(this.orderOne)){
            throw new PersistenceException("");
        } else {
            return null;
        }
    }
    
    @Override
    public void exportData() throws PersistenceException {
        // Do nothing
    }

}
