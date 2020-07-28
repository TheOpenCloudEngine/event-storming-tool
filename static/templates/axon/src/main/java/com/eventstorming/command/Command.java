forEach: Command
fileName: {{namePascalCase}}Command.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/command
---
package {{options.package}}.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class {{namePascalCase}}Command {

    {{#aggregate.aggregateRoot.fieldDescriptors}}
    {{#isKey}}
    @TargetAggregateIdentifier
    {{/isKey}}
    private {{className}} {{nameCamelCase}};
    {{/aggregate.aggregateRoot.fieldDescriptors}}

    {{#aggregate.aggregateRoot.fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{nameCamelCase}};
    }

    public void set{{namePascalCase}}({{className}} {{nameCamelCase}}) {
        this.{{nameCamelCase}} = {{nameCamelCase}};
    }
    {{/aggregate.aggregateRoot.fieldDescriptors}}
}
