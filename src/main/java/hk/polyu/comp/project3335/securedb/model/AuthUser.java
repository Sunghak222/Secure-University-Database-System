package hk.polyu.comp.project3335.securedb.model;

import jakarta.persistence.*;

@Entity
@Table(name="auth_users")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=100)
    private String username;

    @Column(nullable=false, length=100)
    private String passwordHash;

    @Column(nullable=false, length=16)
    private String role;

    private Long staffId;
    private Long studentId;

    private Boolean enabled = true;
}
