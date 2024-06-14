<script>
import { store } from '@/store'
import axios from 'axios'
import automotoraIcono from '@/components/icons/automotoraIcono.vue'
export default {
    name: 'consultasGeograficasSeccion',
    components: {
        automotoraIcono
    },
    data() {
        return {
            store,
            automotoras: [],
            isRankingActive: false
        }
    },
    mounted() {
    },
    methods: {
        clearRanking(){
            this.isRankingActive = !this.isRankingActive;
        },
        cambiarMapaCalorAutos() {
            if (store.mostrandoHeatMapAutos) {
                store.agregarMapaCalorAutos()
            } else {
                store.quitarMapaCalorAutors()
            }
        },
        cambiarZonaSinCobertura() {
            if (store.mostrandoZonaSinCobertura) {
                store.agregarZonaSinCobertura()
            } else {
                store.quitarZonaSinCobertura()
            }
        },
        async automotorasConMasAutos() {
            this.isRankingActive = !this.isRankingActive;
            this.automotoras = []
            const response = await axios.get('/api/consultas/top10Automotoras')
                .then(function (response) {
                    response.data.forEach(automotora => {
                        this.automotoras.push({
                            id: automotora.id,
                            nombre: automotora.nombre,
                            cantAutos: automotora.cantAutosTotal
                        })
                    });
                    let filtroSucursales = 'IN ('
                    response.data.forEach(automotora => {
                        automotora.sucursales.forEach(sucursal => {
                            filtroSucursales = filtroSucursales.concat(`'${sucursal.id}',`)
                        });
                    })

                    if (this.automotoras.length != 0) {
                        filtroSucursales = filtroSucursales.substring(0, filtroSucursales.length - 1);
                    }
                    filtroSucursales = filtroSucursales.concat(')')

                    this.store.fetchSucursalesMapa(filtroSucursales)
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error);
                }.bind(this));
        }
    },
}
</script>

<template>
    <div style="padding: 15px 15px;">
        <div class="check-box">
            <v-checkbox style="margin: auto;" density="compact" class="ma-0 pa-0" label="Mostrar mapa de calor de autos"
                v-model="store.mostrandoHeatMapAutos" @change="cambiarMapaCalorAutos"></v-checkbox>
        </div>
        <div class="check-box">
            <v-checkbox density="compact" label="Mostrar zona sin cobertura" v-model="store.mostrandoZonaSinCobertura"
                @change="cambiarZonaSinCobertura"></v-checkbox>
        </div>
        <button class="boton" @click="store.fetchSucursalesMapa('')">Mostrar todas las automotoras</button>
        <button class="boton" @click="automotorasConMasAutos()">Mostrar automotoras con más autos</button>
        <div v-if="isRankingActive" style="margin-bottom: 15px;">
            <h1><v-icon style="margin: 5px;">mdi-finance</v-icon>Top 10</h1>
            <v-divider style="margin-top: 15px;" />
            <template v-for="(automotora, index) in automotoras" :key="index">
                <div class="contenedor-automotora">
                    <div class="titulo-automotora">
                        <div style="display: flex;">
                            <h3 style="margin-right: 10px;"># {{ index + 1 }}</h3>
                            <automotoraIcono style="margin-right: 12px; width: 19px;" />
                            <div>
                                {{ automotora.nombre }}
                            </div>
                        </div>
                        <h4 style="align-self: flex-end;">
                            <v-icon style="margin-right: 20px">{{ 'mdi-car-side' }}</v-icon>{{ automotora.cantAutos }}
                        </h4>
                    </div>
                </div>
            </template>
            <button style="margin-top: 15px;" class="boton" @click="clearRanking()">Ocultar Ranking</button>
        </div>
    </div>

</template>

<style scoped>
.contenedor-automotora {
    border-style: solid;
    border-color: rgba(255, 255, 255, 0.474);
    border-width: 1px;
    border-right: none;
    border-left: none;
    border-top: none;

}

.check-box {
    border-radius: 20px;
    padding: 4px 15px;
    display: flex !important;
    justify-content: center;
}

.boton:hover {
    background-image: linear-gradient(#ffffff, lightgray);
}

.boton {
    display: block;
    width: 100%;
    cursor: pointer;
    margin-bottom: 10px;
    border: 2px solid gray !important;
    background-color: white;
    color: rgba(0, 0, 0, 0.82);
}

.check-box {
    display: block;
    width: 100%;
    cursor: pointer;
    margin-bottom: 10px;
    color: white;
}

.boton {
    padding: 10px 15px;
}

.titulo-automotora {
    padding: 10px 20px;
    cursor: pointer;
    display: flex;
    justify-content: space-between;
}

.titulo-automotora:active {
    background-color: rgba(0, 0, 0, 0.084);
}
</style>