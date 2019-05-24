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
                :_style="{'label-angle': value.elementView.angle}"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                v-on:dblclick="showProperty"
<<<<<<< HEAD
                v-on:rotateShape="rotateShapeActivity"
                :label.sync="value.inputText"


=======
                v-on:rotateShape="onRotateShape"
                :label.syhc="value.inputText"
                :_style="{
                'label-angle':value.elementView.angle
                }"
>>>>>>> origin/master
        >
            <!--v-on:dblclick="$refs['dialog'].open()"-->
            <geometry-rect
                    :_style="{
          'fill-r': 1,
          'fill-cx': .1,
          'fill-cy': .1,
          'stroke-width': 1.4,
          'stroke': '#F1A746',
          fill: '#F1A746',
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
                        :sub-style="{'font-weight': 'bold'}"
                        :text="value.classReference ? value.classReference : value.name">
                </text-element>

            </sub-elements>
        </geometry-element>


        <modeling-property-panel
                :drawer.sync="value.drawer"
                :titleName.sync="value.name"
                :inputText.sync="value.inputText"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/event.png'"
                  :aggregateList="aggregateList"
                v-model="value"
        >
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'

    export default {
        mixins: [Element],
        name: 'domain-event-definition',
        props: {},
        computed: {
            defaultStyle() {
                return {}
            },
            type() {
                return 'Domain'
            },
            className() {
                return 'org.uengine.uml.model.Domain'
            },
            createNew(elementId, x, y, width, height, angle) {
                return {
                    _type: this.className(),
                    name: 'Event',
                    fieldDescriptors: [],
                    elementView: {
                        '_type': 'org.uengine.modeling.Domain',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 100,
                        'height': 100,
                        'style': JSON.stringify({}),
                        'angle':0,
                    },
                    drawer: false,
                    selected: false,
                    inputText: ''
                }
            }
        },
        data: function () {
            return {
                itemH: 20,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference,
                aggregateList: []
            };
        },
        created: function () {

        },
        watch: {
<<<<<<< HEAD
          'value.drawer': function (newValue, oldValue) {
              var designer = this.getComponent('modeling-designer')

              var me = this
              console.log(me.designer);

              if (newValue == true) {
                  designer.value.forEach(function(temp) {
                    if(temp._type == "org.uengine.uml.model.Aggregate" )
                    me.aggregateList.push(temp.inputText);
                  })
              }

=======
            "value.elementView.angle": {
                handler: function () {
                    this.value.elementView.x = this.value.elementView.x +1
                    this.$nextTick(function () {
                        this.value.elementView.x = this.value.elementView.x -1

                    })
                }
>>>>>>> origin/master
            }
        },
        mounted: function () {
        },
        methods: {}
    }
</script>




<style scoped lang="scss" rel="stylesheet/scss">
</style>
