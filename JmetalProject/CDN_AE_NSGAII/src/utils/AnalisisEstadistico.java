//package utils;
//
//import operadores.CDNCrossover;
//import operadores.CDNmMutation;
//import org.uma.jmetal.algorithm.Algorithm;
//import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
//import org.uma.jmetal.operator.CrossoverOperator;
//import org.uma.jmetal.operator.MutationOperator;
//import org.uma.jmetal.operator.SelectionOperator;
//import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
//import org.uma.jmetal.util.AlgorithmRunner;
//import org.uma.jmetal.util.JMetalLogger;
//import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
//import problema.MultiobjCDN;
//import soluciones.CDNSolution;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class AnalisisEstadistico {
//
//    public AnalisisEstadistico() {
//    }
//
//    List<double[]> global_solutionSet = new ArrayList<>();
//    List<double[]> algoritmoGlobal_solutionSet;
//    List<List<double[]>> paretosSolucionesSeparados = new ArrayList<>();
//    String rutaSalida = "";
//    String separadorColumnasArchivo = " ";
//    //List<String> listaNombreArchivosEjecucion;
//    HashMap<String,  HashMap<String, String>> algoritmo_nombreEjecucion_rutaTotalPareto;
//    String nombreAlgoritmo;
//    String rutaAlgoritmo;
//    String rutaEstadisitcas;
//    String nombreArchiboParetoReferecnciaGLOBAL = "referenciaAE.pr";
//    String nombreArchiboParetoAMPL = "referenciaAMPL.pr";
//    String nombreArchivoGreedyTodos = "TodosGreedy.pr";
//    String nombreArchivoGreedyPareto = "ParetoGreedy.pr";
//    String nombreArchivoRhypervolumen = "hypervolume.r";
//    String nombreArchivoRGraficas = "graficas.r";
//    String nombreArchivoGRAFICAparetoreferenciaTODOSAlgoritmos_AMPL = "grafica_TODOS_AMPL.r";
//    String nombreArchivoGRAFICAparetoreferenciaAE_GREEDY = "grafica_AE_GREEDY.r";
//    String nombreArchivoGRAFICAparetoreferenciaAE_GREEDY_AMPL = "grafica_AE_GREEDY_AMPL.r";
//    String nombreArchivoGRAFICAparetoreferenciaAE_AMPL = "grafica_AE_AMPL.r";
//    HashMap<String, String> nombreAlgoritmo_rutaTotalPareto;
//    HashMap<String, String> nombreEjecucion_rutaTotalPareto;
//
//    double maximoCosto = 0;
//    double maximoQoS = 0;
//    String descripcionInstancia;
//
//    List<String> tiemposEjec;
//    int cantidadAlgoritmos;
//    String instanciaUtilizadaExperimento;
//    List<Double> genMutationProbability;
//    List<Double> crossoverProbability;
//    List<Integer> populationSize;
//    List<Integer> maxEvaluations;
//    List<Integer> cantidadIteraciones;
//
//
//    public static void main(String[] args) {
//
//        try {
//            AnalisisEstadistico eje = new AnalisisEstadistico();
//            JMetalLogger.configureLoggers(new File("configLogger.properties"));
//            String nombreArchivoPropertiesInstancia = "expe_tandaA1_500Vid_300Mins_6Prove";
//            if (args.length != 0) {
//                nombreArchivoPropertiesInstancia = args[0];
//            }
//            eje.leerPropertiesExperimento(nombreArchivoPropertiesInstancia);
//            eje.nombreAlgoritmo_rutaTotalPareto = new HashMap<>();
//            for (int algoritmo = 0; algoritmo < eje.cantidadAlgoritmos; algoritmo++) {
//                eje.tiemposEjec = new ArrayList<>();
//                eje.levantarDatosAlgoritmo(eje.instanciaUtilizadaExperimento,
//                        eje.cantidadIteraciones.get(algoritmo),
//                        eje.genMutationProbability.get(algoritmo),
//                        eje.crossoverProbability.get(algoritmo),
//                        eje.populationSize.get(algoritmo),
//                        eje.maxEvaluations.get(algoritmo));
//
//            }
//
//            //despues de levantar todos los paretos para todas las combinaciones de parametros para la misma instancia
//            //construyo el pareto de referencia
//            List<double[]> paretoGeneral = eje.paretera(eje.global_solutionSet);
//            eje.guardarParetoEnArchvo(paretoGeneral, eje.nombreArchiboParetoReferecnciaGLOBAL, eje.rutaEstadisitcas);
//            eje.nombreAlgoritmo_rutaTotalPareto.put(eje.instanciaUtilizadaExperimento+"PARETO", eje.rutaEstadisitcas+eje.nombreArchiboParetoReferecnciaGLOBAL);
//
//
//
//            //generar script en R de hypervolumen
//            eje.construirScriptRhypervolume();
//            eje.construirScriptRGraficas();
//
//
//            // eje.correrGreedy(eje.rutaSalida, eje.instanciaUtilizadaExperimento);
//        } catch (Exception e) {
//            e.printStackTrace();
//            JMetalLogger.logger.severe(e.getMessage());
//        }
//
//    }
//
//    /**
//     * @param instanciaUtilizada
//     * @param CANT_ITERACIONES
//     * @param genMutationProbability
//     * @param crossoverProbability
//     * @param populationSize         tiene que ser divisible por 2
//     * @param maxEvaluations         cant generacions = MaxEvaluations/PopulationSize
//     */
//    private void levantarDatosAlgoritmo(String instanciaUtilizada,
//                                 int CANT_ITERACIONES,
//                                 double genMutationProbability,
//                                 double crossoverProbability,
//                                 int populationSize,
//                                 int maxEvaluations) {
//
//        nombreAlgoritmo = this.darNombreAlgoritmo(instanciaUtilizada,
//                genMutationProbability,
//                crossoverProbability,
//                populationSize,
//                maxEvaluations);
//
//        //cargo la ruta de salida
//        CdnInstancia insta = new CdnInstancia();
//        insta.leerPropertiesInstancia(instanciaUtilizada);
//        rutaSalida = insta.getRutaAbsolutaSalidaInstancia();
//        rutaEstadisitcas = rutaSalida+"/ESTADISTICAS/";
//        descripcionInstancia = insta.descripcionInstancia;
//
//        rutaAlgoritmo = rutaSalida + nombreAlgoritmo + "/";
//        algoritmoGlobal_solutionSet = new ArrayList<>();
//
//        File directory = new File(rutaSalida);
//        if (!directory.exists()) {
//
//            System.out.println("NO EXISTE LA RUTA " + rutaSalida);
//
//        }
//        directory = new File(rutaAlgoritmo);
//        if (!directory.exists()) {
//
//            System.out.println("NO EXISTE LA RUTA " + rutaAlgoritmo);
//
//        }
//
//         directory = new File(rutaEstadisitcas);
//        if (! directory.exists()){
//            directory.mkdir();
//            System.out.println("Creando directorio "+rutaEstadisitcas);
//        }
//
//        try {
//            //genero y guardo los nombres de todos los archivos de salida del experimento
//            //con esos nombres ahora levanto los paretos del filesystem para hacer el pareto de referencia
//            //luego con esos nombres se construyen script en R para graficar y calcular hypervolumen
//            nombreEjecucion_rutaTotalPareto = new HashMap<>();
//            for (int iteracion = 0; iteracion < CANT_ITERACIONES; iteracion++) {
//                String nombreArchivoEjecucion = nombreAlgoritmo + "I" + iteracion + ".pr";
//                nombreEjecucion_rutaTotalPareto.put(nombreAlgoritmo + "I" + iteracion,rutaAlgoritmo+nombreArchivoEjecucion);
//                List<double[]> paretoArchivo = this.leerParetoDeArchvo(nombreArchivoEjecucion, rutaAlgoritmo);
//                this.acumularParetosSoluciones(paretoArchivo, iteracion);
//            }//for
//            //guardo el hasmap de nombres de los archivitos de cada ejecucion para calcular hypervolume contra pareto general
//            algoritmo_nombreEjecucion_rutaTotalPareto.put(nombreAlgoritmo,nombreEjecucion_rutaTotalPareto);
//            //obtener pareto global del algoritmo y pasarlo al resto de funciones
//            List<double[]> paretoDelAlgoritmo = this.paretera(algoritmoGlobal_solutionSet);
//            this.guardarParetoEnArchvo(paretoDelAlgoritmo, nombreAlgoritmo+"pareto.fp", rutaEstadisitcas);
//            this.nombreAlgoritmo_rutaTotalPareto.put(nombreAlgoritmo, rutaEstadisitcas + nombreAlgoritmo+"pareto.fp");
//
//
//
//        } catch (Exception e) {
//            System.err.println(nombreAlgoritmo);
//            e.printStackTrace();
//            JMetalLogger.logger.severe(e.getMessage());
//        }
//    }
//
//    private List<double[]> leerParetoDeArchvo(String nombreArchivo,
//                                       String ruta) {
//        List<double[]> pareto = new ArrayList<>();
//        Archivos ar = new Archivos();
//        List<String> lineasPareto = ar.readLargerTextFileAlternate(ruta + nombreArchivo);
//        String[] pareja_fila;
//        for (String valorSoluco : lineasPareto) {
//            pareja_fila = valorSoluco.split(separadorColumnasArchivo);
//            double[] valorSolucion = new double[2];
//            valorSolucion[0]=Double.valueOf(pareja_fila[0]) ;
//            valorSolucion[1]=Double.valueOf(pareja_fila[1]) ;
//            pareto.add(valorSolucion);
//        }
//        return pareto;
//    }
//
//    private void correrGreedy(String ruta,
//                              String instanciaUtilizada) {
//        List<String> aLines = new ArrayList<>();
//        List<String> todasGreedy = new ArrayList<>();
//
//        CdnHeuristica heuri = new CdnHeuristica(instanciaUtilizada);
//        List<double[]> solucionesGreedy = heuri.muchasGreedy(instanciaUtilizada);
//        System.out.println("Arranca a calular paretto GRAFICA_GREEDY....");
//
//        List<double[]> elParettoGreedy = this.paretera(solucionesGreedy);
//
//
//        for (double[] valorSoluco : elParettoGreedy) {
//            aLines.add(String.valueOf(valorSoluco[0]) + separadorColumnasArchivo + String.valueOf(valorSoluco[1]));
//        }
//
//
//        for (double[] valorSoluco : solucionesGreedy) {
//            todasGreedy.add(String.valueOf(valorSoluco[0]) + separadorColumnasArchivo + String.valueOf(valorSoluco[1]));
//        }
//
//        boolean appendMode = false;
//        Archivos ar = new Archivos();
//
//        ar.writeLargerTextFile(ruta + nombreArchivoGreedyPareto, aLines, appendMode);
//        ar.writeLargerTextFile(ruta + nombreArchivoGreedyTodos, todasGreedy, appendMode);
//    }
//
//    private void construirScriptRhypervolume() {
//
//        List<String> aLines = new ArrayList<>();
//        String nombreArchivoSalidaR = "hypervolumenes.txt";
//        String varParetoRef = nombreArchiboParetoReferecncia.replace(".", "");
//        aLines.add("setwd('" + rutaAlgoritmo + "')");
//        aLines.add("library(emoa)");
//        aLines.add(varParetoRef + " <- read.csv('" + nombreArchiboParetoReferecncia + "' , sep='" + separadorColumnasArchivo + "')");
//        aLines.add("matrizzz = as.matrix(" + varParetoRef + ")");
//        aLines.add("traspuesta<-t(matrizzz)");
//        aLines.add("maximaX<-traspuesta[2,which.max(traspuesta[2,])]");
//        aLines.add("maximaY<-traspuesta[1,which.max(traspuesta[1,])]");
//        aLines.add("hypervolumenReferencia" + varParetoRef + "<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))");
//
//        int count = 0;
//        for (String nombreArchi : listaNombreArchivosEjecucion) {
//            aLines.add("\n");
//            aLines.add(nombreArchi.replace(".", "") + " <- read.csv('" + nombreArchi + "' , sep='" + separadorColumnasArchivo + "')");
//            aLines.add("matrizzz" + count + " = as.matrix(" + nombreArchi.replace(".", "") + ")");
//            aLines.add("traspuesta" + count + "<-t(matrizzz" + count + ")");
//            aLines.add("hypervolumen" + nombreArchi.replace(".", "") + "<-dominated_hypervolume(traspuesta" + count + ",c(maximaY,maximaX))");
//
//            //calculando hypervolumen
//            aLines.add("hypervolumenRelativo" + nombreArchi.replace(".", "") + "<-hypervolumen" + nombreArchi.replace(".", "") + "/hypervolumenReferencia" + varParetoRef);
//
//            //guarda en archivo
//            aLines.add("write(\"hypervolumenRelativo" + nombreArchi.replace(".", "") + "\", file = '" + nombreArchivoSalidaR + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");
//            aLines.add("write(hypervolumenRelativo" + nombreArchi.replace(".", "") + ", file = '" + nombreArchivoSalidaR + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");
//            count++;
//
//        }
//
//
//        boolean appendMode = false;
//        Archivos ar = new Archivos();
//        ar.writeLargerTextFile(rutaAlgoritmo + nombreArchivoRhypervolumen, aLines, appendMode);
//
//
//    }
//
//
//    private void acumularParetosSoluciones(List<double[]> soluciones,
//                                           int indiceparetochico) {
//
//
//        for (double[] parejaCoso : soluciones) {
//            double costo = parejaCoso[0];
//            double Qos = parejaCoso[1];
//
//
//
//            //se guardan los maximos para ajustar los ejes de las graficas
//            if (maximoCosto < costo) {
//                maximoCosto = costo;
//            }
//            if (maximoQoS < Qos) {
//                maximoQoS = Qos;
//            }
//            //guardo en el conjunto global de todas las soluciones
//            //el que se usara para hacer el pareto de referencia
//            global_solutionSet.add(parejaCoso);
//            algoritmoGlobal_solutionSet.add(parejaCoso);
//
//        }
//        //guardo cada pareto por separado
//        paretosSolucionesSeparados.add(indiceparetochico, soluciones);
//
//    }
//
//
//    private void construirScriptRGraficas() {
//        try {
//            List<String> aLines = new ArrayList<>();
//            String varParetoRef = nombreArchiboParetoReferecncia.replace(".", "");
//
//            aLines.add("setwd('" + rutaAlgoritmo + "')");
//            aLines.add(varParetoRef + " <- read.csv('" + nombreArchiboParetoReferecncia + "' , sep='" + separadorColumnasArchivo + "')");
//            for (String nombreArchi : listaNombreArchivosEjecucion) {
//                aLines.add(nombreArchi.replace(".", "") + " <- read.csv('" + nombreArchi + "' , sep='" + separadorColumnasArchivo + "')");
//            }
//            aLines.add("\n");
//
//
//            aLines.add("plot(" + varParetoRef + " ,xlab='Costo', ylab='RTT (ms)', pch=19,col='red',xlim=range(0," + maximoCosto + "), ylim=range(0," + maximoQoS + "))");
//            String vectorTipoPuntoReferencias = "19";
//            String vectorColorReferencias = "\"red\"";
//            String vectorTextoReferencias = "\"FP Referencia\"";
//            int count = 0;
//            for (String nombreArchi : listaNombreArchivosEjecucion) {
//                aLines.add("points(" + nombreArchi.replace(".", "") + ",col=\"green\", pch=19)");
//                vectorTipoPuntoReferencias += ",19";
//                vectorColorReferencias += ",\"green\"";
//                vectorTextoReferencias += ",\"FP " + count + "\"";
//
//                count++;
//            }
//            aLines.add("\n");
//            aLines.add("legend(");
//            aLines.add("        \"topright\",");
//            aLines.add("        pch=c(" + vectorTipoPuntoReferencias + "),");
//            aLines.add("        col=c(" + vectorColorReferencias + "),");
//            aLines.add("        legend = c(" + vectorTextoReferencias + ")");
//            aLines.add(")");
//            boolean appendMode = false;
//            Archivos ar = new Archivos();
//            ar.writeLargerTextFile(rutaAlgoritmo + nombreArchivoRGraficas, aLines, appendMode);
//
//
//            //todo los paretos contra el ampl
//            List<String> todosContraAMPL = new ArrayList<>();
//            String varParetoAMPLRef = nombreArchiboParetoAMPL.replace(".", "");
//
//            todosContraAMPL.add(varParetoAMPLRef + " <- read.csv('" + rutaSalida + nombreArchiboParetoAMPL + "' , sep='" + separadorColumnasArchivo + "')");
//            for (Map.Entry<String, String> entry : this.nombreAlgoritmo_rutaTotalPareto.entrySet()) {
//                String nombreAlgoritmo = entry.getKey();
//                String rutaArchivoAlgoritmo = entry.getValue();
//                todosContraAMPL.add(nombreAlgoritmo + " <- read.csv('" + rutaArchivoAlgoritmo + "' , sep='" + separadorColumnasArchivo + "')");
//            }
//            todosContraAMPL.add("\n");
//            //VER QUE ACA se queda con los costos maximos del ultimo algoritmo ejecutado
//            todosContraAMPL.add("plot(" + varParetoRef + " ,xlab='Costo', ylab='RTT (ms)', pch=19,col='red',xlim=range(0," + maximoCosto + "), ylim=range(0," + maximoQoS + "))");
//            vectorTipoPuntoReferencias = "19";
//            vectorColorReferencias = "\"red\"";
//            vectorTextoReferencias = "\"AMPL\"";
//            count = 0;
//            for (Map.Entry<String, String> entry : this.nombreAlgoritmo_rutaTotalPareto.entrySet()) {
//                String nombreAlgoritmo = entry.getKey();
//
//                todosContraAMPL.add("points(" + nombreAlgoritmo + ",col=\"green\", pch=19)");
//                vectorTipoPuntoReferencias += ",19";
//                vectorColorReferencias += ",\"green\"";
//                vectorTextoReferencias += ",\"FP " + count + "\"";
//
//                count++;
//            }
//            todosContraAMPL.add("\n");
//            todosContraAMPL.add("legend(");
//            todosContraAMPL.add("        \"topright\",");
//            todosContraAMPL.add("        pch=c(" + vectorTipoPuntoReferencias + "),");
//            todosContraAMPL.add("        col=c(" + vectorColorReferencias + "),");
//            todosContraAMPL.add("        legend = c(" + vectorTextoReferencias + ")");
//            todosContraAMPL.add(")");
//            appendMode = false;
//
//            ar.writeLargerTextFile(rutaSalida + nombreArchivoGRAFICAparetoreferenciaTODOSAlgoritmos_AMPL, todosContraAMPL, appendMode);
//
//
//            //         nombreArchivoGRAFICAparetoreferenciaTODOSAlgoritmos_AMPL
//
//            // PARETO REFERENCIA Y GREEDY
//            //nombreArchivoGRAFICAparetoreferenciaAE_GREEDY
//
//
//            //PARETO REFERENCIA AMPL  Y GREEDY
//            //  nombreArchivoGRAFICAparetoreferenciaAE_GREEDY_AMPL
//
//            //PARETO REFERENCIA AMPL  Y GREEDY
//            //nombreArchivoGRAFICAparetoreferenciaAE_AMPL
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            JMetalLogger.logger.severe(e.getMessage());
//        }
//    }
//
//    private String darNombreAlgoritmo(
//            String instanciaUtilizada,
//            double genMutationProbability,
//            double crossoverProbability,
//            int populationSize,
//            int maxEvaluations) {
//        String muta = "M" + String.valueOf(genMutationProbability).replace(".", "");
//        String cross = "C" + String.valueOf(crossoverProbability).replace(".", "");
//        String generacions = "G" + String.valueOf(maxEvaluations / populationSize);
//        String tamaniopoblacion = "P" + String.valueOf(populationSize);
//
//        return instanciaUtilizada + muta + cross + generacions + tamaniopoblacion;
//    }
//
//
//
//    private List<double[]> paretera(List<double[]> solutionSet) {
//        List<double[]> population = solutionSet;
//        List<double[]> paretoDoublesolutionSet = new ArrayList<>();
//
//        // dominateMe[i] contains the number of solutions dominating i
//        int[] dominateMe = new int[population.size()];
//
//        // iDominate[k] contains the list of solutions dominated by k
//        List<List<Integer>> iDominate = new ArrayList<>(population.size());
//
//        // front[i] contains the list of individuals belonging to the front i
//        // ArrayList<List<Integer>> front = new ArrayList<>(population.size() + 1);
//
//        // Initialize the fronts
////        for (int i = 0; i < population.size() + 1; i++) {
////            front.add(new LinkedList<Integer>());
////        }
//
//        // Fast non dominated sorting algorithm
//        // Contribution of Guillaume Jacquenot
//        for (int p = 0; p < population.size(); p++) {
//            // Initialize the list of individuals that i dominate and the number
//            // of individuals that dominate me
//            iDominate.add(new LinkedList<Integer>());
//            dominateMe[p] = 0;
//        }
//
//        int flagDominate;
//        for (int p = 0; p < (population.size() - 1); p++) {
//            // For all q individuals , calculate if p dominates q or vice versa
//            for (int q = p + 1; q < population.size(); q++) {
//
//                flagDominate = this.dominanceTest(solutionSet.get(p), solutionSet.get(q));
//
//                if (flagDominate == -1) {
//                    iDominate.get(p).add(q);
//                    dominateMe[q]++;
//                } else if (flagDominate == 1) {
//                    iDominate.get(q).add(p);
//                    dominateMe[p]++;
//                }
//            }
//        }
//
//        //me quedo con los no dominados
//        for (int i = 0; i < population.size(); i++) {
//            if (dominateMe[i] == 0) {
//                //  front.get(0).add(i);
//                paretoDoublesolutionSet.add(solutionSet.get(i));
//
//            }
//        }
//
//
//        return paretoDoublesolutionSet;
//
//        //Obtain the rest of fronts
////        int i = 0;
////        Iterator<Integer> it1, it2; // Iterators
////        while (front.get(i).size() != 0) {
////            i++;
////            it1 = front.get(i - 1).iterator();
////            while (it1.hasNext()) {
////                it2 = iDominate.get(it1.next()).iterator();
////                while (it2.hasNext()) {
////                    int index = it2.next();
////                    dominateMe[index]--;
////                    if (dominateMe[index] == 0) {
////                        front.get(i).add(index);
////                        //RankingAndCrowdingAttr.getAttributes(solutionSet.get(index)).setRank(i);
////                        solutionSet.get(index).setAttribute(getAttributeID(), i);
////                    }
////                }
////            }
////        }
////
////        rankedSubPopulations = new ArrayList<>();
////        //0,1,2,....,i-1 are fronts, then i fronts
////        for (int j = 0; j < i; j++) {
////            rankedSubPopulations.add(j, new ArrayList<S>(front.get(j).size()));
////            it1 = front.get(j).iterator();
////            while (it1.hasNext()) {
////                rankedSubPopulations.get(j).add(solutionSet.get(it1.next()));
////            }
////        }
//    }
//
//
//    private int dominanceTest(double[] solution1, double[] solution2) {
//        int result;
//        boolean solution1Dominates = false;
//        boolean solution2Dominates = false;
//
//        int flag;
//        double value1, value2;
//        for (int i = 0; i < 2; i++) {
//            value1 = solution1[i];
//            value2 = solution2[i];
//            if (value1 < value2) {
//                flag = -1;
//                //} else if (value1 / (1 + epsilon) > value2) {
//            } else if (value2 < value1) {
//                flag = 1;
//            } else {
//                flag = 0;
//            }
//
//            if (flag == -1) {
//                solution1Dominates = true;
//            }
//
//            if (flag == 1) {
//                solution2Dominates = true;
//            }
//        }
//
//        if (solution1Dominates == solution2Dominates) {
//            // non-dominated solutions
//            result = 0;
//        } else if (solution1Dominates) {
//            // solution1 dominates
//            result = -1;
//        } else {
//            // solution2 dominates
//            result = 1;
//        }
//        return result;
//    }
//
//
//    public void leerPropertiesExperimento(String nombreArchivoPropertiesInstancia,String basedirExperimento){
//
//        Properties prop = new Properties();
//
//        InputStream input = null;
//
//
//        genMutationProbability=new ArrayList<>();
//        crossoverProbability=new ArrayList<>();
//        populationSize=new ArrayList<>();
//        generacioneslista=new ArrayList<>();
//        cantidadIteraciones=new ArrayList<>();
//
//        System.out.println("Cargando properties "+nombreArchivoPropertiesInstancia);
//
//        try {
//
//
//            //instancia se carga desde carpeta properties con ruta relativa
//            input = new FileInputStream(basedirExperimento+"/propertiesdir/"+nombreArchivoPropertiesInstancia+".properties");
//
//            // load a properties file
//            prop.load(input);
//
//
//
//            instanciaUtilizadaExperimento           = prop.getProperty("instanciaUtilizada");
//            String [] crossoverProbability_split    = prop.getProperty("crossoverProbability").split(",");
//            String [] genMutationProbability_split  = prop.getProperty("genMutationProbability").split(",");
//            String [] populationSize_split          = prop.getProperty("populationSize").split(",");
//            String [] generaciones_split            = prop.getProperty("generaciones").split(",");
//            String [] cantidadIteraciones_split     = prop.getProperty("cantidadIteraciones").split(",");
//
//
//            for(String Pcross:crossoverProbability_split){
//                crossoverProbability.add(Double.valueOf(Pcross));
//            }
//
//            for(String Pmut:genMutationProbability_split){
//                genMutationProbability.add(Double.valueOf(Pmut));
//            }
//
//            for(String Popu:populationSize_split){
//                int poblacion=Integer.valueOf(Popu);
//                populationSize.add(poblacion);
//
//            }
//
//            for(String generaciones:generaciones_split){
//                generacioneslista.add(Integer.valueOf(generaciones));
//            }
//
//            for(String iters:cantidadIteraciones_split){
//                cantidadIteraciones.add(Integer.valueOf(iters));
//            }
//
//
//
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    private  void guardarParetoEnArchvo(List<double []> pareto,
//                                        String nombreArchivo,
//                                        String ruta){
//        List<String> aLines = new ArrayList<>();
//        for (double [] valorSoluco:pareto) {
//            aLines.add(String.valueOf(valorSoluco[0])+separadorColumnasArchivo+String.valueOf(valorSoluco[1]));
//        }
//
//        boolean appendMode = false;
//        Archivos ar = new Archivos();
//        ar.writeLargerTextFile(ruta+nombreArchivo,aLines,appendMode);
//    }
//}