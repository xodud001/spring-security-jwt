package rev.security.springsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rev.security.springsecurityjwt.models.AuthenticationRequest;
import rev.security.springsecurityjwt.models.AuthenticationResponse;
import rev.security.springsecurityjwt.service.MyUserDetailsService;
import rev.security.springsecurityjwt.util.JwtUtil;

import java.lang.module.ResolutionException;

@RestController
public class HelloController {

    private AuthenticationManager authenticationManager;

    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;
    @Autowired
    public HelloController(MyUserDetailsService userDetailsService
            , JwtUtil jwtTokenUtil
            ,AuthenticationManager authenticationManager){
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }
    @GetMapping("/hello")
    public String hello(){
        return "Hello, World!";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
