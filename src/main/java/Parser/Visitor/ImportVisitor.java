package Parser.Visitor;

import Model.Resource;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.List;

public class ImportVisitor extends VoidVisitorAdapter<Resource> {

  /**
   * visits iterates on each class import
   *
   * @param cl
   * @param resource
   */
  public void visit(ImportDeclaration cl, Resource resource) {
    super.visit(cl, resource);
    List<String> dependencies = resource.getDependencies();
    if (addPackagePathToDependencies(cl, dependencies)) {
      resource.setDependencies(dependencies);
    }
  }

  /**
   * addPackagePathToDependencies replaces the dependence inside dependencies with their package
   * path.
   *
   * @param cl
   * @param dependencies
   * @return boolean
   */
  private boolean addPackagePathToDependencies(ImportDeclaration cl, List<String> dependencies) {
    String dependenceName =
        cl.getNameAsString().substring(cl.getNameAsString().lastIndexOf(".") + 1);
    int indexElement = dependencies.indexOf(dependenceName);
    if (indexElement != -1) {
      dependencies.remove(indexElement);
      dependencies.add(cl.getNameAsString());
      return true;
    }
    return false;
  }
}
