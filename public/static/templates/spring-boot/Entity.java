package hello;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="{{name}}table")
public class {{name}} {

    {{#fieldDescriptors}}{{#isKey}}@Id @GeneratedValue(strategy=GenerationType.AUTO){{/isKey}}{{multiplicity}}
    private {{type}} {{name}};
    {{/fieldDescriptors}}

    public {{name}}() {}

    {{#fieldDescriptors}}
    public {{className}} get{{nameCamel}}() {
        return {{name}};
    }

    public void set{{nameCamel}}({{className}} {{name}}) {
        this.{{name}} = {{name}};
    }
    {{/fieldDescriptors}}

}