package com.conexa.saude.plataforma_medicos.service;

import com.conexa.saude.plataforma_medicos.dto.AgendamentoDTO;
import com.conexa.saude.plataforma_medicos.dto.PacienteDTO;
import com.conexa.saude.plataforma_medicos.exceptions.ErrorMessage;
import com.conexa.saude.plataforma_medicos.exceptions.ValidationException;
import com.conexa.saude.plataforma_medicos.model.entity.Agendamento;
import com.conexa.saude.plataforma_medicos.model.entity.Medico;
import com.conexa.saude.plataforma_medicos.model.entity.Paciente;
import com.conexa.saude.plataforma_medicos.repository.AgendamentoRepository;
import com.conexa.saude.plataforma_medicos.repository.MedicoRepository;
import com.conexa.saude.plataforma_medicos.repository.PacienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public void criarAgendamento(AgendamentoDTO agendamentoDTO, String emailMedico) {
        Medico medico = medicoRepository.findByEmail(emailMedico)
                .orElseThrow(() -> new ValidationException(ErrorMessage.USUARIO_NAO_LOCALIZADO));

        validarDataHora(agendamentoDTO.getDataHora());
        verificarDisponibilidade(agendamentoDTO.getDataHora(), medico);

        Paciente paciente = pacienteRepository.findByCpf(agendamentoDTO.getPaciente().getCpf())
                .orElseGet(() -> criarPaciente(agendamentoDTO.getPaciente()));

        Agendamento agendamento = Agendamento.builder()
                .dataHora(agendamentoDTO.getDataHora())
                .medico(medico)
                .paciente(paciente)
                .build();

        agendamentoRepository.save(agendamento);
    }

    public String getEmailByToken() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void validarDataHora(LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new ValidationException(ErrorMessage.DATA_INVALIDA);
        }
    }

    private void verificarDisponibilidade(LocalDateTime dataHora, Medico medico) {
        if (agendamentoRepository.existsByDataHoraAndMedico(dataHora, medico)) {
            throw new ValidationException(ErrorMessage.HORARIO_OCUPADO);
        }
    }

    private Paciente criarPaciente(PacienteDTO pacienteDTO) {
        return Paciente.builder()
                .nome(pacienteDTO.getNome())
                .cpf(pacienteDTO.getCpf())
                .build();
    }
}
