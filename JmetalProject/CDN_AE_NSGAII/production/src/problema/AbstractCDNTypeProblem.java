package problema;

import org.uma.jmetal.problem.impl.AbstractGenericProblem;
import soluciones.CDNSolution;
import soluciones.DefaultCDNSolution;

@SuppressWarnings("serial")
public abstract class AbstractCDNTypeProblem extends AbstractGenericProblem<CDNSolution>
  implements CDNTypeProblem  {



  @Override
  public CDNSolution createSolution() {
    return new DefaultCDNSolution(this)  ;
  }
}
