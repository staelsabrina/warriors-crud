package br.com.zup.warriors.controller

import br.com.zup.warriors.dto.NovosDadosRequest
import br.com.zup.warriors.model.NovoConsole
import br.com.zup.warriors.repository.ConsoleRepository
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import javax.inject.Inject

@MicronautTest(transactional = false)
internal class AtualizaConsolesControllerTest(
    @Inject private val repository: ConsoleRepository,
    @Inject private val atualizaController: AtualizaConsolesController
){

    /**
     * Cenários de Teste
     * 1 - deve atualizar os dados de um console com sucesso
     * 2 - nao deve alterar dados do console quando nao forem passados dados novos
     * 3 - deve retornar not found quando id passado nao estiver cadastrado no banco de dados
     */

    @BeforeEach
    fun setUp(){
        repository.deleteAll()
    }

    @Test
    fun `deve atualizar os dados de um console com sucesso`(){
        //cenário
        val idCadastrado = repository.save(NovoConsole(
            nome = "Mega Drive",
            marca = "nintendo",
            dataLancamento = LocalDate.of(1998, 10, 29)
        )).id

        val novosDados = NovosDadosRequest(
            nome = "Sega Genesis",
            "Sega",
            dataLancamento = LocalDate.of(1988, 10, 29)
        )

        //ação
        val atualizacao = atualizaController.atualiza(idCadastrado!!, novosDados)

        //validação
        val bancoDados = repository.findAll()
        assertEquals(1, bancoDados.size)
        assertEquals(novosDados.nome, bancoDados.get(0).nome)
        assertEquals(novosDados.marca, bancoDados.get(0).marca)
        assertEquals(novosDados.dataLancamento, bancoDados.get(0).dataLancamento)

    }

    @Test
    fun `nao deve alterar dados do console quando nao forem passados dados novos`(){
        //cenário
        val idCadastrado = repository.save(NovoConsole(
            nome = "Mega Drive",
            marca = "Sega",
            dataLancamento = LocalDate.of(1988, 10, 29)
        )).id

        val novosDados = NovosDadosRequest(
            nome = "",
            "",
            dataLancamento = null
        )

        //ação
        atualizaController.atualiza(idCadastrado!!, novosDados)

        //validação
        val bancoDados = repository.findAll()
        assertEquals(1, bancoDados.size)
        assertEquals("Mega Drive", bancoDados.get(0).nome)
        assertEquals("Sega", bancoDados.get(0).marca)
        assertEquals("1988-10-29", bancoDados.get(0).dataLancamento.toString())
    }

    @Test
    fun `deve retornar not found quando id passado nao estiver cadastrado no banco de dados`(){
        //cenário
        val novosDados = NovosDadosRequest(
            nome = "Mega Drive",
            marca = "Sega",
            dataLancamento = LocalDate.of(1988, 10, 29)
        )

        //ação
        val erro = atualizaController.atualiza(1, novosDados)

        //validação
        val bancoDados = repository.findAll()

        assertTrue(bancoDados.isEmpty())
        assertEquals(HttpStatus.NOT_FOUND.code, erro.status.code)
    }

}