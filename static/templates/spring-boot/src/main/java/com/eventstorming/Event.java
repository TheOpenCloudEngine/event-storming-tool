forEach: Event
fileName: {{namePascalCase}}.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
package {{options.package}};

import java.text.SimpleDateFormat;
import java.util.Date;

public class {{namePascalCase}} extends AbstractEvent {

    {{#fieldDescriptors}}
    private {{className}} {{nameCamelCase}};
    {{/fieldDescriptors}}

    public {{namePascalCase}}(){
        this.setEventType(this.getClass().getSimpleName());
        SimpleDateFormat defaultSimpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
        this.timestamp = defaultSimpleDateFormat.format(new Date());
    }

    public {{namePascalCase}}({{aggregate.namePascalCase}} {{aggregate.name}}){
        this();
        // TODO set properties
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
