package br.com.zup.warriors.dto

import br.com.zup.warriors.model.Console
import io.micronaut.core.annotation.Introspected
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent

@Introspected
data class ConsoleRequest(
    @field:NotBlank
    val nome: String = "",
    @field:NotBlank
    val marca: String = "",
    @field:PastOrPresent
    val dataLancamento: LocalDate?
) {

    fun paraNovoConsole(): Console {
        return Console(
            nome = nome,
            marca = marca,
            dataLancamento = dataLancamento
        )
    }

}
