/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryTaxDao;
import com.sg.flooringmastery.model.Taxes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryTaxDaoStubImpl implements FlooringMasteryTaxDao {
    
    public Taxes statetaxOne;
    
    public FlooringMasteryTaxDaoStubImpl() {
        statetaxOne = new Taxes("TX", "Texas", new BigDecimal("1.11"));
    }
    
    public FlooringMasteryTaxDaoStubImpl(Taxes testStatetax) {
        this.statetaxOne = testStatetax;
    }

    @Override
    public List<Taxes> getAllStateTaxes() throws PersistenceException {
        List<Taxes> statetaxList = new ArrayList<>();
        statetaxList.add(statetaxOne);
        return statetaxList;
    }

    @Override
    public Taxes addStateTax(String stateInitial, Taxes stateTax) throws PersistenceException {
        if(stateInitial.equals(statetaxOne.getStateInitials())) {
            return statetaxOne;
        } else {
            return null;
        }
    }

    @Override
    public Taxes updateStateTax(String stateInitial, Taxes stateTax) throws PersistenceException {
        return stateTax;
    }

    @Override
    public Taxes removeStateTax(String stateInitial) throws PersistenceException {
        if(stateInitial.equals(statetaxOne.getStateInitials())) {
            return statetaxOne;
        } else {
            return null;
        }
    }

    @Override
    public Taxes getStateTax(String stateInitial) throws PersistenceException {
        if(stateInitial.equals(statetaxOne.getStateInitials())) {
            return statetaxOne;
        } else {
            return null;
        }
    }

}
