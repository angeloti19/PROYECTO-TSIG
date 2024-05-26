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
        }
    },
}
</script>

<template>
    <div style="padding: 15px 15px;">
        Esta es la seccion de inicio de consultas geograficas
        <v-checkbox label="Mostrar mapa de calor de autos" v-model="store.mostrandoHeatMapAutos" @change="cambiarMapaCalorAutos"></v-checkbox>
    </div>
    
</template>