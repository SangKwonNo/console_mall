package com.console.mall.controller;

import com.console.mall.dto.MemberDTO;
import com.console.mall.entitiy.Address;
import com.console.mall.entitiy.Member;
import com.console.mall.respository.MemberRepository;
//import com.console.mall.service.LoginService;
import com.console.mall.service.CartService;
import com.console.mall.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final CartService cartService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        member.setEmail(form.getEmail());
        member.setLogin_id(form.getLogin_id());
        member.setPw(form.getPw());
        member.setPhone(form.getPhone());
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

//    @PostMapping("members/{id}/delete")
//    public String delete(@PathVariable Long id){
//        memberService.deleteMember(id);
//        return "redirect:/members";
//    }
    //public String loginForm(Model model){
    //    model.addAttribute("loginForm", new loginForm());
    //    return "members/loginForm";
    //}
    @GetMapping("/members/login")
        public String loginForm(){
            return "members/loginForm";
    }

    @ResponseBody
    @PostMapping("/members/login-check")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){

        Member loginResult = memberService.login(memberDTO);
        System.out.println("loginResult = " + loginResult);
        if(loginResult != null){
            session.setAttribute("id",loginResult.getLogin_id());
            Member member = memberService.findOne(memberService.getId(loginResult.getLogin_id()));
            return "yes";
        }
            return "no";

    }
    @GetMapping("/members/updateForm")
    public String updateForm(HttpSession session,Model model){
        String id = (String) session.getAttribute("id");
        Member member = memberService.findId(id);
        UpdateForm form = new UpdateForm();

        form.setId(member.getId());
        form.setName(member.getName());
        form.setCity(member.getAddress().getCity());
        form.setStreet(member.getAddress().getStreet());
        form.setZipcode(member.getAddress().getZipcode());
        form.setEmail(member.getEmail());
        form.setPhone(member.getPhone());
        form.setPw(member.getPw());
        model.addAttribute("form", form);
        return "members/updateForm";
    }
    @ResponseBody
    @PostMapping("/members/updateForm")
    public String update(HttpSession session,@ModelAttribute("form") UpdateForm form) {
        String id = (String) session.getAttribute("id");
        memberService.update(id,form.getName(),form.getCity(),form.getStreet(),form.getZipcode(),form.getEmail(),form.getPhone(),form.getPw());
        return "/";
    }

    @GetMapping("/members/logout")
    public String logout(HttpSession session ){
        session.removeAttribute("id");
        return "redirect:/";
    }
}
