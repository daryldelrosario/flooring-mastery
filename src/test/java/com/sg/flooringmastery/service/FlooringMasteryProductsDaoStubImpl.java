/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryProductsDao;
import com.sg.flooringmastery.model.Products;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryProductsDaoStubImpl implements FlooringMasteryProductsDao {
    
    public Products productOne;
    
    public FlooringMasteryProductsDaoStubImpl() {
        productOne = new Products("Tile", new BigDecimal("1.01"), new BigDecimal("1.02"));
    }
    
    public FlooringMasteryProductsDaoStubImpl(Products testProduct) {
        this.productOne = testProduct;
    }

    @Override
    public List<Products> getAllProducts() throws PersistenceException {
        List<Products> productList = new ArrayList<>();
        productList.add(productOne);
        return productList;
    }

    @Override
    public Products addProduct(String productType, Products product) throws PersistenceException {
        if(productType.equals(productOne.getProductType())) {
            return productOne;
        } else {
            return null;
        }
    }

    @Override
    public Products updateProduct(String productType, Products product) throws PersistenceException {
        return product;
    }

    @Override
    public Products removeProduct(String productType) throws PersistenceException {
        if(productType.equals(productOne.getProductType())) {
            return productOne;
        } else {
            return null;
        }
    }

    @Override
    public Products getProduct(String productType) throws PersistenceException {
        if(productType.equals(productOne.getProductType())) {
            return productOne;
        } else {
            return null;
        }
    }

}
