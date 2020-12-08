package io.moomin.controller;

import io.moomin.domain.Account;
import io.moomin.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("findAll")
    public String findAll(Model model) {
        System.out.println("表现层，查询所有账户...");
        //调用service方法
        List<Account> all = accountService.findAll();
        model.addAttribute("list", all);
        return "list";
    }

    @RequestMapping("save")
    public void save(Account account, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("表现层，保存账户...");
        //调用service方法
        accountService.saveAccount(account);
        response.sendRedirect(request.getContextPath()+"/account/findAll");
        return;
    }
}
