path: {{name}}/{{{options.packagePath}}}/query
---
package {{options.package}}.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import {{options.package}}.command.*;
import {{options.package}}.event.*;
import {{options.package}}.aggregate.*;

@Service
@ProcessingGroup("{{nameCamelCase}}_Policy")
public class PolicyHandler{

//    private final Map<Long, Order> {{namePascalCase}}Map = new HashMap<>();

    {{#policies}}
        {{#relationEventInfo}}
    @EventHandler
    public void whenever{{eventValue.namePascalCase}}_{{namePascalCase}}({{eventValue.namePascalCase}}Event {{eventValue.nameCamelCase}}){
        System.out.println({{eventValue.nameCamelCase}}.toString());
    }
        {{/relationEventInfo}}
    {{/policies}}

}
