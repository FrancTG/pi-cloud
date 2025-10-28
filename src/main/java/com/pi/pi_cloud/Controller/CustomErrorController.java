package com.pi.pi_cloud.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController {

    @RequestMapping("/c-error")
    public String handleError() {
        return "error";
    }
}
