package huynhtanhao.hutech.haohuynh.controllers;

import huynhtanhao.hutech.haohuynh.models.Contact;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact/form";
    }

    @PostMapping("/submit")
    public String submitForm(@ModelAttribute Contact contact, Model model) {
        // Set current date
        contact.setSubmissionDate(LocalDate.now());

        // Pass to result view
        model.addAttribute("contact", contact);
        return "contact/result";
    }
}
