<template>
    <div>
        <geometry-element
                selectable
                movable
                resizable
                connectable
                deletable
                labelEditable
                :id.sync="value.elementView.id"
                :x.sync="value.elementView.x"
                :y.sync="value.elementView.y"
                :width.sync="value.elementView.width"
                :height="value.elementView.height"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                :label="value.name"
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
            <sub-elements labelEditable>
                <!--title-->
                <text-element
                        :sub-width="'100%'"
                        :sub-height="titleH"
                        :sub-top="0"
                        :sub-left="0"
                        :sub-style="{'font-weight': 'bold'}"
                        :text="value.classReference ? value.classReference : value.name">
                </text-element>
            </sub-elements>
        </geometry-element>


        <modeling-property-panel
                :drawer.sync="drawer"
                v-model="value"
                style="height: 100%"
        >
            <template slot="properties-contents" style="height: 100%">
                <v-switch v-model="reference">Reference from other model (Bounded Context)</v-switch>

                <div v-if="!reference">
                    <v-container>
                        <v-text-field type="text"
                                      label="클래스명"
                                      v-model="value.name"></v-text-field>
                    </v-container>

                    <v-layout v-for="attribute in value.fieldDescriptors">
                        <v-layout>
                            <v-checkbox v-model="attribute.attributes.isKey">pk</v-checkbox>
                            <v-container>
                                <label>Attribute Name</label>
                                <v-text-field type="text"
                                              v-model="attribute.name"></v-text-field>
                            </v-container>
                        </v-layout>
                        <v-layout>
                            <v-container>
                                <label>Type</label>
                                <!--md-input type="text" v-model="attribute.className"></md-input-->
                                <md-select type="text"
                                           v-model="attribute.className">

                                    <md-option value="java.lang.String">String</md-option>
                                    <md-option value="java.lang.Long">Long</md-option>
                                    <md-option value="java.lang.Double">Double</md-option>
                                    <md-option value="java.util.Date">Date</md-option>
                                    <md-option value="java.lang.Boolean">Boolean</md-option>

                                </md-select>
                                <md-icon v-on:click.native="removeAttribute(attribute)"
                                         class="md-primary"
                                         style="cursor: pointer"
                                >delete
                                </md-icon>
                            </v-container>
                        </v-layout>
                    </v-layout>

                    <v-btn @click="addAttribute">ADD ATTRIBUTE</v-btn>
                </div>
                <div v-else>
                    <class-selector :modeling-classes-only="true" v-model="referenceClassName"></class-selector>
                    <v-btn @click="updateClassInfo">Update class info</v-btn>
                </div>

            </template>

            <template slot="additional-tabs">
            </template>
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'

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
                    fieldDescriptors: [],
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
                    selected: false
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
            if (this.value.fieldDescriptors) {
                this.value.fieldDescriptors.forEach(function (attribute) {
                    if (!attribute.attributes)
                        attribute.attributes = {};
                });
            }

        },
        watch: {
            referenceClassName: function () {
                this.updateClassInfo();
            },
            drawer: function () {
                console.log(this)
            }

        },
        mounted: function () {

        },
        methods: {
            addAttribute: function () {
                this.value.fieldDescriptors.push({
                    name: 'attribute',
                    className: 'java.lang.String',
                    attributes: {},
                    _type: 'org.uengine.uml.model.Attribute'

                });
            },
            removeAttribute: function (attribute) {
                this.value.fieldDescriptors.splice(this.value.fieldDescriptors.indexOf(attribute), 1);
            },
            updateClassInfo: function () {
                if (this.reference) {
                    var definitionAndClassName = this.referenceClassName.split("#");
                    var definitionName = definitionAndClassName[0];
                    var classNameOnly = definitionAndClassName[1];

                    var me = this;

                    var result;
                    var uri = (encodeURI(window.backend.$bind.ref + "/definition/raw/" + definitionName + ".ClassDiagram.json"))

                    console.log("try to get class diagram: " + uri);

                    var xhr = new XMLHttpRequest();
                    var me = this;

                    xhr.open('GET', uri, false);
                    xhr.setRequestHeader("access_token", localStorage['access_token']);
                    xhr.onload = function () {
                        result = JSON.parse(xhr.responseText)

                        var classDiagram = result.definition;

                        for (var i in classDiagram.classDefinitions[1]) {

                            var classDefinition = classDiagram.classDefinitions[1][i];
                            if (classDefinition.name == classNameOnly) {


                                classDefinition.elementView.x = me.value.elementView.x;
                                classDefinition.elementView.y = me.value.elementView.y;

                                classDefinition.classReference = me.referenceClassName;

                                me.value = classDefinition;
                                //me.$emit("input", me.value);

                                break;
                            }

                        }
                    };
                    xhr.send();


                }
            }
        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>

