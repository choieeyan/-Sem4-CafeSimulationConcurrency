/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccpassignment;

import static ccpassignment.Cafe.cappuccino;
import static java.lang.Thread.sleep;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eBay
 */
public class Customer implements Runnable {
    static Semaphore semaphore = new Semaphore(10);
    
    String name;
    Date inTime;
    int order; 
    
    Cafe cafe;
    Cupboard cupb;  
    static public boolean closingTime = false;
    int seconds;
    static AtomicInteger counter = new AtomicInteger();	
    static AtomicInteger served = new AtomicInteger();	
    static AtomicInteger left = new AtomicInteger();	
    static AtomicInteger askedLeave = new AtomicInteger();
    
    int jRatio, cRatio;
    int[] ratio;

    public Customer() {
    }
    
    
    
    public Customer(Cafe cafe, int seconds, int jRatio, int cRatio)
    {
        this.cafe = cafe; 
        this.seconds = seconds;
        this.jRatio = jRatio;
        this.cRatio = cRatio;
    }
 
    public String getName() {
        return name;
    }
 
    public Date getInTime() {
        return inTime;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }
    
    public int getOrder(){
        return order;
    }
    
    public void setOrder(){
        this.order = order;
    }
 
    public void run()
    {
        try {
            TimeUnit.SECONDS.sleep((long)(Math.random()*(seconds)));     //customer enters cafe randomly in 30 seconds interval 
            setInTime(new Date());
            enterCafe();
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    public synchronized void enterCafe() throws InterruptedException
    {     
        name = "Customer " +counter.incrementAndGet(); 
        System.out.println(name+ " entered the cafe at "+inTime);
        System.out.println("There are " + semaphore.availablePermits() + " seats available at the table.");  
        if(semaphore.tryAcquire(new Random().nextInt(seconds), TimeUnit.SECONDS)){  
            if(closingTime){
                semaphore.release();
                askedLeave.incrementAndGet();
                System.out.println(name+ " is asked to leave as cafe is closing soon.");
                System.out.println(name+ " left.");
            }else{
                served.incrementAndGet();
                System.out.println(name+ " got a seat!"); 
                cafe.getSeat(this); 
                wait(); //wait to order
                
                
                //generate order
                //creating an array to put ratio of each drink into arrays
                ratio = new int[jRatio + cRatio];
                //first we put juice into array first. For example, if ratio of juice is 1, we put 1 juice into array
                for(int i=0; i<jRatio; i++){ 
                    ratio[i] = 1;  // the 1 in array ratio signifies juice
                }
                //next we put cappuccino into array. for example, if ratio cuppocino is 2, we put 2 cuppocino into array.
                for (int j=jRatio; j<(jRatio+cRatio); j++){
                    ratio[j] = 2;  //the 2 in array ratio signifies cuppocino
                } 
                //the end result: if ratio of juice to cappuccino is 1:2, the array will have 1 juice integer and 2 cuppocino integer. We randomly pick from the array thus resulting in somewhat fixed probability of picking either drinks.

                Random r = new Random();
                int i = r.nextInt(ratio.length);
                if(ratio[i] == 1){  
                   System.out.println(name + " ordered Juice!");
                   order = 1;
                }else{
                    System.out.println(name + " ordered Cappuccino!");
                    order = 2;
                }  
                
                notify(); //notify server they ordered  
                wait(); //wait to receive drink
                TimeUnit.SECONDS.sleep((long)(Math.random()*3));  //drinking their drink within 3 second or less
                exitCafe();  
            }
        }else{ 
            if(closingTime){    
                askedLeave.incrementAndGet();
                System.out.println(name+ " is asked to leave as cafe is closing soon.");
                System.out.println(name+ " left.");
            }else{
                left.incrementAndGet();
                System.out.println(name+ " is tired of waiting! Bye");
            } 
        } 
    }
     
    public synchronized void serveDrink() throws InterruptedException
    { 
        System.out.println(name+ " received their drink!");
        notify();  
    }
    
    public synchronized void exitCafe()
    {
        System.out.println(name+ " has finished their drink and is ready to leave!");
        semaphore.release(); //release semaphore for table seats
        System.out.println(name+ " paid and left the cafe.");  
        
    }
    
    public synchronized void takeOrder() throws InterruptedException
    {
        System.out.println(Thread.currentThread().getName()+ " takes " +name+ "'s order");
        notify(); //notify customoer who's waiting to rake order
        wait();  //wait for customer's order
    }
    
    
}
