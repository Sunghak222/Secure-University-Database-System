package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.model.AuthUser;
import hk.polyu.comp.project3335.securedb.repository.AuthUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder encoder;

    public AuthUserService(AuthUserRepository authUserRepository, PasswordEncoder encoder) {
        this.authUserRepository = authUserRepository;
        this.encoder = encoder;
    }

    public void register(String email, String password, String role) {
        AuthUser au = new AuthUser();
        au.setEmail(email);
        au.setPasswordHash(encoder.encode(password));
        au.setRole(role);
        authUserRepository.save(au);
    }

    public boolean login(String email, String password) {
        var user = authUserRepository.findByEmail(email).orElse(null);
        return user != null && encoder.matches(password, user.getPasswordHash());
    }
}
