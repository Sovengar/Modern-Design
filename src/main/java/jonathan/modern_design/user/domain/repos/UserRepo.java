package jonathan.modern_design.user.domain.repos;

import jonathan.modern_design.user.domain.User;

public interface UserRepo extends FindUserRepo {
    void registerUser(User user);
}
