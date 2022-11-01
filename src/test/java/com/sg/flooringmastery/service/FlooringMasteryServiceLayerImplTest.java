/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.model.Products;
import com.sg.flooringmastery.model.Taxes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryServiceLayerImplTest {
    
    private FlooringMasteryServiceLayer testService;
    
    public FlooringMasteryServiceLayerImplTest() {
//        FlooringMasteryOrdersDao testDaoOrder = new FlooringMasteryOrdersDaoStubImpl();
//        FlooringMasteryProductsDao testDaoProduct = new FlooringMasteryProductsDaoStubImpl();
//        FlooringMasteryTaxDao testDaoTax = new FlooringMasteryTaxDaoStubImpl();
//        FlooringMasteryAuditDao testDaoAudit = new FlooringMasteryAuditDaoStubImpl();
 
//        testService = new FlooringMasteryServiceLayerImpl(testDaoOrder, testDaoProduct, testDaoTax, testDaoAudit);
// CODE ABOVE FOR PRE-SPRING DI
        ApplicationContext ctx = new ClassPathXmlApplicationContext("testAppContext.xml");
        this.testService = ctx.getBean("serviceLayer", FlooringMasteryServiceLayer.class);
    }
    
    // ORDER SERVICE LAYER TEST
    @Test
    public void testCreateOrder() throws Exception {
        System.out.println("test Service Layer: Create Order");
        // ARRANGE
        LocalDate futureDate = LocalDate.now().plusDays(11);
        int orderNumberOne = 1;
        String customerNameOne = "One";
        String stateInitialsOne = "TX";
        String stateNameOne = "Texas";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        String productTypeOne = "Laminate";
        BigDecimal areaOne = new BigDecimal("111");
        BigDecimal costPerSquareFootOne = new BigDecimal("1.11");
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("11.1");
        Orders orderOne = new Orders(orderNumberOne, customerNameOne, stateInitialsOne, stateNameOne, 
                taxRateOne, productTypeOne, areaOne, 
                costPerSquareFootOne, laborCostPerSquareFootOne);
        
        // ACT and ASSERT
        try {
            testService.addOrder(futureDate, orderOne);
        } catch(PersistenceException
                | FutureDateException
                | DataValidationException e) {
            fail("Valid order added. No exception should be thrown.");
        }
    }
    
    @Test
    public void testCreateOrderInvalidData() throws Exception {
        System.out.println("test Service Layer: Create Order - DataValidationException");
        // ARRANGE
        LocalDate futureDate = LocalDate.now().plusDays(11);
        int orderNumberOne = 1;
        String customerNameOne = "One!";
        String stateInitialsOne = "TX";
        String stateNameOne = "Texas";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        String productTypeOne = "Laminate";
        BigDecimal areaOne = new BigDecimal("11");
        BigDecimal costPerSquareFootOne = new BigDecimal("1.11");
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("11.1");
        Orders orderInvalid = new Orders(orderNumberOne, customerNameOne, stateInitialsOne, stateNameOne, 
                taxRateOne, productTypeOne, areaOne, 
                costPerSquareFootOne, laborCostPerSquareFootOne);
        
        // ACT and ASSERT
        try {
            testService.addOrder(futureDate, orderInvalid);
            fail("Invalid data entered. DataValidationException SHOULD BE THROWN.");
        } catch (PersistenceException
                | FutureDateException e) {
            System.out.println(e);
            fail("Incorrect exception thrown.");
        } catch (DataValidationException e) {
            return;
        }
    }
    
    @Test
    public void testCreateOrderPastDate() throws Exception {
        System.out.println("test Service Layer: Create Order - FutureDateException");
        // ARRANGE
        LocalDate pastDate = LocalDate.now().minusDays(11);
        int orderNumberOne = 1;
        String customerNameOne = "One";
        String stateInitialsOne = "TX";
        String stateNameOne = "Texas";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        String productTypeOne = "Laminate";
        BigDecimal areaOne = new BigDecimal("111");
        BigDecimal costPerSquareFootOne = new BigDecimal("1.11");
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("11.1");
        Orders orderOne = new Orders(orderNumberOne, customerNameOne, stateInitialsOne, stateNameOne, 
                taxRateOne, productTypeOne, areaOne, 
                costPerSquareFootOne, laborCostPerSquareFootOne);
        
        // ACT and ASSERT
        try {
            testService.addOrder(pastDate, orderOne);
            fail("Past date used. Should throw FutureDateException");
        } catch(PersistenceException
                | DataValidationException e) {
            fail("Incorrect exception thrown.");
        } catch(FutureDateException e) {
            return;
        }
    }
    
    @Test
    public void testEditOrder() throws Exception {
        System.out.println("test Service Layer: Edit Order");
        // ARRANGE
        LocalDate today = LocalDate.now();
        List<Orders> orderList = new ArrayList<>();
        int orderNumberOne = 1;
        String customerNameOne = "One";
        String stateInitialsOne = "TX";
        String stateNameOne = "Texas";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        String productTypeOne = "Laminate";
        BigDecimal areaOne = new BigDecimal("111");
        BigDecimal costPerSquareFootOne = new BigDecimal("1.11");
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("11.1");
        Orders orderOne = new Orders(orderNumberOne, customerNameOne, stateInitialsOne, stateNameOne, 
                taxRateOne, productTypeOne, areaOne, 
                costPerSquareFootOne, laborCostPerSquareFootOne);
        orderList.add(orderOne);
        
        // ACT and ASSERT
        try {
            testService.editOrder(today, orderList, orderOne);
        } catch(PersistenceException
                | DataValidationException e) {
            fail("Order updated with valid entries. Should not throw exception.");
        }
    }
    
    @Test
    public void testEditOrderInvalidData() throws Exception {
        System.out.println("test Service Layer: Edit Order - DataValidationException");
        // ARRANGE
        LocalDate date = LocalDate.now();
        List<Orders> orderList = new ArrayList<>();
        int orderNumberOne = 1;
        String customerNameOne = "One!";
        String stateInitialsOne = "TX";
        String stateNameOne = "Texas";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        String productTypeOne = "Laminate";
        BigDecimal areaOne = new BigDecimal("111");
        BigDecimal costPerSquareFootOne = new BigDecimal("1.11");
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("11.1");
        Orders orderOne = new Orders(orderNumberOne, customerNameOne, stateInitialsOne, stateNameOne, 
                taxRateOne, productTypeOne, areaOne, 
                costPerSquareFootOne, laborCostPerSquareFootOne);
        orderList.add(orderOne);
        
        // ACT and ASSERT
        try {
            testService.editOrder(date, orderList, orderOne);
            fail("Invalid data in customerName One!");
        } catch(PersistenceException e) {
            fail("Incorrect exception thrown.");
        } catch(DataValidationException e) {
            return;
        }
    }
    
    @Test
    public void testGetAllOrders() throws Exception {
        System.out.println("test Service Layer: Get All Orders");
        // ARRANGE
        LocalDate today = LocalDate.now();
        
        // ACT and ASSERT
        try {
            testService.getAllOrders(today);
        } catch(Exception e) {
            fail("No exception should be thrown. Orders are in list.");
        }
    }
    
    @Test
    public void testGetAllOrdersNoOrders() throws Exception {
        System.out.println("test Service Layer: Get All Orders - DoesNotExistException");
        // ARRANGE
        LocalDate noOrderDate = LocalDate.now().plusDays(11);
        
        // ACT and ASSERT
        try {
            testService.getAllOrders(noOrderDate);
            fail("No orders on this date. DoesNotExistException should be thrown.");
        } catch(PersistenceException e) {
            fail("Incorrect exception thrown.");
        } catch(DoesNotExistException e) {
            return;
        }
        
    }
    
    @Test
    public void testGetOrder() throws Exception {
        System.out.println("test Service Layer: Get Order");
        // ARRANGE
        List<Orders> orderList = new ArrayList<>();
        int orderNumberOne = 1;
        String customerNameOne = "One";
        String stateInitialsOne = "TX";
        String stateNameOne = "Texas";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        String productTypeOne = "Laminate";
        BigDecimal areaOne = new BigDecimal("111");
        BigDecimal costPerSquareFootOne = new BigDecimal("1.11");
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("11.1");
        Orders orderOne = new Orders(orderNumberOne, customerNameOne, stateInitialsOne, stateNameOne, 
                taxRateOne, productTypeOne, areaOne, 
                costPerSquareFootOne, laborCostPerSquareFootOne);
        orderList.add(orderOne);
        
        // ACT and ASSERT
        try {
            testService.getOrder(orderList, orderNumberOne);
        } catch(Exception e) {
            fail("No exception should be thrown. Order exists.");
        }
    }

    @Test
    public void testGetOrderNoOrders() throws Exception {
        System.out.println("test Service Layer: Get Order - DoesNotExistException");
        // ARRANGE
        List<Orders> orderList = new ArrayList<>();
        int orderNumberOne = 2;
        String customerNameOne = "Does Not Exist";
        String stateInitialsOne = "TX";
        String stateNameOne = "Texas";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        String productTypeOne = "Laminate";
        BigDecimal areaOne = new BigDecimal("111");
        BigDecimal costPerSquareFootOne = new BigDecimal("1.11");
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("11.1");
        Orders orderDNE = new Orders(orderNumberOne, customerNameOne, stateInitialsOne, stateNameOne, 
                taxRateOne, productTypeOne, areaOne, 
                costPerSquareFootOne, laborCostPerSquareFootOne);
        orderList.add(orderDNE);
        
        // ACT and ASSERT
        try {
            testService.getOrder(orderList, orderDNE.getOrderNumber());
            fail("Order number 2 does not exist in list. DataValidationException SHOULD BE THROWN.");
        } catch(PersistenceException e) {
            fail("Incorrect exception thrown.");
        } catch(DoesNotExistException e) {
            return;
        }
    }
    
    @Test
    public void testGetAllOrderDates() throws Exception {
        System.out.println("test Service Layer: Get All Order Dates");
        // ARRANGE
        List<LocalDate> arrangedDatesList = new ArrayList<>();
        arrangedDatesList.add(LocalDate.now());
        
        // ACT and ASSERT
        List<LocalDate> retrievedDatesList = testService.getAllOrderDates();
        assertEquals(arrangedDatesList, retrievedDatesList, "These lists should be equal");
        assertEquals(1, retrievedDatesList.size(), "There should only be one (1) date in list");
        assertTrue(retrievedDatesList.contains(LocalDate.now()), "The list should have current date.");
    }

    @Test
    public void testRemoveOrder() throws Exception {
        System.out.println("test Service Layer: Remove Order");
        // ARRANGE
        LocalDate today = LocalDate.now();
        List<Orders> orderList = new ArrayList<>();
        int orderNumberOne = 1;
        String customerNameOne = "One";
        String stateInitialsOne = "TX";
        String stateNameOne = "Texas";
        BigDecimal taxRateOne = new BigDecimal("1.11");
        String productTypeOne = "Laminate";
        BigDecimal areaOne = new BigDecimal("111");
        BigDecimal costPerSquareFootOne = new BigDecimal("1.11");
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("11.1");
        Orders orderOne = new Orders(orderNumberOne, customerNameOne, stateInitialsOne, stateNameOne, 
                taxRateOne, productTypeOne, areaOne, 
                costPerSquareFootOne, laborCostPerSquareFootOne);
        orderList.add(orderOne);
        
        int orderNumberTwo = 2;
        String customerNameTwo = "Removed";
        String stateInitialsTwo = "CA";
        String stateNameTwo = "California";
        BigDecimal taxRateTwo = new BigDecimal("2.22");
        String productTypeTwo = "Soft Carpet";
        BigDecimal areaTwo = new BigDecimal("222");
        BigDecimal costPerSquareFootTwo = new BigDecimal("2.22");
        BigDecimal laborCostPerSquareFootTwo = new BigDecimal("22.2");
        Orders orderNotInList = new Orders(orderNumberTwo, customerNameTwo, stateInitialsTwo, stateNameTwo,
                taxRateTwo, productTypeTwo, areaTwo,
                costPerSquareFootTwo, laborCostPerSquareFootTwo);
        orderList.add(orderNotInList);
        
        // ACT and ASSERT
        List<Orders> retrievedOrderlist = testService.removeOrder(today, orderList, orderOne);
        assertTrue(retrievedOrderlist.isEmpty(), "Removed list of orders SHOULD BE EMPTY");
        
        // ACT and ASSERT
        try {
            retrievedOrderlist = testService.removeOrder(today, orderList, orderNotInList);
            fail("Order not in list. Should throw exception");
        } catch(Exception e) {
            return;
        }
    }
    
    // TAX SERVICE LAYER TESTS
    @Test
    public void testCreateValidStatetax() throws Exception {
        System.out.println("test Service Layer: Create Tax Rate");
        // ARRANGE
        Taxes newStatetax = new Taxes("CA", "California", new BigDecimal("2.22"));
        
        // ACT and ASSERT
        try {
            testService.createStateTax(newStatetax);
        } catch(PersistenceException
                | DataValidationException
                | DuplicateEntryException e) {
            fail("Valid state tax was created. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testCreateStatetaxDuplicate() throws Exception {
        System.out.println("test Service Layer: Create Tax Rate - DuplicateEntryExceptin");
        // ARRANGE
        Taxes duplicateStatetax = new Taxes("TX", "Texas", new BigDecimal("1.11"));
        
        // ACT and ASSERT
        try {
            testService.createStateTax(duplicateStatetax);
            fail("Duplicate state tax created. Should THROW EXCEPTION");
        } catch(PersistenceException
                | DataValidationException e) {
            fail("Incorrect exception thrown.");
        } catch(DuplicateEntryException e) {
            return;
        }
    }
    
    @Test
    public void testCreateStatetaxInvalidData() throws Exception {
        System.out.println("test Service Layer: Create Tax Rate - DataValidationException");
        // ARRANGE
        Taxes invalidStatetax = new Taxes("", "", new BigDecimal("9.99"));
        
        // ACT and ASSERT
        try {
            testService.createStateTax(invalidStatetax);
            fail("Invalid state tax created. Should THROW EXCEPTION");
        } catch(PersistenceException
                | DuplicateEntryException e) {
            fail("Incorrect exception thrown.");
        } catch(DataValidationException e) {
            return;
        }
    }
    
    @Test
    public void testUpdateStatetax() throws Exception {
        System.out.println("test Service Layer: Update Tax Rate");
        // ARRANGE
        Taxes editedStatetax = new Taxes("CA", "California", new BigDecimal("9.99"));
        
        // ACT and ASSERT
        try {
            testService.updateStateTax(editedStatetax);
        } catch(PersistenceException
                | DataValidationException e) {
            fail("Tax Rate update was valid.");
        }
    }
    
    @Test
    public void testUdateStatetaxInvalidData() throws Exception {
        System.out.println("test Service Layer: Update Tax Rate - DataValidationException");
        // ARRANGE
        Taxes editedInvalidStatetax = new Taxes("", "", new BigDecimal("7.77"));
        
        // ACT and ASSERT
        try{
            testService.updateStateTax(editedInvalidStatetax);
            fail("Tax Rate was invalid. DataValidationException NOT THROWN.");
        } catch(PersistenceException e) {
            fail("Incorrect exception thrown.");
        } catch(DataValidationException e) {
            return;
        }
    }
    
    @Test
    public void testGetStatetax() throws Exception {
        System.out.println("test Service Layer: Get Tax Rate");
        // ARRANGE
        Taxes arrangedStatetax = new Taxes("TX", "Texas", new BigDecimal("1.11"));
        Taxes retrievedStatetax = new Taxes();
        
        // ACT and ASSERT
        try {
            retrievedStatetax = testService.getStateTax(arrangedStatetax.getStateInitials());
        } catch(PersistenceException
                | DoesNotExistException e) {
            fail("Tax Rate was valid. No exception should be thrown.");
        }
        
        assertEquals(arrangedStatetax, retrievedStatetax, "Tax Rate should be the same.");
    }
    
    @Test
    public void getStatetaxDoesNotExist() throws Exception {
        System.out.println("test Service Layer: Get Tax Rate - DoesNotExistException");
        // ARRANGE
        Taxes retrievedStatetax = new Taxes("CA", "California", new BigDecimal("7.77"));
        
        // ACT and ASSERT
        try {
            testService.getStateTax(retrievedStatetax.getStateInitials());
            fail("Tax Rate CA - California Does Not Exist. Should throw exception.");
        } catch(PersistenceException e) {
            fail("Incorrect exception thrown.");
        } catch(DoesNotExistException e) {
            return;
        }
    }
    
    @Test
    public void testGetAllStatetaxes() throws Exception {
        System.out.println("test Service Layer: Get All Tax Rates");
        // ARRANGE
        List<Taxes> arrangedStatetaxList = new ArrayList<>();
        Taxes arrangedStatetax = new Taxes("TX", "Texas", new BigDecimal("1.11"));
        arrangedStatetaxList.add(arrangedStatetax);
        
        // ACT and ASSERT
        List<Taxes> retrievedStatetaxList = testService.getAllStateTaxes();
        assertEquals(arrangedStatetaxList, retrievedStatetaxList, "List of Tax Rates should be equal.");
    }
    
    @Test
    public void testRemoveStatetax() throws Exception {
        System.out.println("test Service Layer: Remove Tax Rate");
        // ARRANGE
        Taxes arrangedStatetax = new Taxes("TX", "Texas", new BigDecimal("1.11"));
        
        // ACT and ASSERT
        Taxes shouldBeTexas = testService.removeStateTax(arrangedStatetax.getStateInitials());
        assertNotNull(shouldBeTexas, "Removing Texas should be NOT NULL");
        assertEquals(arrangedStatetax, shouldBeTexas, "Tax Rate removed should be Texas");
        
        // ACT and ASSERT
        try {
            Taxes shouldBeNull = testService.removeStateTax("CA");
            fail("CA - California does not exist. Should throw exception.");
        } catch(Exception e) {
            return;
        }
    }
    
    // PRODUCT SERVICE LAYER TESTS
    @Test
    public void testCreateValidProduct() throws Exception {
        System.out.println("test Service Layer: Create Product");
        // ARRANGE
        Products newProduct = new Products("Carpet", new BigDecimal("2.01"), new BigDecimal("2.02"));
        
        // ACT
        try {
            testService.createProduct(newProduct);
        } catch (DuplicateEntryException
                | DataValidationException
                | PersistenceException e) {
            // ASSERT
            fail("Product was valid. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testCreateDuplicateEntryProduct() throws Exception {
        System.out.println("test Service Layer: Create Product - DuplicateEntryException");
        // ARRANGE
        Products duplicateProduct = new Products("Tile", new BigDecimal("1.01"), new BigDecimal("1.02"));
        
        // ACT
        try {
            testService.createProduct(duplicateProduct);
            fail("Expected DuplicateEntryException was NOT thrown.");
        } catch (DataValidationException
                | PersistenceException e) {
            // ASSERT
            fail("Incorrect exception was thrown.");
        } catch (DuplicateEntryException e) {
            return;
        }
    }
    
    @Test
    public void testCreateInvalidDataProduct() throws Exception {
        System.out.println("test Service Layer: Create Product - DataValidationException");
        // ARRANGE
        Products invalidProduct = new Products("", new BigDecimal("1.01"), new BigDecimal("1.02"));
        
        // ACT and ASSERT
        try {
            testService.createProduct(invalidProduct);
            fail("Expected DataValidtionException was NOT THROWN");
        } catch (DuplicateEntryException
                | PersistenceException e) {
            fail("Incorrect exception was thrown.");
        } catch (DataValidationException e) {
            return;
        }
    }
    
    @Test
    public void testUpdateProduct() throws Exception {
        System.out.println("test Service Layer: Update Product");
        // ARRANGE
        Products editedProduct = new Products("Laminate", new BigDecimal("3.01"), new BigDecimal("3.02"));
        
        // ACT and ASSERT
        try {
            testService.updateProduct(editedProduct);
        } catch(DataValidationException
                | PersistenceException e) {
            fail("Product update was valid");
        }
    }
    
    @Test
    public void testUpdateProductInvalidData() throws Exception {
        System.out.println("test Service Layer: Update Product - DataValidationException");
        // ARRANGE
        Products editedInvalidProduct = new Products("", new BigDecimal("3.01"), new BigDecimal("3.02"));
        
        // ACT and ASSERT
        try {
            testService.updateProduct(editedInvalidProduct);
            fail("Product was invalid, DataValidationException NOT THROWN");
        } catch(PersistenceException e) {
            fail("Incorrect exception thrown.");
        } catch(DataValidationException e) {
            return;
        }
    }
    
    @Test
    public void testGetProduct() throws Exception {
        System.out.println("test Service Layer: Get Product");
        // ARRANGE
        Products arrangedProduct = new Products("Tile", new BigDecimal("1.01"), new BigDecimal("1.02"));
        Products retrievedProduct = new Products();
        
        // ACT and ASSERT
        try {
            retrievedProduct = testService.getProduct(arrangedProduct.getProductType());
        } catch(DoesNotExistException 
                | PersistenceException e) {
            fail("Product was valid");
        }
        
        assertEquals(arrangedProduct, retrievedProduct, "Products should be the same.");
    }
    
    @Test
    public void testGetProductDoesNotExist() throws Exception {
        System.out.println("test Service Layer: Get Product - DoesNotExistException");
        // ARRANGE
        Products retrievedProduct = new Products("Laminate", new BigDecimal("4.01"), new BigDecimal("4.01"));
        
        // ACT and ASSERT
        try {
            testService.getProduct(retrievedProduct.getProductType());
            fail("Invalid pull expected DoesNotExistException was NOT THROWN");
        } catch(PersistenceException e) {
            fail("Incorrect exception thrown");
        } catch(DoesNotExistException e) {
            return;
        }
    }
    
    @Test
    public void testGetAllProducts() throws Exception {
        System.out.println("test Service Layer: Get All Products");
        // ARRANGE
        List<Products> arrangedProductlist = new ArrayList<>();
        Products arrangedProduct = new Products("Tile", new BigDecimal("1.01"), new BigDecimal("1.02"));
        arrangedProductlist.add(arrangedProduct);
        
        // ACT and ASSERT
        List<Products> retrievedProductlist = testService.getAllProducts();
        assertEquals(arrangedProductlist, retrievedProductlist, "List of products should be equal.");
    }
    
    @Test
    public void testRemoveProduct() throws Exception {
        System.out.println("test Service Layer: Remove Product");
        // ARRANGE
        Products arrangedProduct = new Products("Tile", new BigDecimal("1.01"), new BigDecimal("1.02"));
        
        // ACT and ASSERT
        Products shouldBeTile = testService.removeProduct(arrangedProduct.getProductType());
        assertNotNull(shouldBeTile, "Removing Tile should be NOT NULL");
        assertEquals(arrangedProduct, shouldBeTile, "Product removed should be Tile");
        
        Products shouldBeNull = testService.removeProduct("Laminate");
        assertNull(shouldBeNull, "Removing laminate should BE NULL");
    }
}
