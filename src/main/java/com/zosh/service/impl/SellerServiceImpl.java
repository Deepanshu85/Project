
package com.zosh.service.impl;

import com.zosh.config.JwtProvider;
import com.zosh.domain.AccountStatus;
import com.zosh.domain.USER_ROLE;
import com.zosh.exception.SellerException;
import com.zosh.modal.Address;
import com.zosh.modal.Seller;
import com.zosh.repository.AddressRepository;
import com.zosh.repository.SellerRepository;
import com.zosh.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
//    private final SellerService sellerService;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {

       String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {

        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
        if(sellerExist!=null){
            throw new Exception("seller already exist use diff email");
        }
        Address savedAddress = addressRepository.save(seller.getPickupAddresses());

        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddresses(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {

        return sellerRepository.findById(id).orElseThrow
                (()-> new SellerException("seller not found with id"+ id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {

        Seller seller = sellerRepository.findByEmail(email);
        if(seller==null){
            throw new Exception("Seller not found");
        }
        return seller;
    }

@Override
public List<Seller> getAllSellers(AccountStatus status) {
    if (status == null) {
        return sellerRepository.findAll();
    }
    return sellerRepository.findByAccountStatus(status);
}

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller existingSeller = this.getSellerById(id);

        if(seller.getSellerName()!=null){
            existingSeller.setSellerName(seller.getSellerName());
        }

        if(seller.getMobile()!=null){
            existingSeller.setMobile(seller.getMobile());
        }

        if(seller.getEmail()!=null){
            existingSeller.setEmail(seller.getEmail());
        }

        if(seller.getBusinessDetails()!=null
          && seller.getBusinessDetails().getBusinessName()!=null ){
            existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().
                    getBusinessName());
        }

        if(seller.getBankDetails()!=null
           && seller.getBankDetails().getAccountHolderName()!=null
                && seller.getBankDetails().getIfscCode()!=null
                && seller.getBankDetails().getAccountNumber()!=null
        ){
            existingSeller.getBankDetails().setAccountHolderName
                    (seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setAccountNumber(
                    seller.getBankDetails().getAccountNumber()
            );
            existingSeller.getBankDetails().setIfscCode(
                    seller.getBankDetails().getIfscCode()
            );
        }

        if(seller.getPickupAddresses()!=null
                && seller.getPickupAddresses().getAddress()!=null
                && seller.getPickupAddresses().getMobile()!=null
                && seller.getPickupAddresses().getCity()!=null
                && seller.getPickupAddresses().getState()!=null
        ){
            existingSeller.getPickupAddresses()
                            .setAddress(seller.getPickupAddresses().getAddress());
            existingSeller.getPickupAddresses().setCity(
                    seller.getPickupAddresses().getCity()
            );
            existingSeller.getPickupAddresses().setState(
                    seller.getPickupAddresses().getState()
            );
            existingSeller.getPickupAddresses().setMobile(seller.getPickupAddresses().getMobile());
            existingSeller.getPickupAddresses().setPinCode(
                    seller.getPickupAddresses().getPinCode()
            );
        }
            if(seller.getGSTIN()!=null){
             existingSeller.setGSTIN(seller.getGSTIN());
                }
        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {

        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
