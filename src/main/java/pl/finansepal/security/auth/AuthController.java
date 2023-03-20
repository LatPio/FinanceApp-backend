package pl.finansepal.security.auth;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.finansepal.security.model.AuthenticationResponse;
import pl.finansepal.security.model.LoginRequest;
import pl.finansepal.security.model.RefreshTokenRequest;
import pl.finansepal.security.model.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

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

    @PostMapping("refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshTokens (@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){

        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
//        return new ResponseEntity<>("Refresh Token Delete Successfully", HttpStatus.OK);
    return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Delete Successfully");
    }


}
