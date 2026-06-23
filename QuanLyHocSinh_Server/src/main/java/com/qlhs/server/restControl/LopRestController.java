package com.qlhs.server.restControl;

import com.qlhs.server.service.LopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lop")
public class LopRestController {

    @Autowired
    private LopService lopService;

    @GetMapping
    public List<Map<String, Object>> getAllLop() {
        return lopService.getAllLop();
    }
}
