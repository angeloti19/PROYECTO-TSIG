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
            this.store.role = ''
            this.store.IsUserLogged = false
            localStorage.setItem("token", null);
            console.log("Sesion cerrada!");
            console.log("Tipo usuario -> ", store.tipoUsuario);
            console.log("Reset Token -> ", localStorage.getItem("token"));
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