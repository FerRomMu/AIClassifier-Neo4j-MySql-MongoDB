package ar.edu.unq.eperdemic.persistencia.repository.mongo


import ar.edu.unq.eperdemic.modelo.Coordenada
import ar.edu.unq.eperdemic.modelo.Distrito
import ar.edu.unq.eperdemic.modelo.UbicacionMongo
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UbicacionMongoRepository: MongoRepository<UbicacionMongo, String> {

    @Query("{nombre:'?0'}")
    fun findByNombre(nombre: String): UbicacionMongo

    @Query(value="{ 'nombre': { \$eq: ?0 }, 'coordenada': { \$nearSphere: { \$geometry: { type: 'Point', coordinates: [?2, ?1] }, \$maxDistance: ?3 } } }",exists = true)
    fun isLocationNearby(nombre: String, longitud: Double, latitud: Double, distancia: Double): Boolean

    @Aggregation(pipeline = [
        "{ \$match: { hayAlgunInfectado: true } }",
        "{ \$project: { _id: 0, longitud: \"\$coordenada.longitud\", latitud: \"\$coordenada.latitud\" } }"
    ])
    fun findCoordenadasConInfectados(): List<Coordenada>

    @Aggregation(pipeline = [
        " { \$match: { hayAlgunInfectado: true } } ",
        "  { \$group: { _id: \"\$distrito.nombre\", count: { \$sum: 1 }, distrito: { \$first: \"\$distrito\" } } } ",
        "  { \$sort: { count: -1 } } ",
        "  { \$limit: 1 } ",
        "  { \$project: { _id: 0, nombre: \"\$distrito.nombre\", coordenadas: \"\$distrito.coordenadas\", polygon: \"\$distrito.polygon\"} }"
    ])
    fun distritoMasEnfermo(): Distrito?
}