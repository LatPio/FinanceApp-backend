package pl.finansepal.security.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.finansepal.security.model.AuthenticationResponse;
import pl.finansepal.security.model.LoginRequest;
import pl.finansepal.security.model.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest request){
        authService.signup(request);
        return new ResponseEntity<>("User Registraion Successfull", HttpStatus.OK);

    }


    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount (@PathVariable String token){
        authService.verifyAccount(token);

        return new ResponseEntity<>("Account Verification Complete", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest){

        return ResponseEntity.ok(authService.login(loginRequest));

    }
}
