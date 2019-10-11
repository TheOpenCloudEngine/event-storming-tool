<template>
    <v-container>
    <!--<v-card>{{value[0].name[0]}}</v-card>-->
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
              // console.log()
              this.code = ''
                if(newVal[0][0].code) {
                    this.code = newVal[0][0].code
                } else {
                    this.setFormat(newVal[0][0].name);
                }


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
            setFormat(name){
                var me = this

                if(name.includes('.java')){

                    this.definitionList.some(function(definition){
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
                    this.code="@REM ----------------------------------------------------------------------------\n" +
                        "@REM Licensed to the Apache Software Foundation (ASF) under one\n" +
                        "@REM or more contributor license agreements.  See the NOTICE file\n" +
                        "@REM distributed with this work for additional information\n" +
                        "@REM regarding copyright ownership.  The ASF licenses this file\n" +
                        "@REM to you under the Apache License, Version 2.0 (the\n" +
                        "@REM \"License\"); you may not use this file except in compliance\n" +
                        "@REM with the License.  You may obtain a copy of the License at\n" +
                        "@REM\n" +
                        "@REM    http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "@REM\n" +
                        "@REM Unless required by applicable law or agreed to in writing,\n" +
                        "@REM software distributed under the License is distributed on an\n" +
                        "@REM \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n" +
                        "@REM KIND, either express or implied.  See the License for the\n" +
                        "@REM specific language governing permissions and limitations\n" +
                        "@REM under the License.\n" +
                        "@REM ----------------------------------------------------------------------------\n" +
                        "\n" +
                        "@REM ----------------------------------------------------------------------------\n" +
                        "@REM Maven2 Start Up Batch script\n" +
                        "@REM\n" +
                        "@REM Required ENV vars:\n" +
                        "@REM JAVA_HOME - location of a JDK home dir\n" +
                        "@REM\n" +
                        "@REM Optional ENV vars\n" +
                        "@REM M2_HOME - location of maven2's installed home dir\n" +
                        "@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands\n" +
                        "@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending\n" +
                        "@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven\n" +
                        "@REM     e.g. to debug Maven itself, use\n" +
                        "@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000\n" +
                        "@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files\n" +
                        "@REM ----------------------------------------------------------------------------\n" +
                        "\n" +
                        "@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'\n" +
                        "@echo off\n" +
                        "@REM set title of command window\n" +
                        "title %0\n" +
                        "@REM enable echoing my setting MAVEN_BATCH_ECHO to 'on'\n" +
                        "@if \"%MAVEN_BATCH_ECHO%\" == \"on\"  echo %MAVEN_BATCH_ECHO%\n" +
                        "\n" +
                        "@REM set %HOME% to equivalent of $HOME\n" +
                        "if \"%HOME%\" == \"\" (set \"HOME=%HOMEDRIVE%%HOMEPATH%\")\n" +
                        "\n" +
                        "@REM Execute a user defined script before this one\n" +
                        "if not \"%MAVEN_SKIP_RC%\" == \"\" goto skipRcPre\n" +
                        "@REM check for pre script, once with legacy .bat ending and once with .cmd ending\n" +
                        "if exist \"%HOME%\\mavenrc_pre.bat\" call \"%HOME%\\mavenrc_pre.bat\"\n" +
                        "if exist \"%HOME%\\mavenrc_pre.cmd\" call \"%HOME%\\mavenrc_pre.cmd\"\n" +
                        ":skipRcPre\n" +
                        "\n" +
                        "@setlocal\n" +
                        "\n" +
                        "set ERROR_CODE=0\n" +
                        "\n" +
                        "@REM To isolate internal variables from possible post scripts, we use another setlocal\n" +
                        "@setlocal\n" +
                        "\n" +
                        "@REM ==== START VALIDATION ====\n" +
                        "if not \"%JAVA_HOME%\" == \"\" goto OkJHome\n" +
                        "\n" +
                        "echo.\n" +
                        "echo Error: JAVA_HOME not found in your environment. >&2\n" +
                        "echo Please set the JAVA_HOME variable in your environment to match the >&2\n" +
                        "echo location of your Java installation. >&2\n" +
                        "echo.\n" +
                        "goto error\n" +
                        "\n" +
                        ":OkJHome\n" +
                        "if exist \"%JAVA_HOME%\\bin\\java.exe\" goto init\n" +
                        "\n" +
                        "echo.\n" +
                        "echo Error: JAVA_HOME is set to an invalid directory. >&2\n" +
                        "echo JAVA_HOME = \"%JAVA_HOME%\" >&2\n" +
                        "echo Please set the JAVA_HOME variable in your environment to match the >&2\n" +
                        "echo location of your Java installation. >&2\n" +
                        "echo.\n" +
                        "goto error\n" +
                        "\n" +
                        "@REM ==== END VALIDATION ====\n" +
                        "\n" +
                        ":init\n" +
                        "\n" +
                        "@REM Find the project base dir, i.e. the directory that contains the folder \".mvn\".\n" +
                        "@REM Fallback to current working directory if not found.\n" +
                        "\n" +
                        "set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%\n" +
                        "IF NOT \"%MAVEN_PROJECTBASEDIR%\"==\"\" goto endDetectBaseDir\n" +
                        "\n" +
                        "set EXEC_DIR=%CD%\n" +
                        "set WDIR=%EXEC_DIR%\n" +
                        ":findBaseDir\n" +
                        "IF EXIST \"%WDIR%\"\\.mvn goto baseDirFound\n" +
                        "cd ..\n" +
                        "IF \"%WDIR%\"==\"%CD%\" goto baseDirNotFound\n" +
                        "set WDIR=%CD%\n" +
                        "goto findBaseDir\n" +
                        "\n" +
                        ":baseDirFound\n" +
                        "set MAVEN_PROJECTBASEDIR=%WDIR%\n" +
                        "cd \"%EXEC_DIR%\"\n" +
                        "goto endDetectBaseDir\n" +
                        "\n" +
                        ":baseDirNotFound\n" +
                        "set MAVEN_PROJECTBASEDIR=%EXEC_DIR%\n" +
                        "cd \"%EXEC_DIR%\"\n" +
                        "\n" +
                        ":endDetectBaseDir\n" +
                        "\n" +
                        "IF NOT EXIST \"%MAVEN_PROJECTBASEDIR%\\.mvn\\jvm.config\" goto endReadAdditionalConfig\n" +
                        "\n" +
                        "@setlocal EnableExtensions EnableDelayedExpansion\n" +
                        "for /F \"usebackq delims=\" %%a in (\"%MAVEN_PROJECTBASEDIR%\\.mvn\\jvm.config\") do set JVM_CONFIG_MAVEN_PROPS=!JVM_CONFIG_MAVEN_PROPS! %%a\n" +
                        "@endlocal & set JVM_CONFIG_MAVEN_PROPS=%JVM_CONFIG_MAVEN_PROPS%\n" +
                        "\n" +
                        ":endReadAdditionalConfig\n" +
                        "\n" +
                        "SET MAVEN_JAVA_EXE=\"%JAVA_HOME%\\bin\\java.exe\"\n" +
                        "set WRAPPER_JAR=\"%MAVEN_PROJECTBASEDIR%\\.mvn\\wrapper\\maven-wrapper.jar\"\n" +
                        "set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain\n" +
                        "\n" +
                        "set DOWNLOAD_URL=\"https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.2/maven-wrapper-0.4.2.jar\"\n" +
                        "FOR /F \"tokens=1,2 delims==\" %%A IN (%MAVEN_PROJECTBASEDIR%\\.mvn\\wrapper\\maven-wrapper.properties) DO (\n" +
                        "\tIF \"%%A\"==\"wrapperUrl\" SET DOWNLOAD_URL=%%B \n" +
                        ")\n" +
                        "\n" +
                        "@REM Extension to allow automatically downloading the maven-wrapper.jar from Maven-central\n" +
                        "@REM This allows using the maven wrapper in projects that prohibit checking in binary data.\n" +
                        "if exist %WRAPPER_JAR% (\n" +
                        "    echo Found %WRAPPER_JAR%\n" +
                        ") else (\n" +
                        "    echo Couldn't find %WRAPPER_JAR%, downloading it ...\n" +
                        "\techo Downloading from: %DOWNLOAD_URL%\n" +
                        "    powershell -Command \"(New-Object Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')\"\n" +
                        "    echo Finished downloading %WRAPPER_JAR%\n" +
                        ")\n" +
                        "@REM End of extension\n" +
                        "\n" +
                        "%MAVEN_JAVA_EXE% %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath %WRAPPER_JAR% \"-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%\" %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*\n" +
                        "if ERRORLEVEL 1 goto error\n" +
                        "goto end\n" +
                        "\n" +
                        ":error\n" +
                        "set ERROR_CODE=1\n" +
                        "\n" +
                        ":end\n" +
                        "@endlocal & set ERROR_CODE=%ERROR_CODE%\n" +
                        "\n" +
                        "if not \"%MAVEN_SKIP_RC%\" == \"\" goto skipRcPost\n" +
                        "@REM check for post script, once with legacy .bat ending and once with .cmd ending\n" +
                        "if exist \"%HOME%\\mavenrc_post.bat\" call \"%HOME%\\mavenrc_post.bat\"\n" +
                        "if exist \"%HOME%\\mavenrc_post.cmd\" call \"%HOME%\\mavenrc_post.cmd\"\n" +
                        ":skipRcPost\n" +
                        "\n" +
                        "@REM pause the script if MAVEN_BATCH_PAUSE is set to 'on'\n" +
                        "if \"%MAVEN_BATCH_PAUSE%\" == \"on\" pause\n" +
                        "\n" +
                        "if \"%MAVEN_TERMINATE_CMD%\" == \"on\" exit %ERROR_CODE%\n" +
                        "\n" +
                        "exit /B %ERROR_CODE%"
                }else if(name == 'pom.xml'){
                    this.code="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "\txsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "\t<modelVersion>4.0.0</modelVersion>\n" +
                        "\t<parent>\n" +
                        "\t\t<groupId>org.springframework.boot</groupId>\n" +
                        "\t\t<artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "\t\t<version>2.1.1.RELEASE</version>\n" +
                        "\t\t<relativePath/> <!-- lookup parent from repository -->\n" +
                        "\t</parent>\n" +
                        "\t<groupId>com.example</groupId>\n" +
                        "\t<artifactId>boot-camp-products</artifactId>\n" +
                        "\t<version>0.0.1.BUILD-SNAPSHOT</version>\n" +
                        "\t<name>boot-camp-products</name>\n" +
                        "\t<description>Demo project for Spring Boot</description>\n" +
                        "\n" +
                        "\t<properties>\n" +
                        "\t\t<java.version>1.8</java.version>\n" +
                        "\t\t<spring-cloud.version>Greenwich.RELEASE</spring-cloud.version>\n" +
                        "\t</properties>\n" +
                        "\n" +
                        "\t<dependencies>\n" +
                        "\t\t<dependency>\n" +
                        "\t\t\t<groupId>org.springframework.boot</groupId>\n" +
                        "\t\t\t<artifactId>spring-boot-starter-web</artifactId>\n" +
                        "\t\t</dependency>\n" +
                        "\t\t<dependency>\n" +
                        "\t\t\t<groupId>org.springframework.boot</groupId>\n" +
                        "\t\t\t<artifactId>spring-boot-starter-data-rest</artifactId>\n" +
                        "\t\t</dependency>\n" +
                        "\t\t<dependency>\n" +
                        "\t\t\t<groupId>org.springframework.kafka</groupId>\n" +
                        "\t\t\t<artifactId>spring-kafka</artifactId>\n" +
                        "\t\t</dependency>\n" +
                        "\t\t<dependency>\n" +
                        "\t\t\t<groupId>org.springframework.boot</groupId>\n" +
                        "\t\t\t<artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                        "\t\t</dependency>\n" +
                        "\t\t<dependency>\n" +
                        "\t\t\t<groupId>com.h2database</groupId>\n" +
                        "\t\t\t<artifactId>h2</artifactId>\n" +
                        "\t\t\t<scope>runtime</scope>\n" +
                        "\t\t</dependency>\n" +
                        "\n" +
                        "\t\t<dependency>\n" +
                        "\t\t\t<groupId>com.google.code.gson</groupId>\n" +
                        "\t\t\t<artifactId>gson</artifactId>\n" +
                        "\t\t\t<version>2.8.5</version>\n" +
                        "\t\t\t<scope>compile</scope>\n" +
                        "\t\t</dependency>\n" +
                        "\t\t\n" +
                        "\t\t<dependency>\n" +
                        "\t\t\t<groupId>org.apache.commons</groupId>\n" +
                        "\t\t\t<artifactId>commons-lang3</artifactId>\n" +
                        "\t\t\t<version>3.8.1</version>\n" +
                        "\t\t</dependency>\n" +
                        "\n" +
                        "\t</dependencies>\n" +
                        "\n" +
                        "\t<dependencyManagement>\n" +
                        "\t\t<dependencies>\n" +
                        "\t\t\t<dependency>\n" +
                        "\t\t\t\t<groupId>org.springframework.cloud</groupId>\n" +
                        "\t\t\t\t<artifactId>spring-cloud-dependencies</artifactId>\n" +
                        "\t\t\t\t<version>${spring-cloud.version}</version>\n" +
                        "\t\t\t\t<type>pom</type>\n" +
                        "\t\t\t\t<scope>import</scope>\n" +
                        "\t\t\t</dependency>\n" +
                        "\t\t</dependencies>\n" +
                        "\t</dependencyManagement>\n" +
                        "\n" +
                        "\t<build>\n" +
                        "\t\t<plugins>\n" +
                        "\t\t\t<plugin>\n" +
                        "\t\t\t\t<groupId>org.springframework.boot</groupId>\n" +
                        "\t\t\t\t<artifactId>spring-boot-maven-plugin</artifactId>\n" +
                        "\t\t\t</plugin>\n" +
                        "\t\t</plugins>\n" +
                        "\t</build>\n" +
                        "\n" +
                        "</project>"
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
                } else if (name== 'application.yml'){
                    this.code="server:\n" +
                        "  port: 8080\n" +
                        "\n" +
                        "\n" +
                        "eventTopic: eventTopic\n" +
                        "\n" +
                        "---\n" +
                        "spring:\n" +
                        "  profiles: default\n" +
                        "  kafka:\n" +
                        "#    bootstrap-servers: http://35.200.47.242:31090\n" +
                        "    bootstrap-servers: localhost:9092\n" +
                        "    consumer:\n" +
                        "      enable-auto-commit: true\n" +
                        "  jpa:\n" +
                        "    properties:\n" +
                        "      hibernate:\n" +
                        "        show_sql: true\n" +
                        "        format_sql: true\n" +
                        "\n" +
                        "logging:\n" +
                        "  level:\n" +
                        "    org:\n" +
                        "      hibernate:\n" +
                        "        type: trace  \n" +
                        "server:\n" +
                        "  port: 8085\n" +
                        "---\n" +
                        "spring:\n" +
                        "  profiles: docker\n" +
                        "  kafka:\n" +
                        "    bootstrap-servers: my-kafka.kafka.svc.cluster.local:9092\n" +
                        "    consumer:\n" +
                        "      enable-auto-commit: true\n" +
                        "eventTopic: eventTopicDocker"
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
