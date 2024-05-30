<script>
import axios from 'axios';
import ContenedorCerrable from '../ContenedorCerrable.vue'

export default {
    name: 'alertModal',
    components: {
        ContenedorCerrable
    },
    data() {
        return {
            visible: false,
            mensaje: "mensaje",
            titulo: "titulo"
        }
    },
    emits: ['onConfirm'],
    methods: {
        cambiarVisibilidad(valor) {
            if (valor) {
                //Cuando se abre el modal
            }
            this.visible = valor
        },
        abrir(){
            this.cambiarVisibilidad(true)
        },
        setContenido(titulo, mensaje){
            this.titulo = titulo
            this.mensaje = mensaje
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
        <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="cambiarVisibilidad(false)">
            <template v-slot:titulo>
                {{titulo}}
            </template>
            <template v-slot:contenido>
                <div style="margin-top:15px">
                    <p>{{ mensaje }}</p>
                </div>
                <div style="display:flex; justify-content:space-between">
                    <div></div>
                    <button class="boton con-borde" style="display: block; margin-top: 25px;" @click="cambiarVisibilidad(false)">
                    Ok</button>
                </div>
                
            </template>
        </ContenedorCerrable>
    </v-dialog>
</template>

<style scoped>
</style>
