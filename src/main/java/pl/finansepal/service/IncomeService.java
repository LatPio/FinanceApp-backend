package pl.finansepal.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.finansepal.controller.dto.IncomeDTO;
import pl.finansepal.controller.mapper.IncomeMapper;
import pl.finansepal.controller.mapper.UserMapper;
import pl.finansepal.model.Income;
import pl.finansepal.repository.IncomeRepository;
import pl.finansepal.security.auth.AuthService;
import pl.finansepal.service.common.CrudService;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.List;
import java.util.stream.Collectors;

import static pl.finansepal.service.IncomeSpecification.*;

@Service
@AllArgsConstructor
@Slf4j
public class IncomeService implements CrudService<IncomeDTO, Long> {

    private final IncomeRepository incomeRepository;

    private final IncomeMapper incomeMapper = Mappers.getMapper(IncomeMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final AuthService authService;



    @Override
    public List<IncomeDTO> list() {
        return incomeRepository.findAll(belongToUser(authService.getCurrentUser()))
                .stream()
                .map(incomeMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public IncomeDTO get(Long aLong) {
        return incomeRepository.findById(aLong)
                .map(income -> incomeMapper.map(income))
                .orElse(null);
    }

    @Override
    public IncomeDTO create(IncomeDTO incomeDTO) {
        incomeDTO.setId(null);
        incomeDTO.setUser(userMapper.map(authService.getCurrentUser()));
        Income created = incomeRepository.save(incomeMapper.map(incomeDTO));
        return incomeMapper.map(created);
    }

    @Override
    public IncomeDTO update(IncomeDTO incomeDTO) {
        IncomeDTO existing = get(incomeDTO.getId());
        if(existing == null){
            return null;
        }
        incomeDTO.setUser(userMapper.map(authService.getCurrentUser()));
        Income updated = incomeRepository.save(incomeMapper.map(incomeDTO));

        return incomeMapper.map(updated);
    }

    @Override
    public void delete(Long aLong) {
        incomeRepository.deleteById(aLong);
    }
}
