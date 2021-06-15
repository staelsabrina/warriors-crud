package br.com.zup.warriors.service

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.ConsoleRequest
import br.com.zup.warriors.dto.DadosRequest
import java.util.*

interface ConsoleService {
    fun atualizaConsole(id: UUID, novosDados: DadosRequest): ConsoleResponse
    fun cadastraConsole(request: ConsoleRequest): ConsoleResponse
    fun consultaConsole(id: UUID): ConsoleResponse
    fun listaConsoles(): List<ConsoleResponse>
    fun deletaConsole(id: UUID)
}