package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.checkers.LoginChecker;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.po.Category;
import club.c1sec.c1ctfplatform.services.CategoryService;
import club.c1sec.c1ctfplatform.vo.Category.CategoryDeleteRequest;
import club.c1sec.c1ctfplatform.vo.Category.CategoryEditRequest;
import club.c1sec.c1ctfplatform.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@InterceptCheck(checkers = {LoginChecker.class})
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/get_all_category")
    public Response<List<Category>> getAllCategory() {
        Response<List<Category>> response = new Response<>();
        response.success("", categoryService.findAllCategory());
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/edit_category")
    public Response<Long> editCategory(@RequestBody CategoryEditRequest req) {
        Response<Long> response = new Response<>();
        Category category = null;
        if (req.getCategoryId() != null) {
            category = categoryService.findByCategoryId(req.getCategoryId());
            if (category == null) {
                response.fail("此分类 ID 不存在");
                return response;
            }
        } else {
            category = new Category();
        }

        if (req.getName() != null) {
            category.setName(req.getName());
        }
        categoryService.addCategory(category);
        response.success("修改/添加成功", category.getCategoryId());
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/delete_category")
    public Response<String> deleteCategory(@RequestBody CategoryDeleteRequest req) {
        Response<String> response = new Response<>();
        if (req.getCategoryId() != null) {
            Long categoryId = req.getCategoryId();
            Category category = categoryService.findByCategoryId(categoryId);
            if (category != null) {
                categoryService.deleteByCategoryId(categoryId);
                response.success("删除成功");
            } else {
                response.fail("此分类 ID 不存在");
            }
        } else {
            response.invalid("无效参数");
        }
        return response;
    }
}
