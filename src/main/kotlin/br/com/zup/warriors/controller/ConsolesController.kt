package br.com.zup.warriors.controller

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.ConsoleRequest
import br.com.zup.warriors.dto.NovosDadosRequest
import br.com.zup.warriors.service.ConsoleService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/v1/consoles")
class ConsolesController {

    @Inject
    lateinit var service: ConsoleService

    @Put("/{id}")
    fun atualizaConsole(@PathVariable id: Long, novosDados: NovosDadosRequest): HttpResponse<Any> {

        val atualizado = try {
            service.atualizaConsole(id, novosDados)
        } catch (e: RuntimeException){
            if(e.message == "Console inexistente no banco de dados"){
                return HttpResponse.notFound(HttpStatus.NOT_FOUND).body(e.message)
            } else
                return HttpResponse.serverError(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
        return HttpResponse.ok(HttpStatus.OK).body(atualizado)
    }

    @Post
    fun cadastraConsole(@Valid @Body request: ConsoleRequest): HttpResponse<ConsoleResponse> {
        return HttpResponse.created(HttpStatus.CREATED).body(service.cadastraConsole(request))
    }

    @Get("/{id}")
    fun consultaConsole(@PathVariable id: Long) : HttpResponse<Any> {
        val responseConsultaId = try {
            service.consultaConsole(id)
        } catch (e: RuntimeException) {
            if(e.message == "Console inexistente no banco de dados"){
                return HttpResponse.notFound(HttpStatus.NOT_FOUND).body(e.message)
            } else
                return HttpResponse.serverError(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
        return HttpResponse.ok(HttpStatus.OK).body(responseConsultaId)
    }

    @Get
    fun listaConsoles(): HttpResponse<List<ConsoleResponse>>{
        val responseConsultaTodos = service.listaConsoles()
        return HttpResponse.ok(HttpStatus.OK).body(responseConsultaTodos)
    }

    @Delete("/{id}")
    fun deletaConsole(@PathVariable id: Long): HttpResponse<Any> {
        service.deletaConsole(id)
        return HttpResponse.noContent()
    }

}