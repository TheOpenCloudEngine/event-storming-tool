forEach: RelationCommandInfo
fileName: {{commandValue.aggregate.namePascalCase}}Service.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/external
---

package {{options.package}}.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * Created by uengine on 2018. 11. 21..
 */

@FeignClient(name="{{commandValue.aggregate.boundedContext.name}}", url="http://{{commandValue.aggregate.boundedContext.name}}:8080")
public interface {{commandValue.aggregate.namePascalCase}}Service {

    @RequestMapping(method= RequestMethod.{{commandValue.restRepositoryInfo.method}}, path="/{{commandValue.aggregate.namePlural}}")
    public void {{commandValue.nameCamelCase}}(@RequestBody {{commandValue.aggregate.namePascalCase}} {{commandValue.aggregate.nameCamelCase}});

}