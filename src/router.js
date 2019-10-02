import Vue from 'vue'
import Router from 'vue-router'
import Designer from './components/Modeler'
// import Designer from './components/mainpage/EventStormingDesigner'
// import EventStormingMain from './components/mainpage/EventStormingMain'
// import ModelingDesigner from './components/mainpage/ModelingDesigner'
// import ModelerImageGenerator from './components/designer/ModelerImageGenerator'

// Vue.component('modeler-image-generator', ModelerImageGenerator);

Vue.use(Router)

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'home',
            component:Designer
        },
        // {
        //     path: '/event',
        //     name: 'EventStormingMain',
        //     component: EventStormingMain
        // },
        // {
        //     path: '/event/:id',
        //     name: 'Designer',
        //     component: Designer
        // }
    ]
})
