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

package org.uma.jmetal.problem.singleobjective;


//import org.uma.jmetal.problem.impl.AbstractCDNTypeProblem;
//import utils.cdn.*;
//import org.uma.jmetal.util.CdnInstancia.CdnInstancia;
//import org.uma.jmetal.solution.CDNSolution;
//import org.uma.jmetal.util.cdnItem.CdnItem;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


@SuppressWarnings("serial")
public class CDNProblem /*extends AbstractCDNTypeProblem */{
//
//  CdnInstancia instanciaDatos;
//  private ArrayList<Double> historicoFitness;
//  private int greedyDeterministic_level = 0;
//  private int formaDeInit=0;
//  private  double genMutationProbabilityAfterGreedy=0.0;
//  public CDNProblem(String nombreArchivoPropertiesInstancia)  throws IOException {
//
//
//    instanciaDatos = new CdnInstancia( nombreArchivoPropertiesInstancia) ;
//    historicoFitness = new ArrayList<>();
//
//
//    setNumberOfVariables(1); //1 varibale que tiene todas las matrices adentro
//    setNumberOfObjectives(1);
//    setName("CND");
//  }
//
//  public CdnInstancia getInstanciaDatos() {
//    return instanciaDatos;
//  }
//  private double CRC(DataCenter c, int [][] Y, int [] Y_techo) {
//	  int minutos_uso_vm_ondemand = 0;
//	  for(int t=0; t<instanciaDatos.getCant_pasosTiempo(); t++) {
//		  if (Y[c.getId()][t] > Y_techo[c.getId()]) {
//			  minutos_uso_vm_ondemand += Y[c.getId()][t] - Y_techo[c.getId()];
//		  }
//
//	  }
//	  float costo_instancias_reservadas = ((float) Y_techo[c.getId()]) * c.getReserved_vm_priceProrrated(instanciaDatos.getCant_pasosTiempo()*60);
//	  float costo_instancias_on_demand = (float) Math.ceil((float)minutos_uso_vm_ondemand/60f) * c.getOn_demand_vm_hourly_price();
//
//	  //System.out.println("Reserved: " + costo_instancias_reservadas);
//	  //System.out.println("OnDemand: " + costo_instancias_on_demand);
//	  //System.out.println("CRC for DC: " + c.getId() + ": R:" + costo_instancias_reservadas + " + D:" + costo_instancias_on_demand);
//	  //minutos_uso_vm_ondemand deberia ser siempre multiplo de 60 SALVO QUE, T no sea multiplo de 60, ej: que se compre una instancia ondemand en el minuto 66 y el T sea 70, por eso hace el CEIL
//	  return costo_instancias_reservadas + costo_instancias_on_demand;
//
//  }
//
//  private double DSC(DataCenter c, int [][] X) {
//
//    int centrodato =  c.getId();
//    double cantBytesAlmacenados =0;
//    for (int contenidoK = 0; contenidoK < instanciaDatos.getCant_contenidos();contenidoK++){
//
//      cantBytesAlmacenados+= (double) X[contenidoK][centrodato]*instanciaDatos.getKS();
//
//    }
//
//
//    return cantBytesAlmacenados*c.getStorage_base_price();
//
//  }
//
//  private double DTC(DataCenter c,  HashMap < Integer,HashMap<Integer,HashMap<Integer,Integer>>> [] Z_hash) {
//
//
//    int centroDato = c.getId();
//    double cantBytesTotalTransfiriendo =0.0;
//      for(int timeStep : Z_hash[centroDato].keySet()) {
//        for(int region: Z_hash[centroDato].get(timeStep).keySet() ){
//          for(int contenido: Z_hash[centroDato].get(timeStep).get(region).keySet() ){
//            //ver que no se usa el metodo getValorZ porque se que recorriendo
//            //de esta manera es mas eficiente y cuando llego a este punto se que existen
//            //todas las keys por las que estoy preguntando
//            cantBytesTotalTransfiriendo+= ((double) Z_hash[centroDato].get(timeStep).get(region).get(contenido))*(instanciaDatos.getTamanioBloqueVideo());
//          }
//
//        }
//
//      }
//    return cantBytesTotalTransfiriendo*c.getTransfer_base_price();
//
//  }
//
//  /** Evaluate() method */
//  public void evaluate(CDNSolution solution){
//	    double fitness=0.0   ;
//
//	    CdnItem variableCDN = solution.getVariableValue(0);
//	    int [][] X=variableCDN.getX();
//	    int [][] Y=variableCDN.getY();
//	    int [] Y_techo=variableCDN.getY_techo();
//
//
//	    double cc =0; //despues sacar estas varriables, estan para debuggear nomas
//	    double ds =0;
//	    double dt =0;
////// TODO: 28/07/16  sacar el llamado doble de las funciones CRC DSC y DTC que se metio para debug
//      for(int centroDato =0;centroDato<instanciaDatos.getCant_centrosDatos();centroDato++) {
//	      DataCenter c = instanciaDatos.getC().get(centroDato);
//	      //cc+=CRC( c, Y,  Y_techo);
//	      //ds+=DSC(c, X);
//	      //dt+=DTC(c, Z);
//	      fitness += CRC( c, Y,  Y_techo) +  DSC(c, X) /*+ DTC(c, variableCDN.getZ_hash())*/;
//	    }
//
//	    solution.setObjective(0, fitness);
//
//
//
//      this.addFitnessHistorico(fitness);
//
//     // System.out.println("fitness = "+fitness);
//	           /*
//	   CdnFactibilizador fact = new CdnFactibilizador();
//	    boolean b = fact.isFactible(solution);
//	    if (b) {
//	    	System.out.println("IS factible with Cost: " + fitness);
//	    	System.out.println("CRC (VM): [" + cc + "] DSC (Storage): [" + ds + "] DTC (Transfer): [" + dt + "]");
//	    } else {
//	    	System.out.println("NO ES FACTIBLE!!");
//	    }
//
//
//	    int [] dd= new int[3];
//	    System.out.println(dd[333]);
//	    */
//
//	  }
//
//
//    public double getGenMutationProbabilityAfterGreedy() {
//        return genMutationProbabilityAfterGreedy;
//    }
//
//    public void setGenMutationProbabilityAfterGreedy(double genMutationProbabilityAfterGreedy) {
//        this.genMutationProbabilityAfterGreedy = genMutationProbabilityAfterGreedy;
//    }
//
//
//    public int getFormaInit(){
//        return this.formaDeInit;
//    }
//
//    public  void setFormaInit(int formaDeInit){
//        this.formaDeInit = formaDeInit;
//    }
//
//    public int getGreedyDeterministic_level(){
//        return this.greedyDeterministic_level;
//    }
//
//    public  void setGreedyDeterministic_level(int deterministic_level){
//        this.greedyDeterministic_level = deterministic_level;
//    }
//
//  public ArrayList<Double> getHistoricoFitness() {
//    return historicoFitness;
//  }
//
//  public void  addFitnessHistorico(double fitness) {
//     historicoFitness.add(fitness);
//  }

}
