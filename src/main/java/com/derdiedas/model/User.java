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
 * <br>
 * The user represents the customer who logs in and uses the system. Therefore,
 * every information relevant to the customer, must be attached to the user.
 */
@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`user`")
public class User implements UserDetails {

    /**
     * Unique entity identifier. It is generated when the entity
     * is created in the database.
     *
     * @param id JPA generated Identifier
     * @return Unique entity identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Exclude
    private Long id;

    /**
     * User's email. Used to login the user and as a unique identifier, meaning
     * it is not possible to register two users with the same email.
     *
     * @param email User's email
     * @return User's email.
     */
    @Column(unique = true, nullable = false)
    @Email(message = "Email must be in the correct format")
    private String email;

    /**
     * Password used to authenticate the user in the system.
     *
     * @param password User's password.
     * @return User's password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * User's first name.
     * @param firstName User's first name.
     * @return User's first name..
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * User's last name.
     * @param lastName User's last name.
     * @return User's last name.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * Set of {@link LearningWord}s the user is currently learning. Note that
     * they wrap the entity {@link Word}, which is the word representation. {@link LearningWord},
     * holds information relevant to the learning status of the word.
     * @param learningWords Set of {@link LearningWord}s related to the @{@link User}.
     * @return Set of {@link LearningWord}s related to the @{@link User}.
     */
    @Singular
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<LearningWord> learningWords;

    /**
     * Number of words which the user learns at a time. For instance, if she learns
     * 5 words at a time, the user will be attached to 5 {@link LearningWord}s entities.
     * @param wordsPerGroup Number of words which the user learns at a time.
     * @return Number of words which the user learns at a time.
     */
    @Column(nullable = false)
    private int wordsPerGroup;

    /**
     * Which groups of words the user is currently learning. She starts at the group 0
     * and moves to until there are no words to learn.
     * @param studyGroupPage The N group of words the user is learning.
     * @return The N group of words the user is learning.
     */
    @Column(nullable = false)
    private int studyGroupPage;

    /**
     * List of Authorities - roles this user has.For now, this is
     * only implemented to use the Spring Authorization and Authentication frameowork.
     * 
     * @return List of Authorities - roles of the current user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * User name used to authenticate the user. Currently the email is used.
     * <br>
     * Needed for Spring Authorization and Authentication
     * 
     * @return User name used to authenticate the user - her email.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Whether the account is a non-expired one. This is always true.
     * <br>
     * Needed for Spring Authorization and Authentication
     * 
     * @return Always true.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Whether the account is not blocked. The account is never blocked,
     * thus true is always returned.
     * <br>
     * Needed for Spring Authorization and Authentication
     * 
     * @return Always true.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Whether the credentials are not expired. They never are, thus
     * true is returned.
     * <br>
     * Needed for Spring Authorization and Authentication
     * 
     * @return Always returns true.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Whether the account is enabled. The account is always enabled,
     * thus true is always returned.
     * <br>
     * Needed for Spring Authorization and Authentication
     * 
     * @return Always returns true.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
