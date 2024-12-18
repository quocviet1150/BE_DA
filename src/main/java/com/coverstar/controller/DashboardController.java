package com.coverstar.controller;

import com.coverstar.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dashboards")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
}
