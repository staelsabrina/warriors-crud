package br.com.zup.warriors.repository

import br.com.zup.warriors.model.Console
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ConsoleRepository : JpaRepository <Console, Long> {
}