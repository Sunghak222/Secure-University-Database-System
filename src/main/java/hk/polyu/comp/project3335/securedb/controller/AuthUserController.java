package hk.polyu.comp.project3335.securedb.controller;

import hk.polyu.comp.project3335.securedb.Dto.LoginRequestDto;
import hk.polyu.comp.project3335.securedb.Dto.LoginResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hk.polyu.comp.project3335.securedb.service.AuthUserService;

@RestController
@RequestMapping("/authUsers")
public class AuthUserController {
    private final AuthUserService authUserService;

    public AuthUserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping("/register")
    public void register() {

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        String token = authUserService.login(dto.getEmail(), dto.getPassword());

        if (token == null) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

}
