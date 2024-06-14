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
        <button @click="cerrarSesion" class="boton">Cerrar sesion</button>
    </div>
</template>

<style scoped>
.boton {
    display: block;
    width: 100%;
    padding: 10px 15px;
    cursor: pointer;
    margin-bottom: 10px;
    border: 2px solid gray !important;
    background-color: #ffffff;
    color: rgba(0, 0, 0, 0.82);
}

.boton:hover{
    background-image: linear-gradient(#ffffff, lightgray);
}
</style>