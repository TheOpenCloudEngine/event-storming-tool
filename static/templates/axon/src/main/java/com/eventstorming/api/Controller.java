forEach: Aggregate
fileName: {{namePascalCase}}Controller.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/api
---
package {{options.package}}.api;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import {{options.package}}.aggregate.*;
import {{options.package}}.command.*;

@RestController
public class {{ namePascalCase }}Controller {

  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;

  public {{ namePascalCase }}Controller(CommandGateway commandGateway, QueryGateway queryGateway) {
      this.commandGateway = commandGateway;
      this.queryGateway = queryGateway;
  }
        {{#commands}}

        {{#isRestRepository}}
  @RequestMapping(value = "/{{ aggregate.nameCamelCase }}/{{nameCamelCase}}",
        method = RequestMethod.{{restRepositoryInfo.method}},
        produces = "application/json;charset=UTF-8")
  public void {{nameCamelCase}}(@RequestBody {{aggregate.namePascalCase}}Aggregate {{aggregate.nameCamelCase}})
        throws Exception {
          System.out.println("##### /{{aggregate.nameCamelCase}}/{{nameCamelCase}}  called #####");

          // make command

          // send command
//          commandGateway.send();

  }
        {{/isRestRepository}}


        {{^isRestRepository}}
  @RequestMapping(value = "/{{controllerInfo.apiPath}}",
        method = RequestMethod.{{controllerInfo.method}},
        produces = "application/json;charset=UTF-8")
  public void {{nameCamelCase}}(@RequestBody {{aggregate.namePascalCase}}Aggregate {{aggregate.nameCamelCase}})
        throws Exception {
          System.out.println("##### /{{aggregate.nameCamelCase}}/{{nameCamelCase}}  called #####");
  }
        {{/isRestRepository}}
        {{/commands}}
}
