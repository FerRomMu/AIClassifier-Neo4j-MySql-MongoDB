package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Ubicacion
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UbicacionRepository : CrudRepository<Ubicacion, Long> {

    //fun vectoresEn(ubicacionId: Long?): Collection<Vector>
}