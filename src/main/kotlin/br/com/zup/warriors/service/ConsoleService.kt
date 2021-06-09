package br.com.zup.warriors.service

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.NovoConsoleRequest
import br.com.zup.warriors.dto.NovosDadosRequest
import javax.validation.Valid

interface ConsoleService {
    fun atualiza(id: Long, novosDados: NovosDadosRequest): ConsoleResponse
    fun cadastra(@Valid request: NovoConsoleRequest): ConsoleResponse
    fun consultaId(id: Long): ConsoleResponse
    fun consultaTodos(): List<ConsoleResponse>
    fun deleta(id: Long)
}