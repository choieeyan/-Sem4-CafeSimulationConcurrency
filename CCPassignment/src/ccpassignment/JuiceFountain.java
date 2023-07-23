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
public class JuiceFountain {
    int jTime;
    public JuiceFountain() { 
    }
    
    public JuiceFountain(int jTime) {
        this.jTime = jTime;
    }
    public void fillGlass() throws InterruptedException{
        System.out.println(Thread.currentThread().getName() + " is fillling glass with JuiceFountain...");
        sleep((long) ((jTime*0.9)*1000));   
        System.out.println(Thread.currentThread().getName() + " is done filling his glass with juice!");
    }
    
}
