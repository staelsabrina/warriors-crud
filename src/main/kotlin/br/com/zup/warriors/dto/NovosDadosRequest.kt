package br.com.zup.warriors.dto

import io.micronaut.core.annotation.Introspected
import java.time.LocalDate

@Introspected
class NovosDadosRequest(
    val nome: String? = null,
    val marca : String? = null,
    val dataLancamento : LocalDate? = null
) {


}
