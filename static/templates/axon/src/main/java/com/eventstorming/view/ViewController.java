forEach: View
fileName: {{namePascalCase}}ViewController.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/view
---
package {{options.package}}.view;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import {{options.package}}.aggregate.*;
import {{options.package}}.command.*;

@RestController
public class {{ namePascalCase }}ViewController {

  private final QueryGateway queryGateway;

  public {{ namePascalCase }}ViewController(QueryGateway queryGateway) {
      this.queryGateway = queryGateway;
  }

  @GetMapping("/{{ nameCamelCase }}/all-views")
  public List<{{ namePascalCase }}View> findAllViews() {
    return queryGateway.query(new String(), ResponseTypes.multipleInstancesOf({{ namePascalCase }}View.class))
    .join();
  }
}
