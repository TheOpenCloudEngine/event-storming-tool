forEach: View
fileName: {{namePascalCase}}View.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/view
---
package {{options.package}}.view;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="{{namePascalCase}}_table")
public class {{namePascalCase}}View {

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
