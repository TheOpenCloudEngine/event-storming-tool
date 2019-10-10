<template>
    <codemirror
                ref="codemirror"
                :value="code"
                :options="cmOption"
                @ready="onCmReady"
                @Focus="onCmFocus"
                @input="onCmCodeChange"
                :height="500"
                >
    </codemirror>
</template>
<script>
    import 'codemirror/mode/clike/clike'
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/darcula.css'
    export default {
        data() {
            return {
                testXML:'',
                code:'',
                cmOption: {
                    autoCloseBrackets: true,
                    tabSize: 4,
                    lineNumbers: true,
                    line: true,
                    mode: 'text/x-java',
                    theme: 'darcula',
                    lineWrapping: true,
                    matchBrackets: true,
                },
                event :{
                    header: "Products",
                    variable:[
                        { attribute:"private", type : "Long", name:"id" , upName:"Id" },
                        { attribute:"public", type : "String", name:"stock" ,upName:"Stock"},
                        { attribute:"public", type : "int", name:"price",upName:"Price"}
                    ],
                    functionName:[
                    ],
                },
            }
        },
        created(){
            this.eventTemplate();
        },
        methods: {
            onCmReady(cm) {
                console.log('the editor is readied!', cm)

            },
            onCmFocus(cm) {
                console.log('the editor is focus!', cm)
            },
            onCmCodeChange(newCode) {
                console.log('this is new code', newCode)
                this.code = newCode
            },
            eventTemplate(){
                this.code = Mustache.render(
                    "package hello;\n" +
                    "\n" +
                    "import javax.persistence.*;\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "@Entity\n" +
                    "@Table(name=\"{{header}}\")\n" +
                    "public class {{header}} {\n" +
                    "\n" +
                    "    @Id\n" +
                    "    @GeneratedValue\n"+
                    "    {{#variable}}\n" +
                    "     {{attribute}} {{type}} {{name}};\n" +
                    "    {{/variable}}\n" +
                    "\n" +
                    "    {{ #variable }}\n" +
                    "      public {{type}} get{{upName}}() {\n" +
                    "           return {{name}};\n" +
                    "       }\n" +
                    "      public void set{{upName}}({{attribute}} {{name}}) {\n" +
                    "           this.{{name}} = {{name}};\n" +
                    "       }\n\n\n"+
                    "    {{ /variable }}\n" +
                    "\n\n\n\n" +
                    "@PostPersist @PostUpdate\n" +
                    "    private void publishStart() {\n" +
                    "        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);\n" +
                    "\n" +
                    "        ObjectMapper objectMapper = new ObjectMapper();\n" +
                    "        String json = null;\n" +
                    "\n" +
                    "        {{header}}Changed productChanged = new ProductChanged();\n" +
                    " {{ #variable }}\n" +
                    "        {{header}}Changed.setProduct{{upName}}(this.{{name}});\n" +
                    " {{ /variable }}\n" +
                    "        try {\n" +
                    "            json = objectMapper.writeValueAsString(productChanged);\n" +
                    "        } catch (JsonProcessingException e) {\n" +
                    "            throw new RuntimeException(\"JSON format exception\", e);\n" +
                    "        }\n" +
                    "\n" +
                    "        if( json != null ){\n" +
                    "            Environment env = Application.applicationContext.getEnvironment();\n" +
                    "            String topicName = env.getProperty(\"eventTopic\");\n" +
                    "            ProducerRecord producerRecord = new ProducerRecord<>(topicName, json);\n" +
                    "            kafkaTemplate.send(producerRecord);\n" +
                    "        }\n" +
                    "    }" +
                    "}\n", this.event)

            }

        },
        mounted () {
        }
    }
</script>
<style>
    .CodeMirror {
        height: auto;
    }
</style>
