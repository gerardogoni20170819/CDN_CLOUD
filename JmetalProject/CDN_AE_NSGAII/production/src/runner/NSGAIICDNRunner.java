//  NSGAIIRunner.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2014 Antonio J. Nebro
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

package runner;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import operadores.*;

import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import problema.MultiobjCDN;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import soluciones.CDNSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

//java -Xmx4000m -XX:+UseParallelOldGC -jar /home/gerardo.goni/greedychico/$iter/cdn.jar /home/gerardo.goni/greedychico/result/$iter/ $(($iter+30)) 0.5 0.00

public class NSGAIICDNRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws IOException
   * @throws SecurityException
   * @throws ClassNotFoundException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.NSGAIITSPRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, IOException {
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------


    //JMetalLogger.configureLoggers(new File("configLogger.properties"));


    //PARAMETROS DEL ALGORITMO
    double genMutationProbability = 0.9; //Se setean con valores si args tiene algo
    double crossoverProbability = 0.9; //Se setean con valores si args tiene algo

    int tamanioTorneo = 2; //aumentar el tamanio de torneo aumenta la presion de seleecion
    int populationSize = 200; //tiene que ser divisible por 2
    int maxEvaluations = 200000; // cant generacions = MaxEvaluations/PopulationSize  16.81183335954285

    //String instanciaUtilizada = "1500Vid_300Min_6Prov";
    //String instanciaUtilizada = "6161Vid_240Mins_6Prove_1XreG_1XreQ";
    //String instanciaUtilizada = "6161Vid_240Mins_6Prove_1XreG_1XreQ";
    //String instanciaUtilizada = "6161Vid_300Mins_6Prove_3XreG_7XreQ";
    //String instanciaUtilizada = "300Vid_300Mins_6Prove_1XreG_1XreQ";

    //String instanciaUtilizada = "1000Vid_300Mins_6Prove_1XreG_1XreQ";
    //String instanciaUtilizada = "tandaA1_500Vid_300Mins_6Prove_1XreG_1XreQ";
    String instanciaUtilizada = "tandaD3_16000Vid_240Mins_6Prove_6XreG_6XreQ";

    double genMutationProbabilityAfterGreedy=0.9;

    int greedyDeterministic_level = 1;
    // 1-GreedyOnCost, 2-GreedyOnCost, OtroValor-inicializa Random
    int formaDeInit=1;
    //---------------------------------------------------------------------


    //---------------------------------------------------------------------
    //se sustituyen algunos parametros por defecto si es llamado desde linea de comandos
    String iteracion ="";
    String rutaAbsolutaArchivosResultados ="";
    //se levantan argumentos si es que existen.
    if (args.length != 0) {
      rutaAbsolutaArchivosResultados = args[0];
      iteracion=args[1];
      crossoverProbability = Double.valueOf(args[2]);
      genMutationProbability = Double.valueOf(args[3]);
      instanciaUtilizada = String.valueOf(args[4]);

    }
    //-------------------------------------------------------------------------



    MultiobjCDN problem;
    Algorithm<List<CDNSolution>> algorithm;
    CrossoverOperator<CDNSolution> crossover;
    MutationOperator<CDNSolution> mutation;
    SelectionOperator<List<CDNSolution>, CDNSolution> selection;

    problem = new MultiobjCDN(instanciaUtilizada);
    problem.setGenMutationProbabilityAfterGreedy(genMutationProbabilityAfterGreedy);

    problem.setFormaInit(formaDeInit);
    problem.setGreedyDeterministic_level(greedyDeterministic_level);

    crossover = new CDNCrossover(crossoverProbability);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    mutation = new CDNmMutation(mutationProbability, genMutationProbability);

    selection = new BinaryTournamentSelection<CDNSolution>(new RankingAndCrowdingDistanceComparator<CDNSolution>());

    algorithm = new NSGAIIBuilder<CDNSolution>(problem, crossover, mutation)
            .setSelectionOperator(selection)
            .setMaxEvaluations(maxEvaluations)
            .setPopulationSize(populationSize)
            .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    List<CDNSolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    for (CDNSolution solute: population ) {
      solute.toFileGeneral(problem.getInstanciaDatos().getRutaAbsolutaSalidaInstancia());
      solute.toFilePareto(problem.getInstanciaDatos().getRutaAbsolutaSalidaInstancia());
    }


    new SolutionListOutput(population)
            .setSeparator("\t")
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
            .print();

    for (String fitnesQos:problem.getHistoricoFitness()
         ) {
      System.out.println(fitnesQos);
    }


    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
  }
}
