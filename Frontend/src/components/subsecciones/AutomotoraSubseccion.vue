<script>
import axios from 'axios';
import { store } from '@/store'
import NuevaSucursalModal from '../modales/NuevaSucursalModal.vue';
import NuevoAutoModal from '../modales/NuevoAutoModal.vue';
import sucursalIcono from '@/components/icons/sucursalIcono.vue'
import autoIcono from '@/components/icons/autoIcono.vue'

export default{
    name: 'automotoraSubseccion',
    components: {
        NuevaSucursalModal,
        sucursalIcono,
        NuevoAutoModal,
        autoIcono
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
        this.store.fetchSucursalesMapa(`automotora_id = '${this.automotoraId}'`)
        this.store.fetchAutosMapa(`automotora_id = '${this.automotoraId}'`)
    },
    unmounted(){
        this.store.fetchSucursalesMapa("")
        this.store.fetchAutosMapa("")
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
                    this.cargandoSucursales = false
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error);
                    this.cargandoSucursales = false
                }.bind(this));
        },
        async fetchAutos(){
            this.autos = []
            const response = await axios.get(import.meta.env.VITE_BACKEND_API + "api/automotora/" + this.automotoraId + "/auto")
                .then(function (response) {
                    response.data.forEach(auto => {
                        this.autos.push({
                            "matricula": auto.matricula,
                            "dist_max": auto.dist_max,
                            "electrico": auto.electrico,
                            "idAutomotora": auto.idAutomotora,
                            "recorrido": auto.recorrido
                        })
                    });
                    this.cargandoAutos = false
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error);
                    this.cargandoAutos = false
                }.bind(this));
        },
        seleccionarSucursal(index){
            console.log("Se selecciono sucursal con index: " + index)
            const coordenadas = this.sucursales[index].coordenadas
            const coord = [coordenadas.x, coordenadas.y]
            this.store.centrarMapaEnCoordenada(coord)
            
        },
        seleccionarAuto(index){
            console.log("Se selecciono auto con index: " + index)
            const coordenadas = this.autos[index].recorrido[0]
            const coord = [coordenadas.x, coordenadas.y]
            this.store.centrarMapaEnCoordenada(coord)
            
        },
        mostrarModalNuevaSucursal(){
            this.$refs.nuevaSucursalModal.abrir()
        },
        mostrarModalNuevoAuto(){
            this.$refs.nuevoAutoModal.abrir()
        }
    },
}
</script>


<template>
    <NuevaSucursalModal ref="nuevaSucursalModal" :automotoraId="automotoraId" @refetch="fetchSucursales"/>
    <NuevoAutoModal ref="nuevoAutoModal" :automotoraId="automotoraId" @refetch="fetchAutos"/>
    <div>
        <div class="contenedor-sucursales"> <!-- Sucursales -->
            <div style="display:flex; justify-content: space-around; margin-bottom: 8px;">
                <button class="boton" @click="mostrarModalNuevaSucursal">Nueva sucursal</button>
                <button class="boton" @click="mostrarModalNuevoAuto">Nuevo auto</button>
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
        <div class="contenedor-autos">
            <p style="font-weight: 600; margin-bottom: 10px"> Autos </p>
            <div v-if="autos.length == 0" style="text-align:center; margin-top: 15px; margin-bottom: 15px;">
                {{cargandoAutos ? 'Buscando autos...' : 'No hay autos registrados'}}
            </div>
            <template v-for="(auto, index) in autos">
                <div class="contenedor-auto">
                    <div class="titulo-auto" @click="seleccionarAuto(index)">
                        <div style="display: flex;">
                            <autoIcono style="margin-right: 12px; width: 25px; margin-top:3px"/>
                            <div>
                                {{ auto.matricula }}
                            </div>
                        </div>
                    </div>
                </div>
            </template>
        </div>
        
    </div>
</template>


<style scoped>
.contenedor-sucursal:hover{
    border-left-style: solid;
    border-left-color: rgba(255, 255, 255, 0.574);
    border-left-width: 2px;

}
.contenedor-auto:hover{
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
.titulo-auto{
    padding: 10px 20px;
    cursor: pointer;
    display: flex;
    justify-content: space-between;
}
.titulo-auto:active{
    background-color: rgba(0, 0, 0, 0.084);
}
.contenedor-sucursales{
    padding: 10px 35px;
}
.contenedor-autos{
    padding: 10px 35px;
}
</style>