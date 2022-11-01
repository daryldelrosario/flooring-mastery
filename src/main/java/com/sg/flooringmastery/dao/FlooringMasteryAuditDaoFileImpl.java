/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.service.PersistenceException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryAuditDaoFileImpl implements FlooringMasteryAuditDao {

    public static final String AUDIT_FILE = "Audit/audit.txt";
    
    @Override
    public void writeAuditLine(String msg) throws PersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch(IOException e) {
            throw new PersistenceException("Could not persist audit information.", e);
        }
        
        String timeStamp = this.getAuditTimeStamp();
        
        out.println(timeStamp + " : " + msg);
        out.flush();
    }
    
    @Override
    public void writeAuditOrderLine(String procedure, LocalDate date, Orders order) throws PersistenceException {
        PrintWriter out;
        String orderNumber = String.valueOf(order.getOrderNumber());
        String orderDate = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch(IOException e) {
            throw new PersistenceException("Could not persist audit information.", e);
        }
        
        String timeStamp = this.getAuditTimeStamp();
        out.println(timeStamp + " : User Accessed Order Menu - " 
                + procedure + " Order Number: " 
                + orderNumber + " for [MM-DD-YYYY] " 
                + orderDate);
        out.flush();
    }
    
    @Override
    public void writeAuditProductLine(String procedure, String productType) throws PersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch(IOException e) {
            throw new PersistenceException("Could not persist audit information.", e);
        }
        
        String timeStamp = this.getAuditTimeStamp();
        out.println(timeStamp + " : Accessed TSG CORP Product Menu - " + procedure + " - Product Type: " + productType.toUpperCase());
        out.flush();
    }
    
    @Override
    public void writeAuditTaxLine(String procedure, String stateInitial, String stateName) throws PersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch(IOException e) {
            throw new PersistenceException("Could not persist audit information.", e);
        }
        
        String timeStamp = this.getAuditTimeStamp();
        out.println(timeStamp + " : Accessed TSG Corp Tax Menu - " + procedure 
                + " - State Tax Rate: " + stateInitial + " - " + stateName);
        out.flush();
    }
    
    private String getAuditTimeStamp() {
        LocalDateTime rightNow = LocalDateTime.now();
        String dateStamp = rightNow.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        String timeStamp = rightNow.format(DateTimeFormatter.ofPattern("HH.mm.ss"));
        String dateTimeStamp = dateStamp + " @ " + timeStamp;
        return dateTimeStamp;
    }
}
