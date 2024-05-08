<script>
import { RouterLink, RouterView } from 'vue-router'
import { store } from './store'

export default {
  name: 'app',
  components: {
    
  },
  data(){
    return{
      store
    }
  },
  mounted(){
    if (localStorage.tipoUsuario) {
      this.store.tipoUsuario = localStorage.tipoUsuario
    } else {
      localStorage.setItem("tipoUsuario", "anonimo")
      this.store.tipoUsuario = "anonimo"
    }
  }
}

</script>

<template>
    <RouterView />
</template>

<style scoped>

</style>
