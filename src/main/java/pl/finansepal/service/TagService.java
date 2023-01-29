package pl.finansepal.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.finansepal.controller.dto.TagDTO;
import pl.finansepal.controller.mapper.TagMapper;
import pl.finansepal.controller.mapper.UserMapper;
import pl.finansepal.model.Tag;
import pl.finansepal.repository.TagRepository;
import pl.finansepal.security.auth.AuthService;
import pl.finansepal.service.common.CrudService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TagService implements CrudService<TagDTO, Long> {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final AuthService authService;

    @Override
    public List<TagDTO> list() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public TagDTO get(Long aLong) {
        return tagRepository.findByIdAndUser(aLong, authService.getCurrentUser())
                .map(tag -> tagMapper.map(tag))
                .orElse(null);
    }

    @Override
    public TagDTO create(TagDTO tagDTO) {
        tagDTO.setId(null);
        tagDTO.setUser(userMapper.map(authService.getCurrentUser()));
        Tag created = tagRepository.save(tagMapper.map(tagDTO));
        return tagMapper.map(created);
    }

    @Override
    public TagDTO update(TagDTO tagDTO) {
        TagDTO existing = get(tagDTO.getId());
        if(existing == null){
            return  null;
        }
        tagDTO.setUser(userMapper.map(authService.getCurrentUser()));
        Tag updated = tagRepository.save(tagMapper.map(tagDTO));
        return tagMapper.map(updated);
    }

    @Override
    public void delete(Long aLong) {
        tagRepository.deleteByIdAndUser(aLong, authService.getCurrentUser());
    }
}
