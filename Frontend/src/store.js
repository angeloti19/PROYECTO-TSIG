import { reactive } from 'vue'
import axios from 'axios';
import TileLayer from 'ol/layer/Tile';
import TileWMS from 'ol/source/TileWMS';
import Draw from 'ol/interaction/Draw';

export const store = reactive({
  //Mapa
  currentGeolocation: [],
  puntoSolicitud: [577026.5895504038, 6138181.158768445],
  center: [575628.150457, 6141793.265429],
  zoom: 14,
  rotation: 0,
  currentCenter: [575628.150457, 6141793.265429],
  currentZoom: 14,
  currentRotation: 0,
  currentResolution: 0,
  puntoSolicitud: [577026.5895504038, 6138181.158768445],
  currentGeolocation: [],
  viewReference: undefined,
  mapReference: undefined,
  capaConFiltroAnterior: undefined,
  capaSucursales: undefined,
  capaAutos: undefined,
  modoInteraccion: "normal",
  tipoInteraccion: "",
  interaccionRef: undefined,
  //Sesion
  tipoUsuario: undefined,
  //Panel
  seccionActual: 'bienvenida',
  funcionUbicacionSucursal: undefined,
  funcionRecorridoAuto: undefined,
  usarUbicacion() {
    if (this.currentGeolocation.length == 0) {
      alert("Permita acceso a su ubicación para utilizar geolocalización.")
      return
    }
    this.puntoSolicitud = this.currentGeolocation
    this.viewReference.animate({ center: this.currentGeolocation })
  },
  async callePtoSolicitud() {
    const pixel = this.mapReference.getPixelFromCoordinate(this.puntoSolicitud)
    const width = this.mapReference.getSize()[0]
    const height = this.mapReference.getSize()[1]
    console.log(pixel)
    console.log(this.viewReference.view.extent)
    const response = await axios.get("http://localhost:8080/geoserver/wms?request=GetFeatureInfo&service=WMS&version=1.3.0&layers=tsige:t00departamento&styles=&srs=EPSG:32721&bbox=366582,6127927,858252,6671738&width=" + width + "&height=" + height + "&&query_layers=tsige:t00departamento&info_format=application/json&i=" + pixel[0] + "&j=" + pixel[1])
      .then(function (response) {
        console.log("Response: " + response.data);

      }.bind(this))
      .catch(function (error) {
        console.log("Error: " + error);
      }.bind(this));
  },
  agregarCapaConFiltro(valorFiltro) {

    // Verificar si se proporciona un valor de filtro válido
    if (valorFiltro.trim() === '') {
      alert('ingrese un nombre de calle válido');
      return;
    }
    // Eliminar la capa anterior si existe
    if (this.capaConFiltroAnterior) {
      this.mapReference.removeLayer(this.capaConFiltroAnterior);
    }

    const nuevaCapa = new TileLayer({
      zIndex: 1001,
      visible: true,
      minZoom: 13,
      source: new TileWMS({
        url: 'http://localhost:8080/geoserver/wms',
        params: {
          'LAYERS': 'tsige:t01_ejes',
          'CQL_FILTER': `nom_calle = '${valorFiltro}'`
        },
        serverType: 'geoserver',
        transition: 0
      })
    });

    // Añade la nueva capa al mapa utilizando la referencia del mapa
    this.mapReference.addLayer(nuevaCapa);
    // Actualizar la referencia de la capa anterior
    this.capaConFiltroAnterior = nuevaCapa;
  },
  fetchSucursalesMapa() {
    // Eliminar la capa anterior si existe
    if (this.capaSucursales) {
      this.mapReference.removeLayer(this.capaSucursales);
    }

    const capaSucursales = new TileLayer({
      zIndex: 1002,
      visible: true,
 
      source: new TileWMS({
        url: 'http://localhost:8080/geoserver/wms',
        params: {
          'LAYERS': 'tsige:sucursal',
          // 'CQL_FILTER': `nom_calle = '${valorFiltro}'`
        },
        serverType: 'geoserver',
        transition: 0
      })
    });
    this.mapReference.addLayer(capaSucursales);
    // Actualizar la referencia de la capa anterior
    this.capaSucursales = capaSucursales;
    // Esto es para refrescar la capa
    let params = this.capaSucursales.getSource().getParams()
    params.t = new Date().getMilliseconds()
    this.capaSucursales.getSource().updateParams(params)
  },
  fetchAutosMapa() {
    // Eliminar la capa anterior si existe
    if (this.capaAutos) {
      this.mapReference.removeLayer(this.capaAutos);
    }

    const capaAutos = new TileLayer({
      zIndex: 1002,
      visible: true,
      minZoom: 13,
      source: new TileWMS({
        url: 'http://localhost:8080/geoserver/wms',
        params: {
          'LAYERS': 'tsige:auto',
          // 'CQL_FILTER': `nom_calle = '${valorFiltro}'`
        },
        serverType: 'geoserver',
        transition: 0
      })
    });
    this.mapReference.addLayer(capaAutos);
    // Actualizar la referencia de la capa anterior
    this.capaAutos = capaAutos;
    // Esto es para refrescar la capa
    let params = this.capaAutos.getSource().getParams()
    params.t = new Date().getMilliseconds()
    this.capaAutos.getSource().updateParams(params)
  },
  marcarPuntoSucursal(coordenada){
    //Si se paso la referencia a la funcion para pasar las coordenadas, se envian
    if(this.funcionUbicacionSucursal){
      this.funcionUbicacionSucursal(coordenada)
    }
  },
  marcarRecorridoAuto(recorrido){
    //Si se paso la referencia a la funcion para pasar el recorrido, se envian
    if(this.funcionRecorridoAuto){
      this.funcionRecorridoAuto(recorrido)
    }
  },
  centrarMapaEnCoordenada(coordenada){
    this.viewReference.animate({ center: coordenada })
  },
  agregarInteraccion(tipo){
    if(this.interaccionRef){
      this.mapReference.removeInteraction(this.interaccionRef)
      this.interaccionRef = undefined
    }
    const interaccion = new Draw({
      type: tipo,
    });
    interaccion.on('drawstart', this.drawstart.bind(this));
    interaccion.on('drawend', this.drawend.bind(this));
    this.tipoInteraccion = tipo;
    this.mapReference.addInteraction(interaccion);
    this.interaccionRef = interaccion
  },
  drawstart(event){
    // console.log("DRAWSTART")
    // console.log(event)
    let puntoClick = event.feature.values_.geometry.flatCoordinates
    //Si el modo es punto de solicitud
    if(this.modoInteraccion == "punto-solicitud"){
      this.puntoSolicitud = puntoClick
      if(this.currentZoom < 15.5){
          this.viewReference.animate({center: puntoClick}, {zoom: 15.5})
      }else{
          this.viewReference.animate({center: puntoClick})
      }
    }
    //Si el modo es ubicacion sucursal
    if(this.modoInteraccion == "punto-sucursal"){
      this.marcarPuntoSucursal(puntoClick)
    }           
  },
  drawend(event){
      // console.log("DRAWEND")
      // console.log(event)
      //Si el modo es recorrido auto
      if(this.modoInteraccion == "recorrido-auto"){
          let recorrido = event.feature.values_.geometry.flatCoordinates
          this.marcarRecorridoAuto(recorrido)
      } 
  }
})