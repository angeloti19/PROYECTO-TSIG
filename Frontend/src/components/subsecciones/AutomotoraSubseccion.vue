<script>
import axios from 'axios';
import { store } from '@/store'
import NuevaSucursalModal from '../modales/NuevaSucursalModal.vue';
import sucursalIcono from '@/components/icons/sucursalIcono.vue'

export default{
    name: 'automotoraSubseccion',
    components: {
        NuevaSucursalModal,
        sucursalIcono
    },
    data(){
        return{
            store,
            sucursales: [],
            autos: [],
            cargandoSucursales: true,
            cargandoAutos: true,
        }
    },
    mounted(){
        this.fetchSucursales()
        this.fetchAutos()
    },
    props:{
        automotoraId : Number
    },
    methods:{
        async fetchSucursales(){
            this.sucursales = []
            const response = await axios.get(import.meta.env.VITE_BACKEND_API + "api/automotora/" + this.automotoraId + "/sucursal")
                .then(function (response) {
                    response.data.forEach(sucursal => {
                        this.sucursales.push({
                            id: sucursal.id,
                            nombre: sucursal.nombre,
                            coordenadas: sucursal.coordenadas,
                        })
                    });
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error);
                }.bind(this));
        },
        async fetchAutos(){
        
        },
        seleccionarSucursal(index){
            console.log("Se selecciono sucursal con index: " + index)
            const coordenadas = this.sucursales[index].coordenadas
            const coord = [coordenadas.x, coordenadas.y]
            this.store.centrarMapaEnCoordenada(coord)
            
        },
        mostrarModalNuevaSucursal(){
            this.$refs.nuevaSucursalModal.abrir()
        }
    },
}
</script>


<template>
    <NuevaSucursalModal ref="nuevaSucursalModal" :automotoraId="automotoraId" @refetch="fetchSucursales"/>
    <div>
        <div class="contenedor-sucursales"> <!-- Sucursales -->
            <div style="display:flex; justify-content: space-around; margin-bottom: 8px;">
                <button class="boton" @click="mostrarModalNuevaSucursal">Nueva sucursal</button>
                <button class="boton">Nuevo auto</button>
            </div>
            
            <p style="font-weight: 600; margin-bottom: 10px"> Sucursales </p>
            <div v-if="sucursales.length == 0" style="text-align:center; margin-top: 15px; margin-bottom: 15px;">
                {{cargandoSucursales ? 'Buscando sucursales...' : 'No hay sucursales'}} <!--Nunca deberia no haber sucursales -->
            </div>
            <template v-for="(sucursal, index) in sucursales">
                <div class="contenedor-sucursal">
                    <div class="titulo-sucursal" @click="seleccionarSucursal(index)">
                        <div style="display: flex;">
                            <sucursalIcono style="margin-right: 12px; width: 19px;"/>
                            <div>
                                {{ sucursal.nombre }}
                            </div>
                        </div>
                    </div>
                </div>
            </template>
        </div>
        <!-- <div class="contenedor-autos">
            <p style="font-weight: 600; margin-bottom: 10px"> Autos </p>
            <div v-if="autos.length == 0" style="text-align:center; margin-top: 15px; margin-bottom: 15px;">
                {{cargandoAutos ? 'Buscando autos...' : 'No hay autos registrados'}}
            </div>
            <template v-for="(auto, index) in autos">
                <div class="contenedor-auto">
                    <div class="titulo-auto" @click="seleccionarAuto(index)">
                        <div>
                            {{ auto.matricula }}
                        </div>
                    </div>
                </div>
            </template>
        </div> -->
        
    </div>
</template>


<style scoped>
.contenedor-sucursal:hover{
    border-left-style: solid;
    border-left-color: rgba(255, 255, 255, 0.574);
    border-left-width: 2px;

}
.titulo-sucursal{
    padding: 10px 20px;
    cursor: pointer;
    display: flex;
    justify-content: space-between;
}
.titulo-sucursal:active{
    background-color: rgba(0, 0, 0, 0.084);
}
.contenedor-sucursales{
    padding: 10px 35px;
}
</style>