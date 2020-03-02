forEach: View
fileName: {{namePascalCase}}ViewHandler.java
path: {{boundedContext.name}}/{{{options.packagePath}}}
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
public class {{namePascalCase}}ViewHandler {
{{#isCqrs}}
    @Autowired
    private {{namePascalCase}}Repository {{nameCamelCase}}Repository;



//{{#sourceEvent}}
//@StreamListener(KafkaProcessor.INPUT)
//public void on{{sourceEvent.namePascalCase}}(@Payload {{sourceEvent.namePascalCase}} {{sourceEvent.nameCamelCase}}) {
//        try {
//            if ({{sourceEvent.nameCamelCase}}.isMe()) {
//                // view 객체 생성
//                {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}();
//                // view 객체에 이벤트의 eventDirectValue 를 set 함
//                {{#viewFieldDescriptors}}
//                    {{nameCamelCase}}.set{{namePascalCase}}({{viewFieldDescriptors.eventDirectValue}});
//                {{/viewFieldDescriptors}}
//
//                // view 레파지 토리에 save
//                {{nameCamelCase}}Repository.save({{nameCamelCase}});
//
//                // 만약 View 컬럼명의 value 와 소스이벤트 컬럼명의 value 가 같으면
//            }
//        }catch (Exception e){
//        e.printStackTrace();
//        }
//}
//{{/sourceEvent}}

{{#viewFieldDescriptors}}
        @StreamListener(KafkaProcessor.INPUT)
        public void on{{sourceEvent.namePascalCase}}(@Payload {{sourceEvent.namePascalCase}} {{sourceEvent.nameCamelCase}}) {
                try {
                    if ({{sourceEvent.nameCamelCase}}.isMe()) {
                        System.out.println("##### listener : " + {{sourceEvent.nameCamelCase}}.toJson());
                        {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}();
                        {{#fieldDescriptors}}
                        {{nameCamelCase}}.set{{namePascalCase}}({{viewFieldDescriptors.eventDirectValue}});
                        {{/fieldDescriptors}}
                        {{nameCamelCase}}Repository.save({{nameCamelCase}});
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
 {{/viewFieldDescriptors}}

{{/isCqrs}}
{{#isMash}}
        Mash
{{/isMash}}
{{#isgraph}}
        isgraph
{{/isgraph}}
}