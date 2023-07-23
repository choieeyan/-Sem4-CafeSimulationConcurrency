/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccpassignment;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eBay
 */
public class CustomerGenerator implements Runnable
{
    Cafe cafe; 
    public boolean closingTime = false;
    int customerNumber;
    int seconds; 
    int jRatio, cRatio;
    
    public CustomerGenerator(Cafe cafe, int customerNumber, int seconds, int jRatio, int cRatio)
    {
        this.cafe = cafe;
        this.customerNumber = customerNumber;
        this.seconds = seconds;
        this.jRatio = jRatio;
        this.cRatio = cRatio; 
    }
 
    public void run()
    {
        int i=0;
        while(!closingTime && i < customerNumber)
        {  
            Customer customer = new Customer(cafe, seconds, jRatio, cRatio); 
            Thread thcustomer = new Thread(customer); 
            thcustomer.start();
            i++;
        }
        if (closingTime) {
            return;}
    }
    
    
}