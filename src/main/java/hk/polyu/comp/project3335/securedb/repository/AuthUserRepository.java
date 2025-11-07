package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    AuthUser findByUsername(String username);
}
