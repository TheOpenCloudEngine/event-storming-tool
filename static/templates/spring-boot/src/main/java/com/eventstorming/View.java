forEach: View
fileName: {{namePascalCase}}.java
path: {{boundedContext.name}}/{{{options.packagePath}}}
---
package {{options.package}};

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="{{namePascalCase}}_table")
public class {{namePascalCase}} {

{{#fieldDescriptors}}
    {{#isKey}}
        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
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
