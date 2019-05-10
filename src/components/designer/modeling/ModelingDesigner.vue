<template xmlns:v-on="http://www.w3.org/1999/xhtml">
    <div class="canvas-panel">
        <v-layout>
            <v-layout md-flex="20">
            </v-layout>

            <opengraph
                    ref="opengraph"
                    focus-canvas-on-select
                    wheelScalable
                    :labelEditable="true"
                    :dragPageMovable="dragPageMovable"
                    :enableContextmenu="false"
                    :enableRootContextmenu="false"
                    :enableHotkeyCtrlC="false"
                    :enableHotkeyCtrlV="false"
                    :enableHotkeyDelete="false"
                    :slider="true"
                    :movable="!editMode"
                    :resizable="!editMode"
                    :selectable="!editMode"
                    :connectable="!editMode"
                    v-if="value"
                    v-on:canvasReady="bindEvents"
                    v-on:connectShape="onConnectShape"
                    v-on:removeShape="onRemoveShape"
            >
                <!--엘리먼트-->
                <div v-if="value[elementListBeanPath]">
                    <component v-for="(element, index) in value[elementListBeanPath][1]" v-if="element != null"
                               :is="getComponentByClassName(element._type)"
                               v-model="value[elementListBeanPath][1][index]" :definition="value"
                    ></component>
                </div>

                <!--릴레이션-->
                <div v-if="value[relationListBeanPath] && elementsLoadDone">
                    <component v-for="(relation, index) in value[relationListBeanPath][1]" v-if="relation != null"
                               :is="relationVueComponentName" v-model="value[relationListBeanPath][1][index]"
                               :definition="value">
                    </component>
                </div>
            </opengraph>

            <v-card class="tools" style="top:100px;">
                <!--<span class="bpmn-icon-hand-tool" v-bind:class="{ icons : !dragPageMovable, hands : dragPageMovable }"-->
                <!--_width="30" _height="30" v-on:click="toggleGrip">-->
                <!--<v-tooltip md-direction="right">Hands</v-tooltip>-->
                <!--</span>-->
                <v-tooltip right
                           v-for="item in elementTypes"
                >
                    <template v-slot:activator="{ on }">
                        <span class="icons draggable"
                              align="center"
                              :_component="item.component"
                              :_width="item.width"
                              :_height="item.height">
                        <img height="30px" width="30px" :src="item.src" v-on="on">
                            </span>
                    </template>
                    <span>{{item.label}}</span>

                </v-tooltip>
            </v-card>

            <v-layout v-if="!editMode">
                <v-container>
                    <label>Definition Name</label>
                    <v-input v-model="value.name" type="text"></v-input>
                </v-container>
            </v-layout>
        </v-layout>
        <modeler-image-generator ref="modeler-image-generator"></modeler-image-generator>
    </div>
</template>

<script>
    export default {
        name: 'modeling-designer',
        components: {},
        props: {
            value: Object
        },
        data() {
            return {

            }
        },
        computed: {


        },
        created: function () {
        },
        mounted() {
            $(document).keydown((evt) => {
                if (evt.keyCode == 46 || evt.keyCode == 8) {
                    this.deleteActivity();
                }
            });

            this.relationVueComponentNameTmp = 'modeling-relation';
            this.$emit('update:relationVueComponentName', this.relationVueComponentNameTmp);

            // this.history = [JSON.parse(JSON.stringify(this.value))];
            this.$nextTick(function () {
                let startTime = new Date().getTime()

                //$nextTick delays the callback function until Vue has updated the DOM
                // (which usually happens as a result of us changing the data
                //  so make any DOM changes here
                this.canvas._CONFIG.FAST_LOADING = false;
                this.canvas.updateSlider();
                //timer end
                this.$refs.opengraph.printTimer(startTime, new Date().getTime());
            });
        },
        watch: {

        },

        methods: {

        }
    }
</script>

<style scoped lang="scss" rel="stylesheet/scss">
    .canvas-panel {
        top: 0px;
        bottom: 0px;
        left: 0px;
        right: 0px;
        position: absolute;
        overflow: hidden;

        .fullcanvas {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 10%;
            left: 0px;
            overflow: hidden;
        }

        .fullcanvashands {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 10%;
            left: 0px;
            overflow: hidden;
            cursor: url('/static/image/symbol/hands.png'), auto;
        }

        .tools {
            position: absolute;
            width: 48px;
            left: 20px;
            top: 20px;
            padding: 4px;
            overflow: hidden;
            .icons {
                margin-top: 5px;
                margin-bottom: 5px;
            }
            .hands {
                margin-top: 5px;
                margin-bottom: 5px;
            }
        }
        .zoom {
            position: absolute;
            width: 42px;
            right: 20px;
            bottom: 120px;
            .icons {
                font-size: 25px;
                margin-left: 10px;
                margin-top: 5px;
                margin-bottom: 5px;
            }
            .hands {
                font-size: 25px;
                margin-left: 10px;
                margin-top: 5px;
                margin-bottom: 5px;
            }
        }
        .icons {
            cursor: pointer;
            font-size: 30px;
            &:hover {
                color: #ffc124;
            }
        }

        .hands {
            cursor: pointer;
            font-size: 30px;
            color: #ffc124;
        }
        .import, .export, .save, .history {
            position: absolute;
            padding: 8px;
            .icons {
                font-size: 25px;
                margin-left: 10px;
            }
        }
        .import {
            left: 80px;
            bottom: 20px;
        }
        .export {
            left: 180px;
            bottom: 20px;
        }
        .history {
            left: 280px;
            bottom: 20px;
        }
    }

    /* The whole thing */
    .custom-menu {
        display: none;
        z-index: 1000;
        position: absolute;
        overflow: hidden;
        border: 1px solid #CCC;
        white-space: nowrap;
        font-family: sans-serif;
        background: #FFF;
        color: #333;
        border-radius: 5px;
        padding: 0;
    }

    /* Each of the items in the list */
    .custom-menu li {
        padding: 8px 12px;
        cursor: pointer;
        list-style-type: none;
        transition: all .3s ease;
        user-select: none;
    }

    .custom-menu li:hover {
        background-color: #DEF;
    }
</style>
