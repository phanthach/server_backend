package org.example.server.Controller.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/addlayout")
    public String addlayout() {
        return "AddLayoutSeat";
    }

    @GetMapping("/addseat")
    public String addseat() {
        return "AddSeat";
    }

    @GetMapping("/addvehicle")
    public String addvehicle() {
        return "AddVehicle";
    }

    @GetMapping("/layoutdetail")
    public String layoutdetail() {
        return "LayoutDetail";
    }

    @GetMapping("/editseat")
    public String editseat() {
        return "EditSeat";
    }

    @GetMapping("/addroute")
    public String addroute() {
        return "AddRoute";
    }

    @GetMapping("/addlocation")
    public String addlocation() {
        return "AddLocation";
    }

    @GetMapping("/routedetail")
    public String routedetail() {
        return "RouteDetail";
    }

    @GetMapping("/editroute")
    public String editlocation() {
        return "EditLocation";
    }
}
