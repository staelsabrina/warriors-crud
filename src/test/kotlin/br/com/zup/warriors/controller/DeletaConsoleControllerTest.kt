package br.com.zup.warriors.controller

import br.com.zup.warriors.model.NovoConsole
import br.com.zup.warriors.repository.ConsoleRepository
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest(transactional = false)
internal class DeletaConsoleControllerTest(
    @Inject private val repository: ConsoleRepository,
    @Inject private val deletaController : DeletaConsoleController
) {

    /**
     * Cenários de teste
     * 1 - deve deletar um console pelo id indicado
     * 2 - deve retornar not found quando o id indicado não estiver cadastrado no banco de dados
     */

    @BeforeEach
    fun setUp(){
        repository.deleteAll()
    }

    @Test
    fun `deve deletar um console pelo id indicado`(){
        //cenário
        val idCadastrado = repository.save(NovoConsole(
            nome = "GameCube",
            marca = "Nintendo",
            dataLancamento = null
        )).id

        //validação1
        val bancoDadosAntesDeletar = repository.findAll()
        assertTrue(bancoDadosAntesDeletar.isNotEmpty())

        //ação
        deletaController.deleta(idCadastrado!!)

        //validação2
        val bancoDadosDepoisDeletar = repository.findAll()
        assertTrue(bancoDadosDepoisDeletar.isEmpty())

    }

    @Test
    fun `deve retornar not found quando o id indicado não estiver cadastrado no banco de dados`(){
        //cenário
        val idNaoCadastrado = 1L

        //ação
        val resultado = deletaController.deleta(idNaoCadastrado)

        //validação
        assertEquals(HttpStatus.NOT_FOUND.code, resultado.status.code)
    }

}