<template>
    <v-container>
        <v-card>
            <v-card-title v-if="value[0][0]">
                {{value[0][0].name}}
            </v-card-title>
            <v-card-text>
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
            </v-card-text>

        </v-card>

    </v-container>
</template>
<script>
    import 'codemirror/mode/yaml/yaml'
    import 'codemirror/mode/dockerfile/dockerfile'
    import 'codemirror/mode/markdown/markdown'
    import 'codemirror/mode/properties/properties'
    import 'codemirror/mode/shell/shell'
    import 'codemirror/mode/xml/xml'

    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/darcula.css'
    import 'codemirror/mode/clike/clike'
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/darcula.css'

    export default {
        props: {
            value: Array
        },
        data() {
            return {
                code: '',
                definitionList: []
            }
        },
        computed: {
            cmOption() {
                if (this.value[0].length > 0) {
                    if (this.value[0][0].name.includes('.java')) {
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-java',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true
                        }
                    } else if (this.value[0][0].name.includes('.yml') || this.value[0][0].name.includes('.yaml')) {
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-yaml',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true
                        }
                    } else if (this.value[0][0].name.includes('.md')) {
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-markdown',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true
                        }
                    } else if (this.value[0][0].name.includes('.properties')) {
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-properties',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true
                        }
                    } else if (this.value[0][0].name.includes('mvnw')) {
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-sh',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true
                        }
                    } else if (this.value[0][0].name.includes('.xml')) {
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'application/xml',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true
                        }
                    }
                    return type

                }
            },
        },
        watch: {
            value(newVal) {
                // console.log()
                this.code = ''

                this.code = newVal[0][0].code


                // console.log(newVal)
                // let fileName = newVal[0].name[0];
                // let list=newVal[1].value;
                // this.definitionList=list
            }
        },
        methods: {

            onCmReady(cm) {
                // console.log('the editor is readied!', cm)

            },
            onCmFocus(cm) {
                // console.log('the editor is focus!', cm)
            },
            onCmCodeChange(newCode) {
                // console.log('this is new code', newCode)
                // this.code = newCode
            },
            setFormat(name) {
                var me = this

                if (name.includes('.java')) {

                    this.definitionList.some(function (definition) {
                        if (name.includes('ed.java')) {
                            me.setEventTemplate(name, definition)
                        } else if (name.includes('Controller.java')) {
                            me.setCommandTemplate(name, definition)
                        } else if (name.includes('Service.java')) {
                            me.setPolicyTemplate(name, definition);
                        } else if (name.includes('Controller.java')) {
                            // me.setViewTemplate(name,definition);
                        } else {
                            me.setAggregateTemplate(name, definition)
                        }

                    })
                } else {
                    me.setDefaultTemplate(name)
                }

            },
            // setEventTemplate(name,definition) {
            //         this.code = Mustache.render(
            //             "package hello;\n" +
            //             "\n" +
            //             "import javax.persistence.*;\n" +
            //             "import java.util.List;\n" +
            //             "\n" +
            //             "@Entity\n" +
            //             "@Table(name=\"{{inputText}}\")\n" +
            //             "public class {{inputText}} {\n" +
            //             "\n" +
            //             "    @Id\n" +
            //             "    @GeneratedValue\n" +
            //             "    private Long id;" +
            //             "    {{#entity}}\n" +
            //             "     public {{type}} {{name}};\n" +
            //             "    {{/entity}}\n" +
            //             "\n" +
            //             "    {{ #entity }}\n" +
            //             "      public {{type}} get{{name}}() {\n" +
            //             "           return {{name}};\n" +
            //             "       }\n" +
            //             "      public void set{{name}}(public {{name}}) {\n" +
            //             "           this.{{name}} = {{name}};\n" +
            //             "       }\n\n\n" +
            //             "    {{ /entity }}\n" +
            //             "\n\n\n\n" +
            //             "@PostPersist @PostUpdate\n" +
            //             "    private void publishStart() {\n" +
            //             "        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);\n" +
            //             "\n" +
            //             "        ObjectMapper objectMapper = new ObjectMapper();\n" +
            //             "        String json = null;\n" +
            //             "\n" +
            //             "        {{inputText}} {{inputText}} = new {{inputText}}();\n" +
            //             " {{ #entity }}\n" +
            //             "        {{name}}Changed.setProduct{{name}}(this.{{name}});\n" +
            //             " {{ /entity }}\n" +
            //             "        try {\n" +
            //             "            json = objectMapper.writeValueAsString(productChanged);\n" +
            //             "        } catch (JsonProcessingException e) {\n" +
            //             "            throw new RuntimeException(\"JSON format exception\", e);\n" +
            //             "        }\n" +
            //             "\n" +
            //             "        if( json != null ){\n" +
            //             "            Environment env = Application.applicationContext.getEnvironment();\n" +
            //             "            String topicName = env.getProperty(\"eventTopic\");\n" +
            //             "            ProducerRecord producerRecord = new ProducerRecord<>(topicName, json);\n" +
            //             "            kafkaTemplate.send(producerRecord);\n" +
            //             "        }\n" +
            //             "    }" +
            //             "}\n", definition)
            //
            // },

            // setViewTemplate(name,definition){
            //     this.code='THIS IS VIEW'
            // },

            // setPolicyTemplate(name,definition){
            //     this.code = Mustache.render("package com.example.template;\n" +
            //   "\n" +
            //   "import com.fasterxml.jackson.databind.DeserializationFeature;\n" +
            //   "import com.fasterxml.jackson.databind.ObjectMapper;\n" +
            //   "import org.apache.kafka.clients.consumer.ConsumerRecord;\n" +
            //   "import org.apache.kafka.clients.producer.ProducerRecord;\n" +
            //   "import org.springframework.beans.factory.annotation.Autowired;\n" +
            //   "import org.springframework.kafka.annotation.KafkaListener;\n" +
            //   "import org.springframework.kafka.core.KafkaTemplate;\n" +
            //   "import org.springframework.messaging.handler.annotation.Payload;\n" +
            //   "import org.springframework.stereotype.Service;\n" +
            //   "\n" +
            //   "import java.io.IOException;\n" +
            //   "import java.util.Optional;\n" +
            //   "\n" +
            //   "@Service\n" +
            //   "public class {{ connectAggregateName }}Service {\n" +
            //   "\n" +
            //   "    @Autowired\n" +
            //   "    private KafkaTemplate kafkaTemplate;\n" +
            //   "\n" +
            //   "    @Autowired\n" +
            //   "    private {{ connectAggregateName }}Repository {{ connectAggregateName }}Repository;\n" +
            //   "\n" +
            //   "    /**\n" +
            //   "     * 상품 변경이 발생할때마다, 상품정보를 저장해 놓음\n" +
            //   "     */\n" +
            //   "    @KafkaListener(topics = \"${eventTopic}\")\n" +
            //   "    public void {{ name }}(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {\n" +
            //   "        System.out.println(\"##### listener : \" + message);\n" +
            //   "\n" +
            //   "       \n" +
            //   "    }\n" +
            //   "}", definition)
            //
            //
            // },
            // setDefaultTemplate(name) {
            //     if ('.gitignore' == name) {
            //     } else if ('Dockerfile' == name) {
            //     } else if ('cloudbuild.yaml' == name) {
            //     } else if ('mvnw' == name) {
            //         this.code =
            //     } else if (name == 'mvnw.cmd') {
            //         this.code =
            //     } else if (name == 'pom.xml') {
            //         this.code =
            //     } else if ('README.md' == name) {
            //         this.code =
            //     } else if (name == 'maven-wrapper.properties') {
            //         this.code =
            //     } else if (name == 'application.yml') {
            //         this.code =
            //     }
            //
            // }
        },
        mounted() {
        }
    }
</script>
<style>
    .CodeMirror {
        height: auto;
    }
</style>
