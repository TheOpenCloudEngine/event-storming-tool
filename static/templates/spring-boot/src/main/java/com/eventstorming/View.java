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

{{#viewFieldDescriptors}}
    {{#isKey}}
        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
    {{/isKey}}
        private {{className}} {{name}};
{{/viewFieldDescriptors}}


        {{#viewFieldDescriptors}}
        public {{className}} get{{name}}() {
            return {{name}};
        }

        public void set{{name}}({{className}} {{name}}) {
            this.{{name}} = {{name}};
        }
        {{/viewFieldDescriptors}}

}
