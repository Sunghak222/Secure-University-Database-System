package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.Dto.DisciplinaryRecordDecryptedDto;
import hk.polyu.comp.project3335.securedb.model.DisciplinaryRecord;
import hk.polyu.comp.project3335.securedb.repository.DisciplinaryRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaryRecordService {
    
    private final DisciplinaryRecordRepository disciplinaryRecordRepository;
    private static final Logger logger = LoggerFactory.getLogger(DisciplinaryRecordService.class);

    @Value("${app.crypto.key}")
    private String cryptoKey;

    public DisciplinaryRecordService(DisciplinaryRecordRepository disciplinaryRecordRepository) {
        this.disciplinaryRecordRepository = disciplinaryRecordRepository;
    }

    public DisciplinaryRecordDecryptedDto create(Long studentId, LocalDate date, Long staffId, String descriptions) {
        logger.info("Disciplinary Record for studentId = {} create requested", studentId);

        disciplinaryRecordRepository.insertEncrypted(studentId, date, staffId, descriptions, cryptoKey);

        Long id = disciplinaryRecordRepository.findLastInserted().orElseThrow();

        return disciplinaryRecordRepository.findDecryptedById(id, cryptoKey)
                .map(DisciplinaryRecordDecryptedDto::from)
                .orElse(null);
    }

    public Optional<DisciplinaryRecordDecryptedDto> update(Long id, LocalDate date, String descriptions) {

        logger.info("Disciplinary Record update requested: recordId={}", id);

        disciplinaryRecordRepository.updateEncrypted(id, date, descriptions, cryptoKey);

        return disciplinaryRecordRepository.findDecryptedById(id, cryptoKey)
                .map(DisciplinaryRecordDecryptedDto::from);
    }

    public boolean delete(Long id) {
        logger.info("Disciplinary Record delete requested: recordId={}", id);

        if (!disciplinaryRecordRepository.existsById(id)) {
            return false;
        }
        disciplinaryRecordRepository.deleteById(id);
        return true;
    }

    public List<DisciplinaryRecordDecryptedDto> listByStudent(Long studentId) {

        return disciplinaryRecordRepository.findDecryptedByStudentId(studentId, cryptoKey).stream()
                .map(DisciplinaryRecordDecryptedDto::from)
                .toList();
    }

    public Optional<DisciplinaryRecordDecryptedDto> getById(Long id) {
        return disciplinaryRecordRepository.findDecryptedById(id, cryptoKey)
                .map(DisciplinaryRecordDecryptedDto::from);
    }

    public List<DisciplinaryRecordDecryptedDto> listAll() {
        return disciplinaryRecordRepository.findDecryptedAll(cryptoKey).stream()
                .map(DisciplinaryRecordDecryptedDto::from)
                .toList();
    }
}
