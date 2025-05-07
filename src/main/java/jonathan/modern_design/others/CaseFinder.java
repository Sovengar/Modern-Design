package jonathan.modern_design.others;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

record Cases(String id) {
}

@Service
@RequiredArgsConstructor
public class CaseFinder {
    //private final IFindCasesService findCasesService;
    private final DataInsideEpisodeChecker dataInsideEpisodeChecker;


    public List<Cases> findCases(UUID patientId, String diseaseName) {
        //final List<Cases> casesByPatient = findCasesService.findCasesByIdPatient(patientId);
        //return casesByPatient.stream().filter(caseEntity -> caseEntity.getDisease().getName().equalsIgnoreCase(diseaseName)).collect(Collectors.toList());
        return null;
    }

    public List<Cases> findCasesInPeriod(UUID patientId, Integer daysBetweenPeriod, String diseaseName, LocalDateTime arrivedKeyDate) {
        //final List<Cases> casesByPatient = findCasesService.findCasesByIdPatient(patientId);

//        return casesByPatient.stream().filter(caseEntity -> {
//            var sameDisease = caseEntity.getDisease().getName().equalsIgnoreCase(diseaseName);
//            var isInPeriod = dataInsideEpisodeChecker.compareWithAnotherKeyDate(caseEntity.getCaseInfo().getKeyDate(), daysBetweenPeriod, arrivedKeyDate);
//            return sameDisease && isInPeriod;
//        }).collect(Collectors.toList());
        return null;
    }

    public Cases findPrincipalCase(List<Cases> cases) {
        //return cases.stream().filter(c -> "Cas principal".equalsIgnoreCase(c.getCaseType())).findFirst().orElseThrow();
        return null;
    }

    public Cases findPrincipalCase(UUID patientId, String diseaseName, Integer daysBetweenPeriod, LocalDateTime arrivedKeyDate) {
        var casesInPeriod = findCasesInPeriod(patientId, daysBetweenPeriod, diseaseName, arrivedKeyDate);
        return findPrincipalCase(casesInPeriod);
    }

    public boolean existsCaseWithoutConsideringIfExpired(UUID patientId, String diseaseName) {
        var cases = findCases(patientId, diseaseName);
        return !cases.isEmpty();
    }
}

