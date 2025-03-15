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

import org.uma.jmetal.operator.MutationOperator;
import soluciones.CDNSolution;
import org.uma.jmetal.util.JMetalException;
import utils.CdnItem;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * uno de los operadores de mutacion para el CDN
 */
@SuppressWarnings("serial")
public class CDNmMutation implements MutationOperator<CDNSolution> {

    private Double mutationProbability = null;
    private JMetalRandom randomGenenerator ;
    private Double  genMutationProbability = null;

    public CDNmMutation() {
        randomGenenerator = JMetalRandom.getInstance() ;
    }

    /**
     *
     * Constructor VER SI QUEDA ESTA FIRMA
     *
     * */
    public CDNmMutation(double mutationProbability, double genMutationProbability) {
        this.mutationProbability = mutationProbability ;
        this.genMutationProbability = genMutationProbability;
        randomGenenerator = JMetalRandom.getInstance() ;
    }

    /* Getters */

    public Double getMutationProbability() {
        return mutationProbability;
    }


    public Double getgenMutationProbability() {
        return genMutationProbability;
    }


    /**
     * Perform the operation
     *
     * @param probability Mutation setProbability
     * @param solution    The solution to mutate
     */
    public void doMutation(double probability, double genMutationProbability,CDNSolution solution)  {



        //probability es la probabilidad de mutar
        //genMutationProbability es la de cada gen
        if (randomGenenerator.nextDouble() < probability) {

            //RECUPERO la unica variable que adentro tiene todas las matrices
            CdnItem variableCDN = solution.getVariableValue(0);
            variableCDN.setInicioMut(System.currentTimeMillis());
            int[][] X = variableCDN.getX();
            int[][] Y = variableCDN.getY();
            int[] Y_techo = variableCDN.getY_techo();


            int cant_contenidos = variableCDN.getCant_contenidos();
            int cant_centrosDatos = variableCDN.getCant_centrosDatos();
            int cant_pasos_Horas = variableCDN.getCant_pasos_Horas();

            boolean seMutoAlgunBit = false;

                    //cambiar recurso de DC
                    int [] cantCentroDatosRecurso = new int[cant_contenidos];
                    for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
                        for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                            cantCentroDatosRecurso[contenidoK]+=X[contenidoK][centroDato];
                        }
                    }

                    for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
                        for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                            if (randomGenenerator.nextDouble() < genMutationProbability) {
                                seMutoAlgunBit = true;
                                if(X[contenidoK][centroDato]==0){
                                    X[contenidoK][centroDato]++;
                                    cantCentroDatosRecurso[contenidoK]++;
                                }else{
                                    //pregunto cuantos hay para q el recurso este en al menos un cd
                                    if( cantCentroDatosRecurso[contenidoK]>1){
                                        X[contenidoK][centroDato]--;
                                        cantCentroDatosRecurso[contenidoK]--;
                                    }
                                }


                            }

                        }
                    }

                    for (int tiempoStep = 0; tiempoStep < cant_pasos_Horas; tiempoStep++) {
                       for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                            if (randomGenenerator.nextDouble() < genMutationProbability) {
                                seMutoAlgunBit = true;
                                 int cd1 = centroDato;
                                int cd2 = randomGenenerator.nextInt(0, cant_centrosDatos-1);
                                while(cd1==cd2){
                                     cd2 = randomGenenerator.nextInt(0, cant_centrosDatos-1);
                                }
                                if (Y[cd1][tiempoStep] > 0) {
                                    int cantMaquinasMoverAotroDc= randomGenenerator.nextInt(1, Y[cd1][tiempoStep]);
                                    Y[cd1][tiempoStep]-=cantMaquinasMoverAotroDc;
                                    Y[cd2][tiempoStep]+=cantMaquinasMoverAotroDc;

                                } else {
                                    //aca se estaria aumentando la cantidad global de maquinas para cubrir la demanda en el tiempo t
                                    Y[cd1][tiempoStep] = 1;
                                }

                            }
                        }

                    }


                    //ytecho no debe ser superior a y en ningun t
                    solution.getInstanciaDatos().factibilizarEntreLasYs(Y,Y_techo);

                    //debe existir algun recurso en un datacenter con vm asignada

                    for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                        if (randomGenenerator.nextDouble() < genMutationProbability) {
                            seMutoAlgunBit = true;
                            //System.out.printlnln(Y_techo[centroDato]);
                            if (Y_techo[centroDato] > 0) {
                                Y_techo[centroDato] = randomGenenerator.nextInt(0, Y_techo[centroDato] - 1);
                            } else {
                                Y_techo[centroDato] = 1;
                            }
                            //System.out.printlnln(Y_techo[centroDato]);
                        }
                    }

           if(seMutoAlgunBit) {
               //se factibiliza la solucion
//               CdnFactibilizador fact = new CdnFactibilizador();
//               fact.factibilizarSolucion(solution);
               solution.factibilizar();
           }
            variableCDN.setFinMut(System.currentTimeMillis());
        }


    }

    /** Execute() method */
    @Override
    public CDNSolution execute(CDNSolution solution) {
        if (null == solution) {
            throw new JMetalException("Null parameter");
        }

        doMutation(mutationProbability,genMutationProbability, solution);

        return solution;
    }
}
