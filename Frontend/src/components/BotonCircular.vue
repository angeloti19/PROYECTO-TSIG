<script>
    export default{
        name: 'botonCircular',
        components: {
        },
        props:{
            color : String
        },
        mounted(){
        }
    }
</script>




<template>
    <div class="base" :style="{ 'background-color': color}">
        <slot></slot>
    </div>
</template>

<style scoped>

.base{
    width: 41px;
    height: 41px;
    padding: 7px;
    border-radius: 100%;
    align-items: center;
    justify-content: center;
    display: flex;
    border-style: solid;
    border-width: 2px;
    border-color: rgba(0, 0, 0, 0.388);
    cursor: pointer;
}

.base:hover {
  filter: brightness(96%);
}

.base:active {
  filter: brightness(79%);
}

</style>