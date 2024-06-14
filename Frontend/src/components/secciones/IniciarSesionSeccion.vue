<script>
import { store } from '@/store';
import axios from 'axios'
import VueJwtDecode from 'vue-jwt-decode';
export default {
  name: 'iniciarSesionSeccion',
  components: {

  },
  data() {
    return {
      store,
      username: '',
      password: '',
      correo: '',
      role: 1, // 1 = ROLE_ADMIN
      message: '',
      isLoading: false,
      isSignUp: false,
      isError: false
    }
  },
  mounted() {
  },
  computed: {
    messageClass() {
      return {
        'text-success': !this.isError,
        'text-error': this.isError
      };
    }
  },
  methods: {
    async iniciarSesion() {
      try {
        const response = await axios.post('/auth/login', {
          username: this.username,
          password: this.password
        });

        const { token } = response.data;
        // Save token to local storage
        localStorage.setItem('token', token);
        console.log('JSON Web Token -> ', token);

        // Decode token
        const decodedToken = VueJwtDecode.decode(token);
        console.log('Decoded Token:', decodedToken);

        // Log issuedAt and expiration dates
        const issuedAt = new Date(decodedToken.iat * 1000).toLocaleString('en-GB', { timeZone: 'America/Montevideo', timeZoneName: 'short' }); // Convert from seconds to milliseconds
        const expiration = new Date(decodedToken.exp * 1000).toLocaleString('en-GB', { timeZone: 'America/Montevideo', timeZoneName: 'short' }); // Convert from seconds to milliseconds
        console.log('Token issued at -> ', issuedAt);
        console.log('Token expires at -> ', expiration);

        // Get user role
        store.role = decodedToken.role[0].authority;
        console.log('Role -> ', store.role);

        store.IsUserLogged = true;

        // Redirect based on user role
        if (store.role === 'ROLE_ADMIN') {
          localStorage.setItem("tipoUsuario", "admin");
          this.store.tipoUsuario = "admin";
          this.store.seccionActual = "automotoras";
          this.isError = false;
          console.log('El usuario inició sesión correctamente!');
        } else {
          console.error('Unknown role:', store.role);
        }
      } catch (error) {
        if (error.response && error.response.data) {
          this.message = error.response.data.message;
        }
        this.isError = true;
      }
    },
    async registrarse() {
      try {
        const response = await axios.post('/auth/register', {
          username: this.username,
          correo: this.correo,
          password: this.password,
          role: this.role
        });
        this.message = response.data.message;
        this.isSignUp = false;
        this.isError = false;
        console.log('El usuario se registró correctamente!');
        console.log('Rol -> ' + this.role);
      } catch (error) {
        if (error.response && error.response.data) {
          this.message = error.response.data.message;
        } else {
          this.message = 'An error occurred. Please try again.';
        }
        this.isError = true;
      }
    },
    toggleForm() {
      this.isSignUp = !this.isSignUp;
      // Clear the form fields when switching
      this.username = '';
      this.password = '';
      this.correo = '';
      this.message = '';
    }
  },
}
</script>

<template>
  <div style="padding: 15px 15px;">
    <p v-if="!isSignUp">
      Si usted es un administrador, puede ingresar aquí.
    </p>
    <p v-if="isSignUp">
      Por favor, complete el formulario para registrarse.
    </p>
    <p :class="messageClass">{{ message }}</p>

    <form v-if="!isSignUp" @submit.prevent="iniciarSesion">
      <div class="contenedor-formulario">
        <label for="username">Usuario</label>
        <input type="text" v-model="username" placeholder="Usuario" required>
        <label for="password">Contraseña</label>
        <input type="password" v-model="password" placeholder="Contraseña" required>
      </div>
      <div class="contenedor-botones">
        <button type="submit" class="boton">Iniciar sesión</button>
        <button type="button" @click="toggleForm" class="boton">Registrarse</button>
      </div>
    </form>

    <form v-if="isSignUp" @submit.prevent="registrarse">
      <h4>{{ store.role }}</h4>
      <div class="contenedor-formulario">
        <label for="username">Usuario</label>
        <input type="text" v-model="username" placeholder="Usuario" required>
        <label for="password">Contraseña</label>
        <input type="password" v-model="password" placeholder="Contraseña" required>
        <label for="Email">Email</label>
        <input type="email" v-model="correo" placeholder="Email" required>
      </div>
      <div class="contenedor-botones">
        <button type="submit" class="boton">Registrarse</button>
        <button type="button" @click="toggleForm" class="boton">Cancelar</button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.contenedor-formulario input {
  padding: 10px 15px;
}

.boton {
  display: block;
  width: 100%;
  padding: 10px 15px;
  cursor: pointer;
  margin-bottom: 10px;
  background-color: #36454F;
  border-color: white;
}

.contenedor-botones {
  margin-top: 25px;
}

.boton:hover {
  background-image: linear-gradient(#36454F, #4d6270);
}

.text-success {
  color: springgreen !important;
}

.text-error {
  color: crimson;
}
</style>