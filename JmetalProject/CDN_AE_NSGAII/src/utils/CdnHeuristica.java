package utils;
import com.sun.org.apache.xpath.internal.SourceTree;
import utils.cdn.DataCenter;

import java.io.File;
import java.util.*;

/**
 * Created by ggoni on 28/03/2017.
 */
public class CdnHeuristica {
    private String rutaAbsolutaSalidaInstancia = "";
    private CdnInstancia instanciaDatos;
    public String rutaGreedy=null;
    public String rutaGreedyQoS=null;
    public String rutaRobin=null;
    public String nombreArchivoRobin=null;
    public String nombreArchivoQoS=null;
    public String nombreArchivoCosto=null;
    public String nombreArchivoTiemposRobin=null;
    public String nombreArchivoTiemposQoS=null;
    public String nombreArchivoTiemposCosto=null;




    public CdnHeuristica(String nombreArchivoPropertiesInstancia){
        instanciaDatos= new CdnInstancia( nombreArchivoPropertiesInstancia);
        this.rutaAbsolutaSalidaInstancia = instanciaDatos.getRutaAbsolutaSalidaInstancia();
        rutaGreedy=null;
        rutaGreedyQoS=null;
        rutaRobin=null;
   }


    public static void main(String[] args){
        List<String> instancias = new ArrayList<>();
       // instancias.add("tandaD5_80000Vid_180Mins_7Prove_5XreG_6XreQ");
       // instancias.add("tandaD3_16000Vid_240Mins_6Prove_6XreG_6XreQ");

        instancias.add("AMPL1");
        instancias.add("AMPL2");
        instancias.add("AMPL3");
        instancias.add("AMPL4");
        instancias.add("CH1");
        instancias.add("CH2");
        instancias.add("M1");
        instancias.add("M2");
        instancias.add("G1");
        instancias.add("G2");
        for (String nombreArchivoPropertiesInstancia:instancias) {
            CdnHeuristica heuri = new CdnHeuristica(nombreArchivoPropertiesInstancia);
            heuri.setRutasHeuristicas();
            /*GREEDYS*/
            heuri.guardarHeuristica(heuri.rutaGreedy,heuri.nombreArchivoCosto,heuri.muchasGreedy());
            /*GREEDYS QoS*/
            heuri.guardarHeuristica(heuri.rutaGreedyQoS,heuri.nombreArchivoQoS,heuri.muchasQoS());
            /*ROUNDROBIN*/
            heuri.guardarHeuristica(heuri.rutaRobin,heuri.nombreArchivoRobin,heuri.muchasRoundRobin());
        }
    }



    public void setRutasHeuristicas(){
        this.rutaGreedy =this.instanciaDatos.getRutaAbsolutaSalidaInstancia()+"greedyC/";
        this.rutaGreedyQoS =this.instanciaDatos.getRutaAbsolutaSalidaInstancia()+"greedyQoS/";
        this.rutaRobin =this.instanciaDatos.getRutaAbsolutaSalidaInstancia()+"robin/";
        this.nombreArchivoCosto="TodosGreedysCost.csv";
        this.nombreArchivoQoS="TodosGreedysQoS.csv";
        this.nombreArchivoRobin="TodosROBIN.csv";

        this.nombreArchivoTiemposRobin="TiemposRobin.csv";
        this.nombreArchivoTiemposQoS="TiemposQoS.csv";
        this.nombreArchivoTiemposCosto="TiemposCosto.csv";


        File directory = new File(this.rutaGreedy);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+this.rutaGreedy);
        }
        directory = new File(this.rutaGreedyQoS);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+this.rutaGreedyQoS);
        }
        directory = new File(this.rutaRobin);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("Creando directorio "+this.rutaRobin);
        }

    }

    public void guardarHeuristica(String carpetaHeuristica,String nombreArchivoHeuristica,List<double []> solucionesHeuristica){
        boolean appendMode = false;
        Archivos ar = new Archivos();
        List<String> todas = new ArrayList<>();
        for (double [] valorSoluco:solucionesHeuristica) {
            todas.add(String.valueOf(valorSoluco[0])+"\t"+String.valueOf(valorSoluco[1]));
        }
        ar.writeLargerTextFile(carpetaHeuristica+nombreArchivoHeuristica,todas,appendMode);
    }


    //todo no modificar se usa en otros lados
    public List<double []> muchasGreedy( ){
        boolean armarZ=true;
        List<double []> retrox = new ArrayList<>();
            ArrayList<CdnItem> listaSolucsOnCost=this.instanciaDatos.solucsGreedyOnCostDistintas( armarZ);
            ArrayList<String> paretoSolucion = new ArrayList<>();
        ArrayList<String> tiemposSolucion = new ArrayList<>();
            int count=0;
            for (CdnItem soluc : listaSolucsOnCost  ) {
                double costoSoluc = (CRC( soluc) +  DSC(soluc) + DTC(soluc));
               // double costoSoluc = soluc.getCosto_instancias_on_demand()+soluc.getCosto_instancias_reservadas()+soluc.getCostoBytesAlmacenados()+soluc.getCostoBytesTransfiriendo();
                paretoSolucion.add(String.valueOf(costoSoluc)+'\t'+String.valueOf(soluc.getMetricaQoS()));
                double [] solucccs = new double[2];
                solucccs[0]=costoSoluc;
                solucccs[1]=soluc.getMetricaQoS();
                retrox.add(solucccs);
                tiemposSolucion.add("costo"+";"+this.rutaGreedy+";"+soluc.getInicioConstruccin()+";"+soluc.getFinConstruccion());
                if(this.rutaGreedy!=null) {
                    soluc.toFileEvaluacion(this.rutaGreedy, "cost" + count + ".sol");
                    count++;
                }
            }

            CdnItem solucGuardadora = new CdnItem();
            solucGuardadora.toFileTiempos(this.rutaGreedy,tiemposSolucion);

       return retrox;
    }



    public List<double []> muchasRoundRobin(  ){
        boolean armarZ=true;
        List<double []> retrox = new ArrayList<>();
        ArrayList<CdnItem> listaSolucsOnCost=this.instanciaDatos.solucsRoundRobinDistintas( armarZ);
        ArrayList<String> paretoSolucion = new ArrayList<>();
        ArrayList<String> tiemposSolucion = new ArrayList<>();
        int count=0;
        for (CdnItem soluc : listaSolucsOnCost  ) {
            double costoSoluc = (CRC( soluc) +  DSC(soluc) + DTC(soluc));
            // double costoSoluc = soluc.getCosto_instancias_on_demand()+soluc.getCosto_instancias_reservadas()+soluc.getCostoBytesAlmacenados()+soluc.getCostoBytesTransfiriendo();
            paretoSolucion.add(String.valueOf(costoSoluc)+'\t'+String.valueOf(soluc.getMetricaQoS()));
            double [] solucccs = new double[2];
            solucccs[0]=costoSoluc;
            solucccs[1]=soluc.getMetricaQoS();
            retrox.add(solucccs);
            tiemposSolucion.add("rr"+";"+this.rutaRobin+";"+soluc.getInicioConstruccin()+";"+soluc.getFinConstruccion());
            if(this.rutaRobin!=null) {
                soluc.toFileEvaluacion(this.rutaRobin, "robin" + count + ".sol");
                count++;
            }
        }
        CdnItem solucGuardadora = new CdnItem();
        solucGuardadora.toFileTiempos(this.rutaRobin,tiemposSolucion);
        return retrox;
    }

    public List<double []> muchasQoS( ){
        CdnFactibilizador fact = new CdnFactibilizador(this.instanciaDatos);
        boolean armarZ=true;
        List<double []> retrox = new ArrayList<>();
        ArrayList<CdnItem> listaSolucsOnCost=this.instanciaDatos.solucsQoSDistintas( armarZ);
        ArrayList<String> paretoSolucion = new ArrayList<>();
        ArrayList<String> tiemposSolucion = new ArrayList<>();
        int count=0;
        for (CdnItem soluc : listaSolucsOnCost  ) {
            if(!fact.isFactible(soluc,armarZ)){
                System.err.println("No factible qos");};
            double costoSoluc = (CRC( soluc) +  DSC(soluc) + DTC(soluc));
            // double costoSoluc = soluc.getCosto_instancias_on_demand()+soluc.getCosto_instancias_reservadas()+soluc.getCostoBytesAlmacenados()+soluc.getCostoBytesTransfiriendo();
            paretoSolucion.add(String.valueOf(costoSoluc)+'\t'+String.valueOf(soluc.getMetricaQoS()));
            double [] solucccs = new double[2];
            solucccs[0]=costoSoluc;
            solucccs[1]=soluc.getMetricaQoS();
            retrox.add(solucccs);
            tiemposSolucion.add("qos"+";"+this.rutaGreedyQoS+";"+soluc.getInicioConstruccin()+";"+soluc.getFinConstruccion());
            if(this.rutaGreedyQoS!=null) {
                soluc.toFileEvaluacion(this.rutaGreedyQoS, "qoS" + count + ".sol");
                count++;
            }
        }

        CdnItem solucGuardadora = new CdnItem();
        solucGuardadora.toFileTiempos(this.rutaGreedyQoS,tiemposSolucion);

        return retrox;
    }




    private double CRC(CdnItem solucionItem) {
        int [][] Y=solucionItem.getY();
        int [] Y_techo=solucionItem.getY_techo();
        double [] costosVms = this.instanciaDatos.costosMaquinasVirtuales(Y,Y_techo);
        solucionItem.setCosto_instancias_on_demand(costosVms[1]);
        solucionItem.setCosto_instancias_reservadas(costosVms[0]);
        return costosVms[0]+costosVms[1];
    }

    private double DSC(CdnItem solucionItem) {
        int [][] X=solucionItem.getX();
        double precioCategoriasAlmacenadas =0;
        for(int centroDato =0;centroDato<instanciaDatos.getCant_centrosDatos();centroDato++) {
            DataCenter c = instanciaDatos.getC().get(centroDato);
            int centrodato =  c.getId();
            for (int contenidoK = 0; contenidoK < instanciaDatos.getCant_contenidos(); contenidoK++) {
                precioCategoriasAlmacenadas += (double) X[contenidoK][centrodato] * this.instanciaDatos.precioAlmacenarCategoriaVideos(centrodato);
            }
        }
        solucionItem.setCostoBytesAlmacenados(precioCategoriasAlmacenadas);
        return precioCategoriasAlmacenadas;
    }

    private double DTC(CdnItem solucionItem) {
        return solucionItem.getCostoBytesTransfiriendo();
    }



}
