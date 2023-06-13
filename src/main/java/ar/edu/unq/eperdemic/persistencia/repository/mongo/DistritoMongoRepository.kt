package ar.edu.unq.eperdemic.persistencia.repository.mongo

import ar.edu.unq.eperdemic.modelo.Distrito
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DistritoMongoRepository : MongoRepository<Distrito, String> {

   // fun distritoMasEnfermo():Distrito
}