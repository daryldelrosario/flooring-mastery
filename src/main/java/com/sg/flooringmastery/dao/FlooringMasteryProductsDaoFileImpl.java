/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.service.PersistenceException;
import com.sg.flooringmastery.model.Products;
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
public class FlooringMasteryProductsDaoFileImpl implements FlooringMasteryProductsDao {
    
    private final String PRODUCTS_FILE;
    public static final String DELIMITER = "::";
    private Map<String, Products> productList = new HashMap<>();
    
    public FlooringMasteryProductsDaoFileImpl() {
        PRODUCTS_FILE = "Data/products.txt";
    }
    
    public FlooringMasteryProductsDaoFileImpl(String productsTextFile) {
        PRODUCTS_FILE = productsTextFile;
    }

    @Override
    public List<Products> getAllProducts() throws PersistenceException {
        this.loadProductsFile();
        return new ArrayList<Products>(productList.values());
    }

    @Override
    public Products addProduct(String productType, Products product) throws PersistenceException {
        this.loadProductsFile();
        Products newProduct = productList.put(productType, product);
        this.writeProductsFile();
        return newProduct;
    }

    @Override
    public Products updateProduct(String productType, Products product) throws PersistenceException {
        this.loadProductsFile();
        Products editedProduct = productList.replace(productType, product);
        this.writeProductsFile();
        return editedProduct;
    }

    @Override
    public Products removeProduct(String productType) throws PersistenceException {
        this.loadProductsFile();
        Products removedProduct = productList.remove(productType);
        this.writeProductsFile();
        return removedProduct;
    }

    @Override
    public Products getProduct(String productType) throws PersistenceException {
        this.loadProductsFile();
        return productList.get(productType);
    }
    
    private Products unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(DELIMITER);
        
        String productType = productTokens[0];
        BigDecimal cost = new BigDecimal(productTokens[1]);
        BigDecimal laborCost = new BigDecimal(productTokens[2]);
        
        Products productFromFile = new Products(productType, cost, laborCost);
        return productFromFile;
    }
    
    private void loadProductsFile() throws PersistenceException {
        Scanner sc;
        
        try {
            sc = new Scanner(new BufferedReader(new FileReader(PRODUCTS_FILE)));
        } catch(FileNotFoundException e) {
            throw new PersistenceException("Could not load product data into memory.", e);
        }
        
        String currentLine;
        Products currentProduct;

        try {
            sc.nextLine();
            while(sc.hasNextLine()) {
                currentLine = sc.nextLine();
                currentProduct = unmarshallProduct(currentLine);
                productList.put(currentProduct.getProductType(), currentProduct);
            }
            sc.close();
        } catch(Exception e) {
           while(sc.hasNextLine()) {
                currentLine = sc.nextLine();
                currentProduct = unmarshallProduct(currentLine);
                productList.put(currentProduct.getProductType(), currentProduct);
            }
            sc.close(); 
        }
    }
    
    private String marshallProduct(Products product) {
        String productAsText = product.getProductType() + DELIMITER;
        productAsText += product.getCostPerSquareFoot().toString() + DELIMITER;
        productAsText += product.getLaborCostPerSquareFoot().toString();
        
        return productAsText;
    }
    
    private void writeProductsFile() throws PersistenceException {
        this.writeProductsHeader();
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(PRODUCTS_FILE, true));
        } catch (IOException e) {
            throw new PersistenceException("Could not save product data.", e);
        }
        
        
        String productAsText;
        List<Products> productList = this.getAllProducts();
        for(Products currentProduct : productList) {
            productAsText = marshallProduct(currentProduct);
            out.println(productAsText);
            out.flush();
        }
        out.close();
    }
    
    private void writeProductsHeader()  throws PersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(PRODUCTS_FILE, false));
        } catch (IOException e) {
            throw new PersistenceException("Could not save product data.", e);
        }
        
        out.println("ProductType::CostPerSquareFoot::LaborCostPerSquareFoot");
        out.flush();
    }

}
