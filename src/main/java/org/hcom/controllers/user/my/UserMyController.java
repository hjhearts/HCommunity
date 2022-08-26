package org.hcom.controllers.user.my;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.LoginUser;
import org.hcom.models.user.dtos.SessionUser;
import org.hcom.services.user.my.UserMyService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Controller
public class UserMyController {

    private final UserMyService userMyService;

    @GetMapping("/my/{username}")
    public String userMyPage(@PathVariable("username") String username, @LoginUser SessionUser sessionUser, Model model) {
        if(sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        if(!username.equals(sessionUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        model.addAttribute("sessionUser", sessionUser);
        return "user/my/my-page";
    }

    @GetMapping("/my/article/{username}")
    public String userMyArticlePage(@PathVariable("username") String username, @LoginUser SessionUser sessionUser, Model model,
                                    @RequestParam(required = false) String search, @RequestParam(required = false) Long page) {
        if(!username.equals(sessionUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO PERMISSION");
        }
        int requestPage;
        if(page == null) {
            requestPage = 0;
        } else {
            requestPage = (int) (page - 1);
        }
        model.addAttribute("articleList", userMyService.getArticleListByUser(requestPage, sessionUser, search));
        model.addAttribute("sessionUser", sessionUser);
        return "user/my/my-article";
    }

    @GetMapping("/my/reply/{username}")
    public String userMyReplyPage(@PathVariable("username") String username, @LoginUser SessionUser sessionUser, Model model) {
        return "user/my/my-reply";
    }

    @GetMapping("/my/like/{username}")
    public String userMyLikePage(@PathVariable("username") String username, @LoginUser SessionUser sessionUser, Model model) {
        return "user/my/my-like";
    }
}