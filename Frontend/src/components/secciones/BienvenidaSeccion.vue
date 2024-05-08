<script>
import logo from '../icons/logo.vue';
import logoIz from '../icons/logoIz.vue';
import logoDer from '../icons/logoDer.vue'
export default{
    name: 'bienvenidaSeccion',
    components: {
        logo,
        logoIz,
        logoDer
    },
    data(){
        return{
        }
    },
    mounted(){
    },
    methods:{
    },
}
</script>

<template>
    <div class="zona-logo">
        <logoIz class="logoiz"/>
        <logo class="logo"/>
        <logoDer class="logoder"/>
    </div>
    <div class="bienvenida">
        <p style="font-weight: 600; font-size: 22px; text-align: center; margin-bottom: 12px;">Es como un taxi, <span style="display: block;">pero tu lo conduces!</span></p>
        <p style="text-align: center; margin-bottom: 12px;">Viaja a donde quieras en Montevideo con RentYou, simplemente selecciona dónde quieres pedir un auto de alquiler y te lo entregaremos enseguida.</p>
        <p style="text-align: center;"> Sólo recuerda que debes dejar el auto en una zona donde la automotora pueda recuperarlo!</p>
    </div>
</template>

<style scoped>
.zona-logo{
    background-color: rgb(255, 255, 255);
    width: 87.5%;
    height: 200px;
    display: flex;

    align-items: center;
    align-content: space-between;
    justify-content: space-between;
}

.logo{
    width: 200px;
    height: 200px;
    
}

.logoiz{
    height: 182px;
    margin-left: 6px;
}

.logoder{
    height: 182px;
    margin-right: 6px;
}

.bienvenida{
    padding: 12px 78px 12px 20px;
}
</style>