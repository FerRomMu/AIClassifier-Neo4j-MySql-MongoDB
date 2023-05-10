package ar.edu.unq.eperdemic.utils.impl

import ar.edu.unq.eperdemic.exceptions.InvalidDataTypeException
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.*
import ar.edu.unq.eperdemic.persistencia.repository.spring.*
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.DataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class DataServiceSpring: DataService {

    @Autowired lateinit var patogenoRepository: PatogenoRepository
    @Autowired lateinit var ubicacionRepository: UbicacionRepository
    @Autowired lateinit var vectorRepository: VectorRepository
    @Autowired lateinit var especieRepository: EspecieRepository
    @Autowired lateinit var mutacionRepositor: MutacionRepository

    override fun eliminarTodo() {
        patogenoRepository.deleteAll()
        vectorRepository.deleteAll()
        ubicacionRepository.deleteAll()
        especieRepository.deleteAll()
        mutacionRepositor.deleteAll()
    }

    override fun persistir(entidades: List<Any>): List<Any> {
        entidades.forEach { entidad ->
            when (entidad) {
                is Patogeno -> patogenoRepository.save(entidad)
                is Vector -> vectorRepository.save(entidad)
                is Especie -> especieRepository.save(entidad)
                is Ubicacion -> ubicacionRepository.save(entidad)
                is Mutacion -> mutacionRepositor.save(entidad)
                else -> throw InvalidDataTypeException("El dato no es persistible.")
            }
        }
        return entidades
    }

    override fun persistir(entidad: Any): Any {
        return this.persistir(listOf(entidad)).first()
    }

    override fun crearSetDeDatosIniciales(): List<Any> {
        val todos: MutableList<Any> = mutableListOf()
        for (i in 0..20) {

            val patogeno = Patogeno("Tipo $i")
            patogeno.tipo = "Tipo $i"
            val especie = patogeno.crearEspecie("Especie $i", "Pais $i")
            val ubicacion = Ubicacion ("Ubicacion $i")
            val vector = Vector(listOf(TipoDeVector.Insecto, TipoDeVector.Animal, TipoDeVector.Persona)[i % 3])
            vector.ubicacion = ubicacion
            vector.especiesContagiadas.add(especie)
            especie.vectores.add(vector)

            patogenoRepository.save(patogeno)
            ubicacionRepository.save(ubicacion)
            especieRepository.save(especie)
            patogenoRepository.save(patogeno)
            vectorRepository.save(vector)

            todos.addAll(listOf(ubicacion, patogeno, especie, vector))
        }
        return todos.toList()
    }

    override fun crearPandemiaPositiva(): Especie {
        val patogeno = Patogeno("Gripe")
        patogenoRepository.save(patogeno)
        val especiePandemica = Especie(patogeno, "virusT", "raccoon city")
        especieRepository.save(especiePandemica)

        for (i in 0..20){
            val vector = Vector(
                listOf(TipoDeVector.Insecto, TipoDeVector.Animal, TipoDeVector.Persona)[i % 3]
            )
            val ubicacion = Ubicacion("Lugar $i")
            vector.ubicacion = ubicacion

            vector.especiesContagiadas.add(especiePandemica)
            especiePandemica.vectores.add(vector)
            ubicacionRepository.save(ubicacion)
            vectorRepository.save(vector)
        }
        return especiePandemica
    }
}