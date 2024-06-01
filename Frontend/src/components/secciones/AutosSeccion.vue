<script>
import axios from 'axios'
import { store } from '@/store'
export default{
    name: 'autosSeccion',
    components: {

    },
    data(){
        return{
            store,
            automotoraId: "1000000",
            tipoAuto: "Ambos",
            distMax: 1,
            automotoras: [],
            cargandoAutomotoras: true,
            mostrandoFiltros: false,
            mensajeError: "",
            filtrarDistMax: false,
            buscandoAuto: false

        }
    },
    mounted(){
        this.fetchAutomotoras()
    },
    methods:{
        toggleFiltroVentana(){
            if(this.mostrandoFiltros){
                this.mostrandoFiltros = false
                document.getElementById("filtroVentana").style.bottom = "-340px"
            }else{
                this.mostrandoFiltros = true
                document.getElementById("filtroVentana").style.bottom = "0px"
            }
            
        },
        async fetchAutomotoras() {
            this.cargandoAutomotoras = true
            this.automotoras = []
            const response = await axios.get(import.meta.env.VITE_BACKEND_API + "api/automotora")
                .then(function (response) {
                    response.data.forEach(automotora => {
                        this.automotoras.push({
                            id: automotora.id,
                            nombre: automotora.nombre
                        })
                    });
                    this.cargandoAutomotoras = false
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error.response.data);
                    this.cargandoAutomotoras = false
                }.bind(this));
        },
        async solicitarAuto(){
            let filtraPorAutomotora = true
            if(this.automotoraId == "1000000"){
                filtraPorAutomotora = false
            }
            if(this.store.puntoSolicitud == undefined){
                this.mensajeError = "Primero seleccione un punto de solicitud"
                return
            }
            if(this.store.puntoDestino == undefined){
                this.mensajeError = "Primero seleccione un punto de destino"
                return
            }
            //Verificacion frontend completa
            this.mensajeError = ""
            let url = import.meta.env.VITE_BACKEND_API + "api/autoSolicitud?ptoSolicitud=POINT(" + this.store.puntoSolicitud[0] + " " + this.store.puntoSolicitud[1] + ")&ptoDestino=POINT(" + this.store.puntoDestino[0] + " " + this.store.puntoDestino[1] + ")" 
            if(filtraPorAutomotora){
                url = url.concat("&idAutomotora=" + this.automotoraId)
            }
            if(this.tipoAuto != "Ambos"){
                if(this.tipoAuto == "Combustion"){
                    url = url.concat("&electrico=false")
                }else{
                    url = url.concat("&electrico=true")
                }
            }
            if(this.filtrarDistMax){
                url = url.concat("&distMax=" + this.distMax)
            }
            this.buscandoAuto = true
            const response = await axios.get(url)
                .then(function (response) {
                    console.log(response)
                    console.log(response.data)
                    this.buscandoAuto = false
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error.response.data);
                    this.buscandoAuto = false
                }.bind(this));

            
        }
    },
}
</script>

<template>
    <div style="padding: 15px 15px;">
        <div style="display:flex; align-items: center; margin-bottom: 15px;">
            <button @click="solicitarAuto" class="boton blanco con-borde" style="width: 100%;">Solicitar auto</button> <v-icon @click="toggleFiltroVentana()" class="filtro">mdi-filter</v-icon>
        </div>
        <!-- <p>Se solicitará el auto más cercano al punto de solicitud que pueda dejarse en el punto de destino, y que encaje con los filtros que proporcione.</p> -->
        <p style="font-size: 13px; margin-top:10px">{{ mensajeError }}</p>
        <!-- <p v-if="false">No se encontró ningún auto apropiado para su solicitud, pruebe usar distintos filtros o cambiar el punto de solicitud o destino.</p> -->
        <!-- {{ automotoraId }}
        {{ tipoAuto }}
        {{ store.puntoDestino }} -->
    </div>
    <div class="filtroVentana" id="filtroVentana">
        <p style="font-weight: 600; margin-bottom: 8px">Filtros</p>
        <div style="display: flex; flex-direction: column;">
            <label for="automotora">Automotora</label>
            <select class="sobre-blanco" v-model="automotoraId" name="automotora" id="automotora">
                <option value=1000000>Cualquier automotora</option>
                <option v-for="automotora in automotoras" :value="automotora.id">{{automotora.nombre}}</option>
            </select>
        </div>
        <div style="font-size: 13px; color: gray;" v-if="automotoras.length == 0">{{ cargandoAutomotoras ? 'Buscando automotoras...' : 'No hay automotoras' }}</div>
        <label for="tipoAuto">Tipo de auto</label>
        <div style="display:flex; justify-content: space-evenly; margin-top:5px">
            <div>
                <input type="radio" id="combustion" value="Combustion" v-model="tipoAuto" />
                <label for="combustion" style="margin-left: 5px;">Combustión</label>
            </div>
            <div>
                <input type="radio" id="electrico" value="Electrico" v-model="tipoAuto" />
                <label for="electrico" style="margin-left: 5px;">Eléctrico</label>
            </div>
            <div>
                <input type="radio" id="ambos" value="Ambos" v-model="tipoAuto" />
                <label for="ambos" style="margin-left: 5px;">Ambos</label>
            </div>
        </div>
        <v-checkbox color="#26A099" density="compact" class="ma-0 pa-0" label="Filtrar por distancia máxima" v-model="filtrarDistMax"></v-checkbox>
        <div :class="{ 'desactivado': !filtrarDistMax }" style="display: flex; flex-direction: column;">
            <label for="distMax">Distancia máxima</label>
            <input :disabled="filtrarDistMax ? false : true" v-model="distMax" name="distMax" id="distMax" type="number" min="1">
        </div>
    </div>
</template>

<style scoped>
.filtro{
    margin-left: 10px;
    margin-right: 4px;
    opacity: 0.7;
    cursor: pointer;
}
.filtro:hover{
    opacity: 1;
}

.filtroVentana{
    background-color: white;
    position: absolute;
    color: black;
    bottom: -340px;
    width: calc(35.86% - 57px);
    outline-style: solid;
    outline-color: rgba(0, 0, 0, 0.315);
    outline-width: 2px;
    border-top-right-radius: 15px;
    border-top-left-radius: 15px;
    padding: 15px;
    transition: bottom 1s;
}

.desactivado{
    color: rgba(0, 0, 0, 0.378);
}

</style>