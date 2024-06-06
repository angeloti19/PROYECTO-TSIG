<script>
import axios from 'axios';
import { store } from '@/store';
import ContenedorCerrable from '../ContenedorCerrable.vue'

export default {
    name: 'nuevaAutomotoraModal',
    components: {
        ContenedorCerrable
    },
    data() {
        return {
            store,
            visible: false,
            automotoraCreada: false,
            automotoraNombre: "",
            sucursalNombre: "",
            x: 0,
            y: 0,
            isLoading: false
        }
    },
    emits: ['refetch'],
    methods: {
        cambiarVisibilidad(valor) {
            if (valor) {
                //Cuando se abre el modal
                this.automotoraCreada = false
            }
            this.visible = valor
        },
        abrir(){
            this.resetDatos()
            this.cambiarVisibilidad(true)
        },
        resetDatos(){
            this.automotoraNombre = ""
            this.sucursalNombre = ""
            this.x = 0
            this.y = 0
        },
        async onSubmitCrearAutomotora(){
            if(this.automotoraNombre == "" || this.sucursalNombre == "" || this.x == "" || this.y == ""){
                alert("datos incorrectos")
                return
            }
            this.isLoading = true
            //Hace el pedido
            const data = {
                "nombreAutomotora": this.automotoraNombre,
                "nombreSucursal": this.sucursalNombre,
                "coordenadaSucursal": {
                    "x": this.x,
                    "y": this.y
                },
            }
            const response = await axios.post(import.meta.env.VITE_BACKEND_API + "api/automotora", data)
                .then(async function (response) {
                    this.automotoraCreada = true
                    this.isLoading = false
                    this.store.fetchSucursalesMapa()
                    this.$emit('refetch')
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error.response.data);
                    this.isLoading = false
                }.bind(this));

        },
        marcarUbicacion(){
            //Esta funcion inicia el modo de interaccion para colocar el punto en el mapa
            store.funcionUbicacionSucursal = this.setUbicacion
            store.modoInteraccion = "punto-sucursal"
            this.cambiarVisibilidad(false)
        },
        setUbicacion(coordenada){
            //Esta funcion es llamada cuando se coloca el punto en el mapa
            store.modoInteraccion = undefined
            this.x = coordenada[0]
            this.y = coordenada[1]
            this.cambiarVisibilidad(true)
        }
    },
    props: {
    },
    mounted() {
    }
}
</script>

<template>
    <v-dialog  opacity="0.05" v-model="visible" width="auto" persistent scrollable>
        <template v-if="!automotoraCreada"> <!-- Automotora por agregar -->
            <ContenedorCerrable style="width: 462px;" @cerrar="cambiarVisibilidad(false)">
                <template v-slot:titulo>
                    Nueva automotora
                </template>
                <template v-slot:contenido>
                    <form> <!-- Formulario -->
                        <div class="contenedor-formulario">
                            <label for="automotoraNombre">Nombre de automotora</label>
                            <input v-model="automotoraNombre" name="automotoraNombre" id="automotoraNombre" type="text">
                            <label for="sucursalNombre">Nombre de sucursal</label>
                            <input v-model="sucursalNombre" name="sucursalNombre" id="sucursalNombre" type="text">
                            <label>Ubicación de sucursal</label>
                            <div class="contenedor-form-ubicacion">
                                <div style="display:flex;">
                                    <div>
                                        <label style="font-size: 14px;" for="longitud">Longitud</label>
                                        <input style="width:176px" v-model="x" name="longitud" id="longitud" type="text">
                                    </div>
                                    <div>
                                        <label style="font-size: 14px;" for="latitud">Latitud</label>
                                        <input style="width:177px" v-model="y" name="latitud" id="latitud" type="text">
                                    </div>
                                </div>
                                <button style="width: 253px" type="button" @click="marcarUbicacion" class="boton con-borde">Marcar ubicación en mapa</button>
                            </div>
                            <button :disabled="isLoading" type="button" @click="onSubmitCrearAutomotora" class="boton con-borde primario" style="margin-top: 15px; width: 140px">Confirmar</button>
                        </div>
                    </form>
                </template>
            </ContenedorCerrable>
        </template>
        <template v-else> <!-- Automotora agregada -->
            <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="cambiarVisibilidad(false)">
                <template v-slot:titulo>
                    Automotora agregada
                </template>
                <template v-slot:contenido>
                    <div style="margin-top:15px">
                        <p>Se agregó la automotora exitosamente.</p>
                    </div>
                    <button style="display: block; margin-top: 25px;" @click="cambiarVisibilidad(false)">
                        Cerrar</button>
                </template>
            </ContenedorCerrable>
        </template>
    </v-dialog>
</template>

<style scoped>

.contenedor-form-ubicacion{
    margin-left: 15px; 
    margin-top: 10px; 
    padding-left: 10px; 
    border-left-style: solid; 
    border-left-color: rgba(0, 0, 0, 0.329); 
    border-left-width: 2px;

}


</style>
