/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccpassignment;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eBay
 */
public class Clock extends Thread{
    CustomerGenerator cg;  
    Owner ow; 
    int seconds;  
    Waiter wt; 
    Cafe cafe;
     
    public Clock(CustomerGenerator cg, Owner ow, Waiter wt, int seconds, Cafe cafe){
        this.cg = cg; 
        this.ow = ow; 
        this.wt = wt;
        this.seconds = seconds; 
        this.cafe = cafe;
    }
    
    //constructor without waiter
    public Clock(CustomerGenerator cg, Owner ow, int seconds){
        this.cg = cg; 
        this.ow = ow;  
        this.seconds = seconds;
        
    }
    
    public void run(){
        System.out.println("Cafe is open for business!");
        try {
            TimeUnit.SECONDS.sleep((long)(seconds));
            NotifyClosed();
         } catch (InterruptedException ex) {
            Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
   
    public synchronized void NotifyClosed(){
        cg.closingTime = true;
        ow.closingTime = true; 
        wt.closingTime = true;  
        cafe.closingTime = true;
        cafe.notifyClosed();
    }
 
}