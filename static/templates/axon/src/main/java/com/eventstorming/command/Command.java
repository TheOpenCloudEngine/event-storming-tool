forEach: Event
fileName: {{namePascalCase}}Command.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/command
---
package {{options.package}}.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import {{options.package}}.*;

public class {{namePascalCase}}Command {

    {{#fieldDescriptors}}
    {{#isKey}}
    @TargetAggregateIdentifier
    {{/isKey}}
    private {{className}} {{nameCamelCase}};
    {{/fieldDescriptors}}

    {{#fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{nameCamelCase}};
    }

    public void set{{namePascalCase}}({{className}} {{nameCamelCase}}) {
        this.{{nameCamelCase}} = {{nameCamelCase}};
    }
    {{/fieldDescriptors}}
}
