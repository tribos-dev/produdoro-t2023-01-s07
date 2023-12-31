package dev.wakandaacademy.produdoro.tarefa.infra;

import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.StatusTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.Optional;
import java.util.UUID;

@Repository
@Log4j2
@RequiredArgsConstructor
public class TarefaInfraRepository implements TarefaRepository {

    private final TarefaSpringMongoDBRepository tarefaSpringMongoDBRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Tarefa salva(Tarefa tarefa) {
        log.info("[inicia] TarefaInfraRepository - salva");
        try {
            tarefaSpringMongoDBRepository.save(tarefa);
        } catch (DataIntegrityViolationException e) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Tarefa já cadastrada", e);
        }
        log.info("[finaliza] TarefaInfraRepository - salva");
        return tarefa;
    }
    @Override
    public Optional<Tarefa> buscaTarefaPorId(UUID idTarefa) {
        log.info("[inicia] TarefaInfraRepository - buscaTarefaPorId");
        Optional<Tarefa> tarefaPorId = tarefaSpringMongoDBRepository.findByIdTarefa(idTarefa);
        log.info("[finaliza] TarefaInfraRepository - buscaTarefaPorId");
        return tarefaPorId;
    }
	@Override
	public List<Tarefa> buscarTodasTarefas(UUID idUsuario) {
        log.info("[inicia] TarefaInfraRepository - buscarTodasTarefas");
        List<Tarefa> listaTarefas = tarefaSpringMongoDBRepository.findAllByIdUsuario(idUsuario);
        log.info("[finaliza] TarefaInfraRepository - buscarTodasTarefas");
		return listaTarefas;
	}

    @Override

    public void limpaTarefasConcluidas(UUID idUsuario) {
        log.info("[inicia] TarefaInfraRepository - limpaTarefasConcluidas");
        tarefaSpringMongoDBRepository.deleteAllByStatusAndIdUsuario(StatusTarefa.CONCLUIDA.name(), idUsuario);
        log.info("[finaliza] TarefaInfraRepository - limpaTarefasConcluidas");

    }

    @Override
    public  List<Tarefa> listTarefasConcluidas(UUID idUsuario) {
        log.info("[inicia] TarefaInfraRepository - listTarefasConcluidas");
        List<Tarefa> tarefasConcluidas = tarefaSpringMongoDBRepository.findAllByStatusAndIdUsuario(StatusTarefa.CONCLUIDA.name(), idUsuario);
        if (tarefasConcluidas.isEmpty()) {
            throw APIException.build(HttpStatus.NOT_FOUND,
                    "Não há tarefas concluídas para o usuário com ID: " + idUsuario);
        }
        log.info("[finaliza] TarefaInfraRepository - listTarefasConcluidas");
        return tarefasConcluidas;
    }

    public void desativaTarefa(UUID idUsuario) {
        log.info("[inicia] TarefaRepositoryMongoDB- desativaTarefa");
        Query query = new Query();
        query.addCriteria(Criteria.where("idUsuario").is(idUsuario));
        Update update = new Update();
        update.set("statusAtivacao", "INATIVA");
        mongoTemplate.updateMulti(query, update, Tarefa.class);
        log.info("[finaliza] TarefaRepositoryMongoDB - desativaTarefa");
    }
}
