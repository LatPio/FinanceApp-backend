package pl.finansepal.controller.mapper;

import org.mapstruct.Mapper;
import pl.finansepal.controller.dto.UserOptionsDTO;
import pl.finansepal.model.UserOptions;

@Mapper
public interface UserOptionsMapper {

    UserOptionsDTO map(UserOptions userOptions);

    UserOptions map(UserOptionsDTO userOptionsDTO);
}
