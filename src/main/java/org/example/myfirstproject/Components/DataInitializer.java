package org.example.myfirstproject.Components;

import jakarta.transaction.Transactional;
import org.example.myfirstproject.Models.Entities.Offering;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.OfferingRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final OfferingRepository offeringRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, OfferingRepository offeringRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.offeringRepository = offeringRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initializeAdmin();
        initializeOfferings();
    }

    private void initializeAdmin() {
        if (userRepository.findByUsername("Stambeto_81").isEmpty()) {
            User masterAdmin = new User();
            masterAdmin.setFirstName("Stanimir");
            masterAdmin.setLastName("Stoev");
            masterAdmin.setEmail("stambqsko@gmail.com");
            masterAdmin.setPhoneNumber("+359895495497");
            masterAdmin.setUsername("Stambeto_81");
            masterAdmin.setPassword(passwordEncoder.encode("Stambeto07"));
            masterAdmin.setRole(RoleEnum.ADMIN);

            userRepository.save(masterAdmin);
            System.out.println("✅ Admin user created.");
        }
    }

    private void initializeOfferings() {
        if (offeringRepository.count() == 0) {
            offeringRepository.save(new Offering("Overnight Stay", new BigDecimal("100"), "One night stay at our hotel."));
            offeringRepository.save(new Offering("Breakfast", new BigDecimal("10"), "Delicious breakfast included."));
            offeringRepository.save(new Offering("Lunch", new BigDecimal("30"), "Three-course meal for lunch."));
            offeringRepository.save(new Offering("Dinner", new BigDecimal("20"), "Enjoy a tasty dinner."));
            offeringRepository.save(new Offering("Parking", new BigDecimal("20"), "Secure parking for your vehicle."));

            System.out.println("✅ Default offerings added.");
        }
    }
}
