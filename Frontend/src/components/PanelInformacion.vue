<script>

import AutosSeccion from '@/components/secciones/AutosSeccion.vue'
import AdminPerfilSeccion from '@/components/secciones/AdminPerfilSeccion.vue'
import AutomotorasSeccion from '@/components/secciones/AutomotorasSeccion.vue'
import ConsultasGeograficasSeccion from '@/components/secciones/ConsultasGeograficasSeccion.vue'
import IniciarSesionSeccion from '@/components/secciones/IniciarSesionSeccion.vue'
import DesarrolloSeccion from '@/components/secciones/DesarrolloSeccion.vue'
import BienvenidaSeccion from '@/components/secciones/BienvenidaSeccion.vue'
import { store } from '@/store'

export default{
    name: 'panelInformacion',
    components: {
        AutosSeccion,
        AdminPerfilSeccion,
        AutomotorasSeccion,
        ConsultasGeograficasSeccion,
        IniciarSesionSeccion,
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
                            {titulo: 'Automotoras',
                            tooltip: 'Automotoras',
                            icono: 'mdi-office-building',
                            id: 'automotoras',
                            tipoUsuario: 'admin'
                            },
                            {titulo: 'Autos disponibles',
                            tooltip: 'Autos',
                            icono: 'mdi-car-side',
                            id: 'autos',
                            tipoUsuario: 'anonimo'
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
        <div style="height:100%" v-show="store.modoInteraccion == 'punto-solicitud' || store.modoInteraccion == 'normal'">
            <div class="seccion-contenido">
                <div class="title-bar">
                    <span>{{getSeccionPorId(store.seccionActual).titulo}}</span>
                </div>
                <div class="cuerpo">
                    <AutosSeccion class="seccion" v-if="store.seccionActual == 'autos'"/>
                    <AdminPerfilSeccion class="seccion"  v-if="store.seccionActual == 'perfil'"/>
                    <AutomotorasSeccion class="seccion" v-if="store.seccionActual == 'automotoras'"/>
                    <ConsultasGeograficasSeccion class="seccion" v-if="store.seccionActual == 'consultas'"/>
                    <IniciarSesionSeccion class="seccion" v-if="store.seccionActual == 'iniciar-sesion'"/>
                    <DesarrolloSeccion class="seccion" v-if="store.seccionActual == 'desarrollo'"/>
                    <BienvenidaSeccion v-if="store.seccionActual == 'bienvenida'"/>
                </div>
                
                
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
        <div class="info-modo" v-if="store.modoInteraccion == 'punto-sucursal'">
            <p>Seleccione la ubicación de la sucursal.</p>
        </div>
        <div class="info-modo" v-if="store.modoInteraccion == 'recorrido-auto'">
            <p>Seleccione el recorrido del auto.</p>
            <p>Para terminar el recorrido, haga click en el último punto.</p>
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
    height: 50px;
}

.cuerpo{
    padding: 0px calc(0% + 56px) 0px 0px;
    overflow-x: hidden;
    overflow-y: auto;
    height: calc(100% - 50px);
    position: relative;
}

.seccion{
    padding-top: 12px;
}
.info-modo{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 30px;
    text-align: center;
}

.seccion-contenido{
    height:100%;
}

</style>