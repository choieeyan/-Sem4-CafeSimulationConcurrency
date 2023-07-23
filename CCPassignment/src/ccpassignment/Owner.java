/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccpassignment;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eBay
 */
public class Owner implements Runnable{

    public boolean closingTime = false; 
    Waiter waiter;
    Cafe cafe;
    Customer cs = new Customer();
  
    public Owner(Cafe cafe) {
        this.cafe = cafe;
    }

    
     public void run()
    {
        System.out.println("Owner is ready to serve.");
        while(!closingTime)
        {
            try {
                cafe.serveCustomer();
            } catch (InterruptedException ex) {
                Logger.getLogger(Owner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (closingTime) {
            System.out.println("********************************Cafe is closing soon!********************************");
            System.out.println("Owner: Cafe is not accepting new customers. Customers waiting for seats are requested to leave");
            cs.closingTime = true;
            while(cafe.listCustomer.size()>0){
                System.out.println("Owner: There are " +cafe.listCustomer.size()+" customers at the table left to serve. Next!");  //serve remaining customers seated at the table
                try {
                    cafe.serveCustomer();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Owner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }      
            
            //check table, if it is empty 
            while (cs.semaphore.availablePermits()<10){  
            }
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Owner.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Owner: Looks like everyone left. Let's close down the cafe."); 
            System.out.println("Before going home, owner does does some sanity checks.............");  
            System.out.println("  Number of customers in cafe now: " + (10-cs.semaphore.availablePermits()));
            System.out.println("  Total number of customers who entered the cafe: " + cs.counter.get());
            System.out.println("  Total number of customers managed to served: " + cs.served.get()); 
            System.out.println("  Total number of customers who left due to long waiting time: " + cs.left.get());
            System.out.println("  Total number of customers who was asked to leave due to cafe closing: " + cs.askedLeave.get()); 
            System.out.println("  Total number of juice ordered: " + cafe.juice.get());
            System.out.println("  Total number of cappuccino ordered: " + cafe.cappuccino.get());
            return;  
        }
         
        
        
    }
    
}
