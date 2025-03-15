package utils;

import org.uma.jmetal.util.JMetalLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by ggoni on 21/09/17.
 */
public class GeneradorScriptsR {

    public GeneradorScriptsR() {
    }




    HashMap<String, HashMap<String, String>> algoritmo_nombreEjecucion_rutaTotalPareto;

    String rutaEstadisitcas;
    String nombreArchiboParetoReferecnciaGLOBAL = "referenciaAE.pr";


    HashMap<String, String> nombreEjecucion_rutaTotalPareto;

    String rutaHyperRelativoLocal;





    String rutaSalida ="";
    String separadorColumnasArchivo =" ";

    String nombreAlgoritmo;
    String rutaAlgoritmo;
    String graficasAmplParetoReferencia;

    String RUTAparetosEjecucion;



    HashMap<String,String> nombreAlgoritmo_rutaTotalPareto;

    double maximoCosto =0;
    double maximoQoS =0;
    String descripcionInstancia;


    String instanciaUtilizadaExperimento;
    List<Double> genMutationProbability;
    List<Double> crossoverProbability;
    List<Integer> populationSize;
    List<Integer> generacioneslista;
    List<Integer> cantidadIteraciones;


    public static void main(String[] args) {

        try {
            //  String rutaAMPL="F:/PROYE/solocodigo/proycodigo/AMPL/paretos/";
            String rutaAMPL = "/home/ggoni/Documentos/proye/proycodigo/AMPL/paretos/";
            ArrayList<String> instancias = new ArrayList<>();

            instancias.add("expe_tandaA1_500Vid_300Mins_6Prove");
            instancias.add("expe_tandaD2_4000Vid_480Mins_7Prove");
            instancias.add("expe_tandaD1_400Vid_300Mins_5Prove");
            instancias.add("expe_tandaC1_1000Vid_240Mins_7Prove");
            for(String nombreArchivoPropertiesInstancia : instancias){
            GeneradorScriptsR eje = new GeneradorScriptsR();
            // JMetalLogger.configureLoggers(new File(System.getProperty("user.dir")+"/CDN_AE_NSGAII/src/resources/propertiesdir/"+"configLogger.properties"));
           // String nombreArchivoPropertiesInstancia = "expe_tandaA1_500Vid_300Mins_6Prove";
            //String nombreArchivoPropertiesInstanciaAMPL ="cdntandaA1_500Vid_300Mins_6Prove_1XreG_1XreQ_pareto";
            //String nombreArchivoPropertiesInstancia ="expe_tandaD2_4000Vid_480Mins_7Prove";
            //String nombreArchivoPropertiesInstanciaAMPL ="NO_TIENE";
            //  String nombreArchivoPropertiesInstancia ="expe_tandaD1_400Vid_300Mins_5Prove";
            //String nombreArchivoPropertiesInstanciaAMPL ="cdntandaD1_400Vid_300Mins_5Prove_1XreG_1XreQ_pareto";
            // String nombreArchivoPropertiesInstancia ="expe_tandaC1_1000Vid_240Mins_7Prove";

            int idinferiorAlgoritmoEjecutar = -1;
            int idsuperiorAlgoritmoEjecutar = -1;
            if (args.length != 0) {
                nombreArchivoPropertiesInstancia = args[0];
                idinferiorAlgoritmoEjecutar = Integer.valueOf(args[1]);
                idsuperiorAlgoritmoEjecutar = Integer.valueOf(args[2]);
            }
            CdnInstancia insta = new CdnInstancia();
            String basedirExperimento = insta.determinarBasedir();
            eje.leerPropertiesExperimento(nombreArchivoPropertiesInstancia, basedirExperimento);
            eje.nombreAlgoritmo_rutaTotalPareto = new HashMap<>();
                CdnInstancia instaparalos = new CdnInstancia(eje.instanciaUtilizadaExperimento);
            // eje.levantarDatosAmplToPareto(rutaAMPL);

            insta.leerPropertiesInstancia(eje.instanciaUtilizadaExperimento);
            eje.rutaSalida = insta.getRutaAbsolutaSalidaInstancia();
            eje.descripcionInstancia = insta.descripcionInstancia;
            eje.algoritmo_nombreEjecucion_rutaTotalPareto = new HashMap<>();

            int idalgoritmoEjecutando = 0;
            for (int cantIteraciones : eje.cantidadIteraciones) {
                for (int cantGeneraciones : eje.generacioneslista) {
                    for (int poblacion : eje.populationSize) {
                        for (double Pcross : eje.crossoverProbability) {
                            for (double Pmut : eje.genMutationProbability) {
                                int maxevaluations = poblacion * cantGeneraciones;

                                // se paraleliza ejecuciones con el ida algoritmo recibido por parametro
                                if (idinferiorAlgoritmoEjecutar == -1 ||
                                        (idalgoritmoEjecutando <= idsuperiorAlgoritmoEjecutar && idinferiorAlgoritmoEjecutar <= idalgoritmoEjecutando)) {

                                    eje.levantarDatosAlgoritmo(eje.instanciaUtilizadaExperimento,
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

            //despues de levantar todos los paretos para todas las combinaciones de parametros para la misma instancia
            //construyo el pareto de referencia
            System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": " + "Construyendo pareto grande de referencia");


            eje.nombreAlgoritmo_rutaTotalPareto.put(eje.instanciaUtilizadaExperimento + "PARETO", eje.rutaEstadisitcas + eje.nombreArchiboParetoReferecnciaGLOBAL);


            //generar script en R de hypervolumen
            eje.construirScriptRhypervolume();
            eje.construirScriptRGraficas();


            //solo genero una vez el greedy
            if ((args.length == 0) || (args.length != 0 && idinferiorAlgoritmoEjecutar == 0)) {
                // eje.correrGreedy(eje.rutaSalida, eje.instanciaUtilizadaExperimento);
            }

        }
        }catch(Exception e){
            e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
        }

    }

    /**
     * @param instanciaUtilizada
     * @param CANT_ITERACIONES
     * @param genMutationProbability
     * @param crossoverProbability
     * @param populationSize         tiene que ser divisible por 2
     * @param maxEvaluations         cant generacions = MaxEvaluations/PopulationSize
     */
    private void levantarDatosAlgoritmo(String instanciaUtilizada,
                                        int CANT_ITERACIONES,
                                        double genMutationProbability,
                                        double crossoverProbability,
                                        int populationSize,
                                        int maxEvaluations) {

        nombreAlgoritmo = this.darNombreAlgoritmo(instanciaUtilizada,
                genMutationProbability,
                crossoverProbability,
                populationSize,
                maxEvaluations);

        //cargo la ruta de salida
        CdnInstancia insta = new CdnInstancia();
        insta.leerPropertiesInstancia(instanciaUtilizada);
        rutaSalida = insta.getRutaAbsolutaSalidaInstancia();
        rutaEstadisitcas = rutaSalida+"ESTADISTICAS/";
        rutaHyperRelativoLocal=rutaEstadisitcas+"hypervolumenInstancia/";
        graficasAmplParetoReferencia=rutaEstadisitcas+"graficasAmpl/";
        descripcionInstancia = insta.descripcionInstancia;

        rutaAlgoritmo = rutaSalida + nombreAlgoritmo + "/";


        File directory = new File(rutaSalida);
        if (!directory.exists()) {

            System.out.println("NO EXISTE LA RUTA " + rutaSalida);

        }
        directory = new File(rutaAlgoritmo);
        if (!directory.exists()) {

            System.out.println("NO EXISTE LA RUTA " + rutaAlgoritmo);

        }

        directory = new File(rutaEstadisitcas);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+rutaEstadisitcas);
        }

        directory = new File(rutaHyperRelativoLocal);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+rutaHyperRelativoLocal);
        }

        directory = new File(graficasAmplParetoReferencia);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+graficasAmplParetoReferencia);
        }

        RUTAparetosEjecucion =  rutaEstadisitcas+"PARETOSEJECUCIONES/";
        directory = new File(RUTAparetosEjecucion);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+RUTAparetosEjecucion);
        }

        try {
            //genero y guardo los nombres de todos los archivos de salida del experimento
            //con esos nombres ahora levanto los paretos del filesystem para hacer el pareto de referencia
            //luego con esos nombres se construyen script en R para graficar y calcular hypervolumen
            nombreEjecucion_rutaTotalPareto = new HashMap<>();
            for (int iteracion = 0; iteracion < CANT_ITERACIONES; iteracion++) {
                String nombreArchivoEjecucion = nombreAlgoritmo + "I" + iteracion + ".pr";
                nombreEjecucion_rutaTotalPareto.put(nombreAlgoritmo + "I" + iteracion,RUTAparetosEjecucion+nombreArchivoEjecucion);

            }//for
            //guardo el hasmap de nombres de los archivitos de cada ejecucion para calcular hypervolume contra pareto general
            algoritmo_nombreEjecucion_rutaTotalPareto.put(nombreAlgoritmo,nombreEjecucion_rutaTotalPareto);

            this.nombreAlgoritmo_rutaTotalPareto.put(nombreAlgoritmo, RUTAparetosEjecucion + nombreAlgoritmo+"pareto.fp");



        } catch (Exception e) {
            System.err.println(nombreAlgoritmo);
            e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
        }
    }






    private void construirScriptRhypervolume() {

        String[] rutaSAlidaAltoNivel = rutaSalida.split("salida");
        String salidaGeneral = rutaSAlidaAltoNivel[0] + "salida/";


        List<String> archivo1 = new ArrayList<>();

        String NOMBRE_PARETO_REFERENCIA_GRANDE = this.nombreArchiboParetoReferecnciaGLOBAL;
        String NOMBRE_RUTA_PARETO_REFERENCIA_GRANDE = this.rutaEstadisitcas + NOMBRE_PARETO_REFERENCIA_GRANDE;


        archivo1.add("#correr este script para distintas instancias y acumula los valores en los archivos correspondientes en la carpteta " + salidaGeneral + "hypervolume/");
        archivo1.add("library(emoa)");
        archivo1.add(NOMBRE_PARETO_REFERENCIA_GRANDE.replace(".", "") + " <- read.csv('" + NOMBRE_RUTA_PARETO_REFERENCIA_GRANDE + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
        archivo1.add("matrizzz = as.matrix(" + NOMBRE_PARETO_REFERENCIA_GRANDE.replace(".", "") + ")");
        archivo1.add("traspuesta<-t(matrizzz)");

        archivo1.add("maximaX<-" + maximoQoS);
        archivo1.add("maximaY<-" + maximoCosto);


        archivo1.add("hypervolumenReferencia<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))");


        HashMap<String, String> nombreCombinacion_rutaCombinacion = new HashMap<>();
        HashMap<String, String> nombreCombinacion_rutaCombinacionLocal = new HashMap<>();

        //para todos los arlgoritmo agrego todas sus ejecuciones
        for (Map.Entry<String, HashMap<String, String>> algoritmo : this.algoritmo_nombreEjecucion_rutaTotalPareto.entrySet()) {


            HashMap<String, String> nombreEjecucion_rutaTotalParetoLocal = algoritmo.getValue();
            String nombreAlgoritmo = algoritmo.getKey();
            //me quiero quedar solo con la parte del nombre que representa la combinacion de parametros
            String[] nombreSpliteado = nombreAlgoritmo.split("Q");
            String nombreCombinacionParametros = nombreSpliteado[1];
            String nombreArchivoResultadosHypervolume = "hypervolumeRelativo_" + nombreCombinacionParametros + ".csv";
            String nombreArchivoSalidaR = salidaGeneral + "hypervolume/" + nombreArchivoResultadosHypervolume;
            String nombreArchivoSalidaRLOCALINSTANCIA = rutaHyperRelativoLocal + nombreArchivoResultadosHypervolume;


            if (!nombreCombinacion_rutaCombinacion.containsKey(nombreCombinacionParametros)) {
                nombreCombinacion_rutaCombinacion.put(nombreCombinacionParametros, nombreArchivoSalidaR);
            }

            if (!nombreCombinacion_rutaCombinacionLocal.containsKey(nombreCombinacionParametros)) {
                nombreCombinacion_rutaCombinacionLocal.put(nombreCombinacionParametros, nombreArchivoSalidaRLOCALINSTANCIA);
            }

            //meto el paretito de cada iteracion
            for (Map.Entry<String, String> ejecucion : nombreEjecucion_rutaTotalParetoLocal.entrySet()) {
                String nombreEjecucion = ejecucion.getKey();
                String rutaNombreParetoEjecucion = ejecucion.getValue();
                archivo1.add("\n");
                archivo1.add(nombreEjecucion + " <- read.csv('" + rutaNombreParetoEjecucion + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");


                archivo1.add("matrizzz = as.matrix(" + nombreEjecucion + ")");
                archivo1.add("traspuesta<-t(matrizzz)");
                archivo1.add("hypervolumen" + nombreEjecucion + "<-dominated_hypervolume(traspuesta,c(maximaY,maximaX))");

                //calculando hypervolumen
                archivo1.add("hypervolumenRelativo" + nombreEjecucion + "<-hypervolumen" + nombreEjecucion + "/hypervolumenReferencia");

                //guarda en archivo
                archivo1.add("escribir <- paste(c(\"" + nombreEjecucion + "\", hypervolumenRelativo" + nombreEjecucion + "), collapse = '" + separadorColumnasArchivo + "')");
                archivo1.add("write(escribir, file = '" + nombreArchivoSalidaR + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");
                archivo1.add("write(escribir, file = '" + nombreArchivoSalidaRLOCALINSTANCIA + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");


            }

            archivo1.add("\n");

        }

        boolean appendMode = false;
        Archivos ar = new Archivos();
        ar.writeLargerTextFile(rutaEstadisitcas + "hypervolume_TODOS_ContraReferencia.r", archivo1, appendMode);



            /*PROMEDIO*/
        String nombreArchivoSalidaPromedios = salidaGeneral + "hypervolume/promediosHypervolume.csv";
        List<String> archivo2 = new ArrayList<>();
        for (Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();

            archivo2.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
            archivo2.add(nombreCombinacion + "_mean" + " <- mean(" + nombreCombinacion + "[,2])");
            archivo2.add(nombreCombinacion + "_sd" + " <- sd(" + nombreCombinacion + "[,2])");
            archivo2.add("escribir <- paste(c(\"" + nombreCombinacion + "\"," + nombreCombinacion + "_mean), collapse = '" + separadorColumnasArchivo + "')");
            archivo2.add("escribir <- paste(c(escribir," + nombreCombinacion + "_sd), collapse = '" + separadorColumnasArchivo + "')");
            archivo2.add("write(escribir, file = '" + nombreArchivoSalidaPromedios + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");

            archivo2.add("\n");


        }

        ar.writeLargerTextFile(rutaEstadisitcas + "promedios_hypervolume_TODOS_ContraReferencia.r", archivo2, appendMode);




            /*MEDIANA*/
        String nombreArchivoSalidaMedianas = salidaGeneral + "hypervolume/medianasHypervolume.csv";
        archivo2 = new ArrayList<>();
        for (Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();

            archivo2.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
            archivo2.add(nombreCombinacion + "_median" + " <- median(" + nombreCombinacion + "[,2])");

            archivo2.add("escribir <- paste(c(\"" + nombreCombinacion + "\"," + nombreCombinacion + "_median), collapse = '" + separadorColumnasArchivo + "')");

            archivo2.add("write(escribir, file = '" + nombreArchivoSalidaMedianas + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");

            archivo2.add("\n");


        }

        ar.writeLargerTextFile(rutaEstadisitcas + "medianas_hypervolume_TODOS_ContraReferencia.r", archivo2, appendMode);








            /*TEST NORMALIDAD*/
        String nombreArchivoSalidaKS = salidaGeneral + "hypervolume/normalidadKS.csv";
        List<String> archivo3 = new ArrayList<>();
        archivo3.add("library(nortest)");
        archivo3.add("library(ggplot2);library(reshape2)");
        String vectorDescripcionYdatosGrafica = "";
        boolean primero = true;
        for (Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();


            archivo3.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
            archivo3.add(nombreCombinacion + "_pvalue" + " <- lillie.test(" + nombreCombinacion + "[,2])$p.value");

            archivo3.add("escribir <- paste(c(\"" + nombreCombinacion + "\"," + nombreCombinacion + "_pvalue), collapse = '" + separadorColumnasArchivo + "')");
            archivo3.add("write(escribir, file = '" + nombreArchivoSalidaKS + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");

            archivo3.add("\n");

            archivo3.add("jpeg('" + salidaGeneral + "hypervolume/densidad" + nombreCombinacion + ".jpg')");
            archivo3.add("plot(density(" + nombreCombinacion + "[,2]))");
            archivo3.add("dev.off()");
            if (primero) {
                vectorDescripcionYdatosGrafica = " x <- data.frame(" + nombreCombinacion + "=" + nombreCombinacion + "[,2]";
                primero = false;
            } else {
                vectorDescripcionYdatosGrafica += "," + nombreCombinacion + "=" + nombreCombinacion + "[,2]";
            }


        }
        archivo3.add("\n");
        archivo3.add("\n");
        archivo3.add("\n");
        vectorDescripcionYdatosGrafica += ")";
        archivo3.add(vectorDescripcionYdatosGrafica);
        archivo3.add("data<- melt(x)");
        archivo3.add("jpeg('" + salidaGeneral + "hypervolume/comparaDensidadRelleno.jpg')");
        archivo3.add("ggplot(data,aes(x=value, fill=variable)) + geom_density(alpha=0.25)");
        archivo3.add("dev.off()");

        archivo3.add("jpeg('" + salidaGeneral + "hypervolume/comparaDensidadHistorgrama.jpg')");
        archivo3.add("ggplot(data,aes(x=value, fill=variable)) + geom_histogram(alpha=0.25)");
        archivo3.add("dev.off()");

        archivo3.add("jpeg('" + salidaGeneral + "hypervolume/comparaDensidadboxplot.jpg')");
        archivo3.add("ggplot(data,aes(x=variable, y=value, fill=variable)) + geom_boxplot()");
        archivo3.add("dev.off()");


        ar.writeLargerTextFile(rutaEstadisitcas + "normalidadKS_hypervolume_TODOS_ContraReferencia.r", archivo3, appendMode);









              /*TEST DE FRIEDMAN*/
        String nombreArchivoSalidaFriedman = salidaGeneral + "hypervolume/friedman.csv";
        List<String> archivo4 = new ArrayList<>();

        String vectorNombresComillas = "";
        String vectorNombresSinComillas = "";
        primero = true;
        for (Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();


            archivo4.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");


            if (primero) {
                vectorNombresComillas = "\"" + nombreCombinacion + "\"";
                vectorNombresSinComillas = nombreCombinacion + "[,2]";
                primero = false;
            } else {
                vectorNombresComillas += "," + "\"" + nombreCombinacion + "\"";
                vectorNombresSinComillas += "," + nombreCombinacion + "[,2]";
            }


        }

        archivo4.add("X <- data.frame(");
        archivo4.add("		  Y = c(" + vectorNombresSinComillas + " ),");
        archivo4.add("		  Z = factor(rep(c(" + vectorNombresComillas + "), 150)),");
        archivo4.add("		  Yr = factor(rep(1:150, rep(27, 150))))");
        archivo4.add("\n");
        archivo4.add("jpeg('" + salidaGeneral + "hypervolume/boxplotingFriedman.jpg')");
        archivo4.add("	with(X , boxplot( Y  ~ Z )) # boxploting");
        archivo4.add("dev.off()");
        archivo4.add("	friedman.test(Y ~ Z | Yr ,X)");


        archivo4.add(" matrizGuardar <-");
        archivo4.add("        matrix(c(" + vectorNombresSinComillas + "),");
        archivo4.add("nrow = 150,  ");
        archivo4.add("        byrow = TRUE,");
        archivo4.add("        dimnames = list(1 : 150,");
        archivo4.add("        c(" + vectorNombresComillas + ")))");
        archivo4.add(" write.table(matrizGuardar, file='" + salidaGeneral + "hypervolume/matrizHypervolumenRelativo.csv', row.names=FALSE ,sep ='\\t')");


        ar.writeLargerTextFile(rutaEstadisitcas + "friedman_hypervolume_TODOS_ContraReferencia.r", archivo4, appendMode);





             /*TEST DE FRIEDMAN LOCAL*/

        archivo4 = new ArrayList<>();

        vectorNombresComillas = "";
        vectorNombresSinComillas = "";
        primero = true;
        for (Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacionLocal.entrySet()) {
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();


            archivo4.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");


            if (primero) {
                vectorNombresComillas = "\"" + nombreCombinacion + "\"";
                vectorNombresSinComillas = nombreCombinacion + "[,2]";
                primero = false;
            } else {
                vectorNombresComillas += "," + "\"" + nombreCombinacion + "\"";
                vectorNombresSinComillas += "," + nombreCombinacion + "[,2]";
            }


        }

        archivo4.add("X <- data.frame(");
        archivo4.add("		  Y = c(" + vectorNombresSinComillas + " ),");
        archivo4.add("		  Z = factor(rep(c(" + vectorNombresComillas + "), 50)),");
        archivo4.add("		  Yr = factor(rep(1:50, rep(27, 50))))");
        archivo4.add("\n");
        archivo4.add("jpeg('" + rutaHyperRelativoLocal + "boxplotingFriedmanInstancia.jpg')");
        archivo4.add("	with(X , boxplot( Y  ~ Z )) # boxploting");
        archivo4.add("dev.off()");
        archivo4.add("	friedman.test(Y ~ Z | Yr ,X)");


        archivo4.add(" matrizGuardar <-");
        archivo4.add("        matrix(c(" + vectorNombresSinComillas + "),");
        archivo4.add("nrow = 50,  ");
        archivo4.add("        byrow = TRUE,");
        archivo4.add("        dimnames = list(1 : 50,");
        archivo4.add("        c(" + vectorNombresComillas + ")))");
        archivo4.add(" write.table(matrizGuardar, file='" + rutaHyperRelativoLocal + "/matrizHypervolumenRelativoInstancia.csv', row.names=FALSE ,sep ='\\t')");


        ar.writeLargerTextFile(rutaHyperRelativoLocal + "friedman_hypervolume_TODOS_ContraReferenciaInstancia.r", archivo4, appendMode);











              /*TEST DE KRUSCAL-WLLIS*/
        String nombreArchivoSalidaKRUSCAL = salidaGeneral + "hypervolume/KRUSCAL.csv";
        String nombreArchivoSumarize = salidaGeneral + "hypervolume/Sumarize.csv";
        List<String> archivo5 = new ArrayList<>();


        vectorNombresSinComillas = "";
        primero = true;
        for (Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();


            archivo5.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");


            if (primero) {
                archivo5.add("write('Configuracion;Min.;1stQu.;Median;Mean;3rdQu.;Max.', file = '" + nombreArchivoSumarize + "',ncolumns = 1,append = TRUE, sep =' ')");
                vectorNombresSinComillas = nombreCombinacion + "=" + nombreCombinacion + "[,2]";
                primero = false;
            } else {

                vectorNombresSinComillas += "," + nombreCombinacion + "=" + nombreCombinacion + "[,2]";
            }

            archivo5.add("sumo<-summary(" + nombreCombinacion + "[,2])");

            archivo5.add("escribir <- paste(c('" + nombreCombinacion + "',sumo), collapse = ' ')");
            archivo5.add("write(escribir, file = '" + nombreArchivoSumarize + "',ncolumns = 1,append = TRUE, sep =' ')");


        }


        archivo5.add("dati = list(" + vectorNombresSinComillas + ")");


        archivo5.add("kruskal.test(dati)");


        ar.writeLargerTextFile(rutaEstadisitcas + "KRUSCAL_hypervolume_TODOS_ContraReferencia.r", archivo5, appendMode);


//
//                /*TEST DE KRUSCAL-WLLIS*/
//        String nombreArchivoSalidaKRUSCALLocal= salidaGeneral +"hypervolume/KRUSCAL.csv";
//        nombreArchivoSumarize= rutaHyperRelativoLocal +"SumarizeInstancia.csv";
//        archivo5 = new ArrayList<>();
//
//
//        vectorNombresSinComillas="";
//        primero = true;
//        for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacionLocal.entrySet()) {
//            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
//            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();
//
//
//            archivo5.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");
//
//
//
//            if(primero) {
//                archivo5.add("write('Configuracion;Min.;1stQu.;Median;Mean;3rdQu.;Max.', file = '"+nombreArchivoSumarize+"',ncolumns = 1,append = TRUE, sep =' ')");
//                vectorNombresSinComillas = nombreCombinacion+"="+ nombreCombinacion + "[,2]" ;
//                primero=false;
//            }else{
//
//                vectorNombresSinComillas += ","+nombreCombinacion+"="+ nombreCombinacion + "[,2]" ;
//            }
//
//            archivo5.add("sumo<-summary("+ nombreCombinacion + "[,2])");
//
//            archivo5.add("escribir <- paste(c('"+ nombreCombinacion +"',sumo), collapse = ' ')");
//            archivo5.add("write(escribir, file = '"+nombreArchivoSumarize+"',ncolumns = 1,append = TRUE, sep =' ')");
//
//
//        }
//
//
//        archivo5.add("dati = list("+vectorNombresSinComillas+")");
//
//
//        archivo5.add("kruskal.test(dati)");
//
//
//
//        ar.writeLargerTextFile(rutaHyperRelativoLocal + "KRUSCAL_hypervolume_TODOS_ContraReferenciaInstancia.r", archivo5, appendMode);
//






                /*TEST DE KRUSCAL-WLLIS*/
        String nombreArchivoSalidaKRUSCALLocal2a2 = salidaGeneral + "hypervolume/KRUSCAL2a2.csv";

        archivo5 = new ArrayList<>();

        String []vectorNombres1 = new String[nombreCombinacion_rutaCombinacionLocal.size()];

        int posic=-1;
        for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacionLocal.entrySet()) {
            posic++;
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();
            archivo5.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");
            vectorNombres1[posic]=nombreCombinacion;

        }



        //planacho los valores quequiero
        vectorNombres1 = new String[4];
        posic=3;
        vectorNombres1[0]="M001C05G1000P200";
        vectorNombres1[1]="M0001C05G1000P200";
        vectorNombres1[2]="M0001C07G1000P200";
        vectorNombres1[3]="M001C07G1000P200";










        for(int posicFuera =0;posicFuera<posic;posicFuera++) {
            String nombreCombinacionFuera =  vectorNombres1[posicFuera];
            vectorNombresSinComillas = "";
            for(int posicDentro =posicFuera+1;posicDentro<=posic;posicDentro++){
                String nombreCombinacion = vectorNombres1[posicDentro];

                    vectorNombresSinComillas = nombreCombinacion + "=" + nombreCombinacion + "[,2]" + "," + nombreCombinacionFuera + "=" + nombreCombinacionFuera + "[,2]";
                    archivo5.add("dati = list("+vectorNombresSinComillas+")");
                    archivo5.add("resultadoTest<-kruskal.test(dati)");
                archivo5.add("escribir <- paste(c('" + nombreCombinacion +" "+nombreCombinacionFuera+ "',resultadoTest$p.value), collapse = ' ')");
                archivo5.add("write(escribir, file = '" + nombreArchivoSalidaKRUSCALLocal2a2 + "',ncolumns = 1,append = TRUE, sep =' ')");

            }
        }
        ar.writeLargerTextFile(rutaHyperRelativoLocal + "KRUSCAL_hypervolume_TODOS_ContraReferenciaInstancia2a2.r", archivo5, appendMode);




//KRUSCAL TODOS GRANDE
        String nombreArchivoSalidaPromediosKRUSCAL_TODOS = salidaGeneral + "hypervolume/KRUSCAL_TODOS.csv";

        archivo5 = new ArrayList<>();

        vectorNombres1 = new String[nombreCombinacion_rutaCombinacion.size()];

         posic=-1;
        for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
            posic++;
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();
            archivo5.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");
            vectorNombres1[posic]=nombreCombinacion;

        }











        for(int posicFuera =0;posicFuera<posic;posicFuera++) {
            String nombreCombinacionFuera =  vectorNombres1[posicFuera];
            vectorNombresSinComillas = "";
            for(int posicDentro =posicFuera+1;posicDentro<=posic;posicDentro++){
                String nombreCombinacion = vectorNombres1[posicDentro];

                vectorNombresSinComillas = nombreCombinacion + "=" + nombreCombinacion + "[,2]" + "," + nombreCombinacionFuera + "=" + nombreCombinacionFuera + "[,2]";
                archivo5.add("dati = list("+vectorNombresSinComillas+")");
                archivo5.add("resultadoTest<-kruskal.test(dati)");
                archivo5.add("escribir <- paste(c('" + nombreCombinacion +" "+nombreCombinacionFuera+ "',resultadoTest$p.value), collapse = ' ')");
                archivo5.add("write(escribir, file = '" + nombreArchivoSalidaPromediosKRUSCAL_TODOS + "',ncolumns = 1,append = TRUE, sep =' ')");

            }
        }
        ar.writeLargerTextFile(rutaHyperRelativoLocal + "hypervolume_KRUSCAL_TODOS2a2.r", archivo5, appendMode);








    }

    private void construirScriptRGraficas() {
        try {
            List<String> grafica1 = new ArrayList<>();

            String NOMBRE_PARETO_REFERENCIA_GRANDE=this.nombreArchiboParetoReferecnciaGLOBAL;
            String NOMBRE_RUTA_PARETO_REFERENCIA_GRANDE=this.rutaEstadisitcas+this.nombreArchiboParetoReferecnciaGLOBAL;
            grafica1.add(NOMBRE_PARETO_REFERENCIA_GRANDE + " <- read.csv('" + NOMBRE_RUTA_PARETO_REFERENCIA_GRANDE + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");



            for (Map.Entry<String, String> entry : this.nombreAlgoritmo_rutaTotalPareto.entrySet()) {

                //pareto referencia del cada algoritmo
                String nombreAlgoritmo = entry.getKey();
                String rutaNombreParetoReferenciaAlgoritmo = entry.getValue();
                grafica1.add(nombreAlgoritmo + " <- read.csv('" + rutaNombreParetoReferenciaAlgoritmo + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
            }
            grafica1.add("\n");


            grafica1.add("plot(" + NOMBRE_PARETO_REFERENCIA_GRANDE + " ,xlab='Costo', ylab='RTT (ms)', pch=19,col='red',xlim=range(0," + maximoCosto + "), ylim=range(0," + maximoQoS + "))");
            String vectorTipoPuntoReferencias = "19";
            String vectorColorReferencias = "\"red\"";
            String vectorTextoReferencias = "\"FP Referencia\"";

            for (Map.Entry<String, String> entry : this.nombreAlgoritmo_rutaTotalPareto.entrySet()) {
                String nombreAlgoritmo = entry.getKey();
                grafica1.add("points(" + nombreAlgoritmo + ",col=\"green\", pch=19)");
                vectorTipoPuntoReferencias += ",19";
                vectorColorReferencias += ",\"green\"";
                vectorTextoReferencias += ",\"" + nombreAlgoritmo + "\"";
            }



            grafica1.add("\n");
            grafica1.add("legend(");
            grafica1.add("        \"topright\",");
            grafica1.add("        pch=c(" + vectorTipoPuntoReferencias + "),");
            grafica1.add("        col=c(" + vectorColorReferencias + "),");
            grafica1.add("        legend = c(" + vectorTextoReferencias + ")");
            grafica1.add(")");
            boolean appendMode = false;
            Archivos ar = new Archivos();
            ar.writeLargerTextFile(rutaEstadisitcas + "graficas_paretoReferencia_paretosAlgoritmos.r", grafica1, appendMode);




            //cada algoritmo contra el ampl
            for (Map.Entry<String, String> entry : this.nombreAlgoritmo_rutaTotalPareto.entrySet()) {
                List<String> grafica2 = new ArrayList<>();
                String nombreAlgoritmo = entry.getKey();
                String rutaNombreParetoReferenciaAlgoritmo = entry.getValue();
                grafica2.add(nombreAlgoritmo + " <- read.csv('" + rutaNombreParetoReferenciaAlgoritmo + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
                grafica2.add("plot(" + nombreAlgoritmo + " ,xlab='Costo', ylab='RTT (ms)', pch=19,col='red',xlim=range(0," + maximoCosto + "), ylim=range(0," + maximoQoS + "))");


                vectorTipoPuntoReferencias = "19";
                vectorColorReferencias = "\"red\"";
                vectorTextoReferencias = "\""+nombreAlgoritmo+"\"";
                String ampl = "AMPL";
                String amplRuta = this.rutaEstadisitcas+"AMPL.pr";
                grafica2.add(ampl + " <- read.csv('" + amplRuta + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");

                grafica2.add("points(" + ampl + ",col=\"green\", pch=19)");
                vectorTipoPuntoReferencias += ",19";
                vectorColorReferencias += ",\"green\"";
                vectorTextoReferencias += ",\"AMPL\"";


                grafica2.add("\n");
                grafica2.add("legend(");
                grafica2.add("        \"topright\",");
                grafica2.add("        pch=c(" + vectorTipoPuntoReferencias + "),");
                grafica2.add("        col=c(" + vectorColorReferencias + "),");
                grafica2.add("        legend = c(" + vectorTextoReferencias + ")");
                grafica2.add(")");
                ar.writeLargerTextFile(graficasAmplParetoReferencia + "graficas_AMPL_paretoReferencia_"+nombreAlgoritmo+".r", grafica2, appendMode);

            }

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


        } catch (Exception e) {
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

        return instanciaUtilizada+muta+cross+generacions+tamaniopoblacion;
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
