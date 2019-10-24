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
      :height="titleH + (value.fieldDescriptors ? value.fieldDescriptors.length * itemH : 0)"
      v-on:dblclick="showProperty"
    >
      <geometry-rect
        :_style="{
          'fill-r': 1,
          'fill-cx': .1,
          'fill-cy': .1,
          'stroke-width': 1.4,
          fill: '#FFFFFF',
          'fill-opacity': 0,
          r: '1'
        }"
      >
      </geometry-rect>

      <sub-elements>
        <!--title-->
        <text-element v-if="value.classReference"
          :sub-width="'100%'"
          :sub-height="titleH/2"
          :sub-top="0"
          :sub-left="0"
          text="<<Bounded Context>>">
        </text-element>
        <text-element
          :sub-width="'100%'"
          :sub-height="titleH"
          :sub-top="0"
          :sub-left="0"
          :sub-style="{'font-weight': 'bold'}"
          :text="value.classReference ? value.classReference : value.name">
        </text-element>

        <edge-element
          :vertices="[[0,titleH - (value.classReference ? 10:0)],['100%',titleH - (value.classReference ? 10:0)]]"
          :label="''"
          :sub-style="{'arrow-start': 'none', 'stroke-width' : '0.5', 'arrow-end': 'none'}"
        >
        </edge-element>

        <text-element v-if="value.UMLEntity"
                      v-for="(item, index) in value.UMLEntity"
                      :sub-width="'90%'"
                      :sub-height="'30%'"
                      :sub-top="titleH + (index * itemH)"
                      :sub-left="'5%'"
                      :sub-style="{'text-anchor': 'start', 'font-size': 15 }"
                      :text="'+'+item.name + ': ' + item.type"
                      >
        </text-element>
      </sub-elements>
    </geometry-element>


    <modeling-property-panel
            :drawer.sync="value.drawer"
            :inputText.sync="value.inputText"
            :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/event.png'"
            :UMLEntity.sync="value.UMLEntity"
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
    name: 'class-definition',
    props: {},
    computed: {
      defaultStyle() {
        return {}
      },
      type() {
        return 'Class'
      },
      className() {
        return 'org.uengine.uml.model.ClassDefinition'
      },
      createNew(elementId, x, y, width, height) {
        return {
          _type: this.className(),
          name: 'Class',
          fieldDescriptors: [],
          elementView: {
            '_type': 'org.uengine.modeling.ElementView',
            'id': elementId,
            'x': x,
            'y': y,
            'width': width,
            'height': height,
            'style': JSON.stringify({})
          },
          drawer: false,
          selected: false,
          inputText: '',
          editing: false,
          UMLEntity: [],
          code: '',
          relationInfo:'',
          boundedContext: ''

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
      if (this.value.fieldDescriptors) {
        this.value.fieldDescriptors.forEach(function (attribute) {
          if (!attribute.attributes)
            attribute.attributes = {};
        });
      }

    },
    watch: {
      referenceClassName: function(){
        this.updateClassInfo();
      },
      "value.UMLEntity": function (newVal) {
        console.log(newVal)
      },


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
      updateClassInfo: function (){
        if(this.reference){
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

             for(var i in classDiagram.classDefinitions[1]){

               var classDefinition = classDiagram.classDefinitions[1][i];
               if(classDefinition.name == classNameOnly){


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

