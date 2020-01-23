/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author Asus
 */
public class PathNotFoundException extends Throwable {
    
    public PathNotFoundException(){
        System.out.println("Not possible to reach!");
    }
    
}
