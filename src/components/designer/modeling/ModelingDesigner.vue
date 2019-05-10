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
                    :movable="true"
                    :resizable="true"
                    :selectable="true"
                    :connectable="true"
                    v-if="value"
                    v-on:canvasReady="bindEvents"
            >
                <!--&lt;!&ndash;엘리먼트&ndash;&gt;-->
                <!--<div v-if="value[elementListBeanPath]">-->
                    <!--<component v-for="(element, index) in value[elementListBeanPath][1]" v-if="element != null"-->
                               <!--:is="getComponentByClassName(element._type)"-->
                               <!--v-model="value[elementListBeanPath][1][index]" :definition="value"-->
                    <!--&gt;</component>-->
                <!--</div>-->

                <!--&lt;!&ndash;릴레이션&ndash;&gt;-->
                <!--<div v-if="value[relationListBeanPath] && elementsLoadDone">-->
                    <!--<component v-for="(relation, index) in value[relationListBeanPath][1]" v-if="relation != null"-->
                               <!--:is="relationVueComponentName" v-model="value[relationListBeanPath][1][index]"-->
                               <!--:definition="value">-->
                    <!--</component>-->
                <!--</div>-->
            </opengraph>

            <v-card class="tools" style="top:100px;">
                <span class="bpmn-icon-hand-tool" v-bind:class="{ icons : !dragPageMovable, hands : dragPageMovable }"
                _width="30" _height="30" v-on:click="toggleGrip">
                <v-tooltip md-direction="right">Hands</v-tooltip>
                </span>
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
        </v-layout>
        <modeler-image-generator ref="modeler-image-generator"></modeler-image-generator>
    </div>
</template>

<script>
    export default {
        name: 'modeling-designer',
        components: {},
        props: {
            value: Object,
            elementTypes: Array
        },
        data() {
            return {
                dragPageMovable: false,
                relationVueComponentName: 'modeling-relation'
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
            toggleGrip: function () {
                this.dragPageMovable = !this.dragPageMovable;

                if (this.dragPageMovable) {
                    this.cursorStyle = 'cursor: url("/static/image/symbol/hands.png"), auto;';
                    this.handsStyle = ' color: #ffc124;';
                } else {
                    this.cursorStyle = null;
                    this.handsStyle = null;
                }
            },
            bindEvents: function (opengraph) {
                var me = this;
                var el = me.$el;
                var canvasEl = $(opengraph.container);
                if (!canvasEl || !canvasEl.length) {
                    return;
                }
                this.canvas = opengraph.canvas;
                //아이콘 드래그 드랍 이벤트 등록
                $(el).find('.draggable').draggable({
                    start: function () {
                        canvasEl.data('DRAG_SHAPE', {
                            'component': $(this).attr('_component'),
                            'width': $(this).attr('_width'),
                            'height': $(this).attr('_height')
                        });
                    },
                    helper: 'clone',
                    appendTo: canvasEl
                });

                canvasEl.droppable({
                    drop: function (event, ui) {
                        var componentInfo = canvasEl.data('DRAG_SHAPE'), shape, element;
                        if (componentInfo) {
                            var dropX = event.pageX - canvasEl.offset().left + canvasEl[0].scrollLeft;
                            var dropY = event.pageY - canvasEl.offset().top + canvasEl[0].scrollTop;

                            dropX = dropX / opengraph.scale;
                            dropY = dropY / opengraph.scale;

                            componentInfo = {
                                component: componentInfo.component,
                                x: dropX,
                                y: dropY,
                                width: parseInt(componentInfo.width, 10),
                                height: parseInt(componentInfo.height, 10),
                                label: ''
                            }
                            me.addElement(componentInfo);
                        }
                        canvasEl.removeData('DRAG_SHAPE');
                    }
                });
            },
            addElement: function (componentInfo, newTracingTag, originalData) {
                this.enableHistoryAdd = true;
                var me = this;
                var additionalData = {};

                var vueComponent = me.getComponentByName(componentInfo.component);
                console.log(componentInfo.component , this.relationVueComponentName)
                var element;

                if (componentInfo.component == this.relationVueComponentName) {
                    element = vueComponent.computed.createNew(
                        componentInfo.sourceElement,
                        componentInfo.targetElement,
                        componentInfo.vertices,
                    );
                } else {
                    element = vueComponent.computed.createNew(
                        this.uuid(),
                        componentInfo.x,
                        componentInfo.y,
                        componentInfo.width,
                        componentInfo.height
                    );
                }
            },
            getComponentByName: function (name) {
                var componentByName;
                $.each(window.Vue.classModelingComponents, function (i, component) {
                    if (component.default.name == name) {
                        componentByName = component.default;
                    }
                });
                return componentByName;
            },
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
            cursor: url('../../../../public/static/image/symbol/hands.png'), auto;
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
