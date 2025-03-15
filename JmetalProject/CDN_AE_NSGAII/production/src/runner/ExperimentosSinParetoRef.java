package runner;




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



import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import operadores.*;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import problema.*;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import soluciones.*;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of experimental study based on solving the ZDT problems with four versions of NSGA-II, each
 * of them applying a different crossover probability (from 0.7 to 1.0).
 *
 * This experiment assumes that the reference Pareto front are not known, so the names of files containing
 * them and the directory where they are located must be specified.
 *
 * Six quality indicators are used for performance assessment.
 *
 * The steps to carry out the experiment are:
 * 1. Configure the experiment
 * 2. Execute the algorithms
 * 3. Generate the reference Pareto fronts
 * 4. Compute the quality indicators
 * 5. Generate Latex tables reporting means and medians
 * 6. Generate Latex tables with the result of applying the Wilcoxon Rank Sum Test
 * 7. Generate Latex tables with the ranking obtained by applying the Friedman test
 * 8. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ExperimentosSinParetoRef {
  private static final int INDEPENDENT_RUNS = 2 ;

  public static void main(String[] args) throws IOException {
//    if (args.length != 2) {
//      throw new JMetalException("Needed arguments: experimentBaseDirectory referenceFrontDirectory") ;
//    }
//    String experimentBaseDirectory = "F:/PROYE/solocodigo/proycodigo/salida/prr/referenceFrontDirectory" ;
//    String referenceFrontDirectory = "F:/PROYE/solocodigo/proycodigo/salida/prr/experimentBaseDirectory" ;
String experimentBaseDirectory = "/home/ggoni/Documentos/prr/referenceFrontDirectory33" ;
    String referenceFrontDirectory = "/home/ggoni/Documentos/prr/experimentBaseDirectory33" ;

    String nombreArchivoPropertiesInstancia1="300Vid_300Mins_6Prove_1XreG_1XreQ";
    String nombreArchivoPropertiesInstancia2="300Vid_300Mins_6Prove_1XreG_1XreQ";



    List<Problem<CDNSolution>> problemList = Arrays.<Problem<CDNSolution>>asList(new MultiobjCDN(nombreArchivoPropertiesInstancia1)) ;

    List<TaggedAlgorithm<List<CDNSolution>>> algorithmList = configureAlgorithmList(problemList, INDEPENDENT_RUNS) ;

    Experiment<CDNSolution, List<CDNSolution>> experiment =
        new ExperimentBuilder<CDNSolution, List<CDNSolution>>("ExperimentosSinParetoRef")
            .setAlgorithmList(algorithmList)
            .setProblemList(problemList)
            .setExperimentBaseDirectory(experimentBaseDirectory)
            .setOutputParetoFrontFileName("FUN")
            .setOutputParetoSetFileName("VAR")
            .setReferenceFrontDirectory(referenceFrontDirectory)
            .setIndicatorList(Arrays.asList(
                new Epsilon<CDNSolution>(), new Spread<CDNSolution>(), new GenerationalDistance<CDNSolution>(),
                new PISAHypervolume<CDNSolution>(),
                new InvertedGenerationalDistance<CDNSolution>(), new InvertedGenerationalDistancePlus<CDNSolution>()))
            .setIndependentRuns(INDEPENDENT_RUNS)
            .setNumberOfCores(1)
            .build();

    new ExecuteAlgorithms<>(experiment).run();
    new GenerateReferenceParetoFront(experiment).run();
    new ComputeQualityIndicators<>(experiment).run() ;
    new GenerateLatexTablesWithStatistics(experiment).run() ;
    new GenerateWilcoxonTestTablesWithR<>(experiment).run() ;
    new GenerateFriedmanTestTables<>(experiment).run();
    new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run() ;
  }

  /**
   * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of a
   * {@link TaggedAlgorithm}, which is a decorator for class {@link Algorithm}. The {@link TaggedAlgorithm}
   * has an optional tag component, that can be set as it is shown in this example, where four variants of a
   * same algorithm are defined.
   *
   * @param problemList
   * @return
   */
  static List<TaggedAlgorithm<List<CDNSolution>>> configureAlgorithmList(
      List<Problem<CDNSolution>> problemList,
      int independentRuns) {
    List<TaggedAlgorithm<List<CDNSolution>>> algorithms = new ArrayList<>() ;

    double genMutationProbability = 0.2; //Se setean con valores si args tiene algo
    double crossoverProbability = 0.9; //Se setean con valores si args tiene algo
    int populationSize = 100; //tiene que ser divisible por 2
    int maxEvaluations = 20000;
    double genMutationProbabilityAfterGreedy=0.9;
    int greedyDeterministic_level = 1;
    // 1-GreedyOnCost, 2-GreedyOnCost, OtroValor-inicializa Random
    int formaDeInit=1;

    Algorithm<List<CDNSolution>> algorithm;
    CrossoverOperator<CDNSolution> crossover;
    MutationOperator<CDNSolution> mutation;
    SelectionOperator<List<CDNSolution>, CDNSolution> selection;
    MultiobjCDN problem ;
    double mutationProbability ;
    for (int run = 0; run < independentRuns; run++) {
      for (int i = 0; i < problemList.size(); i++) {


        genMutationProbability = 0.1; //Se setean con valores si args tiene algo
        crossoverProbability = 0.1; //Se setean con valores si args tiene algo
        populationSize = 100; //tiene que ser divisible por 2
        maxEvaluations = 20000;

        problem = (MultiobjCDN)problemList.get(i);
        problem.setGenMutationProbabilityAfterGreedy(genMutationProbabilityAfterGreedy);
        problem.setFormaInit(formaDeInit);
        problem.setGreedyDeterministic_level(greedyDeterministic_level);
        crossover = new CDNCrossover(crossoverProbability);
        mutationProbability = 1.0 / problem.getNumberOfVariables();
        mutation = new CDNmMutation(mutationProbability, genMutationProbability);
        selection = new BinaryTournamentSelection<CDNSolution>(new RankingAndCrowdingDistanceComparator<CDNSolution>());
        algorithm= new NSGAIIBuilder<CDNSolution>(problem, crossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(maxEvaluations)
                .setPopulationSize(populationSize)
                .build() ;
        algorithms.add(new TaggedAlgorithm<List<CDNSolution>>(algorithm, "NSGAIIExperimentosA", problemList.get(i), run));


         genMutationProbability = 0.8; //Se setean con valores si args tiene algo
         crossoverProbability = 0.5; //Se setean con valores si args tiene algo
         populationSize = 100; //tiene que ser divisible por 2
         maxEvaluations = 20000;


        problem = (MultiobjCDN)problemList.get(i);
        problem.setGenMutationProbabilityAfterGreedy(genMutationProbabilityAfterGreedy);
        problem.setFormaInit(formaDeInit);
        problem.setGreedyDeterministic_level(greedyDeterministic_level);
        crossover = new CDNCrossover(crossoverProbability);
        mutationProbability = 1.0 / problem.getNumberOfVariables();
        mutation = new CDNmMutation(mutationProbability, genMutationProbability);
        selection = new BinaryTournamentSelection<CDNSolution>(new RankingAndCrowdingDistanceComparator<CDNSolution>());
        algorithm= new NSGAIIBuilder<CDNSolution>(problem, crossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(maxEvaluations)
                .setPopulationSize(populationSize)
                .build() ;
        algorithms.add(new TaggedAlgorithm<List<CDNSolution>>(algorithm, "NSGAIIExperimentosB", problemList.get(i), run));




      }


    }
    return algorithms ;
  }
}