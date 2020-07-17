forEach: Event
fileName: {{namePascalCase}}Event.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/event
---
package {{options.package}}.event;

public class {{namePascalCase}}Event {

    {{#fieldDescriptors}}
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
