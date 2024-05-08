<script>
import Mapa from '@/components/Mapa.vue'
import PanelInformacion from '@/components/PanelInformacion.vue';

export default{
    name: 'home',
    components: {
        Mapa,
        PanelInformacion
    },
    mounted(){

    },
}
</script>

<template>
    <div class="contenedor">
        <div class="seccion-mapa">
            <Mapa/>
        </div>
        <div class="seccion-info">
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

.seccion-info{
    flex-basis: 36%;
}

</style>