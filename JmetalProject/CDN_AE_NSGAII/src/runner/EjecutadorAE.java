package runner;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import operadores.*;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.JMetalException;
import problema.MultiobjCDN;
import soluciones.CDNSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import utils.Archivos;
import utils.CdnHeuristica;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.util.front.imp.ArrayFront;
import utils.CdnInstancia;

/**
 * Created by ggoni on 21/09/17.
 */
public class EjecutadorAE {

    public EjecutadorAE() {
    }

    List<double []> global_solutionSet = new ArrayList<>();
    List<List<double []>> paretosSolucionesSeparados = new ArrayList<>();
    String rutaSalida ="";
    String separadorColumnasArchivo =" ";
    List<String> listaNombreArchivosEjecucion;
    String nombreAlgoritmo;
    String rutaAlgoritmo;
    String nombreArchiboParetoReferecncia="referenciaAE.pr";
    String nombreArchiboParetoAMPL="referenciaAMPL.pr";
    String nombreArchivoGreedyTodos="TodosGreedy.pr";
    String nombreArchivoGreedyPareto="ParetoGreedy.pr";
    String nombreArchivoRhypervolumen="hypervolume.r";
    String nombreArchivoRGraficas="graficas.r";
    String nombreArchivoGRAFICAparetoreferenciaTODOSAlgoritmos_AMPL="grafica_TODOS_AMPL.r";
    String nombreArchivoGRAFICAparetoreferenciaAE_GREEDY="grafica_AE_GREEDY.r";
    String nombreArchivoGRAFICAparetoreferenciaAE_GREEDY_AMPL="grafica_AE_GREEDY_AMPL.r";
    String nombreArchivoGRAFICAparetoreferenciaAE_AMPL="grafica_AE_AMPL.r";
    HashMap<String,String> nombreAlgoritmo_rutaTotalPareto;

    double maximoCosto =0;
    double maximoQoS =0;
    String descripcionInstancia;
    List<String> logEjecuciones;
    List<String> tiemposEjec;
    int cantidadAlgoritmos;
    String instanciaUtilizadaExperimento;
    List<Double> genMutationProbability;
    List<Double> crossoverProbability;
    List<Integer> populationSize;
    List<Integer> maxEvaluations;
    List<Integer> cantidadIteraciones;


    public static void main(String[] args) {

        try {
            EjecutadorAE eje = new EjecutadorAE();
            JMetalLogger.configureLoggers(new File("configLogger.properties"));
            String nombreArchivoPropertiesInstancia ="experimentoBase";
            if(args.length!=0){
                nombreArchivoPropertiesInstancia =args[0];
            }
            eje.leerPropertiesExperimento(nombreArchivoPropertiesInstancia);
            eje.nombreAlgoritmo_rutaTotalPareto = new HashMap<>();
            for(int algoritmo =0;algoritmo<eje.cantidadAlgoritmos;algoritmo++){
                eje.logEjecuciones=new ArrayList<>();
                eje.tiemposEjec=new ArrayList<>();
                eje.correrAlgoritmo(  eje.instanciaUtilizadaExperimento,
                        eje.cantidadIteraciones.get(algoritmo),
                        eje.genMutationProbability.get(algoritmo),
                        eje.crossoverProbability.get(algoritmo),
                        eje.populationSize.get(algoritmo),
                        eje.maxEvaluations.get(algoritmo));

            }
            eje.correrGreedy(eje.rutaSalida,eje.instanciaUtilizadaExperimento);
        }catch(Exception e){
            e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
        }

    }

    /**
     *
     * @param instanciaUtilizada
     * @param CANT_ITERACIONES
     * @param genMutationProbability
     * @param crossoverProbability
     * @param populationSize tiene que ser divisible por 2
     * @param maxEvaluations cant generacions = MaxEvaluations/PopulationSize
     */
    private void correrAlgoritmo(String instanciaUtilizada,
                                 int CANT_ITERACIONES,
                                 double genMutationProbability,
                                 double crossoverProbability,
                                 int populationSize,
                                 int maxEvaluations){

        nombreAlgoritmo = this.darNombreAlgoritmo(instanciaUtilizada,
                genMutationProbability,
                crossoverProbability,
                populationSize,
                maxEvaluations);

        //cargo la ruta de salida
        CdnInstancia insta = new CdnInstancia();
        insta.leerPropertiesInstancia(instanciaUtilizada);
        rutaSalida=insta.getRutaAbsolutaSalidaInstancia();
        descripcionInstancia= insta.descripcionInstancia;

        rutaAlgoritmo = rutaSalida+nombreAlgoritmo+"/";
        this.logEjecuciones.add(descripcionInstancia);
        this.logEjecuciones.add(nombreAlgoritmo);

        File directory = new File(rutaSalida);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+rutaSalida);
            this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Creando directorio "+rutaSalida);
        }
        directory = new File(rutaAlgoritmo);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+rutaAlgoritmo);
            this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Creando directorio "+rutaAlgoritmo);
        }

        //crear carpeta con nombre nombreAlgoritmo
        try {

            listaNombreArchivosEjecucion = new ArrayList<>();
            for(int iteracion =0;iteracion<CANT_ITERACIONES;iteracion++) {
                String nombreArchivoEjecucion = nombreAlgoritmo+"I"+iteracion+".pr";
                listaNombreArchivosEjecucion.add(nombreArchivoEjecucion);
                this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Inicio "+nombreArchivoEjecucion);
                //guardar cada paretito chico con nombreEjecucion
                //guardar lista de nombres para construir script R
                List<CDNSolution> soluciones = this.corredorProblem(instanciaUtilizada,
                                                                    genMutationProbability,
                                                                    crossoverProbability,
                                                                    populationSize,
                                                                    maxEvaluations);


                this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Cant soluciones "+soluciones.size());
                this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Fin "+nombreArchivoEjecucion);
                this.acumularParetosSoluciones(soluciones,iteracion);

                this.guardarParetoEnArchvo(paretosSolucionesSeparados.get(iteracion),nombreArchivoEjecucion,rutaAlgoritmo);

            }//for

            //obtener pareto global y pasarlo al resto de funciones
            this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Arranca a calular paretto general....");
            List<double []> paretoGeneral = this.paretera(global_solutionSet);
            this.guardarParetoEnArchvo(paretoGeneral,nombreArchiboParetoReferecncia,rutaAlgoritmo);
            this.nombreAlgoritmo_rutaTotalPareto.put(nombreAlgoritmo,rutaAlgoritmo+nombreArchiboParetoReferecncia);

            //generar script en R de hypervolumen
            this.construirScriptRhypervolume();
            this.construirScriptRGraficas();
            this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"FINAL TOTAL");
            boolean appendMode = false;
            Archivos ar = new Archivos();
            ar.writeLargerTextFile(rutaAlgoritmo+"datosejec.txt",this.logEjecuciones,appendMode);

            appendMode = false;
            ar = new Archivos();
            ar.writeLargerTextFile(rutaAlgoritmo+"tiemposEjecucion.txt",this.tiemposEjec,appendMode);

        }catch(Exception e){
            System.err.println(nombreAlgoritmo);
            e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
        }
    }

    private  void guardarParetoEnArchvo(List<double []> pareto,
                                        String nombreArchivo,
                                        String ruta){
        List<String> aLines = new ArrayList<>();
        for (double [] valorSoluco:pareto) {
            aLines.add(String.valueOf(valorSoluco[0])+separadorColumnasArchivo+String.valueOf(valorSoluco[1]));
        }

        boolean appendMode = false;
        Archivos ar = new Archivos();
        ar.writeLargerTextFile(ruta+nombreArchivo,aLines,appendMode);
    }

private  void correrGreedy(String ruta,
                           String instanciaUtilizada){
        List<String> aLines = new ArrayList<>();
        List<String> todasGreedy = new ArrayList<>();

        CdnHeuristica heuri = new CdnHeuristica(instanciaUtilizada);
        List<double []> solucionesGreedy = heuri.muchasGreedy();
        System.out.println("Arranca a calular paretto GRAFICA_GREEDY....");
        this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Arranca a calular paretto GRAFICA_GREEDY....");
        List<double []> elParettoGreedy = this.paretera( solucionesGreedy);


        for (double [] valorSoluco:elParettoGreedy) {
            aLines.add(String.valueOf(valorSoluco[0])+separadorColumnasArchivo+String.valueOf(valorSoluco[1]));
        }


        for (double [] valorSoluco:solucionesGreedy) {
            todasGreedy.add(String.valueOf(valorSoluco[0])+separadorColumnasArchivo+String.valueOf(valorSoluco[1]));
        }

        boolean appendMode = false;
        Archivos ar = new Archivos();

        ar.writeLargerTextFile(ruta+nombreArchivoGreedyPareto,aLines,appendMode);
        ar.writeLargerTextFile(ruta+nombreArchivoGreedyTodos,todasGreedy,appendMode);
    }

 private  void construirScriptRhypervolume(){

     List<String> aLines = new ArrayList<>();
    String nombreArchivoSalidaR="hypervolumenes.txt";
    String varParetoRef =nombreArchiboParetoReferecncia.replace(".","");
     aLines.add("setwd('"+rutaAlgoritmo+"')");
     aLines.add("library(emoa)");
     aLines.add(varParetoRef+" <- read.csv('"+nombreArchiboParetoReferecncia+"' , sep='"+separadorColumnasArchivo+"')");
     aLines.add("matrizzz = as.matrix("+varParetoRef+")");
     aLines.add("traspuesta<-t(matrizzz)");
     aLines.add("maximaX<-traspuesta[2,which.max(traspuesta[2,])]");
     aLines.add("maximaY<-traspuesta[1,which.max(traspuesta[1,])]");
     aLines.add("hypervolumenReferencia"+varParetoRef+"<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))");

     int count=0;
     for(String nombreArchi:listaNombreArchivosEjecucion){
         aLines.add("\n");
         aLines.add(nombreArchi.replace(".","")+" <- read.csv('"+nombreArchi+"' , sep='"+separadorColumnasArchivo+"')");
         aLines.add("matrizzz"+count+" = as.matrix("+nombreArchi.replace(".","")+")");
         aLines.add("traspuesta"+count+"<-t(matrizzz"+count+")");
         aLines.add("hypervolumen"+nombreArchi.replace(".","")+"<-dominated_hypervolume(traspuesta"+count+",c(maximaY,maximaX))");

         //calculando hypervolumen
         aLines.add("hypervolumenRelativo"+nombreArchi.replace(".","")+"<-hypervolumen"+nombreArchi.replace(".","")+"/hypervolumenReferencia"+varParetoRef);

         //guarda en archivo
         aLines.add("write(\"hypervolumenRelativo"+nombreArchi.replace(".","")+"\", file = '"+nombreArchivoSalidaR+"',ncolumns = 1,append = TRUE, sep ='"+separadorColumnasArchivo+"')");
         aLines.add("write(hypervolumenRelativo"+nombreArchi.replace(".","")+", file = '"+nombreArchivoSalidaR+"',ncolumns = 1,append = TRUE, sep ='"+separadorColumnasArchivo+"')");
         count++;

     }










     boolean appendMode = false;
     Archivos ar = new Archivos();
     ar.writeLargerTextFile(rutaAlgoritmo+nombreArchivoRhypervolumen,aLines,appendMode);









    }


    private  void acumularParetosSoluciones( List<CDNSolution> soluciones,
                                             int indiceparetochico){

        List<double []> paretitoParaGuardar= new ArrayList<>();
        for (CDNSolution solute : soluciones) {
            double costo = solute.getObjective(1);
            double Qos = solute.getObjective(0);
            double[] parejaCoso = new double[2];
            parejaCoso[0] = costo;
            parejaCoso[1] = Qos;

            //se guardan los maximos para ajustar los ejes de las graficas
            if(maximoCosto<costo){
                maximoCosto=costo;
            }
            if(maximoQoS<Qos){
                maximoQoS=Qos;
            }



            //guardo en el conjunto global de todas las soluciones
            //el que se usara para hacer el pareto de referencia
            global_solutionSet.add(parejaCoso);
            paretitoParaGuardar.add(parejaCoso);
        }
        //guardo cada pareto por separado
        paretosSolucionesSeparados.add(indiceparetochico,paretitoParaGuardar);

    }


private void construirScriptRGraficas(){
    try{
        List<String> aLines = new ArrayList<>();
        String varParetoRef =nombreArchiboParetoReferecncia.replace(".","");

        aLines.add("setwd('"+rutaAlgoritmo+"')");
        aLines.add(varParetoRef+" <- read.csv('"+nombreArchiboParetoReferecncia+"' , sep='"+separadorColumnasArchivo+"')");
        for(String nombreArchi:listaNombreArchivosEjecucion){
            aLines.add(nombreArchi.replace(".","")+" <- read.csv('"+nombreArchi+"' , sep='"+separadorColumnasArchivo+"')");
        }
        aLines.add("\n");



        aLines.add("plot("+varParetoRef+" ,xlab='Costo', ylab='RTT (ms)', pch=19,col='red',xlim=range(0,"+maximoCosto+"), ylim=range(0,"+maximoQoS+"))");
        String vectorTipoPuntoReferencias ="19";
        String vectorColorReferencias ="\"red\"";
        String vectorTextoReferencias ="\"FP Referencia\"";
        int count=0;
        for(String nombreArchi:listaNombreArchivosEjecucion){
            aLines.add("points("+nombreArchi.replace(".","")+",col=\"green\", pch=19)");
            vectorTipoPuntoReferencias+=",19";
            vectorColorReferencias+=",\"green\"";
            vectorTextoReferencias+=",\"FP "+count+"\"";

            count++;
        }
        aLines.add("\n");
        aLines.add("legend(");
        aLines.add("        \"topright\",");
        aLines.add("        pch=c("+vectorTipoPuntoReferencias+"),");
        aLines.add("        col=c("+vectorColorReferencias+"),");
        aLines.add("        legend = c("+vectorTextoReferencias+")");
        aLines.add(")");
        boolean appendMode = false;
        Archivos ar = new Archivos();
        ar.writeLargerTextFile(rutaAlgoritmo+nombreArchivoRGraficas,aLines,appendMode);


        //todo los paretos contra el ampl
        List<String> todosContraAMPL = new ArrayList<>();
        String varParetoAMPLRef =nombreArchiboParetoAMPL.replace(".","");

        todosContraAMPL.add(varParetoAMPLRef+" <- read.csv('"+rutaSalida+nombreArchiboParetoAMPL+"' , sep='"+separadorColumnasArchivo+"')");
        for (Map.Entry<String, String> entry : this.nombreAlgoritmo_rutaTotalPareto.entrySet()) {
            String nombreAlgoritmo = entry.getKey();
            String rutaArchivoAlgoritmo = entry.getValue();
            todosContraAMPL.add(nombreAlgoritmo+" <- read.csv('"+rutaArchivoAlgoritmo+"' , sep='"+separadorColumnasArchivo+"')");
        }
        todosContraAMPL.add("\n");
        //VER QUE ACA se queda con los costos maximos del ultimo algoritmo ejecutado
        todosContraAMPL.add("plot("+varParetoRef+" ,xlab='Costo', ylab='RTT (ms)', pch=19,col='red',xlim=range(0,"+maximoCosto+"), ylim=range(0,"+maximoQoS+"))");
         vectorTipoPuntoReferencias ="19";
         vectorColorReferencias ="\"red\"";
         vectorTextoReferencias ="\"AMPL\"";
         count=0;
        for (Map.Entry<String, String> entry : this.nombreAlgoritmo_rutaTotalPareto.entrySet()) {
            String nombreAlgoritmo = entry.getKey();

            todosContraAMPL.add("points("+nombreAlgoritmo+",col=\"green\", pch=19)");
            vectorTipoPuntoReferencias+=",19";
            vectorColorReferencias+=",\"green\"";
            vectorTextoReferencias+=",\"FP "+count+"\"";

            count++;
        }
        todosContraAMPL.add("\n");
        todosContraAMPL.add("legend(");
        todosContraAMPL.add("        \"topright\",");
        todosContraAMPL.add("        pch=c("+vectorTipoPuntoReferencias+"),");
        todosContraAMPL.add("        col=c("+vectorColorReferencias+"),");
        todosContraAMPL.add("        legend = c("+vectorTextoReferencias+")");
        todosContraAMPL.add(")");
        appendMode = false;

        ar.writeLargerTextFile(rutaSalida+nombreArchivoGRAFICAparetoreferenciaTODOSAlgoritmos_AMPL,todosContraAMPL,appendMode);










        //         nombreArchivoGRAFICAparetoreferenciaTODOSAlgoritmos_AMPL

        // PARETO REFERENCIA Y GREEDY
        //nombreArchivoGRAFICAparetoreferenciaAE_GREEDY


        //PARETO REFERENCIA AMPL  Y GREEDY
      //  nombreArchivoGRAFICAparetoreferenciaAE_GREEDY_AMPL

        //PARETO REFERENCIA AMPL  Y GREEDY
        //nombreArchivoGRAFICAparetoreferenciaAE_AMPL


    }catch(Exception e){
        e.printStackTrace();
        JMetalLogger.logger.severe(e.getMessage());
    }
}

private String darNombreAlgoritmo(
                                  String instanciaUtilizada,
                                  double genMutationProbability,
                                  double crossoverProbability,
                                  int populationSize,
                                  int maxEvaluations){
    String muta = "M"+String.valueOf(genMutationProbability).replace(".","");
    String cross = "C"+String.valueOf(crossoverProbability).replace(".","");
    String generacions = "G"+String.valueOf(maxEvaluations/populationSize);
    String tamaniopoblacion = "P"+String.valueOf(populationSize);
    this.logEjecuciones.add("Probabilidad mutacion: "+genMutationProbability);
    this.logEjecuciones.add("Probabilidad cruzamiento: "+crossoverProbability);
    this.logEjecuciones.add("Tamanio poblacion: "+populationSize);
    this.logEjecuciones.add("Cantidad de generaciones: "+maxEvaluations/populationSize);

    return instanciaUtilizada+muta+cross+generacions+tamaniopoblacion;
}



    private  List<CDNSolution> corredorProblem(
                                         String instanciaUtilizada,
                                         double genMutationProbability,
                                         double crossoverProbability,
                                         int populationSize,
                                         int maxEvaluations){
        double genMutationProbabilityAfterGreedy=0.0;
        int formaDeInit=1;
        int greedyDeterministic_level=1;
        MultiobjCDN problem;
        Algorithm<List<CDNSolution>> algorithm;
        CrossoverOperator<CDNSolution> crossover;
        MutationOperator<CDNSolution> mutation;
        SelectionOperator<List<CDNSolution>, CDNSolution> selection;
        try{
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
                .build();
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();
            this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Tiempo "+ algorithmRunner.getComputingTime());

            this.logEjecuciones.add("Inicio Inicializacion Greedy "+problem.getInstanciaDatos().marcaInicioInicializacionGreedy);
            this.logEjecuciones.add("Fin Inicializacion Greedy "+problem.getInstanciaDatos().marcaFinInicializacionGreedy);
            this.tiemposEjec.add(String.valueOf(algorithmRunner.getComputingTime()));

        return algorithm.getResult();
    }catch(Exception e){
        e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
    }
    return null;
    }





    private List<double []> paretera(List<double []> solutionSet){
        List<double []> population = solutionSet;
        List<double []> paretoDoublesolutionSet = new ArrayList<>();

        // dominateMe[i] contains the number of solutions dominating i
        int[] dominateMe = new int[population.size()];

        // iDominate[k] contains the list of solutions dominated by k
        List<List<Integer>> iDominate = new ArrayList<>(population.size());

        // front[i] contains the list of individuals belonging to the front i
        // ArrayList<List<Integer>> front = new ArrayList<>(population.size() + 1);

        // Initialize the fronts
//        for (int i = 0; i < population.size() + 1; i++) {
//            front.add(new LinkedList<Integer>());
//        }

        // Fast non dominated sorting algorithm
        // Contribution of Guillaume Jacquenot
        for (int p = 0; p < population.size(); p++) {
            // Initialize the list of individuals that i dominate and the number
            // of individuals that dominate me
            iDominate.add(new LinkedList<Integer>());
            dominateMe[p] = 0;
        }

        int flagDominate;
        for (int p = 0; p < (population.size() - 1); p++) {
            // For all q individuals , calculate if p dominates q or vice versa
            for (int q = p + 1; q < population.size(); q++) {

                flagDominate = this.dominanceTest(solutionSet.get(p), solutionSet.get(q));

                if (flagDominate == -1) {
                    iDominate.get(p).add(q);
                    dominateMe[q]++;
                } else if (flagDominate == 1) {
                    iDominate.get(q).add(p);
                    dominateMe[p]++;
                }
            }
        }

        //me quedo con los no dominados
        for (int i = 0; i < population.size(); i++) {
            if (dominateMe[i] == 0) {
                //  front.get(0).add(i);
                paretoDoublesolutionSet.add(solutionSet.get(i));

            }
        }

        System.out.println("Soluciones NO dominadas: "+paretoDoublesolutionSet.size()+" Soluciones dominadas: "+(solutionSet.size()-paretoDoublesolutionSet.size()));
        if(this.logEjecuciones!=null){
            this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Soluciones NO dominadas: "+paretoDoublesolutionSet.size());
            this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Soluciones dominadas: "+(solutionSet.size()-paretoDoublesolutionSet.size()));
        }
        return paretoDoublesolutionSet;

        //Obtain the rest of fronts
//        int i = 0;
//        Iterator<Integer> it1, it2; // Iterators
//        while (front.get(i).size() != 0) {
//            i++;
//            it1 = front.get(i - 1).iterator();
//            while (it1.hasNext()) {
//                it2 = iDominate.get(it1.next()).iterator();
//                while (it2.hasNext()) {
//                    int index = it2.next();
//                    dominateMe[index]--;
//                    if (dominateMe[index] == 0) {
//                        front.get(i).add(index);
//                        //RankingAndCrowdingAttr.getAttributes(solutionSet.get(index)).setRank(i);
//                        solutionSet.get(index).setAttribute(getAttributeID(), i);
//                    }
//                }
//            }
//        }
//
//        rankedSubPopulations = new ArrayList<>();
//        //0,1,2,....,i-1 are fronts, then i fronts
//        for (int j = 0; j < i; j++) {
//            rankedSubPopulations.add(j, new ArrayList<S>(front.get(j).size()));
//            it1 = front.get(j).iterator();
//            while (it1.hasNext()) {
//                rankedSubPopulations.get(j).add(solutionSet.get(it1.next()));
//            }
//        }
    }


    private int dominanceTest(double [] solution1, double [] solution2) {
        int result ;
        boolean solution1Dominates = false ;
        boolean solution2Dominates = false ;

        int flag;
        double value1, value2;
        for (int i = 0; i < 2 ; i++) {
            value1 = solution1[i];
            value2 = solution2[i];
            if (value1  < value2) {
                flag = -1;
                //} else if (value1 / (1 + epsilon) > value2) {
            } else if (value2  < value1) {
                flag = 1;
            } else {
                flag = 0;
            }

            if (flag == -1) {
                solution1Dominates = true ;
            }

            if (flag == 1) {
                solution2Dominates = true ;
            }
        }

        if (solution1Dominates == solution2Dominates) {
            // non-dominated solutions
            result = 0;
        } else if (solution1Dominates) {
            // solution1 dominates
            result = -1;
        } else {
            // solution2 dominates
            result = 1;
        }
        return result ;
    }


    public void leerPropertiesExperimento(String nombreArchivoPropertiesInstancia){

        Properties prop = new Properties();

        InputStream input = null;


       genMutationProbability=new ArrayList<>();
       crossoverProbability=new ArrayList<>();
       populationSize=new ArrayList<>();
       maxEvaluations=new ArrayList<>();
        cantidadIteraciones=new ArrayList<>();

        System.out.println("Cargando properties "+nombreArchivoPropertiesInstancia);
        String rutaResources = "/CDN_AE_NSGAII/src/resources" ;
        try {
            //instancia se carga desde carpeta properties con ruta relativa
            input = new FileInputStream(System.getProperty("user.dir")+rutaResources+"/propertiesdir/"+nombreArchivoPropertiesInstancia+".properties");

            // load a properties file
            prop.load(input);


            cantidadAlgoritmos=Integer.valueOf(prop.getProperty("cantidadAlgoritmos"));
            instanciaUtilizadaExperimento=prop.getProperty("instanciaUtilizada");

            for(int algoritmo=0;algoritmo<cantidadAlgoritmos;algoritmo++){
                genMutationProbability.add(algoritmo,Double.valueOf(prop.getProperty("genMutationProbability"+algoritmo)));
                crossoverProbability.add(algoritmo,Double.valueOf(prop.getProperty("crossoverProbability"+algoritmo)));
                populationSize.add(algoritmo,Integer.valueOf(prop.getProperty("populationSize"+algoritmo)));
                maxEvaluations.add(algoritmo,Integer.valueOf(prop.getProperty("maxEvaluations"+algoritmo)));
                cantidadIteraciones.add(algoritmo,Integer.valueOf(prop.getProperty("cantidadIteraciones"+algoritmo)));
            }



        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
