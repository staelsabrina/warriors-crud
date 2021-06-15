package br.com.zup.warriors.repository

import br.com.zup.warriors.model.Console
import java.util.*
import javax.inject.Singleton

@Singleton
interface ConsoleRepository {

    fun save (console: Console) : Console
    fun findById (id: UUID) : Optional<Console>
    fun update (console: Console): Console
    fun findAll(): Collection<Console>
    fun delete(console: Console)

}