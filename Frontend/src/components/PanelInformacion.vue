<script>

import AdminAutosSeccion from '@/components/secciones/AdminAutosSeccion.vue'
import AdminPerfilSeccion from '@/components/secciones/AdminPerfilSeccion.vue'
import AutomotorasSeccion from '@/components/secciones/AutomotorasSeccion.vue'
import ConsultasGeograficasSeccion from '@/components/secciones/ConsultasGeograficasSeccion.vue'
import IniciarSesionSeccion from '@/components/secciones/IniciarSesionSeccion.vue'
import UsuarioAutosSeccion from '@/components/secciones/UsuarioAutosSeccion.vue'
import DesarrolloSeccion from '@/components/secciones/DesarrolloSeccion.vue'
import BienvenidaSeccion from '@/components/secciones/BienvenidaSeccion.vue'
import { store } from '@/store'

export default{
    name: 'panelInformacion',
    components: {
        AdminAutosSeccion,
        AdminPerfilSeccion,
        AutomotorasSeccion,
        ConsultasGeograficasSeccion,
        IniciarSesionSeccion,
        UsuarioAutosSeccion,
        DesarrolloSeccion,
        BienvenidaSeccion
    },
    data(){
        return{
            store,
            secciones: [
                            {titulo: 'Perfil',
                            tooltip: 'Opciones de perfil',
                            icono: 'mdi-account-circle',
                            id: 'perfil',
                            tipoUsuario: 'admin'
                            },
                            {titulo: 'Inicio de sesion',
                            tooltip: 'Ingresar como administrador',
                            icono: 'mdi-account-circle',
                            id: 'iniciar-sesion',
                            tipoUsuario: 'anonimo'
                            },
                            {titulo: 'Automotoras y sucursales',
                            tooltip: 'Automotoras y sucursales',
                            icono: 'mdi-office-building',
                            id: 'automotoras',
                            tipoUsuario: 'admin'
                            },
                            {titulo: 'Autos',
                            tooltip: 'Autos',
                            icono: 'mdi-car-side',
                            id: 'autos',
                            tipoUsuario: 'admin'
                            },
                            {titulo: 'Consultas geográficas',
                            tooltip: 'Consultas geográficas',
                            icono: 'mdi-earth',
                            id: 'consultas',
                            tipoUsuario: 'admin'
                            },
                            {titulo: 'Datos de desarrollo',
                            tooltip: 'Datos de desarrollo',
                            icono: 'mdi-emoticon',
                            id: 'desarrollo',
                            tipoUsuario: 'ambos'
                            },
                            {titulo: 'Sobre RentYou',
                            tooltip: 'Sobre RentYou',
                            icono: 'mdi-information-outline',
                            id: 'bienvenida',
                            tipoUsuario: 'anonimo'
                            }
                       ]
        }
    },
    mounted(){
    },
    methods:{
        getSeccionPorId(id){
            const secc = this.secciones.find((seccion) => seccion.id == id);
            return secc
        },
        esUsuarioCorrecto(tipoUsuario){
            if(tipoUsuario == "ambos"){
                return true
            }
            if(tipoUsuario == this.store.tipoUsuario){
                return true
            }
            return false
        },
        
    },
    computed:{
        obtenerColorUsuario(){
            if(this.store.tipoUsuario == "anonimo"){
                return "#26A099"
            }else{
                return "#FF3A69"
            }
        }
    }
}
</script>




<template>
    <div class="base" :style="{ 'background-color': obtenerColorUsuario}">
        <div class="seccion-contenido">
            <div class="title-bar">
                <span>{{getSeccionPorId(store.seccionActual).titulo}}</span>
            </div>
            <div class="cuerpo">
                <AdminAutosSeccion class="seccion" v-if="store.seccionActual == 'autos'"/>
                <AdminPerfilSeccion class="seccion"  v-if="store.seccionActual == 'perfil'"/>
                <AutomotorasSeccion class="seccion" v-if="store.seccionActual == 'automotoras'"/>
                <ConsultasGeograficasSeccion class="seccion" v-if="store.seccionActual == 'consultas'"/>
                <IniciarSesionSeccion class="seccion" v-if="store.seccionActual == 'iniciar-sesion'"/>
                <UsuarioAutosSeccion class="seccion" v-if="store.seccionActual == 'autos-usuario'"/>
                <DesarrolloSeccion class="seccion" v-if="store.seccionActual == 'desarrollo'"/>
                
            </div>
            <BienvenidaSeccion v-if="store.seccionActual == 'bienvenida'"/>
            
        </div>
        <div class="side-bar" >
            <v-layout>
                <v-navigation-drawer
                style="border-left-width: 2px; border-left-color: rgba(255, 255, 255, 0.466);" :color="obtenerColorUsuario"
                    location="right" rail permanent>
                    <v-list v-model="store.seccionActual" density="compact" nav>
                        <template v-for="seccion in secciones">
                            <v-list-item  v-if="esUsuarioCorrecto(seccion.tipoUsuario)" @click="store.seccionActual = seccion.id" :prepend-icon="seccion.icono" :value="seccion.id">
                                <v-tooltip open-delay="400" activator="parent" location="start">{{ seccion.tooltip }}</v-tooltip>
                            </v-list-item>
                        </template>
                        
                    </v-list>
                </v-navigation-drawer>
            </v-layout>

        </div>
    </div>
</template>




<style scoped>
.base {
    background-color: #26A099;
    height: 100%;
    width: 100%;
    color: white;
    border-left-width: 2px;
    border-left-color: rgba(31, 34, 81, 0.258);
    border-left-style: solid;
}

.title-bar{
    border-bottom-width: 2px;
    border-bottom-color: rgba(255, 255, 255, 0.466);
    border-bottom-style: solid;
    padding: 12px 78px 12px 20px;
    font-weight: 600;
}

.cuerpo{
    padding: 0px 78px 0px 20px;
}

.seccion{
    padding-top: 12px;
}
</style>