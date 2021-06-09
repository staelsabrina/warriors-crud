package br.com.zup.warriors.controller

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.NovosDadosRequest
import br.com.zup.warriors.service.ConsoleService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Put
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject

@Controller("/api/consoles")
class AtualizaConsolesController(@Inject private val service: ConsoleService) {

    @Put("/{id}")
    fun atualiza(@PathVariable id: Long, novosDados: NovosDadosRequest): HttpResponse<Any>{

        val atualizado = try {
            service.atualiza(id, novosDados)
        } catch (e: RuntimeException){
            if(e.message == "Console inexistente no banco de dados"){
                return HttpResponse.notFound(HttpStatus.NOT_FOUND).body(e.message)
            } else
                return HttpResponse.serverError(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }

        return HttpResponse.ok(HttpStatus.OK).body(atualizado)

    }

}