package me.amplitudo.elearning.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String PROFESSOR = "ROLE_PROFESSOR";

    public static final String ASSISTANT = "ROLE_ASSISTANT";

    public static final String STUDENT = "ROLE_STUDENT";

    public static final String GUEST = "ROLE_GUEST";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String USER = "ROLE_USER";


    private AuthoritiesConstants() {
    }
}
