package com.koscom.springboot.web;

import com.koscom.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //json 응답을 줄 수 있는 controller (API 응답을 주고 받는 거)
public class HelloController {

    @GetMapping("/hello")
    //@PostMapping, @PutMapping, ...
    public String hello() {
        return "hello";
    }

    // getA?name=A&age=20
    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name,
                                     @RequestParam("amount") int amount) {
        HelloResponseDto dto = new HelloResponseDto(name, amount);
        System.out.println("dto =" + dto);
        return new HelloResponseDto(name, amount);
    }
}
