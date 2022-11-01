/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.service.PersistenceException;
import com.sg.flooringmastery.model.Products;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public interface FlooringMasteryProductsDao {
    List<Products> getAllProducts() throws PersistenceException;
    Products addProduct(String productType, Products product) throws PersistenceException;
    Products updateProduct(String productType, Products product) throws PersistenceException;
    Products removeProduct(String productType) throws PersistenceException;
    Products getProduct(String productType) throws PersistenceException;
}
