package br.com.zup.warriors.repository

import br.com.zup.warriors.model.Console
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import java.util.*
import javax.inject.Singleton

@Singleton
class ConsoleServiceImpl(private val cqlSession: CqlSession) : ConsoleRepository {

    override fun save(console: Console): Console {
        console.id = UUID.randomUUID()
        val id = cqlSession.execute(
            SimpleStatement
                .newInstance(
                    "INSERT INTO console.console(id, nome, marca, data_lancamento, data_cadastro) VALUES (?,?,?,?,?)",
                    console.id,
                    console.nome,
                    console.marca,
                    console.dataLancamento,
                    console.dataCadastro
                )
        )
        return console
    }

    override fun findById(id: UUID): Optional<Console> {

        val row: Row? = cqlSession.execute(
            SimpleStatement
                .builder("SELECT * FROM console.console WHERE id = ?")
                .addPositionalValue(id)
                .build()
        ).one() ?: return Optional.empty()

        return Optional.of(Console(
            nome = row?.getString("nome")!!,
            marca = row?.getString("marca")!!,
            dataLancamento = row?.getLocalDate("data_lancamento"),
            dataCadastro = row?.getLocalDate("data_cadastro")!!,
            id = row?.getUuid("id")
        ))
    }

    override fun findAll(): Collection<Console> {

        val resultadoBusca: ResultSet = cqlSession.execute(
            SimpleStatement
                .builder("SELECT * FROM console.console")
                .build()
        )

        var listaConsoles = resultadoBusca.map { row ->
            Console(id = row.getUuid("id"),
                nome = row?.getString("nome")!!,
                marca = row?.getString("marca")!!,
                dataLancamento = row?.getLocalDate("data_lancamento"),
                dataCadastro = row?.getLocalDate("data_cadastro")!!,
            )
        }.toList()
        return listaConsoles
    }

    override fun update(console: Console): Console {
        cqlSession.execute(
            SimpleStatement
                .builder("UPDATE console.console SET nome = ?, marca = ?, data_lancamento = ? WHERE id = ?")
                .addPositionalValue(console.nome)
                .addPositionalValue(console.marca)
                .addPositionalValue(console.dataLancamento)
                .addPositionalValue(console.id)
                .build()
        )

        var row = cqlSession.execute(
            SimpleStatement
                .builder("SELECT * FROM console.console WHERE id = ?")
                .addPositionalValue(console.id)
                .build()
        ).one()

        /**
         * Fiz esta busca no banco de dados para ter certeza de que os
         * dados foram alterados na tabela, mas não sei se é certo fazer isso ou se
         * é assim que se faz
         */

        return Console(
            nome = row?.getString("nome")!!,
            marca = row?.getString("marca")!!,
            dataLancamento = row?.getLocalDate("data_lancamento"),
            dataCadastro = row?.getLocalDate("data_cadastro")!!,
            id = row?.getUuid("id")
        )
    }

    override fun delete(console: Console) {
        cqlSession.execute(
            SimpleStatement
                .builder("DELETE FROM console.console WHERE id = ?")
                .addPositionalValue(console.id)
                .build()
        )
    }

}