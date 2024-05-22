package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.po.Bulletin;
import club.c1sec.c1ctfplatform.services.BulletinService;
import club.c1sec.c1ctfplatform.vo.bulletin.BulletinDeleteRequest;
import club.c1sec.c1ctfplatform.vo.bulletin.BulletinEditRequest;
import club.c1sec.c1ctfplatform.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bulletin")
public class BulletinController {
    @Autowired
    BulletinService bulletinService;

    @GetMapping("/get_all_bulletin")
    public Response<List<Bulletin>> getAllBulletin() {
        Response<List<Bulletin>> response = new Response<>();
        response.success("", bulletinService.findAll());
        return response;
    }

    @GetMapping("/get_last_bulletin_id")
    public Response<Long> getLastBulletinId() {
        Response<Long> response = new Response<>();
        response.success("", bulletinService.getLastBulletinId());
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/edit_bulletin")
    public Response<Long> editBulletin(@RequestBody BulletinEditRequest req) {
        Response<Long> response = new Response<>();
        Bulletin bulletin = null;
        if (req.getBulletinId() != null) {
            bulletin = bulletinService.findByBulletinId(req.getBulletinId());
            if (bulletin == null) {
                response.fail("此公告 ID 不存在");
                return response;
            }
        } else {
            bulletin = new Bulletin();
        }

        if (req.getTitle() != null) {
            bulletin.setTitle(req.getTitle());
        }
        if (req.getContent() != null) {
            bulletin.setContent(req.getContent());
        }
        if (req.getPublishTime() != null) {
            bulletin.setPublishTime(Date.from(req.getPublishTime()));
        }
        if (req.getIsSticky() != null) {
            bulletin.setIsSticky(req.getIsSticky());
        }
        bulletinService.addBulletin(bulletin);
        response.success("修改/添加成功", bulletin.getBulletinId());
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/delete_bulletin")
    public Response<String> deleteBulletin(@RequestBody @Valid BulletinDeleteRequest req,
                                           BindingResult bindingResult) {
        Response<String> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.fail("无效参数");
            return response;
        }
        Long bulletinId = req.getBulletinId();
        Bulletin bulletin = bulletinService.findByBulletinId(bulletinId);
        if (bulletin != null) {
            bulletinService.deleteByBulletinId(bulletinId);
            response.success("删除成功");
        } else {
            response.fail("此公告 ID 不存在");
        }
        return response;
    }
}
