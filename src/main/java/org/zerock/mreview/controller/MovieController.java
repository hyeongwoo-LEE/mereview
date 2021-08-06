package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.mreview.dto.AuthMemberDTO;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.service.MovieService;

@RequestMapping("/movie")
@Log4j2
@Controller
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/register")
    public String register(){
        return "/movie/register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute MovieDTO movieDTO, RedirectAttributes redirectAttributes){
        Long mno = movieService.register(movieDTO);
        redirectAttributes.addFlashAttribute("mno", mno);
        return "redirect:/movie/list";
    }

    @GetMapping("/list")
    public String list(@ModelAttribute PageRequestDTO requestDTO, Model model){

        PageResultDTO<MovieDTO, Object[]> result = movieService.getList(requestDTO);
        model.addAttribute("result", result);
        return "/movie/list";
    }

    @GetMapping("/read")
    public String read(@RequestParam("mno") Long mno, Model model,
                       @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                       @AuthenticationPrincipal AuthMemberDTO authMemberDTO){

        MovieDTO dto = movieService.getMovie(mno);

        model.addAttribute("dto",dto);
        model.addAttribute("authMemberDTO", authMemberDTO);
        return "/movie/read";
    }
}
