/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.service;

/**
 *
 * @author Daryl del Rosario
 */
public class DuplicateEntryException extends Exception {
    
    public DuplicateEntryException(String msg) {
        super(msg);
    }
    
    public DuplicateEntryException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
