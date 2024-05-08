import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import OpenLayersMap from "vue3-openlayers";

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { aliases, mdi } from 'vuetify/iconsets/mdi'

const vuetify = createVuetify({
    components,
    directives,
    icons: {
      defaultSet: 'mdi',
      aliases,
      sets: {
        mdi,
      },
    },
  })


const app = createApp(App)

app.use(router)
app.use(OpenLayersMap) //Se pueden pasar opciones
app.use(vuetify)

app.mount('#app')
