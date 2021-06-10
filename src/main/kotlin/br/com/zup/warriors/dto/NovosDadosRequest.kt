package br.com.zup.warriors.dto

import io.micronaut.core.annotation.Introspected
import java.time.LocalDate

@Introspected
data class NovosDadosRequest(
    val nome: String = "",
    val marca : String = "",
    val dataLancamento : LocalDate? = null
)
