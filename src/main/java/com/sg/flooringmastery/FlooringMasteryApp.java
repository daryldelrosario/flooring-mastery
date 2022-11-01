/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringMasteryController;
import com.sg.flooringmastery.service.PersistenceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Daryl del Rosario
 */
public class FlooringMasteryApp {
    
    public static void main(String[] args) {
//        UserIO myIO = new UserIOConsoleImpl();
//        FlooringMasteryView myView = new FlooringMasteryView(myIO);
//        FlooringMasteryOrdersDao myOrdersDao = new FlooringMasteryOrdersDaoFileImpl();
//        FlooringMasteryProductsDao myProductsDao = new FlooringMasteryProductsDaoFileImpl();
//        FlooringMasteryTaxDao myTaxesDao = new FlooringMasteryTaxDaoFileImpl();
//        FlooringMasteryAuditDao myAuditDao = new FlooringMasteryAuditDaoFileImpl();
//        FlooringMasteryServiceLayer myService = new FlooringMasteryServiceLayerImpl(myOrdersDao, myProductsDao, myTaxesDao, myAuditDao);
//        FlooringMasteryController controller = new FlooringMasteryController(myService, myView);
//        controller.runUI();
// ABOVE CODE FOR PRE-SPRING DI
        ApplicationContext ctx = new ClassPathXmlApplicationContext("appContext.xml");
        FlooringMasteryController controller = ctx.getBean("controller", FlooringMasteryController.class);
        controller.runUI();
    }
}
