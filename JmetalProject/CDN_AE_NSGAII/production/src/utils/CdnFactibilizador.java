package utils;

import soluciones.CDNSolution;
import utils.cdn.*;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ggoni on 16/07/16.
 */
public class CdnFactibilizador {



    CdnInstancia instanciaSingleton;

    HashMap<Integer,DataCenter> C ;
    HashMap<Integer,VideoContent> K ;
    HashMap<Integer,GlobalRegion> GR ;
    HashMap<Integer,UserRegion> UR ;
    HashMap<Integer, ArrayList<QoS>> Q ;
    HashMap<Integer,ArrayList<ContentRequest>> R ;

    private JMetalRandom randomGenenerator ;

    int cant_contenidos    ;
    int cant_centrosDatos  ;
    int cant_pasosTiempo   ;
    int cant_pasosHora   ;
    int cant_regionesUsuarios ;
    double KS ;
    //int [][][][] zRuteo;

    public CdnFactibilizador(CdnInstancia instanciaDatos) {
        instanciaSingleton = instanciaDatos;
        C = instanciaSingleton.getC();
        K = instanciaSingleton.getK();
        GR = instanciaSingleton.getGR();
        UR = instanciaSingleton.getUR();
        Q = instanciaSingleton.getQ();
        R = instanciaSingleton.getR();

        cant_contenidos  =instanciaSingleton.getCant_contenidos()   ;
        cant_centrosDatos = instanciaSingleton.getCant_centrosDatos();
        cant_pasosTiempo = instanciaSingleton.getCant_pasosTiempo();
        cant_regionesUsuarios = instanciaSingleton.getCant_regionesUsuarios();
        KS=instanciaSingleton.getKS();
        cant_pasosHora=instanciaSingleton.getTiempoHoras();
        randomGenenerator = JMetalRandom.getInstance() ;
    }

    /**
     * precondicion, debe correrse antes factibilizarVmCumpleDemanda(CdnItem sol)
     * @param sol
     */
    public void factibilizarPisoMinimoReservadasUsadas(CdnItem sol) {
        //las reservadas deben ser menor o igual a las necesarias por la demanda
        for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
            int minimaCantidadVMusadas=Integer.MAX_VALUE;
            for (int tiempoStep = 0; tiempoStep < cant_pasosHora; tiempoStep++) {
                //se que la Y cubre la demanda porque ya se corrio la funcion factibilizarVmCumpleDemanda(CdnItem sol)
                if(minimaCantidadVMusadas > sol.getYvalor(centroDato,tiempoStep) ){
                    minimaCantidadVMusadas= sol.getYvalor(centroDato,tiempoStep);
                }
            }
            //las reservadas las hago igual al minimo de las reservadas
            if(minimaCantidadVMusadas < sol.getY_techovalor(centroDato)){
                sol.setY_techovalor(centroDato,minimaCantidadVMusadas);
            }
        }

    }
    public void factibilizarVmCumpleDemanda(CdnItem sol) {
        int dcSorteado;
        for (int tiempoStep = 0; tiempoStep < cant_pasosHora; tiempoStep++) {
            int demandaVmsHoraActual = instanciaSingleton.getCantMinimaVmsHoraValor(tiempoStep);

            int cantVmsTodosLosDCs =0;
            //sumo las vms usadas por los distintos dcs
            for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                cantVmsTodosLosDCs+=sol.getYvalor(centroDato,tiempoStep);
            }
            int demandaVmsHoraActual_Menos_cantVmsTodosLosDCs=demandaVmsHoraActual-cantVmsTodosLosDCs;
            //si hay deficit de vms en la solucion sorteo vms entre los dcs
            if(demandaVmsHoraActual_Menos_cantVmsTodosLosDCs>0){
                while (demandaVmsHoraActual_Menos_cantVmsTodosLosDCs!=0){
                    dcSorteado = JMetalRandom.getInstance().nextInt(0,cant_centrosDatos-1);
                    sol.setYvalor(dcSorteado,tiempoStep,sol.getYvalor(dcSorteado,tiempoStep)+1);
                    demandaVmsHoraActual_Menos_cantVmsTodosLosDCs--;
                }


            }

            //hago un sorteo de cuantas vms quito del exceso
            demandaVmsHoraActual_Menos_cantVmsTodosLosDCs= JMetalRandom.getInstance().nextInt(demandaVmsHoraActual_Menos_cantVmsTodosLosDCs,0);

            //si hay exceso de vms en la solucion sorteo restas de vms entre los dcs
            if(demandaVmsHoraActual_Menos_cantVmsTodosLosDCs<0){
                while (demandaVmsHoraActual_Menos_cantVmsTodosLosDCs!=0){
                    dcSorteado = JMetalRandom.getInstance().nextInt(0,cant_centrosDatos-1);
                    if(sol.getYvalor(dcSorteado,tiempoStep)>0){
                        sol.setYvalor(dcSorteado,tiempoStep,sol.getYvalor(dcSorteado,tiempoStep)-1);
                        demandaVmsHoraActual_Menos_cantVmsTodosLosDCs++;
                    }
                }
            }
        }

    }

    public boolean isFactibleYcubreDemandaVms(CdnItem sol) {
        boolean EsFactibleSoluc = true;
        for (int tiempoStep = 0; tiempoStep < cant_pasosHora; tiempoStep++) {
            int demandaVmsHoraActual = instanciaSingleton.getCantMinimaVmsHoraValor(tiempoStep);

            int cantVmsTodosLosDCs =0;
            //sumo las vms usadas por los distintos dcs
            for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                cantVmsTodosLosDCs+=sol.getYvalor(centroDato,tiempoStep);
            }
            int demandaVmsHoraActual_Menos_cantVmsTodosLosDCs=demandaVmsHoraActual-cantVmsTodosLosDCs;
            //si hay deficit de vms en la solucion sorteo vms entre los dcs
            if(demandaVmsHoraActual_Menos_cantVmsTodosLosDCs>0){

                System.out.println("deficit de vms faltan=" + demandaVmsHoraActual_Menos_cantVmsTodosLosDCs + " en el tiempo=" + tiempoStep);
                EsFactibleSoluc = false;
            }

        }
return EsFactibleSoluc;
    }

    public boolean isFactible(CdnItem sol,Boolean considerarZ) {
        boolean EsFactibleSoluc = true;
        int [][] X=sol.getX();
        int [][] Y=sol.getY();
        int [] Y_techo=sol.getY_techo();

        String separete ="    ";


//#respetar calidad de servicio
//        subject to Qos:
//        sum {t in Tminutos,c in CENTROSDATOS,v in K,r in REGIONESUSR} zRuteo[c,v,t,r]*qQos[c,r] <= umbralQoS;



//#la cantidad de maquinas usadas deben ser al menos las reservadas
//        subject to PisoUsadasReservadas {t in Thoras,c in CENTROSDATOS}:
//        yReservadas[c] <= yUsadas[c,t];
        for (int tiempoStep = 0; tiempoStep < cant_pasosHora; tiempoStep++) {
            for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                    if (Y[centroDato][tiempoStep] < (Y_techo[centroDato])) {
                        System.out.println("Y[centroDato][min]");
                        System.out.println(separete + "Y[centroDato][tiempoStep] < (Y_techo[centroDato])");
                        System.out.println(separete + "Y[" + centroDato + "][" + tiempoStep + "] =" + Y[centroDato][tiempoStep]);
                        System.out.println(separete + "Y_techo[" + centroDato + "]=" + Y_techo[centroDato]);
                        EsFactibleSoluc = false;
                    }
                }}


            for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                for(int video=0;video<instanciaSingleton.getCant_contenidos();video++){
                    if ( X[video][centroDato]==1 ) {
                        boolean hayAlgunaVmEnElTiempoEnEsteDC=false;
                        for (int tiempoStep = 0;( tiempoStep < cant_pasosHora && !hayAlgunaVmEnElTiempoEnEsteDC); tiempoStep++) {
                            if (Y[centroDato][tiempoStep]>0 ) {
                                hayAlgunaVmEnElTiempoEnEsteDC=true;
                            }
                        }

                        if (!hayAlgunaVmEnElTiempoEnEsteDC ) {
                            System.out.println("Y[centroDato][min]");
                            System.out.println(separete + "(Y[centroDato][tiempoStep]<=0 && X[video][centroDato]==1 )");
                            System.out.println(separete + "Y[" + centroDato + "][.....] ");
                            System.out.println(separete+"X[" + video+"]["+centroDato+ "]=" + X[video][centroDato]);

                            EsFactibleSoluc = false;
                        }
                    }

                }
            }




            EsFactibleSoluc =  EsFactibleSoluc && isFactibleYcubreDemandaVms( sol);







        //if(zRuteo!=null) {
        /*************************************/
        //        subject to CubrirDemanda {v in K,r in REGIONESUSR,t in Tminutos}:  #cubrir demanda de v para cada t para cada instante de tiempo
        //        sum {c in CENTROSDATOS} zRuteo[c,v,t,r] = dDemanda[v,t,r];
        if(considerarZ) {
            for (int request = 0; request < sol.getCant_requests(); request++) {
                //tienen que estar todos los requests asignados
                int dcAsignado = sol.getMapeoRequestsDcsvalor(request);
                if (dcAsignado < 0) {
                    System.out.println("Request " + request + " no asignado. dcAsignado=" + dcAsignado);
                    EsFactibleSoluc = false;
                }
            }



        /*************************************/
        //        subject to UnProvVMyCapacidadDeVM {tminutos in Tminutos,c in CENTROSDATOS}:
        //        (sum {p in PROVEEDORES} vmCantVms [p,c,tminutos]) <= yUsadas[c,ceil(tminutos/60)] ;
        //#me quedo con el primer entero mayor (terminaria tomando el menor posible por tratarse de un problema de minimizacion)
        //        subject to UnProvVMyCapacidadDeVM_Aux {tminutos in Tminutos,c in CENTROSDATOS,p in PROVEEDORES}:
        //        (sum {v in K,r in REGIONESUSR} pVideosProveedor[v,p]*zRuteo[c,v,tminutos,r])/CR <= vmCantVms [p,c,tminutos];
        int [][] requestProveedorDc = new int[cant_centrosDatos][this.instanciaSingleton.getCant_proveedores()];
//        for(int request=0;request<sol.getCant_requests();request++){
//            int dcAsignado = sol.getMapeoRequestsDcsvalor(request);
//            int proveedor = this.instanciaSingleton.getProvRequestsMatrix(request);
//            requestProveedorDc[dcAsignado][proveedor]++;
//        }
        // aca sirve recorrer el esquema viejo que es por tiempo
            int [] picoVmPorHora = new int[cant_pasosHora];
        for(int tic=0;tic<cant_pasosTiempo;tic++){
            ArrayList<ContentRequest> listaRequests_en_tiempo_tic = R.get(tic);
            if(listaRequests_en_tiempo_tic!=null) {
                for (ContentRequest requestvideo : listaRequests_en_tiempo_tic) {
                    int proveedor = K.get(requestvideo.getVideocontentid()).getProviderid();
                    int idrequest = requestvideo.getIdrequest();
                    int dcAsignado = sol.getMapeoRequestsDcsvalor(idrequest);
                    requestProveedorDc[dcAsignado][proveedor]++;
                }
                int cantVmsEnEsteMinuto=0;
                for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                    for(int prove =0;prove<instanciaSingleton.getCant_proveedores();prove++){
                        cantVmsEnEsteMinuto+=(int)Math.ceil(requestProveedorDc[centroDato][prove]/(double)instanciaSingleton.getCR());
                    }
                }
                int horaActual = (int)Math.floor(tic/(double)60);
                //tengoq ue guardar el pico en una hora
                if(picoVmPorHora[horaActual]<cantVmsEnEsteMinuto){
                    picoVmPorHora[horaActual]=cantVmsEnEsteMinuto;
                }


            }

        }

        for( int hora =0;hora<cant_pasosHora;hora++){
            if(instanciaSingleton.getCantMinimaVmsHoraValor(hora)> picoVmPorHora[hora]){
                EsFactibleSoluc = false;
                System.out.println("Hora no cumple con CantMinimaVmsHoraValor");
                System.out.println("picoVmPorHora["+hora+"] "+picoVmPorHora[hora]);
                System.out.println("instanciaSingleton.getCantMinimaVmsHoraValor("+hora+") "+instanciaSingleton.getCantMinimaVmsHoraValor(hora));
            }
        }


//        int horaActual = (int)Math.floor(tic/(double)60);
//        if(cantMinimaVmsHora[horaActual]<sumaTodosVMSProvsEnMinuto){
//            cantMinimaVmsHora[horaActual]=sumaTodosVMSProvsEnMinuto;
//        }
//        6161Vid_300Mins_6Prove_7XreG_15XreQ
        /*************************************/
            //el pico de demanda por minuto por proveedor lo conozco con los requests
            // ver si se cubre esa demanda con alguna configuracion posible
            // #si se rutea un request de un video a un dc, entonces debe existir ese video en ese dc
            //        subject to EstaVideoEnDC {v in K,r in REGIONESUSR,c in CENTROSDATOS,t in Tminutos}:
            //        (zRuteo[c,v,t,r] / picoMaximoRequest) <= xMapeoVideos[c,v];
            for(int request=0;request<sol.getCant_requests();request++){
                int idvideoRequest= this.instanciaSingleton.getVideoIdRequestsMatrix(request);
                int dcAsignado = sol.getMapeoRequestsDcsvalor(request);
                if(X[idvideoRequest][dcAsignado]!=1){
                    System.out.println(separete+"X[" + idvideoRequest+"]["+dcAsignado+ "]=" + X[idvideoRequest][dcAsignado]);
                    EsFactibleSoluc = false;
                }
            }
        }
        return EsFactibleSoluc;
    }



   public boolean isFactible(CDNSolution sol) {
       if (true) {
           return true;
       }

boolean EsFactibleSoluc = true;
       CdnItem variableCDN = sol.getVariableValue(0);

       int [][] X=variableCDN.getX();
       int [][] Y=variableCDN.getY();
       int [] Y_techo=variableCDN.getY_techo();
      // int [][][][] Z=variableCDN.getZ();
String separete ="    ";
     //  System.out.println("----------Inicio check solucion--------------------------");

       for (int tiempoStep = 0; tiempoStep < cant_pasosTiempo; tiempoStep++) {
           //importante resetearl el hash aca
           HashMap<Integer,int []> requestsContenido_Region =new HashMap<>();
           //obtengo todos los requests de este paso de tiempo
           ArrayList<ContentRequest> requestsEnEsteInstante = R.get(tiempoStep);
           if(requestsEnEsteInstante!=null) {
               for (ContentRequest req: requestsEnEsteInstante) {
                   int videocontentid=req.getVideocontentid();
                   int globalregionid=req.getUserregionid();
                   if(requestsContenido_Region.containsKey(videocontentid)){
                       requestsContenido_Region.get(videocontentid)[globalregionid]+=1;
                   }else{
                       int [] requestsPorRegion = new int[cant_regionesUsuarios];
                       requestsPorRegion[globalregionid]=1;
                       requestsContenido_Region.put(videocontentid,requestsPorRegion);//// TODO: 16/07/16 este vector cargarlo en el singleton
                   }
               }

           }
//
//           //hay que recorrer el z entero porque puede ser que sobren cosas
//           for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
//               for (int regUsuaario = 0; regUsuaario < cant_regionesUsuarios;regUsuaario++) {
//                   for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
//
//                       //no cumple calidad de servicio
//                       if(variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario)>0 && ! instanciaSingleton.cumpleQOS(regUsuaario,centroDato) ){
//                           //no sirve ese Z, se descarta por calidad de servicio
//                           System.out.println("Z[regUsuaario][contenidoK][centroDato][tiempoStep]");
//                           System.out.println(separete+"No cumple calidad de servicio");
//                           System.out.println(separete+"Z["+regUsuaario+"]["+contenidoK+"]["+centroDato+"]["+tiempoStep+"]="+ variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario));
//                           EsFactibleSoluc= false;
//
//                       }
//
//                       //se guarda lo que seria la diferencia entre z y la demanda en el hash requestsContedido_Region
//                       if(requestsContenido_Region.get(contenidoK)!=null) {
//                           requestsContenido_Region.get(contenidoK)[regUsuaario]=requestsContenido_Region.get(contenidoK)[regUsuaario]-variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario);
//                       }else{
//                           if(variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario) > 0) {
//                               System.out.println("Z[regUsuaario][contenidoK][centroDato][tiempoStep]");
//                               System.out.println(separete+"no hay request para ese contenido de ninguna region, en este instante de tiempo, per z tiene valor");
//                               System.out.println(separete+"Z[" + regUsuaario + "][" + contenidoK + "][" + centroDato + "][" + tiempoStep + "]=" + variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario));
//                               EsFactibleSoluc= false;
//
//                           }
//                       }
//                   }
//               }
//           }

           for (Integer contenidoK : requestsContenido_Region.keySet()) {
               int [] diferencia_Entre_Z_y_Demanda_contenidoK = requestsContenido_Region.get(contenidoK);
               for (int regUsuaario = 0; regUsuaario < cant_regionesUsuarios;regUsuaario++) {
                   if(diferencia_Entre_Z_y_Demanda_contenidoK[regUsuaario]!=0){
                       System.out.println("Z[regUsuaario][contenidoK][centroDato][tiempoStep]");
                       System.out.println(separete+"Z No cubre la demanda o tiene MAS para el contenido: "+contenidoK+" desde la region de usuario: "+regUsuaario);
                       EsFactibleSoluc= false;

                   }

               }
           }


       }



//factibilizar X

//       for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
//           for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
//               int  hayRequest=0;
//               //si para alguna region o algun tiempo, hay un request de ese recurso
//               for (int regUsuaario = 0; (regUsuaario < cant_regionesUsuarios && hayRequest==0); regUsuaario++) {
//                   for (int tiempoStep = 0; (tiempoStep < cant_pasosTiempo && hayRequest==0); tiempoStep++) {
//
//                       if (variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario) > 0) {
//                           //la z ta usando recurso k del centro de datos, pero el x no lo tiene
//                           if(X[contenidoK][centroDato]==0){
//                               System.out.println("X[contenidoK][centroDato] Vs Z[regUsuaario][contenidoK][centroDato][tiempoStep]");
//                               System.out.println(separete+"Z[regUsuaario][contenidoK][centroDato][tiempoStep] la z ta usando recurso k del centro de datos, pero el x no lo tiene");
//                               System.out.println(separete+"Z[" + regUsuaario + "][" + contenidoK + "][" + centroDato + "][" + tiempoStep + "]=" + variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario));
//                               System.out.println(separete+"X[" + contenidoK+"]["+centroDato+ "]=" + X[contenidoK][centroDato]);
//                               EsFactibleSoluc= false;
//
//                           }
//
//                       }
//
//                   }
//               }
//
//           }
//       }
       int[] cantPorProveedor;
    //   int hora =0;
      // int cantVMs_Pico_en_Una_hora =0;
       //FACTIBILIZAR Y
       //int [] cantPorProveedor;
       for (int tiempoStep = 0; tiempoStep < cant_pasosTiempo; tiempoStep++) {

         //  hora=tiempoStep/60;

           for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {

            //   if(tiempoStep % 60 ==0) {
                   //se pide memoria aca para cantPorProveedor asi ya se inicializa con cero
                   //se hace cada minuto
                    cantPorProveedor = new int[instanciaSingleton.getCant_proveedores()];
              // }
               //siempre suma (ver que cada 60 minutos se resetea cantPorProveedor)
               for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
                   for (int regUsuaario = 0; regUsuaario < cant_regionesUsuarios; regUsuaario++) {
                       int idproveedorDelContenido = instanciaSingleton.getK().get(contenidoK).getProviderid();
                     //  cantPorProveedor[idproveedorDelContenido]+=variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario);
                   }
               }
               int cantVMs_minuto_Por_DC = 0;
               for (int provedor_id = 0; provedor_id < instanciaSingleton.getCant_proveedores(); provedor_id++) {
                   cantVMs_minuto_Por_DC += cantPorProveedor[provedor_id] / instanciaSingleton.getCR(); //division entera
                   //calculo el modulo y pido otra para usarla parcialmente si no da cero
                   if (cantPorProveedor[provedor_id] % instanciaSingleton.getCR() > 0) {
                       cantVMs_minuto_Por_DC++;
                   }
               }
             //  if (cantVMs_Pico_en_Una_hora<cantVMs_minuto_Por_DC){
             //      cantVMs_Pico_en_Una_hora=cantVMs_minuto_Por_DC;
            //   }


                //cada una hora chequeo el estado
              // if(tiempoStep % 60 ==0) {

//System.out.println("check Y en tiempo: "+tiempoStep + " Hora:"+hora+ " Centro dato: "+centroDato);
                       //distingo entre casos por si quiero hacer cosas distintas

                       if (Y[centroDato][tiempoStep] < cantVMs_minuto_Por_DC) {
                           //no se cubre la demanda de VMS hay que asignar mas en Y
                           System.out.println("Y[centroDato][hora]");
                           System.out.println(separete+"No se cubre la demanda de VMS hay que asignar mas en Y");
                           System.out.println(separete+"Y["+centroDato+"]["+tiempoStep+"] =" + Y[centroDato][tiempoStep] );
                           System.out.println(separete+"Cantidad de VMs minima:"+cantVMs_minuto_Por_DC);
                           EsFactibleSoluc= false;
                       }
                       if (Y[centroDato][tiempoStep] > cantVMs_minuto_Por_DC) {
                           System.out.println("OJO!!  Y[centroDato][hora]");
                           System.out.println(separete+"Se PASA de la la demanda de VMS");
                           System.out.println(separete+"Y["+centroDato+"]["+tiempoStep+"] =" + Y[centroDato][tiempoStep] );
                           System.out.println(separete+"Cantidad de VMs minima:"+cantVMs_minuto_Por_DC);



                       }


                       if (Y[centroDato][tiempoStep] < Y_techo[centroDato]) {
                           System.out.println("Y[centroDato][tiempoStep]");
                           System.out.println(separete+"  mal que  Y  < Y_techo");
                           System.out.println(separete+"Y["+centroDato+"]["+tiempoStep+"] =" + Y[centroDato][tiempoStep] );
                           System.out.println(separete+"Y_techo["+centroDato+"]=" + Y_techo[centroDato] );
                           EsFactibleSoluc= false;
                       }
// TODO: 21/07/16 contro que a nivel de horas se mantenga constante el nro de VMS. si alguna Y no vale Y_techo entonces los valores siguientes no pueden ser menor o igual a ese valor de Y durante una hora..(ver que no tiene porque mantenerse el valor ya que en cada minuto se podría alguilar una ondemand nueva, por eso se pide que sea mayor o igual)

                  // cantVMs_Pico_en_Una_hora=0;
            //   }

           }


       }



       for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
           List<Integer> ondemandVMs_Limit_Time_list = new ArrayList<>();
           for (int tiempoStep = 0; tiempoStep < cant_pasosTiempo; tiempoStep++) {

               int cantVMs_Ondemand_This_Time =0;
                for(int j=0; j<ondemandVMs_Limit_Time_list.size();j++){
                    //si hay VMs ondemand que esten vivas todavia..entonces las cuento
                    if(ondemandVMs_Limit_Time_list.get(j) >= tiempoStep) {
                        cantVMs_Ondemand_This_Time++;
                    }
                }

               if(Y[centroDato][tiempoStep]>(Y_techo[centroDato]+cantVMs_Ondemand_This_Time)){
                   // en este instante compraron algunas ondemands
                   int cantVMsOndemand_compradas_This_time = Y[centroDato][tiempoStep]-(Y_techo[centroDato]+cantVMs_Ondemand_This_Time);
                   for(int k=0; k<cantVMsOndemand_compradas_This_time;k++){
                       int limiteProximaHora=tiempoStep+59;
                       //agrego cada vm comprada ondemand y hasta cuando es valida
                       ondemandVMs_Limit_Time_list.add(limiteProximaHora);
                   }
               }

               if(Y[centroDato][tiempoStep]<(Y_techo[centroDato]+cantVMs_Ondemand_This_Time)){
                   System.out.println("Y[centroDato][min]");
                   System.out.println(separete+"Y[centroDato][tiempoStep] < (Y_techo[centroDato]+cantVMs_Ondemand_This_Time)");
                   System.out.println(separete+"Y["+centroDato+"]["+tiempoStep+"] =" + Y[centroDato][tiempoStep] );
                   System.out.println(separete+"cantVMs_Ondemand_This_Time:"+cantVMs_Ondemand_This_Time);
                   System.out.println(separete+"Y_techo["+centroDato+"]=" + Y_techo[centroDato] );
                   EsFactibleSoluc= false;
               }
           }
           ondemandVMs_Limit_Time_list.clear();
       }







//System.out.println("----------Fin check solucion--------------------------");
//





        return EsFactibleSoluc;
    }


    public void factibilizarSolucion(CDNSolution solucion) {
if (true){
    return;
}
        int[] cantPorProveedor;
        CdnItem variableCDN = solucion.getVariableValue(0);

        int[][] X = variableCDN.getX();
        int[][] Y = variableCDN.getY();
        int[] Y_techo = variableCDN.getY_techo();
     //   HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>[] Z_hash  = variableCDN.getZ_hash();
        CdnItem soluc_item_factibilizada;



//
//            CON LOS NUEVOS OPERADORES DE CRUZAMIENTO Z QUEDA FACTIBLE
//            QUEDA MAS EFICIENTE



//factibilizar X
//        for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
//            for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
//                int hayRequest = 0;
//                //si para alguna region o algun tiempo, hay un request de ese recurso
//                for (int regUsuaario = 0; (regUsuaario < cant_regionesUsuarios && hayRequest == 0); regUsuaario++) {
//                    for (int tiempoStep = 0; (tiempoStep < cant_pasosTiempo && hayRequest == 0); tiempoStep++) {
//
//                        if (variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario) > 0) {
//                            hayRequest = 1;
//                        }
//
//                    }
//                }
//                X[contenidoK][centroDato] = hayRequest;
//            }
//        }


        //FACTIBILIZAR Y

        //calcular lo que se necesita
        for (int tiempoStep = 0; tiempoStep < cant_pasosTiempo; tiempoStep++) {
            for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
                cantPorProveedor = new int[instanciaSingleton.getCant_proveedores()];
                //se calculan cantidad de requests por proveedor
                for (int contenidoK = 0; contenidoK < cant_contenidos; contenidoK++) {
                    for (int regUsuaario = 0; regUsuaario < cant_regionesUsuarios; regUsuaario++) {
                        int idproveedorDelContenido = instanciaSingleton.getK().get(contenidoK).getProviderid();
                        //todo optimizar este algoritmo con la nueva Z
                      //  cantPorProveedor[idproveedorDelContenido] += variableCDN.getValorZ(contenidoK,centroDato,tiempoStep,regUsuaario);
                    }
                }
                //se calcula la cantidad de VMs necesarias para cubrir la cant de requests por proveedor
                int cantVMs_minuto_Por_DC = 0;
                for (int provedor_id = 0; provedor_id < instanciaSingleton.getCant_proveedores(); provedor_id++) {
                    cantVMs_minuto_Por_DC += cantPorProveedor[provedor_id] / instanciaSingleton.getCR(); //division entera
                    //calculo el modulo y pido otra para usarla parcialmente si no da cero
                    if (cantPorProveedor[provedor_id] % instanciaSingleton.getCR() > 0) {
                        cantVMs_minuto_Por_DC++;
                    }
                }


                //CUBRIR DEMANDA DE VMS

                //si tengo menos vm de las que necesito, tengo que comprar
                if (Y[centroDato][tiempoStep] < cantVMs_minuto_Por_DC) {
                    int diff = cantVMs_minuto_Por_DC - Y[centroDato][tiempoStep];
                    double sorteoRandom = randomGenenerator.nextDouble();
                    //hay un 75% de prob de darle bola al t pico (para agregar diversidad)
                    if (sorteoRandom <= 0.75 && instanciaSingleton.is_t_pico(tiempoStep)) {
                        //si es pico, compro ondemand
                        for (int j = tiempoStep; j < tiempoStep + 60 && j < instanciaSingleton.getT(); j++) {
                            Y[centroDato][j] += diff;
                        }
                    } else {
                        //si no es pico compro reservada
                        for (int j = 0; j < instanciaSingleton.getT(); j++) {
                            Y[centroDato][j] += diff;
                        }
                        Y_techo[centroDato] += diff;
                    }
                } else {
                    //si tengo mas VM de las que preciso, recorto..
                    Y[centroDato][tiempoStep] = cantVMs_minuto_Por_DC;
                }
            }
        }

        // CUMPLIR RESTRICCION C1
        for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
            List<Integer> ondemandVMs_Limit_Time_list = new ArrayList<>();
            for (int tiempoStep = 0; tiempoStep < cant_pasosTiempo; tiempoStep++) {

                int cantVMs_Ondemand_This_Time = 0;
                for (int j = 0; j < ondemandVMs_Limit_Time_list.size(); j++) {
                    //si hay VMs ondemand que esten vivas todavia..entonces las cuento
                    if (ondemandVMs_Limit_Time_list.get(j) >= tiempoStep) {
                        cantVMs_Ondemand_This_Time++;
                    }
                }

                if (Y[centroDato][tiempoStep] > (Y_techo[centroDato] + cantVMs_Ondemand_This_Time)) {
                    // en este instante compraron algunas ondemands
                    int cantVMsOndemand_compradas_This_time = Y[centroDato][tiempoStep] - (Y_techo[centroDato] + cantVMs_Ondemand_This_Time);
                    for (int k = 0; k < cantVMsOndemand_compradas_This_time; k++) {
                        int limiteProximaHora = tiempoStep + 59;
                        //agrego cada vm comprada ondemand y hasta cuando es valida
                        ondemandVMs_Limit_Time_list.add(limiteProximaHora);
                    }
                }

                if (Y[centroDato][tiempoStep] < (Y_techo[centroDato] + cantVMs_Ondemand_This_Time)) {
                    Y[centroDato][tiempoStep] = (Y_techo[centroDato] + cantVMs_Ondemand_This_Time); // TODO: 22/07/16 pensar esto
                }
            }
            ondemandVMs_Limit_Time_list.clear();
        }




        //se sobreesctiben las variables cdnitem con los nuevos valores
         soluc_item_factibilizada = new CdnItem(cant_contenidos, cant_centrosDatos, cant_pasosTiempo, cant_regionesUsuarios, X, Y, Y_techo);
         solucion.setVariableValue(0, soluc_item_factibilizada);

        if(!isFactible(solucion)){
//            throw new JMetalException("no factible");
        }
    }


//    public int[][][][] getzRuteo() {
//        return zRuteo;
//    }
//
//    public void setzRuteo(int[][][][] zRuteo) {
//        this.zRuteo = zRuteo;
//    }
}
