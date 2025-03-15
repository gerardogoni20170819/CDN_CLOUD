package utils;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;

import sun.plugin.javascript.navig.Array;
import utils.cdn.ContentRequest;
import utils.cdn.DataCenter;
import utils.cdn.GlobalRegion;
import utils.cdn.QoS;
import utils.cdn.UserRegion;
import utils.cdn.VideoContent;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import utils.CdnFactibilizador;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * Created by ggoni on 16/07/16.
 */
public class CdnInstancia {
    private HashMap<Integer,ArrayList<int[][]>> VMM;
    private HashMap<Integer,DataCenter> C ;
    private HashMap<Integer,VideoContent> K ;
    private HashMap<Integer,GlobalRegion> GR ;
    private HashMap<Integer,UserRegion> UR ;
    private HashMap<Integer,ArrayList<QoS>> Q;
    private HashMap<Integer,ArrayList<ContentRequest>> R ;
    private ArrayList<Integer> P;
    private int cant_contenidos;
    private int cant_centrosDatos;
    private int cant_pasosTiempo;
    private int cant_regionesUsuarios;
    private int cant_proveedores;
    private int T;
    private int cantiadPromedioPorCategoria;
    double KS;
    int CR ;
    private int [] martriz_demanda_pico;
    int tiempoHoras;
    int [][] requestsMatrix;
    int [] cantMinimaVmsHora;
    private ArrayList<DataCenter> dataCentersOrdenadosCosto;
    ArrayList<CdnItem> soluicionesInicialesOncost=null;
    // necesarios para el greedyOnCostUmbralRandom
    private boolean yaEncontroElMinimoUmbralQoSConSolucion=false;
    ArrayList<Double> QoSordenados;
    int indiceMenorQoSConSolucion=0;
    // ***************************
    int cantMaximaVmRequerida=0;
    int demanda_limite=0;
    /*parametros que hay que setear se setean antes de pedir un readInstance*/
    //private   String rutaInstancia;
    private   String rutaAbsolutaYnombreArchivoRCD;
    private   String rutaAbsolutaYnombreArchivoRU;
    private   String rutaAbsolutaYnombreArchivoQoS;
    private   String rutaAbsolutaYnombreArchivoDocsVideos;
    private   String rutaAbsolutaYnombreArchivoWorkLoadVideos;
    private   String rutaAbsolutaSalidaInstancia;
    public   String basedir;
    private   int greedyDeterministic_level = 0;
    private   double alfa_Qos ;
    private   int largoRankingAgrupaVideos;
    private   double tamanioBloqueVideo ;
    private   double coef_demanda_pico ;
    private   int cant_requests;
    private int nro_evaluacion;
    public String descripcionInstancia;
    public String marcaInicioInicializacionGreedy="NOSET";
    public String marcaFinInicializacionGreedy;

    public  String basedirUBUNTU="/home/ggoni/Documentos/PROYE/solocodigo/proycodigo";
    public  String basedirWINDOWS="F:/PROYE/solocodigo/proycodigo";
    public  String basedirCluster="/home/gerardo.goni";



public String determinarBasedir(){
    String rutaResources = "/resources" ;
    basedir=basedirUBUNTU/*propGeneral.getProperty("basedirUBUNTU")*/;
    Path path=Paths.get(basedir);

    if (!Files.exists(path)) {
        basedir=basedirWINDOWS/*propGeneral.getProperty("basedirWINDOWS")*/;
        path=Paths.get(basedir);
        if (!Files.exists(path)) {
            basedir=basedirCluster/*propGeneral.getProperty("basedirCluster")*/;

        }
    }

    rutaAbsolutaSalidaInstancia=basedir;
    basedir=basedir+rutaResources;

    path=Paths.get(basedir);
    if (!Files.exists(path)) {
        throw new JMetalException("No se encotro la ruta basedir..."+basedir);
    }
    return basedir;
}


    public int getPrimerIdLibre(){
//// TODO: 23/04/2017 si no encuentra mas memoria que vaya pidiendo mas
        return JMetalRandom.getInstance().nextInt(0,50);
    }



    public double getTamanioBloqueVideo() {
        return tamanioBloqueVideo;
    }

    public int getCantMinimaVmsHoraValor(int hora) {
        return cantMinimaVmsHora[hora];
    }

    public CdnInstancia() {

    }

    public void leerPropertiesInstancia(String nombreArchivoPropertiesInstancia){
        QoSordenados = new ArrayList<>();
        Properties prop = new Properties();
        Properties propGeneral = new Properties();
        InputStream input = null;
        InputStream inputGeneral = null;
        descripcionInstancia="Instancia : "+nombreArchivoPropertiesInstancia+"\n";
        System.out.println("Cargando instancia "+nombreArchivoPropertiesInstancia);

        try {



            basedir =this.determinarBasedir();



            input = new FileInputStream(basedir+"/propertiesdir/"+nombreArchivoPropertiesInstancia+".properties");
            inputGeneral = new FileInputStream(basedir+"/propertiesdir/general.properties");

            // load a properties file
            prop.load(input);
            propGeneral.load(inputGeneral);
            descripcionInstancia+="Ruta base: "+basedir+"\n";;
            rutaAbsolutaSalidaInstancia=rutaAbsolutaSalidaInstancia+prop.getProperty("rutaAbsolutaSalidaInstancia");
            descripcionInstancia+="Ruta salida: "+rutaAbsolutaSalidaInstancia+"\n";;
            alfa_Qos=Double.valueOf(prop.getProperty("alfa_Qos"));






            //   rutaInstancia=System.getProperty("user.dir")+"/instancias/"+prop.getProperty("instanciaUtilizada");
            //  rutaInstancia= rutaInstancia.replace("/", File.separator);
            //  rutaInstancia=rutaInstancia.replace("\\", "/");

            rutaAbsolutaYnombreArchivoRCD=basedir+prop.getProperty("rutaAbsolutaYnombreArchivoRCD");
            rutaAbsolutaYnombreArchivoRU=basedir+prop.getProperty("rutaAbsolutaYnombreArchivoRU");
            rutaAbsolutaYnombreArchivoQoS=basedir+prop.getProperty("rutaAbsolutaYnombreArchivoQoS");
            rutaAbsolutaYnombreArchivoDocsVideos=basedir+prop.getProperty("rutaAbsolutaYnombreArchivoDocsVideos");
            rutaAbsolutaYnombreArchivoWorkLoadVideos=basedir+prop.getProperty("rutaAbsolutaYnombreArchivoWorkLoadVideos");



            greedyDeterministic_level = Integer.valueOf(prop.getProperty("greedyDeterministic_level"));
            largoRankingAgrupaVideos = Integer.valueOf(prop.getProperty("largoRankingAgrupaVideos"));
            tamanioBloqueVideo  = Double.valueOf(prop.getProperty("tamanioBloqueVideo"));
            coef_demanda_pico = Double.valueOf(prop.getProperty("coef_demanda_pico"));

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


    public CdnInstancia(String nombreArchivoPropertiesInstancia) {
        QoSordenados = new ArrayList<>();

        this.leerPropertiesInstancia( nombreArchivoPropertiesInstancia);

        C = new HashMap<Integer, DataCenter>();
         K = new HashMap<Integer, VideoContent>();
         GR = new HashMap<Integer, GlobalRegion>();
         UR = new HashMap<Integer, UserRegion>();
         Q = new HashMap<Integer, ArrayList<QoS>>();
         R = new HashMap<Integer, ArrayList<ContentRequest>>();
         P = new ArrayList<Integer>();
         
         readInstance("dc.1","docs.video","reg.0","reg_users.0","qos.2","workload.video");
        System.out.println("instancia "+nombreArchivoPropertiesInstancia+" cargada..");
        cant_contenidos = K.size()     ;
        cant_centrosDatos = C.size() ;
        cant_regionesUsuarios = UR.size();
        cant_pasosTiempo = T;
        cant_proveedores = P.size();

        cantiadPromedioPorCategoria=largoRankingAgrupaVideos;
        KS = cantiadPromedioPorCategoria*tamanioBloqueVideo;
        //se asume tarjeta de red de 1Gbit/seg
        // 1Gbit/seg = 0.125 1Gbytes/seg = 0.125*10^9bits/seg
        CR=(int) ((0.125f*1000000000f)/tamanioBloqueVideo)*60 /*se pasa a minutos*/ ;
        tiempoHoras=(int)Math.ceil(cant_pasosTiempo/(double)60);
        //redondeo para que el tiempo en minutos sea cantidad de horas justas
        cant_pasosTiempo=tiempoHoras*60;
       // System.out.println("CR=="+CR);

        if(true) {
            descripcionInstancia+="Instancia cargada desde "+"\n";;
            descripcionInstancia+=rutaAbsolutaYnombreArchivoRCD+"\n";;
            descripcionInstancia+=rutaAbsolutaYnombreArchivoRU+"\n";;
            descripcionInstancia+=rutaAbsolutaYnombreArchivoQoS+"\n";;
            descripcionInstancia+=rutaAbsolutaYnombreArchivoDocsVideos+"\n";;
            descripcionInstancia+=rutaAbsolutaYnombreArchivoWorkLoadVideos+"\n";;
            descripcionInstancia+="largoRankingAgrupaVideos "+largoRankingAgrupaVideos+"\n";;
            descripcionInstancia+="tamanioBloqueVideo "+tamanioBloqueVideo+"\n";;
            descripcionInstancia+="Cantidad de requests: "+cant_requests+"\n";;
            descripcionInstancia+="Cantidad de videos: " + K.size()+"\n";;
            descripcionInstancia+="Cantidad de cento de datos: " + C.size()+"\n";;
            descripcionInstancia+="Cantidad de regiones de usuario: " + UR.size()+"\n";;
            descripcionInstancia+="Cantidad de metricas de QoS: " + Q.size()+"\n";;
            descripcionInstancia+="Cantidad de proveedores: " + P.size()+"\n";;
            descripcionInstancia+="Horas    : " + tiempoHoras+"\n";;

            System.out.println(descripcionInstancia);


        }


        this.construirRequestsMatrix();
        this.calcularCantMinimaVmsHora();


    }


    public int getTiempoHoras() {
        return tiempoHoras;
    }



    public    double getCoef_demanda_pico() {
        return coef_demanda_pico;
    }

    public    void setCoef_demanda_pico(double coef_demanda_pico_param) {
        coef_demanda_pico = coef_demanda_pico_param;
    }

    public    void setTamanioBloqueVideo(double tamanioBloqueVideoParam) {
        tamanioBloqueVideo = tamanioBloqueVideoParam;
    }

    public    int getLargoRankingAgrupaVideos() {
        return largoRankingAgrupaVideos;
    }

    public    void setLargoRankingAgrupaVideos(int largoRankingAgrupaVideos_param) {
        largoRankingAgrupaVideos = largoRankingAgrupaVideos_param;
    }

    public  double getAlfa_Qos() {
        return alfa_Qos;
    }

    public  void setAlfa_Qos(double alfa_Qosparam) {
        alfa_Qos = alfa_Qosparam;
    }

//    public  String getRutaInstancia() {
//        return rutaInstancia;
//    }
//
//    public  void setRutaInstancia(String rutaInstanciaParam) {
//        rutaInstancia = rutaInstanciaParam;
//    }



    public ArrayList<Integer> getP() {
		return P;
	}

	public int getCR() {
		return CR;
	}

	public int getCant_proveedores() {
		return cant_proveedores;
	}

	public double getKS() {
        return KS;
    }

    public void setKS(double KS) {
        this.KS = KS;
    }

    public HashMap<Integer, DataCenter> getC() {
        return C;
    }

    public void setC(HashMap<Integer, DataCenter> c) {
        C = c;
    }

    public HashMap<Integer, VideoContent> getK() {
        return K;
    }

    public void setK(HashMap<Integer, VideoContent> k) {
        K = k;
    }

    public HashMap<Integer, GlobalRegion> getGR() {
        return GR;
    }

    public void setGR(HashMap<Integer, GlobalRegion> GR) {
        this.GR = GR;
    }

    public HashMap<Integer, UserRegion> getUR() {
        return UR;
    }

    public void setUR(HashMap<Integer, UserRegion> UR) {
        this.UR = UR;
    }

    public HashMap<Integer, ArrayList<QoS>> getQ() {
        return Q;
    }

    public void setQ(HashMap<Integer, ArrayList<QoS>> q) {
        Q = q;
    }

    public HashMap<Integer, ArrayList<ContentRequest>> getR() {
        return R;
    }

    public void setR(HashMap<Integer, ArrayList<ContentRequest>> r) {
        R = r;
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

    public void readInstance(String C_file, String K_file, String GR_file, String UR_file, String Q_file, String R_file) {
        BufferedReader br = null;
        String line = "";


        try {
            //Get Data Centers

            br = new BufferedReader(new FileReader(rutaAbsolutaYnombreArchivoRCD/*rutaInstancia+C_file*/));
            while ((line = br.readLine()) != null) {
                DataCenter x = new DataCenter(line);

                C.put(x.getId(),x);
            }


            dataCentersOrdenadosCosto = new ArrayList<DataCenter>(C.values());
            dataCentersOrdenadosCosto=this.ordenarDatacentersMasBaratosCostoBruto(dataCentersOrdenadosCosto);
//            System.out.println("dcs ordendos");
//            for (DataCenter dpepe: dataCentersOrdenadosCosto
//                 ) {
//                System.out.println(dpepe.getCostroBruto());
//            }
            HashMap<Integer,VideoContent> K_idOriginales = new HashMap<Integer, VideoContent>();


            //Get Video Content
            br = new BufferedReader(new FileReader(/*rutaInstancia+K_file)*/rutaAbsolutaYnombreArchivoDocsVideos));
            while ((line = br.readLine()) != null) {
                VideoContent x = new VideoContent(line);
                K_idOriginales.put(x.getId(),x);
                if(!P.contains(x.getProviderid())) {
                	P.add(x.getProviderid());
                }
            }

            //se agrupan los videos por proveedor y por categorias dentro del proveedor
            //los videos se encuentran ordenados por popularidad en el archivo, asi que esto seria un ranking por popularidad
            HashMap<Integer,Integer> mapeo_IdVideoOriginal_IdCategoria = new HashMap<>();
            int cantProveedores = P.size();
            int idCategoriaAgrupamiento =0;
            for(int idProveedor : P){
                int catnVideosEnProveedorActual=0;
                for (Map.Entry<Integer, VideoContent> entry : K_idOriginales.entrySet()) {
                    Integer key = entry.getKey();
                    VideoContent video = entry.getValue();
                    //el video pertenece a ese proveedor.
                    if(idProveedor==video.getProviderid()){
                        if(catnVideosEnProveedorActual==0){
                            VideoContent video_categoria = new VideoContent(idCategoriaAgrupamiento, tamanioBloqueVideo*largoRankingAgrupaVideos, idProveedor);
                            K.put(idCategoriaAgrupamiento, video_categoria);
                        }
                        catnVideosEnProveedorActual++;
                        //si la cantidad de videos es mayora que el largo del ranking, paso a otra categoria
                        if (catnVideosEnProveedorActual % largoRankingAgrupaVideos==0){
                            VideoContent video_categoria = new VideoContent(idCategoriaAgrupamiento, tamanioBloqueVideo*largoRankingAgrupaVideos, idProveedor);
                            idCategoriaAgrupamiento++;
                            //guardo en k el video, que tiene el id de la categoria en vez del video original
                            K.put(idCategoriaAgrupamiento, video_categoria);

                        }
                        mapeo_IdVideoOriginal_IdCategoria.put(video.getId(),idCategoriaAgrupamiento);
//System.err.println("proveddor "+idProveedor+" categoria= "+idCategoriaAgrupamiento+ " idoriginal="+video.getId());
                    }
                }
                //las categorias no se comparten entre proveedores
                //por eso la incremento aunque pueda no estar completa
                idCategoriaAgrupamiento++;
            }

  /*

            if(K_idOriginales.size()<largoRankingAgrupaVideos*cantProveedores){
                throw new JMetalException("Cantidad de contenidos no puede ser menor a largoRankingAgrupaVideos*cantProveedores");
            }

            for (HashMap.Entry<Integer, VideoContent> entry : K_idOriginales.entrySet()) {
                Integer key = entry.getKey();
                VideoContent video = entry.getValue();
              //  System.out.println("key "+key +" video "+video.getId());
                int nuevoIdVideo = nuevoIdSegunRankingPorProveedor(video.getProviderid(), video.getId(), largoRankingAgrupaVideos);
               // System.out.println("        key "+key +" nuevoIdVideo "+ nuevoIdVideo);
                if(!K.containsKey(nuevoIdVideo)) {
                    //el video tiene el id de la catgoria a la que pertenece
                    VideoContent x = new VideoContent(nuevoIdVideo, video.getSize(), video.getProviderid()); //(int id, double size, int providerid)
                    //guardo en k el video, que tiene el id de la categoria en vez del video original
                    K.put(nuevoIdVideo, x);
                }
            }
*/

            //Get Global Regions
//            br = new BufferedReader(new FileReader(rutaInstancia+GR_file));
//            while ((line = br.readLine()) != null) {
//                GlobalRegion x = new GlobalRegion(line);
//                GR.put(x.getId(),x);
//            }
            //Get User Regions
            br = new BufferedReader(new FileReader(rutaAbsolutaYnombreArchivoRU/*rutaInstancia+UR_file*/));
            while ((line = br.readLine()) != null) {
                UserRegion x = new UserRegion(line);
                UR.put(x.getId(),x);
            }
            //Get QoS
            br = new BufferedReader(new FileReader(rutaAbsolutaYnombreArchivoQoS/*rutaInstancia+Q_file*/));
            while ((line = br.readLine()) != null) {
                QoS x = new QoS(line);
                if(Q.containsKey(x.getUserregionid())) {
                    Q.get(x.getUserregionid()).add(x);
                } else {
                    ArrayList<QoS> l = new ArrayList<QoS>();
                    l.add(x);
                    Q.put(x.getUserregionid(),l);
                }
                QoSordenados.add(x.getMetric());
                //engancho la qos en cada datacenter
                C.get(x.getDatacenterid()).agregarRegionUsuarioQoS(x.getUserregionid(),x);
            }

            //se ordenan los QoS para usarlos en el greedy
            Collections.sort(QoSordenados, new Comparator<Double>() {
                @Override
                public int compare(Double QoS1, Double QoS2) {
                    if (QoS1 > QoS2)
                        return 1;
                    if (QoS1 < QoS2)
                        return -1;
                    return 0;
                }
            });


            //se ordenan los DCs para cada region de usuario segun QoS
            for (Map.Entry<Integer,ArrayList<QoS>> entry : Q.entrySet()) {
                Integer key = entry.getKey();
                ArrayList<QoS> listaQoS = entry.getValue();
                Q.put(key,ordenadosPorQoS(listaQoS));

//                for(QoS paimprimir : Q.get(key)){
//                    paimprimir.debug();
//                }

            }

         //   int [] requestPorHOra = new int[10000];
         //   int [] requestPorMinutos = new int[10000];

            //Get Requests
            int time_in_min = 0;
            br = new BufferedReader(new FileReader(/*rutaInstancia+R_file*/rutaAbsolutaYnombreArchivoWorkLoadVideos));
            while ((line = br.readLine()) != null) {
                ContentRequest x = new ContentRequest(line);
                cant_requests++;
                //esto es lo que agrupa los videos.....
               // VideoContent videoOriginal = K_idOriginales.get(x.getVideocontentid());
                int nuevoIdVideo =  mapeo_IdVideoOriginal_IdCategoria.get(x.getVideocontentid()) ;
                x.setVideocontentid(nuevoIdVideo);
                // fin de agrupamiento de los videos

                time_in_min = (int) Math.floor(x.getRequest_arrival_time()/(double)60);

                x.setRequest_arrival_time(time_in_min);
                if(R.containsKey(time_in_min)) {
                    R.get(time_in_min).add(x);
                } else {
                    ArrayList<ContentRequest> l = new ArrayList<ContentRequest>();
                    l.add(x);
                    R.put(x.getRequest_arrival_time(),l);
                }
            }
            this.T = time_in_min;
            cant_pasosTiempo=(int)Math.ceil(time_in_min/(double)60);
            cant_pasosTiempo=cant_pasosTiempo*60;//cantidad de minutos q suman justo una hora
            mapeo_IdVideoOriginal_IdCategoria.clear();
            mapeo_IdVideoOriginal_IdCategoria=null;
            K_idOriginales.clear();
            K_idOriginales=null;

        } catch (Exception e) {
            e.printStackTrace();
        }


        
       // calcular_t_picos();

        /*ArrayList<DataCenter> ldc = mejoresDataCenterSegunQOS(4,2);
        DataCenter dc = (DataCenter) elijoUnoRandomConProbUniforme(ldc);
        dc.debug();*/
        
    }


    public void calcularCantMinimaVmsHora(){
        int [][] requestProveedorMinuto = new int[this.cant_proveedores][this.cant_pasosTiempo];
        cantMinimaVmsHora = new int[this.tiempoHoras];
        for(int tic=0;tic<cant_pasosTiempo;tic++){
            ArrayList<ContentRequest> listaRequests_en_tiempo_tic = R.get(tic);
            if(listaRequests_en_tiempo_tic!=null) {
                for (ContentRequest requestvideo : listaRequests_en_tiempo_tic) {
                    int idvideo = requestvideo.getVideocontentid();
                    VideoContent video = this.getK().get(idvideo);
                    int idproveedor = video.getProviderid();
                    requestProveedorMinuto[idproveedor][tic]++;
                }
            }


        }
        // me tengo que quedar con el pico de demanda en alguno de los 60 minutos de la hora
        // es necesario que se cuenten los provs por separado
        for(int tic=0;tic<cant_pasosTiempo;tic++) {
            int sumaTodosVMSProvsEnMinuto=0;
            for (int prov = 0; prov < cant_proveedores; prov++) {
                sumaTodosVMSProvsEnMinuto+=(int)Math.ceil(requestProveedorMinuto[prov][tic]/(double)CR);
            }
            int horaActual = (int)Math.floor(tic/(double)60);
            if(cantMinimaVmsHora[horaActual]<sumaTodosVMSProvsEnMinuto){
                cantMinimaVmsHora[horaActual]=sumaTodosVMSProvsEnMinuto;
            }
            if(this.cantMaximaVmRequerida<cantMinimaVmsHora[horaActual]){
                this.cantMaximaVmRequerida=cantMinimaVmsHora[horaActual];
            }


          //  System.out.println("cantMinimaVmsHora[horaActual] "+cantMinimaVmsHora[horaActual]);
        }
    }





    public void construirRequestsMatrix(){

        requestsMatrix=new int[cant_requests][4];
        int nuevoIdRequest=0;
     //   System.err.println( "cant_pasosTiempo //"+cant_pasosTiempo);
        for(int tic=0;tic<cant_pasosTiempo;tic++){
            ArrayList<ContentRequest> listaRequests_en_tiempo_tic = R.get(tic);
            if(listaRequests_en_tiempo_tic!=null) {
                for (ContentRequest requestvideo : listaRequests_en_tiempo_tic) {
                    this.setTicRequestsMatrix(tic,nuevoIdRequest);
                    this.setVideoIdRequestsMatrix(requestvideo.getVideocontentid(),nuevoIdRequest);
                    this.setRegUsrIdRequestsMatrix(requestvideo.getUserregionid(),nuevoIdRequest);
                    this.setProvRequestsMatrix(this.getK().get(requestvideo.getVideocontentid()).getProviderid(),nuevoIdRequest);
                    requestvideo.setIdrequest(nuevoIdRequest);
                    nuevoIdRequest++;
                 //   System.err.println(nuevoIdRequest+"//"+tic+" "+requestvideo.getVideocontentid()+" "+requestvideo.getUserregionid());
                }
            }

        }
    }


    //param pVideosProveedor {K,PROVEEDORES} binary;
    public String get_pVideosProveedor(){
        String matriz="param pVideosProveedor: ";
        String listaIdProvedoresShifteadosEnUno="";
        ArrayList<Integer> ordenados = P ;
        Collections.sort(ordenados);
        //hay que correr el id porque a ampl no le gusta el indice 0
        for(int pr:ordenados){
            listaIdProvedoresShifteadosEnUno+=(pr+1)+" ";
        }
        listaIdProvedoresShifteadosEnUno+=":="+'\n';
        int [][] mapeoRecursoProveedor= new int[K.size()][P.size()];
        for (Map.Entry<Integer, VideoContent> entry : K.entrySet()) {
            mapeoRecursoProveedor[entry.getKey()][entry.getValue().getProviderid()]=1;
        }

        for(int video=0;video<K.size();video++){
            listaIdProvedoresShifteadosEnUno+=(video+1);//se sifhtea 1 porque a ampl no le gusta el cero
            for(int prove=0;prove<P.size();prove++){
                listaIdProvedoresShifteadosEnUno+=" "+mapeoRecursoProveedor[video][prove];
            }
            listaIdProvedoresShifteadosEnUno+='\n';
        }
        listaIdProvedoresShifteadosEnUno+=";"+'\n';

        return matriz+listaIdProvedoresShifteadosEnUno;
    }



   // param qQos {CENTROSDATOS,REGIONESUSR} >= 0 ;
   public String get_qQos(){
       String matriz="param qQos: ";
       String listaIdProvedoresShifteadosEnUno="";

       //hay que correr el id porque a ampl no le gusta el indice 0
       for(int regusr=0;regusr<cant_regionesUsuarios;regusr++){
           listaIdProvedoresShifteadosEnUno+=(regusr+1)+" ";
       }
       listaIdProvedoresShifteadosEnUno+=":="+'\n';
       double [][] mapeoRegUsrDc= new double[cant_regionesUsuarios][cant_centrosDatos];
       for (Map.Entry<Integer,ArrayList<QoS>> entry : Q.entrySet()) {
           Integer key = entry.getKey();
           ArrayList<QoS> listaQoS = entry.getValue();
           for(QoS qo:listaQoS){
               mapeoRegUsrDc[key][qo.getDatacenterid()]=qo.getMetric();
           }

       }


       for(int dc=0;dc<cant_centrosDatos;dc++){
           listaIdProvedoresShifteadosEnUno+=(dc+1);//se sifhtea 1 porque a ampl no le gusta el cero
           for(int regusr=0;regusr<cant_regionesUsuarios;regusr++){
               listaIdProvedoresShifteadosEnUno+=" "+mapeoRegUsrDc[regusr][dc];
           }
           listaIdProvedoresShifteadosEnUno+='\n';
       }
       listaIdProvedoresShifteadosEnUno+=";"+'\n';

       return matriz+listaIdProvedoresShifteadosEnUno;
   }


    /*
    param cCostoReservadas {CENTROSDATOS} >= 0;
    param cCostoAdemanda {CENTROSDATOS} >= 0;
    param cCostoTransferirCadaVideo {CENTROSDATOS} >= 0;
    param cCostoAlmacenarCadaVideo {CENTROSDATOS} >= 0;
     */
    public String get_Costos(){
       /*
       param cCostoReservadas:=
						1 7.8
						2 2.5
						3 7.7;

        param cCostoAdemanda:=
                                1 7.8
                                2 2.5
                                3 7.7;

        param cCostoTransferirCadaVideo:=
                                1 7.8
                                2 2.5
                                3 7.7;

        param cCostoAlmacenarCadaVideo:=
                                1 7.8
                                2 2.5
                                3 7.7;
       */
/*
        devuelve el precio prorrateado a bytes
	   precio en bytes
	 */

        String cCostoReservadas ="param cCostoReservadas:="+'\n';
        String cCostoAdemanda ="param cCostoAdemanda:="+'\n';
        String cCostoTransferirCadaVideo ="param cCostoTransferirCadaVideo:="+'\n';
        String cCostoAlmacenarCadaVideo ="param cCostoAlmacenarCadaVideo:="+'\n';

        for (int dc=0;dc<cant_centrosDatos;dc++) {
            DataCenter centrodatos = C.get(dc);
            cCostoReservadas           +=(dc+1)+" "+centrodatos.getReserved_vm_priceProrrated(3600)+'\n';
            cCostoAdemanda             +=(dc+1)+" "+centrodatos.getOn_demand_vm_hourly_price()+'\n';
            cCostoTransferirCadaVideo  +=(dc+1)+" "+this.precioTransferirBloqueVideo(dc)+'\n';
            cCostoAlmacenarCadaVideo   +=(dc+1)+" "+this.precioAlmacenarCategoriaVideos(dc)+'\n';
        }
        cCostoReservadas           +=";"+'\n';
        cCostoAdemanda             +=";"+'\n';
        cCostoTransferirCadaVideo  +=";"+'\n';
        cCostoAlmacenarCadaVideo   +=";"+'\n';

        return cCostoReservadas+cCostoAdemanda+cCostoTransferirCadaVideo+cCostoAlmacenarCadaVideo;
    }

    /*
#  dDemanda {K,Tminutos,REGIONESUSR} binary
    */
    public String get_dDemanda(){


        int tiempoRedondo=(int)Math.ceil(cant_pasosTiempo/(double)60);
        tiempoRedondo=tiempoRedondo*60;//cantidad de minutos q suman justo una hora
        int [][][] demanda = new int[cant_contenidos][tiempoRedondo][cant_regionesUsuarios];
        //por simplicidad hago la matriz
        double picoDemanda=0;
        for(int tic=0;tic<tiempoRedondo;tic++){
             ArrayList<ContentRequest> listaRequests_en_tiempo_tic = R.get(tic);
             if(listaRequests_en_tiempo_tic!=null) {
                 for (ContentRequest requestvideo : listaRequests_en_tiempo_tic) {
                     demanda[requestvideo.getVideocontentid()][tic][requestvideo.getUserregionid()]++;
                     if(picoDemanda< demanda[requestvideo.getVideocontentid()][tic][requestvideo.getUserregionid()]){
                         picoDemanda=demanda[requestvideo.getVideocontentid()][tic][requestvideo.getUserregionid()];
                     }
                 }
             }

         }
        String dDemanda ="param picoMaximoRequest:="+picoDemanda+";"+'\n';

         dDemanda +="param dDemanda :="+'\n';
        String cabezalRegUsrs="";
        for(int regUsr=0;regUsr<cant_regionesUsuarios;regUsr++){
            cabezalRegUsrs+=" "+(regUsr+1);
        }

         for(int video=0; video<cant_contenidos;video++){
             dDemanda+="["+(video+1)+",*,*]:"+cabezalRegUsrs+" :="+'\n';
             for(int tic=0;tic<tiempoRedondo;tic++){
                 dDemanda+=(tic+1);
                 for(int regUsr=0;regUsr<cant_regionesUsuarios;regUsr++){
                     dDemanda+=" "+demanda[video][tic][regUsr];
                   //  System.out.println("["+video+"]["+tic+"]["+regUsr+"]");
                 }
                 dDemanda+='\n';
             }
             dDemanda+='\n';
            // System.out.println(video);
        }
        dDemanda+=";";

        return dDemanda;
    }
    public String get_dDemandaSet(){


        int tiempoRedondo=(int)Math.ceil(cant_pasosTiempo/(double)60);
        tiempoRedondo=tiempoRedondo*60;//cantidad de minutos q suman justo una hora
       // int [][][] demanda = new int[cant_contenidos][tiempoRedondo][cant_regionesUsuarios];
        //por simplicidad hago la matriz
        HashMap<String,Integer> demanda = new HashMap<>();
        double picoDemanda=0;
        int picoDemandaInt=0;
        for(int tic=0;tic<tiempoRedondo;tic++){
            ArrayList<ContentRequest> listaRequests_en_tiempo_tic = R.get(tic);
            if(listaRequests_en_tiempo_tic!=null) {
                for (ContentRequest requestvideo : listaRequests_en_tiempo_tic) {
                    int idvideoAMPL = requestvideo.getVideocontentid()+1;
                    int idtiempoAMPL = tic+1;
                    int idregionAMPL = requestvideo.getUserregionid()+1;


                    String clave = idvideoAMPL+","+idtiempoAMPL+","+idregionAMPL;
                    if(demanda.containsKey(clave)){
                        demanda.put(clave,demanda.get(clave)+1);
                    }else{
                        demanda.put(clave,1);
                    }
// set demandaSet  within K cross Tminutos cross REGIONESUSR cross posibleDemandasPositivas;
                //    System.out.println(clave);
                    if(picoDemanda< demanda.get(clave)){
                        picoDemanda=demanda.get(clave);
                    } if(picoDemandaInt< demanda.get(clave)){
                        picoDemandaInt=demanda.get(clave);
                    }
                }
            }

        }
        String dDemanda ="param picoMaximoRequest:="+picoDemanda+";"+'\n';
               dDemanda +="param picoMaximoRequestInteger:="+picoDemandaInt+";"+'\n';

        dDemanda +="set demandaSet := ";

        for (Map.Entry<String, Integer> entry : demanda.entrySet()) {
            String key = entry.getKey();
            Integer valorDemanda = entry.getValue();

            dDemanda += "("+key+","+valorDemanda+") ";

        }
        dDemanda+='\n';
        dDemanda+=";";
/*
        String cabezalRegUsrs="";
        for(int regUsr=0;regUsr<cant_regionesUsuarios;regUsr++){
            cabezalRegUsrs+=" "+(regUsr+1);
        }

        for(int video=0; video<cant_contenidos;video++){
            dDemanda+="["+(video+1)+",*,*]:"+cabezalRegUsrs+" :="+'\n';
            for(int tic=0;tic<tiempoRedondo;tic++){
                dDemanda+=(tic+1);
                for(int regUsr=0;regUsr<cant_regionesUsuarios;regUsr++){
                    dDemanda+=" "+demanda[video][tic][regUsr];
                    //  System.out.println("["+video+"]["+tic+"]["+regUsr+"]");
                }
                dDemanda+='\n';
            }
            dDemanda+='\n';
            System.out.println(video);
        }
        dDemanda+=";";
*/
        return dDemanda;
    }


    public String get_dDemanda_test(int idvideoRecargar,int tiempoRecargar,int regusrRecargar){

        String dDemanda ="param dDemanda :="+'\n';
//[1,*,*]:	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17	18	19	20	21	22	23	24	25	26	27	28	29	30 :=
        int tiempoRedondo=(int)Math.ceil(cant_pasosTiempo/(double)60);
        tiempoRedondo=tiempoRedondo*60;//cantidad de minutos q suman justo una hora
        int [][][] demanda = new int[cant_contenidos][tiempoRedondo][cant_regionesUsuarios];
        //por simplicidad hago la matriz
        for(int tic=0;tic<tiempoRedondo;tic++){
            ArrayList<ContentRequest> listaRequests_en_tiempo_tic = R.get(tic);
            if(listaRequests_en_tiempo_tic!=null) {
                for (ContentRequest requestvideo : listaRequests_en_tiempo_tic) {
                    demanda[requestvideo.getVideocontentid()][tic][requestvideo.getUserregionid()]++;

                }
            }
        }
        String cabezalRegUsrs="";
        for(int regUsr=0;regUsr<cant_regionesUsuarios;regUsr++){
            cabezalRegUsrs+=" "+(regUsr+1);
        }

        //suma valores a los request para meter ruido
        for(int video=0; video<cant_contenidos;video++){
            dDemanda+="["+(video+1)+",*,*]:"+cabezalRegUsrs+" :="+'\n';
            for(int tic=0;tic<tiempoRedondo;tic++){
                dDemanda+=(tic+1);
                for(int regUsr=0;regUsr<cant_regionesUsuarios;regUsr++){
                        demanda[video][tic][regUsr]+=JMetalRandom.getInstance().nextInt(0,CR*5);
                        demanda[video][tic][regUsr]+=JMetalRandom.getInstance().nextInt(0,CR*5);
                        demanda[video][tic][regUsr]+=JMetalRandom.getInstance().nextInt(0,CR*5);
                    dDemanda+=" "+demanda[video][tic][regUsr];
                    //  System.out.println("["+video+"]["+tic+"]["+regUsr+"]");
                }
                dDemanda+='\n';
            }
            dDemanda+='\n';
          //  System.out.println(video);
        }
        dDemanda+=";";

        return dDemanda;
    }





    public ArrayList<QoS> ordenadosPorQoS(ArrayList<QoS> l) {
         int pepe =0;
        Collections.sort(l, new Comparator<QoS>() {
            @Override
            public int compare(QoS q1, QoS q2) {
                return q1.comparar(q2);
            }
        });
        return l;
    }



    private void calcular_t_picos(int cant_VM_pico) {





         demanda_limite =cant_VM_pico;

        if(cant_VM_pico>this.cantMaximaVmRequerida){
            demanda_limite = this.cantMaximaVmRequerida;
        }

        //System.out.println("this.cantMaximaVmRequerida "+this.cantMaximaVmRequerida+ " cant_VM_pico: "+cant_VM_pico+" demanda_limite: "+demanda_limite);


    	//TODO: Deberia ajustarse tmb por datacenter.. pero a priori no se cual es el datacenter, ni el global region id
    	//int [][] demanda = new int[R.size()][C.size()]; 
    	martriz_demanda_pico = new int[this.getT()+1];

    	for(Integer tic : R.keySet()){
            int horaActual = (int)Math.floor(tic/(double)60);
            martriz_demanda_pico[tic]=cantMinimaVmsHora[horaActual];
    	}

    	//System.out.println("Demanda Limite:" + demanda_limite);
    	for(int i=0; i<this.getT()+1; i++) {
    		//for(int j=0; j<C.size(); j++) {
    		//	System.out.println("demanda["+ i +"]["+ j +"] = " + demanda[i][j]);
    		//}
    		//System.out.print("demanda["+ i +"] = " + martriz_demanda_pico[i]);
    		if(martriz_demanda_pico[i] > demanda_limite) {
    			martriz_demanda_pico[i] = 1;
    		} else {
    			martriz_demanda_pico[i] = 0;
    		}
    		//System.out.println(";  es_t_pico = " + martriz_demanda_pico[i]);
    	} 
    	
    }
    
    public boolean is_t_pico(int t) {
    	return (this.martriz_demanda_pico[t]==1);
    }
    
    public int getT() {
    	return this.T;
    }


    public ArrayList<QoS> mejoresQOS(int user_region_id, int mejores_n) {
        ArrayList<QoS> l = Q.get(user_region_id);
        //l ya sele ordenada por qos
        List<QoS> mejores_l = l.subList(0, Math.min(l.size(),mejores_n));
        ArrayList<QoS> mejores_l_transformados = new ArrayList<>();
        mejores_l_transformados.addAll(mejores_l);
        return mejores_l_transformados;
    }




    public ArrayList<DataCenter> mejoresDataCenterSegunQOS(int user_region_id, int mejores_n) {
    	ArrayList<QoS> l = Q.get(user_region_id);
    	//l ya sele ordenada por qos
/*
    	Collections.sort(l, new Comparator<QoS>() {
    	    @Override
    	    public int compare(QoS q1, QoS q2) {
    	        if (q1.getMetric() > q2.getMetric())

    	            return 1;
    	        if (q1.getMetric() < q2.getMetric())
    	            return -1;
    	        return 0;
    	    }
    	});
*/
    	ArrayList<DataCenter> ldc = new ArrayList<DataCenter>();
    	List<QoS> mejores_l = l.subList(0, Math.min(l.size(),mejores_n));
    	for (QoS q : mejores_l) {
    		ldc.add(C.get(q.getDatacenterid()));
    	}
    	return ldc;
    }
    
    public ArrayList<DataCenter> datacentersQueCumplenAlfa(int user_region_id) {
    	ArrayList<DataCenter> ldc = new ArrayList<DataCenter>();
    	for (DataCenter dc : C.values()) {
    		if(this.cumpleQOS(user_region_id, dc.getId())) {
    			ldc.add(dc);
    		}
    	}
    	
    	return ldc;
    } 
    
    public ArrayList<DataCenter> datacentersMasBaratos(ArrayList<DataCenter> datacenters, int mejores_n) {
    	Collections.sort(datacenters, new Comparator<DataCenter>() {
    	    @Override
    	    public int compare(DataCenter dc1, DataCenter dc2) {
    	        if (dc1.getNetCost() > dc2.getNetCost())
    	            return 1;
    	        if (dc1.getNetCost() < dc2.getNetCost())
    	            return -1;
    	        return 0;
    	    }
    	});
    	
    	return new ArrayList<DataCenter>(datacenters.subList(0, Math.min(datacenters.size(),mejores_n)));
    }

    public ArrayList<DataCenter> ordenarDatacentersMasBaratosCostoBruto(ArrayList<DataCenter> datacenters) {
    	Collections.sort(datacenters, new Comparator<DataCenter>() {
    	    @Override
    	    public int compare(DataCenter dc1, DataCenter dc2) {
    	        if (dc1.getCostroBruto() > dc2.getCostroBruto())
    	            return 1;
    	        if (dc1.getCostroBruto() < dc2.getCostroBruto())
    	            return -1;
    	        return 0;
    	    }
    	});

    	return datacenters;
    }

    public Object elijoUnoRandomConProbUniforme(ArrayList<?> lista) {
    	return lista.get((new Random()).nextInt(lista.size()-0) + 0);
    }

    public int datacenterRandomRespetandoQOS(int user_region_id) {
        ArrayList<Integer> centrosConBuenQos = new ArrayList<>();
        for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
            if( cumpleQOS( user_region_id,centroDato)){
                centrosConBuenQos.add(centroDato);
            }
        }
        if(centrosConBuenQos.size()==0){
            throw new JMetalException("ERROR: datacenterRandomRespetandoQOS");
        }
        return  (int)elijoUnoRandomConProbUniforme(centrosConBuenQos);
    }


    public boolean cumpleQOS(int user_region_id,int dataCenterId) {
        //como cavernicola buscar el datacenter en la lista
        for(int indice=0; indice <Q.get(user_region_id).size(); indice++ ){
            if( Q.get(user_region_id).get(indice).getDatacenterid()==dataCenterId){
               return Q.get(user_region_id).get(indice).getMetric() < alfa_Qos;
            }
        }
        throw new JMetalException("ERROR: Ninguno cumple alfa_Qos "+Q.size());
    }


    public double getQOS(int user_region_id,int dataCenterId) {

       double metric = -1.0;

        //como cavernicola buscar el datacenter en la lista
        for(int indice=0; indice <Q.get(user_region_id).size(); indice++ ){
            if( Q.get(user_region_id).get(indice).getDatacenterid()==dataCenterId){
                metric = Q.get(user_region_id).get(indice).getMetric();
            }
        }
        if(metric < 0){
            throw new JMetalException("ERROR: qos negaativo");
        }
        return metric;

    }



    /**
     * Agrupa los idvideo por proveedor y en categorias de largo largocategoria
     * @param idProveedor
     * @param idVideo
     * @param largoCategoria
     * @return
     */
    private int nuevoIdSegunRankingPorProveedor(int idProveedor, int idVideo, int largoCategoria){
        int indiceNuevaCategoria=0;

        indiceNuevaCategoria=largoCategoria*idProveedor + (idVideo%largoCategoria);

       // System.out.println("indiceNuevaCategoria="+indiceNuevaCategoria+" idProveedor="+idProveedor+" idVideo="+idVideo+" largoCategoria="+largoCategoria);

        return indiceNuevaCategoria;
    }


    public void calculaFitnesYQoS(int cant_contenidos,int cant_centrosDatos,int cant_pasosTiempo,int cant_regionesUsuarios, int[][] x, int[][] y, int[] y_techo, int [][][][] z) {


    }

    public int getGreedyDeterministic_level() {
        return greedyDeterministic_level;
    }

    public void setGreedyDeterministic_level(int greedyDeterministic_level) {
        this.greedyDeterministic_level = greedyDeterministic_level;
    }

    public String getRutaAbsolutaSalidaInstancia() {
        return rutaAbsolutaSalidaInstancia;
    }

    public void setRutaAbsolutaSalidaInstancia(String rutaAbsolutaSalidaInstancia) {
        this.rutaAbsolutaSalidaInstancia = rutaAbsolutaSalidaInstancia;
    }
    /**
     *
     * @param Y
     * @param Y_techo
     * @return retorno[0]=costo_instancias_reservadas  retorno[1]=costo_instancias_on_demand
     */
    public double [] costosMaquinasVirtuales(int [][] Y  ,  int [] Y_techo){

        double costo_instancias_reservadas=0.0;
        double costo_instancias_on_demand=0.0;
        for(int i=0; i<this.getC().size(); i++) {
            int idCd = this.getC().get(i).getId();
            int horas_uso_vm_ondemand = 0;
            for(int t=0; t<this.tiempoHoras; t++) {
                if (Y[idCd][t] > Y_techo[idCd]) {
                    horas_uso_vm_ondemand += Y[idCd][t] - Y_techo[idCd];
                }
            }
            costo_instancias_reservadas += ((float) Y_techo[idCd]) * this.getC().get(i).getReserved_vm_priceProrrated(tiempoHoras*60*60);
            costo_instancias_on_demand += horas_uso_vm_ondemand* this.getC().get(i).getOn_demand_vm_hourly_price();
        }
        double [] retorno = new double[2];
        retorno[0]=costo_instancias_reservadas;
        retorno[1]=costo_instancias_on_demand;
        return retorno;
    }


    public double precioTransferirBloqueVideo(int idDatacenter){
        return this.getTamanioBloqueVideo()*this.getC().get(idDatacenter).getTransfer_base_price();
    }

    public double precioAlmacenarCategoriaVideos(int idDatacenter){
        return this.getKS()* this.getC().get(idDatacenter).getStorage_base_price();
    }
    
    
    
    
    /**********/
    //bloque de greedys generales
    /************/

    public CdnItem greedyOnQoS() {
        ArrayList<double[]> largosEinterseccionesComparacion = new ArrayList<>();
        double costo_instancias_reservadas=0.0;
        double costo_instancias_on_demand=0.0;
        double costoBytesAlmacenados=0.0;
        double costoBytesTransfiriendo=0.0;

        int cant_contenidos = this.getCant_contenidos();
        int cant_centrosDatos = this.getCant_centrosDatos();
        int cant_pasosTiempo = this.getCant_pasosTiempo();
        int cant_regionesUsuarios = this.getCant_regionesUsuarios();

        int [][] X        = new int[cant_contenidos][cant_centrosDatos];
        int [][] Y        = new int[cant_centrosDatos][tiempoHoras];
        int [] Y_techo    = new int[cant_centrosDatos];
        this.VMM = new HashMap<Integer,ArrayList<int[][]>>();
        for(int i=0; i<this.getC().size(); i++) {
            ArrayList<int[][]> VM = new ArrayList<int[][]>();
            VMM.put(this.getC().get(i).getId(), VM);
        }

        for(int t=0; t<cant_pasosTiempo; t++) {
            if(!this.getR().containsKey(t)) continue;
            ArrayList<ContentRequest> requests = this.getR().get(t);
            for(ContentRequest r: requests) {
                ArrayList<QoS> mejoresQos = this.mejoresQOS(r.getUserregionid(),this.greedyDeterministic_level);
                QoS mejorQos = (QoS) this.elijoUnoRandomConProbUniforme(mejoresQos);

                //agreo para ir acumulando el error
                //// TODO: 14/04/2017 cuidado ver si no es mejor hacer copia limpia aca 
                largosEinterseccionesComparacion.addAll(mejorQos.getLargosEinterseccionesComparacion());


                int idDatacenteMejorQos =mejorQos.getDatacenterid();
                DataCenter datacenterMejorQoS = this.getC().get(idDatacenteMejorQos);
                X[r.getVideocontentid()][idDatacenteMejorQos] = 1;

                //COSTOS------------------------------------------------------
                costoBytesAlmacenados+=this.precioAlmacenarCategoriaVideos(idDatacenteMejorQos);
                //costo de trasferencia
                costoBytesTransfiriendo+=this.precioTransferirBloqueVideo(idDatacenteMejorQos);
                //------------------------------------------------------


                int proveedor_id = this.getK().get(r.getVideocontentid()).getProviderid();
                if(hasVMdisponible(datacenterMejorQoS, t, proveedor_id)) {
                    //No necesito comprar nada! (la funcion ya actualiza los requests simulatanos)
                } else {
                    //Tengo que comprar!
                    if(this.is_t_pico(t)) {
//                        for(int j=t; j<t+60 && j<this.getT(); j++) {
//                            Y[idDatacenteMejorQos][j]++;
//                        }
                        Y[idDatacenteMejorQos][this.tiempoToHoraIndiceArray(t)]++;

                        buyOnDemandVM(datacenterMejorQoS, t, proveedor_id);
                    } else {
                        //System.out.println("Commpro Reservada");
                        for(int j=0; j<this.tiempoHoras; j++) {
                            Y[idDatacenteMejorQos][j]++;
                        }
                        Y_techo[idDatacenteMejorQos]++;
                        buyReservedVM(datacenterMejorQoS, t, proveedor_id);
                    }
                }
            }
        }
        this.VMM.clear();
        this.VMM=null;
        //COSTOS------------------------------------------------------
        double [] costorsVms=this.costosMaquinasVirtuales( Y  ,Y_techo);
        costo_instancias_reservadas=costorsVms[0];
        costo_instancias_on_demand=costorsVms[1];
        //------------------------------------------------------

        return new CdnItem(cant_contenidos,
                cant_centrosDatos,
                cant_pasosTiempo,
                cant_regionesUsuarios,
                X,
                Y,
                Y_techo,
                costo_instancias_reservadas,
                costo_instancias_on_demand,
                costoBytesAlmacenados,
                costoBytesTransfiriendo,
                largosEinterseccionesComparacion);


    }

    public CdnItem greedyOnCost() {
        double costo_instancias_reservadas=0.0;
        double costo_instancias_on_demand=0.0;
        double costoBytesAlmacenados=0.0;
        double costoBytesTransfiriendo=0.0;
        ArrayList<double[]> largosEinterseccionesComparacion = new ArrayList<>();
        int cant_contenidos       = this.getCant_contenidos();
        int cant_centrosDatos     = this.getCant_centrosDatos();
        int cant_pasosTiempo      = this.getCant_pasosTiempo();
        int cant_regionesUsuarios = this.getCant_regionesUsuarios();

        int [][] X                = new int[cant_contenidos][cant_centrosDatos];
        int [][] Y                = new int[cant_centrosDatos][tiempoHoras];
        int [] Y_techo            = new int[cant_centrosDatos];
        this.VMM                  = new  HashMap<Integer,ArrayList<int[][]>>();

        for(int i=0; i<this.getC().size(); i++) {
            ArrayList<int[][]> VM = new ArrayList<int[][]>();
            VMM.put(this.getC().get(i).getId(), VM);
        }

        for(int t=0; t<cant_pasosTiempo; t++) {
            if(!this.getR().containsKey(t)) continue;
            ArrayList<ContentRequest> requests = this.getR().get(t);
            for(ContentRequest r: requests) {

                ArrayList<DataCenter> datacenters_posibles_que_cumplan_alfa = this.datacentersQueCumplenAlfa(r.getUserregionid());
                //si deterministic_level=1 elige solo 1 dc
                ArrayList<DataCenter> datacenters_mas_baratos = this.datacentersMasBaratos(datacenters_posibles_que_cumplan_alfa,this.greedyDeterministic_level);
                DataCenter datacenter = (DataCenter) this.elijoUnoRandomConProbUniforme(datacenters_mas_baratos);
                X[r.getVideocontentid()][datacenter.getId()] = 1;

// TODO: 22/08/2017 la suma pelada de lo que costaria una maquina al mango transfiriendo, almacenando y precio reservada 


                // TODO: 22/08/2017 armar la z como siempra se armaba a ca (hay que armarla a parte para no interferir con el armado en evaluacion)
                //COSTOS------------------------------------------------------
                costoBytesAlmacenados+=this.precioAlmacenarCategoriaVideos(datacenter.getId());
                //costo de trasferencia
                costoBytesTransfiriendo+=this.precioTransferirBloqueVideo(datacenter.getId());
                //------------------------------------------------------

                int proveedor_id = this.getK().get(r.getVideocontentid()).getProviderid();
                if(hasVMdisponible(datacenter, t, proveedor_id)) {
                    //No necesito comprar nada! (la funcion ya actualiza los requests simulatanos)
                    //System.out.println("No compro nada.");
                } else {
                    //Tengo que comprar!
                    if(this.is_t_pico(t)) {
//                        for(int j=t; j<t+60 && j<this.getT(); j++) {
//                            Y[datacenter.getId()][j]++;
//                        }
                     //   System.out.println("tiempohora"+tiempoHoras+"t."+t+"tiempoToHora"+this.tiempoToHoraIndiceArray(t));
                        Y[datacenter.getId()][this.tiempoToHoraIndiceArray(t)]++;
                        buyOnDemandVM(datacenter, t, proveedor_id);
                    } else {
                        for(int j=0; j<this.getTiempoHoras(); j++) {
                            Y[datacenter.getId()][j]++;
                        }
                        Y_techo[datacenter.getId()]++;
                        buyReservedVM(datacenter, t, proveedor_id);
                    }
                }
            }
        }
        this.VMM.clear();
        this.VMM=null;

        //COSTOS------------------------------------------------------
        double [] costorsVms=this.costosMaquinasVirtuales( Y  ,Y_techo);
        costo_instancias_reservadas=costorsVms[0];
        costo_instancias_on_demand=costorsVms[1];
        //------------------------------------------------------

        return new CdnItem(cant_contenidos,
                cant_centrosDatos,
                cant_pasosTiempo,
                cant_regionesUsuarios,
                X,
                Y,
                Y_techo,
                costo_instancias_reservadas,
                costo_instancias_on_demand,
                costoBytesAlmacenados,
                costoBytesTransfiriendo,
                largosEinterseccionesComparacion);
    }



    public CdnItem greedyOnCostUmbralRandom(boolean modoBusqueda) {
        boolean esOnCost = true;

        CdnItem solucion = null;
        boolean armarZ=false;
        if(!modoBusqueda){
            //sin restriccion de calidad de servicio
            solucion= greedyOnCostOrQoSParametrico(armarZ,esOnCost,Double.MAX_VALUE);
        }else{
            if(yaEncontroElMinimoUmbralQoSConSolucion){


                //sorteo random entre el menor y los umbrales mas grandes
                int indiceQoS =  JMetalRandom.getInstance().nextInt(indiceMenorQoSConSolucion, QoSordenados.size()-1);
                solucion= greedyOnCostOrQoSParametrico(armarZ,esOnCost,QoSordenados.get(indiceQoS));
               // JMetalLogger.logger.log(Level.INFO,"RANDOM indiceMenorQoSConSolucion "+indiceQoS);
            }else{
                while (solucion==null && indiceMenorQoSConSolucion<QoSordenados.size()){
                  //  JMetalLogger.logger.log(Level.INFO,"indiceMenorQoSConSolucion "+indiceMenorQoSConSolucion);
                    //ir aumentando entra los QoS que existen
                    solucion= greedyOnCostOrQoSParametrico(armarZ,esOnCost,QoSordenados.get(indiceMenorQoSConSolucion));
                    if(solucion==null) {//quiero el primer indice que da una solucion
                        indiceMenorQoSConSolucion++;
                    }
                }
                if(indiceMenorQoSConSolucion<QoSordenados.size()){
                    yaEncontroElMinimoUmbralQoSConSolucion=true;
                }else{
                    //mandar a archivo de log

                 //   JMetalLogger.logger.log(Level.SEVERE,"no se encotro solucion");
                 //   throw new JMetalException("no se encotro solucion");
                }
            }
        }
        return solucion;
    }



    public void init_soluicionesInicialesOncost( ) {
        boolean armarZ=false;
        this.soluicionesInicialesOncost = solucsGreedyOnCostDistintas( armarZ);
    }

public ArrayList<CdnItem> solucsGreedyOnCostDistintas( boolean armarZ) {

        ArrayList<CdnItem> tmp = this.greedyOnCostParetoQoS( armarZ);
        ArrayList<CdnItem> soluicionesInicialesOncostRetorno = new ArrayList<>();


        //elimino las repetidas
        for (CdnItem solucTemp: tmp) {

            boolean estaEnConjunto =false;
            for (CdnItem solucConjunto: soluicionesInicialesOncostRetorno) {
                if(solucTemp.comparar(solucConjunto,armarZ)){
                    estaEnConjunto=true;
                    break;
                }
            }

            if(!estaEnConjunto){
                soluicionesInicialesOncostRetorno.add(solucTemp);
            }

        }

        return soluicionesInicialesOncostRetorno;
    }



    public ArrayList<CdnItem> greedyOnCostParetoQoS(boolean armarZ) {


        int cant_VM_pico=0;
        calcular_t_picos(cant_VM_pico);
        ArrayList<CdnItem> listaSolucionesTotales = new ArrayList<>();


        while(cant_VM_pico<=this.cantMaximaVmRequerida) {


            boolean esOnCost = true;
            boolean yaEncontroElMinimoUmbralQoSConSolucionLocal = false;
            CdnItem solucion = null;
            int indiceMenorQoSConSolucionLocal = 0;

//        variar muchas veces coeficiente demanda pico y tirar esos valores pa la lista

//
//
            boolean terminaMeterSolucLista = false;
            int indiceQoS = 0;
            ArrayList<CdnItem> listaSoluciones = new ArrayList<>();

            while (!terminaMeterSolucLista) {
                if (yaEncontroElMinimoUmbralQoSConSolucionLocal) {


                    //sorteo random entre el menor y los umbrales mas grandes
                    //int indiceQoS =  JMetalRandom.getInstance().nextInt(indiceMenorQoSConSolucionLocal, QoSordenados.size()-1);
                    for (indiceQoS = indiceMenorQoSConSolucionLocal; indiceQoS < QoSordenados.size(); indiceQoS++) {
                        listaSoluciones.add(greedyOnCostOrQoSParametrico(armarZ, esOnCost, QoSordenados.get(indiceQoS)));
                    }
                    terminaMeterSolucLista = true;
                    // JMetalLogger.logger.log(Level.INFO,"RANDOM indiceMenorQoSConSolucion "+indiceQoS);
                } else {
                    while (solucion == null && indiceMenorQoSConSolucionLocal < QoSordenados.size()) {
                        //  JMetalLogger.logger.log(Level.INFO,"indiceMenorQoSConSolucion "+indiceMenorQoSConSolucion);
                        //ir aumentando entra los QoS que existen
                        solucion = greedyOnCostOrQoSParametrico(armarZ, esOnCost, QoSordenados.get(indiceMenorQoSConSolucionLocal));
                        if (solucion == null) {//quiero el primer indice que da una solucion
                            indiceMenorQoSConSolucionLocal++;
                        }
                    }
                    if (indiceMenorQoSConSolucionLocal < QoSordenados.size()) {
                        yaEncontroElMinimoUmbralQoSConSolucionLocal = true;
                    } else {
                        //mandar a archivo de log

                        //   JMetalLogger.logger.log(Level.SEVERE,"no se encotro solucion");
                        //   throw new JMetalException("no se encotro solucion");
                    }
                }
            }//while
            cant_VM_pico++;
            calcular_t_picos(cant_VM_pico);
            listaSolucionesTotales.addAll(listaSoluciones);
        }//while coeficiente demanda pico
        return listaSolucionesTotales;
    }



    public ArrayList<CdnItem> greedyQoSParetoQoS(boolean armarZ) {
        int cant_VM_pico=0;
        calcular_t_picos(cant_VM_pico);
        ArrayList<CdnItem> listaSolucionesTotales = new ArrayList<>();
        while(cant_VM_pico<=this.cantMaximaVmRequerida) {
            boolean esOnCost = false;
            listaSolucionesTotales.add(greedyOnCostOrQoSParametrico(armarZ, esOnCost, Integer.MAX_VALUE));
            cant_VM_pico++;
            calcular_t_picos(cant_VM_pico);
        }//while coeficiente demanda pico
        return listaSolucionesTotales;
    }







    public CdnItem greedyQoS( ) {
        boolean esOnCost = false;
        CdnItem solucion = null;
        boolean armarZ=false;

        solucion= greedyOnCostOrQoSParametrico(armarZ,esOnCost,Double.MAX_VALUE);
        return solucion;
    }



    public CdnItem greedyOnCostOrQoSParametrico(boolean armarZ,boolean esOnCost,double umbralQoS_greeedyOncost) {
// Get the current date and time
        LocalDateTime now = LocalDateTime.now();

// Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

// Format the current date and time
        String inicio= now.format(formatter);









        double costo_instancias_reservadas=0.0;
        double costo_instancias_on_demand=0.0;
        double costoBytesAlmacenados=0.0;
        double costoBytesTransfiriendo=0.0;
        ArrayList<double[]> largosEinterseccionesComparacion = new ArrayList<>();
        int cant_contenidos       = this.getCant_contenidos();
        int cant_centrosDatos     = this.getCant_centrosDatos();
        int cant_pasosTiempo      = this.getCant_pasosTiempo();
        int cant_regionesUsuarios = this.getCant_regionesUsuarios();

        int [][] X                = new int[cant_contenidos][cant_centrosDatos];
        int [][] Y                = new int[cant_centrosDatos][tiempoHoras];
        int [] Y_techo            = new int[cant_centrosDatos];
        this.VMM                  = new  HashMap<Integer,ArrayList<int[][]>>();
        int[] mapeoRequestsDcs = null;
        if(armarZ) {
            mapeoRequestsDcs = new int[cant_requests];
        }
        double costoTransferencia=0.0;
        double metricaQoS=0.0;

        for(int i=0; i<this.getC().size(); i++) {
            ArrayList<int[][]> VM = new ArrayList<int[][]>();
            VMM.put(this.getC().get(i).getId(), VM);
        }

        for(int t=0; t<cant_pasosTiempo; t++) {
            if(!this.getR().containsKey(t)) continue;
            ArrayList<ContentRequest> requests = this.getR().get(t);
            for(ContentRequest r: requests) {
                DataCenter datacenter =null;
               if(esOnCost) {
                   ArrayList<DataCenter> datacenters_ordenados_mas_baratos_que_cumplan_umbralQoS = this.datacentersQueCumplenUmbralQoSParam(r.getUserregionid(), umbralQoS_greeedyOncost);
                   //no encontro valores para ese umbral de calidad de servicio
                   if (datacenters_ordenados_mas_baratos_que_cumplan_umbralQoS == null || datacenters_ordenados_mas_baratos_que_cumplan_umbralQoS.isEmpty()) {
                       return null;
                   }

                    datacenter = datacenters_ordenados_mas_baratos_que_cumplan_umbralQoS.get(0);
               }else{
                   // es en QoS
                   ArrayList<QoS> qos_ordenadosPorQos = Q.get(r.getUserregionid());
                   datacenter=C.get(qos_ordenadosPorQos.get(0).getDatacenterid());
               }


                X[r.getVideocontentid()][datacenter.getId()] = 1;
                if(armarZ) {
                    mapeoRequestsDcs[r.getIdrequest()] = datacenter.getId();
                }

                //actualizo costo transferencia
                costoTransferencia+=this.precioTransferirBloqueVideo(datacenter.getId());
                //se actualiza metrica de qos
                metricaQoS+=this.getQOS(r.getUserregionid(),datacenter.getId());





                //COSTOS------------------------------------------------------
                costoBytesAlmacenados+=this.precioAlmacenarCategoriaVideos(datacenter.getId());
                //costo de trasferencia
                costoBytesTransfiriendo+=this.precioTransferirBloqueVideo(datacenter.getId());
                //------------------------------------------------------

                int proveedor_id = this.getK().get(r.getVideocontentid()).getProviderid();
                if(hasVMdisponible(datacenter, t, proveedor_id)) {
                    //No necesito comprar nada! (la funcion ya actualiza los requests simulatanos)
                    //System.out.println("No compro nada.");
                } else {
                    //Tengo que comprar!
                    if(this.is_t_pico(t)) { //// TODO: 23/08/17 analizar el tpico
//                        for(int j=t; j<t+60 && j<this.getT(); j++) {
//                            Y[datacenter.getId()][j]++;
//                        }
                        //   System.out.println("tiempohora"+tiempoHoras+"t."+t+"tiempoToHora"+this.tiempoToHoraIndiceArray(t));
                        Y[datacenter.getId()][this.tiempoToHoraIndiceArray(t)]++;
                        buyOnDemandVM(datacenter, t, proveedor_id);
                    } else {
                        for(int j=0; j<this.getTiempoHoras(); j++) {
                            Y[datacenter.getId()][j]++;
                        }
                        Y_techo[datacenter.getId()]++;
                        buyReservedVM(datacenter, t, proveedor_id);
                    }
                }
            }
        }
        this.VMM.clear();
        this.VMM=null;

        //COSTOS------------------------------------------------------
        double [] costorsVms=this.costosMaquinasVirtuales( Y  ,Y_techo);
        costo_instancias_reservadas=costorsVms[0];
        costo_instancias_on_demand=costorsVms[1];
        //------------------------------------------------------

        CdnItem soluctic = new CdnItem(cant_contenidos,
                cant_centrosDatos,
                cant_pasosTiempo,
                cant_regionesUsuarios,
                X,
                Y,
                Y_techo,
                costo_instancias_reservadas,
                costo_instancias_on_demand,
                costoBytesAlmacenados,
                costoBytesTransfiriendo,
                largosEinterseccionesComparacion);
        if(armarZ) {
            soluctic.setCostoBytesTransfiriendo(costoTransferencia);
            soluctic.setMetricaQoS(metricaQoS);
            soluctic.setMapeoRequestsDcs(mapeoRequestsDcs);
            soluctic.setCant_requests(cant_requests);
            soluctic.setDemanda_limite(demanda_limite);
        }

if(esOnCost){

    soluctic.setTipoGreedy("costo");

}else{
    soluctic.setTipoGreedy("qos");
}
        // seteo inicio y fin de construccion
        now = LocalDateTime.now();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String fin= now.format(formatter);
        soluctic.setInicioConstruccin(inicio);
        soluctic.setFinConstruccion(fin);





        return soluctic;
    }




    public ArrayList<DataCenter> datacentersQueCumplenUmbralQoSParam(int user_region_id,double umbralQoS) {
        ArrayList<DataCenter> ldc = new ArrayList<DataCenter>();
        for (DataCenter dc : dataCentersOrdenadosCosto) {
            boolean cumpleQoS = false;
            //// TODO: 23/08/17  hacer estructura mas eficiente para esto de busar por wos
            for(int indice=0; indice <Q.get(user_region_id).size(); indice++ ){
                if( Q.get(user_region_id).get(indice).getDatacenterid()==dc.getId() && (Q.get(user_region_id).get(indice).getMetric() < umbralQoS)){
                    cumpleQoS=true;
                }
            }

            if(cumpleQoS) {
                ldc.add(dc);
            }
        }
        return ldc;
    }







    public boolean hasVMdisponible(DataCenter dc, int t, int proveedor_id) {

        ArrayList<int[][]> VM = VMM.get(dc.getId());
        for(int i=0; i<VM.size(); i++) {
            //System.out.println("VM " + i + " for dc " + this.getId());
            //System.out.println("VM[" +i +"][" + t +"][" + instancia.getP().indexOf(proveedor_id) +"]?");
            if(VM.get(i)[t][this.getP().indexOf(proveedor_id)]>0) {
                //Tiene disponible, actualizo disponibilidad
                for(int p=0; p<this.getCant_proveedores(); p++) {
                    if(p==this.getP().indexOf(proveedor_id)) {
                        VM.get(i)[t][p]--;
                    } else {
                        VM.get(i)[t][p] = 0;
                    }
                }

                return true;
            }

        }
        return false;
    }

    public void buyOnDemandVM(DataCenter dc, int t, int proveedor_id) {
        //obtengo las maquinas virtuales del datacenter
        ArrayList<int[][]> VM_del_datacenter = VMM.get(dc.getId());

        int[][] ancho_De_Banda_Disponible_De_VM = new int[this.getCant_pasosTiempo()][this.getCant_proveedores()];
        for(int p=0; p<this.getCant_proveedores(); p++) {
            //carga recurso disponibles para cada proveedor en la maquina virtual
            for(int j=t; j<t+60 && j<this.getT(); j++) {
                ancho_De_Banda_Disponible_De_VM[j][p] = this.getCR();
            }
            //se le saca un paso de disponibilidad al recurso de ese proveedor en esa maquina virtual
            if(p == this.getP().indexOf(proveedor_id)) {
                ancho_De_Banda_Disponible_De_VM[t][p] --;
            } else {
                //no permito que otros proveedores la usen mientras esta ocupada por el proveedor proveedor_id
                //entonces le pongo que no hay disponibilidad para ellos
                ancho_De_Banda_Disponible_De_VM[t][p] = 0;
            }

        }
        VM_del_datacenter.add(ancho_De_Banda_Disponible_De_VM); /*
	System.out.println("compro VM " + (VM.size()-1) + " for dc " + this.getId());
	for(int p=0; p<instancia.getCant_proveedores(); p++) {
		for(int j=0; j<instancia.getT(); j++) {
			System.out.println("VM[" + (VM.size()-1) +"][" + j +"][" + p +"] = " + VM.get(VM.size()-1)[j][p]);
		}
	} */
    }

    public void buyReservedVM(DataCenter dc, int t, int proveedor_id) {
        ArrayList<int[][]> VM = VMM.get(dc.getId());

        int[][] x = new int[this.getCant_pasosTiempo()][this.getCant_proveedores()];
        for(int p=0; p<this.getCant_proveedores(); p++) {
            for(int j=0; j<this.getT(); j++) {
                x[j][p] = this.getCR();
            }

            if(p == this.getP().indexOf(proveedor_id)) {
                x[t][p] --;
            } else {
                x[t][p] = 0;
            }

        }
        VM.add(x);
    }



    public CdnItem greedyOnCostVMociosa() {
        double costo_instancias_reservadas=0.0;
        double costo_instancias_on_demand=0.0;
        double costoBytesAlmacenados=0.0;
        double costoBytesTransfiriendo=0.0;
        ArrayList<double[]> largosEinterseccionesComparacion = new ArrayList<>();
        int cant_contenidos = this.getCant_contenidos();
        int cant_centrosDatos = this.getCant_centrosDatos();
        int cant_pasosTiempo = this.getCant_pasosTiempo();
        int cant_regionesUsuarios = this.getCant_regionesUsuarios();

        int [][] X        = new int[cant_contenidos][cant_centrosDatos];
        int [][] Y        = new int[cant_centrosDatos][cant_pasosTiempo];
        int [] Y_techo    = new int[cant_centrosDatos];
        this.VMM = new  HashMap<Integer,ArrayList<int[][]>>();
        for(int i=0; i<this.getC().size(); i++) {
            ArrayList<int[][]> VM = new ArrayList<int[][]>();
            VMM.put(this.getC().get(i).getId(), VM);
        }

        for(int t=0; t<cant_pasosTiempo; t++) {
            if(!this.getR().containsKey(t)) continue;
            ArrayList<ContentRequest> requests = this.getR().get(t);
            for(ContentRequest r: requests) {
                ArrayList<DataCenter> datacenters_posibles_que_cumplan_alfa = this.datacentersQueCumplenAlfa(r.getUserregionid());
                //si deterministic_level=1 elige solo 1 dc
                ArrayList<DataCenter> datacenters_mas_baratos = this.datacentersMasBaratos(datacenters_posibles_que_cumplan_alfa,this.greedyDeterministic_level);
                DataCenter datacenter = (DataCenter) this.elijoUnoRandomConProbUniforme(datacenters_mas_baratos);
                X[r.getVideocontentid()][datacenter.getId()] = 1;

/*
                pedir todos los datacenters
                si no hay que comprar maquina, le doy el pedido
                        mirar slots de una hora y probar en todos los dc
*/

                //COSTOS------------------------------------------------------
                costoBytesAlmacenados+=this.precioAlmacenarCategoriaVideos(datacenter.getId());
                //costo de trasferencia
                costoBytesTransfiriendo+=this.precioTransferirBloqueVideo(datacenter.getId());
                //------------------------------------------------------

                int proveedor_id = this.getK().get(r.getVideocontentid()).getProviderid();
                if(hasVMdisponible(datacenter, t, proveedor_id)) {
                    //No necesito comprar nada! (la funcion ya actualiza los requests simulatanos)
                    //System.out.println("No compro nada.");
                } else {
                    //Tengo que comprar!
                    if(this.is_t_pico(t)) {
                        for(int j=t; j<t+60 && j<this.getT(); j++) {
                            Y[datacenter.getId()][j]++;
                        }
                        buyOnDemandVM(datacenter, t, proveedor_id);
                    } else {
                        for(int j=0; j<this.getT(); j++) {
                            Y[datacenter.getId()][j]++;
                        }
                        Y_techo[datacenter.getId()]++;
                        buyReservedVM(datacenter, t, proveedor_id);
                    }
                }
            }
        }
        this.VMM.clear();
        this.VMM=null;

        //COSTOS------------------------------------------------------
        double [] costorsVms=this.costosMaquinasVirtuales( Y  ,Y_techo);
        costo_instancias_reservadas=costorsVms[0];
        costo_instancias_on_demand=costorsVms[1];
        //------------------------------------------------------

        return new CdnItem(cant_contenidos,
                cant_centrosDatos,
                cant_pasosTiempo,
                cant_regionesUsuarios,
                X,
                Y,
                Y_techo,
                costo_instancias_reservadas,
                costo_instancias_on_demand,
                costoBytesAlmacenados,
                costoBytesTransfiriendo,
                largosEinterseccionesComparacion);
    }

    public int getCant_requests() {
        return cant_requests;
    }

    public void setCant_requests(int cant_requests) {
        this.cant_requests = cant_requests;
    }



private int [] buscaDcVm(boolean noPedirQueEsteElRecurso ,
                         int proveedor,
                         int horaActual,
                         int idvideo,
                         CdnItem solucionAE,
                         int regionusuario,
                         ArrayList<int[]> cantsRequestVmDC,
                         ArrayList<int[]> proveedoresVmDC,
                         int idrequest,
                         boolean mejorCosto,
                         boolean usarMaquinasAlCompletoQoS){

    //System.err.println("paso...");
//String debudddd="";
    ArrayList<String> comparacionesQoS= new ArrayList<>();
    double minCosto = Double.MAX_VALUE;
    double minLatencia = Double.MAX_VALUE;

    int dcSeleccionado = -1;
    int VmLibreEnDc = -1;
    int []retorno = new int[2];
    boolean encontreVmAtendiendoAProveedor = false;

    for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
        int cantVmsEnEsteDcEnEstaHora = solucionAE.getYvalor(dc, horaActual);

        //cumple QoS y  el video esta en el dc y hay alguna VM

        if ((solucionAE.getXvalor(idvideo, dc) == 1 || noPedirQueEsteElRecurso )  &&  cantVmsEnEsteDcEnEstaHora > 0) {
            DataCenter dcActual = this.C.get(dc);

            int cantVmsOnDemandDC         = cantVmsEnEsteDcEnEstaHora - solucionAE.getY_techovalor(dc);
            if(cantVmsOnDemandDC<0){
                JMetalLogger.logger.log(Level.SEVERE,"Error cantVmsOnDemandDC < 0 ");
            }

            if(mejorCosto){
                //if (minCosto > dcActual.getCostroBruto() ) {
                    for (int vm_actual = 0; vm_actual < cantVmsEnEsteDcEnEstaHora; vm_actual++) {
                        //uso las primeras vms como las ondemand
                        int indiceUltimaVmOnDemand = cantVmsOnDemandDC-1;

                        double costoUsarEstaVm =this.precioAlmacenarCategoriaVideos(dc)+ this.precioTransferirBloqueVideo(dc);
                        if(vm_actual<=indiceUltimaVmOnDemand){
                            costoUsarEstaVm+=dcActual.getOn_demand_vm_hourly_price();
                        }else{
                            costoUsarEstaVm+=dcActual.getReserved_vm_priceProrrated(3600);
                        }
                        if (minCosto > costoUsarEstaVm ) {
                            //si ya hay una vm atendiendo a ese proveedor entonces lo mando a esa vm
                            int[] provVM = proveedoresVmDC.get(dc);
                            if (provVM[vm_actual] == proveedor) { //el proveedor esta atendiendo en una maquina
                                int[] requestVM = cantsRequestVmDC.get(dc);
                                if (requestVM[vm_actual] < CR) { //la maquina todavia tiene capacidad
                                    minCosto = costoUsarEstaVm;
                                    dcSeleccionado = dc;
                                    VmLibreEnDc = vm_actual;
                                    encontreVmAtendiendoAProveedor = true;
                                }
                            }
                        }
                    }
              //  }
            }else{

               // usarMaquinasAlCompletoQoS

//mejor qos
//                if (minLatencia > dcActual.getQoSRegion(regionusuario) ) {
//                    for (int vm_actual = 0; vm_actual < cantVmsEnEsteDcEnEstaHora; vm_actual++) {
//                        //si ya hay una vm atendiendo a ese proveedor entonces lo mando a esa vm
//                        int[] provVM = proveedoresVmDC.get(dc);
//                        if (provVM[vm_actual] == proveedor) { //el proveedor esta atendiendo en una maquina
//                            int[] requestVM = cantsRequestVmDC.get(dc);
//                            if (requestVM[vm_actual] < CR) { //la maquina todavia tiene capacidad
//                                minLatencia = dcActual.getQoSRegion(regionusuario);
//                                dcSeleccionado = dc;
//                                VmLibreEnDc = vm_actual;
//                                encontreVmAtendiendoAProveedor = true;
//                            }
//                        }
//                    }
//                }

                //para encontrar el mejor qos la maquina esta libre o es del proveedor no le doy prioridad a usar toda la vm
                //si no encuetra aca que encuentre abajo
                if ( (solucionAE.getXvalor(idvideo, dc) == 1 || noPedirQueEsteElRecurso) && cantVmsEnEsteDcEnEstaHora > 0) {
                    comparacionesQoS.add(String.valueOf(dc));
                    if (minLatencia > dcActual.getQoSRegion(regionusuario)) {
                        for (int vm_actual = 0; vm_actual < cantVmsEnEsteDcEnEstaHora; vm_actual++) {
                            //si ya hay una vm atendiendo a ese proveedor entonces lo mando a esa vm
                            int[] provVM = proveedoresVmDC.get(dc);
                            //la maquina esta libre o es del proveedor
                            if (((provVM[vm_actual] == -1 || provVM[vm_actual] == proveedor)  && ! usarMaquinasAlCompletoQoS) ||
                                ( (( provVM[vm_actual] == proveedor)  &&  usarMaquinasAlCompletoQoS)  )   ) {
                                int[] requestVM = cantsRequestVmDC.get(dc);
                                if (requestVM[vm_actual] < CR) { //la maquina todavia tiene capacidad
                                    minLatencia = dcActual.getQoSRegion(regionusuario);
                                    dcSeleccionado = dc;
                                    VmLibreEnDc = vm_actual;
                                    encontreVmAtendiendoAProveedor = true;
                                }
                            }
                        }
                    }
                } //

                //busco sin esta restriccion

            }
        }
    }
    // si la maquina ya esta atendiendo al proveedor, le asigno el recurso (si no le tenia) para que aproveche la maquina
    if (encontreVmAtendiendoAProveedor && solucionAE.getXvalor(idvideo, dcSeleccionado) != 1) {
        solucionAE.setXvalor(idvideo,dcSeleccionado,1);
    }


    if (!encontreVmAtendiendoAProveedor) {
        comparacionesQoS.clear();
        //si no encuentra vm atendiendo al proveedor entonces busca la mas barata para asignale el request
        for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
            int cantVmsEnEsteDcEnEstaHora = solucionAE.getYvalor(dc, horaActual);
            if(mejorCosto) {
                //cumple QoS y  el video esta en el dc y hay alguna VM
                if ( (solucionAE.getXvalor(idvideo, dc) == 1 || noPedirQueEsteElRecurso) && cantVmsEnEsteDcEnEstaHora > 0) {
                    DataCenter dcActual = this.C.get(dc);

                    int cantVmsOnDemandDC         = cantVmsEnEsteDcEnEstaHora - solucionAE.getY_techovalor(dc);
                    if(cantVmsOnDemandDC<0){
                        JMetalLogger.logger.log(Level.SEVERE,"Error cantVmsOnDemandDC < 0 ");
                    }

                    //if (minCosto > dcActual.getCostroBruto()) {
                        for (int vm_actual = 0; vm_actual < cantVmsEnEsteDcEnEstaHora; vm_actual++) {
                            int indiceUltimaVmOnDemand = cantVmsOnDemandDC-1;

                            double costoUsarEstaVm =this.precioAlmacenarCategoriaVideos(dc)+ this.precioTransferirBloqueVideo(dc);
                            if(vm_actual<=indiceUltimaVmOnDemand){
                                costoUsarEstaVm+=dcActual.getOn_demand_vm_hourly_price();
                            }else{
                                costoUsarEstaVm+=dcActual.getReserved_vm_priceProrrated(3600);
                            }
                            if (minCosto > costoUsarEstaVm ) {
                                //si ya hay una vm atendiendo a ese proveedor entonces lo mando a esa vm
                                int[] provVM = proveedoresVmDC.get(dc);
                                if (provVM[vm_actual] == -1) { //la maquina esta libre
                                    int[] requestVM = cantsRequestVmDC.get(dc);
                                    if (requestVM[vm_actual] < CR) { //la maquina todavia tiene capacidad
                                        minCosto = dcActual.getCostroBruto();
                                        dcSeleccionado = dc;
                                        VmLibreEnDc = vm_actual;
                                    }
                                }
                            }
                        }
                   // }
                } //
            }else{
                if ( (solucionAE.getXvalor(idvideo, dc) == 1 || noPedirQueEsteElRecurso) && cantVmsEnEsteDcEnEstaHora > 0) {
                    DataCenter dcActual = this.C.get(dc);
                    comparacionesQoS.add(String.valueOf(dc));
                    if (minLatencia > dcActual.getQoSRegion(regionusuario)) {
                        for (int vm_actual = 0; vm_actual < cantVmsEnEsteDcEnEstaHora; vm_actual++) {
                            //si ya hay una vm atendiendo a ese proveedor entonces lo mando a esa vm
                            int[] provVM = proveedoresVmDC.get(dc);
                            if (provVM[vm_actual] == -1) { //esta libre la maquina
                                int[] requestVM = cantsRequestVmDC.get(dc);
                                if (requestVM[vm_actual] < CR) { //la maquina todavia tiene capacidad
                                    minLatencia = dcActual.getQoSRegion(regionusuario);
                                    dcSeleccionado = dc;
                                    VmLibreEnDc = vm_actual;

                                }
                            }
                        }
                    }

                }


            }
        }
    }

    //se arregla solucion
    if(noPedirQueEsteElRecurso && VmLibreEnDc!=-1){
        solucionAE.setXvalor(idvideo,dcSeleccionado,1);
    }

//    if(idrequest==435230 /*|| idrequest==256134*/){
//        if(encontreVmAtendiendoAProveedor){
//            System.err.println("Encontro proveee");
//        }
//       System.err.println(idrequest);
//       System.err.println(debudddd);
//    }


        if((!mejorCosto) && dcSeleccionado!=-1){
            //guardo la lista de dc comparados para qos
          //  solucionAE.addDcsComparadosQoSR_UNequests(comparacionesQoS,idrequest);
        }

        retorno[0]=dcSeleccionado;
        retorno[1]=VmLibreEnDc;
        return retorno;
}




//greedyOnCostAE
public  void construirZ_AE(CdnItem solucionAE) {
    solucionAE.setInicioConstruirZ(System.currentTimeMillis());
        CdnFactibilizador facti = new CdnFactibilizador(this);
         boolean buscarMejorCosto=JMetalRandom.getInstance().nextDouble()<0.5;
    //System.err.println(buscarMejorCosto);
         boolean usarMaquinasAlCompletoQoS = false;
        facti.factibilizarVmCumpleDemanda(solucionAE);
        facti.factibilizarPisoMinimoReservadasUsadas(solucionAE);
        int cantBusquedas=0;
        double  costoTransferencia=0.0;
        double  metricaQoS=0.0;
        HashMap<Integer,ArrayList<Integer>> mapeoDCRecursoAsignado= new HashMap<>();;

    ArrayList<ArrayList<int[]>> cantMaximaRequestsVmsHoraDc = new ArrayList<>();
    //marco el tiempo de inicio para luego calcular cuanto llevo construir z




       // if(!facti.isFactibleYcubreDemandaVms(solucionAE)){
            // solucionAE.toFile("F:/PROYE/solocodigo/proycodigo/salida/","solucioneslocas.txt" );
            //       throw new Exception("NO FACTIBLE SOLUC");
      //  }

//    nro_evaluacion++;
//    if((nro_evaluacion-1)%100==0){
//        System.err.println(" Evaluacion: "+nro_evaluacion);
////        JMetalLogger.logger.info(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+" Evaluacion: "+nro_evaluacion);
////        System.gc();
//    }
    boolean encontrandoSolucion = false;
        try {

            while(!encontrandoSolucion && cantBusquedas<10) {
                  costoTransferencia=0.0;
                  metricaQoS=0.0;

                  boolean noPedirQueEsteElRecurso = false;
                  if(cantBusquedas==0 && buscarMejorCosto){
                      solucionAE.setMetodoBuscaZ("COSTO_RESPETA_X");
                      noPedirQueEsteElRecurso = false;
                  }

                if(cantBusquedas==1 && buscarMejorCosto){
                    solucionAE.setMetodoBuscaZ("COSTO_NO_RESPETA_X");
                    noPedirQueEsteElRecurso = true;
                }




                if(cantBusquedas<=1 && !buscarMejorCosto){
                    noPedirQueEsteElRecurso = false;
                    if(cantBusquedas==0){
                        usarMaquinasAlCompletoQoS=false;
                        solucionAE.setMetodoBuscaZ("QOS_RESPETA_X_NO_TRATA_LLENAR_MAQUINA");
                    }
                    if(cantBusquedas==1){
                        usarMaquinasAlCompletoQoS=true;
                        solucionAE.setMetodoBuscaZ("QOS_RESPETA_X_TRATA_LLENAR_MAQUINA");
                    }
                }

                if(cantBusquedas>1 && !buscarMejorCosto){

                    noPedirQueEsteElRecurso = true;
                    if(cantBusquedas==2){
                        usarMaquinasAlCompletoQoS=false;
                        solucionAE.setMetodoBuscaZ("QOS_NO_RESPETA_X_NO_TRATA_LLENAR_MAQUINA");
                    }
                    if(cantBusquedas==3){
                        usarMaquinasAlCompletoQoS=true;
                        solucionAE.setMetodoBuscaZ("QOS_NO_RESPETA_X_TRATA_LLENAR_MAQUINA");
                    }
                    if(cantBusquedas>3){
                        usarMaquinasAlCompletoQoS=true;
                        solucionAE.setMetodoBuscaZ("QOS_NO_ENCUENTRA_SOLUCION");
                    }
                }




                    //if(cantBusquedas>1){
                     //   System.err.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+" cantBusquedas: "+cantBusquedas);
                   // }

                //contabilidad de las vms usadas por hora para cada dc para luego limpiar la solucion en las Y
                cantMaximaRequestsVmsHoraDc.clear();
                for (int ticHora = 0; (ticHora < getTiempoHoras()); ticHora++) {
                    ArrayList<int[]> cantsVmDC = new ArrayList<>();
                    for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
                        int cantsVmsEnDcEnEstaHora = solucionAE.getYvalor(dc, ticHora);
                        cantsVmDC.add(dc, new int[cantsVmsEnDcEnEstaHora]);
                    }
                    cantMaximaRequestsVmsHoraDc.add(ticHora,cantsVmDC);
                }
                //-------------------


                solucionAE.initMapeoRequestsDcs();
                //se construye esta estructura por cuestion de eficiencia
                mapeoDCRecursoAsignado = new HashMap<>();
                for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
                    mapeoDCRecursoAsignado.put(dc,new ArrayList<Integer>());
                }
    
                ArrayList<int[]> cantsRequestVmDC = new ArrayList<>();
                ArrayList<int[]> proveedoresVmDC = new ArrayList<>();

                encontrandoSolucion=true;
                for (int tic = 0; (tic < cant_pasosTiempo)&& (encontrandoSolucion); tic++) {
                    ArrayList<ContentRequest> listaRequests_en_tiempo_tic = R.get(tic);

                    if (listaRequests_en_tiempo_tic != null) {
                        int horaActual = (int) Math.floor(tic / (double) 60);
                        for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
                            int cantsVmsEnDcEnEstaHora = solucionAE.getYvalor(dc, horaActual);
                            cantsRequestVmDC.add(dc, new int[cantsVmsEnDcEnEstaHora]);
                            int[] provVm = new int[cantsVmsEnDcEnEstaHora];
                            for (int indiceVM = 0; indiceVM < cantsVmsEnDcEnEstaHora; indiceVM++) {
                                provVm[indiceVM] = -1;
                            }
                            proveedoresVmDC.add(dc, provVm);
                        }
                        //recorriendo request y llenando las estructuras
                        for (ContentRequest requestvideo : listaRequests_en_tiempo_tic) {
                            int idrequest = requestvideo.getIdrequest();
                            int proveedor = this.getProvRequestsMatrix(idrequest);
                            int regionusuario = this.getRegUsrIdRequestsMatrix(idrequest);
                            int idvideo = this.getVideoIdRequestsMatrix(idrequest);
                            int dcSeleccionado = -1;
                            int VmLibreEnDc = -1;




                            int[] dcSeleccionado_VmLibreEnDc = buscaDcVm(noPedirQueEsteElRecurso,
                                    proveedor,
                                    horaActual,
                                    idvideo,
                                    solucionAE,
                                    regionusuario,
                                    cantsRequestVmDC,
                                    proveedoresVmDC, idrequest, buscarMejorCosto,usarMaquinasAlCompletoQoS);


                            dcSeleccionado = dcSeleccionado_VmLibreEnDc[0];
                            VmLibreEnDc = dcSeleccionado_VmLibreEnDc[1];
                            encontrandoSolucion = VmLibreEnDc != -1;


                                if (!encontrandoSolucion){
                                    if(solucionAE.getMetodoBuscaZ()=="QOS_NO_RESPETA_X_TRATA_LLENAR_MAQUINA" ||
                                       (solucionAE.getMetodoBuscaZ()=="COSTO_NO_RESPETA_X")){
                                        System.err.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+" "+solucionAE.getMetodoBuscaZ() );
                                        String cantsRequestVmDCString="";
                                        String proveedoresVmDCString="";
                                        for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
                                            int cantsVmsEnDcEnEstaHora = solucionAE.getYvalor(dc, horaActual);
                                            for (int indiceVM = 0; indiceVM < cantsVmsEnDcEnEstaHora; indiceVM++) {
                                                cantsRequestVmDCString+="cantsRequestVmDC.get("+dc+"))["+indiceVM+"]="+(cantsRequestVmDC.get(dc))[indiceVM]+"\n";
                                                proveedoresVmDCString+="proveedoresVmDC.get("+dc+"))["+indiceVM+"]="+(proveedoresVmDC.get(dc))[indiceVM]+"\n";
                                            }
                                        }
                                         String errorera="";
                                        errorera+="FALTAN VMS noPedirQueEsteElRecurso";
                                        errorera+="proveedor "+proveedor;
                                        errorera+= "horaActual "+horaActual;
                                        errorera+= "idvideo "+idvideo;
                                        errorera+="regionusuario "+regionusuario;
                                        errorera+="cantsRequestVmDC "+cantsRequestVmDCString;
                                        errorera+="proveedoresVmDC "+proveedoresVmDCString;
                                        errorera+="idrequest "+idrequest;
                                        errorera+= "buscarMejorCosto "+buscarMejorCosto;
                                        errorera+="usarMaquinasAlCompletoQoS "+usarMaquinasAlCompletoQoS;
                                       this.reportarErrorArchivo( errorera+solucionAE.toString());
                                    }
                                    break;//mato el for de requests
                                }
                            if(encontrandoSolucion) {
                                solucionAE.setMapeoRequestsDcsvalor(idrequest, dcSeleccionado);
                                //actualizo estructura para optimizar x depues
                                mapeoDCRecursoAsignado.get(dcSeleccionado).add(idvideo);
                                (cantsRequestVmDC.get(dcSeleccionado)[VmLibreEnDc])++;

                                proveedoresVmDC.get(dcSeleccionado)[VmLibreEnDc] = proveedor;

                                //actualizo costo transferencia
                                costoTransferencia += this.precioTransferirBloqueVideo(dcSeleccionado);
                                //se actualiza metrica de qos
                                //todo guardar el qos en los dcs para no recorrer
                                metricaQoS += this.getQOS(regionusuario, dcSeleccionado);

                                //actualizo contador de vms usadas en dc por hora cantVmsUsadasHoraDc.add(ticHora,cantsVmDC)
                                if(cantMaximaRequestsVmsHoraDc.get(horaActual).get(dcSeleccionado)[VmLibreEnDc]<cantsRequestVmDC.get(dcSeleccionado)[VmLibreEnDc]){
                                    cantMaximaRequestsVmsHoraDc.get(horaActual).get(dcSeleccionado)[VmLibreEnDc]=cantsRequestVmDC.get(dcSeleccionado)[VmLibreEnDc];
                                }
                            }//if(encontrandoSolucion)
                        }// for de requests
                        cantsRequestVmDC.clear();
                        proveedoresVmDC.clear();

                    }//if lista request tiene para este tic

                } //for principal
                cantBusquedas++;

            }//while encontro solucion
           // System.err.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+" "+solucionAE.getMetodoBuscaZ() );
       //calcular pico de vms usadas por hora por cd para poder limpiar las y despues y que quede mas barato


            for (int ticHora = 0; (ticHora < getTiempoHoras()); ticHora++) {
                int [] cantVmUsadaDc = new int[this.cant_centrosDatos];
                for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
                    int cantsVmsEnDcEnEstaHora = solucionAE.getYvalor(dc, ticHora);
                    for (int vm = 0; vm < cantsVmsEnDcEnEstaHora; vm++) {
                        if(cantMaximaRequestsVmsHoraDc.get(ticHora).get(dc)[vm]>0){
                            cantVmUsadaDc[dc]++;
                        }
                    }// vms

                    if(cantVmUsadaDc[dc]<cantsVmsEnDcEnEstaHora){

                        //se achica las vms usadas en el dc
                        solucionAE.setYvalor(dc,ticHora,cantVmUsadaDc[dc]);
                        if(solucionAE.getYvalor(dc, ticHora)<solucionAE.getY_techovalor(dc)){
                            //las rservadas no pueden ser mayor a las usadas
                            solucionAE.setY_techovalor(dc,solucionAE.getYvalor(dc, ticHora));
                        }
                    }

                }//dc



            }//tic hora



            //recorrer requests y si un video no es solicitado, entonces pelarlo del x solucionAE.getXvalor(idvideo, dc)
            for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
                ArrayList<Integer> listaVideosAsignadosEnDC = mapeoDCRecursoAsignado.get(dc);
                for (int elVideo = 0; elVideo < this.cant_contenidos; elVideo++) {
                    if(solucionAE.getXvalor(elVideo, dc)==1){
                        //si el video del X no esta en la lista de videos asignados para el dc, entonces se borra
                        if(!listaVideosAsignadosEnDC.contains(elVideo)){
                            solucionAE.setXvalor(elVideo,dc,0);
                        }
                    }
                }

            }
            // TODO: 09/07/2017 sacar este control
//            if(!facti.isFactible(solucionAE,true)){
//               // solucionAE.toFile("F:/PROYE/solocodigo/proycodigo/salida/","solucioneslocas.txt" );
//                this.reportarErrorArchivo("NO FACTIBLE SOLUC");
//                throw new Exception("NO FACTIBLE SOLUC");
//            }

            solucionAE.setCostoBytesTransfiriendo(costoTransferencia);
            solucionAE.setMetricaQoS(metricaQoS);
            String Z_construido_con = "MEJOR_QOS";
            if(buscarMejorCosto){
                Z_construido_con = "MEJOR_COSTO";
            }
            solucionAE.setZ_construido_con(Z_construido_con);
            //seteo el tiempo fin para saber cuanto llevo calcular z
            solucionAE.setFinConstruirZ(System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<DataCenter> datacentersQueCumplenAlfaQueTienenElVideo(int idregion,int idvideo,CdnItem solucionAE) {
        ArrayList<DataCenter> ldc = new ArrayList<DataCenter>();
        for (DataCenter dc : C.values()) {
            if(this.cumpleQOS(idregion, dc.getId()) && solucionAE.getXvalor(idvideo,dc.getId())==1) {
                ldc.add(dc);
            }
        }

        return ldc;
    }


    /**
     * retorna el floor de los minutos pasados a hora, para que sirva para usar como indice del array
     * @param tiempoMinutos
     * @return
     */
    public int tiempoToHoraIndiceArray(int tiempoMinutos){
        return (int)Math.floor(tiempoMinutos/(double)60) ;
    }


    public ArrayList<DataCenter> datacentersOrdenadosPorPrecio(ArrayList<DataCenter> datacenters) {
        Collections.sort(datacenters, new Comparator<DataCenter>() {
            @Override
            public int compare(DataCenter dc1, DataCenter dc2) {
                if (dc1.getNetCost() > dc2.getNetCost())
                    return 1;
                if (dc1.getNetCost() < dc2.getNetCost())
                    return -1;
                return 0;
            }
        });

        return datacenters;
    }

    /**
     *
     * @param idRequest
     * @return
     */
    public int getTicRequestsMatrix(int idRequest) {
        return requestsMatrix[idRequest][0];
    }

    /**
     *
     * @param ticRequestsMatrix
     * @param idRequest
     */
    public void setTicRequestsMatrix(int ticRequestsMatrix,int idRequest) {
        requestsMatrix[idRequest][0]=ticRequestsMatrix;
    }

    /**
     *
     * @param idRequest
     * @return
     */
    public int getVideoIdRequestsMatrix(int idRequest) {
        return requestsMatrix[idRequest][1];
    }

    /**
     *
     * @param videoIdRequestsMatrix
     * @param idRequest
     */
    public void setVideoIdRequestsMatrix(int videoIdRequestsMatrix,int idRequest) {
        requestsMatrix[idRequest][1]=videoIdRequestsMatrix;
    }

    /**
     *
     * @param idRequest
     * @return
     */
    public int getRegUsrIdRequestsMatrix(int idRequest) {
        return requestsMatrix[idRequest][2];
    }

    /**
     *
     * @param regUsrIdRequestsMatrix
     * @param idRequest
     */
    public void setRegUsrIdRequestsMatrix(int regUsrIdRequestsMatrix,int idRequest) {
        requestsMatrix[idRequest][2]=regUsrIdRequestsMatrix;
    }


    public void setProvRequestsMatrix(int idprovRequestsMatrix,int idRequest) {
        requestsMatrix[idRequest][3]=idprovRequestsMatrix;
    }


    public int getProvRequestsMatrix(int idRequest) {
        return requestsMatrix[idRequest][3];
    }

//    /**
//     *
//     * @param idRequest
//     * @return
//     */
//    public int getDcIdRequestsMatrix(int idRequest) {
//        return requestsMatrix[idRequest][3];
//    }
//
//    /**
//     *
//     * @param dcIdRequestsMatrix
//     * @param idRequest
//     */
//    public void setDcIdRequestsMatrix(int dcIdRequestsMatrix,int idRequest) {
//         requestsMatrix[idRequest][3]=dcIdRequestsMatrix;
//    }


    public void writeLargerTextFile(String aFileName, List<String> aLines, boolean appendMode){
        try {
            File file = new File(aFileName);
            FileWriter fileWriter = new FileWriter(file,appendMode);
            for(String line : aLines){
                fileWriter.write(line+'\n');
                // fileWriter.write('\n');
            }
            fileWriter.flush();
            fileWriter.close();
        }  catch (IOException ex) {
            ex.printStackTrace();
            //

            // Logger.getLogger(utils.Archivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public void reportarErrorArchivo(String lineaError){
//        try {
        System.err.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+"antes  reportarErrorArchivo... ");
            JMetalLogger.logger.log(Level.SEVERE,"nro_evaluacion "+nro_evaluacion+" "+lineaError);
        System.err.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+"despues  reportarErrorArchivo... ");
//            String timestamp=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//            boolean appendMode=true;
//            int linea = Thread.currentThread().getStackTrace()[2].getLineNumber();
//            String clase = Thread.currentThread().getStackTrace()[2].getClassName();
//            File file = new File(basedir+"/"+"ERRORES.TXT");
//            FileWriter fileWriter = new FileWriter(file,appendMode);
//            fileWriter.write(timestamp+" Linea "+linea+" "+" Clase: "+clase+" "+lineaError+'\n');
//            fileWriter.flush();
//            fileWriter.close();
//        }  catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    public void factibilizarEntreLasYs(int [] [] Y, int [] Y_techo){
        //ytecho no debe ser superior a y en ningun t
        for (int centroDato = 0; centroDato < cant_centrosDatos; centroDato++) {
            int minimaCantVmDC=Integer.MAX_VALUE;
            for (int tiempoStep = 0; tiempoStep < this.getTiempoHoras(); tiempoStep++) {
                if(Y[centroDato][tiempoStep]<minimaCantVmDC){
                    minimaCantVmDC=Y[centroDato][tiempoStep];
                }
            }
            //esto asegura restricciones, la variacion la da  la mautacion de este campo
            Y_techo[centroDato] = minimaCantVmDC;
        }
    }

    public ArrayList<CdnItem> getSoluicionesInicialesOncost() {
        return soluicionesInicialesOncost;
    }

    public void setSoluicionesInicialesOncost(ArrayList<CdnItem> soluicionesInicialesOncost) {
        this.soluicionesInicialesOncost = soluicionesInicialesOncost;
    }

    public int getCantMaximaVmRequerida() {
        return cantMaximaVmRequerida;
    }

    public void setCantMaximaVmRequerida(int cantMaximaVmRequerida) {
        this.cantMaximaVmRequerida = cantMaximaVmRequerida;
    }


    public HashMap<Integer, ArrayList<int[][]>> getVMM() {
        return VMM;
    }

    public void setVMM(HashMap<Integer, ArrayList<int[][]>> VMM) {
        this.VMM = VMM;
    }

    public void setP(ArrayList<Integer> p) {
        P = p;
    }

    public void setCant_proveedores(int cant_proveedores) {
        this.cant_proveedores = cant_proveedores;
    }

    public void setT(int t) {
        T = t;
    }

    public int getCantiadPromedioPorCategoria() {
        return cantiadPromedioPorCategoria;
    }

    public void setCantiadPromedioPorCategoria(int cantiadPromedioPorCategoria) {
        this.cantiadPromedioPorCategoria = cantiadPromedioPorCategoria;
    }

    public void setCR(int CR) {
        this.CR = CR;
    }

    public int[] getMartriz_demanda_pico() {
        return martriz_demanda_pico;
    }

    public void setMartriz_demanda_pico(int[] martriz_demanda_pico) {
        this.martriz_demanda_pico = martriz_demanda_pico;
    }

    public void setTiempoHoras(int tiempoHoras) {
        this.tiempoHoras = tiempoHoras;
    }

    public int[][] getRequestsMatrix() {
        return requestsMatrix;
    }

    public void setRequestsMatrix(int[][] requestsMatrix) {
        this.requestsMatrix = requestsMatrix;
    }

    public int[] getCantMinimaVmsHora() {
        return cantMinimaVmsHora;
    }

    public void setCantMinimaVmsHora(int[] cantMinimaVmsHora) {
        this.cantMinimaVmsHora = cantMinimaVmsHora;
    }

    public ArrayList<DataCenter> getDataCentersOrdenadosCosto() {
        return dataCentersOrdenadosCosto;
    }

    public void setDataCentersOrdenadosCosto(ArrayList<DataCenter> dataCentersOrdenadosCosto) {
        this.dataCentersOrdenadosCosto = dataCentersOrdenadosCosto;
    }

    public boolean isYaEncontroElMinimoUmbralQoSConSolucion() {
        return yaEncontroElMinimoUmbralQoSConSolucion;
    }

    public void setYaEncontroElMinimoUmbralQoSConSolucion(boolean yaEncontroElMinimoUmbralQoSConSolucion) {
        this.yaEncontroElMinimoUmbralQoSConSolucion = yaEncontroElMinimoUmbralQoSConSolucion;
    }

    public ArrayList<Double> getQoSordenados() {
        return QoSordenados;
    }

    public void setQoSordenados(ArrayList<Double> qoSordenados) {
        QoSordenados = qoSordenados;
    }

    public int getIndiceMenorQoSConSolucion() {
        return indiceMenorQoSConSolucion;
    }

    public void setIndiceMenorQoSConSolucion(int indiceMenorQoSConSolucion) {
        this.indiceMenorQoSConSolucion = indiceMenorQoSConSolucion;
    }

    public String getRutaAbsolutaYnombreArchivoRCD() {
        return rutaAbsolutaYnombreArchivoRCD;
    }

    public void setRutaAbsolutaYnombreArchivoRCD(String rutaAbsolutaYnombreArchivoRCD) {
        this.rutaAbsolutaYnombreArchivoRCD = rutaAbsolutaYnombreArchivoRCD;
    }

    public String getRutaAbsolutaYnombreArchivoRU() {
        return rutaAbsolutaYnombreArchivoRU;
    }

    public void setRutaAbsolutaYnombreArchivoRU(String rutaAbsolutaYnombreArchivoRU) {
        this.rutaAbsolutaYnombreArchivoRU = rutaAbsolutaYnombreArchivoRU;
    }

    public String getRutaAbsolutaYnombreArchivoQoS() {
        return rutaAbsolutaYnombreArchivoQoS;
    }

    public void setRutaAbsolutaYnombreArchivoQoS(String rutaAbsolutaYnombreArchivoQoS) {
        this.rutaAbsolutaYnombreArchivoQoS = rutaAbsolutaYnombreArchivoQoS;
    }

    public String getRutaAbsolutaYnombreArchivoDocsVideos() {
        return rutaAbsolutaYnombreArchivoDocsVideos;
    }

    public void setRutaAbsolutaYnombreArchivoDocsVideos(String rutaAbsolutaYnombreArchivoDocsVideos) {
        this.rutaAbsolutaYnombreArchivoDocsVideos = rutaAbsolutaYnombreArchivoDocsVideos;
    }

    public String getRutaAbsolutaYnombreArchivoWorkLoadVideos() {
        return rutaAbsolutaYnombreArchivoWorkLoadVideos;
    }

    public void setRutaAbsolutaYnombreArchivoWorkLoadVideos(String rutaAbsolutaYnombreArchivoWorkLoadVideos) {
        this.rutaAbsolutaYnombreArchivoWorkLoadVideos = rutaAbsolutaYnombreArchivoWorkLoadVideos;
    }

    public String getBasedir() {
        return basedir;
    }

    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }

    public int getNro_evaluacion() {
        return nro_evaluacion;
    }

    public void setNro_evaluacion(int nro_evaluacion) {
        this.nro_evaluacion = nro_evaluacion;
    }



    public  void buscar_backtracking(CdnItem solucionAE,
                                     List <CdnItem>  todasSolucs,
                                     int  idrequestProcesandoParam,
                                     ArrayList<int[]> cantsRequestVmDC,
                                     ArrayList<int[]> proveedoresVmDC) {

    boolean encontreSolucion=idrequestProcesandoParam==(this.cant_requests-1);


    if(encontreSolucion){
    //paso base
        //todo optimizar las y
        CdnItem clonada = solucionAE.clone();
        todasSolucs.add(clonada);
        nro_evaluacion++;
        System.err.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())+" soluc "+nro_evaluacion);
    }else{
        int idrequestProcesando =idrequestProcesandoParam+1;
        int ticactual = this.getTicRequestsMatrix(idrequestProcesando);
        int horaActual = (int) Math.floor(ticactual / (double) 60);
        int ticAnterior = this.getTicRequestsMatrix(idrequestProcesandoParam);
       //si el request es de otro tic limpio estructuras
        if(ticAnterior!=ticactual){ //se que los tics son contiguos tomando orndenado por idrequest
            //reiniciar contadores cantsRequestVmDC y proveedoresVmDC
            this.resetEstructurasBacktracking( solucionAE, horaActual,cantsRequestVmDC, proveedoresVmDC);
        }


        int proveedor = this.getProvRequestsMatrix(idrequestProcesando);
        int regionusuario = this.getRegUsrIdRequestsMatrix(idrequestProcesando);
        int idvideo = this.getVideoIdRequestsMatrix(idrequestProcesando);
       // int horaActual = (int) Math.floor(tic / (double) 60);
        for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
            int cantVmsEnEsteDcEnEstaHora = solucionAE.getYvalor(dc, horaActual);
            //cumple restriccion del X
            if(solucionAE.getXvalor(idvideo,dc)==1) {
                for (int vm_actual = 0; vm_actual < cantVmsEnEsteDcEnEstaHora; vm_actual++) {
                    int[] provVM = proveedoresVmDC.get(dc);
                    if (provVM[vm_actual] == -1 || provVM[vm_actual] == proveedor) { //esta libre la maquina
                        int[] requestVM = cantsRequestVmDC.get(dc);
                        if (requestVM[vm_actual] < CR) { //la maquina todavia tiene capacidad
                            solucionAE.setMapeoRequestsDcsvalor(idrequestProcesando, dc);
                            double metricaQoSAnterior = solucionAE.getMetricaQoS();
                            solucionAE.setMetricaQoS(metricaQoSAnterior+this.getQOS(regionusuario,dc));
                            (cantsRequestVmDC.get(dc)[vm_actual])++;
                            int valorAnteriorproveedoresVmDC=proveedoresVmDC.get(dc)[vm_actual];
                            proveedoresVmDC.get(dc)[vm_actual] = proveedor;
                            buscar_backtracking(solucionAE, todasSolucs, idrequestProcesando,cantsRequestVmDC,proveedoresVmDC);
                            //limpio la solucion
                            solucionAE.setMapeoRequestsDcsvalor(idrequestProcesando, -1);
                            (cantsRequestVmDC.get(dc)[vm_actual])--;
                            solucionAE.setMetricaQoS(metricaQoSAnterior);
                            proveedoresVmDC.get(dc)[vm_actual] = valorAnteriorproveedoresVmDC;
                        }
                    }


                }
            } // if(solucionAE.getXvalor(idvideo,dc)==1)
        }
    }



    }


    /**
     *
     * @param solucionAE
     * @return null si no encuentra solucion, o la solucion elegida del conjunto solucion encontrado
     */
    public CdnItem construirZ_backtracking_AE(CdnItem solucionAE) {

        List <CdnItem> todasSolucs = new ArrayList<>();
        solucionAE.initMapeoRequestsDcs();
        ArrayList<int[]> cantsRequestVmDC = new ArrayList<>();
        ArrayList<int[]> proveedoresVmDC = new ArrayList<>();
        int idrequestProcesando =0;
        int tic = this.getTicRequestsMatrix(idrequestProcesando);
        int horaActual = (int) Math.floor(tic / (double) 60);
        this.resetEstructurasBacktracking( solucionAE, horaActual,cantsRequestVmDC, proveedoresVmDC);
        for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
            int cantVmsEnEsteDcEnEstaHora = solucionAE.getYvalor(dc, horaActual);
            for (int vm_actual = 0; vm_actual < cantVmsEnEsteDcEnEstaHora; vm_actual++) {
                buscar_backtracking(solucionAE, todasSolucs, idrequestProcesando,cantsRequestVmDC,proveedoresVmDC);
            }
        }
        //todo aca seleccionar que solución es la que queda, por ahora agarra una nomas
        if(!todasSolucs.isEmpty()) {
            return todasSolucs.get(0);
        }
        return null;
    }


    void resetEstructurasBacktracking(CdnItem solucionAE,int horaActual,ArrayList<int[]> cantsRequestVmDC,ArrayList<int[]> proveedoresVmDC){
        cantsRequestVmDC.clear();
        proveedoresVmDC.clear();
        for (int dc = 0; dc < this.cant_centrosDatos; dc++) {
            int cantsVmsEnDcEnEstaHora = solucionAE.getYvalor(dc, horaActual);
            cantsRequestVmDC.add(dc, new int[cantsVmsEnDcEnEstaHora]);
            int[] provVm = new int[cantsVmsEnDcEnEstaHora];
            for (int indiceVM = 0; indiceVM < cantsVmsEnDcEnEstaHora; indiceVM++) {
                provVm[indiceVM] = -1;
            }
            proveedoresVmDC.add(dc, provVm);
        }
    }


    public CdnItem solucRoundRobin() {
// Get the current date and time
        LocalDateTime now = LocalDateTime.now();

// Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

// Format the current date and time
        String inicio= now.format(formatter);



        double costo_instancias_reservadas=0.0;
        double costo_instancias_on_demand=0.0;
        double costoBytesAlmacenados=0.0;
        double costoBytesTransfiriendo=0.0;
        ArrayList<double[]> largosEinterseccionesComparacion = new ArrayList<>();
        int cant_contenidos       = this.getCant_contenidos();
        int cant_centrosDatos     = this.getCant_centrosDatos();
        int cant_pasosTiempo      = this.getCant_pasosTiempo();
        int cant_regionesUsuarios = this.getCant_regionesUsuarios();

        int [][] X                = new int[cant_contenidos][cant_centrosDatos];
        int [][] Y                = new int[cant_centrosDatos][tiempoHoras];
        int [] Y_techo            = new int[cant_centrosDatos];
        this.VMM                  = new  HashMap<Integer,ArrayList<int[][]>>();
        int[] mapeoRequestsDcs = null;

            mapeoRequestsDcs = new int[cant_requests];

        double costoTransferencia=0.0;
        double metricaQoS=0.0;

        for(int i=0; i<this.getC().size(); i++) {
            ArrayList<int[][]> VM = new ArrayList<int[][]>();
            VMM.put(this.getC().get(i).getId(), VM);
        }

        int idDcSeleccionadoRounRobin =-1;

        for(int t=0; t<cant_pasosTiempo; t++) {
            if(!this.getR().containsKey(t)) continue;
            ArrayList<ContentRequest> requests = this.getR().get(t);
            for(ContentRequest r: requests) {
                DataCenter datacenter =null;

                //proximo dc en el rounRobin
                idDcSeleccionadoRounRobin++;
                if(this.getC().size()==idDcSeleccionadoRounRobin){
                    idDcSeleccionadoRounRobin=0;
                }
                //el dc elegido roundRobin
                datacenter=this.getC().get(idDcSeleccionadoRounRobin);

                X[r.getVideocontentid()][datacenter.getId()] = 1;

                    mapeoRequestsDcs[r.getIdrequest()] = datacenter.getId();


                //actualizo costo transferencia
                costoTransferencia+=this.precioTransferirBloqueVideo(datacenter.getId());
                //se actualiza metrica de qos
                metricaQoS+=this.getQOS(r.getUserregionid(),datacenter.getId());





                //COSTOS------------------------------------------------------
                costoBytesAlmacenados+=this.precioAlmacenarCategoriaVideos(datacenter.getId());
                //costo de trasferencia
                costoBytesTransfiriendo+=this.precioTransferirBloqueVideo(datacenter.getId());
                //------------------------------------------------------

                int proveedor_id = this.getK().get(r.getVideocontentid()).getProviderid();
                if(hasVMdisponible(datacenter, t, proveedor_id)) {
                    //No necesito comprar nada! (la funcion ya actualiza los requests simulatanos)
                    //System.out.println("No compro nada.");
                } else {
                    //Tengo que comprar!
                    if(this.is_t_pico(t)) {
//                        for(int j=t; j<t+60 && j<this.getT(); j++) {
//                            Y[datacenter.getId()][j]++;
//                        }
                        //   System.out.println("tiempohora"+tiempoHoras+"t."+t+"tiempoToHora"+this.tiempoToHoraIndiceArray(t));
                        Y[datacenter.getId()][this.tiempoToHoraIndiceArray(t)]++;
                        buyOnDemandVM(datacenter, t, proveedor_id);
                    } else {
                        for(int j=0; j<this.getTiempoHoras(); j++) {
                            Y[datacenter.getId()][j]++;
                        }
                        Y_techo[datacenter.getId()]++;
                        buyReservedVM(datacenter, t, proveedor_id);
                    }
                }
            }
        }
        this.VMM.clear();
        this.VMM=null;

        //COSTOS------------------------------------------------------
        double [] costorsVms=this.costosMaquinasVirtuales( Y  ,Y_techo);
        costo_instancias_reservadas=costorsVms[0];
        costo_instancias_on_demand=costorsVms[1];
        //------------------------------------------------------

        CdnItem soluctic = new CdnItem(cant_contenidos,
                cant_centrosDatos,
                cant_pasosTiempo,
                cant_regionesUsuarios,
                X,
                Y,
                Y_techo,
                costo_instancias_reservadas,
                costo_instancias_on_demand,
                costoBytesAlmacenados,
                costoBytesTransfiriendo,
                largosEinterseccionesComparacion);

            soluctic.setCostoBytesTransfiriendo(costoTransferencia);
            soluctic.setMetricaQoS(metricaQoS);
            soluctic.setMapeoRequestsDcs(mapeoRequestsDcs);
            soluctic.setCant_requests(cant_requests);
            soluctic.setDemanda_limite(demanda_limite);







            soluctic.setTipoGreedy("robin");

        // seteo inicio y fin de construccion
        now = LocalDateTime.now();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String fin= now.format(formatter);
        soluctic.setInicioConstruccin(inicio);
        soluctic.setFinConstruccion(fin);






        return soluctic;
    }



    public ArrayList<CdnItem> greedyQoSParetoRoundRobin() {
        int cant_VM_pico=0;
        calcular_t_picos(cant_VM_pico);
        ArrayList<CdnItem> listaSolucionesTotales = new ArrayList<>();
        while(cant_VM_pico<=this.cantMaximaVmRequerida) {
            listaSolucionesTotales.add(solucRoundRobin());
            cant_VM_pico++;
            calcular_t_picos(cant_VM_pico);
        }
        return listaSolucionesTotales;
    }




    public ArrayList<CdnItem> solucsRoundRobinDistintas( boolean armarZ) {

        ArrayList<CdnItem> tmp = this.greedyQoSParetoRoundRobin( );
        ArrayList<CdnItem> soluicionesInicialesOncostRetorno = new ArrayList<>();


        //elimino las repetidas
        for (CdnItem solucTemp: tmp) {

            boolean estaEnConjunto =false;
            for (CdnItem solucConjunto: soluicionesInicialesOncostRetorno) {
                if(solucTemp.comparar(solucConjunto,armarZ)){
                    estaEnConjunto=true;
                    break;
                }
            }

            if(!estaEnConjunto){
                soluicionesInicialesOncostRetorno.add(solucTemp);
            }

        }

        return soluicionesInicialesOncostRetorno;
    }

    public ArrayList<CdnItem> solucsQoSDistintas( boolean armarZ) {

        ArrayList<CdnItem> tmp = this.greedyQoSParetoQoS(armarZ );
        ArrayList<CdnItem> soluicionesInicialesOncostRetorno = new ArrayList<>();


        //elimino las repetidas
        for (CdnItem solucTemp: tmp) {

            boolean estaEnConjunto =false;
            for (CdnItem solucConjunto: soluicionesInicialesOncostRetorno) {
                if(solucTemp.comparar(solucConjunto,armarZ)){
                    estaEnConjunto=true;
                    break;
                }
            }

            if(!estaEnConjunto){
                soluicionesInicialesOncostRetorno.add(solucTemp);
            }

        }

        return soluicionesInicialesOncostRetorno;
    }
}
