<script>

export default {
    name: 'contenedorCerrable',
    components: {
    },
    data() {
        return {
        }
    },
    emits: ['cerrar']
}
</script>

<template>
    <div class="contenedor">
        <div style="display:flex; justify-content: space-between; margin-top: 8px; margin-bottom: 8px;">
            <h1 class="contenedor-titulo">
                <slot name="titulo">
                    <!-- Titulo -->
                </slot>
            </h1>
            <div @click="$emit('cerrar')" style="cursor: pointer; display: flex; flex-direction: column; justify-content: center;">
                <v-icon class="close">mdi-window-close</v-icon>
            </div>
        </div>
        <v-divider/>
        <div class="contenedor-contenido" style="padding-right: 35px; padding-top: 5px;">
            <slot name="contenido">
                Contenitdo
            </slot>
        </div>

    </div>
</template>

<style scoped>
.contenedor {

    border-radius: 10px;
    border: 2px solid rgba(0, 0, 0, 0.5); 
    max-width: 562px;
    min-width: 400px;
    margin: auto;
    margin-top: 30px;
    box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.25);
    padding: 45px 25px;
    padding-top: 5px;
    padding-right: 10px;
    background-color: white;
}

.contenedor-contenido{
    overflow-y: auto;
    max-height: 389px;
}

h1 {
    margin-top: 0;
    font-weight: 300;
    font-size: 28px;
}

.close{
    color: rgba(0, 0, 0, 0.507);
    margin-right: 12px;
}
.close:hover{
    color: rgba(0, 0, 0, 0.907);
}


</style>