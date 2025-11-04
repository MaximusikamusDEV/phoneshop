package com.es.phoneshop.web.controller.pages;

import com.es.phoneshop.web.constants.WebConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class AuthenticationControllerTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private SecurityContextHolderStrategy securityContextHolderStrategy;
    @Mock
    private SecurityContextRepository securityContextRepository;
    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginGet() {
        String response = authenticationController.login();
        assertEquals("login", response);
    }

    @Test
    void testLoginPostFail() {
        String username = "none";
        String password = "none";
        Model model = new ExtendedModelMap();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        when(authenticationManager.authenticate(authenticationToken)).thenThrow(
                new BadCredentialsException("Invalid credentials")
        );

        String result = authenticationController.loginAccept(
                username,
                password,
                request,
                response,
                model);

        assertEquals("login", result);
        assertEquals(WebConstants.INVALID_CREDENTIALS_MESSAGE, model.getAttribute(WebConstants.ERROR_ATTR));
    }

    @Test
    void testLoginPostSuccess() {
        String username = "none";
        String password = "none";
        Model model = new ExtendedModelMap();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        doNothing().when(securityContextHolderStrategy).setContext(any(SecurityContext.class));
        doNothing().when(securityContextRepository).saveContext(
                any(SecurityContext.class),
                any(HttpServletRequest.class),
                any(HttpServletResponse.class));


        String result = authenticationController.loginAccept(
                username,
                password,
                request,
                response,
                model);

        verify(authenticationManager).authenticate(authenticationToken);
        assertEquals("redirect:/", result);
    }

    @Test
    void testLogout() {
        when(securityContextHolderStrategy.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        String result = authenticationController.logout(request, response);

        assertEquals("redirect:/", result);
    }
}
