package ar.edu.unq.eperdemic.persistencia.repository.mongo

import ar.edu.unq.eperdemic.modelo.Coordenada
import ar.edu.unq.eperdemic.modelo.Distrito
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DistritoMongoRepository : MongoRepository<Distrito, String> {
    @Query(value = "{ polygon: { \$geoIntersects: { \$geometry: { type: 'Point', coordinates: [?1, ?0] } } } }")
    fun findByPoint(longitud: Double, latitud: Double): Distrito?
}