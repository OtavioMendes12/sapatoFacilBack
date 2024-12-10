package SapatoFacil.TIS.repository;

import SapatoFacil.TIS.model.CarrinhoModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarrinhoRepository extends JpaRepository<CarrinhoModel, Long> {
}