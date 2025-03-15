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
public class ParserEvaluacionCDNNSGAII {

    public ParserEvaluacionCDNNSGAII() {
    }

    List<double []> paretosSolucionesSeparados = new ArrayList<>();
    String rutaSalida ="";
    String separadorColumnasArchivo =""+'\t';
    List<String> listaNombreArchivosEjecucion;
    String nombreAlgoritmo;
    String rutaAlgoritmo;

    //para guardar soluciones*****************
    List<double []> paretoGeneral;
    List<double[]> muchas_Soluciones_Greedy;
    List<double[]> muchas_Soluciones_QoS;
    List<double[]> muchas_Soluciones_RoundRobin;
    List<double[]> todasSolucionesJuntas;
    //*****************************************

            String nombreArchivoGreedyTodos="TodosGreedy.pr";
    public String rutaGreedy=null;
    public String rutaGreedyQoS=null;
    public String rutaRobin=null;
    public String nombreArchivoRobin=null;
    public String nombreArchivoQoS=null;
    public String nombreArchivoCosto=null;



    HashMap<String,String> nombreAlgoritmo_rutaTotalPareto;

    double maximoCosto =0;
    double maximoQoS =0;
    String descripcionInstancia;
    List<String> logEjecuciones;
    List<String> tiemposEjec=new ArrayList<>();

    String instanciaUtilizadaExperimento;
    List<Double> genMutationProbability;
    List<Double> crossoverProbability;
    List<Integer> populationSize;
    List<Integer> generacioneslista;
    List<Integer> cantidadIteraciones;


    String scriptR_cargadoVariables;
    String scriptR_nombreVariableMuchas_Soluciones_Greedy="MuchasSolucionesGreedy";
    String scriptR_nombreVariableMuchas_Soluciones_QoS="MuchasSolucionesQoS";
    String scriptR_nombreVariableMuchas_Soluciones_RoundRobin="MuchasSolucionesRoundRobin";
    String scriptR_nombreVariableparetoGeneral="ParetoGeneral";



    public static void main(String[] args) {

        /*


If you want to know all the shortcut in intellij hit "Ctrl + J

         */
        List<String> instancias= new ArrayList<>();

      //  instancias.add("eval_G2");


          instancias.add("eval_CH1");
      instancias.add("eval_CH2");
        instancias.add("eval_M1");
        instancias.add("eval_M2");
        instancias.add("eval_G1");
        instancias.add("eval_G2");

        instancias.add("eval_AMPL1");
        instancias.add("eval_AMPL2");
        instancias.add("eval_AMPL3");
        instancias.add("eval_AMPL4");
    for(String instanciaActual:instancias){

        try {
            int idinferiorAlgoritmoEjecutar = 0;
            int idsuperiorAlgoritmoEjecutar = 49;
            //int idsuperiorAlgoritmoEjecutar = 1;
            // String nombreArchivoPropertiesInstancia ="evaluacionBase";
           // String nombreArchivoPropertiesInstancia = "eval_tandaD5_80000Vid_180Mins_7Prove_5XreG_6XreQ";
            String nombreArchivoPropertiesInstancia = instanciaActual;
            //  String nombreArchivoPropertiesInstancia ="eval_tandaD3_16000Vid_240Mins_6Prove_6XreG_6XreQ";

            ParserEvaluacionCDNNSGAII eje = new ParserEvaluacionCDNNSGAII();
            CdnInstancia insta = new CdnInstancia();
            String basedirExperimento = insta.determinarBasedir();
            JMetalLogger.configureLoggers(new File(basedirExperimento + "/propertiesdir/" + "configLogger.properties"));


            System.out.println("nombreArchivoPropertiesInstancia " + nombreArchivoPropertiesInstancia);
            System.out.println("idinferiorAlgoritmoEjecutar " + idinferiorAlgoritmoEjecutar);
            System.out.println("idsuperiorAlgoritmoEjecutar " + idsuperiorAlgoritmoEjecutar);

            eje.leerPropertiesExperimento(nombreArchivoPropertiesInstancia, basedirExperimento);
            eje.nombreAlgoritmo_rutaTotalPareto = new HashMap<>();


            insta.leerPropertiesInstancia(eje.instanciaUtilizadaExperimento);
            eje.rutaSalida = insta.getRutaAbsolutaSalidaInstancia();
            eje.descripcionInstancia = insta.descripcionInstancia;


            //levantar rutas de lo greedys
            CdnHeuristica heuri = new CdnHeuristica(eje.instanciaUtilizadaExperimento);
            heuri.setRutasHeuristicas();
            eje.rutaGreedy = heuri.rutaGreedy;
            eje.rutaGreedyQoS = heuri.rutaGreedyQoS;
            eje.rutaRobin = heuri.rutaRobin;
            eje.nombreArchivoRobin = heuri.nombreArchivoRobin;
            eje.nombreArchivoQoS = heuri.nombreArchivoQoS;
            eje.nombreArchivoCosto = heuri.nombreArchivoCosto;


            int idalgoritmoEjecutando = 0;

            for (int cantGeneraciones : eje.generacioneslista) {
                for (int poblacion : eje.populationSize) {
                    for (double Pcross : eje.crossoverProbability) {
                        for (double Pmut : eje.genMutationProbability) {
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
            if ((args.length == 0) || (args.length != 0 && idinferiorAlgoritmoEjecutar == 0)) {
                System.out.println(eje.rutaGreedy + eje.nombreArchivoCosto);
                System.out.println(eje.rutaGreedyQoS + eje.nombreArchivoQoS);
                System.out.println(eje.rutaRobin + eje.nombreArchivoRobin);

                /*******/
                //en este punto estan en memoria los greedys, el round robin y el pareto general del algoritmo
                //paretoGeneral
                eje.muchas_Soluciones_Greedy = heuri.muchasGreedy();
                eje.muchas_Soluciones_QoS = heuri.muchasQoS();
                eje.muchas_Soluciones_RoundRobin = heuri.muchasRoundRobin();

                //guardo todas las solucs juntas
                eje.todasSolucionesJuntas = new ArrayList<>();
                eje.todasSolucionesJuntas.addAll(eje.paretoGeneral);
                eje.todasSolucionesJuntas.addAll(eje.muchas_Soluciones_Greedy);
                eje.todasSolucionesJuntas.addAll(eje.muchas_Soluciones_QoS);
                eje.todasSolucionesJuntas.addAll(eje.muchas_Soluciones_RoundRobin);
                //calculo el maximo valor en el eje x y en el eje y
                double maximoCosto = 0;
                double maximoQos = 0;
                for (double[] soluc : eje.todasSolucionesJuntas) {
                    if (soluc[0] > maximoCosto) {
                        maximoCosto = soluc[0];
                    }
                    if (soluc[1] > maximoQos) {
                        maximoQos = soluc[1];
                    }
                }



                /***genero scritp R ***/
                List<String> scriptGraficaTodosJuntos = new ArrayList<>();
                //se guardan los archivos consumidos por el script R
                heuri.guardarHeuristica(heuri.rutaGreedy,heuri.nombreArchivoCosto,eje.muchas_Soluciones_Greedy);
                heuri.guardarHeuristica(heuri.rutaGreedyQoS,heuri.nombreArchivoQoS,eje.muchas_Soluciones_QoS);
                heuri.guardarHeuristica(heuri.rutaRobin,heuri.nombreArchivoRobin,eje.muchas_Soluciones_RoundRobin);

                //cargado de variables dese las respectivas rutas
                eje.scriptR_cargadoVariables = eje.scriptR_nombreVariableMuchas_Soluciones_Greedy + " <- read.csv('" + eje.rutaGreedy + eje.nombreArchivoCosto + "' , sep='" + eje.separadorColumnasArchivo + "',header = FALSE)" + "\n";
                eje.scriptR_cargadoVariables += eje.scriptR_nombreVariableMuchas_Soluciones_QoS + " <- read.csv('" + eje.rutaGreedyQoS + eje.nombreArchivoQoS + "' , sep='" + eje.separadorColumnasArchivo + "',header = FALSE)" + "\n";
                eje.scriptR_cargadoVariables += eje.scriptR_nombreVariableMuchas_Soluciones_RoundRobin + " <- read.csv('" + eje.rutaRobin + eje.nombreArchivoRobin + "' , sep='" + eje.separadorColumnasArchivo + "',header = FALSE)" + "\n";
                eje.scriptR_cargadoVariables += eje.scriptR_nombreVariableparetoGeneral + " <- read.csv('" + eje.rutaAlgoritmo + "paretoGeneral.txt" + "' , sep='" + eje.separadorColumnasArchivo + "',header = FALSE)" + "\n";
                eje.scriptR_cargadoVariables += "\n";

                //generacion script graficas todos juntos
                scriptGraficaTodosJuntos.add(eje.scriptR_cargadoVariables);
                System.out.println(eje.scriptR_cargadoVariables);



                //le doy un margen del 20% al mayor valor para que no quede en el limite de la grafica
                double porcentajeMargen = 0.20;
                maximoCosto = maximoCosto + (maximoCosto * porcentajeMargen);
                maximoQos = maximoQos + (maximoQos * porcentajeMargen);

                scriptGraficaTodosJuntos.add("plot(" + eje.scriptR_nombreVariableMuchas_Soluciones_Greedy + " ,xlab=\"Costo\", ylab=\"RTT (ms)\", pch=19,col=\"red\",xlim=range(0," + maximoCosto + "), ylim=range(0," + maximoQos + "))");
                scriptGraficaTodosJuntos.add("points(" + eje.scriptR_nombreVariableMuchas_Soluciones_RoundRobin + ",col=\"blue\", pch=19)");
                scriptGraficaTodosJuntos.add("points(" + eje.scriptR_nombreVariableMuchas_Soluciones_QoS + ",col=\"green\", pch=19)");
                scriptGraficaTodosJuntos.add("points(" + eje.scriptR_nombreVariableparetoGeneral + ",col=\"grey\", pch=19)");
                scriptGraficaTodosJuntos.add("legend(\n" +
                        "  \"topright\", \n" +
                        "  pch=c(19,19,19,19), \n" +
                        "  col=c(\"red\", \"green\", \"blue\", \"grey\"), \n" +
                        "  legend = c(\"Greedy costo\", \"Greedy Qos\", \"Round robin\", \"AE\")\n" +
                        ")");

                boolean appendMode = false;
                Archivos ar = new Archivos();
                ar.writeLargerTextFile(eje.rutaAlgoritmo + "graficasTodosJuntos.r", scriptGraficaTodosJuntos, appendMode);
                System.out.println(eje.rutaAlgoritmo + "/graficasTodosJuntos.r");


                /******************/
/******************/
/******************/
/******************/
//script calculador de normas
                List<String> normasPuntos = new ArrayList<>();
                normasPuntos.add(eje.calculadorNormaToString("AE",eje.paretoGeneral,eje.separadorColumnasArchivo));
                normasPuntos.add(eje.calculadorNormaToString("Greddy Costo",eje.muchas_Soluciones_Greedy,eje.separadorColumnasArchivo));
                normasPuntos.add(eje.calculadorNormaToString("Greddy QoS",eje.muchas_Soluciones_QoS,eje.separadorColumnasArchivo));
                normasPuntos.add(eje.calculadorNormaToString("Round Robin",eje.muchas_Soluciones_RoundRobin,eje.separadorColumnasArchivo));
                normasPuntos.add("NORMALIZADOS");

                //calculo del maximo y el minimo global para la normalizacion
                double costoMin= Double.MAX_VALUE;
                double QoSMin= Double.MAX_VALUE;
                double costoMax= 0;
                double QoSMax= 0;
                //busca los maximos y minimos
                for(double [] soluciones: eje.todasSolucionesJuntas){
                    double costo = soluciones[0];
                    double QoS = soluciones[1];
                    if(costoMax<costo){
                        costoMax=costo;
                    }
                    if(QoSMax<QoS){
                        QoSMax=QoS;
                    }
                    if(costoMin>costo){
                        costoMin=costo;
                    }
                    if(QoSMin>QoS){
                        QoSMin=QoS;
                    }
                }

                boolean appendMode_are = false;
                Archivos are = new Archivos();
                ArrayList<String> listaNormasDummy = new ArrayList<>();
                listaNormasDummy.add("Costo"+eje.separadorColumnasArchivo+"QoS"+eje.separadorColumnasArchivo+"Norma");
                are.writeLargerTextFile(eje.rutaAlgoritmo + "normasSoluciones.csv", listaNormasDummy, appendMode_are);
                normasPuntos.add(eje.calculadorNormaToStringNORMALIZADO(costoMin,QoSMin, costoMax, QoSMax,"AE",eje.paretoGeneral,eje.separadorColumnasArchivo));
                normasPuntos.add(eje.calculadorNormaToStringNORMALIZADO(costoMin,QoSMin, costoMax, QoSMax,"GreddyCosto",eje.muchas_Soluciones_Greedy,eje.separadorColumnasArchivo));
                normasPuntos.add(eje.calculadorNormaToStringNORMALIZADO(costoMin,QoSMin, costoMax, QoSMax,"GreddyQoS",eje.muchas_Soluciones_QoS,eje.separadorColumnasArchivo));
                normasPuntos.add(eje.calculadorNormaToStringNORMALIZADO(costoMin,QoSMin, costoMax, QoSMax,"RoundRobin",eje.muchas_Soluciones_RoundRobin,eje.separadorColumnasArchivo));


                ar = new Archivos();
                ar.writeLargerTextFile(eje.rutaAlgoritmo + "normasPuntos.csv", normasPuntos, appendMode);






                eje.generarArchivoDataGrafica(eje.paretoGeneral,
                                              eje.muchas_Soluciones_Greedy,
                                              eje.muchas_Soluciones_QoS,
                                              eje.muchas_Soluciones_RoundRobin,
                                              eje.rutaAlgoritmo,
                        costoMin,costoMax,QoSMin,QoSMax);

eje.generarScriptsRparaHipervolumenRelativo(instanciaActual,
                        costoMin,costoMax,QoSMin,QoSMax);

            }
        } catch (Exception e) {
            e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
        }

    }//for

    }



    private  void generarScriptsRparaHipervolumenRelativo(
                                            String instanciaActual,double costoMin,double costoMax,double QoSMin,double QoSMax) {


        String[] rutaSAlidaAltoNivel = rutaSalida.split("salida");
        String salidaGeneral = rutaSAlidaAltoNivel[0] + "salida/scriptHipervolumenEvalExpe/";
        List<String> archivo1 = new ArrayList<>();


        String rutaGreedy= this.rutaGreedy;
        String rutaGreedyQoS= this.rutaGreedyQoS;
        String rutaRobin= this.rutaRobin;


        String nombreArchivoGreedy=this.nombreArchivoCosto;
        String nombreArchivoQoS=this.nombreArchivoQoS;
        String nombreArchivoRoundRobin=this.nombreArchivoRobin;


        boolean usarParetos=true;
        if(usarParetos){
             rutaGreedy= salidaGeneral+"paretos/";
             rutaGreedyQoS= salidaGeneral+"paretos/";
             rutaRobin= salidaGeneral+"paretos/";
             nombreArchivoGreedy=instanciaActual+"_"+this.scriptR_nombreVariableMuchas_Soluciones_Greedy.replace(".", "")+"_pareto.txt";
             nombreArchivoQoS=instanciaActual+"_"+this.scriptR_nombreVariableMuchas_Soluciones_QoS.replace(".", "")+"_pareto.txt";
             nombreArchivoRoundRobin=instanciaActual+"_"+this.scriptR_nombreVariableMuchas_Soluciones_RoundRobin.replace(".", "")+"_pareto.txt";

            //transforma en pareto y guardarlo
            this.guardarParetoEnArchvo( paretera(this.muchas_Soluciones_Greedy),nombreArchivoGreedy,rutaGreedy);
            this.guardarParetoEnArchvo( paretera(this.muchas_Soluciones_QoS),nombreArchivoQoS,rutaGreedyQoS);
            this.guardarParetoEnArchvo( paretera(this.muchas_Soluciones_RoundRobin),nombreArchivoRoundRobin,rutaRobin);
        }




        //cargado de variables dese las respectivas rutas
        this.scriptR_cargadoVariables = this.scriptR_nombreVariableMuchas_Soluciones_Greedy + " <- read.csv('" + rutaGreedy + nombreArchivoGreedy + "' , sep='" + this.separadorColumnasArchivo + "',header = FALSE)" + "\n";
        this.scriptR_cargadoVariables += this.scriptR_nombreVariableMuchas_Soluciones_QoS + " <- read.csv('" + rutaGreedyQoS + nombreArchivoQoS + "' , sep='" + this.separadorColumnasArchivo + "',header = FALSE)" + "\n";
        this.scriptR_cargadoVariables += this.scriptR_nombreVariableMuchas_Soluciones_RoundRobin + " <- read.csv('" + rutaRobin + nombreArchivoRoundRobin + "' , sep='" + this.separadorColumnasArchivo + "',header = FALSE)" + "\n";
        this.scriptR_cargadoVariables += this.scriptR_nombreVariableparetoGeneral.replace(".", "") + " <- read.csv('" + this.rutaAlgoritmo + "paretoGeneral.txt" + "' , sep='" + this.separadorColumnasArchivo + "',header = FALSE)" + "\n";
        this.scriptR_cargadoVariables += "\n";






        archivo1.add("#correr este script para distintas instancias y acumula los valores en los archivos correspondientes en la carpteta " + salidaGeneral + "hypervolume/");
        archivo1.add("library(emoa)");
        archivo1.add(this.scriptR_cargadoVariables);
        //archivo1.add(NOMBRE_PARETO_REFERENCIA_GRANDE.replace(".", "") + " <- read.csv('" + NOMBRE_RUTA_PARETO_REFERENCIA_GRANDE + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
        archivo1.add("matrizzz = as.matrix(" + this.scriptR_nombreVariableparetoGeneral.replace(".", "") + ")");
        archivo1.add("traspuesta<-t(matrizzz)");



        //le doy un margen del 20% para la eleccion del punto para calcular el hipervolumen
        double porcentajeMargen = 0.20;
        costoMax = costoMax + (costoMax * porcentajeMargen);
        QoSMax = QoSMax + (QoSMax * porcentajeMargen);

        archivo1.add("maximaX<-"+QoSMax);
        archivo1.add("maximaY<-"+costoMax);


        archivo1.add("hypervolumenReferencia<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))");

        archivo1.add("\n");
        String nombreEjecucion=this.scriptR_nombreVariableMuchas_Soluciones_Greedy.replace(".", "");

        archivo1.add("matrizzz = as.matrix(" + nombreEjecucion + ")");
        archivo1.add("traspuesta<-t(matrizzz)");
        archivo1.add("hypervolumen" + nombreEjecucion + "<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))");

        //calculando hypervolumen
        archivo1.add("hypervolumenRelativo" + nombreEjecucion + "<-hypervolumen" + nombreEjecucion + "/hypervolumenReferencia");

        //guarda en archivo
        archivo1.add("escribir <- paste(c(\"" + nombreEjecucion + "\", hypervolumenRelativo" + nombreEjecucion + "), collapse = '" + separadorColumnasArchivo + "')");
        archivo1.add("write(escribir, file = '"+ salidaGeneral +instanciaActual + "_hypervolumenRelativoEEVALEXPE.csv"+ "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");

        archivo1.add("escribir <- paste(c(\"" + nombreEjecucion+ "_"+instanciaActual  + "\", hypervolumenRelativo" + nombreEjecucion + "), collapse = '" + separadorColumnasArchivo + "')");
        archivo1.add("write(escribir, file = '"+salidaGeneral + "hypervolumenRelativoEEVALEXPE_todos_GreedyCosto.csv"+ "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");


        archivo1.add("\n");
         nombreEjecucion=this.scriptR_nombreVariableMuchas_Soluciones_QoS.replace(".", "");

        archivo1.add("matrizzz = as.matrix(" + nombreEjecucion + ")");
        archivo1.add("traspuesta<-t(matrizzz)");
        archivo1.add("hypervolumen" + nombreEjecucion + "<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))");

        //calculando hypervolumen
        archivo1.add("hypervolumenRelativo" + nombreEjecucion + "<-hypervolumen" + nombreEjecucion + "/hypervolumenReferencia");

        //guarda en archivo
        archivo1.add("escribir <- paste(c(\"" + nombreEjecucion + "\", hypervolumenRelativo" + nombreEjecucion + "), collapse = '" + separadorColumnasArchivo + "')");
        archivo1.add("write(escribir, file = '"+salidaGeneral + instanciaActual + "_hypervolumenRelativoEEVALEXPE.csv"+ "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");


        archivo1.add("escribir <- paste(c(\"" + nombreEjecucion+ "_"+ instanciaActual  + "\", hypervolumenRelativo" + nombreEjecucion + "), collapse = '" + separadorColumnasArchivo + "')");
        archivo1.add("write(escribir, file = '"+salidaGeneral + "hypervolumenRelativoEEVALEXPE_todos_QoS.csv"+ "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");


        archivo1.add("\n");
        nombreEjecucion=this.scriptR_nombreVariableMuchas_Soluciones_RoundRobin.replace(".", "");

        archivo1.add("matrizzz = as.matrix(" + nombreEjecucion + ")");
        archivo1.add("traspuesta<-t(matrizzz)");
        archivo1.add("hypervolumen" + nombreEjecucion + "<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))");

        //calculando hypervolumen
        archivo1.add("hypervolumenRelativo" + nombreEjecucion + "<-hypervolumen" + nombreEjecucion + "/hypervolumenReferencia");

        //guarda en archivo
        archivo1.add("escribir <- paste(c(\"" + nombreEjecucion + "\", hypervolumenRelativo" + nombreEjecucion + "), collapse = '" + separadorColumnasArchivo + "')");
        archivo1.add("write(escribir, file = '"+salidaGeneral + instanciaActual + "_hypervolumenRelativoEEVALEXPE.csv"+ "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");


        archivo1.add("escribir <- paste(c(\"" + nombreEjecucion+ "_"+instanciaActual  + "\", hypervolumenRelativo" + nombreEjecucion + "), collapse = '" + separadorColumnasArchivo + "')");
        archivo1.add("write(escribir, file = '"+salidaGeneral + "hypervolumenRelativoEEVALEXPE_todos_robin.csv"+ "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");




        archivo1.add("\n");
        boolean appendMode = false;
        Archivos ar = new Archivos();
        ar.writeLargerTextFile(salidaGeneral + "hypervolume_TODOS_ContraReferenciaEVALEXPE_"+instanciaActual+".r", archivo1, appendMode);


        System.out.println(salidaGeneral + "hypervolume_TODOS_ContraReferenciaEVALEXPE_"+instanciaActual+".r");



         appendMode = true;
         ar = new Archivos();
         archivo1 = new ArrayList<>();
        archivo1.add("Rscript "+salidaGeneral + "hypervolume_TODOS_ContraReferenciaEVALEXPE_"+instanciaActual+".r");

        ar.writeLargerTextFile(salidaGeneral + "correrTodosLosR.sh", archivo1, appendMode);





//
//
//        //pasar los maximos y minimos globales por parametros
//        //arrancar por el AE
//        //guardar coordenadas de maximo y minimo de qos y de costo son 4
//        //guardar coordenadas de maximo y minimo de qos y de costo
//        ArrayList<String> coordenadasMaximoMinimo = new ArrayList<>();
//        coordenadasMaximoMinimo.addAll(this.listaMinimosYmaximos(paretoAe,"AE"));
//        coordenadasMaximoMinimo.addAll(this.listaMinimosYmaximos(paretoCosto,"GCosto"));
//        coordenadasMaximoMinimo.addAll(this.listaMinimosYmaximos(paretoQoS,"GQoS"));
//        coordenadasMaximoMinimo.addAll(this.listaMinimosYmaximos(paretoRR,"RR"));
//        coordenadasMaximoMinimo.add("-------------");
//        coordenadasMaximoMinimo.add("GLOBAL");
//        coordenadasMaximoMinimo.add("costoMin"+separadorColumnasArchivo+costoMin);
//        coordenadasMaximoMinimo.add("costoMax"+separadorColumnasArchivo+costoMax);
//        coordenadasMaximoMinimo.add("QoSMin"+separadorColumnasArchivo+QoSMin);
//        coordenadasMaximoMinimo.add("QoSMax"+separadorColumnasArchivo+QoSMax);
//
//        boolean appendMode = false;
//        Archivos ar = new Archivos();
//        ar.writeLargerTextFile(ruta+"maximosYminimos.csv",coordenadasMaximoMinimo,appendMode);
//
//
//
//        ArrayList<String> coordenadasMinimo = new ArrayList<>();
//        coordenadasMinimo.addAll(this.listaMinimos(paretoAe,"AE"));
//        coordenadasMinimo.addAll(this.listaMinimos(paretoCosto,"GCosto"));
//        coordenadasMinimo.addAll(this.listaMinimos(paretoQoS,"GQoS"));
//        coordenadasMinimo.addAll(this.listaMinimos(paretoRR,"RR"));
//
//        appendMode = false;
//        ar = new Archivos();
//        ar.writeLargerTextFile(ruta+"minimos.csv",coordenadasMinimo,appendMode);
//
//
//
//
//        ArrayList<String> solocabezal = new ArrayList<>();
//        solocabezal.add("x    y    label");
//        //se reinicia el archivo y se le mete el cabezal
//        ar.writeLargerTextFile(ruta+"grafica.data",solocabezal,false);
//        this.guardaFileData(paretoAe,"AE", ruta,ar);
//        this.guardaFileData(paretoCosto,"GCosto", ruta,ar);
//        this.guardaFileData(paretoQoS,"GQoS", ruta,ar);
//        this.guardaFileData(paretoRR,"RR", ruta,ar);
//
//
//
//
//







    }

    private String calculadorNormaToStringNORMALIZADO(double costoMin,
                                                      double QoSMin,
                                                      double costoMax,
                                                      double QoSMax,
                                                      String inicioLinea,
                                                      List<double[]> listaSolucs,
                                                      String separadorColumnasArchivo){
        double normaMenor= Double.MAX_VALUE;
        double CoordenadaCostoNormaMin= Double.MAX_VALUE;
        double CoordenadaQoSNormaMin= Double.MAX_VALUE;
        ArrayList<String> listaNormas = new ArrayList<>();
        ArrayList<String> valoresNormalizados = new ArrayList<>();




        listaNormas.add(inicioLinea);
        for(double [] solucion: listaSolucs){
            double costo = solucion[0];
            double QoS = solucion[1];
            System.out.println("--------------------");
            System.out.println(costoMax+" - "+costoMin);
            System.out.println(QoSMax+" - "+QoSMin);
            System.out.println(costo+" ++ "+QoS);

            double costoNORMAL = costoMax!=costoMin?(costo-costoMin)/(costoMax-costoMin):0;
            double QoSNORMAL = QoSMax!=QoSMin?(QoS-QoSMin)/(QoSMax-QoSMin):0;
            valoresNormalizados.add(costoNORMAL+separadorColumnasArchivo+QoSNORMAL);
            System.out.println(costoNORMAL+" "+QoSNORMAL);
            System.out.println("--------------------");

            double normaPunto = Math.sqrt(Math.pow(costoNORMAL,2)+Math.pow(QoSNORMAL,2)) ;
            if(normaMenor>normaPunto){
                normaMenor=normaPunto;
                CoordenadaCostoNormaMin=costo;
                CoordenadaQoSNormaMin=QoS;
            }
            listaNormas.add(costo+separadorColumnasArchivo+QoS+separadorColumnasArchivo+normaPunto);
        }
        //meto 2 saltos de linea que luego ocupo con promedio y stdv
        listaNormas.add("");
        listaNormas.add("");

        boolean appendMode = true;
        Archivos ar = new Archivos();
        ar.writeLargerTextFile(this.rutaAlgoritmo + "normasSoluciones.csv", listaNormas, appendMode);

         appendMode = false;
        Archivos are = new Archivos();
        are.writeLargerTextFile(this.rutaAlgoritmo + inicioLinea+"_Normalizado.csv", valoresNormalizados, appendMode);

        return inicioLinea+separadorColumnasArchivo+CoordenadaCostoNormaMin+separadorColumnasArchivo+CoordenadaQoSNormaMin+separadorColumnasArchivo+normaMenor;
    }


    private String calculadorNormaToString(String inicioLinea,List<double[]> listaSolucs,String separadorColumnasArchivo){
        double normaMenor= Double.MAX_VALUE;
        double costoMin= Double.MAX_VALUE;
        double QoSMin= Double.MAX_VALUE;
        for(double [] solucion: listaSolucs){
            double costo = solucion[0];
            double QoS = solucion[1];

            double normaPunto = Math.sqrt(Math.pow(costo,2)+Math.pow(QoS,2)) ;
            if(normaMenor>normaPunto){
                normaMenor=normaPunto;
                costoMin=costo;
                QoSMin=QoS;
            }
        }
        return inicioLinea+separadorColumnasArchivo+costoMin+separadorColumnasArchivo+QoSMin+separadorColumnasArchivo+normaMenor;
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
            System.out.println("No existe directorio "+rutaSalida);

        }
        directory = new File(rutaAlgoritmo);
        if (! directory.exists()){
            System.out.println("No existe directorio  "+rutaAlgoritmo);

        }

        //crear carpeta con nombre nombreAlgoritmo
        try {

            listaNombreArchivosEjecucion = new ArrayList<>();
            Archivos archi = new Archivos();


            for(int iteracion =idinferiorAlgoritmoEjecutar;iteracion<=idsuperiorAlgoritmoEjecutar;iteracion++) {
                String rutaCarpetaEjecucion = rutaAlgoritmo+nombreAlgoritmo+"I"+iteracion+"/";
//                this.logEjecuciones = new ArrayList<>();
//                this.tiemposEjec = new ArrayList<>();
//                this.logEjecuciones.add(descripcionInstancia);
//                this.logEjecuciones.add(nombreAlgoritmo);
//                this.logEjecuciones.add(nombreAlgoritmo+"I"+iteracion);
                directory = new File(rutaCarpetaEjecucion);
                if (! directory.exists()){
//                    directory.mkdir();
                    System.out.println("No existe directorio  "+rutaCarpetaEjecucion);
                 //   this.logEjecuciones.add(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Creando directorio "+rutaCarpetaEjecucion);
                }

                String nombreParetoEjecucion = "pareto.pr";
               // System.out.println(rutaCarpetaEjecucion+nombreParetoEjecucion);
               // System.out.println(rutaCarpetaEjecucion+"datosejec.txt");
               // System.out.println(rutaCarpetaEjecucion+"tiemposEjecucion.txt");

                List<String> contenidoParetoEjecucion = archi.readLargerTextFileAlternate(rutaCarpetaEjecucion+nombreParetoEjecucion);

                for (String fila:contenidoParetoEjecucion) {
                    String [] valores = fila.split(separadorColumnasArchivo);
                    double costo =Double.valueOf(valores[0]);
                    double Qos =Double.valueOf(valores[1]);
                    double [] filaDouble = new double[2];
                    filaDouble[0]=costo;
                    filaDouble[1]=Qos;
                    paretosSolucionesSeparados.add(filaDouble);
                }

                //solo me quedo con los 10 que ejecutaron en el nodo clase 50
                if(iteracion<=9) {
                    List<String> contenidoTiempo = archi.readLargerTextFileAlternate(rutaCarpetaEjecucion + "tiemposEjecucion.txt");
                    for (String fila : contenidoTiempo) {
                        tiemposEjec.add(fila);
                    }
                }

            }//for

            paretoGeneral=  paretera(paretosSolucionesSeparados);
            this.guardarParetoEnArchvo(paretoGeneral,"paretoGeneral.txt",rutaAlgoritmo);
            archi.writeLargerTextFile(rutaAlgoritmo+"/tiemposGral.txt",tiemposEjec,false);


        }catch(Exception e){
            System.err.println(nombreAlgoritmo);
            e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
        }
    }


    private  void guardarParetoEnArchvo(List<double []> pareto,
                                        String nombreArchivo,
                                        String ruta) {
        List<String> aLines = new ArrayList<>();
        for (double[] valorSoluco : pareto) {
            aLines.add(String.valueOf(valorSoluco[0]) + separadorColumnasArchivo + String.valueOf(valorSoluco[1]));
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

           // System.out.println(basedirExperimento+"/propertiesdir/"+nombreArchivoPropertiesInstancia+".properties");

            instanciaUtilizadaExperimento           = prop.getProperty("instanciaUtilizada");
           // System.out.println(instanciaUtilizadaExperimento);
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

    private List<double[]> paretera(List<double[]> solutionSet) {
        List<double[]> population = new ArrayList<>();
        //voy a eliminar los repetidos
        for(double[] valorNuevo:solutionSet){
            boolean esta = false;
            for(double[] valorYaAgregado:population){
                if(valorNuevo[0]==valorYaAgregado[0] && valorNuevo[1]==valorYaAgregado[1]){
                    esta=true;
                    break;
                }
            }
            if(!esta){
                population.add(valorNuevo);
            }
        }
        int cantidadEliminados = solutionSet.size()-  population.size() ;
        solutionSet=population;


        List<double[]> paretoDoublesolutionSet = new ArrayList<>();
        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Calculando pareto tamanio poblacion "+String.valueOf(population.size()));

        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Se eliminaron "+String.valueOf(cantidadEliminados));

        // dominateMe[i] contains the number of solutions dominating i
        int[] dominateMe = new int[population.size()];

        // iDominate[k] contains the list of solutions dominated by k
        //  List<List<Integer>> iDominate = new ArrayList<>(population.size());

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
            // iDominate.add(new LinkedList<Integer>());
            dominateMe[p] = 0;
        }

        int flagDominate;
        for (int p = 0; p < (population.size() - 1); p++) {
            // For all q individuals , calculate if p dominates q or vice versa
            for (int q = p + 1; q < population.size(); q++) {

                flagDominate = this.dominanceTest(solutionSet.get(p), solutionSet.get(q));

                if (flagDominate == -1) {
                    //  iDominate.get(p).add(q);
                    dominateMe[q]++;
                } else if (flagDominate == 1) {
                    // iDominate.get(q).add(p);
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
        return paretoDoublesolutionSet;

    }


    private int dominanceTest(double[] solution1, double[] solution2) {
        int result;
        boolean solution1Dominates = false;
        boolean solution2Dominates = false;

        int flag;
        double value1, value2;
        for (int i = 0; i < 2; i++) {
            value1 = solution1[i];
            value2 = solution2[i];
            if (value1 < value2) {
                flag = -1;
                //} else if (value1 / (1 + epsilon) > value2) {
            } else if (value2 < value1) {
                flag = 1;
            } else {
                flag = 0;
            }

            if (flag == -1) {
                solution1Dominates = true;
            }

            if (flag == 1) {
                solution2Dominates = true;
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
        return result;
    }





    private  void generarArchivoDataGrafica(List<double []> paretoAe,
                                            List<double []> paretoCosto,
                                            List<double []> paretoQoS,
                                            List<double []> paretoRR,
                                            String ruta,double costoMin,double costoMax,double QoSMin,double QoSMax) {
        //pasar los maximos y minimos globales por parametros
        //arrancar por el AE
        //guardar coordenadas de maximo y minimo de qos y de costo son 4
        //guardar coordenadas de maximo y minimo de qos y de costo
        ArrayList<String> coordenadasMaximoMinimo = new ArrayList<>();
        coordenadasMaximoMinimo.addAll(this.listaMinimosYmaximos(paretoAe,"AE"));
        coordenadasMaximoMinimo.addAll(this.listaMinimosYmaximos(paretoCosto,"GCosto"));
        coordenadasMaximoMinimo.addAll(this.listaMinimosYmaximos(paretoQoS,"GQoS"));
        coordenadasMaximoMinimo.addAll(this.listaMinimosYmaximos(paretoRR,"RR"));
        coordenadasMaximoMinimo.add("-------------");
        coordenadasMaximoMinimo.add("GLOBAL");
        coordenadasMaximoMinimo.add("costoMin"+separadorColumnasArchivo+costoMin);
        coordenadasMaximoMinimo.add("costoMax"+separadorColumnasArchivo+costoMax);
        coordenadasMaximoMinimo.add("QoSMin"+separadorColumnasArchivo+QoSMin);
        coordenadasMaximoMinimo.add("QoSMax"+separadorColumnasArchivo+QoSMax);

        boolean appendMode = false;
        Archivos ar = new Archivos();
        ar.writeLargerTextFile(ruta+"maximosYminimos.csv",coordenadasMaximoMinimo,appendMode);



        ArrayList<String> coordenadasMinimo = new ArrayList<>();
        coordenadasMinimo.addAll(this.listaMinimos(paretoAe,"AE"));
        coordenadasMinimo.addAll(this.listaMinimos(paretoCosto,"GCosto"));
        coordenadasMinimo.addAll(this.listaMinimos(paretoQoS,"GQoS"));
        coordenadasMinimo.addAll(this.listaMinimos(paretoRR,"RR"));

         appendMode = false;
         ar = new Archivos();
        ar.writeLargerTextFile(ruta+"minimos.csv",coordenadasMinimo,appendMode);




        ArrayList<String> solocabezal = new ArrayList<>();
        solocabezal.add("x    y    label");
        //se reinicia el archivo y se le mete el cabezal
        ar.writeLargerTextFile(ruta+"grafica.data",solocabezal,false);
        this.guardaFileData(paretoAe,"AE", ruta,ar);
        this.guardaFileData(paretoCosto,"GCosto", ruta,ar);
        this.guardaFileData(paretoQoS,"GQoS", ruta,ar);
        this.guardaFileData(paretoRR,"RR", ruta,ar);
    }


    private  ArrayList<String> listaMinimosYmaximos(List<double []> soluciones,String nombreHeuristica){
        ArrayList<String> coordenadasMaximoMinimo = new ArrayList<>();
        double costoMin= Double.MAX_VALUE;
        double CoordenadaQoS_costoMin= Double.MAX_VALUE;
        double QoSMin= Double.MAX_VALUE;
        double CoordenadaCosto_QoSMin= Double.MAX_VALUE;
        double costoMax= 0;
        double CoordenadaQoS_costoMax= 0;
        double QoSMax= 0;
        double CoordenadaCosto_QoSMax= 0;
        for (double[] valorSoluco : soluciones) {
            //busca los maximos y minimos

            double costo = valorSoluco[0];
            double QoS = valorSoluco[1];
            if(costoMax<costo){
                costoMax=costo;
                CoordenadaQoS_costoMax=QoS;
            }
            if(QoSMax<QoS){
                QoSMax=QoS;
                CoordenadaCosto_QoSMax= costo;
            }
            if(costoMin>costo){
                costoMin=costo;
                CoordenadaQoS_costoMin=QoS;
            }
            if(QoSMin>QoS){
                QoSMin=QoS;
                CoordenadaCosto_QoSMin= costo;
            }

        }
        coordenadasMaximoMinimo.add(nombreHeuristica);
        coordenadasMaximoMinimo.add("MinCosto:"+separadorColumnasArchivo+costoMin+separadorColumnasArchivo+"QoS:"+separadorColumnasArchivo+CoordenadaQoS_costoMin);
        coordenadasMaximoMinimo.add("MaxCosto:"+separadorColumnasArchivo+costoMax+separadorColumnasArchivo+"QoS:"+separadorColumnasArchivo+CoordenadaQoS_costoMax);
        coordenadasMaximoMinimo.add("Costo:"+separadorColumnasArchivo+CoordenadaCosto_QoSMin+separadorColumnasArchivo+"MinQoS:"+separadorColumnasArchivo+QoSMin);
        coordenadasMaximoMinimo.add("Costo:"+separadorColumnasArchivo+CoordenadaCosto_QoSMax+separadorColumnasArchivo+"MaxQoS:"+separadorColumnasArchivo+QoSMax);
        return coordenadasMaximoMinimo;
    }


    private  ArrayList<String> listaMinimos(List<double []> soluciones,String nombreHeuristica){
        ArrayList<String> coordenadasMaximoMinimo = new ArrayList<>();
        double costoMin= Double.MAX_VALUE;
        double CoordenadaQoS_costoMin= Double.MAX_VALUE;
        double QoSMin= Double.MAX_VALUE;
        double CoordenadaCosto_QoSMin= Double.MAX_VALUE;
        double costoMax= 0;
        double CoordenadaQoS_costoMax= 0;
        double QoSMax= 0;
        double CoordenadaCosto_QoSMax= 0;
        for (double[] valorSoluco : soluciones) {
            //busca los maximos y minimos

            double costo = valorSoluco[0];
            double QoS = valorSoluco[1];
            if(costoMax<costo){
                costoMax=costo;
                CoordenadaQoS_costoMax=QoS;
            }
            if(QoSMax<QoS){
                QoSMax=QoS;
                CoordenadaCosto_QoSMax= costo;
            }
            if(costoMin>costo){
                costoMin=costo;
                CoordenadaQoS_costoMin=QoS;
            }
            if(QoSMin>QoS){
                QoSMin=QoS;
                CoordenadaCosto_QoSMin= costo;
            }

        }
        coordenadasMaximoMinimo.add(nombreHeuristica);
        coordenadasMaximoMinimo.add("MinCosto:"+separadorColumnasArchivo+costoMin+separadorColumnasArchivo+"QoS:"+separadorColumnasArchivo+CoordenadaQoS_costoMin);
        //coordenadasMaximoMinimo.add("MaxCosto:"+separadorColumnasArchivo+costoMax+separadorColumnasArchivo+"QoS:"+separadorColumnasArchivo+CoordenadaQoS_costoMax);
        coordenadasMaximoMinimo.add("Costo:"+separadorColumnasArchivo+CoordenadaCosto_QoSMin+separadorColumnasArchivo+"MinQoS:"+separadorColumnasArchivo+QoSMin);
       // coordenadasMaximoMinimo.add("Costo:"+separadorColumnasArchivo+CoordenadaCosto_QoSMax+separadorColumnasArchivo+"MaxQoS:"+separadorColumnasArchivo+QoSMax);






       return coordenadasMaximoMinimo;
    }



    private  void guardaFileData(List<double []> soluciones,String nombreHeuristica,String ruta,Archivos ar){
        ArrayList<String> coordenadas = new ArrayList<>();
        boolean appendMode = true;



        for (double[] valorSoluco : soluciones) {
            //busca los maximos y minimos

            double costo = valorSoluco[0];
            double QoS = valorSoluco[1];
            String idcoso="";
            if(nombreHeuristica=="AE"){
                idcoso="k"+separadorColumnasArchivo+"\\\\";
            }
            if(nombreHeuristica=="GCosto"){
                idcoso="M"+separadorColumnasArchivo+"\\\\";
            }
            if(nombreHeuristica=="GQoS"){
                idcoso="E"+separadorColumnasArchivo+"\\\\";
            }
            if(nombreHeuristica=="RR"){
                idcoso="F"+separadorColumnasArchivo+"\\\\";
            }

            coordenadas.add(costo+separadorColumnasArchivo+QoS+separadorColumnasArchivo+idcoso);
        }
        ar.writeLargerTextFile(ruta+"grafica.data",coordenadas,appendMode);

    }
}
