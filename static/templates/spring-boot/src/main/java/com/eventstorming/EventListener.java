forEach: Aggregate
fileName: {{namePascalCase}}EventListener.java
path: {{boundedContext}}/{{{options.packagePath}}}
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
public class {{namePascalCase}}EventListener{

    @StreamListener(KafkaProcessor.INPUT)
    public void onListener(@Payload String message){
        //event emmit code
        System.out.println("##### listener : "+message);

        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        try{
            AbstractEvent abstractEvent = objectMapper.readValue(message, AbstractEvent.class);
    {{#policies}}
        {{#relationEventInfo}}
            if(abstractEvent.getEventType().equals({{relationEventInfo.namePascalCase}}.class.getSimpleName())){
                {{relationEventInfo.namePascalCase}} {{relationEventInfo.name}}=objectMapper.readValue(message,{{relationEventInfo.namePascalCase}}.class);
            }
        {{/relationEventInfo}}
    {{/policies}}

        }catch(Exception e){

        }
    }

}
