package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.Dto.LoginResult;
import hk.polyu.comp.project3335.securedb.model.AuthUser;
import hk.polyu.comp.project3335.securedb.repository.AuthUserRepository;
import hk.polyu.comp.project3335.securedb.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthUserService(AuthUserRepository authUserRepository, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.authUserRepository = authUserRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    //no need
    public void register(String email, String password, String role) {
        AuthUser au = new AuthUser();
        au.setEmail(email);
        au.setPasswordHash(encoder.encode(password));
        au.setRole(role);
        authUserRepository.save(au);
    }

    //returns jwt token
    public LoginResult login(String email, String password) {
        AuthUser user = authUserRepository.findByEmail(email).orElse(null);

        if (user == null || !encoder.matches(password, user.getPasswordHash())) {
            return null;
        }
        String token = jwtUtil.generateToken(user);

        return new LoginResult(
                token,
                user.getRole(),
                user.getStudentId(),
                user.getGuardianId(),
                user.getStaffId()
        );
    }
}
