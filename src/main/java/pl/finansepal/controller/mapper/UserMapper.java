package pl.finansepal.controller.mapper;

import org.mapstruct.Mapper;
import pl.finansepal.controller.dto.UserDTO;
import pl.finansepal.model.User;

@Mapper
public interface UserMapper {

    UserDTO map(User user);
    User map(UserDTO userDTO);
}
