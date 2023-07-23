/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccpassignment;

import java.util.Random;

/**
 *
 * @author eBay
 */
public class Main {
 
    public static void main(String[] args) throws InterruptedException { 
        int customerNumber = 20; //how many customer enters
        int seconds = 30; //cafe last call time
        
        //juice and cappuccino ratio
        int juiceRatio = 1;
        int cappuccinoRatio = 2; 
        
        
        //time to make cappuccino and juice in seconds
        int juiceTime = 3;
        int cappuccinoTime = 3;
         
        
        Cafe cafe = new Cafe(juiceTime, cappuccinoTime);
        CustomerGenerator customerGenerator = new CustomerGenerator(cafe, customerNumber, seconds, juiceRatio, cappuccinoRatio);  
        Waiter waiter = new Waiter(cafe);
        Owner owner = new Owner(cafe);
        Clock clock = new Clock(customerGenerator, owner, waiter, seconds, cafe); 
       // Clock clock = new Clock(customerGenerator, owner, seconds); 
        Thread cg = new Thread(customerGenerator);
        Thread ow = new Thread(owner);
        Thread wt = new Thread(waiter);
        Thread ck = new Thread(clock);
        
        ow.setName("Owner");
        wt.setName("Waiter"); 
        ck.start();
        cg.start(); 
        ow.start(); 
        wt.start(); 
         
          
    }
    
}
