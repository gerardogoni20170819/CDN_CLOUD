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

package operadores;

import org.uma.jmetal.operator.CrossoverOperator;
import soluciones.CDNSolution;
import org.uma.jmetal.util.JMetalException;
import utils.CdnItem;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class CDNCrossover implements CrossoverOperator<CDNSolution> {
  private double crossoverProbability ;
  private JMetalRandom randomGenerator ;

  /** Constructor */
  public CDNCrossover(double crossoverProbability) {
    if (crossoverProbability < 0) {
      throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
    }
    this.crossoverProbability = crossoverProbability ;
    randomGenerator = JMetalRandom.getInstance() ;
  }

  /* Getter */
  public double getCrossoverProbability() {
    return this.crossoverProbability;
  }

  /** Execute() method */
  public List<CDNSolution> execute(List<CDNSolution> parents) {
    if (parents.size() != 2) {
      throw new JMetalException("CDNCrossover.execute: operator needs two parents");
    }
    return doCrossover(crossoverProbability, parents.get(0), parents.get(1));
  }

  /**
   * Perform the crossover operation
   *
   * @param probability Crossover setProbability
   * @param parent1     The first parent
   * @param parent2     The second parent
   * @return An array containing the two offspring
   * @throws JMetalException
   */
  public List<CDNSolution> doCrossover(double probability,
                                       CDNSolution parent1,
                                       CDNSolution parent2) throws JMetalException {

    //copia limpia de los padres
    //el copy() hace copia limpia en profundidad

    List<CDNSolution> offspring = new ArrayList<>();
    offspring.add((CDNSolution) parent1.copy()) ; //clono siempre, y si cruzo lo hago directamente sobre esta variable copiada
    offspring.add((CDNSolution) parent2.copy()) ;//clono siempre, y si cruzo lo hago directamente sobre esta variable copiada


      //decide si los cruza o no
      if (randomGenerator.nextDouble() <  probability) {

          //RECUPERO la unica variable que adentro tiene todas las matrices

          //CdnItem padre1 = parent1.getVariableValue(0);
          //se edita el valor de los hijos directamente
          //si se copia nuevamente el padre se pierda un disparate de memoria
          //ver que lo que hay en offspring ya es copia limpia
          CdnItem hijo1_variableCDN = offspring.get(0).getVariableValue(0)  /*padre1.clone()*/;
          hijo1_variableCDN.setInicioCross(System.currentTimeMillis());
         // hijo1_variableCDN.setInicioCross(-10000);
          int [][] parent1_X=hijo1_variableCDN.getX();
          int [][] parent1_Y=hijo1_variableCDN.getY();
          int [] parent1_Y_techo=hijo1_variableCDN.getY_techo();

        //  System.err.println(System.currentTimeMillis());
          //RECUPERO la unica variable que adentro tiene todas las matrices
          CdnItem hijo2_variableCDN = offspring.get(1).getVariableValue(0);
          hijo2_variableCDN.setInicioCross(System.currentTimeMillis());
          int [][] parent2_X=hijo2_variableCDN.getX();
          int [][] parent2_Y=hijo2_variableCDN.getY();
          int [] parent2_Y_techo=hijo2_variableCDN.getY_techo();



          int cant_contenidos       = hijo2_variableCDN.getCant_contenidos();
          int cant_centrosDatos     = hijo2_variableCDN.getCant_centrosDatos();
          int cant_pasos_Horas      = hijo2_variableCDN.getCant_pasos_Horas();

              //para cada recurso sorteo si entercambio su distribucion en las soluciones
              for (int contenidoK = 0; contenidoK < cant_contenidos;contenidoK++){
                  if  (randomGenerator.nextDouble() < 0.5) {
                          for(int centroDato =0;centroDato<cant_centrosDatos;centroDato++){
                              int aux =parent1_X[contenidoK][centroDato];
                              parent1_X[contenidoK][centroDato]=parent2_X[contenidoK][centroDato];
                              parent2_X[contenidoK][centroDato]=aux;
                      }
                  }
              }
              //intercambiando por columnas de tiempo me aseguro que se sigur cubriendo la demanda
              for(int tiempoStep =0;tiempoStep<cant_pasos_Horas;tiempoStep++){
                      if  (randomGenerator.nextDouble() < 0.5) {
                          for (int centroDato = 0; centroDato < cant_centrosDatos;centroDato++){
                          int aux =parent1_Y[centroDato][tiempoStep];
                          parent1_Y[centroDato][tiempoStep]=parent2_Y[centroDato][tiempoStep];
                          parent2_Y[centroDato][tiempoStep]=aux;
                      }
                  }
              }

              for(int centroDato = 0; centroDato < cant_centrosDatos;centroDato++){
                  if  (randomGenerator.nextDouble() < 0.5) {
                      int aux =parent1_Y_techo[centroDato];
                      parent1_Y_techo[centroDato]=parent2_Y_techo[centroDato];
                      parent2_Y_techo[centroDato]=aux;
                  }
              }

         // }


          //ytecho no debe ser superior a y en ningun t
          parent1.getInstanciaDatos().factibilizarEntreLasYs(parent1_Y,parent1_Y_techo);
          parent1.getInstanciaDatos().factibilizarEntreLasYs(parent2_Y,parent2_Y_techo);

/*
          if(!fact.isFactible(offspring.get(0))){
              throw new JMetalException("no factible el loco");
          }else{
              System.out.println("factible el loco 0");
          }

          if(!fact.isFactible(offspring.get(1))){
              throw new JMetalException("no factible el loco");
          }else{
              System.out.println("factible el loco 1");
          }*/

          offspring.get(0).factibilizar();
          offspring.get(1).factibilizar();
          hijo1_variableCDN.setFinCross(System.currentTimeMillis());
          hijo2_variableCDN.setFinCross(System.currentTimeMillis());
        //  System.err.println("fing"+System.currentTimeMillis());
      }

    return offspring;
  }

  /**
   * Two parents are required to apply this operator.
   * @return
   */
  public int getNumberOfParents() {
    return 2 ;
  }
}
