package com.dians.raising_stocks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

  @GetMapping
  @RequestMapping(value = {"", "/", "/home", "/companies"})
  // Returns the static Companies page.
  public String getCompaniesPage(Model model) {
    model.addAttribute("title", "Companies");
    model.addAttribute("bodyContent", "companies");
    return "master-template";
  }

  @GetMapping("/stocks")
  // Returns the static Stocks page.
  public String getStocksPage(Model model) {
    model.addAttribute("title", "Stocks");
    model.addAttribute("bodyContent", "stocks");
    return "master-template";
  }
}