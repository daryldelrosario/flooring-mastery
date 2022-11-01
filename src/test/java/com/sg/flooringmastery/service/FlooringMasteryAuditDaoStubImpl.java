/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryAuditDao;
import com.sg.flooringmastery.model.Orders;
import java.time.LocalDate;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryAuditDaoStubImpl implements FlooringMasteryAuditDao {

    @Override
    public void writeAuditOrderLine(String procedure, LocalDate date, Orders order) throws PersistenceException {
        // Do Nothing
    }
    @Override
    public void writeAuditLine(String msg) throws PersistenceException {
        // Do Nothing
    }

    @Override
    public void writeAuditProductLine(String procedure, String productType) throws PersistenceException {
        // Do Nothing
    }

    @Override
    public void writeAuditTaxLine(String prodcedure, String stateInitial, String stateName) throws PersistenceException {
        // Do Nothing
    }
}
