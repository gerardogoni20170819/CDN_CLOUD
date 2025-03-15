package utils;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ggoni on 22/05/2017.
 */
public class GeneradorDataAmpl {

    public static void main(String[] args){
 //       String instancia="2000Vid_240Mins_4Prove";

//String instancia="300Vid_300Mins_6Prove_1XreG_1XreQ";
//String instancia="500Vid_420Mins_6Prove_1XreG_1XreQ";
//String instancia="1000Vid_300Mins_6Prove_1XreG_1XreQ";
//String instancia="tandaA1_500Vid_300Mins_6Prove_1XreG_1XreQ";
//String instancia="tandaA2_600Vid_360Mins_5Prove_1XreG_1XreQ";
//String instancia="tandaA2_400Vid_300Mins_5Prove_1XreG_1XreQ";
//String instancia="tandaA3_500Vid_240Mins_6Prove_1XreG_1XreQ";
//String instancia="tandaC1_700Vid_300Mins_6Prove_1XreG_1XreQ";
//String instancia="tandaD1_400Vid_300Mins_5Prove_1XreG_1XreQ";

//
//String instancia="tandaD1_400Vid_180Mins_5Prove_1XreG_1XreQ";
//String instancia="tandaD1_400Vid_240Mins_5Prove_1XreG_1XreQ";
//String instancia="tandaD1_400Vid_360Mins_5Prove_1XreG_1XreQ";
//String instancia="tandaD1_400Vid_420Mins_5Prove_1XreG_1XreQ";
String instancia="tandaC1_1000Vid_240Mins_7Prove_1XreG_1XreQ";







//String instancia="500Vid_240Mins_6Prove";
//String instancia="1000Vid_240Mins_6Prove";
//String instancia="2_6161Vid_240Mins_6Prove_Grup100";//probar este agrupando de a 100
//String instancia="2_2000Vid_240Mins_6Prove";
//String instancia="2_1000Vid_240Mins_6Prove";
//String instancia="2_00Vid_240Mins_6Prove";
//String instancia="2_200Vid_240Mins_6Prove";


      //  String instancia="1000Vid_240Mins_4Prove";
       // String instancia="6161Vid_240Mins_6Prove_10XreG_50XreQ";
     //   6161Vid_300Mins_6Prove_7XreG_15XreQ
       // 6161Vid_300Mins_6Prove_3XreG_7XreQ
     /*
      String instancia="6161Vid_300Mins_6Prove_20XreG_100XreQ";
      String instancia="6161Vid_300Mins_6Prove_15XreG_75XreQ";
       String instancia="6161Vid_240Mins_6Prove_20XreG_100XreQ";
      estas dieron error de memoria

        esta intancia dio problema de
        CPLEX 12.6.3.0: timelimit=86400
        error running /usr/local/ampl/cplex:
        termination code 9
                <BREAK>
   */

                
      //  String instancia="6161Vid_300Mins_6Prove_3XreG_7XreQ";

      //  String instancia="6161Vid_300Mins_6Prove_1XreG_1XreQ";
       // String instancia="6161Vid_240Mins_6Prove_20XreG_100XreQ";
      //  String instancia="6161Vid_240Mins_6Prove_1XreG_1XreQ";



        GeneradorDataAmpl gene = new GeneradorDataAmpl();
        CdnInstancia inst = new  CdnInstancia( instancia);
        List<String> aLines = new ArrayList<>();
        aLines.add("data;");
        //aLines.add("param umbralQoS:="+inst.getAlfa_Qos()+";");
       // aLines.add("set set_umbralQoS =5000.0 10000.0 15000.0 20000.0 25000.0 30000.0 35000.0 40000.0 45000.0 50000.0 55000.0 60000.0 ;");
       // aLines.add("set set_umbralQoS = 6000000000000.0 ;");
        aLines.add("set set_umbralQoS = 2 33;");
        aLines.add("param CR:="+inst.getCR()+";");
        aLines.add("param cantproveedores:="+inst.getCant_proveedores()+";");
        aLines.add("param cantdcs:="+inst.getCant_centrosDatos()+";");
        aLines.add("param tiempoTotalHoras:="+(int) Math.ceil(inst.getCant_pasosTiempo()/(double)60)+";");
        aLines.add("param cantRegionesUsuarios:="+inst.getCant_regionesUsuarios()+";");
        aLines.add("param cantvideos:="+inst.getCant_contenidos()+";");
        aLines.add(inst.get_pVideosProveedor());
        aLines.add(inst.get_qQos());
        aLines.add(inst.get_Costos());
        aLines.add(inst.get_dDemandaSet());
       // aLines.add(inst.get_dDemanda());
      //  aLines.add(inst.get_dDemanda_test(1,1,1));


        try {
            System.out.println(System.getProperty("user.dir")+"/AMPL/archivosDAT/"+instancia+".dat");
            gene.writeLargerTextFile(System.getProperty("user.dir")+"/AMPL/archivosDAT/"+instancia+".dat", aLines, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void writeLargerTextFile(String aFileName, List<String> aLines, boolean appendMode) throws IOException {
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

}
