package pl.finansepal.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.finansepal.controller.dto.UserOptionsDTO;
import pl.finansepal.controller.mapper.IncomeMapper;
import pl.finansepal.controller.mapper.UserMapper;
import pl.finansepal.controller.mapper.UserOptionsMapper;
import pl.finansepal.model.UserOptions;
import pl.finansepal.repository.UserOptionsRepository;
import pl.finansepal.security.auth.AuthService;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserOptionsService {

    private final UserOptionsRepository userOptionsRepository;
    private final UserOptionsMapper userOptionsMapper = Mappers.getMapper(UserOptionsMapper.class);

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final AuthService authService;


    public UserOptionsDTO get(Long aLong){
        return userOptionsRepository.findByIdAndUser(aLong, authService.getCurrentUser())
                .map(userOptions -> userOptionsMapper.map(userOptions))
                .orElse(null);
    }

    public UserOptionsDTO get1(){
        return userOptionsRepository.findByUser(authService.getCurrentUser())
                .map(userOptions -> userOptionsMapper.map(userOptions))
                .orElse(null);
    }

    public UserOptionsDTO update(UserOptionsDTO userOptionsDTO){
        UserOptionsDTO existing = get(userOptionsDTO.getId());
        if (existing == null){
            return null;
        }
        userOptionsDTO.setUser(userMapper.map(authService.getCurrentUser()));
        UserOptions updated = userOptionsRepository.save(userOptionsMapper.map(userOptionsDTO));
        return userOptionsMapper.map(updated);
    }


}
