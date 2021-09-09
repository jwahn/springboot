package com.koscom.springboot.web;

import com.koscom.springboot.config.auth.dto.SessionUser;
import com.koscom.springboot.config.auth.login.LoginUser;
import com.koscom.springboot.domain.user.User;
import com.koscom.springboot.service.PostsService;
import com.koscom.springboot.web.dto.posts.PostsResponseDto;
import com.koscom.springboot.web.dto.posts.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
//    private final HttpSession httpSession;

    @GetMapping("/") //localhost:8080
    public String index(Model model, @LoginUser SessionUser user) {
        //SessionUser user = (SessionUser) httpSession.getAttribute("user"); // (1)

        postsService.save(new PostsSaveRequestDto("test", "test", "test"));
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) { // (2)
            model.addAttribute("user", user);
        }
        return "index"; //index.mustache 라고 할 필요가 없음 (자동으로 templates 밑에 있는 index.xxx 를 찾는다)
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    //localhost:8080/posts/update/1 << 1번 글의 수정 화면으로 이동하게 됩니다.
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
