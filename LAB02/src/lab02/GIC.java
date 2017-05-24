/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab02;

import java.util.ArrayList;
import java.util.LinkedHashSet;


/**
 *
 * @author mateodaza
 */

public class GIC {

    public ArrayList<String> terminales;
    public ArrayList<String> noTerminales;
    public int[] estadoRecNoTerminal;
    public String sInicio;
    public ArrayList<String> producciones;
    public ArrayList<String> newProd;
    public ArrayList<String> newProdFact;
    public ArrayList<ArrayList<String>> prim;
    public ArrayList<ArrayList<String>>  sgtes;
    public String[][] mTab;
    public String[] mTabColumn;


    public GIC(ArrayList<String> P){
        terminales = getTerminales(P);
        noTerminales = getNoTerminales(P);
        sInicio = getSInicio(P);
        producciones = P;
        newProd = new ArrayList<String> ();
        newProdFact = new ArrayList<String> ();
        prim = new ArrayList<ArrayList<String>>();
        sgtes = new ArrayList<ArrayList<String>>();
        mTab = null;
        mTabColumn = new String[terminales.size()+2];
    }
    // Getters
    private ArrayList<String> getTerminales(ArrayList<String> P) {
        ArrayList<String> out = new ArrayList<String>();
        
        for (int i = 0; i < P.size(); i++) {
            String temp = P.get(i);
                    for (int k = 0; k < temp.length(); k++) {
                        //String c =  String.valueOf(temp.charAt(k));
                        char c = temp.charAt(k);
                        char cdesp = ' ';
                        char cante = ' ';
                        if(k<temp.length()-2){  //Char siguiente
                           cdesp = temp.charAt(k+1);
                        }
                        if(k>0){//Char anterior
                           cante = temp.charAt(k-1);
                        }
                        if(!Character.isUpperCase(c) && isInArray(out,String.valueOf(c))==false && c!='\'' && c!='&'){
                            if(c=='-'){ //Produccion -
                                if(cdesp!='>'){
                                  out.add(String.valueOf(c)); 
                                }
                            }else{
                                if(c=='>'){  //Produccion >
                                    if(cante!='-'){
                                      out.add(String.valueOf(c));    
                                    }
                                }else{
                                      out.add(String.valueOf(c)); 
                                }
                            }
                        }
                    }
        }
        
        return out;
    }
    public ArrayList<String> getNoTerminales(ArrayList<String> P) {
        ArrayList<String> out = new ArrayList<String>();
                
        for (int i = 0; i < P.size(); i++) {
            String temp = P.get(i);
                    //String c =  String.valueOf(temp.charAt(k));
                    char c = temp.charAt(0);
                    String NT = String.valueOf(c);
                    char cc = temp.charAt(1);
                    if(cc=='\''){
                       NT = NT+"'";
                    }
                    if(isInArray(out,NT)==false){
                        out.add(String.valueOf(NT)); 
                    }
        }
        
        return out;
    }
    private String getSInicio(ArrayList<String> P) {
        String out = String.valueOf(P.get(0).toString().charAt(0));      
        return out;
    }
    public void clean(){
        terminales = new ArrayList<String>();
        noTerminales = new ArrayList<String>();
        sInicio = null;
        producciones = new ArrayList<String>();
        newProd = new ArrayList<String>();
        newProdFact = new ArrayList<String>();
        prim = new ArrayList<ArrayList<String>>();
        sgtes = new ArrayList<ArrayList<String>>();
        mTab = null;
        mTabColumn = null;
    }
    public void orderGram(){
        ArrayList<String> newGram = new ArrayList<String>();
        for (int i = 0; i < noTerminales.size(); i++) {
            String temp = noTerminales.get(i);
            
            if(temp.length()==1){
                for (int j = 0; j < producciones.size(); j++) {
                    String tempp = producciones.get(j);
                    if(String.valueOf(tempp.charAt(0)).equals(String.valueOf(temp.charAt(0))) && !String.valueOf(tempp.charAt(1)).equals("'") && isInArray(newGram,tempp)==false){
                        newGram.add(tempp);
                    }
                }
                for (int j = 0; j < producciones.size(); j++) {
                    String temp2 = producciones.get(j);
                    if(String.valueOf(temp2.charAt(0)).equals(String.valueOf(temp.charAt(0)))){
                        if(isInArray(newGram,temp2)==false){
                            newGram.add(temp2);
                        }
                    }
                }
            }
        }
        producciones = newGram;
    }
    //Rec Izq & Fact
    private boolean isInArray(ArrayList<String> array, String str){
        boolean out = false;
        for (int i = 0; i < array.size(); i++) {
            if(array.get(i).toString().equals(str)){
                out = true;
            }
        }
        return out;
    }
    public void eliminarRecIzq(){
        estadoRecNoTerminal = new int[noTerminales.size()];
        //Estado de recursividad de cada no terminal falso
        for (int i = 0; i < estadoRecNoTerminal.length; i++) {
            estadoRecNoTerminal[i] = 0;
        }
        //Recorre todas las producciones en busca de recursividad y actualiza
        for (int i = 0; i < producciones.size(); i++) {
            String temp = producciones.get(i); 
            if(isRecIzq(temp)==true){ //La produccion tiene recursividad a Izq
                String beta = ( (String.valueOf(temp.charAt(3))) +"'"+"->"+(temp.substring(4, temp.length())) + (String.valueOf(temp.charAt(3))) +"'");
                producciones.set(i, beta);
                setStateRecNoTerm(String.valueOf(temp.charAt(0)));  //Cambia estado array rec izq no term
            }        
        } 
        for (int i = 0; i < estadoRecNoTerminal.length; i++) {
            int cont = 0;
           if(estadoRecNoTerminal[i]==1){
               String noTermConRec = noTerminales.get(i);  //No Terminal con recursividad para agregar noTerminal ->betanoTerm'
               for (int j = 0; j < producciones.size(); j++) {
                   String temp2 = producciones.get(j);
                   String noTerm = String.valueOf(temp2.charAt(0));
                   String sigNoTerm = String.valueOf(temp2.charAt(1));
                   if(noTerm.equals(noTermConRec) && !sigNoTerm.equals("'")){  //Encuentra no term con rec en las producciones
                       String beta2 = noTerm + "->" + temp2.substring(3, temp2.length()) + noTerm +"'";
                       newProd.add(beta2);
                       cont++;
                   }
               }
               if(cont==0){
                   String betanull = String.valueOf(noTerminales.get(i).charAt(0)) + "->" + String.valueOf(noTerminales.get(i).charAt(0))+"'";
                   newProd.add(betanull);
               }
               String betaEpsilon = String.valueOf(noTerminales.get(i).charAt(0)) + "'->&";
               newProd.add(betaEpsilon);
           }
        }
        deleteRecStates();
        producciones.addAll(newProd);
    }
    public boolean isRecIzq(String prod){
        boolean out = false;
            String noT = String.valueOf(prod.charAt(0));
            String prim = String.valueOf(prod.charAt(3));
            if(noT.equals(prim)){
                out = true;
            }
        return out;
    }
    public void factorizar(){
        for (int i = 0; i < noTerminales.size(); i++) {
            String NT = noTerminales.get(i);
            ArrayList<String> prods = getProductionsforFact(NT); //Obtiene producciones del noTerminal
            if(prods!=null && prods.size()>1){  //No se aplicó recursividad previamente
                String[] prodsArr = new String[prods.size()];
                for (int j = 0; j < prods.size(); j++) {
                   prodsArr[j]=prods.get(j);
                }
                ArrayList<String> onlyFirstChar = getFirstLetterArr(prods);
                for (int k = 0; k < onlyFirstChar.size(); k++) {
                    String[] prodsArrOnlyChar = leaveAppearence(prodsArr,onlyFirstChar.get(k));
                    if(prodsArrOnlyChar.length>1){
                        String subcadena = longestCommonPrefix(prodsArrOnlyChar);
                        if(!subcadena.equals(null)){  //Encontró parecido
                            changePosFact(subcadena);
                            String beta1 = NT+"->"+subcadena+NT+"'";
                            producciones.add(beta1);
                        } 
                    }

                }
       
               
            }
            
        }
        //producciones.addAll(newProdFact);
        //System.out.println(producciones);
        
    }
    private void changePosFact(String sub){
        for(int i=0; i<producciones.size();i++){
            String temp = producciones.get(i);
            String NT = String.valueOf(temp.charAt(0));
            if(temp.length()>=3+sub.length()){
               String cad = temp.substring(3,3+sub.length());
                if(cad.equals(sub)){ //Encontró la cadena
                    String beta = null;
                    if(temp.length()==sub.length()+3){  //Epsilon
                       beta = NT+"'->&"; 
                      producciones.set(i,beta);
                    }else{
                       String rest = temp.substring(sub.length()+3,temp.length());
                       beta = NT+"'->"+rest;
                       producciones.set(i,beta);
                    }
                    //newProdFact.add(beta);
                } 
            }
        }
    }
    private String longestCommonPrefix(String[] strs) {
    if(strs.length==0) return "";
    String minStr=strs[0];

    for(int i=1;i<strs.length;i++){
        if(strs[i].length()<minStr.length())
            minStr=strs[i];
    }
    int end=minStr.length();
    for(int i=0;i<strs.length;i++){
        int j;
        for( j=0;j<end;j++){
            if(minStr.charAt(j)!=strs[i].charAt(j))
                break;
        }
        if(j<end)
            end=j;
    }
    return minStr.substring(0,end);
}    
    private ArrayList<String> getProductionsforFact(String noTerm){
        ArrayList<String> out = new ArrayList<String>();
        boolean recIzq = false;
            for (int j = 0; j < producciones.size(); j++) {
                String temp = producciones.get(j);
                String firstTemp = String.valueOf(temp.charAt(0));
                String secTemp = String.valueOf(temp.charAt(1));
                if(firstTemp.equals(noTerm)){
                    out.add(temp.substring(3,temp.length()));
                    if(secTemp.equals("\'")){
                        recIzq = true;
                    }
                }
            }
        if(recIzq==true){
            out = null;
        }
        return out;
    }
    private String[] leaveAppearence(String [] temp, String a){
        ArrayList<String> out =  new ArrayList<String>();
        int cont = 0;
        for (int i = 0; i < temp.length; i++) {
            if(a.equals(String.valueOf(temp[i].charAt(0)))){
                out.add(temp[i]);
                cont++;
            }
        }
        String[] outt = new String[out.size()];
        for (int i = 0; i < out.size(); i++) {
            outt[i]=out.get(i);
        }
        return outt;
    }
    private void setStateRecNoTerm(String noterm){ 
        for (int i = 0; i < noTerminales.size(); i++) {
            if(noTerminales.get(i).equals(noterm)){
                estadoRecNoTerminal[i]=1;
            }
        }
    }
    private void deleteRecStates(){
        for (int i = 0; i < estadoRecNoTerminal.length; i++) {
           if(estadoRecNoTerminal[i]==1){
               String noTermConRec = noTerminales.get(i);    
               int cont = 0;
               while(cont<producciones.size()){
                  String temp2 = producciones.get(cont);
                       String noTerm = String.valueOf(temp2.charAt(0));
                       String sigNoTerm = String.valueOf(temp2.charAt(1));
                       if(noTerm.equals(noTermConRec) && !sigNoTerm.equals("'")){  //Encuentra no term con rec en las producciones
                           producciones.remove(cont);                         
                        }else{
                           cont++;
                       }
               }
            }
        }
    }
    private ArrayList<String> getFirstLetterArr(ArrayList<String> temp){
        ArrayList<String> out = new ArrayList<String>();
        
        for(int i=0; i<temp.size(); i++){
            String c = String.valueOf(temp.get(i).charAt(0));
            out.add(c);
        }
        
        return noDuplicates(out);
    }
    private ArrayList<String> noDuplicates(ArrayList<String> list){
        return list = new ArrayList<String>(new LinkedHashSet<String>(list));
    }
    
    //PRIMERO 
    private ArrayList<String> getProductions(String noTerm, boolean sw){
        ArrayList<String> out = new ArrayList<String>();
            for (int j = 0; j < producciones.size(); j++) {
                String temp = producciones.get(j);
                String firstTemp = String.valueOf(temp.charAt(0));
                if(firstTemp.equals(noTerm) && sw ==false && checkApostrophe(temp)==false){
                    out.add(temp.substring(3,temp.length()));
                }else{
                    if (firstTemp.equals(noTerm) && sw ==true && checkApostrophe(temp)==true) {
                       out.add(temp.substring(4,temp.length()));         
                    }
                }
            }
        return out;
    }
    private boolean checkApostrophe(String word){
        boolean out = false;
        if(word.length()>1){
           if(String.valueOf(word.charAt(1)).equals("'")){
             out = true;
           }
        }
        
        return out;
    }
    private ArrayList<String> getPrim(String p){
        ArrayList<String> out = new ArrayList<String>();
        String prod = p;
        
        if(!Character.isUpperCase(prod.charAt(0))){  //Es Terminal
            out.add(String.valueOf(prod.charAt(0)));
        }else{ // Es no terminal
                ArrayList<String> temp = new ArrayList<String>();
                ArrayList<String> primeros = new ArrayList<String>();
                
            if(checkApostrophe(prod)==true){ // ES de la forma E'
                temp = getProductions(String.valueOf(prod.charAt(0)), true);
                for (int i = 0; i < temp.size(); i++) {
                    primeros.addAll(getPrim(temp.get(i)));
                }
            }else{
                temp = getProductions(String.valueOf(prod.charAt(0)), false);
                for (int i = 0; i < temp.size(); i++) {
                    primeros.addAll(getPrim(temp.get(i)));
                }
            }
            out.addAll(primeros);
        }
        return out;
    }
    public void prim(){
         //FIll prim
         for (int j = 0; j < noTerminales.size(); j++) {
             ArrayList<String> temp = new ArrayList<String>();
             temp.add(noTerminales.get(j));
             prim.add(temp);
         }
        for (int i = 0; i < producciones.size(); i++) {
            String prod = producciones.get(i);
            ArrayList<String> primer = new ArrayList<String>();
            if(checkApostrophe(prod)==true){ // ES de la forma E'
                primer.addAll(getPrim(prod.substring(4,prod.length())));
            }else{
                primer.addAll(getPrim(prod.substring(3,prod.length())));
            }
            //System.out.println("Prim de "+prod.substring(0,2)+" "+primer);
            //prim.addAll(primer);
           
            String nt = prod.substring(0,2);
            String nn = prod.substring(1,2);
            if(nn.equals("-")){
                nt = prod.substring(0,1);
            }
            for (int j = 0; j < noTerminales.size(); j++) {
                if(nt.equals(noTerminales.get(j))){
                        ArrayList<String> temp = prim.get(j);
                        temp.addAll(primer);
                        prim.set(j, temp);
                }
            }            
        }    
    }
    
    //SIGUIENTE
    public void sgte(){
        //Fill sgte
         for (int j = 0; j < noTerminales.size(); j++) {
             ArrayList<String> temp = new ArrayList<String>();
             temp.add(noTerminales.get(j));
             if(noTerminales.get(j).equals(sInicio)){
                temp.add("$"); 
             }
             sgtes.add(temp);
         }
         ArrayList<String> sgte2 = new ArrayList<String>();
        for (int i = 0; i < producciones.size(); i++) { //Revisar todas las producciones
            String prod = producciones.get(i);  // Obtiene produccion
            ArrayList<String> sgte = new ArrayList<String>();
            //Obtener A
            int inicioProd = 3;   
            String A = String.valueOf(prod.charAt(0));
            if(String.valueOf(prod.charAt(1)).equals("'")){  //Revisa que sea de la forma E'
                inicioProd = 4;
                A = A + "'";
            }
            //Obtener No terminales de la producción
            ArrayList<String> NT = noTerminales(prod, inicioProd);
            //Cantidad de NoTerminales en la produccion
            int cantNT = NT.size();
            if(cantNT==0){  // No tiene noTerminales, continua a la produccion siguiente

            }else{

                for (int j = 0; j < cantNT; j++) {  //Rotar y aplicar reglas para cada NT
                    //Declaraciones iniciales pre-rotacion
                    String alfa = "&";
                    String B = "&";
                    String beta = prod.substring(inicioProd,prod.length());
               
                    //Incio Rotacion y reglas
                    sgte.addAll(rotate(alfa,B,beta,NT.get(j),beta,A));
                }
                System.out.println(sgte);
                sgte2.addAll(sgte);
                
                //Agregar a ArrayList sgtes correspondiente
                for (int j = 0; j < sgte.size(); j++) {
                    for (int k = 0; k < sgtes.size(); k++) {
                        if(String.valueOf(sgte.get(j).charAt(0)).equals(sgtes.get(k).get(0))){ //Encontró sgte en sgtes
                            
                                //if(sgtes.get(k).get(1).equals(String.valueOf(sgte.get(j).charAt(1)))){
                                  //  System.out.println("WHAT");
                                    //if(isInArray(sgtes.get(k),String.valueOf(sgte.get(j).charAt(3)))==false){
                                      //  sgtes.get(k).add(String.valueOf(sgte.get(j).charAt(3)));
                                   // }
                                //}else{
                                    if(isInArray(sgtes.get(k),String.valueOf(sgte.get(j).charAt(2)))==false){
                                        sgtes.get(k).add(String.valueOf(sgte.get(j).charAt(2)));
                                    }
                                //}
                        }         
                    }
                }
            }
        }
        //Primera pasada
        for (int j = 0; j < sgte2.size(); j++) {
            String temp = sgte2.get(j);
            if(String.valueOf(temp.charAt(0)).equals("º")){
                String nt1 = String.valueOf(temp.charAt(1));
                String nt2 = "";
                if(String.valueOf(sgte2.get(j).charAt(2)).equals("'")){
                   nt1 = nt1 + "'"; 
                }
                if(temp.length()==4){
                    if(String.valueOf(sgte2.get(j).charAt(2)).equals("'")){
                        nt2 = String.valueOf(temp.charAt(3)); 
                    }else{
                        nt2= String.valueOf(temp.charAt(2)) + "'";
                    }
                }else{
                    if(temp.length()==5){
                        nt2= String.valueOf(temp.charAt(3)) + "'";  
                    }else{
                        nt2= String.valueOf(temp.charAt(2));
                    }
                }
                int posA = getPos(noTerminales, nt1);
                int posB = getPos(noTerminales, nt2);
                setAB(posA, posB);
            }
        }
        //Segunda pasada
         for (int j = 0; j < sgte2.size(); j++) {
            String temp = sgte2.get(j);
            if(String.valueOf(temp.charAt(0)).equals("º")){
                String nt1 = String.valueOf(temp.charAt(1));
                String nt2 = "";
                if(String.valueOf(sgte2.get(j).charAt(2)).equals("'")){
                   nt1 = nt1 + "'"; 
                }
                if(temp.length()==4){
                    if(String.valueOf(sgte2.get(j).charAt(2)).equals("'")){
                        nt2 = String.valueOf(temp.charAt(3)); 
                    }else{
                        nt2= String.valueOf(temp.charAt(2)) + "'";
                    }
                }else{
                    if(temp.length()==5){
                        nt2= String.valueOf(temp.charAt(3)) + "'";  
                    }else{
                        nt2= String.valueOf(temp.charAt(2));
                    }
                }
                int posA = getPos(noTerminales, nt1);
                int posB = getPos(noTerminales, nt2);
                setAB(posA, posB);
            }
        }
        
    }
    public ArrayList<String> rotate(String alfa, String B, String beta, String NT, String prod, String A){
        ArrayList<String> out = new ArrayList<String>();
            String alf = prodIzqDer(NT,prod,"Izq");
            String b = NT;
            String bet = prodIzqDer(NT,prod,"Der");
            //System.out.println("Al lado Izq de "+ NT + " en la prod "+prod+" hay: "+ prodIzq);
            //System.out.println("Al lado Der de "+ NT + " en la prod "+prod+" hay: "+ prodDer);
            
            if(bet.isEmpty()){ //REGLA 3.1
                out.add("º"+b+A);  //En ste B se mete ste A
            }else{  //REGLA 2
                //Revisar si es NT o T
                if(!Character.isUpperCase(bet.charAt(0)) && !String.valueOf(bet.charAt(0)).equals("'")){  //Es Terminal
                    out.add(b+" "+String.valueOf(bet.charAt(0)));

                }else{ //Es No terminal
                    //Revisar PRIMERO del NT
                    String nt = String.valueOf(bet.charAt(0));
                    if(bet.length()>1){
                        if(String.valueOf(bet.charAt(1)).equals("'")){
                            nt = nt+"'";
                        }
                    }
                    for (int i = 0; i < noTerminales.size(); i++) {
                        //System.out.println("Compara "+ nt + " con "+ prim.get(i).get(0));
                        if(nt.equals(prim.get(i).get(0))){
                            for (int j = 1; j < prim.get(i).size(); j++) {
                                //System.out.println("le mete: "+prim.get(i).get(j));
                                if(prim.get(i).get(j).equals("&")){  //REGLA 3.2
                                    out.add("º"+b+A);  //En ste B se mete ste A
                                }else{
                                    out.add(b+" "+prim.get(i).get(j));
                                }
                            }
                        }
                    }
                    
                }
            }

        return out;
    }
    private ArrayList<String> noTerminales(String prod, int start){
        ArrayList<String> out = new ArrayList<String>();
        for (int i = start; i < prod.length(); i++) {
            if( Character.isUpperCase(prod.charAt(i)) ){
                String w = String.valueOf(prod.charAt(i));
                if(i<prod.length()-1){
                    if(String.valueOf(prod.charAt(i+1)).equals("'")){
                        out.add(w+"'");
                    }else{
                        out.add(w);
                    }
                }else{
                    out.add(w);
                }
            }
        }
        return out;
    }
    private String prodIzqDer(String NT, String prod, String tipo){
        String out = null;
        String N = String.valueOf(NT.charAt(0));
        int posant = 0;
        int posdes = 0;
        for (int i = 0; i < prod.length(); i++) {
            if(String.valueOf(prod.charAt(i)).equals(String.valueOf(NT.charAt(0)))){
                posant = i-1;
                if(NT.length()==2){
                    posdes = i+2;
                }else{
                    posdes = i+1;
                }
            }
        }
        if(tipo.equals("Izq")){
            out = prod.substring(0,posant+1);
        }else{
            if(tipo.equals("Der")){
              out = prod.substring(posdes,prod.length());
            }
        }
        
        return out;
    }
    private void setAB(int posA, int posB){
        for (int i = 0; i < sgtes.get(posB).size(); i++) {
            String temp = sgtes.get(posB).get(i);
            if(temp.length()!=2 && !Character.isUpperCase(temp.charAt(0))){
                if(isInArray(sgtes.get(posA),temp)==false){
                    sgtes.get(posA).add(sgtes.get(posB).get(i));
                }
            }
            
        }
    }
    private int getPos(ArrayList<String> arr, String A){
        int out = -1;
        for (int i = 0; i < arr.size(); i++) {
            String t = String.valueOf(A.charAt(0));
            String tt = String.valueOf(arr.get(i).charAt(0));
            if(A.length()==2 && arr.get(i).length()==2){
                if(t.equals(tt)){
                    out=i;
                }
            }else{
                if(arr.get(i).length()==1 && A.length()==1){
                    if(t.equals(tt)){
                        out=i;
                    }
                }
            }
        }
        
        return out;
    }

    //Tabla M
    public void generateMTableColumns(){
        //Llenar primer fila
        for (int i = 0; i < terminales.size()+2; i++) {
            if(i==0){
                mTabColumn[i] = "NT/T";
            }else{
                if(i==terminales.size()+1){
                    mTabColumn[i] = "$";
                }else{
                    if(i<=terminales.size()){
                       mTabColumn[i] = terminales.get(i-1);  
                    }
                }
            }
        }
    }
    public void generateMTable(){
        mTab = new String[noTerminales.size()+1][terminales.size()+2];
        mTab[0][0] = "NT/T";
        mTab[0][terminales.size()+1] = "$";
        for (int i = 1; i <= noTerminales.size(); i++) {
             mTab[i][0] = noTerminales.get(i-1);     
        }
        for (int i = 1; i <= terminales.size(); i++) {
             mTab[0][i] = terminales.get(i-1); 
        }
        // Recorrer producciones
        for (int i = 0; i < producciones.size(); i++) {
            String prod = producciones.get(i);
            String nt = "";
            String pr = "";
            if(checkApostrophe(prod)==true){ // Forma E'
                 nt = prod.substring(0,2);
                 pr = prod.substring(4,5);
                 if (checkApostrophe(prod.substring(4,5))==true) {
                    pr = pr +"'";
                 }
            }else{
                 nt = prod.substring(0,1);
                 pr = prod.substring(3,4);
                 if (prod.length()>4) {
                     if (checkApostrophe(prod.substring(3,4))==true) {
                        pr = pr +"'";
                    }
                 }
            }
            //Revisar primero de la prod
            ArrayList<String> todo = new ArrayList<String>();
            if(!Character.isUpperCase(pr.charAt(0))){  //Es Terminal
                //Agregar a la table
                if(String.valueOf(pr.charAt(0)).equals("&")){  //Epsilon
                    //Obtener Siguiente A
                    int posnt = -1;
                    for (int j = 0; j < noTerminales.size(); j++) {
                        if (noTerminales.get(j).equals(nt)) {
                            posnt = j;
                        }
                    }
                    todo = sgtes.get(posnt);
                    
                }else{
                    //Agregar Siguiente
                    todo.add(pr);
                }
            }else{  // No Terminal 
                //Obtener primero NT
                int posnt = -1;
                for (int j = 0; j < noTerminales.size(); j++) {
                    if (noTerminales.get(j).equals(pr)) {
                        posnt = j;
                    }
                }
                todo = prim.get(posnt);
            }
            
            //Agregar a la Matriz
            for (int l = 0; l < todo.size(); l++) {
                for (int j = 0; j < noTerminales.size()+1; j++) {
                    if (mTab[j][0].equals(nt)) {
                        for (int k = 0; k < terminales.size()+2; k++) {
                            if(mTab[0][k].equals(todo.get(l))){
                               mTab[j][k] = prod;
                            }
                        }
                    }
                 }
            }
            
           
            
            
        }
        
        
    }
    
    //Reconocimiento
    public ArrayList<String> reconocer(String cad){            
        ArrayList<String> process = new ArrayList<String>();
        
        try {
            String w = cad + "$";
            String pila = "$"+sInicio;
            String entrada = w;
            String salida = inTable(sInicio,String.valueOf(cad.charAt(0)));
            //String salida = "";
            process.add("| PILA | ENTRADA | SALIDA | ");
            int i = 0;
            outerloop:while((!entrada.equals("$") || !pila.equals("$"))) {
                if(salida.equals("error")){
                    break outerloop;
                }
                //nT
                String nt = "";
                String term =""; 
                if(pila.length()>1){
                    nt = String.valueOf(pila.charAt(pila.length()-1));
                    term = String.valueOf(entrada.charAt(0));;
                }else{
                    nt = pila;
                    term = entrada;
                }
                if(nt.equals("&")){
                    pila = pila.substring(0,pila.length()-1);
                    nt=String.valueOf(pila.charAt(pila.length()-1));
                }
                if(nt.equals("'")){
                    nt = String.valueOf(pila.charAt(pila.length()-2)) + nt;
                }

                System.out.println(term);
                if(Character.isUpperCase(nt.charAt(0))){
                    //terminal entrada
                    //produccion Tabla M
                    salida = inTable(nt,term);
                    if(salida.equals(null)){
                        salida = "error";
                    }else{
                        //actualizar pila
                        process.add(pila + "        " + entrada + "          " + salida);
                        String salidaNt = "";
                        String salidaPr = "";
                        if(checkApostrophe(salida)==true){ // Forma E'
                             salidaNt = salida.substring(0,2);
                             salidaPr = salida.substring(4,5);
                             if (checkApostrophe(salida.substring(4,5))==true) {
                                salidaPr = salidaPr +"'";
                             }
                        }else{
                             salidaNt = salida.substring(0,1);
                             salidaPr = salida.substring(3,4);
                             if (salida.length()>4) {
                                 if (checkApostrophe(salida.substring(3,4))==true) {
                                    salidaPr = salidaPr +"'";
                                }
                             }
                        }
                        if(pila.substring(pila.length()-1, pila.length()).equals("'")){
                            pila = pila.substring(0, pila.length()-2);
                        }else{
                            if(!pila.equals("$")){
                                 pila = pila.substring(0, pila.length()-1);
                            }
                        }
                         String t = "";
                        loop: for (int j = 0; j < salida.length(); j++) {
                            int posFin = salida.length()-j;
                            if(salida.substring(posFin-1, posFin).equals(">")){
                                break loop;
                            }else{
                                if(!salida.substring(posFin-1, posFin).equals("'")){
                                    if(j!=0){
                                       if(salida.substring(posFin, posFin+1).equals("'")){
                                        t=t+salida.substring(posFin-1, posFin+1); 
                                       }else{
                                           t =t+salida.substring(posFin-1, posFin);
                                       }   
                                    }else{
                                        t =t+salida.substring(posFin-1, posFin);
                                    }
                                }
                            }
                        }
                        pila = pila + t;
                        

                    }
                }
                nt = "";
                term =""; 
                if(pila.length()>1){
                    nt = String.valueOf(pila.charAt(pila.length()-1));
                    term = String.valueOf(entrada.charAt(0));;
                }else{
                    nt = pila;
                    term = entrada;
                }
                if(nt.equals("&")){
                    pila = pila.substring(0,pila.length()-1);
                    nt=String.valueOf(pila.charAt(pila.length()-1));
                }
                if(nt.equals("'")){
                    nt = String.valueOf(pila.charAt(pila.length()-2)) + nt;
                }
                //System.out.println(term);
                if(nt.equals(term)){
                   salida=""; 
                   if(!pila.equals("$") && !entrada.equals("$")){
                     process.add(pila + "        " + entrada + "          " + salida);
                     pila = pila.substring(0, pila.length()-1); 
                     entrada = entrada.substring(1,entrada.length());
                   }
                   //i++;

                }else{
                    if(!Character.isUpperCase(nt.charAt(0))){
                       salida = "error";
                    }
                }

                 //while((!entrada.equals("$") || !pila.equals("$")))
                 if (entrada.equals("$") && pila.equals("$")) {
                    process.add(pila + "        " + entrada + "          " + salida);
                 }
                /*if(entrada.equals("")||pila.equals("")){
                    break outerloop;
                }*/
            }
            if (entrada.equals("$") && pila.equals("$")) {
                process.add("Acepta");
            }
        } catch (IndexOutOfBoundsException e) {
            process.add("ERROR");
        } catch (NullPointerException e){
            process.add("ERROR");
        }
        
        return process;
    }
    
    private String inTable(String nt, String term){
         //pos noTerminal en tabla
         String out ="";
        int posNt = -1;
        for (int j = 1; j <= noTerminales.size(); j++) {
            if(mTab[j][0].equals(nt)){
                posNt = j;
            }
        }
        //get pos Terminal en tabla
        int posT = -1;
        for (int j = 1; j <= terminales.size()+1; j++) {
            if(mTab[0][j].equals(term)){
                posT = j;
            }
        }
        //produccion Tabla M
        out = mTab[posNt][posT];
        return out;
    }

}
