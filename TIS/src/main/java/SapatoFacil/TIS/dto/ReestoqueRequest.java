package SapatoFacil.TIS.dto;

import lombok.Data;

@Data
public class ReestoqueRequest {

    private Long produtoId;
    private Integer quantidade;
}