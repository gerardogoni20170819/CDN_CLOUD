package utils;

import org.uma.jmetal.util.JMetalException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GenerarQsubs {

    public static void main(String[] args) throws JMetalException, IOException {
        String nombreCarpetaIntancia="AMPL4";
        String nombrePropertiesEval="eval_AMPL4";
        String prefijoNombreArchivos="ampl4";
        String walltime="00:05:39:00";
       // String rutaLocalDestinoArchivos="F:/PROYE/solocodigo/proycodigo/cluster/tocluster/";
        String rutaLocalDestinoArchivos="/home/ggoni/Documentos/proye/proycodigo/cluster/tocluster/";
        String colaEjecucion="small_jobs";

        int TopeiterInferiro=50;
        int TopeiterSuperiro=50;

  //  for(int iterInferior=0;iterInferior<TopeiterInferiro;iterInferior=iterInferior+10){
        int iterInferior=0;
        int hilosParalelos=5;
        int iterSuperior=hilosParalelos-1;

        int cantEjecuciones=50;
        int cantCiclosFor = cantEjecuciones/hilosParalelos;
        ArrayList<String> ejecutar_Qsubs = new ArrayList();
        for(int iter=0;iter<cantCiclosFor;iter++){


            String pedidaRecursos="nodes=1:ppn="+hilosParalelos;
            if(iterSuperior<=9){
                pedidaRecursos="nodes=1:class5:ppn="+hilosParalelos;
            }

            String nombreDistintivoTrabajo=prefijoNombreArchivos+"eval_iter10Paralelos"+iterInferior+"a"+iterSuperior;
            ArrayList<String> Plantilla_qsub = new ArrayList();//tambien guardo tiempo en construir z
            Plantilla_qsub.add("#!/bin/bash");
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# Nombre del trabajo");
            Plantilla_qsub.add("#PBS -N "+nombreDistintivoTrabajo);
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# Requerimientos");
            Plantilla_qsub.add("#PBS -l "+pedidaRecursos+",walltime="+walltime);
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# Cola de ejecución");
            Plantilla_qsub.add("#PBS -q "+colaEjecucion);
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# Directorio de trabajo");
            Plantilla_qsub.add("#PBS -d /home/gerardo.goni/"+nombreCarpetaIntancia+"/");
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# Correo electrónico");
            Plantilla_qsub.add("#PBS -M gerardo.goni@fing.edu.uy");
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# Email");
            Plantilla_qsub.add("#PBS -m bea");
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# n: no mail will be sent.");
            Plantilla_qsub.add("# a: mail is sent when the job is aborted by the batch system.");
            Plantilla_qsub.add("# b: mail is sent when the job begins execution.");
            Plantilla_qsub.add("# e: mail is sent when the job terminates.");
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# Directorio donde se guardará la salida estándar del nuestro trabajo.");
            Plantilla_qsub.add("#PBS -o /home/gerardo.goni/"+nombreCarpetaIntancia+"/");
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# Directorio donde se guardará la salida de error del nuestro trabajo.");
            Plantilla_qsub.add("#PBS -e /home/gerardo.goni/"+nombreCarpetaIntancia+"/");
            Plantilla_qsub.add("");
            Plantilla_qsub.add("echo Job Name: $PBS_JOBNAME");
            Plantilla_qsub.add("echo Working directory: $PBS_O_WORKDIR");
            Plantilla_qsub.add("echo Queue: $PBS_QUEUE");
            Plantilla_qsub.add("echo Cantidad de tasks: $PBS_TASKNUM");
            Plantilla_qsub.add("echo Home: $PBS_O_HOME");
            Plantilla_qsub.add("echo Puerto del MOM: $PBS_MOMPORT");
            Plantilla_qsub.add("echo Nombre del usuario: $PBS_O_LOGNAME");
            Plantilla_qsub.add("echo Idioma: $PBS_O_LANG");
            Plantilla_qsub.add("echo Cookie: $PBS_JOBCOOKIE");
            Plantilla_qsub.add("echo Offset de numero de nodos: $PBS_NODENUM");
            Plantilla_qsub.add("echo Shell: $PBS_O_SHELL");
            Plantilla_qsub.add("echo JobID: $PBS_O_JOBID");
            Plantilla_qsub.add("echo Host: $PBS_O_HOST");
            Plantilla_qsub.add("echo Cola de ejecucion: $PBS_QUEUE");
            Plantilla_qsub.add("echo Archivo de nodos: $PBS_NODEFILE");
            Plantilla_qsub.add("echo Path: $PBS_O_PATH");
            Plantilla_qsub.add("echo");
            Plantilla_qsub.add("cd $PBS_O_WORKDIR");
            Plantilla_qsub.add("echo Current path:");
            Plantilla_qsub.add("pwd");
            Plantilla_qsub.add("echo");
            Plantilla_qsub.add("echo Nodos:");
            Plantilla_qsub.add("cat $PBS_NODEFILE");
            Plantilla_qsub.add("echo");
            Plantilla_qsub.add("echo Cantidad de nodos:");
            Plantilla_qsub.add("NPROCS=$(/usr/bin/wc -l < $PBS_NODEFILE)");
            Plantilla_qsub.add("echo $NPROCS");
            Plantilla_qsub.add("echo");
            Plantilla_qsub.add("");
            Plantilla_qsub.add("# Ejecuto la tarea");
            Plantilla_qsub.add("time /home/gerardo.goni/"+nombreCarpetaIntancia+"/"+nombreDistintivoTrabajo+".sh");

            boolean appendMode = false;
            Archivos ar = new Archivos();
            File directory = new File(rutaLocalDestinoArchivos+nombreCarpetaIntancia);
            if (! directory.exists()){
                directory.mkdir();
                System.out.println("Creando directorio "+rutaLocalDestinoArchivos+nombreCarpetaIntancia);

            }
            String nombreQsub ="do_"+nombreDistintivoTrabajo+".sh";
            ejecutar_Qsubs.add("qsub "+nombreQsub);
            ar.writeLargerTextFile(rutaLocalDestinoArchivos+nombreCarpetaIntancia+"/"+nombreQsub,Plantilla_qsub,appendMode);



            ArrayList<String> plantilla_ejecutador = new ArrayList();//tambien guardo tiempo en construir z


            for (int indiceEjecLinea=0; indiceEjecLinea<hilosParalelos;indiceEjecLinea++) {
                int indiceCreacionCarpetaJar=(indiceEjecLinea+1+iterInferior);
                plantilla_ejecutador.add("java -jar /home/gerardo.goni/"+nombreCarpetaIntancia+"/" + indiceCreacionCarpetaJar + "/CDN_AE_NSGAII.jar "+nombrePropertiesEval+" "+(indiceCreacionCarpetaJar-1)+" "+(indiceCreacionCarpetaJar-1)+" &");
            }





            plantilla_ejecutador.add("wait");
            ar = new Archivos();
            ar.writeLargerTextFile(rutaLocalDestinoArchivos+nombreCarpetaIntancia+"/"+nombreDistintivoTrabajo+".sh",plantilla_ejecutador,appendMode);








             iterInferior=iterInferior+hilosParalelos;
             iterSuperior=iterSuperior+hilosParalelos;
        }
       ArrayList<String> plantilla_creadorCarpetas = new ArrayList();
        plantilla_creadorCarpetas.add("for iter in {1.."+(iterSuperior-(hilosParalelos-1))+"}");
        plantilla_creadorCarpetas.add("do");
        plantilla_creadorCarpetas.add("mkdir $iter");
        plantilla_creadorCarpetas.add("cp CDN_AE_NSGAII.jar ./$iter/");
        plantilla_creadorCarpetas.add("done");
        plantilla_creadorCarpetas.add("chmod -R 744 .");
        Archivos ar = new Archivos();
        ar.writeLargerTextFile(rutaLocalDestinoArchivos+nombreCarpetaIntancia+"/"+"copy_jarNSGA.sh",plantilla_creadorCarpetas,false);

        ArrayList<String> plantilla_borradorCarpetas = new ArrayList();
        plantilla_borradorCarpetas.add("for iter in {1.."+(iterSuperior-(hilosParalelos-1))+"}");
        plantilla_borradorCarpetas.add("do");
        plantilla_borradorCarpetas.add("rm -r $iter");
        plantilla_borradorCarpetas.add("done");

         ar = new Archivos();
        ar.writeLargerTextFile(rutaLocalDestinoArchivos+nombreCarpetaIntancia+"/"+"borrar_jarNSGA.sh",plantilla_borradorCarpetas,false);


        ArrayList<String> plantilla_comandos = new ArrayList();

        plantilla_comandos.add("******************");
        plantilla_comandos.add("tar -zcf "+nombreCarpetaIntancia+".tar.gz /home/gerardo.goni/salida/"+nombreCarpetaIntancia);
        plantilla_comandos.add("tar -zcvf "+nombreCarpetaIntancia+"_logs.tar.gz *.e* *.o*  cdnLog.* jMetal.*");
        plantilla_comandos.add("scp -r gerardo.goni@rocks-cluster.fing.edu.uy:/home/gerardo.goni/"+nombreCarpetaIntancia+".tar.gz /ens/home01/g/gerardo.goni/aecdnlulu/fromcluster/"+nombreCarpetaIntancia+".tar.gz");
        plantilla_comandos.add("scp -r gerardo.goni@rocks-cluster.fing.edu.uy:/home/gerardo.goni/"+nombreCarpetaIntancia+"/"+nombreCarpetaIntancia+"_logs.tar.gz /ens/home01/g/gerardo.goni/aecdnlulu/fromcluster/"+nombreCarpetaIntancia+"_logs.tar.gz");
        plantilla_comandos.add("******************");
        plantilla_comandos.add("scp -r /ens/home01/g/gerardo.goni/aecdnlulu/tocluster/"+nombreCarpetaIntancia+"/*.sh gerardo.goni@rocks-cluster.fing.edu.uy:/home/gerardo.goni/"+nombreCarpetaIntancia+"/");
        plantilla_comandos.add("scp -r /ens/home01/g/gerardo.goni/aecdnlulu/tocluster/"+nombreCarpetaIntancia+"/*.jar gerardo.goni@rocks-cluster.fing.edu.uy:/home/gerardo.goni/"+nombreCarpetaIntancia+"/");
        plantilla_comandos.add("scp -r /ens/home01/g/gerardo.goni/aecdnlulu/tocluster/"+nombreCarpetaIntancia+"/*.txt gerardo.goni@rocks-cluster.fing.edu.uy:/home/gerardo.goni/"+nombreCarpetaIntancia+"/");
        plantilla_comandos.add("scp -r /ens/home01/g/gerardo.goni/aecdnlulu/tocluster/"+nombreCarpetaIntancia+"/*.properties gerardo.goni@rocks-cluster.fing.edu.uy:/home/gerardo.goni/resources/propertiesdir/");
        plantilla_comandos.add("scp -r /ens/home01/g/gerardo.goni/aecdnlulu/tocluster/"+nombreCarpetaIntancia+"/*.video gerardo.goni@rocks-cluster.fing.edu.uy:/home/gerardo.goni/resources/instancias/"+nombreCarpetaIntancia);
        plantilla_comandos.add("******************");
        plantilla_comandos.add( "dos2unix *.sh");

        plantilla_comandos.add("******************");
        plantilla_comandos.add("chmod -R 744 .");
        plantilla_comandos.add("******************");
        plantilla_comandos.add("******************");
        plantilla_comandos.add("./copy_jarNSGA.sh");
        plantilla_comandos.add("******************");
        plantilla_comandos.add("showq -u gerardo.goni");

        plantilla_comandos.addAll(ejecutar_Qsubs);

         ar = new Archivos();
        ar.writeLargerTextFile(rutaLocalDestinoArchivos+nombreCarpetaIntancia+"/"+"comandos.txt",plantilla_comandos,false);


        //  }




}


}
