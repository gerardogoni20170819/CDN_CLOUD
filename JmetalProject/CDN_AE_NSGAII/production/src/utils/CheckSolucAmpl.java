package utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;

import static java.lang.Math.abs;

/**
 *
 * @author ggoni
 */


import static java.lang.Math.abs;

import java.util.HashMap;

import utils.CdnFactibilizador;

/**
 *
 * @author ggoni
 */
public class CheckSolucAmpl {

    double Fcost = -1;
    int cantDC;
    int cantHoras;
    int cantVideos;
    int cantProveedores;
    int cantRegusr;
    double Qos;
    int[] yReservadas;
    int[][] yUsadas;
    int[][] xMapeoVideos;
    int[][] xMapeoVideosCDN;
    int[][][] vmCantVms; //{PROVEEDORES,CENTROSDATOS,Tminutos}
    int[][][][] zRuteo; //zRuteo {CENTROSDATOS,K,Tminutos,REGIONESUSR}
   // ArrayList<String> tokens;
    HashMap<String,String> tokens;
    CdnItem solucCdn;
    public CheckSolucAmpl() {
        this.tokens = new HashMap<>();
        this.tokens.put("Fcost","Fcost");
        this.tokens.put("yReservadas","yReservadas");
        this.tokens.put("yUsadas","yUsadas");
        this.tokens.put("xMapeoVideos","xMapeoVideos");
        this.tokens.put("vmCantVms","vmCantVms");
        this.tokens.put("zRuteo","zRuteo");
        this.tokens.put("Qos","Qos");
    }

    public static void main(String[] args) {
        CheckSolucAmpl che = new CheckSolucAmpl();
        che.levantarFile();
    }

    public void levantarFile() {
        FileReader input = null;
        BufferedWriter writer = null;
     //   String ruta = "F:/PaHacer/TdeDescPrMat/amplide.mswin64/models/cdn.sal";
        String ruta = "F:/PROYE/solocodigo/proycodigo/AMPL/archivosSAL/cdn6161Vid_240Mins_6Prove_1XreG_1XreQ - copia.sal";
     //   String ruta = "/home/ggoni/Documentos/proye/proycodigo/AMPL/prrrrrr.sal";
        String instancia="6161Vid_240Mins_6Prove_1XreG_1XreQ";

        try {
            input = new FileReader(ruta);
            BufferedReader bufRead = new BufferedReader(input);
            int tamosTodos=0;
            String myLine = null;
            while ((myLine = bufRead.readLine()) != null) {
                System.out.println(myLine);
                myLine = myLine.trim();
                String[] array1 = myLine.split("\\s+");
                if(array1!=null){
                    switch (array1[0]) {
                        case "cantDC":
                            this.setCantDC(Integer.valueOf(array1[1]));
                            tamosTodos++;
                            break;
                        case "cantHoras":
                            this.setCantHoras(Integer.valueOf(array1[1]));
                            tamosTodos++;
                            break;
                        case "cantVideos":
                            this.setCantVideos(Integer.valueOf(array1[1]));
                            tamosTodos++;
                            break;
                        case "cantProveedores":
                            this.setCantProveedores(Integer.valueOf(array1[1]));
                            tamosTodos++;
                            break;
                        case "cantRegusr":
                            this.setCantRegusr(Integer.valueOf(array1[1]));
                            tamosTodos++;
                            break;
                        case "Fcost":
                              this.setFcost(Double.valueOf(array1[1]));
                            break;
                            case "Qos":
                                this.setQos(Double.valueOf(array1[1]));
                            break;
                        case "yReservadas":
                            // dc valor
                            this.yReservadas[Integer.valueOf(array1[1])-1]=Integer.valueOf(array1[2]);
                            break;
                        case "yUsadas":
                            // dc t valor
                            this.yUsadas[Integer.valueOf(array1[1])-1][Integer.valueOf(array1[2])-1]=Integer.valueOf(array1[3]);
                            break;
                        case "xMapeoVideos":
                            // dc v valor
                            this.xMapeoVideos[Integer.valueOf(array1[1])-1][Integer.valueOf(array1[2])-1]=Integer.valueOf(array1[3]);
                            this.xMapeoVideosCDN[Integer.valueOf(array1[2])-1][Integer.valueOf(array1[1])-1]=Integer.valueOf(array1[3]);
                            break;
                        case "vmCantVms":
                            // prov dc t valor
                            this.vmCantVms[Integer.valueOf(array1[1])-1][Integer.valueOf(array1[2])-1][Integer.valueOf(array1[3])-1]=Integer.valueOf(array1[4]);
                            break;
                        case "zRuteo":
                            // dc v t reg valor
                            this.zRuteo[Integer.valueOf(array1[1])-1][Integer.valueOf(array1[2])-1][Integer.valueOf(array1[3])-1][Integer.valueOf(array1[4])-1]=Integer.valueOf(array1[5]);
                            break;
                        default:
                            break;
                    }

                    if(tamosTodos==5){
                        this.yReservadas = new int[cantDC];
                        this.yUsadas = new int[cantDC][cantHoras];
                        this.xMapeoVideos = new int[cantDC][cantVideos];
                        this.xMapeoVideosCDN = new int[cantVideos][cantDC];
                        this.vmCantVms = new int[cantProveedores][cantDC][cantHoras * 60];
                        this.zRuteo = new int[cantDC][cantVideos][cantHoras * 60][cantRegusr];
                        this.solucCdn = new CdnItem();
                        this.solucCdn.setCant_centrosDatos(cantDC);
                        this.solucCdn.setCant_pasos_Horas(cantHoras);
                        this.solucCdn.setCant_contenidos(cantVideos);
                        this.solucCdn.setCant_regionesUsuarios(cantRegusr);
                        this.solucCdn.setCant_pasosTiempo(cantHoras*60);
                        this.solucCdn.setY_techo(yReservadas);
                        this.solucCdn.setY(yUsadas);
                        this.solucCdn.setX(xMapeoVideosCDN);

                        tamosTodos=0;



                    }
                }else{
                    System.out.println("No encontro token "+array1[0]);
                }
            }
/*
            for (int i = 0; i < this.cantDC; i++) {
                System.out.println("yReservadas==> " + this.yReservadas[i]);
            }
            System.out.println("yUsadas==> ");
            for (int i = 0; i < this.cantDC; i++) {
                for (int horaLeida = 0; horaLeida < cantHoras; horaLeida++) {
                    System.out.print(this.yUsadas[i][horaLeida]);
                }
                System.out.println("-");
            }
            System.out.println("xMapeoVideos==> ");
            for (int i = 0; i < this.cantDC; i++) {
                for (int video = 0; video < cantVideos; video++) {
                    System.out.print(this.xMapeoVideos[i][video]);
                }
                System.out.println("-");
            }

            System.out.println("vmCantVms[Prove][DC][minuto]");
            for (int proveedorLeido = 0; proveedorLeido < cantProveedores; proveedorLeido++) {
                for (int minutoLeido = 0; minutoLeido < cantHoras * 60; minutoLeido++) {
                    for (int dcLeido = 0; dcLeido < cantDC; dcLeido++) {
                        if (this.vmCantVms[proveedorLeido][dcLeido][minutoLeido] != 0) {

                            System.out.println("vmCantVms[" + proveedorLeido + "][" + dcLeido + "][" + minutoLeido + "]==>  " + this.vmCantVms[proveedorLeido][dcLeido][minutoLeido]);

                        };
                    }
                }
            }

            System.out.println("------------------------------");
            System.out.println("zRuteo[dc][vid][minuto][reg]");
            for (int dcLeido = 0; dcLeido < cantDC; dcLeido++) {
                for (int video = 0; video < cantVideos; video++)
                    for (int minutoLeido = 0; minutoLeido < cantHoras * 60; minutoLeido++) {
                        for (int regusr = 0; regusr < cantRegusr; regusr++) {
                            if (this.zRuteo[dcLeido][video][minutoLeido][regusr] != 0) {
                                System.out.println("zRuteo[" + dcLeido + "][" + video + "][" + minutoLeido + "][" + regusr + "]==>  " + this.zRuteo[dcLeido][video][minutoLeido][regusr]);
                            };
                        }
                    }
            }
*/



            for (int dcLeido = 0; dcLeido < cantDC; dcLeido++) {
                for (int video = 0; video < cantVideos; video++)
                    for (int minutoLeido = 0; minutoLeido < cantHoras * 60; minutoLeido++) {
                        for (int regusr = 0; regusr < cantRegusr; regusr++) {
                            if (this.zRuteo[dcLeido][video][minutoLeido][regusr]>0 && this.xMapeoVideos[dcLeido][video]==0) {
                                System.out.println("*********************************");
                                System.out.println("ERROR:");
                                System.out.println("zRuteo[" + dcLeido + "][" + video + "][" + minutoLeido + "][" + regusr + "]==>  " + this.zRuteo[dcLeido][video][minutoLeido][regusr]);
                                System.out.println("xMapeoVideos[" + dcLeido + "][" + video + "]==>  " +this.xMapeoVideos[dcLeido][video]);
                                System.out.println("*********************************");
                                throw new Exception("no factible soluc");
                            };
                        }
                    }
            }


//            #la cantidad de maquinas usadas deben ser al menos las reservadas
//                subject to PisoUsadasReservadas {t in Thoras,c in CENTROSDATOS}:
//                   yReservadas[c] <= yUsadas[c,t];

            for (int i = 0; i < this.cantDC; i++) {
                for (int horaLeida = 0; horaLeida < cantHoras; horaLeida++) {
                    if(this.yReservadas[i]>this.yUsadas[i][horaLeida]){
                        System.out.println("*********************************");
                        System.out.println("ERROR:  yReservadas[c] <= yUsadas[c,t];");
                        System.out.println("*********************************");
                        throw new Exception("no factible soluc");
                    }
                }
            }



            //(sum {p in PROVEEDORES} vmCantVms [p,c,t+minshoras]) <= yUsadas[c,t] ;

            for (int minutoLeido = 0; minutoLeido < cantHoras * 60; minutoLeido++) {
                int horaLeida = (int)Math.floor(minutoLeido/60f);
                for (int dcLeido = 0; dcLeido < cantDC; dcLeido++) {
                    int sumaVmsPorProveedor = 0;
                    String logger="";
                    for (int proveedorLeido = 0; proveedorLeido < cantProveedores; proveedorLeido++) {
                        logger+="vmCantVms[" + proveedorLeido + "][" + dcLeido + "][" + minutoLeido + "]==>  " + this.vmCantVms[proveedorLeido][dcLeido][minutoLeido]+"\n";
                        sumaVmsPorProveedor+=this.vmCantVms[proveedorLeido][dcLeido][minutoLeido];
                    }
                    if(yUsadas[dcLeido][horaLeida] < sumaVmsPorProveedor){
                        System.out.println("*********************************");
                        System.out.println("ERROR:  (sum {p in PROVEEDORES} "+sumaVmsPorProveedor+"==vmCantVms  <= yUsadas["+dcLeido+","+horaLeida+"] ;");
                        System.out.println(logger);
                        System.out.println("*********************************");
                        throw new Exception("no factible soluc");
                    }
                }
            }

            CdnInstancia inst = new  CdnInstancia( instancia);
            this.solucCdn.setCant_requests(inst.getCant_requests());
            this.solucCdn.initMapeoRequestsDcs();
int encontrados=0;
            for (int dcLeido = 0; dcLeido < cantDC; dcLeido++) {
                for (int video = 0; video < cantVideos; video++)
                    for (int minutoLeido = 0; minutoLeido < cantHoras * 60; minutoLeido++) {
                        for (int regusr = 0; regusr < cantRegusr; regusr++) {
                            if (this.zRuteo[dcLeido][video][minutoLeido][regusr]>0) {
                               //busco el idrequest por la clave [video][minutoLeido][regusr]
                                int cantRequestAtendidosIntante=this.zRuteo[dcLeido][video][minutoLeido][regusr];
                                for(int req=0;(req<inst.getCant_requests())&&(cantRequestAtendidosIntante>0);req++){
                                    int idvideo = inst.getVideoIdRequestsMatrix(req);
                                    int idregion = inst.getRegUsrIdRequestsMatrix(req);
                                    int idtick = inst.getTicRequestsMatrix(req);
                                  //  System.err.println(req+" sdsd "+idvideo+" ++++"+idregion+"******"+idtick);

                                    if(idvideo==video &&
                                       minutoLeido == idtick &&
                                       idregion==regusr && this.solucCdn.getMapeoRequestsDcsvalor(req)==-1){
                                        this.solucCdn.setMapeoRequestsDcsvalor(req,dcLeido);
                                      //  System.err.println(req+" ++++"+cantRequestAtendidosIntante);
                                        encontrados++;
                                        cantRequestAtendidosIntante--;
                                      //  throw new Exception(req+" ++++"+cantRequestAtendidosIntante);
                                    }

                                }
                            };
                        }
                    }
            }


            if(encontrados!=inst.getCant_requests()){
                throw new Exception("No encontro todo los requests encontrados="+encontrados+" total ="+inst.getCant_requests());
            }


            CdnFactibilizador  factibilizador = new CdnFactibilizador(inst);

            if(!factibilizador.isFactible(this.solucCdn,true)){
                throw new Exception("no factible soluc");
            }else{
                System.out.println("ES FACTIBLE LA SOLUC");
            }
            System.out.println(this.solucCdn.toString());


            /*
            subject to Qos:
                sum {t in Tminutos,c in CENTROSDATOS,v in K,r in REGIONESUSR} zRuteo[c,v,t,r]*qQos[c,r] <= umbralQoS;



                subject to CubrirDemanda {v in K,r in REGIONESUSR,t in Tminutos}:  #cubrir demanda de v para cada t para cada instante de tiempo
                  sum {c in CENTROSDATOS} zRuteo[c,v,t,r] = dDemanda[v,t,r];

                 #cada vm puede atender hasta CR request de  un proveedor por instante de tiempo
                 #me aseguro que existan suficientes vms como cubir demanda de cada proveedor
                 #recordar que el video pertenece a un unico proveedor
                subject to UnProvVMyCapacidadDeVM {t in Thoras,minshoras in MINUTOS_EN_HORA,c in CENTROSDATOS}:
                   (sum {p in PROVEEDORES} vmCantVms [p,c,t+minshoras]) <= yUsadas[c,t] ;
                #subject to UnProvVMyCapacidadDeVM {t in Thoras,c in CENTROSDATOS}:
                #   sum {p in PROVEEDORES} ceil((sum {m in MINUTOS_EN_HORA,v in K,r in REGIONESUSR} pVideosProveedor[v,p]*zRuteo[c,v,t+m,r])/CR) <= yUsadas[c,t] ;

                #me quedo con el primer entero mayor (terminaria tomando el menor posible por tratarse de un problema de minimizacion)
                subject to UnProvVMyCapacidadDeVM_Aux {tminutos in Tminutos,c in CENTROSDATOS,p in PROVEEDORES}:
                     (sum {v in K,r in REGIONESUSR} pVideosProveedor[v,p]*zRuteo[c,v,tminutos,r])/CR <= vmCantVms [p,c,tminutos];



                 #si se rutea un request de un video a un dc, entonces debe existir ese video en ese dc
                 subject to EstaVideoEnDC {v in K,r in REGIONESUSR,c in CENTROSDATOS,t in Tminutos}:
                 (zRuteo[c,v,t,r] / picoMaximoRequest) <= xMapeoVideos[c,v];

            */
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                input.close();
                writer.close();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }

        }
    }




    public int getCantDC() {
        return cantDC;
    }

    public void setCantDC(int cantDC) {
        this.cantDC = cantDC;
    }

    public int getCantHoras() {
        return cantHoras;
    }

    public void setCantHoras(int cantHoras) {
        this.cantHoras = cantHoras;
    }

    public int getCantVideos() {
        return cantVideos;
    }

    public void setCantVideos(int cantVideos) {
        this.cantVideos = cantVideos;
    }

    public int getCantProveedores() {
        return cantProveedores;
    }

    public void setCantProveedores(int cantProveedores) {
        this.cantProveedores = cantProveedores;
    }

    public int getCantRegusr() {
        return cantRegusr;
    }

    public void setCantRegusr(int cantRegusr) {
        this.cantRegusr = cantRegusr;
    }

    public double getFcost() {
        return Fcost;
    }

    public void setFcost(double fcost) {
        Fcost = fcost;
    }

    public double getQos() {
        return Qos;
    }

    public void setQos(double qos) {
        Qos = qos;
    }
}
