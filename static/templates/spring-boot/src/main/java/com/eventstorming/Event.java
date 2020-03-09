forEach: Event
fileName: {{namePascalCase}}.java
path: {{boundedContext.name}}/{{{options.packagePath}}}
---
package {{options.package}};

public class {{namePascalCase}} extends AbstractEvent {

    {{#fieldDescriptors}}
    private {{className}} {{nameCamelCase}};
    {{/fieldDescriptors}}

    public {{namePascalCase}}(){
        super();
    }

    {{#fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{nameCamelCase}};
    }

    public void set{{namePascalCase}}({{className}} {{nameCamelCase}}) {
        this.{{nameCamelCase}} = {{nameCamelCase}};
    }
    {{/fieldDescriptors}}
}
