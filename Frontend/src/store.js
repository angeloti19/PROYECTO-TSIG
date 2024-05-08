import { reactive } from 'vue'

export const store = reactive({
  //Mapa
  currentGeolocation: [],
  puntoSolicitud: [577026.5895504038, 6138181.158768445],
  center: [575628.150457,6141793.265429],
  zoom: 14,
  rotation: 0,
  currentCenter: [575628.150457,6141793.265429],
  currentZoom: 14,
  currentRotation: 0,
  currentResolution: 0,
  puntoSolicitud: [577026.5895504038, 6138181.158768445],
  currentGeolocation: [],
  viewReference: undefined,
  //Sesion
  tipoUsuario: undefined,
  //Panel
  seccionActual: 'bienvenida',
  increment() {
    this.count++
  },
  usarUbicacion(){
    if (this.currentGeolocation.length == 0){
        alert("Permita acceso a su ubicación para utilizar geolocalización.")
        return
    }
    this.puntoSolicitud = this.currentGeolocation
    this.viewReference.animate({center: this.currentGeolocation})
  },
})