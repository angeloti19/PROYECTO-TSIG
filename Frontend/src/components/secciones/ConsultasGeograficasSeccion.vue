<script>
import { store } from '@/store'
import axios from 'axios'
import automotoraIcono from '@/components/icons/automotoraIcono.vue'
export default{
    name: 'consultasGeograficasSeccion',
    components: {
        automotoraIcono
    },
    data(){
        return{
            store,
            automotoras: []
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
        },
        async automotorasConMasAutos(){
            this.automotoras = []
            const response = await axios.get('/api/consultas/top10Automotoras')
                .then(function (response) {
                    response.data.forEach(automotora => {
                        this.automotoras.push({
                            id: automotora.id,
                            nombre: automotora.nombre,
                            cantAutos: automotora.cantAutosTotal
                        })
                    });
                    let filtroSucursales = 'IN ('
                    response.data.forEach(automotora => {
                        automotora.sucursales.forEach(sucursal => {
                            filtroSucursales = filtroSucursales.concat(`'${sucursal.id}',`) 
                        });
                    })
                    
                    if(this.automotoras.length != 0){
                    filtroSucursales = filtroSucursales.substring(0, filtroSucursales.length - 1);
                    }
                    filtroSucursales = filtroSucursales.concat(')')

                    this.store.fetchSucursalesMapa(filtroSucursales)
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error);
                }.bind(this));
        }
    },
}
</script>

<template>
    <div style="padding: 15px 15px;">
        <v-checkbox density="compact" class="ma-0 pa-0" label="Mostrar mapa de calor de autos" v-model="store.mostrandoHeatMapAutos" @change="cambiarMapaCalorAutos"></v-checkbox>
        <v-checkbox density="compact" class="ma-0 pa-0" label="Mostrar zona sin cobertura" v-model="store.mostrandoZonaSinCobertura" @change="cambiarZonaSinCobertura"></v-checkbox>
        <button class="boton" @click="automotorasConMasAutos()">Mostrar automotoras con más autos</button>
        
    
        <div v-if="automotoras.length != 0" style="margin-bottom: 15px;">
            <template v-for="(automotora, index) in automotoras">
                <div class="contenedor-automotora">
                    <div class="titulo-automotora">
                        <div style="display: flex;">
                            <automotoraIcono style="margin-right: 12px; width: 19px;" />
                            <div>
                                {{ automotora.nombre }}
                            </div>
                        </div>
                        <p style="align-self: flex-end;">{{ automotora.cantAutos }}</p>
                    </div>
                </div>
            </template>
        </div>
        
    
        <button class="boton" @click="store.fetchSucursalesMapa('')">Mostrar todas las automotoras</button>
    
    </div>
    
</template>

<style scoped>
.contenedor-automotora {
    border-style: solid;
    border-color: rgba(255, 255, 255, 0.474);
    border-width: 1px;
    border-right: none;
    border-left: none;
    border-top: none;

}

.titulo-automotora {
    padding: 10px 20px;
    cursor: pointer;
    display: flex;
    justify-content: space-between;
}

.titulo-automotora:active {
    background-color: rgba(0, 0, 0, 0.084);
}

</style>