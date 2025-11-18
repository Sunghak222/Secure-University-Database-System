package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.Dto.StaffDecryptedDto;
import hk.polyu.comp.project3335.securedb.model.Guardian;
import hk.polyu.comp.project3335.securedb.model.Staff;
import hk.polyu.comp.project3335.securedb.repository.GuardianRepository;
import hk.polyu.comp.project3335.securedb.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    @Value("${app.crypto.key}")
    private String cryptoKey;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Optional<StaffDecryptedDto> findByEmail(String email) {
        return staffRepository.findDecryptedByEmail(email, cryptoKey)
                .map(StaffDecryptedDto::from);
    }
    public Optional<StaffDecryptedDto> findById(Long id) {
        return staffRepository.findDecryptedById(id, cryptoKey)
                .map(StaffDecryptedDto::from);
    }
}
