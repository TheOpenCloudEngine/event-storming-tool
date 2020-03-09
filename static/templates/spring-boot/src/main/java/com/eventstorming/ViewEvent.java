forEach: ViewEventInfo
fileName: {{namePascalCase}}.java
path: {{boundedContext.name}}/{{{options.packagePath}}}
---
package {{options.package}};

public class {{namePascalCase}} extends AbstractEvent {

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