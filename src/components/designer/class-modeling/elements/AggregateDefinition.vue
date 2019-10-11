<template>
    <div>
        <geometry-element
                selectable
                movable
                resizable
                connectable
                deletable
                :id.sync="value.elementView.id"
                :x.sync="value.elementView.x"
                :y.sync="value.elementView.y"
                :width.sync="value.elementView.width"
                :height.sync="value.elementView.height"
                :angle.sync="value.elementView.angle"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                v-on:dblclick="showProperty"
                v-on:addedToGroup="onAddedToGroup"
                :label="value.inputText"
                :_style="{
                'label-angle':value.elementView.angle,
                'font-weight': 'bold','font-size': '16'
                }"
        >
            <!--v-on:dblclick="$refs['dialog'].open()"-->
            <geometry-rect
                    :_style="{
          'fill-r': 1,
          'fill-cx': .1,
          'fill-cy': .1,
          'stroke-width': 1.4,
          'stroke': '#F8D454',
          fill: '#F8D454',
          'fill-opacity': 1,
          r: '1'
        }"
            >
            </geometry-rect>
            <sub-elements>
                <!--title-->
                <text-element
                        :sub-width="'100%'"
                        :sub-height="titleH"
                        :sub-top="0"
                        :sub-left="0"
                        :text="value.classReference ? value.classReference : '<< ' + value.name + ' >>'">
                </text-element>
            </sub-elements>
        </geometry-element>


        <modeling-property-panel
                :drawer.sync="value.drawer"
                :titleName="value.name"
                :inputText.sync="value.inputText"
                :innerAggregate="value.innerAggregate"
                :entity.sync="value.aggregateEntity"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/aggregate.png'"
                :restApi.sync="value.restApi"
                v-model="value"
        >
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'
    var Mustache = require('mustache')

    export default {
        mixins: [Element],
        name: 'aggregate-definition',
        props: {},
        computed: {
            defaultStyle() {
                return {}
            },
            type() {
                return 'Aggregate'
            },
            className() {
                return 'org.uengine.uml.model.Aggregate'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    _type: this.className(),
                    name: 'Aggregate',
                    innerAggregate: {
                        'domain': [],
                        'command': [],
                        'view': [],
                        'policy': [],
                        'external': []
                    },
                    elementView: {
                        '_type': 'org.uengine.modeling.Aggregate',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 100,
                        'height': 100,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    selected: false,
                    inputText: '',
                    restApi: '',
                    aggregateEntity: [],
                    aggregateCode: '',
                    repositoryCode: ''
                }

            }
        },
        data: function () {
            return {
                itemH: 200,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference
            };
        },
        created: function () {

        },
        watch: {
            "value.inputText": function (newVal) {
                console.log(this.value)
                // console.log(this.code)
                // this.code = this.codeGenerate;
                this.value.aggregateCode = this.setAggregateTemplate(newVal, this.value)
                this.value.repositoryCode = this.setRepositoryTemplate(newVal, this.value)

            },
            "value.aggregateEntity": function () {
                var me = this
                console.log(this.value)
                // console.log(this.code)
                // this.code = this.codeGenerate;
                this.value.aggregateCode = me.setAggregateTemplate(me.value.inputText, this.value)
                this.value.repositoryCode = this.setRepositoryTemplate(me.value.inputText, this.value)
            }
        },
        mounted: function () {

        },
        methods: {
            setRepositoryTemplate(name, definition) {
                return Mustache.render(
                    "package com.example.template;\n " +
                    "import org.springframework.data.repository.PagingAndSortingRepository; \n " +
                    "public interface {{ inputText }}Repository extends PagingAndSortingRepository < {{ name }}, Long > { \n " +
                    "}\n", definition)
            },
            setAggregateTemplate(name, definition) {
                return Mustache.render(
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
                    "public class {{inputText}} {\n" +
                    "\n" +
                    "    @Id\n" +
                    "    @GeneratedValue\n" +
                    "    private Long id;\n\n" +

                    "{{#aggregateEntity}}\n" +
                    "    public {{type}} {{name}};\n" +
                    "{{/aggregateEntity}}\n" +
                    "\n" +
                    "    public Long getId() {\n" +
                    "        return id;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setId(Long id) {\n" +
                    "        this.id = id;\n" +
                    "    }\n" +
                    "\n" +
                    "{{#aggregateEntity}} \n" +
                    "    public {{type}} get{{upName}}() { \n" +
                    "        return {{name}};\n" +
                    "    }\n" +
                    "\n" +
                    "    public void set{{upName}}({{type}} {{name}}) { \n" +
                    "        this.{{name}} = {{name}};\n" +
                    "    }\n" +
                    "{{/aggregateEntity}}\n" +
                    "\n" +
                    "}", definition)
            }
        },

    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
