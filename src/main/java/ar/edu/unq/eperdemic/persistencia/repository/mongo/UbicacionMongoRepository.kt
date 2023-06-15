package ar.edu.unq.eperdemic.persistencia.repository.mongo


import ar.edu.unq.eperdemic.modelo.UbicacionMongo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UbicacionMongoRepository: MongoRepository<UbicacionMongo, String> {
    @Query("{nombre:'?0'}")
    fun findByNombre(nombre: String): UbicacionMongo

    @Query(value="{ 'nombre': { \$eq: ?0 }, 'coordenada': { \$nearSphere: { \$geometry: { type: 'Point', coordinates: [?2, ?1] }, \$maxDistance: ?3 } } }",exists = true)
    fun isLocationNearby(nombre: String, longitud: Double, latitud: Double, distancia: Double): Boolean

}