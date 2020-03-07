/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author ottom
 */
public class Pila {
    private Nodo ultimoValorIngresado;
    public Pila(){
        ultimoValorIngresado = null;
    }
    
    //Método
    public void Insertar(char valor){
        Nodo nuevoNodo = new Nodo();
        nuevoNodo.informacion = valor;
        
        if (ultimoValorIngresado == null) {
            
            nuevoNodo.siguiente = null;
            ultimoValorIngresado = nuevoNodo;
            
        } else {
            nuevoNodo.siguiente = ultimoValorIngresado;
            ultimoValorIngresado = nuevoNodo;
        }
    }
    
    //Extraer
    public char extraer(){
        if (ultimoValorIngresado != null) {
            char informacion = ultimoValorIngresado.informacion;
            ultimoValorIngresado = ultimoValorIngresado.siguiente;
            return informacion;
        } else {
            return Character.MAX_VALUE;
        }
    }
    
    //Pila vacia
    public boolean pilaVacia(){
        return ultimoValorIngresado == null;
        
    }
}
