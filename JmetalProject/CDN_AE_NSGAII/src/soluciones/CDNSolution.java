
package soluciones;

import org.uma.jmetal.solution.Solution;
import utils.CdnInstancia;
import utils.CdnItem;


public interface CDNSolution extends Solution<CdnItem> {

      void factibilizar();
     boolean isFactible();
     int getIdMemoriaUsada();
     void toFileGeneral(String directorioSalida);
    long  getTiempoConstruirZ();
     void toFilePareto(String directorioSalida);
     void toFileEvaluacion(String directorioSalida,String nombreArchivo);
    CdnInstancia getInstanciaDatos();
}
