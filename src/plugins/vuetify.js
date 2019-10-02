import Vue from 'vue';
import Vuetify, {
    VApp, VAppBar,
    VNavigationDrawer,
    VList,
    VContent,
    VTextField,
    VIcon,
    VToolbar,
    VToolbarTitle,
    VToolbarItems,
    VInput,
    VRow,
    VAppBarNavIcon,
    VListItemAction,
    VListItemTitle,
    VListItemActionText,
    VListItem,
    VListItemContent
} from 'vuetify/lib';

Vue.use(Vuetify, {
    components: {
        VApp,
        VAppBar,
        VNavigationDrawer,
        VList,
        VContent,
        VTextField,
        VIcon,
        VToolbar,
        VToolbarTitle,
        VToolbarItems,
        VInput,
        VRow,
        VAppBarNavIcon,
        VListItemAction,
        VListItemTitle,
        VListItemActionText,
        VListItem,
        VListItemContent
    }
});

export default new Vuetify({
    icons: {
        iconfont: 'mdi',
    },
});
