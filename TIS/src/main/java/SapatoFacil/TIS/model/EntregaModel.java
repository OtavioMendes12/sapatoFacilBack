package SapatoFacil.TIS.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class EntregaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String complemento;

    @Column(nullable = false)
    private String telefoneContato;


}