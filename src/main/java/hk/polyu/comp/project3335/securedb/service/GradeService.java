package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.Dto.GradeDecryptedDto;
import hk.polyu.comp.project3335.securedb.model.Grade;
import hk.polyu.comp.project3335.securedb.repository.GradeRepository;
import hk.polyu.comp.project3335.securedb.repository.StudentRepository;
import hk.polyu.comp.project3335.securedb.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private static final Logger logger = LoggerFactory.getLogger(GradeService.class);

    @Value("${app.crypto.key}")
    private String cryptoKey;

    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public void create(Long studentId, Long courseId, String term, String grade, String comments) {
        logger.info("Grade create requested: studentId={}, courseId={}", studentId, courseId);

        // Validate that student exists
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist");
        }

        // Validate that course exists
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist");
        }

        gradeRepository.insertEncryptedGrade(
                studentId,
                courseId,
                term,
                grade,
                comments,
                cryptoKey
        );
    }
    @Transactional
    public void update(Long id, String term, String grade, String comments) {
        logger.info("Grade update requested: gradeId={}", id);

        Grade existing = gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found"));

        //term is plain
        existing.setTerm(term);
        gradeRepository.save(existing);

        gradeRepository.updateEncryptedGrade(
                id,
                grade,
                comments,
                cryptoKey
        );
    }

    public void delete(Long id) {
        logger.warn("Grade delete requested: gradeId={}", id);

        if (!gradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Grade not found");
        }
        gradeRepository.deleteById(id);
    }
    public List<GradeDecryptedDto> listByStudent(Long studentId) {
        return gradeRepository.findDecryptedByStudentId(studentId, cryptoKey)
                .stream()
                .map(GradeDecryptedDto::from)
                .toList();
    }
    public List<GradeDecryptedDto> listByStudentAndTerm(Long studentId, String term) {
        return gradeRepository.findDecryptedByStudentIdAndTerm(studentId, term, cryptoKey)
                .stream()
                .map(GradeDecryptedDto::from)
                .toList();
    }

    public List<GradeDecryptedDto> listAll() {
        return gradeRepository.findAll().stream()
                .map(g -> gradeRepository.findDecryptedById(g.getId(), cryptoKey))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(GradeDecryptedDto::from)
                .toList();
    }
    public Optional<GradeDecryptedDto> listByStudentAndCourse(Long studentId, Long courseId) {
        return gradeRepository
                .findDecryptedByStudentIdAndCourseId(studentId, courseId, cryptoKey)
                .map(GradeDecryptedDto::from);
    }
    public Optional<GradeDecryptedDto> getById(Long id) {
        return gradeRepository
                .findDecryptedById(id, cryptoKey)
                .map(GradeDecryptedDto::from);
    }
}
