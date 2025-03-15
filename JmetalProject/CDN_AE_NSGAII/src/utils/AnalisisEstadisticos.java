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
public class AnalisisEstadisticos {

    public AnalisisEstadisticos() {
    }


    List<double[]> global_solutionSet = new ArrayList<>();
    List<double[]> global_solutionSet_soloParetos = new ArrayList<>();
    List<double[]> algoritmoGlobal_solutionSet;

    HashMap<String,  HashMap<String, String>> algoritmo_nombreEjecucion_rutaTotalPareto;

    String rutaEstadisitcas;
    String nombreArchiboParetoReferecnciaGLOBAL = "referenciaAE.pr";


    HashMap<String, String> nombreEjecucion_rutaTotalPareto;

    String rutaHyperRelativoLocal;






    List<List<double []>> paretosSolucionesSeparados = new ArrayList<>();
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
            String rutaAMPL="/home/ggoni/Documentos/proye/proycodigo/AMPL/paretos/";




            AnalisisEstadisticos eje = new AnalisisEstadisticos();
            // JMetalLogger.configureLoggers(new File(System.getProperty("user.dir")+"/CDN_AE_NSGAII/src/resources/propertiesdir/"+"configLogger.properties"));
            //String nombreArchivoPropertiesInstancia ="expe_tandaA1_500Vid_300Mins_6Prove";
            //String nombreArchivoPropertiesInstanciaAMPL ="cdntandaA1_500Vid_300Mins_6Prove_1XreG_1XreQ_pareto";
            //String nombreArchivoPropertiesInstancia ="expe_tandaD2_4000Vid_480Mins_7Prove";
            //String nombreArchivoPropertiesInstanciaAMPL ="NO_TIENE";
          //String nombreArchivoPropertiesInstancia ="expe_tandaD1_400Vid_300Mins_5Prove";
          //String nombreArchivoPropertiesInstanciaAMPL ="cdntandaD1_400Vid_300Mins_5Prove_1XreG_1XreQ_pareto";
            String nombreArchivoPropertiesInstancia ="expe_tandaC1_1000Vid_240Mins_7Prove";
            String nombreArchivoPropertiesInstanciaAMPL ="cdntandaC1_1000Vid_240Mins_7Prove_1XreG_1XreQ_pareto";
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

            eje.levantarDatosAmplToPareto(rutaAMPL);

            insta.leerPropertiesInstancia(eje.instanciaUtilizadaExperimento);
            eje.rutaSalida=insta.getRutaAbsolutaSalidaInstancia();
            eje.descripcionInstancia= insta.descripcionInstancia;
            eje.algoritmo_nombreEjecucion_rutaTotalPareto = new HashMap<>();

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
            System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Construyendo pareto grande de referencia");





            /******************/
            //AGREGO EL PARETO DEL AMPL AL PARETO DE REFERENCIA PARA METER UN POCO DE RUIDO
            if(nombreArchivoPropertiesInstanciaAMPL!="NO_TIENE") {

                List<double[]> solucionesAMPL = eje.leerParetoDeArchvo(nombreArchivoPropertiesInstanciaAMPL + ".sal", rutaAMPL);
                eje.global_solutionSet_soloParetos.addAll(solucionesAMPL);
                eje.global_solutionSet.addAll(solucionesAMPL);
                System.err.println("Fin cargada desde ampl...");
            }else{
                System.err.println("No tiene frente pareto en ampl...");
            }
            /*************************/









            List<double[]> paretoGeneral = eje.paretera(eje.global_solutionSet);
           // List<double[]> paretoGeneral = eje.paretera(eje.global_solutionSet_soloParetos);
            eje.guardarParetoEnArchvo(paretoGeneral, eje.nombreArchiboParetoReferecnciaGLOBAL, eje.rutaEstadisitcas);
            eje.nombreAlgoritmo_rutaTotalPareto.put(eje.instanciaUtilizadaExperimento+"PARETO", eje.rutaEstadisitcas+eje.nombreArchiboParetoReferecnciaGLOBAL);



            //generar script en R de hypervolumen
            eje.construirScriptRhypervolume();
            eje.construirScriptRGraficas();



            //solo genero una vez el greedy
            if((args.length==0) || ( args.length!=0 && idinferiorAlgoritmoEjecutar==0)) {
               // eje.correrGreedy(eje.rutaSalida, eje.instanciaUtilizadaExperimento);
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
        algoritmoGlobal_solutionSet = new ArrayList<>();

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
                System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+" Levantando archivo "+rutaAlgoritmo+nombreArchivoEjecucion);
                List<double[]> paretoArchivo = this.leerParetoDeArchvo(nombreArchivoEjecucion, rutaAlgoritmo);
                this.guardarParetoEnArchvo(paretoArchivo, nombreArchivoEjecucion, RUTAparetosEjecucion);
                this.acumularParetosSoluciones(paretoArchivo, iteracion);
            }//for
            //guardo el hasmap de nombres de los archivitos de cada ejecucion para calcular hypervolume contra pareto general
            algoritmo_nombreEjecucion_rutaTotalPareto.put(nombreAlgoritmo,nombreEjecucion_rutaTotalPareto);
            //obtener pareto global del algoritmo y pasarlo al resto de funciones
            List<double[]> paretoDelAlgoritmo = this.paretera(algoritmoGlobal_solutionSet);
            //guardo el paretito del algoritmo para calcular el pareto de referencia despues
            global_solutionSet_soloParetos.addAll(paretoDelAlgoritmo);
            this.guardarParetoEnArchvo(paretoDelAlgoritmo, nombreAlgoritmo+"pareto.fp", RUTAparetosEjecucion);
            this.nombreAlgoritmo_rutaTotalPareto.put(nombreAlgoritmo, RUTAparetosEjecucion + nombreAlgoritmo+"pareto.fp");



        } catch (Exception e) {
            System.err.println(nombreAlgoritmo);
            e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
        }
    }

    private List<double[]> leerParetoDeArchvo(String nombreArchivo,
                                              String ruta) {
        List<double[]> pareto = new ArrayList<>();
        Archivos ar = new Archivos();
        List<String> lineasPareto = ar.readLargerTextFileAlternate(ruta + nombreArchivo);
        String[] pareja_fila;
        for (String valorSoluco : lineasPareto) {
            pareja_fila = valorSoluco.split(separadorColumnasArchivo);
            double[] valorSolucion = new double[2];
            valorSolucion[0]=Double.valueOf(pareja_fila[0]) ;
            valorSolucion[1]=Double.valueOf(pareja_fila[1]) ;
            pareto.add(valorSolucion);
        }
        //transformar salida del algoritmo guarada en un pareto real
        pareto = this.paretera(pareto);

        return pareto;
    }
    private  void guardarParetoEnArchvo(List<double []> pareto,
                                        String nombreArchivo,
                                        String ruta){
        List<String> aLines = new ArrayList<>();
        for (double [] valorSoluco:pareto) {
            aLines.add(String.valueOf(valorSoluco[0])+separadorColumnasArchivo+String.valueOf(valorSoluco[1]));
        }
        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"Guardando pareto "+ruta+nombreArchivo);

        boolean appendMode = false;
        Archivos ar = new Archivos();
        ar.writeLargerTextFile(ruta+nombreArchivo,aLines,appendMode);
        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+": "+"FIN Guardando pareto "+ruta+nombreArchivo);
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

                archivo1.add("maximaX<-"+maximoQoS);
                archivo1.add("maximaY<-"+maximoCosto);


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
            String nombreArchivoSalidaPromedios= salidaGeneral +"hypervolume/promediosHypervolume.csv";
            List<String> archivo2 = new ArrayList<>();
            for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
                String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
                String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();

                archivo2.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
                archivo2.add(nombreCombinacion+"_mean" + " <- mean(" + nombreCombinacion + "[,2])");
                archivo2.add(nombreCombinacion+"_sd" + " <- sd(" + nombreCombinacion + "[,2])");
                archivo2.add("escribir <- paste(c(\""+ nombreCombinacion +"\","+ nombreCombinacion+"_mean), collapse = '" + separadorColumnasArchivo + "')");
                archivo2.add("escribir <- paste(c(escribir,"+ nombreCombinacion+"_sd), collapse = '" + separadorColumnasArchivo + "')");
                archivo2.add("write(escribir, file = '" + nombreArchivoSalidaPromedios + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");

                archivo2.add("\n");



            }

            ar.writeLargerTextFile(rutaEstadisitcas + "promedios_hypervolume_TODOS_ContraReferencia.r", archivo2, appendMode);




            /*MEDIANA*/
            String nombreArchivoSalidaMedianas= salidaGeneral +"hypervolume/medianasHypervolume.csv";
           archivo2 = new ArrayList<>();
            for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
                String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
                String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();

                archivo2.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
                archivo2.add(nombreCombinacion+"_median" + " <- median(" + nombreCombinacion + "[,2])");

                archivo2.add("escribir <- paste(c(\""+ nombreCombinacion +"\","+ nombreCombinacion+"_median), collapse = '" + separadorColumnasArchivo + "')");

                archivo2.add("write(escribir, file = '" + nombreArchivoSalidaMedianas + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");

                archivo2.add("\n");



            }

            ar.writeLargerTextFile(rutaEstadisitcas + "medianas_hypervolume_TODOS_ContraReferencia.r", archivo2, appendMode);








            /*TEST NORMALIDAD*/
            String nombreArchivoSalidaKS= salidaGeneral +"hypervolume/normalidadKS.csv";
            List<String> archivo3 = new ArrayList<>();
            archivo3.add("library(nortest)");
            archivo3.add("library(ggplot2);library(reshape2)");
            String vectorDescripcionYdatosGrafica="";
            boolean primero = true;
            for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
                String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
                String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();


                archivo3.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "',header =FALSE)");
                archivo3.add(nombreCombinacion+"_pvalue" + " <- lillie.test(" + nombreCombinacion + "[,2])$p.value");

                archivo3.add("escribir <- paste(c(\""+ nombreCombinacion +"\","+ nombreCombinacion+"_pvalue), collapse = '" + separadorColumnasArchivo + "')");
                archivo3.add("write(escribir, file = '" + nombreArchivoSalidaKS + "',ncolumns = 1,append = TRUE, sep ='" + separadorColumnasArchivo + "')");

                archivo3.add("\n");

                archivo3.add("jpeg('"+salidaGeneral +"hypervolume/densidad"+nombreCombinacion+".jpg')");
                archivo3.add("plot(density(" + nombreCombinacion + "[,2]))");
                archivo3.add("dev.off()");
                if(primero) {
                    vectorDescripcionYdatosGrafica = " x <- data.frame("+nombreCombinacion + "=" + nombreCombinacion + "[,2]";
                    primero=false;
                }else{
                    vectorDescripcionYdatosGrafica += ","+nombreCombinacion + "=" + nombreCombinacion + "[,2]";
                }



            }
            archivo3.add("\n");
            archivo3.add("\n");
            archivo3.add("\n");
            vectorDescripcionYdatosGrafica+=")";
            archivo3.add(vectorDescripcionYdatosGrafica);
            archivo3.add("data<- melt(x)");
            archivo3.add("jpeg('"+salidaGeneral +"hypervolume/comparaDensidadRelleno.jpg')");
            archivo3.add("ggplot(data,aes(x=value, fill=variable)) + geom_density(alpha=0.25)");
            archivo3.add("dev.off()");

            archivo3.add("jpeg('"+salidaGeneral +"hypervolume/comparaDensidadHistorgrama.jpg')");
            archivo3.add("ggplot(data,aes(x=value, fill=variable)) + geom_histogram(alpha=0.25)");
            archivo3.add("dev.off()");

            archivo3.add("jpeg('"+salidaGeneral +"hypervolume/comparaDensidadboxplot.jpg')");
            archivo3.add("ggplot(data,aes(x=variable, y=value, fill=variable)) + geom_boxplot()");
            archivo3.add("dev.off()");



            ar.writeLargerTextFile(rutaEstadisitcas + "normalidadKS_hypervolume_TODOS_ContraReferencia.r", archivo3, appendMode);









              /*TEST DE FRIEDMAN*/
            String nombreArchivoSalidaFriedman= salidaGeneral +"hypervolume/friedman.csv";
           List<String> archivo4 = new ArrayList<>();

            String vectorNombresComillas="";
            String vectorNombresSinComillas="";
             primero = true;
            for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
                String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
                String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();


                archivo4.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");



                if(primero) {
                    vectorNombresComillas = "\""+nombreCombinacion + "\"";
                    vectorNombresSinComillas = nombreCombinacion + "[,2]" ;
                    primero=false;
                }else{
                    vectorNombresComillas += ","+"\""+nombreCombinacion + "\"";
                    vectorNombresSinComillas += ","+nombreCombinacion+ "[,2]" ;
                }



            }

            archivo4.add("X <- data.frame(");
            archivo4.add("		  Y = c("+vectorNombresSinComillas+" ),");
            archivo4.add("		  Z = factor(rep(c("+vectorNombresComillas+"), 150)),");
            archivo4.add("		  Yr = factor(rep(1:150, rep(27, 150))))");
            archivo4.add("\n");
            archivo4.add("jpeg('"+salidaGeneral +"hypervolume/boxplotingFriedman.jpg')");
            archivo4.add("	with(X , boxplot( Y  ~ Z )) # boxploting");
            archivo4.add("dev.off()");
            archivo4.add("	friedman.test(Y ~ Z | Yr ,X)");


            archivo4.add(" matrizGuardar <-");
            archivo4.add("        matrix(c("+vectorNombresSinComillas+"),");
            archivo4.add("nrow = 150,  ");
            archivo4.add("        byrow = TRUE,");
            archivo4.add("        dimnames = list(1 : 150,");
            archivo4.add("        c("+vectorNombresComillas+")))");
            archivo4.add(" write.table(matrizGuardar, file='"+salidaGeneral +"hypervolume/matrizHypervolumenRelativo.csv', row.names=FALSE ,sep ='\\t')");




            ar.writeLargerTextFile(rutaEstadisitcas + "friedman_hypervolume_TODOS_ContraReferencia.r", archivo4, appendMode);





             /*TEST DE FRIEDMAN LOCAL*/

        archivo4 = new ArrayList<>();

         vectorNombresComillas="";
         vectorNombresSinComillas="";
        primero = true;
        for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacionLocal.entrySet()) {
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();


            archivo4.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");



            if(primero) {
                vectorNombresComillas = "\""+nombreCombinacion + "\"";
                vectorNombresSinComillas = nombreCombinacion + "[,2]" ;
                primero=false;
            }else{
                vectorNombresComillas += ","+"\""+nombreCombinacion + "\"";
                vectorNombresSinComillas += ","+nombreCombinacion+ "[,2]" ;
            }



        }

        archivo4.add("X <- data.frame(");
        archivo4.add("		  Y = c("+vectorNombresSinComillas+" ),");
        archivo4.add("		  Z = factor(rep(c("+vectorNombresComillas+"), 50)),");
        archivo4.add("		  Yr = factor(rep(1:50, rep(27, 50))))");
        archivo4.add("\n");
        archivo4.add("jpeg('"+rutaHyperRelativoLocal +"boxplotingFriedmanInstancia.jpg')");
        archivo4.add("	with(X , boxplot( Y  ~ Z )) # boxploting");
        archivo4.add("dev.off()");
        archivo4.add("	friedman.test(Y ~ Z | Yr ,X)");


        archivo4.add(" matrizGuardar <-");
        archivo4.add("        matrix(c("+vectorNombresSinComillas+"),");
        archivo4.add("nrow = 50,  ");
        archivo4.add("        byrow = TRUE,");
        archivo4.add("        dimnames = list(1 : 50,");
        archivo4.add("        c("+vectorNombresComillas+")))");
        archivo4.add(" write.table(matrizGuardar, file='"+rutaHyperRelativoLocal +"/matrizHypervolumenRelativoInstancia.csv', row.names=FALSE ,sep ='\\t')");




        ar.writeLargerTextFile(rutaHyperRelativoLocal + "friedman_hypervolume_TODOS_ContraReferenciaInstancia.r", archivo4, appendMode);











              /*TEST DE KRUSCAL-WLLIS*/
            String nombreArchivoSalidaKRUSCAL= salidaGeneral +"hypervolume/KRUSCAL.csv";
            String nombreArchivoSumarize= salidaGeneral +"hypervolume/Sumarize.csv";
            List<String> archivo5 = new ArrayList<>();


             vectorNombresSinComillas="";
            primero = true;
            for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacion.entrySet()) {
                String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
                String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();


                archivo5.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");



                if(primero) {
                    archivo5.add("write('Configuracion;Min.;1stQu.;Median;Mean;3rdQu.;Max.', file = '"+nombreArchivoSumarize+"',ncolumns = 1,append = TRUE, sep =' ')");
                    vectorNombresSinComillas = nombreCombinacion+"="+ nombreCombinacion + "[,2]" ;
                    primero=false;
                }else{

                    vectorNombresSinComillas += ","+nombreCombinacion+"="+ nombreCombinacion + "[,2]" ;
                }

                archivo5.add("sumo<-summary("+ nombreCombinacion + "[,2])");

                archivo5.add("escribir <- paste(c('"+ nombreCombinacion +"',sumo), collapse = ' ')");
                archivo5.add("write(escribir, file = '"+nombreArchivoSumarize+"',ncolumns = 1,append = TRUE, sep =' ')");


            }


            archivo5.add("dati = list("+vectorNombresSinComillas+")");


            archivo5.add("kruskal.test(dati)");



            ar.writeLargerTextFile(rutaEstadisitcas + "KRUSCAL_hypervolume_TODOS_ContraReferencia.r", archivo5, appendMode);








                /*TEST DE KRUSCAL-WLLIS*/
        String nombreArchivoSalidaKRUSCALLocal= salidaGeneral +"hypervolume/KRUSCAL.csv";
        nombreArchivoSumarize= rutaHyperRelativoLocal +"SumarizeInstancia.csv";
        archivo5 = new ArrayList<>();


        vectorNombresSinComillas="";
        primero = true;
        for(Map.Entry<String, String> archivoHypervolumeCombinacion : nombreCombinacion_rutaCombinacionLocal.entrySet()) {
            String nombreCombinacion = archivoHypervolumeCombinacion.getKey();
            String rutaArchivoCombinacion = archivoHypervolumeCombinacion.getValue();


            archivo5.add(nombreCombinacion + " <- read.csv('" + rutaArchivoCombinacion + "' , sep='" + separadorColumnasArchivo + "' , header =FALSE)");



            if(primero) {
                archivo5.add("write('Configuracion;Min.;1stQu.;Median;Mean;3rdQu.;Max.', file = '"+nombreArchivoSumarize+"',ncolumns = 1,append = TRUE, sep =' ')");
                vectorNombresSinComillas = nombreCombinacion+"="+ nombreCombinacion + "[,2]" ;
                primero=false;
            }else{

                vectorNombresSinComillas += ","+nombreCombinacion+"="+ nombreCombinacion + "[,2]" ;
            }

            archivo5.add("sumo<-summary("+ nombreCombinacion + "[,2])");

            archivo5.add("escribir <- paste(c('"+ nombreCombinacion +"',sumo), collapse = ' ')");
            archivo5.add("write(escribir, file = '"+nombreArchivoSumarize+"',ncolumns = 1,append = TRUE, sep =' ')");


        }


        archivo5.add("dati = list("+vectorNombresSinComillas+")");


        archivo5.add("kruskal.test(dati)");



        ar.writeLargerTextFile(rutaHyperRelativoLocal + "KRUSCAL_hypervolume_TODOS_ContraReferenciaInstancia.r", archivo5, appendMode);





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


    private void acumularParetosSoluciones(List<double[]> soluciones,
                                           int indiceparetochico) {


        for (double[] parejaCoso : soluciones) {
            double costo = parejaCoso[0];
            double Qos = parejaCoso[1];



            //se guardan los maximos para ajustar los ejes de las graficas
            if (maximoCosto < costo) {
                maximoCosto = costo;
            }
            if (maximoQoS < Qos) {
                maximoQoS = Qos;
            }
            //guardo en el conjunto global de todas las soluciones
            //el que se usara para hacer el pareto de referencia
            global_solutionSet.add(parejaCoso);
            algoritmoGlobal_solutionSet.add(parejaCoso);

        }
        //guardo cada pareto por separado
        paretosSolucionesSeparados.add(indiceparetochico, soluciones);

    }





    private void levantarDatosAmplToPareto(String ruta) {

       // ArrayList<String> paretorAMPL = new ArrayList<>();
        try {


            ArrayList<String> archivos = new ArrayList<>();
            archivos.add("cdntandaA1_500Vid_300Mins_6Prove_1XreG_1XreQ_pareto");
            archivos.add("cdntandaA2_400Vid_300Mins_5Prove_1XreG_1XreQ_pareto");
            archivos.add("cdntandaA2_600Vid_360Mins_5Prove_1XreG_1XreQ_pareto");
            archivos.add("cdntandaA3_500Vid_240Mins_6Prove_1XreG_1XreQ_pareto");
            archivos.add("cdntandaB1_300Vid_300Mins_6Prove_1XreG_1XreQ_pareto");
            archivos.add("cdntandaC1_1000Vid_240Mins_7Prove_1XreG_1XreQ_pareto");
            archivos.add("cdntandaC1_700Vid_300Mins_6Prove_1XreG_1XreQ_pareto");
            archivos.add("cdntandaD1_400Vid_300Mins_5Prove_1XreG_1XreQ_pareto");
            archivos.add("cdntandaD1_400Vid_360Mins_5Prove_1XreG_1XreQ_pareto");
            archivos.add("cdntandaD1_400Vid_420Mins_5Prove_1XreG_1XreQ_pareto");

            for(String archvito:archivos){
                List<double[]> Ampl_Archivo = this.leerParetoDeArchvo(archvito+".sal", ruta);
                List<double[]> paretoDelampl = this.paretera(Ampl_Archivo);
                this.guardarParetoEnArchvo(paretoDelampl, archvito+"PARETO.fp", ruta);
            }



        } catch (Exception e) {
            System.err.println(nombreAlgoritmo);
            e.printStackTrace();
            JMetalLogger.logger.severe(e.getMessage());
        }
    }





}
