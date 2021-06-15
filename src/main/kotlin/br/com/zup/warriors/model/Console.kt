package br.com.zup.warriors.model

import br.com.zup.warriors.dto.ConsoleResponse
import io.micronaut.core.annotation.Introspected
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent

@Introspected
class Console(
    @field:NotBlank
    var nome: String = "",
    @field:NotBlank
    var marca: String = "",
    @field:PastOrPresent
    var dataLancamento: LocalDate?,

    var id: UUID? = null,
    var dataCadastro: LocalDate = LocalDate.now()
) {

    fun toDto(): ConsoleResponse{
        return ConsoleResponse(
            id = id!!,
            nome = nome,
            marca = marca,
            dataLancamento = dataLancamento.toString(),
            dataCadastro = dataCadastro.toString()
        )
    }

    override fun toString(): String {
        return "Console(nome='$nome', marca='$marca', dataLancamento=$dataLancamento, id=$id, dataCadastro=$dataCadastro)"
    }


}
