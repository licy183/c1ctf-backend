package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.po.Log;
import club.c1sec.c1ctfplatform.services.LogService;
import club.c1sec.c1ctfplatform.vo.log.GetLogRequest;
import club.c1sec.c1ctfplatform.vo.PageList;
import club.c1sec.c1ctfplatform.vo.PageListRequest;
import club.c1sec.c1ctfplatform.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@InterceptCheck(checkers = {AdminChecker.class})
@RestController
@RequestMapping("/api/log")
public class LogController {
    @Autowired
    LogService logService;

    @PostMapping("/get_log")
    public Response<PageList<List<Log>>> getAllLog(@RequestBody @Valid GetLogRequest getLogRequest, BindingResult bindingResult) {
        Response<PageList<List<Log>>> response = new Response<>();

        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }

        PageListRequest pageListRequest = getLogRequest.getPageListRequest();
        if (pageListRequest.getPageNo() != null && pageListRequest.getPageSize() != null) {
            int pageNo = pageListRequest.getPageNo();
            int pageSize = pageListRequest.getPageSize();
            PageList pageList = new PageList();
            Page<Log> page = logService.getLogWithPage(pageNo, pageSize, getLogRequest.getLogType(), getLogRequest.getContent());
            pageList.setTotalPages(page.getTotalPages());
            pageList.setContent(page.getContent());
            response.success("", pageList);
        } else {
            response.invalid("无效参数");
        }
        return response;
    }
}
