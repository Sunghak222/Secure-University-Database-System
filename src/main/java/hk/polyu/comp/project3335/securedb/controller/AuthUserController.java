package hk.polyu.comp.project3335.securedb.controller;

import org.springframework.web.bind.annotation.PostMapping;
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
}
