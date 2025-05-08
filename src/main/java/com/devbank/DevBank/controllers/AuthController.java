package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.EmailOrCpfVerificationDTO;
import com.devbank.DevBank.dtos.LoginDTO;
import com.devbank.DevBank.dtos.LoginVerifyDTO;
import com.devbank.DevBank.dtos.UserRegisterDTO;
import com.devbank.DevBank.exeptions.*;
import com.devbank.DevBank.services.EmailVerifyService;
import com.devbank.DevBank.services.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private EmailVerifyService emailVerifyService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO data) {
        System.out.println(data.getEmail());
        try {
            Map<String, String> response = userAuthService.userRegister(data);
            return ResponseEntity.status(201).body(Map.of("message", response.get("message")));
        } catch (EmailAlreadyRegisteredException | CpfAlreadyRegisteredException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error Interno do Servidor" + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody  LoginDTO data, HttpServletRequest request){
        try{
            String clientIp = request.getRemoteAddr();
            Map<String, String>  response = userAuthService.loginUser(data, clientIp);
            return ResponseEntity.status(200).body(Map.of("message", response.get("message"), "email", response.get("email")));
        } catch (AccountBlockedException | IncorrectPasswordException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (UserNotFoundException e){
          return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }  catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error Interno do Servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/login/2fa")
    public ResponseEntity<?> loginUser2fa(@RequestBody LoginVerifyDTO data){
        try{
            Map<String, String>  response = userAuthService.verifyCodeAndFinishLogin(data);
            return ResponseEntity.status(200).body(response);
        } catch (InvalidCodeException e){
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (UserNotFoundException e){
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error Interno do Servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/login/2fa/resend")
    public ResponseEntity<?> resend2FACode(@RequestBody Map<String, String> body){
        try{
            userAuthService.resend2FACode(body.get("emailOrCpf"));
            return ResponseEntity.status(200).body(Map.of("message", "Novo código 2FA enviado com sucesso!"));
        }catch (Exception e){
            System.out.println(e);
           return  ResponseEntity.status(500).body(Map.of("error","Erro ao enviar código de verificação."));
        }
    }

    @PostMapping("/register/email")
    public ResponseEntity<?> sendEmailCode(@RequestBody Map<String, String> body) {
        try{
            emailVerifyService.generateAndStoreCode(body.get("email"), body.get("name"));
            return ResponseEntity.ok(Map.of("message", "Código enviado com sucesso."));
        }catch (DuplicateKeyException e){
            return  ResponseEntity.status(409).body(Map.of("error",e.getMessage()));
        } catch (Exception e){
            return  ResponseEntity.status(500).body(Map.of("error","Erro ao enviar código de verificação."));
        }
    }

    @PostMapping("/register/email/resend")
    public ResponseEntity<?> resendEmailCode(@RequestBody Map<String, String> body) {
        try{
            emailVerifyService.generateAndStoreCode(body.get("email"), body.get("name"));
            return ResponseEntity.ok(Map.of("message", "Código reenviado com sucesso."));
        }catch (Exception e){
            return  ResponseEntity.status(500).body(Map.of("error","Erro ao enviar código de verificação."));
        }
    }
    @PostMapping("/register/email/verify")
    public ResponseEntity<?> emailVerify(@RequestBody Map<String, String> body) {
        try{
            emailVerifyService.validCode(body.get("email"), body.get("code"));
            return ResponseEntity.ok(Map.of("message", "Email Validado Com Sucesso!"));
        }catch (InvalidCodeException e){
            return  ResponseEntity.status(403).body(Map.of("error",e.getMessage()));
        } catch (Exception e){
            return  ResponseEntity.status(500).body(Map.of("error","Erro ao enviar código de verificação."));
        }
    }


    @PostMapping("/validation")
    public ResponseEntity<?> verifyEmailOrCpf(@RequestBody EmailOrCpfVerificationDTO data){
        try{
            boolean dataVerify = userAuthService.verifyEmailOrCpf(data.getEmailOrCpf());
            if(dataVerify) {
                return ResponseEntity.status(409).body(Map.of("error", "Email Ou Cpf Já em uso!"));
            }
            return  ResponseEntity.ok(Map.of("message", "Dados sem duplicidade!"));
        }catch (Exception e){
            return  ResponseEntity.status(500).body(Map.of("error","Erro verificar dados."));
        }
    }
}