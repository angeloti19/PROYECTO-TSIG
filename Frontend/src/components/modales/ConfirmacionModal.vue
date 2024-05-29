<script>
import axios from 'axios';
import ContenedorCerrable from '../ContenedorCerrable.vue'

export default {
    name: 'nuevaAutomotoraModal',
    components: {
        ContenedorCerrable
    },
    data() {
        return {
            visible: false
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
        }
    },
    props: {
        mensaje : String
    },
    mounted() {
    }
}
</script>

<template>
    <v-dialog  opacity="0.05" v-model="visible" width="auto" persistent scrollable>
        <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="cambiarVisibilidad(false)">
            <template v-slot:titulo>
                Confirmar
            </template>
            <template v-slot:contenido>
                <div style="margin-top:15px">
                    <p>{{ mensaje }}</p>
                </div>
                <div style="display:flex; justify-content:space-between">
                    <button class="boton con-borde" style="display: block; margin-top: 25px;" @click="cambiarVisibilidad(false)">
                    Cancelar</button>
                    <button class="boton primario" style="display: block; margin-top: 25px;" @click="$emit('onConfirm');cambiarVisibilidad(false);">
                        Confirmar</button>
                </div>
                
            </template>
        </ContenedorCerrable>
    </v-dialog>
</template>

<style scoped>
</style>
