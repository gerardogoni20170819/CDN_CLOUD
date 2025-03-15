package utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserNohup {

    public static void main(String[] args){
        String rutaArchivo = "/home/ggoni/Documentos/proye/proycodigo/cluster/nohupS.out";
        String rutaArchivoSalida = "/home/ggoni/Documentos/proye/proycodigo/cluster/urlDescargar.sh";
        boolean appendMode=true;
        Archivos ar = new Archivos();
        //ar.writeLargerTextFile(rutaArchivo,lineas,appendMode);

        List<String> lineasArchivo=ar.readLargerTextFileAlternate(rutaArchivo);
       // ArrayList<String> urlGuardadas=new ArrayList<>();
       // ArrayList<String> =new ArrayList<>();
        HashMap<String,String> urlGuardadas = new HashMap<>();
        HashMap<String,String> todasUrls = new HashMap<>();
        HashMap<String,String> urlsAdescargar = new HashMap<>();
int totalUrllevantada=0;
int totalGuardadasPosta=0;
int totalparaDescargar=0;



        //se usa hasmap para eliminar repetidas
        //find . -name \*.gz -print > guardadas.txt

        for (String linea:lineasArchivo){

            String rexpre="(ftp\\.ncdc\\.noaa\\.gov/pub/data/noaa/(\\d|/|-)*\\.gz)";
            Pattern p = Pattern.compile(rexpre);
            Matcher m = p.matcher(linea);

            if(linea.contains("saved") && m.find()){
                urlGuardadas.put(m.group(0),m.group(0));
                totalGuardadasPosta++;
            }

            if(m.find()){
                //System.out.println(m.group(0));
                todasUrls.put(m.group(0),m.group(0));
                totalUrllevantada++;
            }
        }
        System.out.println("Fin for");
        for (Map.Entry<String, String> algoritmo : todasUrls.entrySet()) {
            String urlEnTodas = algoritmo.getKey();
            //si la url no es de las guadadas va para descargar entonces
            if(!urlGuardadas.containsKey(urlEnTodas)){
                urlsAdescargar.put(urlEnTodas,urlEnTodas);
                totalparaDescargar++;
            }
        }
        System.out.println("Fin for 2");

        ArrayList<String> urlsAdescargarLista = new ArrayList<>();
        for (Map.Entry<String, String> algoritmo : urlsAdescargar.entrySet()) {
            urlsAdescargarLista.add(algoritmo.getKey());

        }
        System.out.println("Fin for 3");
        System.out.println("TotalUrls"+totalUrllevantada);
        System.out.println("Totalguardadas"+totalGuardadasPosta);
        System.out.println("Totaldescaragar"+totalparaDescargar);

        Collections.sort(urlsAdescargarLista);

        ar.writeLargerTextFile(rutaArchivoSalida,urlsAdescargarLista,false);


       // System.out.println("Datos guardados en: "+rutaArchivo);


    }



}
