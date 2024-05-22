<script>
import axios from 'axios';
import { store } from '@/store';
import ContenedorCerrable from '../ContenedorCerrable.vue'

export default {
    name: 'nuevaSucursalModal',
    components: {
        ContenedorCerrable
    },
    data() {
        return {
            store,
            visible: false,
            autoCreado: false,
            matricula: "",
            distMax: 0,
            tipoAuto: "Combustion",
            recorrido: [
                {x: 0, y: 0}, //Mostrar solo el primero y final?
                {x: 1, y: 1},
                {x: 2, y: 3},
            ],
            isLoading: false,
            mostrarCoordenadas: false
        }
    },
    emits: ['refetch'],
    methods: {
        cambiarVisibilidad(valor) {
            if (valor) {
                //Cuando se abre el modal
                this.autoCreado = false
            }
            this.visible = valor
        },
        abrir(){
            this.resetDatos()
            this.cambiarVisibilidad(true)
        },
        resetDatos(){
            this.matricula = ""
            this.distMax = 0
            this.tipoAuto = "Combustion"
            this.x = 0
            this.y = 0
            this.recorrido = [
                {x: 0, y: 0},
                {x: 1, y: 1},
                {x: 2, y: 3},
            ]
            this.mostrarCoordenadas = false
        },
        async onSubmitCrearAuto(){
            if(this.matricula == "" || this.recorrido.length == 0 || this.distMax <= 0){
                alert("datos incorrectos")
                return
            }
            this.isLoading = true
            //Hace el pedido
            let electrico = true
            if(this.tipoAuto == "Combustion"){
                electrico = false
            }
            const data = {
                "matricula": this.matricula,
                "dist_max": this.distMax,
                "electrico": electrico,
                "recorrido": this.recorrido
            }
            const response = await axios.post(import.meta.env.VITE_BACKEND_API + "api/automotora/" + this.automotoraId + "/auto", data)
                .then(async function (response) {
                    this.autoCreado = true
                    this.isLoading = false
                    //this.store.fetchAutosMapa()  hacer esto luego
                    this.$emit('refetch')
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error);
                    this.isLoading = false
                }.bind(this));

        },
        marcarRecorrido(){
            //Esta funcion inicia el modo de interaccion para colocar el recorrido en el mapa
            store.funcionRecorridoAuto = this.setRecorrido
            store.modoInteraccion = "recorrido-auto"
            store.agregarInteraccion("LineString")
            this.cambiarVisibilidad(false)
        },
        setRecorrido(recorrido){
            //Esta funcion es llamada cuando se coloca el recorrido en el mapa
            //Recorrido viene en un array, cada 2 valores es una coordenada
            let recorridoFormateado = []
            for (let i = 0; i < recorrido.length; i += 2) {
                recorridoFormateado.push({
                    x: recorrido[i],
                    y: recorrido[i + 1]
                })
            }
            store.modoInteraccion = "punto-solicitud"
            store.agregarInteraccion("Point")
            this.recorrido = recorridoFormateado
            this.cambiarVisibilidad(true)
        }
    },
    props: {
        automotoraId : Number
    },
    mounted() {
    }
}
</script>

<template>
    <v-dialog  opacity="0.05" v-model="visible" width="auto" persistent scrollable>
        <template v-if="!autoCreado"> <!-- Auto por agregar -->
            <ContenedorCerrable style="width: 462px;" @cerrar="cambiarVisibilidad(false)">
                <template v-slot:titulo>
                    Nuevo auto
                </template>
                <template v-slot:contenido>
                    <form> <!-- Formulario -->
                        <div class="contenedor-formulario">
                            <label for="matricula">Matricula</label>
                            <input v-model="matricula" name="matricula" id="matricula" type="text">
                            <label for="distMax">Distancia máxima de desvío (m)</label>
                            <input v-model="distMax" name="distMax" id="distMax" type="number" min="1">

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
                            </div>

                            <label>Recorrido</label>
                            <div class="contenedor-form-recorrido">
                                <button type="button" style="text-decoration: underline;" @click="mostrarCoordenadas = true" v-show="!mostrarCoordenadas">Mostrar coordenadas</button>
                                <button type="button" style="text-decoration: underline;" @click="mostrarCoordenadas = false" v-show="mostrarCoordenadas">Ocultar coordenadas</button>
                                <v-expand-transition>
                                    <div class="contenedor-recorrido" v-show="mostrarCoordenadas">
                                        <template v-for="(coord, index) in recorrido">
                                            <div style="display:flex; ">
                                                <p style="margin-right: 15px; color: gray;">{{ index + 1 }}.</p>
                                                <div>
                                                    <label style="font-size: 14px;" for="longitud">Longitud</label>
                                                    <input style="width:126px" v-model="coord.x" name="longitud" id="longitud" type="number">
                                                </div>
                                                <div>
                                                    <label style="font-size: 14px;" for="latitud">Latitud</label>
                                                    <input style="width:126px" v-model="coord.y" name="latitud" id="latitud" type="number">
                                                </div>
                                            </div>
                                        </template>
                                    </div>
                                </v-expand-transition>
                                <button style="width: 253px" type="button" @click="marcarRecorrido" class="boton con-borde">Marcar recorrido en mapa</button>
                            </div>
                            <button :disabled="isLoading" type="button" @click="onSubmitCrearAuto" class="boton con-borde primario" style="margin-top: 15px; width: 140px">Confirmar</button>
                        </div>
                    </form>
                </template>
            </ContenedorCerrable>
        </template>
        <template v-else> <!-- Auto agregado -->
            <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="cambiarVisibilidad(false)">
                <template v-slot:titulo>
                    Auto agregado
                </template>
                <template v-slot:contenido>
                    <div style="margin-top:15px">
                        <p>Se agregó el auto exitosamente.</p>
                    </div>
                    <button style="display: block; margin-top: 25px;" @click="cambiarVisibilidad(false)">
                        Cerrar</button>
                </template>
            </ContenedorCerrable>
        </template>
    </v-dialog>
</template>

<style scoped>

.contenedor-form-recorrido{
    margin-left: 15px; 
    margin-top: 10px; 
    padding-left: 10px; 
    border-left-style: solid; 
    border-left-color: rgba(0, 0, 0, 0.329); 
    border-left-width: 2px;
}

.contenedor-recorrido{
    height: 140px;
    overflow-y: auto;
    overflow-x: hidden;
    border-style: solid;
    border-color: gray;
    border-width: 1px;
    margin: 5px;
    padding: 6px;
}


</style>
