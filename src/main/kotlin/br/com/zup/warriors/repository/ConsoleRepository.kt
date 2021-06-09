package br.com.zup.warriors.repository

import br.com.zup.warriors.model.NovoConsole
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ConsoleRepository : JpaRepository <NovoConsole, Long> {
}