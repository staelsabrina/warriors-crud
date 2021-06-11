package br.com.zup.warriors.model

import br.com.zup.warriors.dto.ConsoleResponse
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent

@Entity
class Console(
    @field:NotBlank
    var nome: String,
    @field:NotBlank
    var marca: String,
    @field:PastOrPresent
    var dataLancamento: LocalDate?,

    @Id
    @GeneratedValue
    var id: Long? = null,
    val dataCadastro: LocalDate = LocalDate.now()
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
}
