forEach: View
fileName: {{namePascalCase}}Repository.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/view
---
package {{options.package}}.view;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface {{namePascalCase}}Repository extends CrudRepository<{{namePascalCase}}View, Long> {

{{#updateRules}}
    {{#where}}
    {{^viewField.isKey}}
    List<{{namePascalCase}}View> findBy{{viewField.namePascalCase}}({{viewField.className}} {{viewField.nameCamelCase}});
    {{/viewField.isKey}}
    {{/where}}
{{/updateRules}}

{{#deleteRules}}
    {{#where}}
    {{^viewField.isKey}}
        void deleteBy{{viewField.namePascalCase}}({{viewField.className}} {{viewField.nameCamelCase}});
    {{/viewField.isKey}}
    {{/where}}
{{/deleteRules}}
}