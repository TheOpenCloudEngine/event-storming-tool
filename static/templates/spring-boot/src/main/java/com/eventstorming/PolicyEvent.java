forEach: RelationEventInfo
fileName: {{eventValue.namePascalCase}}.java
path: {{boundedContext.name}}/{{{options.packagePath}}}
---
{{#eventValue}}
package {{options.package}};


public class {{eventValue.namePascalCase}} extends AbstractEvent {

{{#eventValue.fieldDescriptors}}
    private {{className}} {{name}};
{{/eventValue.fieldDescriptors}}

{{#eventValue.fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{name}};
    }

    public void set{{namePascalCase}}({{className}} {{name}}) {
        this.{{name}} = {{name}};
    }
{{/eventValue.fieldDescriptors}}
}
{{/eventValue}}
