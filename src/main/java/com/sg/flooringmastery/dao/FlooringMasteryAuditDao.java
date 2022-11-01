/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.service.PersistenceException;
import java.time.LocalDate;

/**
 *
 * @author Daryl del Rosario
 */
public interface FlooringMasteryAuditDao {
    
    public void writeAuditLine(String msg) throws PersistenceException;
    public void writeAuditOrderLine(String procedure, LocalDate date, Orders order) throws PersistenceException;
    public void writeAuditProductLine(String procedure, String productType) throws PersistenceException;
    public void writeAuditTaxLine(String prodcedure, String stateInitial, String stateName) throws PersistenceException;
}
