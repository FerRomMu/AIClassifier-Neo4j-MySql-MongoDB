package ar.edu.unq.eperdemic.persistencia.repository.spring

import ar.edu.unq.eperdemic.modelo.Ubicacion
import org.springframework.data.repository.CrudRepository

interface UbicacionRepository : CrudRepository<Ubicacion, Long>