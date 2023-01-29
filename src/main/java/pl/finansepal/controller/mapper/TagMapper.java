package pl.finansepal.controller.mapper;

import org.mapstruct.Mapper;
import pl.finansepal.controller.dto.TagDTO;
import pl.finansepal.model.Tag;

import java.util.List;

@Mapper
public interface TagMapper {

    TagDTO map(Tag tag);

    Tag map(TagDTO tagDTO);

    List<TagDTO> mapToDTO(List<Tag> tagList);
}
