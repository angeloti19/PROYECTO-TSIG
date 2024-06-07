<script>
import Mapa from '@/components/Mapa.vue'
import PanelInformacion from '@/components/PanelInformacion.vue';
import { store } from '@/store';

export default{
    name: 'home',
    components: {
        Mapa,
        PanelInformacion
    },
    data(){
        return{
            store
        }
    },
    mounted(){

    },
}
</script>

<template>
    <div class="contenedor">
        <div class="seccion-mapa" :class="{ 'mapa-completo': store.mapaCompleto }">
            <Mapa :key="store.tipoUsuario"/>
        </div>
        <div class="seccion-info" :class="{ 'mapa-completo': store.mapaCompleto }">
           <PanelInformacion/>
        </div>
    </div>
    
    
</template>

<style scoped>

.contenedor{
    display: flex;
    justify-content: space-between;
    height: 100%;
}

.seccion-mapa{
    flex-basis: 64%;
}
.seccion-mapa.mapa-completo{
    flex-basis: 100%;
}

.seccion-info{
    flex-basis: 36%;
}

.seccion-info.mapa-completo{
    display: none;
}


</style>