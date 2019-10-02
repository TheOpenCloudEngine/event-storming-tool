<template>
  <v-app id="app">
    <v-navigation-drawer
            v-model="drawer"
            app
            clipped
    >
      <v-list dense>
        <v-list-item
                v-for="item in items"
                :key="item.text"
                :to="item.router"
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              {{ item.text }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <!--<v-subheader class="mt-4 grey&#45;&#45;text text&#45;&#45;darken-1">SUBSCRIPTIONS</v-subheader>-->
      </v-list>
    </v-navigation-drawer>

    <v-app-bar
            app
            clipped-left
            color="red"
            dense
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-icon class="mx-4">fas fa-sticky-note</v-icon>
      <v-toolbar-title class="mr-12 align-center">
        <span class="title">EventStomring</span>
      </v-toolbar-title>
      <div class="flex-grow-1"></div>
      <v-row
              align="center"
              style="max-width: 650px"
      >
        <v-text-field
                :append-icon-cb="() => {}"
                placeholder="Search..."
                single-line
                append-icon="search"
                color="white"
                hide-details
        ></v-text-field>
      </v-row>
    </v-app-bar>

    <v-content>
        <router-view />
    </v-content>
  </v-app>
</template>

<script>
    export default {
        props: {
            source: String,
        },
        data: () => ({
        drawer: null,
        items: [
            { icon: 'fa fa-book', text: 'Introduce', router: '/'},
            { icon: 'fa fa-sticky-note', text: 'EventStorming', router: '/event'},
        ],
    }),
    created () {
        this.$vuetify.theme.dark = true
    },
    }
</script>
