<template>
    <v-container>
    <v-card>{{value[0].name[0]}}</v-card>
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
                    scroll: true
                },
                definitionList:[]
            }
        },
        watch: {
          value(newVal) {
              this.code = ''
              console.log(newVal)
              let fileName = newVal[0].name[0];
              let list=newVal[1].value;
              this.definitionList=list
              this.setFormat(fileName);
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
                console.log(newCode)
                // console.log('this is new code', newCode)
                // this.code = newCode
            },
            setFormat(name){
                var me = this

                if(name.includes('.java')){

                    console.log(name)
                    this.definitionList.some(function(definition){
                        console.log(name,definition)

                        if( name.includes('ed.java') ){
                            me.setEventTemplate(name,definition)
                        } else if(name.includes('Controller.java')){
                            me.setCommandTemplate(name,definition)
                        } else if(name.includes('Service.java')){
                            me.setPolicyTemplate(name,definition);
                        } else if(name.includes('Controller.java')) {
                            // me.setViewTemplate(name,definition);
                        }else{
                            me.setAggregateTemplate(name,definition)
                        }

                    })
                }else{
                    me.setDefaultTemplate(name)
                }

            },
            setEventTemplate(name,definition) {
                    this.code = Mustache.render(
                        "package hello;\n" +
                        "\n" +
                        "import javax.persistence.*;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "@Entity\n" +
                        "@Table(name=\"{{inputText}}\")\n" +
                        "public class {{inputText}} {\n" +
                        "\n" +
                        "    @Id\n" +
                        "    @GeneratedValue\n" +
                        "    private Long id;" +
                        "    {{#entity}}\n" +
                        "     public {{type}} {{name}};\n" +
                        "    {{/entity}}\n" +
                        "\n" +
                        "    {{ #entity }}\n" +
                        "      public {{type}} get{{name}}() {\n" +
                        "           return {{name}};\n" +
                        "       }\n" +
                        "      public void set{{name}}(public {{name}}) {\n" +
                        "           this.{{name}} = {{name}};\n" +
                        "       }\n\n\n" +
                        "    {{ /entity }}\n" +
                        "\n\n\n\n" +
                        "@PostPersist @PostUpdate\n" +
                        "    private void publishStart() {\n" +
                        "        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);\n" +
                        "\n" +
                        "        ObjectMapper objectMapper = new ObjectMapper();\n" +
                        "        String json = null;\n" +
                        "\n" +
                        "        {{inputText}} {{inputText}} = new {{inputText}}();\n" +
                        " {{ #entity }}\n" +
                        "        {{name}}Changed.setProduct{{name}}(this.{{name}});\n" +
                        " {{ /entity }}\n" +
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
                        "}\n", definition)

            },
            setAggregateTemplate(name,definition){

                if(name == definition.name+'Repository.java'){
                    this.code = Mustache.render(
                        "package com.example.template;\n " +
                        "import org.springframework.data.repository.PagingAndSortingRepository; \n " +
                        "public interface {{ name }}Repository extends PagingAndSortingRepository < {{ name }}, Long > { \n " +
                        "}\n", definition)
                }else{
                    this.code = Mustache.render(
                        "package com.example.template;\n" +
                        "\n" +
                        "import com.fasterxml.jackson.core.JsonProcessingException;\n" +
                        "import com.fasterxml.jackson.databind.ObjectMapper;\n" +
                        "import org.apache.kafka.clients.producer.ProducerRecord;\n" +
                        "import org.springframework.core.env.Environment;\n" +
                        "import org.springframework.kafka.core.KafkaTemplate;\n" +
                        "\n" +
                        "import javax.persistence.*;\n" +
                        "\n" +
                        "@Entity\n" +
                        "public class {{name}} {\n" +
                        "\n" +
                        "    @Id\n" +
                        "    @GeneratedValue\n" +
                        "    private Long id;\n\n" +
                        "{{#aggregateEntity}}" +
                        "    {{type}} {{name}};\n" +
                        "{{/aggregateEntity}}" +
                        "\n" +
                        " {{#aggregateEntity}} " +
                        "    public {{type}} getId() {\n" +
                        "        return {{name}};\n" +
                        "    }\n" +
                        "\n" +
                        "    public void setId(Long {{name}}) {\n" +
                        "        this.{{name}} = {{name}};\n" +
                        "    }\n" +
                        "{{/aggregateEntity}}" +
                        "\n" +
                        "}", definition)
                }


            },
            setViewTemplate(name,definition){
                this.code='THIS IS VIEW'
            },
            setCommandTemplate(name,definition){

                if(name = definition.name+'Controller.java'){
                    this.code = Mustache.render(
                        "package com.example.template;\n" +
                        "\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.web.bind.annotation.PathVariable;\n" +
                        "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                        "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                        "import org.springframework.web.bind.annotation.RestController;\n" +
                        "\n" +
                        "import javax.servlet.http.HttpServletRequest;\n" +
                        "import javax.servlet.http.HttpServletResponse;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "@RestController\n" +
                        "public class {{ name }}Controller {\n" +
                        "\n" +
                        "}", definition)
                }

            },
            setPolicyTemplate(name,definition){
                this.code = Mustache.render("package com.example.template;\n" +
              "\n" +
              "import com.fasterxml.jackson.databind.DeserializationFeature;\n" +
              "import com.fasterxml.jackson.databind.ObjectMapper;\n" +
              "import org.apache.kafka.clients.consumer.ConsumerRecord;\n" +
              "import org.apache.kafka.clients.producer.ProducerRecord;\n" +
              "import org.springframework.beans.factory.annotation.Autowired;\n" +
              "import org.springframework.kafka.annotation.KafkaListener;\n" +
              "import org.springframework.kafka.core.KafkaTemplate;\n" +
              "import org.springframework.messaging.handler.annotation.Payload;\n" +
              "import org.springframework.stereotype.Service;\n" +
              "\n" +
              "import java.io.IOException;\n" +
              "import java.util.Optional;\n" +
              "\n" +
              "@Service\n" +
              "public class {{ connectAggregateName }}Service {\n" +
              "\n" +
              "    @Autowired\n" +
              "    private KafkaTemplate kafkaTemplate;\n" +
              "\n" +
              "    @Autowired\n" +
              "    private {{ connectAggregateName }}Repository {{ connectAggregateName }}Repository;\n" +
              "\n" +
              "    /**\n" +
              "     * 상품 변경이 발생할때마다, 상품정보를 저장해 놓음\n" +
              "     */\n" +
              "    @KafkaListener(topics = \"${eventTopic}\")\n" +
              "    public void {{ name }}(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {\n" +
              "        System.out.println(\"##### listener : \" + message);\n" +
              "\n" +
              "       \n" +
              "    }\n" +
              "}", definition)


            },
            setDefaultTemplate(name){
             if('.gitignore'== name){
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
                    this.code="#!/bin/sh\n" +
                        "# ----------------------------------------------------------------------------\n" +
                        "# Licensed to the Apache Software Foundation (ASF) under one\n" +
                        "# or more contributor license agreements.  See the NOTICE file\n" +
                        "# distributed with this work for additional information\n" +
                        "# regarding copyright ownership.  The ASF licenses this file\n" +
                        "# to you under the Apache License, Version 2.0 (the\n" +
                        "# \"License\"); you may not use this file except in compliance\n" +
                        "# with the License.  You may obtain a copy of the License at\n" +
                        "#\n" +
                        "#    http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "#\n" +
                        "# Unless required by applicable law or agreed to in writing,\n" +
                        "# software distributed under the License is distributed on an\n" +
                        "# \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n" +
                        "# KIND, either express or implied.  See the License for the\n" +
                        "# specific language governing permissions and limitations\n" +
                        "# under the License.\n" +
                        "# ----------------------------------------------------------------------------\n" +
                        "\n" +
                        "# ----------------------------------------------------------------------------\n" +
                        "# Maven2 Start Up Batch script\n" +
                        "#\n" +
                        "# Required ENV vars:\n" +
                        "# ------------------\n" +
                        "#   JAVA_HOME - location of a JDK home dir\n" +
                        "#\n" +
                        "# Optional ENV vars\n" +
                        "# -----------------\n" +
                        "#   M2_HOME - location of maven2's installed home dir\n" +
                        "#   MAVEN_OPTS - parameters passed to the Java VM when running Maven\n" +
                        "#     e.g. to debug Maven itself, use\n" +
                        "#       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000\n" +
                        "#   MAVEN_SKIP_RC - flag to disable loading of mavenrc files\n" +
                        "# ----------------------------------------------------------------------------\n" +
                        "\n" +
                        "if [ -z \"$MAVEN_SKIP_RC\" ] ; then\n" +
                        "\n" +
                        "  if [ -f /etc/mavenrc ] ; then\n" +
                        "    . /etc/mavenrc\n" +
                        "  fi\n" +
                        "\n" +
                        "  if [ -f \"$HOME/.mavenrc\" ] ; then\n" +
                        "    . \"$HOME/.mavenrc\"\n" +
                        "  fi\n" +
                        "\n" +
                        "fi\n" +
                        "\n" +
                        "# OS specific support.  $var _must_ be set to either true or false.\n" +
                        "cygwin=false;\n" +
                        "darwin=false;\n" +
                        "mingw=false\n" +
                        "case \"`uname`\" in\n" +
                        "  CYGWIN*) cygwin=true ;;\n" +
                        "  MINGW*) mingw=true;;\n" +
                        "  Darwin*) darwin=true\n" +
                        "    # Use /usr/libexec/java_home if available, otherwise fall back to /Library/Java/Home\n" +
                        "    # See https://developer.apple.com/library/mac/qa/qa1170/_index.html\n" +
                        "    if [ -z \"$JAVA_HOME\" ]; then\n" +
                        "      if [ -x \"/usr/libexec/java_home\" ]; then\n" +
                        "        export JAVA_HOME=\"`/usr/libexec/java_home`\"\n" +
                        "      else\n" +
                        "        export JAVA_HOME=\"/Library/Java/Home\"\n" +
                        "      fi\n" +
                        "    fi\n" +
                        "    ;;\n" +
                        "esac\n" +
                        "\n" +
                        "if [ -z \"$JAVA_HOME\" ] ; then\n" +
                        "  if [ -r /etc/gentoo-release ] ; then\n" +
                        "    JAVA_HOME=`java-config --jre-home`\n" +
                        "  fi\n" +
                        "fi\n" +
                        "\n" +
                        "if [ -z \"$M2_HOME\" ] ; then\n" +
                        "  ## resolve links - $0 may be a link to maven's home\n" +
                        "  PRG=\"$0\"\n" +
                        "\n" +
                        "  # need this for relative symlinks\n" +
                        "  while [ -h \"$PRG\" ] ; do\n" +
                        "    ls=`ls -ld \"$PRG\"`\n" +
                        "    link=`expr \"$ls\" : '.*-> \\(.*\\)$'`\n" +
                        "    if expr \"$link\" : '/.*' > /dev/null; then\n" +
                        "      PRG=\"$link\"\n" +
                        "    else\n" +
                        "      PRG=\"`dirname \"$PRG\"`/$link\"\n" +
                        "    fi\n" +
                        "  done\n" +
                        "\n" +
                        "  saveddir=`pwd`\n" +
                        "\n" +
                        "  M2_HOME=`dirname \"$PRG\"`/..\n" +
                        "\n" +
                        "  # make it fully qualified\n" +
                        "  M2_HOME=`cd \"$M2_HOME\" && pwd`\n" +
                        "\n" +
                        "  cd \"$saveddir\"\n" +
                        "  # echo Using m2 at $M2_HOME\n" +
                        "fi\n" +
                        "\n" +
                        "# For Cygwin, ensure paths are in UNIX format before anything is touched\n" +
                        "if $cygwin ; then\n" +
                        "  [ -n \"$M2_HOME\" ] &&\n" +
                        "    M2_HOME=`cygpath --unix \"$M2_HOME\"`\n" +
                        "  [ -n \"$JAVA_HOME\" ] &&\n" +
                        "    JAVA_HOME=`cygpath --unix \"$JAVA_HOME\"`\n" +
                        "  [ -n \"$CLASSPATH\" ] &&\n" +
                        "    CLASSPATH=`cygpath --path --unix \"$CLASSPATH\"`\n" +
                        "fi\n" +
                        "\n" +
                        "# For Mingw, ensure paths are in UNIX format before anything is touched\n" +
                        "if $mingw ; then\n" +
                        "  [ -n \"$M2_HOME\" ] &&\n" +
                        "    M2_HOME=\"`(cd \"$M2_HOME\"; pwd)`\"\n" +
                        "  [ -n \"$JAVA_HOME\" ] &&\n" +
                        "    JAVA_HOME=\"`(cd \"$JAVA_HOME\"; pwd)`\"\n" +
                        "  # TODO classpath?\n" +
                        "fi\n" +
                        "\n" +
                        "if [ -z \"$JAVA_HOME\" ]; then\n" +
                        "  javaExecutable=\"`which javac`\"\n" +
                        "  if [ -n \"$javaExecutable\" ] && ! [ \"`expr \\"+
                 "$javaExecutable\\ '\\([^ ]*\\)\'\" = \"no\" ]; then\n" +
                 "    # readlink(1) is not available as standard on Solaris 10.\n" +
                 "    readLink= \`which readlink\`\n" +
                 "    if [ ! \`expr \"$readLink\" : '\\([^ ]*\\)'\` = \"no\" ]; then\n" +
                 "      if $darwin ; then\n" +
                 "        javaHome=\"\`dirname $javaExecutable\\\`"+
                 "        javaExecutable=\"\`cd $javaHome\\ && pwd -P \`/javac\"\n" +
                 "      else\n" +
                 "        javaExecutable=\"\`readlink -f $javaExecutable \`\"\n" +
                 "      fi\n" +
                 "      javaHome=\"\`dirname $javaExecutable\\ \`\"\n" +
                 "      javaHome=\`expr \"$javaHome\" : '\\(.*\\)/bin'\`\n" +
                 "      JAVA_HOME=\"$javaHome\"\n" +
                 "      export JAVA_HOME\n" +
                 "    fi\n" +
                 "  fi\n" +
                 "fi\n" +
                 "\n" +
                 "if [ -z \"$JAVACMD\" ] ; then\n" +
                 "  if [ -n \"$JAVA_HOME\"  ] ; then\n" +
                 "    if [ -x \"$JAVA_HOME/jre/sh/java\" ] ; then\n" +
                 "      # IBM's JDK on AIX uses strange locations for the executables\n" +
                 "      JAVACMD=\"$JAVA_HOME/jre/sh/java\"\n" +
                 "    else\n" +
                 "      JAVACMD=\"$JAVA_HOME/bin/java\"\n" +
                 "    fi\n" +
                 "  else\n" +
                 "    JAVACMD=\"\`which java\`\"\n" +
                 "  fi\n" +
                 "fi\n" +
                 "\n" +
                 "if [ ! -x \"$JAVACMD\" ] ; then\n" +
                 "  echo \"Error: JAVA_HOME is not defined correctly.\" >&2\n" +
                 "  echo \"  We cannot execute $JAVACMD\" >&2\n" +
                 "  exit 1\n" +
                 "fi\n" +
                 "\n" +
                 "if [ -z \"$JAVA_HOME\" ] ; then\n" +
                 "  echo \"Warning: JAVA_HOME environment variable is not set.\"\n" +
                 "fi\n" +
                 "\n" +
                 "CLASSWORLDS_LAUNCHER=org.codehaus.plexus.classworlds.launcher.Launcher\n" +
                 "\n" +
                 "# traverses directory structure from process work directory to filesystem root\n" +
                 "# first directory with .mvn subdirectory is considered project base directory\n" +
                 "find_maven_basedir() {\n" +
                 "\n" +
                 "  if [ -z \"$1\" ]\n" +
                 "  then\n" +
                 "    echo \"Path not specified to find_maven_basedir\"\n" +
                 "    return 1\n" +
                 "  fi\n" +
                 "\n" +
                 "  basedir=\"$1\"\n" +
                 "  wdir=\"$1\"\n" +
                 "  while [ \"$wdir\" != '/' ] ; do\n" +
                 "    if [ -d \"$wdir\"/.mvn ] ; then\n" +
                 "      basedir=$wdir\n" +
                 "      break\n" +
                 "    fi\n" +
                 "    # workaround for JBEAP-8937 (on Solaris 10/Sparc)\n" +
                 "    if [ -d \"\${wdir}\" ]; then\n" +
                 "      wdir=\`cd \"$wdir/..\"; pwd\`\n" +
                 "    fi\n" +
                 "    # end of workaround\n" +
                 "  done\n" +
                 "  echo \"\${basedir}\"\n" +
                 "}\n" +
                 "\n" +
                 "# concatenates all lines of a file\n" +
                 "concat_lines() {\n" +
                 "  if [ -f \"$1\" ]; then\n" +
                 "    echo \"$(tr -s '\\n' ' ' < \"$1\")\"\n" +
                 "  fi\n" +
                 "}\n" +
                 "\n" +
                 "BASE_DIR=\`find_maven_basedir \"$(pwd)\"\`\n" +
                 "if [ -z \"$BASE_DIR\" ]; then\n" +
                 "  exit 1;\n" +
                 "fi\n" +
                 "\n" +
                 "##########################################################################################\n" +
                 "# Extension to allow automatically downloading the maven-wrapper.jar from Maven-central\n" +
                 "# This allows using the maven wrapper in projects that prohibit checking in binary data.\n" +
                 "##########################################################################################\n" +
                 "if [ -r \"$BASE_DIR/.mvn/wrapper/maven-wrapper.jar\" ]; then\n" +
                 "    if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                 "      echo \"Found .mvn/wrapper/maven-wrapper.jar\"\n" +
                 "    fi\n" +
                 "else\n" +
                 "    if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                 "      echo \"Couldn't find .mvn/wrapper/maven-wrapper.jar, downloading it ...\"\n" +
                 "    fi\n" +
                 "    jarUrl=\"https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.2/maven-wrapper-0.4.2.jar\"\n" +
                 "    while IFS=\"=\" read key value; do\n" +
                 "      case \"$key\" in (wrapperUrl) jarUrl=\"$value\"; break ;;\n" +
                 "      esac\n" +
                 "    done < \"$BASE_DIR/.mvn/wrapper/maven-wrapper.properties\"\n" +
                 "    if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                 "      echo \"Downloading from: $jarUrl\"\n" +
                 "    fi\n" +
                 "    wrapperJarPath=\"$BASE_DIR/.mvn/wrapper/maven-wrapper.jar\"\n" +
                 "\n" +
                 "    if command -v wget > /dev/null; then\n" +
                 "        if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                 "          echo \"Found wget ... using wget\"\n" +
                 "        fi\n" +
                 "        wget \"$jarUrl\" -O \"$wrapperJarPath\"\n" +
                 "    elif command -v curl > /dev/null; then\n" +
                 "        if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                 "          echo \"Found curl ... using curl\"\n" +
                 "        fi\n" +
                 "        curl -o \"$wrapperJarPath\" \"$jarUrl\"\n" +
                 "    else\n" +
                 "        if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                 "          echo \"Falling back to using Java to download\"\n" +
                 "        fi\n" +
                 "        javaClass=\"$BASE_DIR/.mvn/wrapper/MavenWrapperDownloader.java\"\n" +
                 "        if [ -e \"$javaClass\" ]; then\n" +
                 "            if [ ! -e \"$BASE_DIR/.mvn/wrapper/MavenWrapperDownloader.class\" ]; then\n" +
                 "                if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                 "                  echo \" - Compiling MavenWrapperDownloader.java ...\"\n" +
                 "                fi\n" +
                 "                # Compiling the Java class\n" +
                 "                (\"$JAVA_HOME/bin/javac\" \"$javaClass\")\n" +
                 "            fi\n" +
                 "            if [ -e \"$BASE_DIR/.mvn/wrapper/MavenWrapperDownloader.class\" ]; then\n" +
                 "                # Running the downloader\n" +
                 "                if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                 "                  echo \" - Running MavenWrapperDownloader.java ...\"\n" +
                 "                fi\n" +
                 "                (\"$JAVA_HOME/bin/java\" -cp .mvn/wrapper MavenWrapperDownloader \"$MAVEN_PROJECTBASEDIR\")\n" +
                 "            fi\n" +
                 "        fi\n" +
                 "    fi\n" +
                 "fi\n" +
                 "##########################################################################################\n" +
                 "# End of extension\n" +
                 "##########################################################################################\n" +
                 "\n" +
                 "export MAVEN_PROJECTBASEDIR=\${MAVEN_BASEDIR:-\"$BASE_DIR\"}\n" +
                 "if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                 "  echo $MAVEN_PROJECTBASEDIR\n" +
                 "fi\n" +
                 "MAVEN_OPTS=\"$(concat_lines \"$MAVEN_PROJECTBASEDIR/.mvn/jvm.config\") $MAVEN_OPTS\"\n" +
                 "\n" +
                 "# For Cygwin, switch paths to Windows format before running java\n" +
                 "if $cygwin; then\n" +
                 "  [ -n \"$M2_HOME\" ] &&\n" +
                 "    M2_HOME=\`cygpath --path --windows \"$M2_HOME\"\`\n" +
                 "  [ -n \"$JAVA_HOME\" ] &&\n" +
                 "    JAVA_HOME=\`cygpath --path --windows \"$JAVA_HOME\"\`\n" +
                 "  [ -n \"$CLASSPATH\" ] &&\n" +
                 "    CLASSPATH=\`cygpath --path --windows \"$CLASSPATH\"\`\n" +
                 "  [ -n \"$MAVEN_PROJECTBASEDIR\" ] &&\n" +
                 "    MAVEN_PROJECTBASEDIR=\`cygpath --path --windows \"$MAVEN_PROJECTBASEDIR\"\`\n" +
                 "fi\n" +
                 "\n" +
                 "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain\n" +
                 "\n" +
                 "exec \"$JAVACMD\" \\\n" +
                 "  $MAVEN_OPTS \\\n" +
                 "  -classpath \"$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar\" \\\n" +
                 "  \"-Dmaven.home=\${M2_HOME}\" \"-Dmaven.multiModuleProjectDirectory=\${MAVEN_PROJECTBASEDIR}\" \\\n" +
                 "  \${WRAPPER_LAUNCHER} $MAVEN_CONFIG \"$@\""
                } else if(name == 'mvnw.cmd'){
                        this.code='mvnw.cmd'
                }else if(name == 'pom.xml'){
                    this.code='pom.xml'
                } else if('README.md' == name){
                 this.code="# event-ui\n" +
                     "\n" +
                     "## Project setup\n" +
                     "npm install\n" +
                     "\n" +
                     "### Compiles and hot-reloads for development\n" +
                     "npm run serve\n" +
                     "\n" +
                     "### Compiles and minifies for production\n" +
                     "npm run build\n" +
                     "\n" +
                     "### Run your tests\n" +
                     "npm run test\n" +
                     "\n" +
                     "### Lints and fixes files\n" +
                     "npm run lint\n" +
                     "\n" +
                     "### Customize configuration\n" +
                     "See [Configuration Reference](https://cli.vuejs.org/config/)."
                } else if ( name == 'maven-wrapper.properties' ){
                  this.code="distributionUrl=https://repo1.maven.org/maven2/org/apache/maven/apache-maven/3.3.9/apache-maven-3.3.9-bin.zip"
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
