package ar.edu.unq.eperdemic.persistencia.repository.mongo


import ar.edu.unq.eperdemic.modelo.UbicacionMongo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UbicacionMongoRepository: MongoRepository<UbicacionMongo, String> {

    fun validarMovimiento(nombreUbicacionOrigen: String, nombreUbicacionDestino: String): Int
    fun actualizarEstadoDeInfectadosEn(ubicacionOrigen: String, quedanInfectados: Boolean)
}