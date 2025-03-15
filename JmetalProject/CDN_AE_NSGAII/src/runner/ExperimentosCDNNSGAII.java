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
public class ExperimentosCDNNSGAII {

    public ExperimentosCDNNSGAII() {
    }

    List<List<double []>> paretosSolucionesSeparados = new ArrayList<>();
    String rutaSalida ="";
    String separadorColumnasArchivo =" ";
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
            ExperimentosCDNNSGAII eje = new ExperimentosCDNNSGAII();
           // JMetalLogger.configureLoggers(new File(System.getProperty("user.dir")+"/CDN_AE_NSGAII/src/resources/propertiesdir/"+"configLogger.properties"));
            String nombreArchivoPropertiesInstancia ="experimentoBase";
            int idinferiorAlgoritmoEjecutar =-1;
            int idsuperiorAlgoritmoEjecutar =-1;
            if(args.length!=0){
                nombreArchivoPropertiesInstancia =args[0];
                idinferiorAlgoritmoEjecutar =Integer.valueOf(args[1]);
                idsuperiorAlgoritmoEjecutar =Integer.valueOf(args[2]);
            }
            CdnInstancia insta = new CdnInstancia();
            String basedirExperimento=insta.determinarBasedir();
            eje.leerPropertiesExperimento(nombreArchivoPropertiesInstancia,basedirExperimento);
            eje.nombreAlgoritmo_rutaTotalPareto = new HashMap<>();



            insta.leerPropertiesInstancia(eje.instanciaUtilizadaExperimento);
            eje.rutaSalida=insta.getRutaAbsolutaSalidaInstancia();
            eje.descripcionInstancia= insta.descripcionInstancia;


            int idalgoritmoEjecutando=0;
            for(int cantIteraciones:eje.cantidadIteraciones){
                for(int cantGeneraciones:eje.generacioneslista){
                    for(int poblacion:eje.populationSize){
                        for(double Pcross:eje.crossoverProbability){
                            for(double Pmut:eje.genMutationProbability) {
                                int maxevaluations = poblacion * cantGeneraciones;

                                // se paraleliza ejecuciones con el ida algoritmo recibido por parametro
                                if (idinferiorAlgoritmoEjecutar == -1 ||
                                        (idalgoritmoEjecutando <= idsuperiorAlgoritmoEjecutar && idinferiorAlgoritmoEjecutar <= idalgoritmoEjecutando)){
                                    eje.logEjecuciones = new ArrayList<>();
                                    eje.tiemposEjec = new ArrayList<>();
                                    eje.correrAlgoritmo(eje.instanciaUtilizadaExperimento,
                                            cantIteraciones,
                                            Pmut,
                                            Pcross,
                                            poblacion,
                                            maxevaluations);
                            }
                                idalgoritmoEjecutando++;
                            }
                        }
                    }
                }
            }
            //solo genero una vez el greedy
            if((args.length==0) || ( args.length!=0 && idinferiorAlgoritmoEjecutar==0)) {
                eje.correrGreedy(eje.rutaSalida, eje.instanciaUtilizadaExperimento);
            }
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


        for (double [] valorSoluco:solucionesGreedy) {
            todasGreedy.add(String.valueOf(valorSoluco[0])+separadorColumnasArchivo+String.valueOf(valorSoluco[1]));
        }

        boolean appendMode = false;
        Archivos ar = new Archivos();


        ar.writeLargerTextFile(ruta+nombreArchivoGreedyTodos,todasGreedy,appendMode);
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

            paretitoParaGuardar.add(parejaCoso);
        }
        //guardo cada pareto por separado
        paretosSolucionesSeparados.add(indiceparetochico,paretitoParaGuardar);

    }
}
