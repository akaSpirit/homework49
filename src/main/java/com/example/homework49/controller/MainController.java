package com.example.homework49.controller;

import com.example.homework49.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    private final CandidateService service;
    private User enteredUser;
    private final UserModel userModel = new UserModel();
    private boolean status = true;

    public MainController(CandidateService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String candidatesHandler(Model model) {
        List<Candidate> candidates = service.getAllCandidates();
        model.addAttribute("candidates", candidates);
        model.addAttribute("status", true);
        return "candidates";
    }

    @PostMapping("/")
    public String logout(Model model) {
        enteredUser = null;
        return "login";
    }

    @PostMapping("/vote")
    public String voteHandler(@RequestParam(name = "candidateId") int candidateId, Model model) {
        if (candidateId < 1 || candidateId > 6) {
            return "notfound";
        }
        Candidate candidate = service.getCandidate(candidateId);
        if (enteredUser != null) {
            if (enteredUser.isVoted()) {
                return "redirect:/voted";
            } else {
                candidate.setVote(candidate.getVote() + 1);
                double voteSum = service.getAllCandidates().stream().mapToInt(Candidate::getVote).sum();
                service.getAllCandidates().forEach(e -> e.setVotePercent((e.getVote() / voteSum) * 100));
                enteredUser.setVoted(true);
                userModel.getUsers().put(enteredUser.getUsername(), enteredUser);
                FileService.writeUsers(userModel.getUsers());
                FileService.writeCandidates(service.getAllCandidates());
                model.addAttribute("candidate", candidate);
                return "thankyou";
            }
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/voted")
    public String votedHandler() {
        return "voted";
    }

    @RequestMapping("/votes")
    public String votesHandler(Model model) {
        List<Candidate> candidates = service.getAllCandidates().stream()
                .sorted(Comparator.comparing(Candidate::getVote)).toList();
        model.addAttribute("candidates", candidates);
        return "votes";
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        enteredUser = null;
        model.addAttribute("status", status);
        return "login";
    }

    @PostMapping("/login")
    public String loginHandler(@RequestBody String bodyRow, Model model) {
        Map<String, String> body = Utils.parseUrlEncoded(bodyRow, "&");
        String username = body.get("username");
        String password = body.get("password");
        if (userModel.getUsers().containsKey(username)) {
            if (userModel.getUsers().get(username).getPassword().equals(password)) {
                enteredUser = userModel.getUsers().get(username);
                status = true;
                model.addAttribute("status", status);
                return "redirect:/";
            } else {
                enteredUser = null;
                status = false;
                model.addAttribute("status", status);
                return "redirect:/login";
            }
        } else {
            enteredUser = null;
            status = false;
            model.addAttribute("status", status);
            return "redirect:/login";
        }
    }

}
