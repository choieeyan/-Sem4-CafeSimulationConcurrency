/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccpassignment;

import static java.lang.Thread.sleep;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author eBay
 */
public class Cafe {

    List<Customer> listCustomer;  
    Lock cupboardlk = new ReentrantLock(); 
    Lock juicefountainlk = new ReentrantLock(); 
    Lock ingredient = new ReentrantLock(); 
    int  jTime, cTime;
    int order;
    
    public boolean closed=false;
    public boolean closingTime= false;
    
    static AtomicInteger juice = new AtomicInteger();	
    static AtomicInteger cappuccino = new AtomicInteger();	
    
    public Cafe(int jTime, int cTime) {
        listCustomer = new LinkedList<Customer>();  
        this.jTime = jTime;
        this.cTime = cTime;
       
    } 
     
    
    public void serveCustomer() throws InterruptedException
    {
        
        Cupboard cb = new Cupboard(jTime, cTime);
        JuiceFountain jf = new JuiceFountain(jTime);
        Customer customer;
        boolean ingredients = true;
        
        synchronized (listCustomer)
        {
             
            while(listCustomer.size()==0 && closed == false) 
            { 
                try
                { 
                    listCustomer.wait(); //wait until customer arrive and gotseat
                }
                catch(InterruptedException iex)
                {
                    iex.printStackTrace();
                }
            }
            
            //if closing time is true we also get out of wait and we check if there is still customer at the table since it may be closingTime but we may still have customers at the table
            if(listCustomer.size()==0 && closingTime){
                return;
            }
            
            customer = (Customer)((LinkedList<?>)listCustomer).poll();  //remove item from list
            System.out.println(Thread.currentThread().getName() + " found " +customer.getName()+ " waiting at the table."); 
        }
        
        
        customer.takeOrder();  
        
        if(customer.getOrder() == 2){ 
            cappuccino.incrementAndGet(); 
            System.out.println(Thread.currentThread().getName() + " goes to cupboard to collect ingrediants...");
 
            ingredient.lock();    //locks ingredient in case other server who also serves coffee does not hog the cupboard while waiting for ingredient, causing deadlock as this thread tries to return ingredients to cupboard
            cupboardlk.lock();    //only able to acquire cupboard lock if thread has the ingredient lock  
            cb.getCup(); 
            cb.getMilk(); 
            cb.getCoffee();
            cupboardlk.unlock(); //unlocks cupboard in case the other server needs to collect glass for juice while this thread is mixing the ingredients- for time efficiency
            
            System.out.println(Thread.currentThread().getName() + " is mixing the ingredients...");
            sleep((long) ((cTime*0.5)*1000)); //50 percent of time to make coffee to mix ingredients 
            
            cupboardlk.lock(); //acquires lock for cupboard again to return the ingredients
            cb.returnMilk();
            cb.returnCoffee();
            ingredient.unlock(); //unlocks ingredient lock for other server tyrig to get the ingredients
            cupboardlk.unlock();
            
        }else{ 
            juice.incrementAndGet(); 
            System.out.println(Thread.currentThread().getName() + " goes to cupboard to get a glass...");
            cupboardlk.lock();
            cb.getGlass();
            cupboardlk.unlock(); 
            
            System.out.println(Thread.currentThread().getName() + " goes to JuiceFountain to fill glass with juice...");
            juicefountainlk.lock();  
            jf.fillGlass();
            juicefountainlk.unlock();
        } 
        System.out.println(Thread.currentThread().getName() + " finished preparing drink and is ready to serve " +customer.getName());
        customer.serveDrink();
        
    } 
    
 
    public void getSeat(Customer customer)
    { 
        
        synchronized (listCustomer)
        { 
            ((LinkedList<Customer>)listCustomer).offer(customer); //adds to tail 
            if(listCustomer.size()==1){
                System.out.println(customer.getName()+ " waiting to order!");
                listCustomer.notifyAll();
            }
        
        }
    }
    
    public synchronized void ordered(){ 
        notify();
    }
    
    public void notifyClosed(){ //this method is to ensure that waiter or owner are not stuck at wait() forever
        //called by clock during closingTime
        synchronized (listCustomer)
        { 
            closed = true;
            listCustomer.notifyAll(); 
        }
    }

}
      