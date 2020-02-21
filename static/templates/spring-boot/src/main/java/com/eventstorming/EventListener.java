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

    {{#policies}}
        {{#relationEventInfo}}
    @StreamListener(KafkaProcessor.INPUT)
    public void on{{relationEventInfo.namePascalCase}}Listener(@Payload {{relationEventInfo.namePascalCase}} {{relationEventInfo.name}}){

        if({{relationEventInfo.name}}.isMe()){
            System.out.println("##### listener {{relationEventInfo.name}} : " + {{relationEventInfo.name}}.toJson());
        }
    }
        {{/relationEventInfo}}
    {{/policies}}

}
