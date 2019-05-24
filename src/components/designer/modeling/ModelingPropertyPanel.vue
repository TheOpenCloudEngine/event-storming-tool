<!--
       * Todo: Property 작성 하는 부분 *
-->

<template>
<v-layout wrap>
  <v-navigation-drawer v-model="navigationDrawer" absolute right temporary>

    <v-list class="pa-1">
      <v-list-tile avatar>

        <v-list-tile-avatar>
          <img :src="img">
        </v-list-tile-avatar>

        <v-list-tile-content>
          <v-list-tile-title>{{ titleName }}</v-list-tile-title>
        </v-list-tile-content>

      </v-list-tile>
    </v-list>

    <v-list class="pt-0" dense>
      <v-divider></v-divider>

      <v-card v-if="value.name == 'Bounded Context'">
        <v-card-title>
          <span class="headline" v-if="titleName">{{titleName}} 내용 입력 </span>
        </v-card-title>

        <v-card-text>
          <v-autocomplete v-model="input" :items="aggregateList" label="Aggregate" persistent-hint prepend-icon="mdi-city">
          </v-autocomplete>
        </v-card-text>
      </v-card>

      <v-card v-else-if="value.name == 'Aggregate'">
        <v-card-text>
            <v-textarea name="input-7-1" outline :label="titleName" auto-grow v-model="input"></v-textarea>
        </v-card-text>

        <v-card-title>
          <span class="headline" v-if="titleName">연결 리스트 </span>
        </v-card-title>
      </v-card>

      <v-card v-else>
        <v-card-text>
            <v-textarea name="input-7-1" outline :label="titleName" auto-grow v-model="input"></v-textarea>
        </v-card-text>

        <v-card-title>
          <span class="headline" v-if="titleName">Aggregate 선택</span>
        </v-card-title>
        <v-card-text>
          <v-autocomplete v-model="selectAggregate" :items="aggregateList" label="Aggregate" persistent-hint prepend-icon="mdi-city">
          </v-autocomplete>
        </v-card-text>
      </v-card>

    </v-list>
  </v-navigation-drawer>
</v-layout>
</template>


<!--
<template>
<v-layout row justify-center>
  <v-dialog v-model="navigationDrawer" max-width="600px"> -->
    <!-- Bounded Context Setting Start -->
    <!-- <v-card v-if="value.name == 'Bounded Context'">
                <v-card-title>
                    <span class="headline" v-if="titleName">{{titleName}} 내용 입력 </span>
                </v-card-title>

                <v-card-text>
                    <v-autocomplete
                            v-model="input"
                            :items="aggregateList"
                            label="Aggregate"
                            persistent-hint
                            prepend-icon="mdi-city"
                    >
                    </v-autocomplete>
                </v-card-text>
            </v-card> -->
    <!-- Bounded Context Setting End -->
    <!-- Other Component Setting Start -->
    <!-- <v-card v-else>
                <v-card-title>
                    <span class="headline">{{titleName}} 내용 입력 </span>
                </v-card-title>
                <v-card-text>
                    <v-container fluid grid-list-md>
                        <v-textarea
                                name="input-7-1"
                                outline
                                :label="titleName"
                                auto-grow
                                v-model="input"
                        ></v-textarea>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" flat @click="navigationDrawer = false">확인</v-btn>
                </v-card-actions> -->
    <!-- Other Component Setting Stop -->
    <!-- </v-card>
        </v-dialog>
    </v-layout>
</template> -->

<script>
export default {
  name: 'modeling-property-panel',
  props: {
    drawer: Boolean,
    value: Object,
    titleName: String,
    inputText: String,
    aggregateList: Array,
    otherList: Array,
    img: String,
  },
  computed: {},
  data: function() {
    var me = this;
    return {
      navigationDrawer: false,
      _item: this.value,
      preventWatch: false,
      x: null,
      y: null,
      width: null,
      height: null,
      style: [],
      active: null,
      tracingTag: null,
      input: '',
      angle: null,
      selectAggregate:''
    }
  },
  created: function() {

  },
  mounted: function() {
    console.log(value)
  },
  watch: {
      input: function(newVal) {
          if(this.titleName == "Aggregate") {
              this.$emit('update:inputText', newVal)
          } else if (this.titleName == "Boundary Context") {
              this.$emit('update:inputText', newVal)
          } else {
              if(this.selectAggregate.length > 0){
                  this.$emit('update:inputText', newVal +'\n \n \n Aggregate:\n' +this.selectAggregate)
              }else{
                  this.$emit('update:inputText', newVal)
              }
          }
      },
      selectAggregate: function(newVal) {
          this.$emit('update:inputText', this.input +'\n \n \n Aggregate:\n' + newVal)
      },
    drawer: function(val) {
      this.navigationDrawer = val;
    },
    // value: {
    //   handler: function() {
    //     this.$emit("input", this.value);
    //   },
    //   deep: true
    // },
    //프로퍼티 창이 오픈되었을 때 모델값을 새로 반영한다.
    navigationDrawer: {
      handler: function(val, oldval) {
        if (val == true) {
          this._item = this.value;

          if (this.value.elementView) {
            this.x = this.value.elementView.x;
            this.y = this.value.elementView.y;
            this.width = this.value.elementView.width;
            this.height = this.value.elementView.height;
          }
          this.$emit('update:drawer', val);
        } else {
          //프로퍼티 에디팅 해제.
          this.$emit('update:drawer', false);
        }
      }
    },
    x: function(val) {
      this._item.elementView.x = val;
      this.$emit('update:value', this._item);
    },
    y: function(val) {
      this._item.elementView.y = val;
      this.$emit('update:value', this._item);
    },
    width: function(val) {
      this._item.elementView.width = val;
      this.$emit('update:value', this._item);
    },
    height: function(val) {
      this._item.elementView.height = val;
      this.$emit('update:value', this._item);
    },
    angle: function(val) {
      this._item.elementView.angle = val;
      this.$emit('update:value', this._item);
    },
    style: {
      handler: function(newVal, oldVal) {
        var style = {};
        if (newVal && newVal.length) {
          $.each(newVal, function(i, item) {
            style[item.key] = item.value;
          });
        }
        var view = this._item.elementView || this._item.relationView;
        view.style = JSON.stringify(style);
        this.$emit('update:value', this._item);
      },
      deep: true
    }
  },
  mounted: function() {

  },
  methods: {

  }
}
</script>


<style lang="scss" rel="stylesheet/scss">
.md-sidenav .md-sidenav-content {
    width: 400px;
}

.md-sidenav.md-right .md-sidenav-content {
    width: 600px;
}
</style>
