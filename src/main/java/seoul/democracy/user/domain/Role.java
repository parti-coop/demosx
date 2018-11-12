package seoul.democracy.user.domain;

public enum Role {
    ROLE_USER,
    ROLE_MANAGER,
    ROLE_ADMIN;

    public boolean isUser() {
        return this == ROLE_USER;
    }

    public boolean isManager() {
        return this == ROLE_MANAGER;
    }

    public boolean isAdmin() {
        return this == ROLE_ADMIN;
    }
}
