package pl.adiks.securitycontext.controller;

import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HelloController {

    // Obtaining the SecurityContext from the SecurityContextHolder
    @GetMapping("/hello/v1")
    public String hello1() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        return "Hello " + authentication.getName() + " !";
    }

    // Spring injects Authentication value in the parameter of the method
    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        return "Hello " + authentication.getName() + " !";
    }

    // Defining a Callable object and executing it as a task on a separate thread
    @GetMapping("/ciao")
    public String ciao() throws Exception {
        Callable<String> task = () -> {
            SecurityContext context = SecurityContextHolder.getContext();
            return context.getAuthentication().getName();
        };

        // Defining an ExecutorService and submitting the task
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            // Running the task decorated by DelegatingSecurityContextCallable
            DelegatingSecurityContextCallable<String> contextTask = new DelegatingSecurityContextCallable<>(task);
            return "Ciao " + executorService.submit(contextTask).get() + " !";
        } finally {
            executorService.shutdown();
        }
    }

    // Propagating the SecurityContext
    @GetMapping("/hola")
    public String hola() throws Exception {
        Callable<String> task = () -> {
            SecurityContext context = SecurityContextHolder.getContext();
            return context.getAuthentication().getName();
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService = new DelegatingSecurityContextExecutorService(executorService);
        try {
            return "Hola " + executorService.submit(task).get() + " !";
        } finally {
            executorService.shutdown();
        }
    }
}
