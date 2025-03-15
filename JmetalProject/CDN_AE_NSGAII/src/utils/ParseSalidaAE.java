package utils; /**
 * Created by ggoni on 19/09/17.
 */

import utils.CdnInstancia;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ParseSalidaAE {


    public ParseSalidaAE() {
    }

    public static void main(String[] args){
        ParseSalidaAE pr = new ParseSalidaAE();
        pr.transformaArchivoRecursos();

    }


    public void transformaArchivoRecursos(){
        FileReader input = null;
        BufferedWriter writer =null;
        boolean primeraLinea = true;

        CdnInstancia cndInst = new CdnInstancia();

        try {
            String instancia ="300Vid_300Mins_6Prove_1XreG_1XreQ";
            //String nombreArchivoEntrada ="OnCost_300Vid_300Mins_6Prove_1XreG_1XreQ.txt";
            //String nombreArchivoEntrada ="todasInstanciasPareto.txt";
            String nombreArchivoEntrada ="GRAFICA_GREEDY_old.TXT";
            //String nombreArchivoSalida ="OnCost_300Vid_300Mins_6Prove_1XreG_1XreQ_SALIDA_PROCESADA.txt";
            String nombreArchivoSalida ="GRAFICA_GREEDY.TXT";
            cndInst.leerPropertiesInstancia(instancia);
            String rutaSalida = cndInst.getRutaAbsolutaSalidaInstancia();

            Path path = Paths.get(rutaSalida+nombreArchivoSalida);
            input = new FileReader(rutaSalida+nombreArchivoEntrada);
            BufferedReader bufRead = new BufferedReader(input);


            writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);


            String myLine = null;
            List<double []> solutionSet = new ArrayList<>();
            while ( (myLine = bufRead.readLine()) != null)
            {
                String [] solucsString = myLine.split("\t");
                double [] solucDouble = new double[2];
                solucDouble[0]=Double.valueOf(solucsString[0]);
                solucDouble[1]=Double.valueOf(solucsString[1]);
                solutionSet.add(solucDouble);
            }
            List<double []> paretoGuardar = paretera(solutionSet);
            for (double [] solucGuardar :paretoGuardar) {
                writer.write(String.valueOf(solucGuardar[0])+"\t"+String.valueOf(solucGuardar[1]));
                writer.newLine();
            }
            writer.close();
            input.close();
          //  System.out.println("Archivo Origen: "+Constantes.rutaOrigen+"docs.video");
           // System.out.println("Archivo Destino: "+Constantes.rutaDestino+"docs.video");
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

//    public ArrayList <Double []> calcularPareto(ArrayList <Double []> listasoluciones) {
//        ArrayList <Double []> pareto = new ArrayList<>();
//
//
//        return pareto;
//    }


    private List<double []> paretera(List<double []> solutionSet){
        List<double []> population = solutionSet;
        List<double []> paretoDoublesolutionSet = new ArrayList<>();

        // dominateMe[i] contains the number of solutions dominating i
        int[] dominateMe = new int[population.size()];

        // iDominate[k] contains the list of solutions dominated by k
        List<List<Integer>> iDominate = new ArrayList<>(population.size());

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
            iDominate.add(new LinkedList<Integer>());
            dominateMe[p] = 0;
        }

        int flagDominate;
        for (int p = 0; p < (population.size() - 1); p++) {
            // For all q individuals , calculate if p dominates q or vice versa
            for (int q = p + 1; q < population.size(); q++) {

                    flagDominate = this.dominanceTest(solutionSet.get(p), solutionSet.get(q));

                if (flagDominate == -1) {
                    iDominate.get(p).add(q);
                    dominateMe[q]++;
                } else if (flagDominate == 1) {
                    iDominate.get(q).add(p);
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

        System.out.println("Soluciones NO dominadas: "+paretoDoublesolutionSet.size()+" Soluciones dominadas: "+(solutionSet.size()-paretoDoublesolutionSet.size()));

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


    private int dominanceTest(double [] solution1, double [] solution2) {
        int result ;
        boolean solution1Dominates = false ;
        boolean solution2Dominates = false ;

        int flag;
        double value1, value2;
        for (int i = 0; i < 2 ; i++) {
            value1 = solution1[i];
            value2 = solution2[i];
            if (value1  < value2) {
                flag = -1;
                //} else if (value1 / (1 + epsilon) > value2) {
            } else if (value2  < value1) {
                flag = 1;
            } else {
                flag = 0;
            }

            if (flag == -1) {
                solution1Dominates = true ;
            }

            if (flag == 1) {
                solution2Dominates = true ;
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
        return result ;
    }

}
