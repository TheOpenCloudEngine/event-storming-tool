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
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    {{#policies}}
        {{#relationEventInfo}}
    @StreamListener(KafkaProcessor.INPUT)
    public void whenever{{eventValue.namePascalCase}}_{{namePascalCase}}(@Payload {{eventValue.namePascalCase}} {{eventValue.nameCamelCase}}){

        if({{eventValue.nameCamelCase}}.isMe()){
            System.out.println("##### listener {{namePascalCase}} : " + {{eventValue.nameCamelCase}}.toJson());
        }
    }
        {{/relationEventInfo}}
    {{/policies}}

}
