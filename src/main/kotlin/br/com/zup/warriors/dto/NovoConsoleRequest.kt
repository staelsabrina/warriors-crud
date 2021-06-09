package br.com.zup.warriors.dto

import br.com.zup.warriors.model.NovoConsole
import io.micronaut.core.annotation.Introspected
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent

@Introspected
data class NovoConsoleRequest(
    @field:NotBlank
    val nome: String,
    @field:NotBlank
    val marca: String,
    @field:PastOrPresent
    val dataLancamento: LocalDate?
) {

    fun paraNovoConsole(): NovoConsole {
        return NovoConsole(
            nome = nome,
            marca = marca,
            dataLancamento = dataLancamento
        )
    }

}
