package com.zosh.controller;

import com.zosh.config.JwtProvider;
import com.zosh.domain.AccountStatus;
import com.zosh.exception.SellerException;
import com.zosh.modal.Seller;
import com.zosh.modal.SellerReport;
import com.zosh.modal.VerificationCode;
import com.zosh.repository.SellerReportRepository;
import com.zosh.repository.VerificationCodeRepository;
import com.zosh.request.LoginRequest;
import com.zosh.response.ApiResponse;
import com.zosh.response.AuthResponse;
import com.zosh.service.AuthService;
import com.zosh.service.EmailService;
import com.zosh.service.SellerReportService;
import com.zosh.service.SellerService;
import com.zosh.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("sellers")
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;
    private final SellerReportService sellerReportService;



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {

        String otp = req.getOtp();
        String email = req.getEmail();
        req.setEmail("seller_"+ email);



        AuthResponse authResponse= authService.signing(req);
        return ResponseEntity.ok(authResponse);
    }
        @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp)
            throws Exception{
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("wrong otp");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(),otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);

        }

        @PostMapping
     public ResponseEntity<Seller> createSeller(@RequestBody Seller seller)
            throws Exception, MessagingException {

            Seller savedSeller = sellerService.createSeller(seller);

            String otp = OtpUtil.generateOtp();

            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setOtp(otp);
            verificationCode.setEmail(seller.getEmail());
            verificationCodeRepository.save(verificationCode);

            String subject = "Market Bazaar Email Verification Code";
            String text = "Welcome to Deepanshu Market, verify your account using this link";
            String frontend_url = "http://localhost:3000/verify-sller/";
            emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(),
                  subject,text + frontend_url  );
            return new ResponseEntity<>(savedSeller,HttpStatus.CREATED);
        }
        @GetMapping("/{id}")
      public ResponseEntity<Seller> getSellerById(@PathVariable Long id)
            throws SellerException {

            Seller seller = sellerService.getSellerById(id);
            return new ResponseEntity<>(seller, HttpStatus.OK);
        }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(
            @RequestHeader("Authorization") String jwtHeader) throws Exception {

        if (!jwtHeader.startsWith("Bearer ")) {
            throw new Exception("Invalid JWT Token format");
        }

        String jwt = jwtHeader.substring(7); // Remove "Bearer " prefix

        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(
            @RequestHeader("Authorization") String jwt) throws Exception{
        Seller seller= sellerService.getSellerProfile(jwt);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return new ResponseEntity<>(report,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(
            @RequestParam(required = false)AccountStatus status){
                List<Seller> sellers = sellerService.getAllSellers(status);
                return ResponseEntity.ok(sellers);
    }


    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String jwtHeader,
            @RequestBody Seller seller) throws Exception {

        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            throw new Exception("Invalid JWT Token format");
        }

        String jwt = jwtHeader.substring(7); // Extract token
        System.out.println("Received JWT: " + jwt); // Debug log

        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(
            @PathVariable Long id) throws Exception{
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
        }




}
