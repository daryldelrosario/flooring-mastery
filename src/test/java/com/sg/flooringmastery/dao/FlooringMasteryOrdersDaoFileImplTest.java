/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.service.PersistenceException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryOrdersDaoFileImplTest {
    
    FlooringMasteryOrdersDao testDaoOrder;
    
    public FlooringMasteryOrdersDaoFileImplTest() {
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        PrintWriter out;
        String testFolder = "Test/TestOrders";
        String testFile = "Test/TestOrders/Orders_";
        String testDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
        String writeToFile = testFile + testDate;
        try {
            out = new PrintWriter(new FileWriter(writeToFile));
        } catch(IOException e) {
            throw new PersistenceException("Could not save data.", e);
        }
        out.println("HEADER");
        out.flush();
        out.close();
        
        testDaoOrder = new FlooringMasteryOrdersDaoFileImpl(testFolder, testFile);
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        new FileWriter("Test/TestOrders/Orders_" 
                + LocalDate.now().format(DateTimeFormatter.ofPattern("MMddyyyy")) 
                + ".txt");
        
        new FileWriter("Test/TestOrders/Orders_"
                + LocalDate.now().plusDays(11).format(DateTimeFormatter.ofPattern("MMddyyyy"))
                + ".txt");
    }

    @Test
    public void testAddGetOrder() throws Exception {
        System.out.println("test Order Dao: Add and Get Order");
        // ARRANGE
        List<Orders> arrangedOrderlist = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
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
        arrangedOrderlist.add(orderOne);
        
        // ACT
        testDaoOrder.addOrder(today, orderOne);
        Orders retrievedOrder = testDaoOrder.getOrder(arrangedOrderlist, orderNumberOne);
        
        // ASSERT
        assertEquals(retrievedOrder, orderOne, "Orders should be equal");
    }
    
    @Test
    public void testAddGetAllOrders() throws Exception {
        System.out.println("test Order Dao: Add and Get All Orders");
        // ARRANGE
        List<Orders> arrangedOrderlist = new ArrayList<>();
        LocalDate today = LocalDate.now();
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
        arrangedOrderlist.add(orderOne);

        int orderNumberTwo = 2;
        String customerNameTwo = "Two";
        String stateInitialsTwo = "CA";
        String stateNameTwo = "California";
        BigDecimal taxRateTwo = new BigDecimal("2.22");
        String productTypeTwo = "Carpet";
        BigDecimal areaTwo = new BigDecimal("222");
        BigDecimal costPerSquareFootTwo = new BigDecimal("2.22");
        BigDecimal laborCostPerSquareFootTwo = new BigDecimal("22.2");
        Orders orderTwo = new Orders(orderNumberTwo, customerNameTwo, stateInitialsTwo, stateNameTwo, 
            taxRateTwo, productTypeTwo, areaTwo, 
            costPerSquareFootTwo, laborCostPerSquareFootTwo);
        arrangedOrderlist.add(orderTwo);
        
        // ACT and ASSERT
        testDaoOrder.addOrder(today, orderOne);
        testDaoOrder.addOrder(today, orderTwo);
        List<Orders> retrievedList = testDaoOrder.getAllOrders(today);
        Orders retrievedOrderOne = testDaoOrder.getOrder(retrievedList, orderNumberOne);
        Orders retrievedOrderTwo = testDaoOrder.getOrder(retrievedList, orderNumberTwo);
        
        assertNotNull(retrievedList, "The list of orders must be NOT NULL");
        assertEquals(2, retrievedList.size(), "The list of orders should have two (2) orders");
        assertTrue(retrievedList.contains(retrievedOrderOne), "The list of orders should contain Order One");
        assertTrue(retrievedList.contains(retrievedOrderTwo), "The list of orders should contain Order Two");
        assertNotEquals(retrievedOrderOne, retrievedOrderTwo, "The orders should NOT BE EQUAL");
    }
    
    @Test
    public void testAddAndGetAllOrderDates() throws Exception {
        System.out.println("test Order Dao: Add and Get All Order Dates");
        // ARRANGE
        List<Orders> arrangedOrderlist = new ArrayList<>();
        LocalDate today = LocalDate.now();
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
        arrangedOrderlist.add(orderOne);
            
        LocalDate futureDate = LocalDate.now().plusDays(11);
        int orderNumberTwo = 2;
        String customerNameTwo = "Two";
        String stateInitialsTwo = "CA";
        String stateNameTwo = "California";
        BigDecimal taxRateTwo = new BigDecimal("2.22");
        String productTypeTwo = "Carpet";
        BigDecimal areaTwo = new BigDecimal("222");
        BigDecimal costPerSquareFootTwo = new BigDecimal("2.22");
        BigDecimal laborCostPerSquareFootTwo = new BigDecimal("22.2");
        Orders orderTwo = new Orders(orderNumberTwo, customerNameTwo, stateInitialsTwo, stateNameTwo, 
                taxRateTwo, productTypeTwo, areaTwo, 
                costPerSquareFootTwo, laborCostPerSquareFootTwo);
        arrangedOrderlist.add(orderTwo);
        
        // ACT and ASSERT
        testDaoOrder.addOrder(today, orderOne);
        testDaoOrder.addOrder(futureDate, orderTwo);
        List<LocalDate> retrievedDates = testDaoOrder.getAllOrderDates();
        
        assertEquals(2, retrievedDates.size(), "List of dates should be two (2)");
        assertTrue(retrievedDates.contains(today), "List of dates should contain today");
        assertTrue(retrievedDates.contains(futureDate), "List of dates should contain future date.");
    }
    
    @Test
    public void testEditOrder() throws Exception {
        System.out.println("test Order Dao: Edit an Order");
        // ARRANGE
        List<Orders> arrangedList = new ArrayList<>();
        LocalDate today = LocalDate.now();
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
        arrangedList.add(orderOne);
        
        int orderNumberEdit = 1;
        String customerNameEdit = "DNAProductions, INC.";
        String stateInitialsEdit = "ON";
        String stateNameEdit = "Ontario";
        BigDecimal taxRateEdit = new BigDecimal("1.43");
        String productTypeEdit = "Soft Carpet";
        BigDecimal areaEdit = new BigDecimal("143");
        BigDecimal costPerSquareFootEdit = new BigDecimal("1.43");
        BigDecimal laborCostPerSquareFootEdit = new BigDecimal("14.3");
        Orders orderEdit = new Orders(orderNumberEdit, customerNameEdit, stateInitialsEdit, stateNameEdit,
                taxRateEdit, productTypeEdit, areaEdit,
                costPerSquareFootEdit, laborCostPerSquareFootEdit);
        
        // ACT and ASSERT
        testDaoOrder.addOrder(today, orderOne);
        List<Orders> retrievedOrderlist = testDaoOrder.getAllOrders(today);
        
        assertEquals(1, retrievedOrderlist.size(), "The list of orders should only have one (1)");
        assertTrue(retrievedOrderlist.contains(orderOne), "List of orders should contain orderOne");
        assertFalse(retrievedOrderlist.contains(orderEdit), "List of orders should NOT CONTAIN orderEdit");
        
        // ACT and ASSERT
        testDaoOrder.editOrder(today, retrievedOrderlist, orderEdit);
        retrievedOrderlist = testDaoOrder.getAllOrders(today);
        
        assertEquals(1, retrievedOrderlist.size(), "The list of orders should still only have one (1)");
        assertTrue(retrievedOrderlist.contains(orderEdit), "List of orders should now contain orderEdit");
        assertFalse(retrievedOrderlist.contains(orderOne), "List of orders should NOT CONTAIN orderOne");
        assertNotEquals(orderOne, orderEdit, "This two orders should NOT BE Equal");
        assertEquals(1, retrievedOrderlist.get(0).getOrderNumber(), "Order numbers should still be one (1)");
    }
    
    @Test
    public void testRemoveOrder() throws Exception {
        System.out.println("test Order Dao: Remove an Order");
        // ARRANGE
        List<Orders> arrangedOrderlist = new ArrayList<>();
        LocalDate today = LocalDate.now();
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
        arrangedOrderlist.add(orderOne);
        
        int orderNumberTwo = 2;
        String customerNameTwo = "Two";
        String stateInitialsTwo = "CA";
        String stateNameTwo = "California";
        BigDecimal taxRateTwo = new BigDecimal("2.22");
        String productTypeTwo = "Carpet";
        BigDecimal areaTwo = new BigDecimal("222");
        BigDecimal costPerSquareFootTwo = new BigDecimal("2.22");
        BigDecimal laborCostPerSquareFootTwo = new BigDecimal("22.2");
        Orders orderTwo = new Orders(orderNumberTwo, customerNameTwo, stateInitialsTwo, stateNameTwo, 
                taxRateTwo, productTypeTwo, areaTwo, 
                costPerSquareFootTwo, laborCostPerSquareFootTwo);
        arrangedOrderlist.add(orderTwo);
        
        // ACT and ASSERT
        testDaoOrder.addOrder(today, orderOne);
        testDaoOrder.addOrder(today, orderTwo);
        List<Orders> retrievedOrder = testDaoOrder.getAllOrders(today);
        assertEquals(arrangedOrderlist.size(), retrievedOrder.size(), "List should have two (2) orders.");
        
        // ACT and ASSERT
        testDaoOrder.removeOrder(today, arrangedOrderlist, orderOne);
        assertEquals(1, arrangedOrderlist.size(), "List should have one (1) order.");
        assertTrue(arrangedOrderlist.contains(orderTwo), "List should only have orderTwo");
        assertFalse(arrangedOrderlist.contains(orderOne), "List should NOT CONTAIN orderOne");
        
        // ACT and ASSERT
        testDaoOrder.removeOrder(today, arrangedOrderlist, orderTwo);
        assertTrue(arrangedOrderlist.isEmpty(), "List should now be empty");
        assertFalse(arrangedOrderlist.contains(orderOne), "List should NOT CONTAIN orderOne");
        assertFalse(arrangedOrderlist.contains(orderTwo), "List should NOT CONTAIN orderTwo");
    }
}

// Template for Arranged Orders
/*
--- ARRANGED ORDER ONE ---
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

--- ARRANGED ORDER TWO ---
int orderNumberTwo = 2;
String customerNameTwo = "Two";
String stateInitialsTwo = "CA";
String stateNameTwo = "California";
BigDecimal taxRateTwo = new BigDecimal("2.22");
String productTypeTwo = "Carpet";
BigDecimal areaTwo = new BigDecimal("222");
BigDecimal costPerSquareFootTwo = new BigDecimal("2.22");
BigDecimal laborCostPerSquareFootTwo = new BigDecimal("22.2");
Orders orderTwo = new Orders(orderNumberTwo, customerNameTwo, stateInitialsTwo, stateNameTwo, 
        taxRateTwo, productTypeTwo, areaTwo, 
        costPerSquareFootTwo, laborCostPerSquareFootTwo);
*/
