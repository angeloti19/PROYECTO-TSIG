<script>
import axios from 'axios';
import AutomotoraSubseccion from '@/components/subsecciones/AutomotoraSubseccion.vue'
import automotoraIcono from '@/components/icons/automotoraIcono.vue'
import NuevaAutomotoraModal from '../modales/NuevaAutomotoraModal.vue';

export default{
    name: 'automotorasSeccion',
    components: {
        AutomotoraSubseccion,
        automotoraIcono,
        NuevaAutomotoraModal
    },
    data(){
        return{
            automotoras: [],
            automotoraSeleccionada: null,
            cargando: true
        }
    },
    mounted(){
        this.fetchAutomotoras()
    },
    methods:{
        async fetchAutomotoras(){
            this.automotoras = []
            const response = await axios.get(import.meta.env.VITE_BACKEND_API + "api/automotora")
                .then(function (response) {
                    response.data.forEach(automotora => {
                        this.automotoras.push({
                            id: automotora.id,
                            nombre: automotora.nombre
                        })
                    });
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error.response.data);
                }.bind(this));
        },
        mostrarModalNuevaAutomotora(){
            this.$refs.nuevaAutomotoraModal.abrir()
        }
    },
}
</script>

<template>
    <NuevaAutomotoraModal ref="nuevaAutomotoraModal" @refetch="fetchAutomotoras"/>
    <div class="boton-agregar-automotora">
        <div style="display: flex; flex-direction: column; align-items: center;">
            <button class="boton blanco con-borde" @click="mostrarModalNuevaAutomotora">Nueva automotora</button>
            <div class="handle"></div>
        </div>
        
    </div>
    
    <div v-if="automotoras.length == 0" style="text-align:center; margin-top: 80px">
        {{cargando ? 'Buscando automotoras...' : 'No hay automotoras'}}
    </div>
    <template v-for="(automotora, index) in automotoras">
        <div class="contenedor-automotora">
            <div class="titulo-automotora" @click="automotoraSeleccionada == index ? automotoraSeleccionada = null : automotoraSeleccionada = index">
                <div style="display: flex;">
                    <automotoraIcono style="margin-right: 12px; width: 19px;"/>
                    <div>
                        {{ automotora.nombre }}
                    </div>
                </div>
                <v-icon style="align-self: flex-end;" :icon="automotoraSeleccionada == index ? 'mdi-chevron-up' : 'mdi-chevron-down'"></v-icon>
            </div>
            <v-expand-transition>
                <div v-if="automotoraSeleccionada == index">
                    <v-divider opacity="0.3" thickness="2" style="margin: 0 10px;"/>
                    <!-- Aqui va componente de automotora -->
                    <AutomotoraSubseccion :automotoraId="automotora.id"/>
                </div>
            </v-expand-transition>
        </div>

    </template>
    
</template>

<style scoped>
.contenedor-automotora{
    border-style: solid;
    border-color: rgba(255, 255, 255, 0.474);
    border-width: 1px;
    border-right: none;
    border-left: none;
    border-top: none;

}
.titulo-automotora{
    padding: 10px 20px;
    cursor: pointer;
    display: flex;
    justify-content: space-between;
}
.titulo-automotora:active{
    background-color: rgba(0, 0, 0, 0.084);
}

.boton-agregar-automotora{
    position: absolute;
    top: -64px;
    margin-left: 24%;
    transition: top 0.5s;
    padding-top: 20px;

}
.boton-agregar-automotora:hover{
    position: absolute;
    top: -30px;
    margin-left: 24%;
}
.boton-agregar-automotora .boton{
    margin-bottom: 0;
    border-top-right-radius: 0;
    border-top-left-radius: 0;

}
.handle{
    width: 0; 
    height: 0; 
    border-left: 18px solid transparent;
    border-right: 18px solid transparent;
    
    border-top: 8px solid rgba(255, 255, 255, 0.847);
    cursor: pointer;
    transition: border-top 0.5s;
}

.boton-agregar-automotora:hover .handle{
    border-top: 0px solid rgba(255, 255, 255, 0.847);
}
</style>