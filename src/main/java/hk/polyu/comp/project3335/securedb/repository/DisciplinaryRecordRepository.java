package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.DisciplinaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisciplinaryRecordRepository extends JpaRepository<DisciplinaryRecord, Long> {

    List<DisciplinaryRecord> findDisciplinaryRecordsByStudentId(Long studentId);
    List<DisciplinaryRecord> findDisciplinaryRecordsByStaffId(Long staffId);
    List<DisciplinaryRecord> findDisciplinaryRecordsByStudentIdAndStaffId(Long studentId, Long staffId);
}
