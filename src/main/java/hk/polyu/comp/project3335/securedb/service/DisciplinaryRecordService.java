package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.model.DisciplinaryRecord;
import hk.polyu.comp.project3335.securedb.repository.DisciplinaryRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaryRecordService {
    
    private final DisciplinaryRecordRepository disciplinaryRecordRepository;

    public DisciplinaryRecordService(DisciplinaryRecordRepository disciplinaryRecordRepository) {
        this.disciplinaryRecordRepository = disciplinaryRecordRepository;
    }

    public DisciplinaryRecord create(Long studentId,LocalDate date, Long staffId,  String descriptions) {
        DisciplinaryRecord record = new DisciplinaryRecord(studentId, date, staffId, descriptions);
        return disciplinaryRecordRepository.save(record);
    }

   public Optional<DisciplinaryRecord> update(Long id, LocalDate date, String description) {
        Optional<DisciplinaryRecord> recordOpt = disciplinaryRecordRepository.findById(id);
        if (recordOpt.isEmpty()) {
            return Optional.empty();
        }
        
        DisciplinaryRecord record = recordOpt.get();
        record.setDate(date);
        record.setDescriptions(description);
        return Optional.of(disciplinaryRecordRepository.save(record));
    }

    public boolean delete(Long id) {
        if (!disciplinaryRecordRepository.existsById(id)) {
            return false;
        }
        disciplinaryRecordRepository.deleteById(id);
        return true;
    }


    public List<DisciplinaryRecord> listByStudent(Long studentId) {
        return disciplinaryRecordRepository.findDisciplinaryRecordsByStudentId(studentId);
    }

    public Optional<DisciplinaryRecord> getById(Long id) {
        return disciplinaryRecordRepository.findById(id);
    }

    public List<DisciplinaryRecord> listAll() {
        return disciplinaryRecordRepository.findAll();
    }
}
