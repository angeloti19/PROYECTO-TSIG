<script>
import { store } from '@/store';
export default{
    name: 'iniciarSesionSeccion',
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
        iniciarSesion(){
            localStorage.setItem("tipoUsuario", "admin")
            this.store.tipoUsuario = "admin"
            this.store.seccionActual = "automotoras"
        }
    },
}
</script>

<template>
    <div>
        <p>Esta es la seccion de inicio de sesion (por ahora es solo un boton)</p>

        <button @click="iniciarSesion" class="boton">Iniciar sesion</button>
    </div>

</template>