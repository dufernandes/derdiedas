package com.derdiedas.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.Set;

/**
 * User entity. Also implements {@link UserDetails}
 * which enables this entity to serve as an User for Spring
 * framework.
 */
@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name="`user`")
public class User implements UserDetails {

    /**
     * Unique entity identifier. It is generated when the entity
     * is created in the database.
     *
     * @param id JPA generated Identifier
     *
     * @return Unique entity identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(unique = true, nullable = false)
    @Email(message = "Email must be in the correct format")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Singular
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY,
            mappedBy = "user")
    @EqualsAndHashCode.Exclude
    private Set<LearningWord> learningWords;

    @Column(nullable = false)
    private int wordsPerGroup;

    @Column(nullable = false)
    private int studyGroupPage;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
