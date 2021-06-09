package br.com.zup.warriors.model

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.NovosDadosRequest
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent

@Entity
class NovoConsole(
    @field:NotBlank
    var nome: String,
    @field:NotBlank
    var marca: String,
    @field:PastOrPresent
    var dataLancamento: LocalDate?,
) {

    @Id
    @GeneratedValue
    var id: Long? = null

    val dataCadastro: LocalDate = LocalDate.now()

    fun toDto(): ConsoleResponse{
        return ConsoleResponse(
            id = id!!,
            nome = nome,
            marca = marca,
            dataLancamento = dataLancamento.toString(),
            dataCadastro = dataCadastro.toString()
        )
    }

    fun atualiza(novosDados: NovosDadosRequest) {
        if (novosDados.nome != null && novosDados.nome != "") {
            nome = novosDados.nome
        }
        if (novosDados.marca != null && novosDados.marca != "") {
            marca = novosDados.marca
        }
        if (novosDados.dataLancamento != null) {
            dataLancamento = novosDados.dataLancamento
        }
    }

}
