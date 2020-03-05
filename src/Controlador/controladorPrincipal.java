/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.awt.event.ActionListener;
import Vista.*;
import Principal.*;
import java.awt.event.ActionEvent;
import java.util.Stack;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ottom
 */
public class controladorPrincipal implements ActionListener{
    
    jdPrincipal vistaPrincipal = new jdPrincipal(null, true);
    
    public controladorPrincipal(jdPrincipal principal){
        vistaPrincipal = principal;
        vistaPrincipal.btnCalcular.addActionListener(this);
        vistaPrincipal.btnBorrar.addActionListener(this);
        vistaPrincipal.btnSalir.addActionListener(this);    
    }
    
    /*public void llenarTabla(JTable tablaD){
        DefaultTableModel modeloT = new DefaultTableModel();
        tablaD.setModel(modeloT);
        
        modeloT.addColumn("TOKEN");
        modeloT.addColumn("ATRIBUTO");
        modeloT.addColumn("OBSERVACION");
        
        Object[] columna = new Object[3];
        
       
        
    }*/
    
    @Override
    public void actionPerformed(ActionEvent e){
        
        if(vistaPrincipal.btnCalcular == e.getSource()){
            String operacion = vistaPrincipal.txtOperacionAritmetica.getText();
            //char[] caracteres = operacion.toCharArray();
            Stack pila = new Stack();
            /*for(char output : caracteres){
                System.out.println(output);
            }*/
            for(int i = 0;i<operacion.length();i++){
                pila.add(operacion.charAt(i));
                System.out.println("No "+i+" contenido "+pila.pop());
            }
            //System.out.println(pila.pop());
        }
        if(vistaPrincipal.btnBorrar == e.getSource()){
            
        }
        if(vistaPrincipal.btnSalir == e.getSource()){
            System.exit(0);
        }
        
    }
    
}
