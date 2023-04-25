package ar.edu.unq.eperdemic.services

interface MutacionService {
  fun agregarMutacion(especieId: Long, mutacion: Mutacion): Mutacion
}