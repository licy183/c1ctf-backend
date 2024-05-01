package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.dao.CategoryDao;
import club.c1sec.c1ctfplatform.po.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;

    public List<Category> findAllCategory() {
        return categoryDao.findAll();
    }

    public Category findByCategoryId(Long id) {
        return categoryDao.findByCategoryId(id);
    }

    public void deleteByCategoryId(Long id) {
        categoryDao.deleteByCategoryId(id);
    }

    public Boolean existsByCategoryId(Long id) {
        return categoryDao.existsByCategoryId(id);
    }

    public void addCategory(Category category) {
        categoryDao.save(category);
    }
}
