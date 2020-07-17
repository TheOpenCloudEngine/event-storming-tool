forEach: RelationEventInfo
fileName: {{eventValue.namePascalCase}}Event.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/event
---

package {{options.package}}.event;

public class {{eventValue.namePascalCase}}Event{

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
