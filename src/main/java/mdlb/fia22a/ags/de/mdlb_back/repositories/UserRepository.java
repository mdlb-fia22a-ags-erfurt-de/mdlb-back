package mdlb.fia22a.ags.de.mdlb_back.repositories;

import mdlb.fia22a.ags.de.mdlb_back.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
