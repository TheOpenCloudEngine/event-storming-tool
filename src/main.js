/* eslint-disable */
import Vue from 'vue';
import i18next from 'i18next';
import router from './router'
import VueI18Next from '@panter/vue-i18next';
import ModelerApp from './ModelerApp.vue';
import App from './App.vue';
import BootstrapVue from 'bootstrap-vue';
import './setup/initialLoad';
import translations from '@/setup/translations.json';
import vuetify from '@/plugins/vuetify'

Vue.use(BootstrapVue);
Vue.use(vuetify);

import * as VueDeepSet from 'vue-deepset';

Vue.use(VueDeepSet);

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';

import '@fortawesome/fontawesome-free/css/all.min.css';

Vue.config.productionTip = false;

Vue.use(VueI18Next);

i18next.init({
    lng: 'en',
    resources: {en: {translation: translations}},
});

const i18n = new VueI18Next(i18next);

new Vue({
    render: h => h(App),
    vuetify,
    router,
    i18n,
}).$mount('#app');
