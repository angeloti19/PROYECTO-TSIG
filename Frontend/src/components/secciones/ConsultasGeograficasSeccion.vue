<script>
import { store } from '@/store'
export default{
    name: 'consultasGeograficasSeccion',
    components: {

    },
    data(){
        return{
            store
        }
    },
    mounted(){
    },
    methods:{
        cambiarMapaCalorAutos(){
            if(store.mostrandoHeatMapAutos){
                store.agregarMapaCalorAutos()
            }else{
                store.quitarMapaCalorAutors()
            }
        },
        cambiarZonaSinCobertura(){
            if(store.mostrandoZonaSinCobertura){
                store.agregarZonaSinCobertura()
            }else{
                store.quitarZonaSinCobertura()
            }
        }
    },
}
</script>

<template>
    <div style="padding: 15px 15px;">
        <v-checkbox density="compact" class="ma-0 pa-0" label="Mostrar mapa de calor de autos" v-model="store.mostrandoHeatMapAutos" @change="cambiarMapaCalorAutos"></v-checkbox>
        <v-checkbox density="compact" class="ma-0 pa-0" label="Mostrar zona sin cobertura" v-model="store.mostrandoZonaSinCobertura" @change="cambiarZonaSinCobertura"></v-checkbox>
    </div>
    
</template>