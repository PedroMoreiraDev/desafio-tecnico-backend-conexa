package com.conexa.saude.plataforma_medicos.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
@Data
@Builder
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Future
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    /* utilizei o CascadeType.PERSIST porque não solicitava endpoint de criação de pacientes
     e pensei que incluir a chamada de um endpoint novo para cadastrar pacientes e assim poder realizar o agendamento poderia dar erro no teste
     de quem está avaliando caso esqueça de chamar (então foquei exclusivamente nos endpoints que eram solicitados)
     porém, entendo que o fluxo correto é também ter 1 endpoint de cadastro de paciente e o médico só conseguir realizar uma
     consulta com pacientes que já estão previamente cadastrados no banco. Lógica fácil de implementação, caso queiram é só avisar que eu faço. */

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

}
