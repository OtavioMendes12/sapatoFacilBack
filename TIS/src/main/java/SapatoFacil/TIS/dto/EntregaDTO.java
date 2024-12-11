package SapatoFacil.TIS.dto;

import SapatoFacil.TIS.model.EntregaModel;
import lombok.Data;

@Data
public class EntregaDTO {
    private Long id;
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
    private String telefoneContato;

    public static EntregaDTO fromModel(EntregaModel entrega) {
        EntregaDTO dto = new EntregaDTO();
        dto.setId(entrega.getId());
        dto.setRua(entrega.getRua());
        dto.setCidade(entrega.getCidade());
        dto.setEstado(entrega.getEstado());
        dto.setCep(entrega.getCep());
        dto.setComplemento(entrega.getComplemento());
        dto.setTelefoneContato(entrega.getTelefoneContato());
        return dto;
    }
} 