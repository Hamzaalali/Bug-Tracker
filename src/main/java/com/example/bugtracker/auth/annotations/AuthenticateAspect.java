package com.example.bugtracker.auth.annotations;
import com.example.acm_backend.auth.Utils.AuthUtils;
import com.example.acm_backend.auth.entities.User;
import com.example.acm_backend.auth.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticateAspect {
    @Autowired
    private HttpServletRequest request;
    private final AuthenticationService authenticationService;

    @Before(value = "@annotation(authenticate)", argNames = "authenticate")
    public void preHandle(Authenticate authenticate) {
        String token= AuthUtils.getToken(request);
        User user=authenticationService.validToken(token);
        request.setAttribute("user",user);
    }
}
