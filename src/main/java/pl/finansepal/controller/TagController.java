package pl.finansepal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.finansepal.controller.dto.TagDTO;
import pl.finansepal.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@AllArgsConstructor
@Slf4j
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagDTO> create(@RequestBody TagDTO tagDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.create(tagDTO));
    }

    @GetMapping("/list")
    public ResponseEntity<List<TagDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(tagService.list());
    }

    @GetMapping
    public ResponseEntity<TagDTO> get(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(tagService.get(id));
    }

    @PutMapping
    public ResponseEntity<TagDTO> update(@RequestBody TagDTO tagDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tagService.update(tagDTO));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam Long id){
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
