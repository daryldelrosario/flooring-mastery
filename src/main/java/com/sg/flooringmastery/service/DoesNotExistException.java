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
public class DoesNotExistException extends Exception {
    
    public DoesNotExistException (String msg) {
        super(msg);
    }
    
    public DoesNotExistException (String msg, Throwable cause) {
        super(msg, cause);
    }

}
