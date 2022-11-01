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
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public interface FlooringMasteryOrdersDao {
    List<LocalDate> getAllOrderDates() throws PersistenceException;
    List<Orders> getAllOrders(LocalDate date) throws PersistenceException, DoesNotExistException;
    Orders getOrder(List<Orders> orderList, int orderNumber) throws PersistenceException, DoesNotExistException;
    List<Orders> addOrder(LocalDate futureOrderdate, Orders order) throws PersistenceException, DataValidationException, FutureDateException;
    List<Orders> editOrder(LocalDate date, List<Orders> orderList, Orders editedOrder) throws PersistenceException, DataValidationException;
    List<Orders> removeOrder(LocalDate date, List<Orders> orderList, Orders order) throws PersistenceException;
    void exportData() throws PersistenceException;
    
}
