path: {{name}}/{{{options.packagePath}}}
---
package {{options.package}};

import {{options.package}}.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EventListener{
    {{ policies.toString() }}
    {{#policies}}
        {{#relationEventInfo}}
    @StreamListener(KafkaProcessor.INPUT)
    public void on{{eventValue.namePascalCase}}Listener(@Payload {{eventValue.namePascalCase}} {{eventValue.name}}){

        if({{eventValue.name}}.isMe()){
            System.out.println("##### listener {{name}} : " + {{eventValue.name}}.toJson());
        }
    }
        {{/relationEventInfo}}
    {{/policies}}

}
