package club.c1sec.c1ctfplatform.dao;

import club.c1sec.c1ctfplatform.po.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CategoryDao extends JpaRepository<Category, Long> {
    List<Category> findAll();

    Category findByCategoryId(Long id);

    void deleteByCategoryId(Long id);

    Boolean existsByCategoryId(Long id);
}
