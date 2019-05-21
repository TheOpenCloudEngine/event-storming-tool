<template>
  <div>
    <geometry-element
      selectable
      movable
      resizable
      connectable
      :id.sync="value.elementView.id"
      :x.sync="value.elementView.x"
      :y.sync="value.elementView.y"
      :width.sync="value.elementView.width"
      :height.sync="value.elementView.height"
      v-on:dblclick="showProperty"
      v-on:selectShape="selectedActivity"
      v-on:deSelectShape="deSelectedActivity"
      v-on:addedToGroup="onAddedToGroup"
    >
      <!--v-on:dblclick="$refs['dialog'].open()"-->
      <geometry-rect
        :_style="{
          'fill-r': 1,
          'fill-cx': .1,
          'fill-cy': .1,
          'stroke-width': 1.4,
          'stroke': '#5FC08B',
          fill: '#5FC08B',
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
            :titleName="value.name"
            :inputText.sync="value.inputText"
            v-model="value"
    >
    </modeling-property-panel>

  </div>
</template>

<script>
  import Element from '../../modeling/Element'

  export default {
    mixins: [Element],
    name: 'view-definition',
    props: {},
    computed: {
      defaultStyle() {
        return {}
      },
      type() {
        return 'View'
      },
      className() {
        return 'org.uengine.uml.model.View'
      },
      createNew(elementId, x, y, width, height) {

        return {
          _type: this.className(),
          name: 'View',
          fieldDescriptors: [],
          elementView: {
            '_type': 'org.uengine.modeling.View',
            'id': elementId,
            'x': x,
            'y': y,
            'width': width,
            'height': height,
            'style': JSON.stringify({})
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
        reference: this.value.classReference!=null,
        referenceClassName: this.value.classReference
      };
    },
    created: function () {

    },
    watch: {

    },
    mounted: function () {

    },
    methods: {

    }
  }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>

