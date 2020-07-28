forEach: View
fileName: {{namePascalCase}}ViewQuery.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/view
---
package {{options.package}}.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import {{options.package}}.event.*;

@Service
@ProcessingGroup("{{nameCamelCase}}_ViewQuery")
public class {{namePascalCase}}ViewQuery {

    private final Map<String, {{namePascalCase}}View> viewMap = new HashMap<>();

    {{#createRules}}
    @EventHandler
    public void when{{when.namePascalCase}}_then_{{operation}}_{{_index}} ({{when.namePascalCase}}Event {{when.nameCamelCase}}) {
        try {
            String viewId = UUID.randomUUID().toString();
            // view 객체 생성
            {{namePascalCase}}View {{nameCamelCase}} = new {{namePascalCase}}View();
            // view 객체에 이벤트의 Value 를 set 함
        {{#fieldMapping}}
            {{nameCamelCase}}.set{{viewField.namePascalCase}}({{when.nameCamelCase}}.get{{eventField.namePascalCase}}());
        {{/fieldMapping}}
            // view map 에 저장
            viewMap.put(viewId, {{nameCamelCase}});
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    {{/createRules}}

    {{#updateRules}}
    @EventHandler
    public void when{{when.namePascalCase}}_then_{{operation}}_{{_index}}({{when.namePascalCase}}Event {{when.nameCamelCase}}) {
        try {
            // view 객체 조회
        {{#where}}
            viewMap.forEach((k, v) -> {
                if( v.get{{viewField.namePascalCase}}().equals({{when.nameCamelCase}}.get{{eventField.namePascalCase}}()){
                {{#fieldMapping}}
                    v.set{{viewField.namePascalCase}}({{when.nameCamelCase}}.get{{eventField.namePascalCase}}());
                {{/fieldMapping}}
                    viewMap.put(k, v);
                }
            });
        {{/where}}

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    {{/updateRules}}

    {{#deleteRules}}
    @EventHandler
    public void when{{when.namePascalCase}}_then_{{operation}}_{{_index}}({{when.namePascalCase}}Event {{when.nameCamelCase}}) {
        try {

        {{#where}}
            viewMap.forEach((k, v) -> {
                if( v.get{{viewField.namePascalCase}}().equals({{when.nameCamelCase}}.get{{eventField.namePascalCase}}()){
                    viewMap.remove(k);
                }
            });
        {{/where}}

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    {{/deleteRules}}

    @QueryHandler
    public List<{{namePascalCase}}View> handle(String query) {
        return new ArrayList<>(viewMap.values());
    }
}