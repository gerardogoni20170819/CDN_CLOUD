
package problema;

import org.uma.jmetal.problem.Problem;
import soluciones.CDNSolution;
import utils.CdnInstancia;

public interface CDNTypeProblem extends Problem<CDNSolution> {

      double getGenMutationProbabilityAfterGreedy();

      void setGenMutationProbabilityAfterGreedy(double genMutationProbabilityAfterGreedy);

    int getFormaInit();

    void setFormaInit(int formaDeInit);

     CdnInstancia getInstanciaDatos();

    int getGreedyDeterministic_level();

    void setGreedyDeterministic_level(int deterministic_level);

}
