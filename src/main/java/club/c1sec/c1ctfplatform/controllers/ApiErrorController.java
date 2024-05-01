package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ApiErrorController extends AbstractErrorController {
    @Autowired
    ApplicationContext context;

    public ApiErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping("/error")
    public Response<String> errorHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Response<String> response = new Response<>();
        String errorMessage = (String) httpServletRequest.getAttribute("club.c1sec.CheckError");
        if (errorMessage != null) {
            httpServletResponse.setStatus(400);
            response.invalid(errorMessage);
        } else {
            response.fail("Internal Error");
        }
        return response;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
