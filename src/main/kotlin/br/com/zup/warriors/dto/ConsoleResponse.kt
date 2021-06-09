package br.com.zup.warriors.dto

import io.micronaut.core.annotation.Introspected

@Introspected
class ConsoleResponse(
    val id: Long,
    val nome: String,
    val marca: String,
    val dataLancamento: String?,
    val dataCadastro: String
) {

    override fun toString(): String {
        return "ConsoleResponse(id=$id, nome='$nome', marca='$marca', dataLancamento=$dataLancamento, dataCadastro='$dataCadastro')"
    }
}
