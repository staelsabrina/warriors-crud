package br.com.zup.warriors.controller

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.service.ConsoleService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import javax.inject.Inject

@Controller("/api/consoles/consulta")
class ConsultaConsolesController(@Inject private val service: ConsoleService) {

    @Get("/{id}")
    fun consultaId(@PathVariable id: Long) : HttpResponse<Any> {
        val responseConsultaId = try {
            service.consultaId(id)
        } catch (e: RuntimeException) {
            if(e.message == "Console inexistente no banco de dados"){
                return HttpResponse.notFound(HttpStatus.NOT_FOUND).body(e.message)
            } else
                return HttpResponse.serverError(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
        return HttpResponse.ok(HttpStatus.OK).body(responseConsultaId)
    }

    @Get
    fun consultaTodos(): HttpResponse<List<ConsoleResponse>>{
        val responseConsultaTodos = service.consultaTodos()
        return HttpResponse.ok(HttpStatus.OK).body(responseConsultaTodos)
    }

}