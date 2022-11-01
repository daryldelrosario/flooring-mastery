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
public class Orders {
    private int orderNumber; // 1 - arr[0]
    private String customerName; // 2 - arr [1]
    private String stateInitials; // 3 - arr [2]
    private String stateName; // 4 - arr [3]
    private BigDecimal taxRate; // 5 - arr [4]
    private String productType; // 6 - arr [5]
    private BigDecimal area; // 7 - arr [6]
    private BigDecimal costPerSquareFoot; // 8 - arr [7]
    private BigDecimal laborCostPerSquareFoot; // 9 - arr [8]
    private BigDecimal materialCost; // 10 - arr [9]
    private BigDecimal laborCost; // 11 - arr [10]
    private BigDecimal tax; // 12 - arr [11]
    private BigDecimal total; // 13 - arr [12]
    
    public Orders() {
        
    }

    public Orders (String customerName, Taxes tax, Products product, BigDecimal area) {
        this.customerName = customerName;
        this.stateInitials = tax.getStateInitials();
        this.stateName = tax.getStateName();
        this.taxRate = tax.getTaxRate();
        this.productType = product.getProductType();
        this.area = area;
        this.costPerSquareFoot = product.getCostPerSquareFoot();
        this.laborCostPerSquareFoot = product.getLaborCostPerSquareFoot();
        this.setCalculations();
    }
    
    public Orders(int orderNumber,
            String customerName,
            String stateInitials,
            String stateName,
            BigDecimal taxRate,
            String productType,
            BigDecimal area,
            BigDecimal costPerSquareFoot,
            BigDecimal laborCostPerSquareFoot) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.stateInitials = stateInitials;
        this.stateName = stateName;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
        this.setCalculations();
    }

    public int getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getStateInitials() {
        return stateInitials;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getProductType() {
        return productType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }
    
    public BigDecimal getTax() {
        return tax;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public String getStateName() {
        return stateName;
    }
    
    // Support Function and Code
    private void setCalculations() {
        this.materialCost = this.area.multiply(this.costPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
        this.laborCost = this.area.multiply(this.laborCostPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
        this.tax = ((this.materialCost.add(this.laborCost)).multiply((this.taxRate.divide(new BigDecimal("100"))))).setScale(2, RoundingMode.HALF_UP);
        this.total = this.materialCost.add(this.laborCost).add(this.tax);
    }  

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.orderNumber;
        hash = 67 * hash + Objects.hashCode(this.customerName);
        hash = 67 * hash + Objects.hashCode(this.stateInitials);
        hash = 67 * hash + Objects.hashCode(this.stateName);
        hash = 67 * hash + Objects.hashCode(this.taxRate);
        hash = 67 * hash + Objects.hashCode(this.productType);
        hash = 67 * hash + Objects.hashCode(this.area);
        hash = 67 * hash + Objects.hashCode(this.costPerSquareFoot);
        hash = 67 * hash + Objects.hashCode(this.laborCostPerSquareFoot);
        hash = 67 * hash + Objects.hashCode(this.materialCost);
        hash = 67 * hash + Objects.hashCode(this.laborCost);
        hash = 67 * hash + Objects.hashCode(this.tax);
        hash = 67 * hash + Objects.hashCode(this.total);
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
        final Orders other = (Orders) obj;
        if (this.orderNumber != other.orderNumber) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.stateInitials, other.stateInitials)) {
            return false;
        }
        if (!Objects.equals(this.stateName, other.stateName)) {
            return false;
        }
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.costPerSquareFoot, other.costPerSquareFoot)) {
            return false;
        }
        if (!Objects.equals(this.laborCostPerSquareFoot, other.laborCostPerSquareFoot)) {
            return false;
        }
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCost, other.laborCost)) {
            return false;
        }
        if (!Objects.equals(this.tax, other.tax)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Orders{" + "orderNumber=" + orderNumber + ", customerName=" + customerName + ", stateInitials=" + stateInitials + ", stateName=" + stateName + ", taxRate=" + taxRate + ", productType=" + productType + ", area=" + area + ", costPerSquareFoot=" + costPerSquareFoot + ", laborCostPerSquareFoot=" + laborCostPerSquareFoot + ", materialCost=" + materialCost + ", laborCost=" + laborCost + ", tax=" + tax + ", total=" + total + '}';
    }
    
    
}
