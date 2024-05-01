package club.c1sec.c1ctfplatform.dao;

import club.c1sec.c1ctfplatform.po.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigDao extends JpaRepository<Config, String> {
    Config findConfigByKey(String key);
}
