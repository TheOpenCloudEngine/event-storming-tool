<template>
    <v-container>
    <v-card>Title</v-card>
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
    </v-container>
</template>
<script>
    import 'codemirror/mode/clike/clike'
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/darcula.css'
    export default {
        props:{
            value: Array
        },
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
                format :{},
            }
        },
        watch: {
          value(newVal) {
              this.code = ''
              let fileName = newVal[0];
              this.setFormat(fileName)
          }
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
                // this.code = newCode
            },
            setFormat(name){
                var setformat=[
                    {
                        type:"Aggregate",
                        header: "Products",
                        variable:[
                            { attribute:"private", type : "Long", name:"id" , upName:"Id" },
                            { attribute:"public", type : "String", name:"stock" ,upName:"Stock"},
                            { attribute:"public", type : "int", name:"price",upName:"Price"}
                        ]
                    },
                    {
                        type:"Aggregate",
                        header: "Products",
                        variable:[
                            { attribute:"private", type : "Long", name:"id" , upName:"Id" },
                            { attribute:"public", type : "String", name:"stock" ,upName:"Stock"},
                            { attribute:"public", type : "int", name:"price",upName:"Price"}
                        ]
                    }
                ]
                this.format=setformat;

                this.setTemplate(name)

            },
            setTemplate(name){
                if(name=='123'){
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
                        "}\n", this.format)
                }else if('.gitignore'== name){
                    this.code="/target/\n" +
                        "/bin/\n" +
                        "/.settings/\n" +
                        "*#\n" +
                        "*.iml\n" +
                        "*.ipr\n" +
                        "*.iws\n" +
                        "*.jar\n" +
                        "*.sw?\n" +
                        "*~\n" +
                        ".#*\n" +
                        ".*.md.html\n" +
                        ".DS_Store\n" +
                        ".classpath\n" +
                        ".factorypath\n" +
                        ".gradle\n" +
                        ".idea\n" +
                        ".metadata\n" +
                        ".project\n" +
                        ".recommenders\n" +
                        ".settings\n" +
                        ".springBeans\n" +
                        "/build\n" +
                        "/code\n" +
                        "MANIFEST.MF\n" +
                        "_site/\n" +
                        "activemq-data\n" +
                        "bin\n" +
                        "build\n" +
                        "build.log\n" +
                        "dependency-reduced-pom.xml\n" +
                        "dump.rdb\n" +
                        "interpolated*.xml\n" +
                        "lib/\n" +
                        "manifest.yml\n" +
                        "overridedb.*\n" +
                        "settings.xml\n" +
                        "target\n" +
                        "transaction-logs\n" +
                        ".flattened-pom.xml\n" +
                        "secrets.yml\n" +
                        ".gradletasknamecache\n" +
                        ".sts4-cache\n" +
                        "node_modules\n" +
                        ".dist/"
                }else if('Dockerfile' == name){
                   this.code = "FROM openjdk:8u212-jdk-alpine\n" +
                       "COPY target/*SNAPSHOT.jar app.jar\n" +
                       "EXPOSE 8080\n" +
                       "ENTRYPOINT [\"java\",\"-Xmx400M\",\"-Djava.security.egd=file:/dev/./urandom\",\"-jar\",\"/app.jar\",\"--spring.profiles.active=docker\"]"
                }else if ('cloudbuild.yaml'== name){
                    this.code="steps:\n" +
                        "  ### Test\n" +
                        "#  - id: 'test'\n" +
                        "#    name: 'gcr.io/cloud-builders/mvn'\n" +
                        "#    args: [\n" +
                        "#      'test',\n" +
                        "#      '-Dspring.profiles.active=test'\n" +
                        "#    ]\n" +
                        "  ### Build\n" +
                        "  - id: 'build'\n" +
                        "    name: 'gcr.io/cloud-builders/mvn'\n" +
                        "    args: [\n" +
                        "      'clean',\n" +
                        "      'package',\n" +
                        "      '-Dmaven.test.skip=true'\n" +
                        "    ]\n" +
                        "#    waitFor: ['test']\n" +
                        "  ### docker Build\n" +
                        "  - id: 'docker build'\n" +
                        "    name: 'gcr.io/cloud-builders/docker'\n" +
                        "    args:\n" +
                        "      - 'build'\n" +
                        "      - '--tag=gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA'\n" +
                        "      - '.'\n" +
                        "  ### Publish\n" +
                        "  - id: 'publish'\n" +
                        "    name: 'gcr.io/cloud-builders/docker'\n" +
                        "    entrypoint: 'bash'\n" +
                        "    args:\n" +
                        "      - '-c'\n" +
                        "      - |\n" +
                        "        docker push gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA\n" +
                        "  ### deploy\n" +
                        "  - id: 'deploy'\n" +
                        "    name: 'gcr.io/cloud-builders/gcloud'\n" +
                        "    entrypoint: 'bash'\n" +
                        "    args:\n" +
                        "      - '-c'\n" +
                        "      - |\n" +
                        "        PROJECT=$$(gcloud config get-value core/project)\n" +
                        "        gcloud container clusters get-credentials \"$${CLOUDSDK_CONTAINER_CLUSTER}\" \\\n" +
                        "          --project \"$${PROJECT}\" \\\n" +
                        "          --zone \"$${CLOUDSDK_COMPUTE_ZONE}\"\n" +
                        "        cat <<EOF | kubectl apply -f -\n" +
                        "        apiVersion: v1\n" +
                        "        kind: Service\n" +
                        "        metadata:\n" +
                        "          name: $_PROJECT_NAME\n" +
                        "          labels:\n" +
                        "            app: $_PROJECT_NAME\n" +
                        "        spec:\n" +
                        "          ports:\n" +
                        "            - port: 8080\n" +
                        "              targetPort: 8080\n" +
                        "          selector:\n" +
                        "            app: $_PROJECT_NAME\n" +
                        "        EOF\n" +
                        "        cat <<EOF | kubectl apply -f -\n" +
                        "        apiVersion: apps/v1\n" +
                        "        kind: Deployment\n" +
                        "        metadata:\n" +
                        "          name: $_PROJECT_NAME\n" +
                        "          labels:\n" +
                        "            app: $_PROJECT_NAME\n" +
                        "        spec:\n" +
                        "          replicas: 1\n" +
                        "          selector:\n" +
                        "            matchLabels:\n" +
                        "              app: $_PROJECT_NAME\n" +
                        "          template:\n" +
                        "            metadata:\n" +
                        "              labels:\n" +
                        "                app: $_PROJECT_NAME\n" +
                        "            spec:\n" +
                        "              containers:\n" +
                        "                - name: $_PROJECT_NAME\n" +
                        "                  image: gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA\n" +
                        "                  ports:\n" +
                        "                    - containerPort: 8080\n" +
                        "        EOF\n" +
                        "options:\n" +
                        "  env:\n" +
                        "    #    # location/name of GKE cluster (used by all kubectl commands)\n" +
                        "    - CLOUDSDK_COMPUTE_ZONE=asia-northeast1-a\n" +
                        "    - CLOUDSDK_CONTAINER_CLUSTER=standard-cluster-1"
                }else if('mvnw' == name){
                    this.code="mvnw"
                }else if('README.md' == name){

                }

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
