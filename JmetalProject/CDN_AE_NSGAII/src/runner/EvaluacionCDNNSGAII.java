package runner;

import operadores.CDNCrossover;
import operadores.CDNmMutation;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import problema.MultiobjCDN;
import soluciones.CDNSolution;
import utils.Archivos;
import utils.CdnHeuristica;
import utils.CdnInstancia;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ggoni on 21/09/17.
 */
public class EvaluacionCDNNSGAII {

    private String[] crossoverProbability_split;

    public EvaluacionCDNNSGAII() {
    }

    List<double []> global_solutionSet = new ArrayList<>();
    String rutaSalida ="";
    String separadorColumnasArchivo =""+'\t';
    List<String> listaNombreArchivosEjecucion;
    String nombreAlgoritmo;
    String rutaAlgoritmo;


    String nombreArchivoGreedyTodos="TodosGreedy.pr";




    HashMap<String,String> nombreAlgoritmo_rutaTotalPareto;

    double maximoCosto =0;
    double maximoQoS =0;
    String descripcionInstancia;
    List<String> logEjecuciones;
    List<String> tiemposEjec;

    String instanciaUtilizadaExperimento;
    List<Double> genMutationProbability;
    List<Double> crossoverProbability;
    List<Integer> populationSize;
    List<Integer> generacioneslista;
    List<Integer> cantidadIteraciones;


    public static void main(String[] args) {

        try {
            /***
             * recibe por argumento los identificadores de los extremos de rangos de ejecuciones a ejecutar
            usar alguna de las chicas para comparar contra ampl







            *////
            EvaluacionCDNNSGAII eje = new EvaluacionCDNNSGAII();
            CdnInstancia insta = new CdnInstancia();
            String basedirExperimento=insta.determinarBasedir();
            JMetalLogger.configureLoggers(new File(basedirExperimento+"/propertiesdir/"+"configLogger.properties"));


           // String nombreArchivoPropertiesInstancia ="evaluacionBase";
            String nombreArchivoPropertiesInstancia ="AMPL1";


            int idinferiorAlgoritmoEjecutar =-1;
            int idsuperiorAlgoritmoEjecutar =-1;
            if(args.length!=0){
                nombreArchivoPropertiesInstancia =args[0];
                idinferiorAlgoritmoEjecutar =Integer.valueOf(args[1]);
                idsuperiorAlgoritmoEjecutar =Integer.valueOf(args[2]);
            }
System.out.println("nombreArchivoPropertiesInstancia "+nombreArchivoPropertiesInstancia);
System.out.println("idinferiorAlgoritmoEjecutar "+idinferiorAlgoritmoEjecutar);
System.out.println("idsuperiorAlgoritmoEjecutar "+idsuperiorAlgoritmoEjecutar);

            eje.leerPropertiesExperimento(nombreArchivoPropertiesInstancia,basedirExperimento);
            eje.nombreAlgoritmo_rutaTotalPareto = new HashMap<>();



            insta.leerPropertiesInstancia(eje.instanciaUtilizadaExperimento);
            eje.rutaSalida=insta.getRutaAbsolutaSalidaInstancia();
            eje.descripcionInstancia= insta.descripcionInstancia;


            int idalgoritmoEjecutando=0;

                for(int cantGeneraciones:eje.generacioneslista){
                    for(int poblacion:eje.populationSize){
                        for(double Pcross:eje.crossoverProbability){
                            for(double Pmut:eje.genMutationProbability) {
                                int maxevaluations = poblacion * cantGeneraciones;

                                // se paraleliza ejecuciones con el ida algoritmo recibido por parametro


                                    eje.correrAlgoritmo(eje.instanciaUtilizadaExperimento,
                                            idinferiorAlgoritmoEjecutar,
                                            idsuperiorAlgoritmoEjecutar,
                                            Pmut,
                                            Pcross,
                                            poblacion,
                                            maxevaluations);

                                idalgoritmoEjecutando++;
                            }
                        }
                    }

            }
            //solo genero una vez el greedy
            if((args.length==0) || ( args.length!=0 && idinferiorAlgoritmoEjecutar==0)) {
                eje.correrGreedy(eje.rutaSalida, eje.instanciaUtilizadaExperimento);
                //Todo correr el round robin
            }
        }catch(Exception e){
            e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
        }

    }


    private void correrAlgoritmo(String instanciaUtilizada,
                                 int idinferiorAlgoritmoEjecutar,
                                 int idsuperiorAlgoritmoEjecutar,
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


        rutaAlgoritmo = rutaSalida+nombreAlgoritmo+"/";


        File directory = new File(rutaSalida);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+rutaSalida);

        }
        directory = new File(rutaAlgoritmo);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+rutaAlgoritmo);

        }

        //crear carpeta con nombre nombreAlgoritmo
        try {

            listaNombreArchivosEjecucion = new ArrayList<>();
            if(idinferiorAlgoritmoEjecutar==-1 || idsuperiorAlgoritmoEjecutar==-1){
                idinferiorAlgoritmoEjecutar=0;
                idsuperiorAlgoritmoEjecutar=1;
            }


            for(int iteracion =idinferiorAlgoritmoEjecutar;iteracion<=idsuperiorAlgoritmoEjecutar;iteracion++) {
                String rutaCarpetaEjecucion = rutaAlgoritmo+nombreAlgoritmo+"I"+iteracion+"/";
                this.logEjecuciones = new ArrayList<>();
                this.tiemposEjec = new ArrayList<>();
                this.logEjecuciones.add(descripcionInstancia);
                this.logEjecuciones.add(nombreAlgoritmo);
                this.logEjecuciones.add(nombreAlgoritmo+"I"+iteracion);
                directory = new File(rutaCarpetaEjecucion);
                if (! directory.exists()){
                    directory.mkdir();
                    System.out.println("Creando directorio "+rutaCarpetaEjecucion);
                    this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Creando directorio "+rutaCarpetaEjecucion);
                }

                String nombreParetoEjecucion = "pareto.pr";

                this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Inicio "+rutaCarpetaEjecucion+"/"+nombreParetoEjecucion);

                List<CDNSolution> soluciones = this.corredorProblem(instanciaUtilizada,
                                                                    genMutationProbability,
                                                                    crossoverProbability,
                                                                    populationSize,
                                                                    maxEvaluations);
                int count=0;
                ArrayList<String> tiemposZ = new ArrayList();//tambien guardo tiempo en construir z
                ArrayList<String> tiemposCross = new ArrayList();//tambien guardo tiempo en cruzamiento
                ArrayList<String> tiemposMut = new ArrayList();//tambien guardo tiempo en mutacion
                for(CDNSolution soluc:soluciones){
                    soluc.toFileEvaluacion(rutaCarpetaEjecucion,"solucion"+String.valueOf(count)+".sol");
                    count++;
                    tiemposZ.add(String.valueOf(soluc.getTiempoConstruirZ()));
                    tiemposCross.add(String.valueOf(soluc.getVariableValue(0).getTiempoCross()));
                    tiemposMut.add(String.valueOf(soluc.getVariableValue(0).getTiempoMut()));
                }
                this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Cant soluciones "+soluciones.size());
                this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Fin "+rutaCarpetaEjecucion+"/"+nombreParetoEjecucion);
                this.guardarSoluciones(soluciones,nombreParetoEjecucion,rutaCarpetaEjecucion);

                this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"FINAL TOTAL");
                boolean appendMode = false;
                Archivos ar = new Archivos();
                ar.writeLargerTextFile(rutaCarpetaEjecucion+"datosejec.txt",this.logEjecuciones,appendMode);

                appendMode = false;
                ar = new Archivos();
                ar.writeLargerTextFile(rutaCarpetaEjecucion+"tiemposEjecucion.txt",this.tiemposEjec,appendMode);


                appendMode = false;
                ar = new Archivos();
                ar.writeLargerTextFile(rutaCarpetaEjecucion+"tiemposEjecucionArmarZ.txt",tiemposZ,appendMode);

                appendMode = false;
                ar = new Archivos();
                ar.writeLargerTextFile(rutaCarpetaEjecucion+"tiemposEjecucionMutacion.txt",tiemposMut,appendMode);

                appendMode = false;
                ar = new Archivos();
                ar.writeLargerTextFile(rutaCarpetaEjecucion+"tiemposEjecucionCruzamiento.txt",tiemposCross,appendMode);


            }//for






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


    private  void guardarSoluciones(List<CDNSolution> soluciones,
                                        String nombreArchivo,
                                        String ruta){
        List<String> aLines = new ArrayList<>();
        for (CDNSolution valorSoluco:soluciones) {
            aLines.add(String.valueOf(valorSoluco.getObjective(1))+separadorColumnasArchivo+String.valueOf(valorSoluco.getObjective(0)));
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
       // this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Arranca a calular paretto GRAFICA_GREEDY....");


        for (double [] valorSoluco:solucionesGreedy) {
            todasGreedy.add(String.valueOf(valorSoluco[0])+separadorColumnasArchivo+String.valueOf(valorSoluco[1]));
        }

        boolean appendMode = false;
        Archivos ar = new Archivos();


        ar.writeLargerTextFile(ruta+nombreArchivoGreedyTodos,todasGreedy,appendMode);
       System.out.println("FIN calular paretto GRAFICA_GREEDY....");
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
//    this.logEjecuciones.add("Probabilidad mutacion: "+genMutationProbability);
//    this.logEjecuciones.add("Probabilidad cruzamiento: "+crossoverProbability);
//    this.logEjecuciones.add("Tamanio poblacion: "+populationSize);
//    this.logEjecuciones.add("Cantidad de generaciones: "+maxEvaluations/populationSize);

    return "eval_"+instanciaUtilizada+muta+cross+generacions+tamaniopoblacion;
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




    public void leerPropertiesExperimento(String nombreArchivoPropertiesInstancia,String basedirExperimento){

        Properties prop = new Properties();

        InputStream input = null;


       genMutationProbability=new ArrayList<>();
       crossoverProbability=new ArrayList<>();
       populationSize=new ArrayList<>();
        generacioneslista=new ArrayList<>();
        cantidadIteraciones=new ArrayList<>();

        System.out.println("Cargando properties "+nombreArchivoPropertiesInstancia);

        try {


            //instancia se carga desde carpeta properties con ruta relativa
            input = new FileInputStream(basedirExperimento+"/propertiesdir/"+nombreArchivoPropertiesInstancia+".properties");

            // load a properties file
            prop.load(input);



            instanciaUtilizadaExperimento           = prop.getProperty("instanciaUtilizada");
            String [] crossoverProbability_split    = prop.getProperty("crossoverProbability").split(",");
            String [] genMutationProbability_split  = prop.getProperty("genMutationProbability").split(",");
            String [] populationSize_split          = prop.getProperty("populationSize").split(",");
            String [] generaciones_split            = prop.getProperty("generaciones").split(",");
            String [] cantidadIteraciones_split     = prop.getProperty("cantidadIteraciones").split(",");


            for(String Pcross:crossoverProbability_split){
                crossoverProbability.add(Double.valueOf(Pcross));
            }

            for(String Pmut:genMutationProbability_split){
                genMutationProbability.add(Double.valueOf(Pmut));
            }

            for(String Popu:populationSize_split){
                int poblacion=Integer.valueOf(Popu);
                populationSize.add(poblacion);

            }

            for(String generaciones:generaciones_split){
                generacioneslista.add(Integer.valueOf(generaciones));
            }

             for(String iters:cantidadIteraciones_split){
                 cantidadIteraciones.add(Integer.valueOf(iters));
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
