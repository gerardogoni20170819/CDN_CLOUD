package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Archivos {


    public void writeLargerTextFile(String aFileName, List<String> aLines, boolean appendMode) {
        try {
            File file = new File(aFileName);
            FileWriter fileWriter = new FileWriter(file,appendMode);
            for(String line : aLines){
                fileWriter.write(line+'\n');
            }
            fileWriter.flush();
            fileWriter.close();
        }  catch (IOException ex) {
            ex.printStackTrace();
        }
    }




   public  List<String> readLargerTextFileAlternate(String rutaCompletaArchivo) {
        List<String> leidas=new ArrayList<>();
        Path path = Paths.get(rutaCompletaArchivo);




        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            String line;
            while ((line = reader.readLine()) != null) {
                leidas.add(line);
            }
        return leidas;
        }  catch (IOException ex) {

//            try{
//
//                leidas.add("1.1 2.2");
//
//            File file = new File("/home/ggoni/Documentos/proye/proycodigo/salida/archivosQFaltan.txt");
//            FileWriter fileWriter = new FileWriter(file,true);
//
//                fileWriter.write(rutaCompletaArchivo+'\n');
//
//            fileWriter.flush();
//            fileWriter.close();
//            return leidas;
//
//            }  catch (IOException exe) {
//                exe.printStackTrace();
//            }



           ex.printStackTrace();
        }
        return null;
    }






}
