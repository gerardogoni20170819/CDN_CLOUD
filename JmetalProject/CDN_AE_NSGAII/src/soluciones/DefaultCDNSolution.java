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

package soluciones;


import operadores.CDNmMutation;
import problema.CDNTypeProblem;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;
import utils.CdnInstancia;
import utils.CdnFactibilizador;
import utils.CdnItem;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static java.lang.Math.abs;


@SuppressWarnings("serial")
public class DefaultCDNSolution
    extends AbstractGenericSolution<CdnItem, CDNTypeProblem>
    implements CDNSolution {


    @Override
    public boolean isFactible() {
        CdnFactibilizador fact = new CdnFactibilizador(problem.getInstanciaDatos());
        return fact.isFactible(this);
    }

    @Override
    public void toFileGeneral(String directorioSalida) {
        CdnItem solu = this.getVariableValue(0);
        solu.toFileGeneral(directorioSalida);
    }

    @Override
    public void toFileEvaluacion(String directorioSalida,String nombreArchivo) {
        CdnItem solu = this.getVariableValue(0);
        solu.toFileEvaluacion( directorioSalida, nombreArchivo);
    }

   @Override
    public void toFilePareto(String directorioSalida) {
        CdnItem solu = this.getVariableValue(0);
        solu.toFilePareto(directorioSalida);
    }

    @Override
    public int getIdMemoriaUsada() {
        return 0;
    }

    @Override
    public CdnInstancia getInstanciaDatos() {
        return problem.getInstanciaDatos();
    }

    @Override
    public void factibilizar() {
        CdnFactibilizador fact = new CdnFactibilizador(problem.getInstanciaDatos());
        fact.factibilizarSolucion(this);
    }

    @Override
    public long  getTiempoConstruirZ() {
        CdnItem solu = this.getVariableValue(0);
        return solu.getTiempoConstruirZ();
    }

    private HashMap<Integer,ArrayList<int[][]>> VMM;
    private CdnInstancia instanciaDatos= problem.getInstanciaDatos();



  /** Constructor */
  public DefaultCDNSolution(CDNTypeProblem problem) {
    super(problem) ;
    CdnInstancia instanciaDatos = problem.getInstanciaDatos();

    if(instanciaDatos.marcaInicioInicializacionGreedy=="NOSET"){
        instanciaDatos.marcaInicioInicializacionGreedy=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }
    int idMemoriaUsada= instanciaDatos.getPrimerIdLibre();
    CdnItem solucInicial;
    boolean greedyModoBusqueda=true;
    int formaDeInit = problem.getFormaInit();
//    if(formaDeInit==1) {
    	//solucInicial = initCDNSolutionWithGreedyOnCost(problem,problem.getGreedyDeterministic_level());
    	//solucInicial = instanciaDatos.greedyOnCost();
        double elrandom =JMetalRandom.getInstance().nextDouble();
       // String dguu = String.valueOf(elrandom);
       // JMetalLogger.logger.log(Level.INFO,String.valueOf(elrandom));

        if(elrandom < 0.5) {
         //   dguu+="<";

            if(instanciaDatos.getSoluicionesInicialesOncost()==null){
                instanciaDatos.init_soluicionesInicialesOncost();
            }
            int indice =  JMetalRandom.getInstance().nextInt(0, instanciaDatos.getSoluicionesInicialesOncost().size()-1);
            solucInicial = instanciaDatos.getSoluicionesInicialesOncost().get(indice);
        }else {
          //  dguu+=">";
            boolean armarZ = true;
            ArrayList<CdnItem> listaSolucsOnCost=instanciaDatos.greedyQoSParetoQoS(armarZ);
            int indice =  JMetalRandom.getInstance().nextInt(0, listaSolucsOnCost.size()-1);
            solucInicial = listaSolucsOnCost.get(indice);
        }

      //  JMetalLogger.logger.log(Level.INFO,dguu);
//    } else{
//    	//solucInicial = initCDNSolutionWithGreedyOnQoS(problem,problem.getGreedyDeterministic_level());
//    	solucInicial = instanciaDatos.greedyOnQoS();
//    }


    solucInicial.setIdMemoriaUsada(idMemoriaUsada);
    solucInicial.setCant_requests(instanciaDatos.getCant_requests());
     // solucInicial.initdcsComparadosQoSRequests();
    setVariableValue(0,solucInicial);

      //CdnFactibilizador fa = new CdnFactibilizador();
      //if(!fa.isFactible(this)){

        //  throw new JMetalException("mal la soluc");
      //}
      CDNmMutation mutador = new CDNmMutation();
      if((formaDeInit==1) || (formaDeInit==2)) {
          //aca se muta la solucion obtenida con el greedy
//// TODO: 24/08/2017 probar primero sin mutacion a ver que hace 
          mutador.doMutation(1, problem.getGenMutationProbabilityAfterGreedy(), this);
      }else{
          //se factibiliza al random
          this.factibilizar();

//          CdnFactibilizador fa = new CdnFactibilizador();
//          fa.factibilizarSolucion(this);

      }

      instanciaDatos.marcaFinInicializacionGreedy=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

      //  System.err.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+" Init solucion...");
    initializeObjectiveValues();//ese llama al de la clase padre AbstractGenericSolution
      System.gc(); //evita el pico de memoria al principio (doble del consumo de memoria que tiene mas adelante)
  }


  

  /** Copy constructor */
  public DefaultCDNSolution(DefaultCDNSolution solution) {
    super(solution.problem);
    

    for (int i = 0; i < problem.getNumberOfVariables(); i++) {
      setVariableValue(i,  solution.getVariableValue(i).clone());
    }

    for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
      setObjective(i, solution.getObjective(i)) ;
    }

    //overallConstraintViolationDegree = solution.overallConstraintViolationDegree ;
    //numberOfViolatedConstraints = solution.numberOfViolatedConstraints ;

    attributes = new HashMap<Object, Object>(solution.attributes) ;
  }





  @Override
  public DefaultCDNSolution copy() {
    return new DefaultCDNSolution(this);
  }



  @Override
  public String getVariableValueString(int index) {



      String result = "" ;


      CdnItem solucionProblema = getVariableValue(index);

      int [][] X        = solucionProblema.getX();
      int [][] Y        = solucionProblema.getY();
      int [] Y_techo    = solucionProblema.getY_techo();
     // int [][][][] Z    = solucionProblema.getZ();

      int cant_contenidos       = solucionProblema.getCant_contenidos();
      int cant_centrosDatos     = solucionProblema.getCant_centrosDatos();
      int cant_pasosTiempo      = solucionProblema.getCant_pasosTiempo();
      int cant_regionesUsuarios = solucionProblema.getCant_regionesUsuarios();
      int cant_pasos_horas      = solucionProblema.getCant_pasos_Horas();


      result+="Y_techo[centroDato]="+"\n";
      for (int centroDato = 0; centroDato < cant_centrosDatos;centroDato++){

          if(Y_techo[centroDato]>0){
              result+="Y_techo["+centroDato+"]="+String.valueOf(Y_techo[centroDato])+"\n";
          }
      }

      result+="\n";




      result+="Y[centroDato][tiempoStep]="+"\n";



      for (int centroDato = 0; centroDato < cant_centrosDatos;centroDato++){
          for(int tiempoStep =0;tiempoStep<cant_pasos_horas;tiempoStep++){

              if(Y[centroDato][tiempoStep]>0){
                  result+="Y["+centroDato+"]["+tiempoStep+"]="+String.valueOf(Y[centroDato][tiempoStep])+"\n";
              }
          }
          result+="\n";
      }

      result+="\n";



      result+="X[contenidoK][centroDato]="+"\n";
      for (int contenidoK = 0; contenidoK < cant_contenidos;contenidoK++){
          for(int centroDato =0;centroDato<cant_centrosDatos;centroDato++){
              if(X[contenidoK][centroDato]>0){
                  result+="X["+contenidoK+"]["+centroDato+"]="+String.valueOf(X[contenidoK][centroDato])+"\n";
              }

          }

      }
      result+="\n";


//      result+="Z[regUsuaario][contenidoK][centroDato][tiempoStep]="+"\n";
//
//      for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
//          for(int timeStep : solucionProblema.getZ_hash()[centroDato].keySet()) {
//              for(int region: solucionProblema.getZ_hash()[centroDato].get(timeStep).keySet() ){
//                  for(int contenido: solucionProblema.getZ_hash()[centroDato].get(timeStep).get(region).keySet() ){
//                      int cantidadDeContenidos = solucionProblema.getZ_hash()[centroDato].get(timeStep).get(region).get(contenido);
//                      result+="Z["+region+"]["+contenido+"]["+centroDato+"]["+timeStep+"]="+String.valueOf(cantidadDeContenidos)+"\n";
//                  }
//              }
//          }
//      }



      result+="\n";
      return result ;

  }


    private HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>[]  transform_Z_to_Zhash(int cant_contenidos,int cant_centrosDatos,int cant_pasosTiempo,int cant_regionesUsuarios,int [][][][] Z){

        HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>[] Z_hash    = new HashMap [cant_centrosDatos];
        for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
            Z_hash[centroDato] = new HashMap<>();
            for (int tiempoStep = 0; tiempoStep < cant_pasosTiempo; tiempoStep++) {
                HashMap<Integer, HashMap<Integer, Integer>> regionesCopia = null;
                for (int regUsuaario = 0; regUsuaario < cant_regionesUsuarios;regUsuaario++) {
                    HashMap <Integer,Integer> cantidadesContenidos = null;
                    for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {


                        if(Z[regUsuaario][contenidoK][centroDato][tiempoStep]>0){
                            if(cantidadesContenidos==null){
                                cantidadesContenidos = new HashMap<>();
                            }
                            cantidadesContenidos.put(contenidoK,Z[regUsuaario][contenidoK][centroDato][tiempoStep]);
                        }

                    }
                    if(cantidadesContenidos!=null){
                        if(regionesCopia==null){
                            regionesCopia = new HashMap<>();
                        }
                        regionesCopia.put(regUsuaario,cantidadesContenidos);
                    }
                }
                if(regionesCopia!=null){
                    Z_hash[centroDato].put(tiempoStep,regionesCopia);
                }

            }
        }
        return Z_hash;
    }


}
