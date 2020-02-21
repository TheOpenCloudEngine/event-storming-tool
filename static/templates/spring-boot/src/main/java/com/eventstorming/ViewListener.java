forEach: View
fileName: {{namePascalCase}}Listener.java
path: {{boundedContext}}/{{{options.packagePath}}}
---
package {{options.package}};


import com.example.template.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class {{namePascalCase}}Listener {
{{#isCqrs}}
    @Autowired
    private {{aggregate.namePascalCase}}Repository {{aggregate.nameCamelCase}}Repository;

{{#viewFieldDescriptors}}
        @StreamListener(KafkaProcessor.INPUT)
        public void on{{sourceEvent.namePascalCase}}(@Payload {{sourceEvent.namePascalCase}} {{sourceEvent.nameCamelCase}}) {
                try {
                    if ({{sourceEvent.nameCamelCase}}.isMe()) {
                        System.out.println("##### listener : " + {{sourceEvent.nameCamelCase}}.toJson());
                        {{aggregate.namePascalCase}} {{aggregate.nameCamelCase}} = new {{aggregate.namePascalCase}}();
                        {{#aggregate.fieldDescriptors}}
                        {{aggregate.nameCamelCase}}.set{{namePascalCase}}({{viewFieldDescriptors.eventDirectValue}});
                        {{/aggregate.fieldDescriptors}}
                        {{aggregate.nameCamelCase}}Repository.save({{aggregate.nameCamelCase}});
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
 {{/viewFieldDescriptors}}

    public {{aggregate.namePascalCase}} save(String data){
        ObjectMapper mapper = new ObjectMapper();
        {{aggregate.namePascalCase}} {{aggregate.nameCamelCase}} = null;

        try {
            {{aggregate.nameCamelCase}} = mapper.readValue(data, {{aggregate.namePascalCase}}.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return {{aggregate.nameCamelCase}}Repository.save({{aggregate.nameCamelCase}});
    }
{{/isCqrs}}
{{#isMash}}
        Mash
{{/isMash}}
{{#isgraph}}
        isgraph
{{/isgraph}}
}