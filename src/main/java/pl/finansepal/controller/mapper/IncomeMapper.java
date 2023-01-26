package pl.finansepal.controller.mapper;

import org.mapstruct.Mapper;
import pl.finansepal.controller.dto.IncomeDTO;
import pl.finansepal.model.Income;

import java.util.List;

@Mapper
public interface IncomeMapper {

    IncomeDTO map(Income income);

    Income map(IncomeDTO incomeDTO);

    List<IncomeDTO> mapToDto(List<Income> incomeList);
}
