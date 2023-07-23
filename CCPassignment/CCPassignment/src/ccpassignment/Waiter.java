/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccpassignment;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eBay
 */
public class Waiter implements Runnable{
    public boolean closingTime = false;
    Cafe cafe;
    
 
    public Waiter(Cafe cafe) {
        this.cafe = cafe;
    }
    
    
    public void run()
    {
        System.out.println("Waiter is ready to serve.");
        while(!closingTime)
        {
            try {
                cafe.serveCustomer();
            } catch (InterruptedException ex) {
                Logger.getLogger(Waiter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
        if (closingTime) { 
            while(cafe.listCustomer.size()>0){
                try {  
                     cafe.serveCustomer();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Waiter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //waiter leaves on time once he's done serving all customers
            System.out.println("Waiter: Looks like there is no customers left to serve. Waiter is leaving the cafe now. Goodbye!"); 
            return;
        }
    }
}
