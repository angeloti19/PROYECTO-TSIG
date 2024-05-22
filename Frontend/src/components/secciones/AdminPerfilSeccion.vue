<script>
import { store } from '@/store';
export default{
    name: 'adminPerfilSeccion',
    components: {

    },
    data(){
        return{
            store
        }
    },
    mounted(){
    },
    methods:{
        cerrarSesion(){
            localStorage.setItem("tipoUsuario", "anonimo")
            this.store.tipoUsuario = "anonimo"
            this.store.seccionActual = "bienvenida"
        }
    },
}
</script>

<template>
    <div style="padding: 15px 15px;">
        <p>Esta es la seccion de perfil para admins, donde pueden cerrar su sesion</p>
        <button @click="cerrarSesion" class="boton">Cerrar sesion</button>
    </div>
</template>