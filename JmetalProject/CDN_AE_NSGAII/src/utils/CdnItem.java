

package utils;

import org.uma.jmetal.util.JMetalLogger;

import utils.Archivos;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

/**
 * Esta clase contiene las matrices de nuestro problema
 */

@SuppressWarnings("serial")
public class CdnItem {

  public String getTipoGreedy() {
    return tipoGreedy;
  }

  public void setTipoGreedy(String tipoGreedy) {
    this.tipoGreedy = tipoGreedy;
  }

  String tipoGreedy;

  String metodoBuscaZ;

  public String getInicioConstruccin() {
    return inicioConstruccin;
  }

  public void setInicioConstruccin(String inicioConstruccin) {
    this.inicioConstruccin = inicioConstruccin;
  }

  public String getFinConstruccion() {
    return finConstruccion;
  }

  public void setFinConstruccion(String finConstruccion) {
    this.finConstruccion = finConstruccion;
  }

  String inicioConstruccin;
  String finConstruccion;
  double costo_instancias_reservadas;
  double costo_instancias_on_demand;
  double costoBytesAlmacenados;
  double costoBytesTransfiriendo;
  double metricaQoS;
  ArrayList<double[]> largosEinterseccionesComparacion;
  int idMemoriaUsada;
String z_construido_con;
  //por ahora de enteros
  private int [][] X;
  private int [][] Y;
  private int [] Y_techo;
  private int [] mapeoRequestsDcs;
  //private ArrayList<ArrayList<String>> dcsComparadosQoSRequests;
  private int demanda_limite;

  private int cant_requests;
  private int cant_contenidos;
  private int cant_centrosDatos;
  private int cant_pasosTiempo;
  private int cant_regionesUsuarios;
  private int cant_pasos_Horas;

  public CdnItem(int cant_contenidos,
                 int cant_centrosDatos,
                 int cant_pasosTiempo,
                 int cant_regionesUsuarios,
                 int[][] x,
                 int[][] y,
                 int[] y_techo,
                 double costo_instancias_reservadas,
                 double costo_instancias_on_demand,
                 double costoBytesAlmacenados,
                 double costoBytesTransfiriendo,
                 ArrayList<double[]> largosEinterseccionesComparacion
  ) {
    X = x;
    Y = y;
    Y_techo = y_techo;
    this.cant_contenidos       = cant_contenidos;
    this.cant_centrosDatos     = cant_centrosDatos;
    this.cant_pasosTiempo      = cant_pasosTiempo;
    this.cant_regionesUsuarios = cant_regionesUsuarios;
    this.costo_instancias_reservadas = costo_instancias_reservadas;
    this.costo_instancias_on_demand = costo_instancias_on_demand;
    this.costoBytesAlmacenados = costoBytesAlmacenados;
    this.costoBytesTransfiriendo = costoBytesTransfiriendo;
    this.largosEinterseccionesComparacion=largosEinterseccionesComparacion;
    this.cant_pasos_Horas=cant_pasosTiempo/60;

  }

  public void setDemanda_limite(int demanda_limite) {
    this.demanda_limite = demanda_limite;
  }

  public int getCant_contenidos() {
    return cant_contenidos;
  }

  public void setCant_contenidos(int cant_contenidos) {
    this.cant_contenidos = cant_contenidos;
  }

  public int getCant_centrosDatos() {
    return cant_centrosDatos;
  }

  public void setCant_centrosDatos(int cant_centrosDatos) {
    this.cant_centrosDatos = cant_centrosDatos;
  }

  public int getCant_pasosTiempo() {
    return cant_pasosTiempo;
  }

  public void setCant_pasosTiempo(int cant_pasosTiempo) {
    this.cant_pasosTiempo = cant_pasosTiempo;
  }

  public int getCant_regionesUsuarios() {
    return cant_regionesUsuarios;
  }

  public void setCant_regionesUsuarios(int cant_regionesUsuarios) {
    this.cant_regionesUsuarios = cant_regionesUsuarios;
  }

  public int getCant_pasos_Horas() {
    return cant_pasos_Horas;
  }

  public CdnItem() {


  }


  public CdnItem(int cant_contenidos,int cant_centrosDatos,int cant_pasosTiempo,int cant_regionesUsuarios, int[][] x, int[][] y, int[] y_techo) {
    X = x;
    Y = y;
    Y_techo = y_techo;
    this.cant_contenidos       = cant_contenidos;
    this.cant_centrosDatos     = cant_centrosDatos;
    this.cant_pasosTiempo      = cant_pasosTiempo;
    this.cant_regionesUsuarios = cant_regionesUsuarios;
    this.cant_pasos_Horas=cant_pasosTiempo/60;
  }


  public int[][] getX() {
    return X;
  }

  public void setX(int[][] x) {
    X = x;
  }

  public int[][] getY() {
    return Y;
  }

  public void setY(int[][] y) {
    Y = y;
  }

  public int[] getY_techo() {
    return Y_techo;
  }

  public void setY_techo(int[] y_techo) {
    Y_techo = y_techo;
  }

  public CdnItem clone () {
    CdnItem copiaLimpia;

    int [][] X_copia                 = new int[cant_contenidos][cant_centrosDatos];
    int [][] Y_copia                 = new int[cant_centrosDatos][cant_pasos_Horas];
    int [] Y_techo_copia             = new int[cant_centrosDatos];
    int [] mapeoRequestsDcs_copia    = new int[cant_requests];
   // ArrayList<ArrayList<String>> dcsComparadosQoSRequests_copia    = new ArrayList(cant_requests);

    for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
      for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
        X_copia[contenidoK][centroDato] = X[contenidoK][centroDato];
      }
    }

    for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
      for (int tiempoStep = 0; tiempoStep < cant_pasos_Horas; tiempoStep++) {
        Y_copia[centroDato][tiempoStep] = Y[centroDato][tiempoStep];
      }
    }

    for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
      Y_techo_copia[centroDato] = Y_techo[centroDato];
    }

    for (int request = 0; request < cant_requests; request++) {
      mapeoRequestsDcs_copia[request] = mapeoRequestsDcs[request];
    }


//    for (int request = 0; request < cant_requests; request++) {
//      dcsComparadosQoSRequests_copia.add(request, new ArrayList(dcsComparadosQoSRequests.get(request).size()));
//      for (int indicxx = 0; indicxx < dcsComparadosQoSRequests.get(request).size(); indicxx++) {
//        dcsComparadosQoSRequests_copia.get(request).add(dcsComparadosQoSRequests.get(request).get(indicxx));
//      }
//
//
//    }

    copiaLimpia=new CdnItem();

    copiaLimpia.costo_instancias_reservadas = costo_instancias_reservadas;
    copiaLimpia.costo_instancias_on_demand  = costo_instancias_on_demand;
    copiaLimpia.costoBytesAlmacenados       = costoBytesAlmacenados;
    copiaLimpia.costoBytesTransfiriendo     = costoBytesTransfiriendo;
    copiaLimpia.metricaQoS                  = metricaQoS;
    copiaLimpia.cant_requests               = cant_requests;
    copiaLimpia.cant_contenidos             = cant_contenidos;
    copiaLimpia.cant_centrosDatos           = cant_centrosDatos;
    copiaLimpia.cant_pasosTiempo            = cant_pasosTiempo;
    copiaLimpia.cant_regionesUsuarios       = cant_regionesUsuarios;
    copiaLimpia.cant_pasos_Horas            = cant_pasos_Horas;
    copiaLimpia.X                           = X_copia;
    copiaLimpia.Y_techo                     = Y_techo_copia;
    copiaLimpia.Y                           = Y_copia;
    copiaLimpia.mapeoRequestsDcs            = mapeoRequestsDcs_copia;
   // copiaLimpia.dcsComparadosQoSRequests    = dcsComparadosQoSRequests_copia;
    return copiaLimpia;
  }

  public boolean comparar (CdnItem acomparar,boolean compararZ) {

    boolean soniguales=true;

    for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
      for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
        if(acomparar.X[contenidoK][centroDato] != this.X[contenidoK][centroDato]){
          soniguales=false;
          break;
        }
      }
    }

    for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
      for (int tiempoStep = 0; tiempoStep < cant_pasos_Horas; tiempoStep++) {
        if(acomparar.Y[centroDato][tiempoStep] != this.Y[centroDato][tiempoStep]){
          soniguales=false;
          break;
        }
      }
    }

    for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
      if(acomparar.Y_techo[centroDato] != this.Y_techo[centroDato] ){
        soniguales=false;
        break;
      }
    }

    if(compararZ){
      for (int request = 0; request < cant_requests; request++) {
        if(acomparar.mapeoRequestsDcs[request]!= this.mapeoRequestsDcs[request] ){
          soniguales=false;
          break;
        }
      }
    }

    return soniguales;
  }

  public String toString () {


    String retorno="";

    ArrayList<String> lineasCabezal = new ArrayList<>();
    ArrayList<String> lineas = new ArrayList<>();
    String nombreArchivoSalida="Solo va a string";



    this.armarListasStringsLogs(lineasCabezal, lineas, nombreArchivoSalida);

    for (String lineacoso: lineasCabezal ) {
    //  System.err.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+lineacoso+" lineasCabezal ");
      retorno+=lineacoso+'\n';
    }

    for (String lineacoso: lineas ) {
     // System.err.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+lineacoso+" lineas ");
      retorno+=lineacoso+'\n';
    }

    return  retorno;
  }


  void armarListasStringsLogs(ArrayList<String> lineasCabezal,ArrayList<String> lineas,String nombreArchivoSalida){
    int minutos_pagados_vm_reservadas =0;
    int minutos_pagados_vm_ondemand =0;
    String retorno="";

    retorno+="Ejecucion                   = "+nombreArchivoSalida+'\n';
    retorno+="Cant Agrupaciones Videos    = "+this.cant_contenidos+'\n';
    retorno+="Cantidad de CD              = "+this.cant_centrosDatos+'\n';
    retorno+="Total minutos demanda       = "+this.cant_pasosTiempo+'\n';
    retorno+="Cantidad Regiones Usuarios  = "+this.cant_regionesUsuarios+'\n';
    retorno+='\n'+""+'\n';
    retorno+="costo_instancias_reservadas = "+costo_instancias_reservadas+'\n';
    retorno+="costo_instancias_on_demand  = "+costo_instancias_on_demand+'\n';
    retorno+="costoBytesAlmacenados       = "+costoBytesAlmacenados+'\n';
    retorno+="costoBytesTransfiriendo     = "+costoBytesTransfiriendo+'\n';

    lineasCabezal.add(retorno);
    double total = costoBytesAlmacenados+costo_instancias_on_demand+costo_instancias_reservadas+costoBytesTransfiriendo;

    lineasCabezal.add("Total                       = "+total);
    lineas.add('\n'+""+'\n');

    double todalLargos = 0;
    double todalIntersecciones = 0;
    if(largosEinterseccionesComparacion!=null) {
      for (double[] pareja : largosEinterseccionesComparacion) {
        todalLargos += pareja[0] + pareja[1];
        todalIntersecciones += pareja[2];

        lineas.add("pareja[0]: " + pareja[0]);
        lineas.add("pareja[1]: " + pareja[1]);
        lineas.add("pareja[2]: " + pareja[2]);
      }
    }
    lineasCabezal.add("Error                       = "+(todalLargos>0?todalIntersecciones/todalLargos:0));

    if(largosEinterseccionesComparacion!=null) {
      for (double[] pareja : largosEinterseccionesComparacion) {
        lineas.add("pareja[0]: " + pareja[0]);
        lineas.add("pareja[1]: " + pareja[1]);
        lineas.add("pareja[2]: " + pareja[2]);
      }
    }



    lineas.add('\n'+""+'\n');
    lineas.add("CD");

    for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
      if(Y_techo[centroDato]>0) {
        lineas.add("Y_techo[" + centroDato + "] = " + Y_techo[centroDato]);
      }
    }

    lineas.add('\n'+""+'\n');
    lineas.add("K  CD");
    for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
      for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
        if(X[contenidoK][centroDato]>0) {
          lineas.add("X[" + contenidoK + "][" + centroDato + "] = " + X[contenidoK][centroDato]);
        }
      }
    }



    lineas.add('\n'+""+'\n');
    lineas.add("CD t");
    for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
      for (int tiempoStep = 0; tiempoStep < cant_pasos_Horas; tiempoStep++) {
        if(Y[centroDato][tiempoStep]>0) {
          lineas.add("Y[" + centroDato + "][" + tiempoStep + "] = " + Y[centroDato][tiempoStep] );
          if (Y[centroDato][tiempoStep] > Y_techo[centroDato]) {
            minutos_pagados_vm_ondemand += Y[centroDato][tiempoStep] - Y_techo[centroDato];
          }
          minutos_pagados_vm_reservadas+= Y_techo[centroDato];
        }
      }
    }

if(cant_requests<5000) {
  for (int request = 0; request < cant_requests; request++) {
    if (mapeoRequestsDcs[request] >= 0) {
      // lineas.add("mapeoRequestsDcs[" + request + "] = " + mapeoRequestsDcs[request]);
    } else {
      lineas.add("ERROR:  mapeoRequestsDcs[" + request + "] = " + mapeoRequestsDcs[request]);
      JMetalLogger.logger.log(Level.SEVERE, "ERROR:  mapeoRequestsDcs[" + request + "] = " + mapeoRequestsDcs[request]);
      break;//solo para reportar el error en el archivo
    }
  }
}else{
  lineas.add("NO SE MUESTRA REQUESTS PORQUE SON MUCHOS: "+cant_requests );
}

    lineasCabezal.add('\n'+""+'\n');

    lineasCabezal.add("minutos_demanda                 = "+cant_pasosTiempo);
    lineasCabezal.add("horas_demanda                   = "+cant_pasos_Horas);
    lineasCabezal.add("minutos_pagados_vm_reservadas   = "+minutos_pagados_vm_reservadas);
    lineasCabezal.add("minutos_pagados_vm_ondemand     = "+minutos_pagados_vm_ondemand);

  }


  void armarListasStringsEvaluacion(ArrayList<String> lineasCabezal,ArrayList<String> lineas,String nombreArchivoSalida){
    int minutos_pagados_vm_reservadas =0;
    int minutos_pagados_vm_ondemand =0;
    String retorno="";

    retorno+="Ejecucion                   = "+nombreArchivoSalida+'\n';
    retorno+="Cant Agrupaciones Videos    = "+this.cant_contenidos+'\n';
    retorno+="Cantidad de CD              = "+this.cant_centrosDatos+'\n';
    retorno+="Total minutos demanda       = "+this.cant_pasosTiempo+'\n';
    retorno+="Cantidad Regiones Usuarios  = "+this.cant_regionesUsuarios+'\n';
    retorno+="Demanda limite              = "+this.demanda_limite+'\n';
    retorno+='\n'+""+'\n';
    retorno+="costo_instancias_reservadas = "+costo_instancias_reservadas+'\n';
    retorno+="costo_instancias_on_demand  = "+costo_instancias_on_demand+'\n';
    retorno+="costoBytesAlmacenados       = "+costoBytesAlmacenados+'\n';
    retorno+="costoBytesTransfiriendo     = "+costoBytesTransfiriendo+'\n';

    lineasCabezal.add(retorno);
    double total = costoBytesAlmacenados+costo_instancias_on_demand+costo_instancias_reservadas+costoBytesTransfiriendo;

    lineasCabezal.add("Total                       = "+total);
    lineas.add('\n'+""+'\n');

    lineasCabezal.add("QoS                       = "+metricaQoS);
    lineas.add('\n'+""+'\n');

    //todo podria hacer la cuenta del error aca con los nuevos parametros
    double todalLargos = 0;
    double todalIntersecciones = 0;
    if(largosEinterseccionesComparacion!=null) {
      for (double[] pareja : largosEinterseccionesComparacion) {
        todalLargos += pareja[0] + pareja[1];
        todalIntersecciones += pareja[2];

        lineas.add("pareja[0]: " + pareja[0]);
        lineas.add("pareja[1]: " + pareja[1]);
        lineas.add("pareja[2]: " + pareja[2]);
      }
    }
   // lineasCabezal.add("Error                       = "+(todalLargos>0?todalIntersecciones/todalLargos:0));



    lineas.add('\n'+""+'\n');
    lineas.add("CD");

    for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
      if(Y_techo[centroDato]>0) {
        lineas.add("Y_techo[" + centroDato + "] = " + Y_techo[centroDato]);
      }
    }





    lineas.add('\n'+""+'\n');
    lineas.add("CD t");
    for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
      for (int tiempoStep = 0; tiempoStep < cant_pasos_Horas; tiempoStep++) {
        if(Y[centroDato][tiempoStep]>0) {
          lineas.add("Y[" + centroDato + "][" + tiempoStep + "] = " + Y[centroDato][tiempoStep] );
          if (Y[centroDato][tiempoStep] > Y_techo[centroDato]) {
            minutos_pagados_vm_ondemand += Y[centroDato][tiempoStep] - Y_techo[centroDato];
          }
          minutos_pagados_vm_reservadas+= Y_techo[centroDato];
        }
      }
    }

    lineas.add('\n'+""+'\n');
    lineas.add("K  CD");
    for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
      for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
        if(X[contenidoK][centroDato]>0) {
          lineas.add("X[" + contenidoK + "][" + centroDato + "] = " + X[contenidoK][centroDato]);
        }
      }
    }

    if(cant_requests<5000000) {

//      for (int request = 0; request < cant_requests; request++) {
//        String comparacionesRequests = "dcsComparadosQoSRequests[" + request + "] = ";
//        for(String comparados:dcsComparadosQoSRequests.get(request)){
//          comparacionesRequests+='\t'+comparados;
//        }
//        lineas.add(comparacionesRequests);
//      }

      lineas.add('\n'+""+'\n');

      for (int request = 0; request < cant_requests; request++) {
        if (mapeoRequestsDcs[request] >= 0) {
           lineas.add("mapeoRequestsDcs[" + request + "] = " + mapeoRequestsDcs[request]);
        } else {
          lineas.add("ERROR:  mapeoRequestsDcs[" + request + "] = " + mapeoRequestsDcs[request]);
          JMetalLogger.logger.log(Level.SEVERE, "ERROR:  mapeoRequestsDcs[" + request + "] = " + mapeoRequestsDcs[request]);
          break;//solo para reportar el error en el archivo
        }
      }
      if(cant_requests==0){
        lineas.add("ERROR:  cantrequests = " + 0);
      }
    }else{
      lineas.add("NO SE MUESTRA REQUESTS PORQUE SON MUCHOS: "+cant_requests );
    }

    lineasCabezal.add('\n'+""+'\n');

    lineasCabezal.add("minutos_demanda                 = "+cant_pasosTiempo);
    lineasCabezal.add("horas_demanda                   = "+cant_pasos_Horas);
    lineasCabezal.add("minutos_pagados_vm_reservadas   = "+minutos_pagados_vm_reservadas);
    lineasCabezal.add("minutos_pagados_vm_ondemand     = "+minutos_pagados_vm_ondemand);

  }




  public void toFile (String directorioSalida,String nombreArchivoSalida) {
    ArrayList<String> lineasCabezal = new ArrayList<>();
    ArrayList<String> lineas = new ArrayList<>();
    String rutaArchivo = directorioSalida+nombreArchivoSalida;



    this.armarListasStringsLogs(lineasCabezal, lineas, nombreArchivoSalida);

      lineasCabezal.addAll(lineas);
      boolean appendMode=false;
      Archivos ar = new Archivos();
      ar.writeLargerTextFile(rutaArchivo,lineasCabezal,appendMode);
      System.out.println("Datos guardados en: "+rutaArchivo);


  }

  public void toFileGeneral(String directorioSalida){
    ArrayList<String> lineas = new ArrayList<>();

    String rutaArchivo = directorioSalida+"todasInstancias.txt";

    String retorno=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+'\n';
    retorno+="Instancia"+'\t'+rutaArchivo+'\n';
    retorno+="z_construido_con"+'\t'+z_construido_con+'\n';
    retorno+="metodoBuscaZ"+'\t'+metodoBuscaZ+'\n';
    retorno+="Cant Agrupaciones Videos"+'\t'+this.cant_contenidos+'\n';
    retorno+="Cantidad de CD"+'\t'+this.cant_centrosDatos+'\n';
    retorno+="Total minutos demanda"+'\t'+this.cant_pasosTiempo+'\n';
    retorno+="Cantidad Regiones Usuarios"+'\t'+this.cant_regionesUsuarios+'\n';
    retorno+="costo_instancias_reservadas"+'\t'+costo_instancias_reservadas+'\n';
    retorno+="costo_instancias_on_demand"+'\t'+costo_instancias_on_demand+'\n';
    retorno+="costoBytesAlmacenados"+'\t'+costoBytesAlmacenados+'\n';
    retorno+="costoBytesTransfiriend"+'\t'+costoBytesTransfiriendo+'\n';


    double total = costoBytesAlmacenados+costo_instancias_on_demand+costo_instancias_reservadas+costoBytesTransfiriendo;

    retorno+="Total"+'\t'+total+'\n';
    lineas.add(retorno);
    lineas.add("QoS"+'\t'+metricaQoS+'\n');

    lineas.add('\n'+""+'\n');


      boolean appendMode=true;
      Archivos ar = new Archivos();
      ar.writeLargerTextFile(rutaArchivo,lineas,appendMode);
      System.out.println("Datos guardados en: "+rutaArchivo);

  }
  public void toFileEvaluacion(String directorioSalida,String nombreArchivo){
//    ArrayList<String> lineas = new ArrayList<>();
//
//    String rutaArchivo = directorioSalida+nombreArchivo;
//
//    String retorno=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+'\n';
//    retorno+="Instancia"+'\t'+rutaArchivo+'\n';
//    retorno+="z_construido_con"+'\t'+z_construido_con+'\n';
//    retorno+="metodoBuscaZ"+'\t'+metodoBuscaZ+'\n';
//    retorno+="Cant Agrupaciones Videos"+'\t'+this.cant_contenidos+'\n';
//    retorno+="Cantidad de CD"+'\t'+this.cant_centrosDatos+'\n';
//    retorno+="Total minutos demanda"+'\t'+this.cant_pasosTiempo+'\n';
//    retorno+="Cantidad Regiones Usuarios"+'\t'+this.cant_regionesUsuarios+'\n';
//    retorno+="costo_instancias_reservadas"+'\t'+costo_instancias_reservadas+'\n';
//    retorno+="costo_instancias_on_demand"+'\t'+costo_instancias_on_demand+'\n';
//    retorno+="costoBytesAlmacenados"+'\t'+costoBytesAlmacenados+'\n';
//    retorno+="costoBytesTransfiriend"+'\t'+costoBytesTransfiriendo+'\n';
//
//
//    double total = costoBytesAlmacenados+costo_instancias_on_demand+costo_instancias_reservadas+costoBytesTransfiriendo;
//
//    retorno+="Total"+'\t'+total+'\n';
//    lineas.add(retorno);
//    lineas.add("QoS"+'\t'+metricaQoS+'\n');
//
//    lineas.add('\n'+""+'\n');


    ArrayList<String> lineasCabezal = new ArrayList<>();
    ArrayList<String> lineas = new ArrayList<>();
    String rutaArchivo = directorioSalida+nombreArchivo;



    this.armarListasStringsEvaluacion(lineasCabezal, lineas, nombreArchivo);

    lineasCabezal.addAll(lineas);



    boolean appendMode=true;
    Archivos ar = new Archivos();
    ar.writeLargerTextFile(rutaArchivo,lineasCabezal,appendMode);
    System.out.println("Datos guardados en: "+rutaArchivo);

  }



  public void toFileTiempos(String directorioSalida, ArrayList<String> tiemposSolucion ){

    //String rutaArchivo = directorioSalida+"tiemposGreedy.txt";

    String rutaArchivo = "/home/ggoni/Documentos/PROYE/solocodigo/proycodigo/salida/todosTiemposHeuristicasGreddyRobinJuntos.csv";




    boolean appendMode=true;
    Archivos ar = new Archivos();
    ar.writeLargerTextFile(rutaArchivo,tiemposSolucion,appendMode);
    System.out.println("Tiempos guardados en: "+rutaArchivo);

  }



  public void toFilePareto(String directorioSalida){
    ArrayList<String> lineas = new ArrayList<>();

    String rutaArchivo = directorioSalida+"todasInstanciasPareto.txt";


    double total = costoBytesAlmacenados+costo_instancias_on_demand+costo_instancias_reservadas+costoBytesTransfiriendo;

    String retorno=String.valueOf(total)+'\t'+String.valueOf(metricaQoS);
    lineas.add(retorno);

      boolean appendMode=true;
      Archivos ar = new Archivos();
      ar.writeLargerTextFile(rutaArchivo,lineas,appendMode);
      System.out.println("Datos guardados en: "+rutaArchivo);

  }

  public double getCosto_instancias_reservadas() {
    return costo_instancias_reservadas;
  }

  public void setCosto_instancias_reservadas(double costo_instancias_reservadas) {
    this.costo_instancias_reservadas = costo_instancias_reservadas;
  }

  public double getCosto_instancias_on_demand() {
    return costo_instancias_on_demand;
  }

  public void setCosto_instancias_on_demand(double costo_instancias_on_demand) {
    this.costo_instancias_on_demand = costo_instancias_on_demand;
  }

  public double getCostoBytesAlmacenados() {
    return costoBytesAlmacenados;
  }

  public void setCostoBytesAlmacenados(double costoBytesAlmacenados) {
    this.costoBytesAlmacenados = costoBytesAlmacenados;
  }

  public double getCostoBytesTransfiriendo() {
    return costoBytesTransfiriendo;
  }

  public void setCostoBytesTransfiriendo(double costoBytesTransfiriendo) {
    this.costoBytesTransfiriendo = costoBytesTransfiriendo;
  }

    public int getIdMemoriaUsada() {
        return idMemoriaUsada;
    }

    public void setIdMemoriaUsada(int idMemoriaUsada) {
        this.idMemoriaUsada = idMemoriaUsada;
    }

    public int[] getMapeoRequestsDcs() {
        return mapeoRequestsDcs;
    }

    public void setMapeoRequestsDcs(int[] mapeoRequestsDcs) {
        this.mapeoRequestsDcs = mapeoRequestsDcs;
    }

    public int getCant_requests() {
        return cant_requests;
    }

    public void setCant_requests(int cant_requests) {
        this.cant_requests = cant_requests;
    }

  public double getMetricaQoS() {
    return metricaQoS;
  }

  public void setMetricaQoS(double metricaQoS) {
    this.metricaQoS = metricaQoS;
  }


  public void setCant_pasos_Horas(int cant_pasos_Horas) {
    this.cant_pasos_Horas = cant_pasos_Horas;
  }

public int getXvalor(int idvideo,int dc){
    return X[idvideo][dc];
}

  public void setXvalor(int idvideo,int dc,int valor){
    X[idvideo][dc]=valor;
  }

  public int getYvalor(int dc,int pasohora){
    return Y[dc][pasohora];
  }

  public void setYvalor(int dc,int pasohora,int valor){
    Y[dc][pasohora]=valor;
  }

  public int getY_techovalor(int dc){
    return Y_techo[dc];
  }


  public void setY_techovalor(int dc,int valor){
     Y_techo[dc]=valor;
  }

  public int getMapeoRequestsDcsvalor(int idrequest){
    return mapeoRequestsDcs[idrequest];
  }

  public void setMapeoRequestsDcsvalor(int idrequest,int dc){
     mapeoRequestsDcs[idrequest]=dc;
  }


  public void initMapeoRequestsDcs(){
   mapeoRequestsDcs= new int[cant_requests];
    for(int req=0;req<cant_requests;req++){
      mapeoRequestsDcs[req]=-1;
    }
  }
//  public void initdcsComparadosQoSRequests(){
//    dcsComparadosQoSRequests= new ArrayList<ArrayList<String>>(cant_requests);
//    for(int req=0;req<cant_requests;req++){
//      ArrayList<String> ins = new ArrayList<String>();
//      dcsComparadosQoSRequests.add(req,ins);
//    }
//  }





//  public void addDcsComparadosQoSR_UNequests(ArrayList<String> dcsComparadosQoSRequests, int idrequest) {
//    if(dcsComparadosQoSRequests!=null) {
//      this.dcsComparadosQoSRequests.get(idrequest).addAll(dcsComparadosQoSRequests);
//    }
//  }
  public String getZ_construido_con() {
    return z_construido_con;
  }

  public void setZ_construido_con(String z_construido_con) {
    this.z_construido_con = z_construido_con;
  }

  public String getMetodoBuscaZ() {
    return metodoBuscaZ;
  }

  public void setMetodoBuscaZ(String metodoBuscaZ) {
    this.metodoBuscaZ = metodoBuscaZ;
  }

long inicioConstruirZ;
long finConstruirZ;

long inicioCross;
long finCross;

long inicioMut;
long finMut;

  public long getTiempoConstruirZ() {
    return finConstruirZ-inicioConstruirZ;
  }

  public long getTiempoCross() {
    return finCross-inicioCross;
  }

  public long getTiempoMut() {
    return finMut-inicioMut;
  }


  public void setInicioConstruirZ(long inicioConstruirZ) {
    this.inicioConstruirZ = inicioConstruirZ;
  }



  public void setFinConstruirZ(long finConstruirZ) {
    this.finConstruirZ = finConstruirZ;
  }



  public void setInicioCross(long inicioCross) {
    this.inicioCross = inicioCross;
  }



  public void setFinCross(long finCross) {
    this.finCross = finCross;
  }



  public void setInicioMut(long inicioMut) {
    this.inicioMut = inicioMut;
  }

  public void setFinMut(long finMut) {
    this.finMut = finMut;
  }
}
