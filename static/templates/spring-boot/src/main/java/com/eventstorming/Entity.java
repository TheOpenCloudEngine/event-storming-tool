forEach: Aggregate
fileName: {{namePascalCase}}.java
path: {{boundedContext.name}}/{{{options.packagePath}}}
---
package {{options.package}};

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="{{namePascalCase}}_table")
public class {{namePascalCase}} {

    {{#aggregateRoot.fieldDescriptors}}
    {{#isKey}}
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    {{/isKey}}
    private {{className}} {{nameCamelCase}};
    {{/aggregateRoot.fieldDescriptors}}

{{#lifeCycles}}
    {{annotation}}
    public void when_{{trigger}}_publishEvent(){
    {{#events}}
        {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}();
        BeanUtils.copyProperties(this, {{nameCamelCase}});
        {{nameCamelCase}}.publish();

    {{/events}}
    }

{{/lifeCycles}}
//{{#events}}
//    {{trigger}}
//    public void publish{{namePascalCase}}(){
//
//        {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}();
//        BeanUtils.copyProperties(this, {{nameCamelCase}});
//        {{nameCamelCase}}.publish();
//
//    }
//{{/events}}

{{#aggregateRoot.fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{nameCamelCase}};
    }

    public void set{{namePascalCase}}({{className}} {{nameCamelCase}}) {
        this.{{nameCamelCase}} = {{nameCamelCase}};
    }
{{/aggregateRoot.fieldDescriptors}}

}
