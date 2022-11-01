/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Taxes;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryTaxDaoFileImplTest {
    
    FlooringMasteryTaxDao testDaoTax;
    
    public FlooringMasteryTaxDaoFileImplTest() {
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        String testFile = "Test/TestData/testTaxes.txt";
        new FileWriter(testFile);
        testDaoTax = new FlooringMasteryTaxDaoFileImpl(testFile);
    }

    @Test
    public void testAddGetStateTax() throws Exception {
        System.out.println("test Tax Dao: Add and Get State Tax");
        // ARRANGE
        String stateInitials = "ON";
        String stateName = "Ontario";
        BigDecimal taxRate = new BigDecimal("1.11");
        Taxes stateTaxOne = new Taxes(stateInitials, stateName, taxRate);
        
        // ACT
        testDaoTax.addStateTax(stateInitials, stateTaxOne);
        Taxes retrievedStateTax = testDaoTax.getStateTax(stateInitials);
        
        // ASSERT
        assertEquals(stateTaxOne.getStateInitials(), retrievedStateTax.getStateInitials(), "Checking State Initials.");
        assertEquals(stateTaxOne.getStateName(), retrievedStateTax.getStateName(), "Checking State Name.");
        assertEquals(stateTaxOne.getTaxRate(), retrievedStateTax.getTaxRate(), "Checking Tax Rate.");
    }
    
    @Test
    public void testAddGetAllStateTaxes() throws Exception {
        System.out.println("test Tax Dao: Add and Get All State Taxes.");
        // ARRANGE
        String stateInitials = "ON";
        String stateName = "Ontario";
        BigDecimal taxRate = new BigDecimal("1.11");
        Taxes stateTaxOne = new Taxes(stateInitials, stateName, taxRate);    
        
        String stateInitialsTwo = "TX";
        String stateNameTwo = "Texas";
        BigDecimal taxRateTwo = new BigDecimal("2.22");
        Taxes stateTaxTwo = new Taxes(stateInitialsTwo, stateNameTwo, taxRateTwo);
        
        // ACT
        testDaoTax.addStateTax(stateInitials, stateTaxOne);
        testDaoTax.addStateTax(stateInitialsTwo, stateTaxTwo);
        List<Taxes> statetaxList = testDaoTax.getAllStateTaxes();
        
        // ASSERT
        assertNotNull(statetaxList, "The list of state taxes must NOT BE NULL.");
        assertEquals(2, statetaxList.size(), "List of state taxes should have two (2)");
        assertTrue(testDaoTax.getAllStateTaxes().contains(stateTaxOne), "List of state taxes should have ON - Ontario.");
        assertTrue(testDaoTax.getAllStateTaxes().contains(stateTaxTwo), "List of state taxes should have TX - Texas.");
    }
    
    @Test
    public void testRemoveStateTax() throws Exception {
        System.out.println("test Tax Dao: Remove State Tax.");
        // ARRANGE
        String stateInitialsOne = "ON";
        String stateNameOne = "Ontario";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        Taxes stateTaxOne = new Taxes(stateInitialsOne, stateNameOne, taxRateOne);
        
        String stateInitialsTwo = "TX";
        String stateNameTwo = "Texas";
        BigDecimal taxRateTwo = new BigDecimal("2.22");
        Taxes stateTaxTwo = new Taxes(stateInitialsTwo, stateNameTwo, taxRateTwo);
        
        // ACT and ASSERT
        testDaoTax.addStateTax(stateInitialsOne, stateTaxOne);
        testDaoTax.addStateTax(stateInitialsTwo, stateTaxTwo);
        Taxes removedStatetax = testDaoTax.removeStateTax(stateInitialsOne);
        assertEquals(removedStatetax, stateTaxOne, "The removed state tax should be ON - Ontario.");
        
        // ACT and ASSERT
        List<Taxes> allStateTaxes = testDaoTax.getAllStateTaxes();
        assertNotNull(allStateTaxes, "All state tax list should NOT BE NULL.");
        assertEquals(1, allStateTaxes.size(), "All state tax list should only have one (1) state tax.");
        assertFalse(allStateTaxes.contains(stateTaxOne), "All state tax list should NOT INCLUDE ON - Ontario.");
        assertTrue(allStateTaxes.contains(stateTaxTwo), "All state tax list should INCLUDE TX - Texas.");
        
        // ACT and ASSERT
        removedStatetax = testDaoTax.removeStateTax(stateInitialsTwo);
        assertEquals(removedStatetax, stateTaxTwo, "The removed state tax should be TX - Texas.");
        
        // ACT and ASSERT
        allStateTaxes = testDaoTax.getAllStateTaxes();
        assertTrue(allStateTaxes.isEmpty(), "The retrieved list of state taxes SHOULD BE EMPTY now.");
        
        Taxes retrievedStatetax = testDaoTax.getStateTax(stateInitialsOne);
        assertNull(retrievedStatetax, "ON - Ontario was removed, SHOULD BE NULL.");
        
        Taxes retrievedStateTax = testDaoTax.getStateTax(stateInitialsTwo);
        assertNull(retrievedStateTax, "TX - Texas was removed, SHOULD BE NULL.");
    }
    
    @Test
    public void testUpdateStateTax() throws Exception {
        System.out.println("test Tax Dao: Update State Tax.");
        // ARRANGE
        String stateInitialsOne = "ON";
        String stateNameOne = "Ontario";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        Taxes stateTaxOne = new Taxes(stateInitialsOne, stateNameOne, taxRateOne);
        
        String stateInitialsUpdated = "ON";
        String stateNameUpdated = "Ontario";
        BigDecimal taxRateUpdated = new BigDecimal("3.33");
        Taxes stateTaxUpdated = new Taxes(stateInitialsUpdated, stateNameUpdated, taxRateUpdated);
        
        // ACT and ASSERT
        testDaoTax.addStateTax(stateInitialsOne, stateTaxOne);
        Taxes retrievedStateTaxOne = testDaoTax.getStateTax(stateInitialsOne);
        assertEquals(stateTaxOne, retrievedStateTaxOne, "Checking same state tax ON - Ontario.");
        
        // ACT and ASSERT
        testDaoTax.updateStateTax(stateInitialsUpdated, stateTaxUpdated);
        Taxes retrievedStateTaxUpdated = testDaoTax.getStateTax(stateInitialsUpdated);
        assertNotEquals(stateTaxOne, retrievedStateTaxUpdated, "Updated state tax SHOULD NOT BE EQUAL to State Tax One.");
        assertEquals(stateTaxUpdated, retrievedStateTaxUpdated, "Checking state tax updated.");
        
        // ACT and ASSERT
        List<Taxes> allStatetaxes = testDaoTax.getAllStateTaxes();
        assertEquals(1, allStatetaxes.size(), "All state tax list should only have one (1)");
        assertEquals(stateTaxOne.getStateInitials(), stateTaxUpdated.getStateInitials(), "State Initials SHOULD BE EQUAL.");
        assertEquals(stateTaxOne.getStateName(), stateTaxUpdated.getStateName(), "State Names SHOULD BE EQUAL.");
        assertNotEquals(retrievedStateTaxOne.getTaxRate(), retrievedStateTaxUpdated.getTaxRate(), "Tax Rates SHOULD NOT BE EQUAL.");
    }
    
    // Template for Arranged Taxes
    /*
    --- ARRANGED STATE TAX ONE ---
    String stateInitialsOne = "ON";
    String stateNameOne = "Ontario";
    BigDecimal taxRateOne = new BigDecimal("1.11");
    Taxes stateTaxOne = new Taxes(stateInitialsOne, stateNameOne, taxRateOne);
    
    --- ARRANGED STATE TAX TWO ---
    String stateInitialsTwo = "TX";
    String stateNameTwo = "Texas";
    BigDecimal taxRateTwo = new BigDecimal("2.22");
    Taxes stateTaxTwo = new Taxes(stateInitialsTwo, stateNameTwo, taxRateTwo);
    */
    
}
