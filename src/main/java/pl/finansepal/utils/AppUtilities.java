package pl.finansepal.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.finansepal.security.auth.JwtService;

@Component
@AllArgsConstructor
public class AppUtilities {

    private final JwtService jwtService;


    public String getUserName(HttpServletRequest request){
        final String authenticationHeader= request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        jwtToken = authenticationHeader.substring(7);
        userEmail = jwtService.extractUsername(jwtToken);
        return userEmail;
    }

}
