<script>
import { store } from '@/store';
export default{
    name: 'iniciarSesionSeccion',
    components: {

    },
    data(){
        return{
            store,
            correo: "",
            contrasena: "",
            isLoading: false
        }
    },
    mounted(){
    },
    methods:{
        iniciarSesion(){
            localStorage.setItem("tipoUsuario", "admin")
            this.store.tipoUsuario = "admin"
            this.store.seccionActual = "automotoras"
        },
        async onSubmitInicioSesion(){
            console.log("los datos ingresados son los siguientes:")
            console.log(this.correoIS + " "+ this.contrasenaIS)
            // this.isLoading = true
            // //Hace el pedido
            // const data = {
            //     "correo": this.correo,
            //     "contrasenia": this.contrasena,
            // }
            // const response = await axios.post(import.meta.env.VITE_BACKEND_API + "api/login/", data)
            //     .then(function (response) {
            //         console.log("Response: " + response);
            //         this.isLoading = false

            //     }.bind(this))
            //     .catch(function (error) {
            //         console.log("Error: " + error);
            //         this.isLoading = false
            //     }.bind(this));
            
        },
    },
}
</script>

<template>
    <div>
        <p>Si usted es un administrador, puede ingresar aquí.</p>
        <form> 
            <div class="contenedor-formulario">
                <label for="correo">Correo</label>
                <input v-model="correo" name="correo" id="correo" type="email">
                <label for="contrasena">Contraseña</label>
                <input v-model="contrasena" name="contrasena" id="contrasena" type="password">
                <button :disabled="isLoading" type="button" @click="onSubmitInicioSesion" class="boton" style="width: 130px; margin-top: 15px;">Confirmar</button>
            </div>
        </form>
        <button @click="iniciarSesion" class="boton">Iniciar sesion</button>
    </div>

</template>