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
        vistaPrincipal.btnSemantico.addActionListener(this);
        vistaPrincipal.btnBorrar.addActionListener(this);
        vistaPrincipal.btnSalir.addActionListener(this);    
    }
    
    //Sintactico
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
        if(vistaPrincipal.btnSemantico == e.getSource()){
            
            String expresion;
            expresion = vistaPrincipal.txtOperacionAritmetica.getText();
            Analizador analizador = new Analizador();
            try {
                Double s = analizador.evaluar(expresion);
                String cadena = String.valueOf(s);
                vistaPrincipal.txtResultado.setText(cadena);
            }catch(Excepciones exc) {
                JOptionPane.showMessageDialog(null, exc);
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
    
    //Lexico
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
    
    //Semantico
    class Excepciones extends Exception {
        String errStr; // Muestra el error
        public Excepciones(String str) {
            errStr = str;
        }
        public String toString() {
            return errStr;
        }
    }

    class Analizador {
        //estos son los tipos de token
        final int NINGUNO = 0;
        final int DELIMITADOR = 1;
        final int VARIABLE = 2;
        final int NUMERO = 3;
        //estos son los tipos de errores de sintaxis
        final int SYNTAXIS = 0;
        final int PARENTESIS = 1;
        final int SINEXP = 2;
        final int DIVENTRECERO = 3;
        //Este token indican fin de la expresion
        final String FINEXP = "\0";

        private String exp; // hace referencia a la cadena de expresion
        private int expIndice; // indica el indice actual de la expresion
        private String token; // contiene token actual
        private int tipoToken; // contien tipo de token

        // metodo de punto de entrada del analizador
        public double evaluar(String cadenaExp) throws Excepciones{
            double resultado;
            exp = cadenaExp;
            expIndice = 0;
            obtieneToken();
            if(token.equals(FINEXP)){
                obtieneError(SINEXP); // no hay expresion presente
            } 
            // analiza y evalua la expresion
            resultado = evaluarExp2();
            if(!token.equals(FINEXP)){ // el ultimo token debe ser FINEXP
                obtieneError(SYNTAXIS);
            }
            return resultado;
        }

        // metodo para suma o resta
        private double evaluarExp2() throws Excepciones{
            char op;
            double resultado;
            double resultadoParcial;
            resultado = evaluarExp3();
            while((op = token.charAt(0)) == '+' || op == '-') {
                obtieneToken();
                resultadoParcial = evaluarExp3();
                switch(op) {
                    case '-':
                        resultado = resultado - resultadoParcial;
                    break;
                    case '+':
                        resultado = resultado + resultadoParcial;
                    break;
                } 
            }
            return resultado;
        }

    // metodo para multiplicacion, division o modulo
    private double evaluarExp3() throws Excepciones{
        char op;
        double resultado;
        double resultadoParcial;
        resultado = evaluarExp4();
        while((op = token.charAt(0)) == '*' || op == '/' || op == '%'){
            obtieneToken();
            resultadoParcial = evaluarExp4();
            switch(op) {
                case '*':
                    resultado = resultado * resultadoParcial;
                break;
                case '/':
                    if(resultadoParcial == 0.0){
                        obtieneError(DIVENTRECERO);
                    }
                    resultado = resultado / resultadoParcial;
                break;
                case '%':
                    if(resultadoParcial == 0.0){
                        obtieneError(DIVENTRECERO);
                    }
                    resultado = resultado % resultadoParcial;
                break;
            }
        }
        return resultado;
      }

    // metodo que evalua un exponente
    private double evaluarExp4() throws Excepciones{
        double resultado;
        double resultadoParcial;
        double ex;
        int t;
        resultado = evaluarExp5();
        if(token.equals("^")){
            obtieneToken();
            resultadoParcial = evaluarExp4();
            ex = resultado;
            if(resultadoParcial == 0.0) {
                resultado = 1.0;
            }else{
                for(t=(int)resultadoParcial-1; t > 0; t--){
                    resultado = resultado * ex;
                }
            }
        }
        return resultado;
    }

    // metodo que evalua operador unario + ó -.
    private double evaluarExp5() throws Excepciones{
        double resultado;
        String  op;
        op = "";
        if((tipoToken == DELIMITADOR) && token.equals("+") || token.equals("-")){
            op = token;
            obtieneToken();
        }
        resultado = evaluarExp6();
        if(op.equals("-")){
            resultado = -resultado;
        }
        return resultado;
    }

    // metodo que procesa los parentesis
    private double evaluarExp6() throws Excepciones{
        double resultado;
        if(token.equals("(")) {
            obtieneToken();
            resultado = evaluarExp2();
            if(!token.equals(")")){
                obtieneError(PARENTESIS);
            }
            obtieneToken();
        }else{
            resultado = valor();
        }
        return resultado;
    }

    //Metodo que obtiene el valor de un numero
    private double valor() throws Excepciones{
        double resultado = 0.0;
        switch(tipoToken){
            case NUMERO:
                try {
                  resultado = Double.parseDouble(token);
                } catch (NumberFormatException exc) {
                  obtieneError(SYNTAXIS);
                }
                obtieneToken();
                break;
            default:
                obtieneError(SYNTAXIS);
                break;
        }
        return resultado;
    }

    //metodo que devuelve mensaje en caso de error
    private void obtieneError(int error) throws Excepciones{
        String[] err = {
            "ERROR DE SYNTAXIS",
            "PARENTESIS NO BALANCEADOS",
            "NO EXISTE EXPRESION",
            "DIVISION POR CERO"
        };
        throw new Excepciones(err[error]);
    }

    //obtiene la siguiente token
    private void obtieneToken(){
        tipoToken = NINGUNO;
        token = "";
        //Busca el final de la expresion
        if(expIndice == exp.length()){
            token = FINEXP;
            return;
        }
        //Omite el espacio en blanco
        while(expIndice < exp.length() && Character.isWhitespace(exp.charAt(expIndice))){
            ++expIndice;
        }
        //Espacio en blanco termina la expresion
        if(expIndice == exp.length()){
            token = FINEXP;
            return; 
        }
        if(esDelimitador(exp.charAt(expIndice))){ // es operador
            token += exp.charAt(expIndice);
            expIndice++;
            tipoToken = DELIMITADOR;
        }else if(Character.isLetter(exp.charAt(expIndice))) { // es variable
            while(!esDelimitador(exp.charAt(expIndice))){
                token += exp.charAt(expIndice);
                expIndice++;
                if(expIndice >= exp.length()){
                    break;
                }
            }
        tipoToken = VARIABLE;
        }else if(Character.isDigit(exp.charAt(expIndice))){ // es numero
            while(!esDelimitador(exp.charAt(expIndice))){
                token += exp.charAt(expIndice);
                expIndice++;
                if(expIndice >= exp.length()){
                    break;
                }
            }
        tipoToken = NUMERO;
        }else{ //caracter desconocido termina la expresion
            token = FINEXP;
            return;
        } 
    }


    //Devuelve true si c es un delimitardor
    private boolean esDelimitador(char c){
        if (("+-/*^=%()".indexOf (c) != -1)){
            return true;
        }else{
            return false;
        }
      }
    }
    
    
}
