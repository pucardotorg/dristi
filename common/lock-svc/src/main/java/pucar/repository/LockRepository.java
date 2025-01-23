package pucar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pucar.web.models.Lock;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LockRepository extends JpaRepository<Lock, UUID> {
    Optional<Lock> getLockByUniqueIdAndTenantIdAndIsLocked(String uniqueId, String tenantId, Boolean isLocked);
}
