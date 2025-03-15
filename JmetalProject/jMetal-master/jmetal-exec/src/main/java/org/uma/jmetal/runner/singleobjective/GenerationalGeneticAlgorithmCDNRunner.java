////  This program is free software: you can redistribute it and/or modify
////  it under the terms of the GNU Lesser General Public License as published by
////  the Free Software Foundation, either version 3 of the License, or
////  (at your option) any later version.
////
////  This program is distributed in the hope that it will be useful,
////  but WITHOUT ANY WARRANTY; without even the implied warranty of
////  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
////  GNU Lesser General Public License for more details.
////
////  You should have received a copy of the GNU Lesser General Public License
////  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//package org.uma.jmetal.runner.singleobjective;
//
//import org.uma.jmetal.algorithm.Algorithm;
//import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
//import org.uma.jmetal.operator.CrossoverOperator;
//import org.uma.jmetal.operator.MutationOperator;
//import org.uma.jmetal.operator.SelectionOperator;
//import org.uma.jmetal.operator.impl.crossover.CDNCrossover;
//import org.uma.jmetal.operator.impl.mutation.CDNmMutation;
//import org.uma.jmetal.operator.impl.selection.NaryTournamentSelection;
//
//import org.uma.jmetal.problem.singleobjective.CDNProblem;
//import org.uma.jmetal.solution.CDNSolution;
//import org.uma.jmetal.util.AlgorithmRunner;
//import org.uma.jmetal.util.JMetalLogger;
//import org.uma.jmetal.util.comparator.ObjectiveComparator;
//import org.uma.jmetal.util.fileoutput.SolutionListOutput;
//import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.*;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Class to configure and run a generational genetic algorithm. The target problem is OneMax.
// *
// * @author Antonio J. Nebro <antonio@lcc.uma.es>
// */
//public class GenerationalGeneticAlgorithmCDNRunner {
//  /**
//   * Usage: java org.uma.jmetal.runner.singleobjective.GenerationalGeneticAlgorithmRunner
//   */
//  public static void main(String[] args) throws Exception {
//    CDNProblem problem;
//    String nombreArchivoPropertiesInstancia="instancia1";
//    //---------------------------------------------------------------------
//    //---------------------------------------------------------------------
//    //PARAMETROS DEL ALGORITMO
//      double genMutationProbability = 0.1; //Se setean con valores si args tiene algo
//      double crossoverProbability = 0.5; //Se setean con valores si args tiene algo
//
//      int tamanioTorneo = 2; //aumentar el tamanio de torneo aumenta la presion de seleecion
//      int populationSize = 10; //tiene que ser divisible por 2
//      int maxEvaluations = 10; // cant generacions = MaxEvaluations/PopulationSize  16.81183335954285
//      //------------------------------------------------------------------
//
//    // PARAMETROS DEL PROBLEMA /CDN/otraGeneracion/1500Vid_300Min_6Prov/
//      // se utiliza si no se llama desde consola                   // otraGeneracion   /otraGeneracion/1500Vid_1199Min_6Prov /CDN/otraGeneracion/1500Vid_600Min_6Prov/
//      String instanciaUtilizada = "/CDN/otraGeneracion/1500Vid_300Min_6Prov/"; //   100Vid_1155Min 200Vid_595Min 100Videos_3Hhoras 2000Vid_1199Min_6Prove
//   // String instanciaUtilizada = "F:\\PROYE\\proygrado\\proy\\jMetal-master\\jmetal-core\\target\\classes\\CDN\\otraGeneracion\\1500Vid_300Min_6Prov\\"; //   100Vid_1155Min 200Vid_595Min 100Videos_3Hhoras 2000Vid_1199Min_6Prove
//
//
//    //el PROMEDIO de la Qos es 0.494392724
//      //el MEDIANA de la Qos es 0.5309633076
//      double alfa_Qos = 0.5 ; //se usa la mediana
//      //tamanio del ranking de los n primeros mas populares por categoria
//      int largoRankingAgrupaVideos =100;
//      // 0.5 Mbyte asumimos quee un archivo original pesa eso
//      double tamanioBloqueVideo = 500000;
//      double coef_demanda_pico = 0.5; //(+70% de lo comun)
//      double genMutationProbabilityAfterGreedy=0.5;
//
//      int greedyDeterministic_level = 1;
//      // 1-GreedyOnCost, 2-GreedyOnCost, OtroValor-inicializa Random
//      int formaDeInit=1;
//    //---------------------------------------------------------------------
//
////// TODO: 29/03/2017 cambiar los parametros por el nombre de la instancia nomas
//    //---------------------------------------------------------------------
//    //se sustituyen algunos parametros por defecto si es llamado desde linea de comandos
//    String iteracion ="";
//    String rutaAbsolutaArchivosResultados ="";
//    //se levantan argumentos si es que existen.
//    if (args.length != 0) {
//      rutaAbsolutaArchivosResultados = args[0];
//      iteracion=args[1];
//      crossoverProbability = Double.valueOf(args[2]);
//      genMutationProbability = Double.valueOf(args[3]);
//
//    }
//    //-------------------------------------------------------------------------
//
//
//    Algorithm<CDNSolution> algorithm;
//    CrossoverOperator<CDNSolution> crossover;
//    MutationOperator<CDNSolution> mutation;
//    SelectionOperator<List<CDNSolution>, CDNSolution> selection;
//
//
//    problem = new CDNProblem( nombreArchivoPropertiesInstancia) ;
//    //se setea la probabilidad de mutar cada gen de la solucion del greedy
//    problem.setGenMutationProbabilityAfterGreedy(genMutationProbabilityAfterGreedy);
//
//    problem.setFormaInit(formaDeInit);
//    problem.setGreedyDeterministic_level(greedyDeterministic_level);
//
//    crossover = new CDNCrossover(crossoverProbability);
//
//    double mutationProbability = 1.0 / problem.getNumberOfVariables();
//    mutation = new CDNmMutation(mutationProbability, genMutationProbability);
//
//
//    //selection = new BinaryTournamentSelection<CDNSolution>(new ObjectiveComparator<CDNSolution>(0, ObjectiveComparator.Ordering.ASCENDING));
//
//    //selection = new NaryTournamentSelection<CDNSolution>(tamanioTorneo,new CostComparator<CDNSolution>());
//
//    selection = new NaryTournamentSelection<CDNSolution>(tamanioTorneo, new ObjectiveComparator<CDNSolution>(0, ObjectiveComparator.Ordering.ASCENDING));
//
//
//            algorithm = new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
//            .setPopulationSize(populationSize)
//            .setMaxEvaluations(maxEvaluations)
//            .setSelectionOperator(selection)
//            .build();
//
//
//
//   /* algorithm = new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
//            .setPopulationSize(populationSize)
//            .setMaxEvaluations(maxEvaluations)
//            .setSelectionOperator(selection)
//            .build();*/
//
//    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
//            .execute();
//
//    CDNSolution solution = algorithm.getResult();
//    List<CDNSolution> population = new ArrayList<>(1);
//    population.add(solution);
//
//    long computingTime = algorithmRunner.getComputingTime();
//
//    //si lo llamo con args es muy probable que este llamando varias instancias
//    //por lo tanto no mando a log y guardo los valores de ejcuciones
//    //en archivos diferentes.
//    if(args.length!=0) {
//      String separador = " ";
//      String datosEjecucion ="";
//      datosEjecucion+="Instancia"+separador+instanciaUtilizada;
//      datosEjecucion+="\n";
//      datosEjecucion+="genMutationProbability"+separador+genMutationProbability;
//      datosEjecucion+="\n";
//      datosEjecucion+="crossoverProbability"+separador+crossoverProbability;
//      datosEjecucion+="\n";
//      datosEjecucion+="tamanioTorneo"+separador+tamanioTorneo;
//      datosEjecucion+="\n";
//      datosEjecucion+="populationSize"+separador+populationSize;
//      datosEjecucion+="\n";
//      datosEjecucion+="maxEvaluations"+separador+maxEvaluations;
//      datosEjecucion+="\n";
//      datosEjecucion+="alfa_Qos"+separador+alfa_Qos;
//      datosEjecucion+="\n";
//      datosEjecucion+="largoRankingAgrupaVideos"+separador+largoRankingAgrupaVideos;
//      datosEjecucion+="\n";
//      datosEjecucion+="tamanioBloqueVideo"+separador+tamanioBloqueVideo;
//      datosEjecucion+="\n";
//      datosEjecucion+="coef_demanda_pico"+separador+coef_demanda_pico;
//      datosEjecucion+="\n";
//      datosEjecucion+="genMutationProbabilityAfterGreedy"+separador+genMutationProbabilityAfterGreedy;
//      datosEjecucion+="\n";
//      datosEjecucion+="greedyDeterministic_level"+separador+greedyDeterministic_level;
//      datosEjecucion+="\n";
//      datosEjecucion+="formaDeInit"+separador+formaDeInit;
//      datosEjecucion+="\n";
//
//
//
//
//
//
//      String rutaResultados = rutaAbsolutaArchivosResultados+args[2]+"_"+args[3]+"_"+"Resultados.tsv";
//      String rutaHistoricoFitness = rutaAbsolutaArchivosResultados+"fitness/"+args[2]+"_"+args[3]+"_"+"fitness.tsv";
//
//
//      String rutaVariablesImpresas = rutaAbsolutaArchivosResultados+"vars/"+iteracion+"_"+args[2]+"_"+args[3]+"_"+"VAR_"+computingTime+"ms.tsv";
//      // se guardan las soluciones en carpeta a parte
//     /* new SolutionListOutput(population)
//              .setSeparator("\t")
//              .setVarFileOutputContext(new DefaultFileOutputContext(rutaVariablesImpresas))
//              .print();*/
//
//
//      /*En la primer iteracion tambien se crea el archivo y  su cabezal
//      * en las siguientes iteraciones se guardan solo los valores de las soluciones*/
//
//      BufferedWriter writer =null;
//      try{
//      Path path = Paths.get(rutaResultados);
//
//
//     /*
//     * La idea de este archivo no es que quede lindo
//     * es poder levantarlo con excel o algo de eso para hacer las cuentas
//     * por eso se separa todos los campos con el mismo separador
//     * */
//
//
//      if(! Files.exists(path)){
//        writer = Files.newBufferedWriter(path,  StandardCharsets.UTF_8, StandardOpenOption.CREATE);
//        writer.write(datosEjecucion);
//        writer.newLine();
//        writer.newLine();
//        writer.newLine();
//        writer.write("Iteracion"+separador+ "Costo"+separador+ "Tiempo");
//        writer.newLine();
//        writer.close();
//      }
//
//      //guardo valores de la solucion actual.
//      writer = Files.newBufferedWriter(path,  StandardCharsets.UTF_8, StandardOpenOption.APPEND);
//      writer.write(iteracion+separador+solution.getObjective(0)+separador+computingTime);
//      writer.newLine();
//
//      writer.close();
//
//
//
//        Path pathfitness = Paths.get(rutaHistoricoFitness);
//
//
//     /*
//     * La idea de este archivo no es que quede lindo
//     * es poder levantarlo con excel o algo de eso para hacer las cuentas
//     * por eso se separa todos los campos con el mismo separador
//     * */
//
//
//        if(! Files.exists(pathfitness)){
//          writer = Files.newBufferedWriter(pathfitness,  StandardCharsets.UTF_8, StandardOpenOption.CREATE);
//          writer.newLine();
//          writer.write(datosEjecucion);
//          writer.newLine();
//          writer.newLine();
//          writer.newLine();
//          writer.close();
//        }
//
//        //guardo el historico de fitness de la soluc actual.
//        writer = Files.newBufferedWriter(pathfitness,  StandardCharsets.UTF_8, StandardOpenOption.APPEND);
//          writer.write("--");
//          writer.newLine();
//          writer.write("--");
//          writer.newLine();
//        writer.write("--");
//          writer.newLine();
//        writer.write("Historico de Fitness Iteracion: "+iteracion);
//          writer.newLine();
//          writer.write("Tiempo de ejecucion: "+computingTime+" ms");
//          writer.newLine();
//        int generacion =0;
//        int contudo=0;
//        ArrayList <Double> historicofintess = problem.getHistoricoFitness();
//        double minimo = Collections.min(historicofintess);
//        for(double fitness :historicofintess){
//          if (contudo%populationSize==0){
//            writer.write("--------------------------------");
//              writer.newLine();
//            writer.write("Fitness Generacion: "+generacion);
//              writer.newLine();
//            writer.write("--------------------------------");
//              writer.newLine();
//            generacion++;
//          }
//          String resaltador = minimo==fitness?" ===> ":"      ";
//          writer.write(resaltador+fitness);
//            writer.newLine();
//          contudo++;
//        }
//        writer.newLine();
//          writer.write("--------------------------------");
//          writer.newLine();
//          writer.write("FIN  Iteracion: "+iteracion);
//          writer.newLine();
//          writer.newLine();
//          writer.write("Tiempo de ejecucion: "+computingTime+" ms");
//          writer.write("--------------------------------");
//        writer.newLine();
//        writer.newLine();
//        writer.close();
//
//
//      System.out.println("Se guard info:"+rutaVariablesImpresas);
//      System.out.println("Se guard info:"+rutaResultados);
//      System.out.println("Se guard info:"+rutaHistoricoFitness);
//
//    } catch (Exception ex) {
//      ex.printStackTrace();
//      try {
//        writer.close();
//      } catch (IOException ex2) {
//        ex2.printStackTrace();
//      }
//    }
//
//
//
//  }else{
//      //Configuracion por defecto, para correr local desde el ide
//      new SolutionListOutput(population)
//              .setSeparator("\t")
//              .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
//              .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
//              .print();
//
//
//      //despues se tirara en el archivo de las pruebas.
//      System.out.println("--");
//      System.out.println("Historico de Fitness:");
//      int generacion =0;
//      int contudo=0;
//      ArrayList <Double> historicofintess = problem.getHistoricoFitness();
//      double minimo = Collections.min(historicofintess);
//      for(double fitness :historicofintess){
//        if (contudo%populationSize==0){
//          System.out.println("--------------------------------");
//          System.out.println("Fitness Generacion: "+generacion);
//          System.out.println("--------------------------------");
//          generacion++;
//        }
//        String resaltador = minimo==fitness?" ===> ":"      ";
//        System.out.println(resaltador+fitness);
//        contudo++;
//      }
//
//
//
//
//      JMetalLogger.logger.info("Mejor Costo: " + solution.getObjective(0));
//      JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
//      JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
//      JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
//    }
//  }
//}
