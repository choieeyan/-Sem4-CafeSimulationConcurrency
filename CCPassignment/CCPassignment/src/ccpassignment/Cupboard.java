/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccpassignment;

import static java.lang.Thread.sleep;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author eBay
 */
public class Cupboard{
    int jTime, cTime;
    

    public Cupboard() {
        
    }
    
    public Cupboard(int jTime, int cTime) {
        this.jTime = jTime;
        this.cTime = cTime;
    }
     
    public void getCup() throws InterruptedException{
        System.out.println(Thread.currentThread().getName()+ " is getting a cup from the cupboard now.");
        sleep((long) ((cTime*0.1)*1000));  
    }
    
    public void getGlass() throws InterruptedException{
        System.out.println(Thread.currentThread().getName()+ " is getting a glass from the cupboard now.");
        sleep((long) ((jTime*0.1)*1000));  
        
    }
    
    public void getMilk() throws InterruptedException{
        System.out.println(Thread.currentThread().getName()+ " is getting milk from the cupboard.");
        sleep((long) ((cTime*0.1)*1000));   
    }
    
    public void getCoffee() throws InterruptedException{
        System.out.println(Thread.currentThread().getName()+ " is getting coffee from the cupboard now."); 
        sleep((long) ((cTime*0.1)*1000));  
    }
       
    public void returnMilk() throws InterruptedException{
        System.out.println(Thread.currentThread().getName()+ " returns milk to the cupboard.");  
        sleep((long) ((cTime*0.1)*1000));   
    }
    
    public void returnCoffee() throws InterruptedException{
        System.out.println(Thread.currentThread().getName()+ " returns coffee to the cupboard.");  
        sleep((long) ((cTime*0.1)*1000));  
    }
}
