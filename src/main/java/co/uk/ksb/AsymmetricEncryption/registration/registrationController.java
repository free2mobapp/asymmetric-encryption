package co.uk.ksb.AsymmetricEncryption.registration;

import co.uk.ksb.AsymmetricEncryption.crypto.RSAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rest")
public class registrationController {

    @Autowired
    RSAService encryption;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public UserRequest register(@RequestBody UserRequest userRequest) {
        System.out.println(userRequest.toString());
        String encText = encryption.encryptToBase64(userRequest.getSecureContext());
        System.out.println("Enc txt " + userRequest.getSecureContext() +", env value " + encText);
        System.out.println("Decrypted txt is ["  + encryption.decryptFromBase64(encText) + "]");

        return userRequest;
    }

    @GetMapping(value = "/intro")
    public String intro() {
        System.out.println("Inside intro");
        return "SUCCESS";
    }
}
