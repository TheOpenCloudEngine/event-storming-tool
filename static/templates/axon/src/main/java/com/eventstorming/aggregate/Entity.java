forEach: Aggregate
fileName: {{namePascalCase}}Aggregate.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/aggregate
---
package {{options.package}}.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import org.springframework.beans.BeanUtils;
import java.util.List;

import {{options.package}}.command.*;
import {{options.package}}.event.*;

@Aggregate
public class {{namePascalCase}}Aggregate {

    {{#aggregateRoot.fieldDescriptors}}
    {{#isKey}}
    @AggregateIdentifier
    {{/isKey}}
    private {{className}} {{nameCamelCase}};
    {{/aggregateRoot.fieldDescriptors}}

    private {{namePascalCase}}Aggregate(){}

{{#lifeCycles}}
    {{#events}}
    @CommandHandler
    public void on({{namePascalCase}}Command command){
        {{namePascalCase}}Event {{nameCamelCase}} = new {{namePascalCase}}Event();
        BeanUtils.copyProperties(command, {{nameCamelCase}});
        AggregateLifecycle.apply({{nameCamelCase}});
        {{#relationCommandInfo}}
        {{#commandValue}}
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        {{options.package}}.external.{{aggregate.namePascalCase}} {{aggregate.nameCamelCase}} = new {{options.package}}.external.{{aggregate.namePascalCase}}();
        // mappings goes here
        {{relationCommandInfo.boundedContext.namePascalCase}}Application.applicationContext.getBean({{options.package}}.external.{{aggregate.namePascalCase}}Service.class)
            .{{nameCamelCase}}({{aggregate.nameCamelCase}});

        {{/commandValue}}
        {{/relationCommandInfo}}
    }

    @EventSourcingHandler
    public void on({{namePascalCase}}Event event) {
        BeanUtils.copyProperties(event, this);
    }

    {{/events}}

{{/lifeCycles}}

{{#aggregateRoot.fieldDescriptors}}
    public {{className}} get{{namePascalCase}}() {
        return {{nameCamelCase}};
    }

    public void set{{namePascalCase}}({{className}} {{nameCamelCase}}) {
        this.{{nameCamelCase}} = {{nameCamelCase}};
    }
{{/aggregateRoot.fieldDescriptors}}




}
