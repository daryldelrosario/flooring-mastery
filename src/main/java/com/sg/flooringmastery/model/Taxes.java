/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 *
 * @author Daryl del Rosario
 */
public class Taxes {
    
    private String stateInitials;
    private String stateName;
    private BigDecimal taxRate;
    
    public Taxes() {
        
    }
    
    public Taxes(String stateInitials, String stateName, BigDecimal taxRate) {
        this.stateInitials = stateInitials;
        this.stateName = stateName;
        this.taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
    }

    public String getStateInitials() {
        return stateInitials;
    }

    public String getStateName() {
        return stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.stateInitials);
        hash = 89 * hash + Objects.hashCode(this.stateName);
        hash = 89 * hash + Objects.hashCode(this.taxRate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Taxes other = (Taxes) obj;
        if (!Objects.equals(this.stateInitials, other.stateInitials)) {
            return false;
        }
        if (!Objects.equals(this.stateName, other.stateName)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Taxes{" + "stateInitials=" + stateInitials + ", stateName=" + stateName + ", taxRate=" + taxRate + '}';
    }
    
}
