package pl.finansepal.security.auth;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.finansepal.security.model.AuthenticationResponse;
import pl.finansepal.security.model.LoginRequest;
import pl.finansepal.security.model.RegisterRequest;
import pl.finansepal.exeption.AppRuntimeException;
import pl.finansepal.model.NotificationEmail;
import pl.finansepal.model.User;
import pl.finansepal.model.VerificationToken;
import pl.finansepal.repository.UserRepository;
import pl.finansepal.repository.VerificationTokenRepository;
import pl.finansepal.service.MailService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;

    public void signup(RegisterRequest request){
        var user = User.builder()
                .userName(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(Instant.now())
                .enabled(false)
                .build();

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail(
                "Please Activate your Account",
                user.getEmail(),
                "Thank you for signing up to FinancePal "+
                        "please click on th bellow url to activate your account: " +
                        "http://localhost:8080/api/auth/accountVerification/"+token
                ));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow( () -> new AppRuntimeException("Invalid token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {

        String email = verificationToken.getUser().getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppRuntimeException("User not found - " + email));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        var user = userRepository.findByEmail(loginRequest.getEmail())
                        .orElseThrow( () -> new AppRuntimeException("User not found - " + loginRequest.getEmail()));
        var jwtToken = jwtService.generateToken(user);
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        String token = jwtService.generateToken(authenticate);
//        return new AuthenticationResponse(token, loginRequest.getUsername());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
