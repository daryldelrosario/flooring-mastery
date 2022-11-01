/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.model;

import com.sg.flooringmastery.service.DoesNotExistException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 *
 * @author Daryl del Rosario
 */
public class Products {
    
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    
    public Products() {
        
    }
    
    public Products(String productType, BigDecimal cost, BigDecimal laborCost) {
        this.productType = productType;
        this.costPerSquareFoot = cost.setScale(2, RoundingMode.HALF_UP);
        this.laborCostPerSquareFoot = laborCost.setScale(2, RoundingMode.HALF_UP);
    }

    public String getProductType() {      
        return productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.productType);
        hash = 89 * hash + Objects.hashCode(this.costPerSquareFoot);
        hash = 89 * hash + Objects.hashCode(this.laborCostPerSquareFoot);
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
        final Products other = (Products) obj;
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.costPerSquareFoot, other.costPerSquareFoot)) {
            return false;
        }
        if (!Objects.equals(this.laborCostPerSquareFoot, other.laborCostPerSquareFoot)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Products{" + "productType=" + productType + ", costPerSquareFoot=" + costPerSquareFoot + ", laborCostPerSquareFoot=" + laborCostPerSquareFoot + '}';
    }
    
}
