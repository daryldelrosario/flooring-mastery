/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Taxes;
import com.sg.flooringmastery.service.PersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryTaxDaoFileImpl implements FlooringMasteryTaxDao {

    private final String TAX_FILE;
    public static final String DELIMITER = "::";
    private Map<String, Taxes> stateTaxList = new HashMap<>();
    
    public FlooringMasteryTaxDaoFileImpl() {
        TAX_FILE = "Data/taxes.txt";
    }
    public FlooringMasteryTaxDaoFileImpl(String taxTextFile) {
        TAX_FILE = taxTextFile;
    }
    
    @Override
    public List<Taxes> getAllStateTaxes() throws PersistenceException {
        this.loadTaxFile();
        return new ArrayList<Taxes>(stateTaxList.values());
    }

    @Override
    public Taxes addStateTax(String stateInitial, Taxes stateTax) throws PersistenceException {
        this.loadTaxFile();
        Taxes newStateTax = stateTaxList.put(stateInitial, stateTax);
        this.writeTaxFile();
        return newStateTax;
    }

    @Override
    public Taxes updateStateTax(String stateInitial, Taxes stateTax) throws PersistenceException {
        this.loadTaxFile();
        Taxes editedStateTax = stateTaxList.replace(stateInitial, stateTax);
        this.writeTaxFile();
        return editedStateTax;
    }

    @Override
    public Taxes removeStateTax(String stateInitial) throws PersistenceException {
        this.loadTaxFile();
        Taxes removedStateTax = stateTaxList.remove(stateInitial);
        this.writeTaxFile();
        return removedStateTax;
    }

    @Override
    public Taxes getStateTax(String stateInitial) throws PersistenceException {
        this.loadTaxFile();
        return stateTaxList.get(stateInitial);
    }
    
    private Taxes unmarshallStateTax(String taxesAsText) {
        String[] taxTokens = taxesAsText.split(DELIMITER);
        
        String stateInitials = taxTokens[0];
        String stateName = taxTokens[1];
        BigDecimal taxRate = new BigDecimal(taxTokens[2]);
        
        Taxes taxFromFile = new Taxes(stateInitials, stateName, taxRate);
        return taxFromFile;
    }
    
    private void loadTaxFile() throws PersistenceException {
        Scanner sc;
        
        try {
            sc = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        } catch(FileNotFoundException e) {
            throw new PersistenceException("Could not load tax data into memory.", e);
        }
        
        String currentLine;
        Taxes currentStateTax;
        
        try {
            sc.nextLine();
            while(sc.hasNextLine()) {
                currentLine = sc.nextLine();
                currentStateTax = unmarshallStateTax(currentLine);
                stateTaxList.put(currentStateTax.getStateInitials(), currentStateTax);
            }
            sc.close();
        } catch(Exception e) {
            while(sc.hasNextLine()) {
                currentLine = sc.nextLine();
                currentStateTax = unmarshallStateTax(currentLine);
                stateTaxList.put(currentStateTax.getStateInitials(), currentStateTax);
            }
            sc.close();
        }
    }
    
    private String marshallStateTax(Taxes stateTax) {
        String stateTaxAsText = stateTax.getStateInitials() + DELIMITER;
        stateTaxAsText += stateTax.getStateName() + DELIMITER;
        stateTaxAsText += stateTax.getTaxRate().toString();
        
        return stateTaxAsText;
    }
    
    private void writeTaxFile() throws PersistenceException {
        this.writeTaxHeader();
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(TAX_FILE, true));
        } catch(IOException e) {
            throw new PersistenceException("Could not save tax data.", e);
        }
        
        String stateTaxAsText;
        List<Taxes> stateTaxList = this.getAllStateTaxes();
        for(Taxes currentStateTax : stateTaxList) {
            stateTaxAsText = marshallStateTax(currentStateTax);
            out.println(stateTaxAsText);
            out.flush();
        }
        out.close();
    }

    private void writeTaxHeader() throws PersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(TAX_FILE, false));
        } catch(IOException e) {
            throw new PersistenceException("Could not save tax data.", e);
        }
        
        out.println("StateInitials::StateName::TaxRate");
        out.flush();
    }
}
