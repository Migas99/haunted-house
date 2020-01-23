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
public class VertexNotFoundException extends Throwable {
    
    public VertexNotFoundException(){
        System.out.println("Vertex not found!");
    }
    
    public VertexNotFoundException(String msg){
        System.out.println(msg);
    }
    
}
