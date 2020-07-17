forEach: View
fileName: {{namePascalCase}}ViewHandler.java
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

import {{options.package}}.event.*;

@Service
@ProcessingGroup("{{nameCamelCase}}_View")
public class {{namePascalCase}}ViewHandler {


    @Autowired
    private {{namePascalCase}}Repository {{nameCamelCase}}Repository;

    {{#createRules}}
    @EventHandler
    public void when{{when.namePascalCase}}_then_{{operation}}_{{_index}} ({{when.namePascalCase}}Event {{when.nameCamelCase}}) {
        try {
                // view 객체 생성
                {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}();
                // view 객체에 이벤트의 Value 를 set 함
            {{#fieldMapping}}
                {{nameCamelCase}}.set{{viewField.namePascalCase}}({{when.nameCamelCase}}.get{{eventField.namePascalCase}}());
            {{/fieldMapping}}
                // view 레파지 토리에 save
                {{nameCamelCase}}Repository.save({{nameCamelCase}});
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
            {{#viewField.isKey}}
                Optional<{{namePascalCase}}> {{nameCamelCase}}Optional = {{nameCamelCase}}Repository.findBy{{viewField.namePascalCase}}({{when.nameCamelCase}}.get{{eventField.namePascalCase}}());
                if( {{nameCamelCase}}Optional.isPresent()) {
                    {{namePascalCase}} {{nameCamelCase}} = {{nameCamelCase}}Optional.get();
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    {{#fieldMapping}}
                    {{nameCamelCase}}.set{{viewField.namePascalCase}}({{when.nameCamelCase}}.get{{eventField.namePascalCase}}());
                    {{/fieldMapping}}
                    // view 레파지 토리에 save
                    {{nameCamelCase}}Repository.save({{nameCamelCase}});
                }
            {{/viewField.isKey}}
            {{^viewField.isKey}}
                List<{{namePascalCase}}> {{nameCamelCase}}List = {{nameCamelCase}}Repository.findBy{{viewField.namePascalCase}}({{when.nameCamelCase}}.get{{eventField.namePascalCase}}());
                for({{namePascalCase}} {{nameCamelCase}} : {{nameCamelCase}}List){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                {{#fieldMapping}}
                    {{nameCamelCase}}.set{{viewField.namePascalCase}}({{when.nameCamelCase}}.get{{eventField.namePascalCase}}());
                {{/fieldMapping}}
                    // view 레파지 토리에 save
                    {{nameCamelCase}}Repository.save({{nameCamelCase}});
                }
            {{/viewField.isKey}}
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
                // view 레파지 토리에 삭제 쿼리
            {{#viewField.isKey}}
                {{nameCamelCase}}Repository.deleteById({{when.nameCamelCase}}.get{{eventField.namePascalCase}}());
            {{/viewField.isKey}}
            {{^viewField.isKey}}
                {{nameCamelCase}}Repository.deleteBy{{viewField.namePascalCase}}({{when.nameCamelCase}}.get{{eventField.namePascalCase}}());
            {{/viewField.isKey}}
        {{/where}}

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    {{/deleteRules}}
}