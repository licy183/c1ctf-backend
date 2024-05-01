package club.c1sec.c1ctfplatform;

import club.c1sec.c1ctfplatform.controllers.CategoryController;
import club.c1sec.c1ctfplatform.vo.Category.CategoryEditRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class C1ctfPlatformApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Autowired
    CategoryController t;

    @Test
    public void newCategory() {
        CategoryEditRequest c = new CategoryEditRequest();
        c.setName("Web");
        t.editCategory(c);

    }

}
