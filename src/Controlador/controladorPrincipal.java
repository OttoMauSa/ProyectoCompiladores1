/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Pila;
import java.awt.event.ActionListener;
import Vista.*;
import Principal.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ottom
 */
public class controladorPrincipal implements ActionListener{
    char C[];
    int index = 0;
    int numID;
    int filaTodo;
    int filaSimb;
    
    jdPrincipal vistaPrincipal = new jdPrincipal(null, true);
    
    public controladorPrincipal(jdPrincipal principal){
        vistaPrincipal = principal;
        vistaPrincipal.btnLexico.addActionListener(this);
        vistaPrincipal.btnSintactico.addActionListener(this);
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
    
    public boolean validarExpresion(){
        Pila pila = new Pila();
        String cadena = vistaPrincipal.txtOperacionAritmetica.getText();
        for(int i = 0; i<cadena.length();i++){
            if (cadena.charAt(i)=='(' || cadena.charAt(i)=='{' || cadena.charAt(i)=='[') {
                pila.Insertar(cadena.charAt(i));
                
            } else {
                if (cadena.charAt(i)==')') {
                    if(pila.extraer()!='('){
                        return false;
                    }
                } else {
                    if (cadena.charAt(i)=='}') {
                        if (pila.extraer()!='{') {
                            return false;
                        }
                    } else {
                        if (cadena.charAt(i)==']') {
                            if (pila.extraer()!='[') {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return pila.pilaVacia();
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        
        if(vistaPrincipal.btnLexico == e.getSource()){
            int token, vAd, aux[];
            int indexAnterior = 0;
            C = this.vistaPrincipal.txtOperacionAritmetica.getText().toCharArray();

            aux = buscar();
            token = aux[0];
            vAd = aux[1];
            
            while (token != -1 && token != 0) {
            try {
                guardar(token, vAd, indexAnterior);
            } catch (IOException ex) {
                System.out.print("Error...");
            } catch (ClassNotFoundExeption ex) {
                Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
                indexAnterior = index;
                aux = buscar();
                token = aux[0];
                vAd = aux[1];
            }
            if (token != -1) {
                this.vistaPrincipal.txtComent.setText("Terminado con exito...");
            }
            index = 0;
        }
        if(vistaPrincipal.btnSintactico == e.getSource()){
            if (validarExpresion()) {
                JOptionPane.showMessageDialog(null, "La formula esta escrita correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error");
            }
        }
        if(vistaPrincipal.btnBorrar == e.getSource()){
            for (int i = 0; i < filaTodo; i++) {
                this.vistaPrincipal.jtDatos.setValueAt(null, i, 0);
                this.vistaPrincipal.jtDatos.setValueAt(null, i, 1);
                this.vistaPrincipal.jtDatos.setValueAt(null, i, 2);
            }

            index = 0;
            this.vistaPrincipal.txtComent.setText("");
            this.numID = 0;
            this.filaTodo = 0;
            vistaPrincipal.txtOperacionAritmetica.setText("");
        }
        if(vistaPrincipal.btnSalir == e.getSource()){
            System.exit(0);
        }
        
    }
    private boolean esEspacio(char x) {
        return x == ' ' || x == '\t';
    }
    private int[] buscar() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (index >= C.length) {
            return new int[]{0, 0};
        }
        while (index < C.length && esEspacio(C[index])) {
            index++;
        }
        char aux;
        if (index < C.length) {
            aux = C[index];
        } else {
            return error();
        }
        switch (aux) {
            case '<': { //read print <<=>=<-->
                if (++index < C.length) {
                    if (C[index] == '-') {
                        index++;
                        return new int[]{3, '<' + '-'};
                    }
                    if (C[index] == '>') {
                        index++;
                        return new int[]{3, '<' + '>'};
                    }
                    if (C[index] == '=') {
                        index++;
                        return new int[]{3, '<' + '='};
                    }
                }
                return new int[]{3, '<'};
            }
            case '>': {
                if (++index < C.length && C[index] == '=') {
                    index++;
                    return new int[]{3, '>' + '='};
                }
                return new int[]{3, '>'};
            }
            case '=': {
                index++;
                return new int[]{3, '='};
            }
            case '+': {
                index++;
                return new int[]{3, '+'};
            }
            case '-': {
                index++;
                if (index < C.length && C[index] == '>') {
                    index++;
                    return new int[]{3, '-' + '>'};
                }
                return new int[]{3, '-'};
            }
            case '*': {
                index++;
                return new int[]{3, '*'};
            }
            case '/': {
                index++;
                if (index < C.length && C[index] == '/') {
                    while (index < C.length) {
                        index++;
                    }
                    return new int[]{1, 0};
                }
                return new int[]{3, '/'};
            }
            case '%': {
                index++;
                return new int[]{3, '%'};
            }
            case '&': {
                index++;
                return new int[]{3, '&'};
            }
            case '|': {
                index++;
                return new int[]{3, '|'};
            }
            case '¬': {
                index++;
                return new int[]{3, '¬'};
            }
            case ',': {
                index++;
                return new int[]{2, ','};
            }
            case ':': {
                index++;
                return new int[]{2, ':'};
            }
            case '$': {
                index++;
                return new int[]{2, '$'};
            }
            case ')': {
                index++;
                return new int[]{2, ')'};
            }
            case '[': {
                index++;
                return new int[]{2, '['};
            }
            case ']': {
                index++;
                return new int[]{2, ']'};
            }
            case '{': {
                index++;
                return new int[]{2, '{'};
            }
            case '}': {
                index++;
                return new int[]{2, '}'};
            }
            case '\'': {
                index++;
                if (++index < C.length && C[index] == '\'') {
                    index++;
                    return new int[]{502, 0};
                }
                return error();
            }
            case '\"': {
                while (++index < C.length && C[index] != '\"') {
                }
                if (index < C.length) {
                    index++;
                    return new int[]{503, 0};
                }
                return error();
            }
            case '(': {
                index++;
                if (index < C.length) {
                    aux = C[index];
                } else {
                    return new int[]{2, '('};
                }
                if (aux == '*') {
                    boolean t;
                    index++;
                    if (index < C.length) {
                        aux = C[index];
                    } else {
                        return error();
                    }
                    do {
                        t = false;
                        while (index < C.length && aux != '*') {
                            aux = C[index];
                            index++;
                        }
                        if (index < C.length) {
                            if (aux == C[index]) {
                                index++;
                            }
                            if (index < C.length) {
                                aux = C[index];
                            } else {
                                return error();
                            }
                            aux = C[index];
                        } else {
                            return error();
                        }
                        if (aux == ')') {
                            index++;
                            return new int[]{1, 0};
                        } else {
                            t = true;
                        }
                    } while (t);
                } else {
                    return new int[]{2, '('};
                }
                break;
            }
        }

        if (Character.isAlphabetic(aux) || aux == '_') {
            if (++index < C.length && ((C[index] >= 'a' && C[index] <= 'z')
                    || (C[index] >= 'A' && C[index] <= 'Z')
                    || Character.isDigit(C[index]))
                    && !esEspacio(C[index])) {
                while (++index < C.length && ((C[index] >= 'a' && C[index] <= 'z')
                        || (C[index] >= 'A' && C[index] <= 'Z')
                        || Character.isDigit(C[index]))
                        && !esEspacio(C[index])) {
                }
            }
            return new int[]{1000, 0};
        }
        if (Character.isDigit(aux)) {
            while (++index < C.length && Character.isDigit(C[index])) {
            }
            if (index < C.length && C[index] == '.') {//var a, b<-9.23, c<-3:float
                if (++index < C.length && Character.isDigit(C[index])) {
                    while (++index < C.length && Character.isDigit(C[index])) {
                    }
                    return new int[]{501, 0};
                } else {
                    return error();
                }
            } else {
                if (!Character.isDigit(aux)) {
                    index--;
                }
                return new int[]{500, 0};
            }
        } else {
            return error();
        }
    }
    
    private int[] error() {
        this.vistaPrincipal.txtComent.setText("Para por error...");
        return new int[]{-1, 0};
    }
    
    private void guardar(int token, int vAd, int indexAnt) throws IOException, ClassNotFoundExeption {
        // throw new UnsupportedOperationException("Not supported yet.");
        String desc = ""; //descripcion
        String dato = "";
        for (int i = indexAnt; i < index; i++) {
            if (!esEspacio(C[i])) {
                dato += String.valueOf(C[i]);
            }
        }
        switch (token) {
            case 1: {    //Comentario
                System.out.println("Hubo un comentario...");
                return;
            }

            case 2: {
                desc = "Simbolo: ";
                switch (vAd) {
                    case ':': {
                        desc += "Dos puntos";
                        break;
                    }
                    case ',': {
                        desc += "Coma";
                        break;
                    }
                    case '$': {
                        desc += "Separador de sentencias for";
                        break;
                    }
                    case '#': {
                        desc += "Protegido";
                        break;
                    }
                    case '(': {
                        desc += "Abre parentesis";
                        break;
                    }
                    case ')': {
                        desc += "Cierra parentesis";
                        break;
                    }
                    case '[': {
                        desc += "Abre corchetes";
                        break;
                    }
                    case ']': {
                        desc += "Cierra corchetes";
                        break;
                    }
                    case '{': {
                        desc += "Abre llaves";
                        break;
                    }
                    case '}': {
                        desc += "Cierra llaves";
                        break;
                    }
                }
                token += vAd;
                break;
            }
            case 3: {
                desc = "Operador ";
                switch (vAd) {
                    case '<': {
                        desc += "relacional: Menor";
                        break;
                    }
                    case '<' + '=': {
                        desc += "relacional: Menor o igual";
                        break;
                    }
                    case '>': {
                        desc += "relacional: Mayor";
                        break;
                    }
                    case '>' + '=': {
                        desc += "relacional: Mayor o igual";
                        break;
                    }
                    case '<' + '>': {
                        desc += "relacional: Diferente";
                        break;
                    }
                    case '=': {
                        desc += "relacional: Igual";
                        break;
                    }
                    case '+': {
                        desc += "aritmetico: Suma (publico)";
                        break;
                    }
                    case '-': {
                        desc += "aritmetico: Resta (privado)";
                        break;
                    }
                    case '*': {
                        desc += "aritmetico: Multiplicacion";
                        break;
                    }
                    case '/': {
                        desc += "aritmetico: Division";
                        break;
                    }
                    case '%': {
                        desc += "aritmetico: Resto de la division";
                        break;
                    }
                    case '&': {
                        desc += "logico: AND";
                        break;
                    }
                    case '|': {
                        desc += "logico: OR";
                        break;
                    }
                    case '¬': {
                        desc += "logico: NOT";
                        break;
                    }
                    case '<' + '-': {
                        desc += "de Asignacion";
                        break;
                    }
                    case '-' + '>': {
                        desc += "de Acceso";
                        break;
                    }
                }
                token += vAd;
                break;
            }
            case 500: {
                desc = "Entero";
                break;
            }
            case 501: {
                desc = "Real";
                break;
            }
            /*case 502: {
                desc = "Caracter";
                break;
            }
            case 503: {
                desc = "Cadena";
                break;
            }*/
            default: {
                System.out.println("Error...");
                return;
            }
        }
        // token, des, lexema
        this.vistaPrincipal.jtDatos.setValueAt(String.valueOf(token), this.filaTodo, 0);
        this.vistaPrincipal.jtDatos.setValueAt(String.valueOf(desc), this.filaTodo, 1);
        this.vistaPrincipal.jtDatos.setValueAt(dato, this.filaTodo, 2);
        filaTodo++;
    }
    
    private boolean esEspacio(char x, char[] extra) {
        boolean t = x == ' ';
        if (!t) {
            for (char aux : extra) {
                if (x == aux) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
}
