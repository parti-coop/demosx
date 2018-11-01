package seoul.democracy.user.domain;

public enum Role {
    ROLE_USER,
    ROLE_MANAGER,
    ROLE_ADMIN;

    public boolean isManager() {
        return this == ROLE_MANAGER;
    }
}
