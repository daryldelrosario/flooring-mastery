/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Products;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryProductsDaoFileImplTest {
    
    FlooringMasteryProductsDao testDaoProduct;
    
    public FlooringMasteryProductsDaoFileImplTest() {
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        String testFile = "Test/TestData/testProducts.txt";
        new FileWriter(testFile);
        testDaoProduct = new FlooringMasteryProductsDaoFileImpl(testFile);
    }

    @Test
    public void testAddGetProduct() throws Exception {
        System.out.println("test Product Dao: Add and Get Product");
        // ARRANGE
        String productType = "Tile";
        BigDecimal cost = new BigDecimal("1.01");
        BigDecimal labor = new BigDecimal("1.02");
        Products product = new Products(productType, cost, labor);
        
        // ACT
        testDaoProduct.addProduct(productType, product);
        Products retrievedProduct = testDaoProduct.getProduct(productType);
        
        // ASSERT
        assertEquals(product.getProductType(), retrievedProduct.getProductType(), "Checking Product Type.");
        assertEquals(product.getCostPerSquareFoot(), retrievedProduct.getCostPerSquareFoot(), "Checking Cost.");
        assertEquals(product.getLaborCostPerSquareFoot(), retrievedProduct.getLaborCostPerSquareFoot(), "Checking Labor Cost.");
    }
    
    @Test
    public void testAddGetAllProducts() throws Exception {
        System.out.println("test Product Dao: Add and Get All Products");
        // ARRANGE
        String productTypeOne = "Tile";
        BigDecimal costOne = new BigDecimal("1.01");
        BigDecimal laborCostOne = new BigDecimal("1.02");
        Products productOne = new Products(productTypeOne, costOne, laborCostOne);
        
        String productTypeTwo = "Carpet";
        BigDecimal costTwo = new BigDecimal("2.01");
        BigDecimal laborCostTwo = new BigDecimal("2.02");
        Products productTwo = new Products(productTypeTwo, costTwo, laborCostTwo);
        
        // ACT
        testDaoProduct.addProduct(productTypeOne, productOne);
        testDaoProduct.addProduct(productTypeTwo, productTwo);
        List<Products> allProducts = testDaoProduct.getAllProducts();
        
        // ASSERT
        assertNotNull(allProducts, "The list of products must NOT BE NULL.");
        assertEquals(2, allProducts.size(), "List of products should have two (2).");
        assertTrue(testDaoProduct.getAllProducts().contains(productOne), "List of products should have Tile.");
        assertTrue(testDaoProduct.getAllProducts().contains(productTwo), "List of products should have Carpet.");
    }
    
    @Test
    public void testRemoveProduct() throws Exception {
        System.out.println("test Product Dao: Remove Products");
        // ARRANGE
        String productTypeOne = "Tile";
        BigDecimal costOne = new BigDecimal("1.01");
        BigDecimal laborCostOne = new BigDecimal("1.02");
        Products productOne = new Products(productTypeOne, costOne, laborCostOne);
        
        String productTypeTwo = "Carpet";
        BigDecimal costTwo = new BigDecimal("2.01");
        BigDecimal laborCostTwo = new BigDecimal("2.02");
        Products productTwo = new Products(productTypeTwo, costTwo, laborCostTwo);
        
        // ACT and ASSERT
        testDaoProduct.addProduct(productTypeOne, productOne);
        testDaoProduct.addProduct(productTypeTwo, productTwo);
        Products removedProduct = testDaoProduct.removeProduct(productTypeOne);
        assertEquals(removedProduct, productOne, "The removed product should be Tile.");
        
        // ACT and ASSERT
        List<Products> allProducts = testDaoProduct.getAllProducts();
        assertNotNull(allProducts, "All products list should NOT BE NULL.");
        assertEquals(1, allProducts.size(), "All products list should only have one (1) product.");
        assertFalse(allProducts.contains(productOne), "All products should NOT INCLUDE TILE.");
        assertTrue(allProducts.contains(productTwo), "All products should INCLUDE CARPET.");
        
        // ACT and ASSERT
        removedProduct = testDaoProduct.removeProduct(productTypeTwo);
        assertEquals(removedProduct, productTwo, "The removed product should be Carpet.");
        
        // ACT and ASSERT
        allProducts = testDaoProduct.getAllProducts();
        assertTrue(allProducts.isEmpty(), "The retrieved list of products SHOULD BE EMPTY.");
        
        Products retrievedProduct = testDaoProduct.getProduct(productTypeOne);
        assertNull(retrievedProduct, "Tile was removed, SHOULD BE NULL.");
        
        retrievedProduct = testDaoProduct.getProduct(productTypeTwo);
        assertNull(retrievedProduct, "Carpet was removed, SHOULD BE NULL.");
    }
    
    @Test
    public void testUpdateProduct() throws Exception {
        System.out.println("test Product Dao: Update Product");
        // ARRANGE
        String productTypeOne = "Tile";
        BigDecimal costOne = new BigDecimal("1.01");
        BigDecimal laborCostOne = new BigDecimal("1.02");
        Products productOne = new Products(productTypeOne, costOne, laborCostOne);
        
        String productTypeUpdated = "Tile";
        BigDecimal costUpdated = new BigDecimal("3.01");
        BigDecimal laborCostUpdated = new BigDecimal("3.02");
        Products productUpdated = new Products(productTypeUpdated, costUpdated, laborCostUpdated);
        
        // ACT and ASSERT
        testDaoProduct.addProduct(productTypeOne, productOne);
        Products retrievedProductOne = testDaoProduct.getProduct(productTypeOne);
        assertEquals(productOne, retrievedProductOne, "Checking same product.");
        
        // ACT and ASSERT
        testDaoProduct.updateProduct(productTypeUpdated, productUpdated);
        Products retrievedProductUpdated = testDaoProduct.getProduct(productTypeUpdated);
        assertNotEquals(productOne, retrievedProductUpdated, "Updated product SHOULD NOT BE EQUAL to Product One.");
        assertEquals(productUpdated, retrievedProductUpdated, "Checking product updated.");
        
        // ACT and ASSERT
        List<Products> allProducts = testDaoProduct.getAllProducts();
        assertEquals(1, allProducts.size(), "All product list should only have one (1)");
        assertEquals(productOne.getProductType(), productUpdated.getProductType(), "Product Type SHOULD BE EQUAL.");
        assertNotEquals(productOne.getCostPerSquareFoot(), productUpdated.getCostPerSquareFoot(), "Product cost SHOULD NOT BE EQUAL.");
        assertNotEquals(productOne.getLaborCostPerSquareFoot(), productUpdated.getLaborCostPerSquareFoot(), "Product labor cost SHOULD NOT BE EQUAL.");
        
    }
    
    // Template for Arranged Products
    /*
    --- ARRANGED PRODUCT ONE ---
    String productTypeOne = "Tile";
    BigDecimal costOne = new BigDecimal("1.01");
    BigDecimal laborCostOne = new BigDecimal("1.02");
    Products productOne = new Products(productTypeOne, costOne, laborCostOne);
    
    --- ARRANGE PRODUCT TWO ---
    String productTypeTwo = "Carpet";
    BigDecimal costTwo = new BigDecimal("2.01");
    BigDecimal laborCostTwo = new BigDecimal("2.02");
    Products productTwo = new Products(productTypeTwo, costTwo, laborCostTwo);
    */
}
