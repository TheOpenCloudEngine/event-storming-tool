<template xmlns:v-on="http://www.w3.org/1999/xhtml">
    <div class="canvas-panel">
        <v-layout>
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
                    v-on:connectShape="onConnectShape"
            >
                <!--엘리먼트-->
                <div v-for="(element, index) in value">
                    <component
                            :is="getComponentByClassName(element._type)"
                            v-model="value[index]"
                    ></component>
                </div>

                <!--&lt;!&ndash;릴레이션&ndash;&gt;-->
                <!--<div v-if="value[relationListBeanPath] && elementsLoadDone">-->
                <!--<component v-for="(relation, index) in value[relationListBeanPath][1]" v-if="relation != null"-->
                <!--:is="relationVueComponentName" v-model="value[relationListBeanPath][1][index]"-->
                <!--:definition="value">-->
                <!--</component>-->
                <!--</div>-->
            </opengraph>

            <div style="z-index: 100; width: 100%;" align="right">
                <v-flex xs12 sm6 style="display: inline-block">
                    <v-text-field
                            label="Project Name"
                            v-model="projectName"
                            single-line
                    ></v-text-field>
                </v-flex>
                <text-reader
                        :importType="'json'"
                        @load="value = $event"
                        style="display: inline-block"
                        :fileName.sync="projectName"
                ></text-reader>
                <v-btn color="info" v-on:click.native="download">save</v-btn>
            </div>

            <v-card class="tools" style="top:100px; text-align: center;">
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
    import TextReader from "@/components/yaml.vue";

    var FileSaver = require('file-saver');
    import {saveAs} from 'file-saver';

    export default {
        name: 'modeling-designer',
        components: {
            TextReader,
            saveAs,
        },
        props: {
            elementTypes: Array
        },
        data() {
            return {
                canvas: null,
                dragPageMovable: false,
                relationVueComponentName: 'modeling-relation',
                value: [],
                enableHistoryAdd: false,
                undoing: false,
                undoed: false,
                undoIndex: 0,
                aggregateList: [],
                tmpValue: [],
                projectName: '',
                noPushUndo: false,
                redoArray: [],
                undoArray: [[]],
            }
        },
        computed: {},
        created: function () {
        },
        mounted() {
            var me = this
            me.$ModelingBus.$on('MoveEvent', function () {
                me.$nextTick(function () {
                    me.undoArray.push(JSON.parse(JSON.stringify(me.value)));
                    me.redoArray = [];
                })
            })
            this.$nextTick(function () {
                let startTime = new Date().getTime()
                //$nextTick delays the callback function until Vue has updated the DOM
                // (which usually happens as a result of us changing the data
                //  so make any DOM changes here

                this.canvas._CONFIG.FAST_LOADING = false;
                this.canvas.updateSlider();

                //timer end
                this.$refs.opengraph.printTimer(startTime, new Date().getTime());

                $(document).keydown((evt) => {
                    if (evt.keyCode == 67 && (evt.metaKey || evt.ctrlKey)) {
                        console.log("COPY");
                        this.copy();
                    } else if (evt.keyCode == 86 && (evt.ctrlKey || evt.metaKey)) {
                        console.log("paste");
                        this.paste();
                    } else if (evt.keyCode == 46 || evt.keyCode == 8) {
                        console.log("delete");
                        this.deleteActivity();
                    } else if (evt.keyCode == 90 && (evt.metaKey || evt.ctrlKey)) {
                        if (evt.shiftKey) {
                            console.log("Control_SHIFIT_Z");
                            me.redo()
                        } else {
                            console.log("Control_Z");
                            me.undo();
                        }
                    }
                });
            });
        },
        watch: {

        },

        methods: {
            //복사
            copy: function () {
                var me = this
                me.tempValue = []
                me.value.forEach(function (tmp, idx) {
                    if (tmp.selected == true) {
                        me.tempValue.push(tmp)
                    }
                })
            },
            //붙여넣기
            paste: function () {
                var me = this
                var temp = JSON.parse(JSON.stringify(me.tempValue))

                if (me.tempValue != null) {
                    temp.forEach(function (tmp, idx) {
                        tmp.elementView.id = me.uuid();
                        tmp.elementView.x = tmp.elementView.x + 10
                        tmp.elementView.y = tmp.elementView.y + 10

                        me.value.push(tmp);
                        me.history.push(tmp);
                    })
                    //초기화
                } else {
                    console.log("다시 복사 필요");
                }
            },
            download: function () {
                console.log("aa")

                var me = this;
                var text = JSON.stringify(me.value);

                var filename = this.projectName + '.json';

                var file = new File([text], filename, {type: "text/json;charset=utf-8"});
                FileSaver.saveAs(file);
            },
            deleteActivity: function () {
                var me = this

                let tmpArray = JSON.parse(JSON.stringify(me.value));
                let drawer;
                let selected = []
                this.value.some(function (tmp) {
                    if (tmp.drawer) {
                        drawer = true
                    }
                })
                if (!drawer) {
                    tmpArray.forEach(function (valueTmp, index) {
                        if (valueTmp.selected) {
                            if (valueTmp.elementView) {
                                selected.push(valueTmp.elementView.id)
                                me.undoArray.push(valueTmp);
                                me.redoArray = [];
                                tmpArray[index] = null
                            } else if (valueTmp.relationView) {
                                selected.push(valueTmp.relationView.id)
                                me.undoArray.push(valueTmp);
                                me.redoArray = [];
                                tmpArray[index] = null
                            }
                        }
                    })
                    console.log(selected)
                    let tmpArray2 = tmpArray.filter(n => n)

                    selected.forEach(function (selectedTmp) {
                        tmpArray2.forEach(function (relationTmp, index) {
                            if (relationTmp != null) {
                                if (relationTmp.relationView) {
                                    if (relationTmp.relationView.from == selectedTmp || relationTmp.relationView.to == selectedTmp) {
                                        me.undoArray.push(relationTmp);
                                        me.redoArray = [];
                                        tmpArray2[index] = null
                                    }
                                }
                            }
                        })
                    })

                    me.value = tmpArray2.filter(n => n)
                }

            },
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
            uuid: function () {
                function s4() {
                    return Math.floor((1 + Math.random()) * 0x10000)
                        .toString(16)
                        .substring(1);
                }

                return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                    s4() + '-' + s4() + s4() + s4();
            },
            onConnectShape: function (edge, from, to) {
                var me = this;

                //존재하는 릴레이션인 경우 (뷰 컴포넌트), 데이터 매핑에 의해 자동으로 from, to 가 변경되어있기 때문에 따로 로직은 필요없음.
                //=> 바뀌어야 함.
                //신규 릴레이션인 경우에는 릴레이션 생성
                var edgeElement, originalData;
                var isComponent = false;
                if (edge.shape) {
                    edgeElement = edge;
                } else {
                    isComponent = true;
                    edgeElement = edge.element;
                }

                if (edgeElement && from && to) {
                    var vertices = '[' + edgeElement.shape.geom.vertices.toString() + ']';
                    var componentInfo = {
                        component: 'class-relation',
                        sourceElement: from.$parent,
                        targetElement: to.$parent,
                        vertices: vertices,
                        isFilled: true,
                        relationView: {
                            style: JSON.stringify({}),
                            value: vertices,
                        }
                    }

                    from.$parent.value.elementView.id = from.id;
                    to.$parent.value.elementView.id = to.id;

                    if (isComponent) {
                        me.canvas.removeShape(edgeElement, true);
                        //this.removeComponentByOpenGraphComponentId(edgeElement.id);
                        //기존 컴포넌트가 있는 경우 originalData 와 함께 생성
                        this.addElement(componentInfo, null, JSON.parse(JSON.stringify(originalData)));
                    }
                    else {
                        me.canvas.removeShape(edgeElement, true);
                        //기존 컴포넌트가 없는 경우 신규 생성
                        this.addElement(componentInfo);
                    }
                }
            },
            redo: function () {
                var me = this
                if (me.redoArray.length > 0) {
                    var tmpData = me.redoArray.pop();
                    me.value = JSON.parse(JSON.stringify(tmpData));
                    if (me.undoArray.length == 0 && me.value.length == 0) {
                        me.undoArray.push([])
                    }
                    me.undoArray.push(JSON.parse(JSON.stringify(tmpData)));
                } else {
                    console.log(">>NO DATA");
                }
            },
            undo: function () {
                var me = this
                if (me.undoArray.length > 0) {
                    if (me.undoArray[me.undoArray.length - 1].length > 0) {
                        me.redoArray.push(JSON.parse(JSON.stringify(me.value)));
                    }
                    var tmpData = me.undoArray.pop();
                    me.value = JSON.parse(JSON.stringify(me.undoArray[me.undoArray.length - 1]));
                } else {
                    console.log(">>NO DATA");
                }
            },
            addElement: function (componentInfo, newTracingTag, originalData) {
                this.enableHistoryAdd = true;
                var me = this;
                var additionalData = {};

                var vueComponent = me.getComponentByName(componentInfo.component);
                // console.log(componentInfo.component , this.relationVueComponentName)
                var element;


                if (componentInfo.component == 'class-relation') {
                    element = vueComponent.computed.createNew(
                        componentInfo.sourceElement.value,
                        componentInfo.targetElement.value,
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
                // console.log(this.value, element.elementView.id)
                if (me.value == null) {
                    me.value = []
                }
                me.value.push(element);
                me.undoArray.push(JSON.parse(JSON.stringify(me.value)));
                me.redoArray = [];

            },
            getComponentByName: function (name) {
                var componentByName;
                $.each(window.Vue._components, function (i, component) {
                    if (component.name == name) {
                        // console.log(component.default.name)
                        componentByName = component;
                    }
                });
                return componentByName;
            },
            getComponentByClassName: function (className) {
                var componentByClassName;

                $.each(window.Vue.classModelingComponents, function (i, component) {
                    if (component.default.computed && component.default.computed.className && component.default.computed.className() == className) {
                        componentByClassName = component.default;
                    }
                });
                return componentByClassName;
            }
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
