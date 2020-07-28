forEach: ViewEventInfo
fileName: {{namePascalCase}}Event.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/event
---
package {{options.package}}.event;

public class {{namePascalCase}}Event {

    {{#fieldDescriptors}}
    private {{className}} {{name}};
    {{/fieldDescriptors}}

    {{#fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{name}};
    }

    public void set{{namePascalCase}}({{className}} {{name}}) {
        this.{{name}} = {{name}};
    }
    {{/fieldDescriptors}}
}