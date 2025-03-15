//  MOTSP.java
//
//  Author:
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package problema;


import problema.*;
import soluciones.*;
import utils.*;
import utils.cdn.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


@SuppressWarnings("serial")
public class MultiobjCDN extends AbstractCDNTypeProblem {


  HashMap<Integer,DataCenter> C ;
  HashMap<Integer,VideoContent> K ;
  HashMap<Integer,GlobalRegion> GR ;
  HashMap<Integer,UserRegion> UR ;
  HashMap<Integer,ArrayList<QoS>> Q ;
  HashMap<Integer,ArrayList<ContentRequest>> R ;
  CdnInstancia instanciaDatos;



  private ArrayList<String> historicoFitness;
  private int greedyDeterministic_level = 0;
  private int formaDeInit=0;
  private  double genMutationProbabilityAfterGreedy=0.0;
  int cant_contenidos    ;
  int cant_centrosDatos  ;
  int cant_pasosTiempo   ;
  int cant_regionesUsuarios ;
  int cant_requests;
  double KS ;

  public MultiobjCDN(String nombreArchivoPropertiesInstancia)  throws IOException {

    //el Problem ya es un singleton. en un futuro sacar al singleton.
    instanciaDatos = new CdnInstancia( nombreArchivoPropertiesInstancia) ;



    cant_requests = instanciaDatos.getCant_requests();
    C = instanciaDatos.getC();
    K = instanciaDatos.getK();
    GR = instanciaDatos.getGR();
    UR = instanciaDatos.getUR();
    Q = instanciaDatos.getQ();
    R = instanciaDatos.getR();

    cant_contenidos  = instanciaDatos.getCant_contenidos()   ;
    cant_centrosDatos = instanciaDatos.getCant_centrosDatos();
    cant_pasosTiempo = instanciaDatos.getCant_pasosTiempo();
    cant_regionesUsuarios = instanciaDatos.getCant_regionesUsuarios();
    KS= instanciaDatos.getKS();

    historicoFitness = new ArrayList<>();;

    setNumberOfVariables(1);
    setNumberOfObjectives(2);
    setName("MultiobjectiveCDN");
  }

  /** Evaluate() method */
  public void evaluate(CDNSolution solution){
    double fitness   ;
    double coefQoS   ;
    CdnItem variableCDN = solution.getVariableValue(0);
    //este seria el que construye el Z
   // instanciaDatos.construirZ_AE(variableCDN);
    //CdnItem solucionEncontrada = instanciaDatos.construirZ_backtracking_AE(variableCDN);
    //if(solucionEncontrada==null){
       instanciaDatos.construirZ_AE(variableCDN);
    CdnItem solucionEncontrada=variableCDN;
   // }else{
   //   solution.setVariableValue(0,solucionEncontrada);
   // }

    fitness = (CRC( solucionEncontrada) +  DSC(solucionEncontrada) + DTC(solucionEncontrada)) ;
    coefQoS = fqos(solucionEncontrada);
    solution.setObjective(1, fitness);
    //this.addFitnessHistorico(String.valueOf(fitness)+" "+String.valueOf(coefQoS));
    solution.setObjective(0, coefQoS);
  }


  private double CRC(CdnItem solucionItem) {
    int [][] Y=solucionItem.getY();
    int [] Y_techo=solucionItem.getY_techo();
    double [] costosVms = instanciaDatos.costosMaquinasVirtuales(Y,Y_techo);
    solucionItem.setCosto_instancias_on_demand(costosVms[1]);
    solucionItem.setCosto_instancias_reservadas(costosVms[0]);
    return costosVms[0]+costosVms[1];
  }

  private double DSC(CdnItem solucionItem) {
    int [][] X=solucionItem.getX();
    double precioCategoriasAlmacenadas =0;
    for(int centroDato =0;centroDato<cant_centrosDatos;centroDato++) {
      DataCenter c = C.get(centroDato);
      int centrodato =  c.getId();
      for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
        precioCategoriasAlmacenadas += (double) X[contenidoK][centrodato] * instanciaDatos.precioAlmacenarCategoriaVideos(centrodato);
      }
    }
    solucionItem.setCostoBytesAlmacenados(precioCategoriasAlmacenadas);
    return precioCategoriasAlmacenadas;
  }

  private double DTC(CdnItem solucionItem) {
    return solucionItem.getCostoBytesTransfiriendo();
  }

  private double DTC(DataCenter c,  HashMap < Integer,HashMap<Integer,HashMap<Integer,Integer>>> [] Z_hash) {


    int centroDato = c.getId();
    double cantBytesTotalTransfiriendo =0.0;
    for(int timeStep : Z_hash[centroDato].keySet()) {
      for(int region: Z_hash[centroDato].get(timeStep).keySet() ){
        for(int contenido: Z_hash[centroDato].get(timeStep).get(region).keySet() ){
          //ver que no se usa el metodo getValorZ porque se que recorriendo
          //de esta manera es mas eficiente y cuando llego a este punto se que existen
          //todas las keys por las que estoy preguntando
          cantBytesTotalTransfiriendo+= ((double) Z_hash[centroDato].get(timeStep).get(region).get(contenido))*(instanciaDatos.getTamanioBloqueVideo());
        }

      }

    }
    return cantBytesTotalTransfiriendo*c.getTransfer_base_price();

  }

  private double fqos(CdnItem solucionItem) {
    return solucionItem.getMetricaQoS();
  }
  private double fqos(DataCenter c,  HashMap < Integer,HashMap<Integer,HashMap<Integer,Integer>>> [] Z_hash) {


    int centroDato = c.getId();
    double qosAcumulada =0.0;
    for(int timeStep : Z_hash[centroDato].keySet()) {
      for(int region: Z_hash[centroDato].get(timeStep).keySet() ){
        for(int contenido: Z_hash[centroDato].get(timeStep).get(region).keySet() ){
          qosAcumulada+= ((double) Z_hash[centroDato].get(timeStep).get(region).get(contenido))*(instanciaDatos.getQOS(region,centroDato));
        }

      }

    }
    return qosAcumulada;

  }

  public int getCant_requests() {
    return cant_requests;
  }

  public void setCant_requests(int cant_requests) {
    this.cant_requests = cant_requests;
  }

  @Override
  public CdnInstancia getInstanciaDatos() {
    return instanciaDatos;
  }




  public int getCant_contenidos() {
    return cant_contenidos;
  }

  public void setCant_contenidos(int cant_contenidos) {
    this.cant_contenidos = cant_contenidos;
  }

  public int getCant_centrosDatos() {
    return cant_centrosDatos;
  }

  public void setCant_centrosDatos(int cant_centrosDatos) {
    this.cant_centrosDatos = cant_centrosDatos;
  }

  public int getCant_pasosTiempo() {
    return cant_pasosTiempo;
  }

  public void setCant_pasosTiempo(int cant_pasosTiempo) {
    this.cant_pasosTiempo = cant_pasosTiempo;
  }

  public int getCant_regionesUsuarios() {
    return cant_regionesUsuarios;
  }

  public void setCant_regionesUsuarios(int cant_regionesUsuarios) {
    this.cant_regionesUsuarios = cant_regionesUsuarios;
  }


  public double getGenMutationProbabilityAfterGreedy() {
    return genMutationProbabilityAfterGreedy;
  }

  public void setGenMutationProbabilityAfterGreedy(double genMutationProbabilityAfterGreedy) {
    this.genMutationProbabilityAfterGreedy = genMutationProbabilityAfterGreedy;
  }


  public int getFormaInit(){
    return this.formaDeInit;
  }

  public  void setFormaInit(int formaDeInit){
    this.formaDeInit = formaDeInit;
  }

  public int getGreedyDeterministic_level(){
    return this.greedyDeterministic_level;
  }

  public  void setGreedyDeterministic_level(int deterministic_level){
    this.greedyDeterministic_level = deterministic_level;
  }

  public ArrayList<String> getHistoricoFitness() {
    return historicoFitness;
  }

  public void  addFitnessHistorico(String fitness) {
    historicoFitness.add(fitness);
  }

}
