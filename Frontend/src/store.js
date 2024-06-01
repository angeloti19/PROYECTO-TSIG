import { reactive } from 'vue'
import axios from 'axios';
import TileLayer from 'ol/layer/Tile';
import TileWMS from 'ol/source/TileWMS';
import Draw from 'ol/interaction/Draw';
import { Heatmap } from 'ol/layer';
import VectorSource from 'ol/source/Vector';
import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import { WKT } from 'ol/format';
import { Style } from 'ol/style';
import { Stroke, Fill } from 'ol/style';

export const store = reactive({
  //Mapa
  currentGeolocation: [],
  center: [575628.150457, 6141793.265429],
  zoom: 14,
  rotation: 0,
  currentCenter: [575628.150457, 6141793.265429],
  currentZoom: 14,
  currentRotation: 0,
  currentResolution: 0,
  puntoSolicitud: undefined,
  puntoDestino: undefined,
  currentGeolocation: [],
  //Referencias 
  viewReference: undefined,
  mapReference: undefined,
  interaccionRef: undefined,
  //Capas
  capaSucursales: undefined,
  capaAutos: undefined,
  heatMapAutos: undefined,
  zonaSinCobertura: undefined,
  //Filtros de capa
  filtroSucursal: "",
  filtroAuto: "",
  //Condicionales de visibilidad de capa
  mostrandoHeatMapAutos: false,
  mostrandoZonaSinCobertura: false,
  //Manejo de estado de interaccion
  modoInteraccion: undefined,
  tipoInteraccion: "",
  //Sesion
  tipoUsuario: undefined,
  //Panel
  seccionActual: 'bienvenida',
  //Referencias a funciones callback
  funcionUbicacionSucursal: undefined,
  funcionRecorridoAuto: undefined,
  //Metodos
  usarUbicacion() {
    if (this.currentGeolocation.length == 0) {
      alert("Permita acceso a su ubicación para utilizar geolocalización.")
      return
    }
    this.puntoSolicitud = this.currentGeolocation
    this.viewReference.animate({ center: this.currentGeolocation })
  },
  async callePtoSolicitud() { //Experimento
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
  fetchSucursalesMapa(filtro) {
    // Si se proporciona un filtro, se utiliza
    if(filtro != undefined){
      this.filtroSucursal = filtro
    }
    // Eliminar la capa anterior si existe
    if (this.capaSucursales) {
      this.mapReference.removeLayer(this.capaSucursales);
    }

    const capaSucursales = new TileLayer({
      zIndex: 1002,
      visible: true,
      minZoom: 13,
      source: new TileWMS({
        url: 'http://localhost:8080/geoserver/wms',
        params: {
          'LAYERS': 'tsige:sucursal',
          'CQL_FILTER': this.filtroSucursal
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
  fetchAutosMapa(filtro) {
    // Si se proporciona un filtro, se utiliza
    if(filtro != undefined){
      this.filtroAuto = filtro
    }
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
          'CQL_FILTER': this.filtroAuto
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
      //Si el modo es recorrido auto
      if(this.modoInteraccion == "recorrido-auto"){
          let recorrido = event.feature.values_.geometry.flatCoordinates
          this.marcarRecorridoAuto(recorrido)
      } 
      //Si el modo es poligono para busqueda de autos
      if(this.modoInteraccion == "poligono-autos"){
        let poligono = event.feature.values_.geometry.flatCoordinates
        this.buscarAutosPoligono(poligono)
      }
      //Si el modo es punto de destino
      if(this.modoInteraccion == "punto-destino"){
        let puntoDestino = event.feature.values_.geometry.flatCoordinates
        this.confirmarPuntoDestino(puntoDestino)
      }
  },
  agregarMapaCalorAutos(){
    // Eliminar la capa anterior si existe
    if (this.heatMapAutos) {
      this.mapReference.removeLayer(this.heatMapAutos);
    }
    const heatMap = new Heatmap({
      zIndex: 1020,
      source: new VectorSource({
        format: new GeoJSON(),
        url: 'http://localhost:8080/geoserver/wfs?service=WFS&version=1.1.0' +
             '&request=GetFeature&typename=tsige:auto&outputFormat=application/json' +
             '&PropertyName=ubicacion'
      }),
      blur: 46,
      radius: 35,
    })

    this.mapReference.addLayer(heatMap)
    this.heatMapAutos = heatMap
  },
  quitarMapaCalorAutors(){
    if (this.heatMapAutos) {
      this.mapReference.removeLayer(this.heatMapAutos);
    }
  },
  async agregarZonaSinCobertura(){
    // Eliminar la capa anterior si existe
    if (this.zonaSinCobertura) {
      this.mapReference.removeLayer(this.zonaSinCobertura);
    }
    //Primero se busca el WKT del servidor
    const response = await axios.get(import.meta.env.VITE_BACKEND_API + "api/consultas/zonasSinCobertura")
      .then(function (response) {
        const zona = response.data
        console.log(zona)
        const format = new WKT();
        const feature = format.readFeature(zona, {
          dataProjection: 'EPSG:32721'
        });

        const zonaStyle = new Style({
          stroke: new Stroke({
            color: 'red',
            width: 2,
          }),
          fill: new Fill({
            color: 'rgba(255, 0, 0, 0.2)',
          }),
        });
        
        const vector = new VectorLayer({
          source: new VectorSource({
            features: [feature],
          }),
          style: [zonaStyle]
        });

        this.mapReference.addLayer(vector)
        this.zonaSinCobertura = vector
      }.bind(this))
      .catch(function (error) {
        console.log(error)
        console.log("Error: " + error.response.data);
      }.bind(this));
  },
  quitarZonaSinCobertura(){
    if (this.zonaSinCobertura) {
      this.mapReference.removeLayer(this.zonaSinCobertura);
    }
  },
  iniciarBusquedaPoligono(){
    this.modoInteraccion = "poligono-autos"
    this.agregarInteraccion("Polygon")
  },
  buscarAutosPoligono(poligono){
    console.log(poligono)
    let poligonoFormateado = []
    for (let i = 0; i < poligono.length; i += 2) {
      poligonoFormateado.push({
        x: poligono[i],
        y: poligono[i + 1]
      })
    }
    console.log(poligonoFormateado)
    this.modoInteraccion = "punto-solicitud"
    this.agregarInteraccion("Point")

    let wkt = "POLYGON(("
    poligonoFormateado.forEach(punto => {
      wkt = wkt.concat(punto.x, " ", punto.y, ",") 
    });
    wkt = wkt.substring(0, wkt.length - 1);
    wkt = wkt.concat("))")

    this.fetchAutosMapa(`WITHIN(ubicacion, ${wkt})`)
    
  },
  marcarPuntoDestino(){
    this.modoInteraccion = "punto-destino"
  },
  confirmarPuntoDestino(destino){
    console.log(destino)
    this.puntoDestino = destino
    if(this.currentZoom < 15.5){
      this.viewReference.animate({center: destino}, {zoom: 15.5})
    }else{
        this.viewReference.animate({center: destino})
    }
  }
})