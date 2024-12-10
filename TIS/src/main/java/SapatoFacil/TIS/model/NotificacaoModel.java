package SapatoFacil.TIS.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class NotificacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    private Boolean visualizado;

}