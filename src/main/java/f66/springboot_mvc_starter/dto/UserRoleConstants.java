package f66.springboot_mvc_starter.dto;

import java.util.List;

public class UserRoleConstants {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_GUEST = "ROLE_GUEST";
    public static final List<String> ALL_ROLES = List.of(
            ROLE_USER,
            ROLE_ADMIN,
            ROLE_GUEST
    );

    private UserRoleConstants() {
    }

    public static boolean isValidRole(String role) {

        return ALL_ROLES.contains(role);
    }
}
