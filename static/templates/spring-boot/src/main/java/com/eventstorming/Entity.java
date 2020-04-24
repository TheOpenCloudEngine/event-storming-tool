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
    public void on{{trigger}}(){
    {{#events}}
        {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}();
        BeanUtils.copyProperties(this, {{nameCamelCase}});
        {{nameCamelCase}}.publishAfterCommit();

        {{#relationCommandInfo}}
        {{#commandValue}}
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        {{options.package}}.external.{{aggregate.namePascalCase}} {{aggregate.nameCamelCase}} = new {{options.package}}.external.{{aggregate.namePascalCase}}();
        // mappings goes here
        Application.applicationContext.getBean({{options.package}}.external.{{aggregate.namePascalCase}}Service.class)
            .{{nameCamelCase}}({{aggregate.nameCamelCase}});

        {{/commandValue}}
        {{/relationCommandInfo}}

    {{/events}}
    }

{{/lifeCycles}}

{{#aggregateRoot.fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{nameCamelCase}};
    }

    public void set{{namePascalCase}}({{className}} {{nameCamelCase}}) {
        this.{{nameCamelCase}} = {{nameCamelCase}};
    }
{{/aggregateRoot.fieldDescriptors}}




}
