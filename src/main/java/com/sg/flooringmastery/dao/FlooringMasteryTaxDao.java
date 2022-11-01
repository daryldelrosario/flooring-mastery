/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Taxes;
import com.sg.flooringmastery.service.PersistenceException;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public interface FlooringMasteryTaxDao {
    List<Taxes> getAllStateTaxes() throws PersistenceException;
    Taxes addStateTax(String stateInitial, Taxes stateTax) throws PersistenceException;
    Taxes updateStateTax(String stateInitial, Taxes stateTax) throws PersistenceException;
    Taxes removeStateTax(String stateInitial) throws PersistenceException;
    Taxes getStateTax(String stateInitial) throws PersistenceException;
}
