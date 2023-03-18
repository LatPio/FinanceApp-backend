package pl.finansepal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.finansepal.controller.dto.UserOptionsDTO;
import pl.finansepal.service.UserOptionsService;

@RestController
@RequestMapping("/api/options")
@AllArgsConstructor
@Slf4j
public class UserOptionsController {

    private final UserOptionsService userOptionsService;

    @GetMapping
    public ResponseEntity<UserOptionsDTO> get(){
        return ResponseEntity.status(HttpStatus.OK).body(userOptionsService.get1());
    }

    @PutMapping
    public ResponseEntity<UserOptionsDTO> update(@RequestBody UserOptionsDTO userOptionsDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userOptionsService.update(userOptionsDTO));
    }
}
