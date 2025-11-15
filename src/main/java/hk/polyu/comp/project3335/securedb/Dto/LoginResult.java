package hk.polyu.comp.project3335.securedb.Dto;

public record LoginResult(
        String token,
        String role,
        Long studentId,
        Long guardianId,
        Long staffId
) {}