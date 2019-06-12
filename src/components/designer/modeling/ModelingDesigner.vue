<template xmlns:v-on="http://www.w3.org/1999/xhtml">
    <div class="canvas-panel">
        <v-layout right>
            <opengraph ref="opengraph" focus-canvas-on-select wheelScalable :labelEditable="true"
                       :dragPageMovable="dragPageMovable" :enableContextmenu="false" :enableRootContextmenu="false"
                       :enableHotkeyCtrlC="false" :enableHotkeyCtrlV="false"
                       :enableHotkeyDelete="false" :enableHotkeyCtrlZ="false" :enableHotkeyCtrlD="false"
                       :enableHotkeyCtrlG="false" :slider="false" :movable="true" :resizable="true" :selectable="true"
                       :connectable="true" v-if="value" v-on:canvasReady="bindEvents"
                       v-on:connectShape="onConnectShape" :imageBase="imageBase">
                <!--엘리먼트-->
                <div v-for="(element, index) in value.definition">
                    <component :is="getComponentByClassName(element._type)"
                               v-model="value.definition[index]"></component>
                </div>
                <div v-for="(element, index) in value.relation">
                    <component :is="getComponentByClassName(element._type)" v-model="value.relation[index]"></component>
                </div>
            </opengraph>


            <v-layout style="margin-top: 16px; margin-left: 5px; margin-right: 10px;">
                <v-badge overlap>
                    <template v-slot:badge>
                        <span>{{ connectCount }}</span>
                    </template>

                    <v-avatar>
                        <v-layout justify-end row v-if="show">
                            <v-tooltip v-for="item in connectInfo" bottom>
                                <template v-slot:activator="{ on }">
                                    <v-avatar v-on="on">
                                        <img :src='item.img'>
                                    </v-avatar>
                                </template>
                                <span>{{ item.name }}</span>
                            </v-tooltip>
                        </v-layout>
                        <v-btn
                                fab
                                small
                                @click="connectshow()">
                        </v-btn>
                    </v-avatar>
                </v-badge>

                <v-btn color="info" v-on:click.native="restApiPush">BUILD
                </v-btn>
            </v-layout>

            <v-layout right>
                <v-flex xs12 sm6 style="display: inline-block">
                    <v-text-field label="Project Name" v-model="projectName" single-line></v-text-field>
                </v-flex>
                <text-reader :importType="'json'" @load="value = $event" style="display: inline-block"
                             :fileName.sync="projectName"></text-reader>
                <v-btn color="info" v-on:click.native="download"
                       style="margin-top: 16px; margin-left: 5px; margin-right: 10px;">save
                </v-btn>
            </v-layout>


            <v-card class="tools" style="top:100px; text-align: center;">
      <span class="bpmn-icon-hand-tool" v-bind:class="{ icons : !dragPageMovable, hands : dragPageMovable }" _width="30"
            _height="30" v-on:click="toggleGrip">
        <v-tooltip md-direction="right">Hands</v-tooltip>
      </span>
                <v-tooltip right v-for="(item, key) in elementTypes" :key="key">
                    <template v-slot:activator="{ on }">
          <span
                  class="icons draggable"
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

        <v-snackbar v-model="snackbar" :color="color" :multi-line="mode === 'multi-line'" :timeout="timeout"
                    :vertical="mode === 'vertical'">
            {{ text }}
            <v-btn dark flat @click="snackbar = false">
                Close
            </v-btn>
        </v-snackbar>
        <modeler-image-generator ref="modeler-image-generator"></modeler-image-generator>
    </div>
</template>

<script>
    import TextReader from "@/components/yaml.vue";
    import {
        v4
    } from 'uuid';
    import Pusher from 'pusher-js';

    var FileSaver = require('file-saver');
    import {
        saveAs
    } from 'file-saver';

    export default {
        name: 'modeling-designer',
        components: {
            TextReader,
            saveAs,
            Pusher
        },
        props: {
            elementTypes: Array
        },
        data() {
            return {
                canvas: null,
                dragPageMovable: false,
                relationVueComponentName: 'modeling-relation',
                value: {
                    'definition': [],
                    'relation': []
                },
                enableHistoryAdd: false,
                undoing: false,
                undoed: false,
                undoIndex: 0,
                tmpValue: [],
                projectName: '',
                noPushUndo: false,
                redoArray: [],
                undoArray: [],
                imageBase: 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/symbol/',
                userId: '',

                snackbar: false,
                color: 'error',
                mode: 'multi-line',
                timeout: 6000,
                text: '수정중입니다.',

                connectCount: 0,
                connectInfo: [],
                show: false


            }
        },
        computed: {
            drawer: {
                get: function () {
                    var me = this
                    var temp = false;
                    var tmpArray = JSON.parse(JSON.stringify(me.value.definition))
                    if (tmpArray.length > 0) {
                        tmpArray.some(function (tmp, index) {
                            if (tmp.drawer) {
                                temp = true
                                return;
                            }
                        })
                    }
                    return temp;
                }
            },
            id: {
                get: function () {
                    return this.projectName
                }
            }
        },
        created: function () {
        },
        mounted() {
            var me = this

            me.$ModelingBus.$on('MoveEvent', function () {
                me.$nextTick(function () {
                    me.connectInfoR("add", 'https://stickershop.line-scdn.net/stickershop/v1/product/718/LINEStorePC/main.png;compress=true', 'Zang')
                    me.undoArray.push(JSON.parse(JSON.stringify(me.value)));
                    me.redoArray = [];
                    me.value.definition.forEach(function (tmp) {
                        if (tmp.selected == true) {
                            me.syncOthers(tmp);
                        }
                    })
                })
            })

            const pusher = new Pusher('33169ca8c59c1f7f97cd', {
                cluster: 'ap3',
            });

            const channel = pusher.subscribe('painting');
            this.userId = v4();

            console.log(channel);

            //
            // channel.bind('pusher:subscription_succeeded', function(members) {
            //   // for example
            //   update_member_count(members.count);
            //
            //   console.log(members.count);
            //   members.ForEach(function(member) {
            //     // for example:
            //     // add_member(member.id, member.info);
            //   });
            // })
            //
            channel.bind('draw', (data) => {
                // console.log(data)
                //https://event-lhgws4pe7a-uc.a.run.app/
                // me.$http.get('http://api-ap3.pusher.com/apps/791580/channels/painting').then(function (result) {
                //   console.log(result)
                // })

                const {
                    userId: id,
                    newVal
                } = data;
                console.log(newVal);
                console.log("비교", me.userId, id);
                //다른 창에서 바꿨을때
                if (me.userId !== id) {
                    var check = true
                    me.value.definition.forEach(function (elementA) {
                        if (elementA.elementView.id == newVal.elementView.id) {
                            console.log("변경");
                            elementA.selected = false
                            elementA.elementView = newVal.elementView
                            elementA.inputText = newVal.inputText
                            elementA.restApi = newVal.restApi
                            check = false
                            if (newVal.drawer == true) {
                                elementA.editing = true
                            } else {
                                elementA.editing = false
                            }

                        }
                    })

                    if (check) {
                        console.log("추가");
                        me.value.definition.push(newVal);
                    }
                } else {
                    //같은창에서 변경 했을때

                }
            });

            this.$nextTick(function () {
                let startTime = new Date().getTime()
                //$nextTick delays the callback function until Vue has updated the DOM
                // (which usually happens as a result of us changing the data
                //  so make any DOM changes here

                this.canvas._CONFIG.FAST_LOADING = false;
                this.canvas.updateSlider();
                //timer end
                me.undoArray.push({
                    'definition': [],
                    'relation': []
                })
                this.$refs.opengraph.printTimer(startTime, new Date().getTime());

                $(document).keydown((evt) => {
                    if (evt.keyCode == 67 && (evt.metaKey || evt.ctrlKey)) {
                        this.copy();
                    } else if (evt.keyCode == 86 && (evt.ctrlKey || evt.metaKey)) {
                        this.paste();
                    } else if (evt.keyCode == 46 || evt.keyCode == 8) {
                        this.deleteActivity();
                    } else if (evt.keyCode == 90 && (evt.metaKey || evt.ctrlKey)) {
                        if (evt.shiftKey) {
                            me.redo()
                        } else {
                            me.undo();
                        }
                    }
                });
            });
        },
        watch: {},

        methods: {
            connectInfoR: function (state, img, name) {
                var me = this
                if (state == "add") {
                    let tmpObject = {"img": img, "name": name}
                    me.connectCount = me.connectCount + 1
                    me.connectInfo.push(tmpObject);
                } else {
                    me.connectCount = me.connectCount - 1
                    me.connectInfo.pop();
                }

            },
            connectshow: function () {
                var me = this
                if (me.show == true) {
                    me.show = false
                } else {
                    me.show = true
                }
            },
            restApiPush: function () {
                var me = this;
                me.$http.post(`http://localhost:8081/event/${me.projectName}`, me.value, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(function () {
                    console.log("done")
                })

            },
            //멀티
            syncOthers(elements) {
                var me = this
                let userId = this.userId
                let newVal = elements

                const body = {
                    newVal,
                    userId,
                };
                fetch('http://localhost:4000/paint', {
                    method: 'post',
                    body: JSON.stringify(body),
                    headers: {
                        'content-type': 'application/json',
                    },
                }).then(() => console.log("throw"));
            },
            //복사
            copy: function () {
                var me = this
                if (!me.drawer) {
                    me.tempValue = []
                    me.value.definition.forEach(function (tmp, idx) {
                        if (tmp.selected == true) {
                            me.tempValue.push(tmp)
                        }
                    })
                    me.value.relation.forEach(function (tmp, idx) {
                        if (tmp.selected == true) {
                            me.tempValue.push(tmp)
                        }
                    })
                    this.syncOthers(tmp);
                }

            },
            //붙여넣기
            paste: function () {
                var me = this
                if (!me.drawer) {
                    var temp = JSON.parse(JSON.stringify(me.tempValue))

                    if (me.tempValue != null) {
                        temp.forEach(function (tmp, idx) {
                            tmp.elementView.id = me.uuid();
                            tmp.elementView.x = tmp.elementView.x + 10
                            tmp.elementView.y = tmp.elementView.y + 10
                            me.value.definition.push(tmp);
                            me.redoArray.push(tmp);
                        })
                        this.syncOthers(tmp);
                        //초기화
                    } else {
                    }
                }
            },
            download: function () {
                var me = this;
                var text = JSON.stringify(me.value);

                var filename = this.projectName + '.json';

                var file = new File([text], filename, {
                    type: "text/json;charset=utf-8"
                });
                FileSaver.saveAs(file);
            },
            deleteActivity: function () {
                var me = this
                if (!me.drawer) {
                    let selected = []
                    let definitionArray = JSON.parse(JSON.stringify(me.value.definition));
                    let relationArray = JSON.parse(JSON.stringify(me.value.relation));
                    console.log(me.value)
                    definitionArray.forEach(function (definitionTmp, index) {
                        if (definitionTmp.selected) {
                            selected.push(definitionTmp.elementView.id)
                            definitionArray[index] = null
                        }
                    })
                    definitionArray = definitionArray.filter(n => n)
                    selected.forEach(function (selectedTmp) {
                        relationArray.forEach(function (relation, index) {
                            if (relation.to == selectedTmp || relation.from == selectedTmp) {
                                relationArray[index] = null
                            }
                        })
                    })

                    relationArray = relationArray.filter(n => n)
                    relationArray.forEach(function (relationTmp, index) {
                        if (relationTmp.selected) {
                            relationArray[index] = null
                        }
                    })
                    relationArray = relationArray.filter(n => n)

                    me.value.definition = definitionArray
                    me.value.relation = relationArray
                    this.syncOthers();
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
                        var componentInfo = canvasEl.data('DRAG_SHAPE'),
                            shape, element;
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
                // console.log(edge)
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
                    } else {
                        me.canvas.removeShape(edgeElement, true);
                        //기존 컴포넌트가 없는 경우 신규 생성
                        this.addElement(componentInfo);
                    }
                    this.syncOthers();
                }
            },
            redo: function () {
                var me = this
                if (!me.drawer) {
                    if (me.redoArray.length > 0) {
                        var tmpData = me.redoArray.pop();
                        me.value = JSON.parse(JSON.stringify(tmpData));
                        if (me.undoArray.length == 0 && me.value.length == 0) {
                            me.undoArray.push({
                                'definition': [],
                                'relation': []
                            })
                        }
                        me.undoArray.push(JSON.parse(JSON.stringify(tmpData)));
                        this.syncOthers(JSON.parse(JSON.stringify(tmpData)));
                    } else {
                    }
                }
            },
            undo: function () {
                var me = this;
                if (!me.drawer) {
                    if (me.undoArray.length > 1) {
                        me.redoArray.push(me.undoArray[me.undoArray.length - 1])
                        me.undoArray.pop()
                        me.value = JSON.parse(JSON.stringify(me.undoArray[me.undoArray.length - 1]))
                    } else if (me.undoArray.length == 1) {
                        me.undoArray.pop();
                        // console.log("undo length 0")
                        me.undoArray.push(JSON.parse(JSON.stringify(me.value)))
                        // this.syncOthers(JSON.parse(JSON.stringify(me.value)));
                    } else {
                    }
                }
            },
            addElement: function (componentInfo, newTracingTag, originalData) {
                this.enableHistoryAdd = true;
                var me = this;
                var additionalData = {};

                var vueComponent = me.getComponentByName(componentInfo.component);
                // console.log(componentInfo.component , this.relationVueComponentName)
                var element;

                // console.log(componentInfo)
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
                    me.value = {
                        'definition': [],
                        'relation': []
                    }
                }
                if (element._type == 'org.uengine.uml.model.relation') {
                    me.value['relation'].push(element);
                } else {
                    me.value['definition'].push(element);
                }
                me.undoArray.push(JSON.parse(JSON.stringify(me.value)));
                me.redoArray = [];
                this.syncOthers(element);
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
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        position: absolute;
        overflow: hidden;

        .fullcanvas {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 10%;
            left: 0;
            overflow: hidden;
        }

        .fullcanvashands {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 10%;
            left: 0;
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
        .export,
        .history,
        .import,
        .save {
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
        transition: all 0.3s ease;
        user-select: none;
    }

    .custom-menu li:hover {
        background-color: #DEF;
    }
</style>
