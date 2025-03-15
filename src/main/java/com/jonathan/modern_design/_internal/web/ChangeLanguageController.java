package com.jonathan.modern_design._internal.web;

import com.jonathan.modern_design._internal.config.annotations.WebAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@WebAdapter
@RequestMapping("/internal/changeLanguage")
@RequiredArgsConstructor
public class ChangeLanguageController {
    private final LocaleResolver localeResolver;

    @GetMapping()
    public String changeLanguage(@RequestParam("lang") String language, HttpServletRequest request, HttpServletResponse response) {
        Locale locale = new Locale(language);
        localeResolver.setLocale(request, response, locale);
        return "redirect:/";
    }
}

