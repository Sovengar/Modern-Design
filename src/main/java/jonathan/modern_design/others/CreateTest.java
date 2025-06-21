package jonathan.modern_design.others;

import jonathan.modern_design._shared.tags.ApplicationService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static jonathan.modern_design.others.TestResolution.NEGATIVE_TEST_ON_EXISTING_CASE;
import static jonathan.modern_design.others.TestResolution.NEGATIVE_TEST_WHEN_NO_CASE;
import static jonathan.modern_design.others.TestResolution.NEGATIVE_TEST_WITH_EXPIRED_CASE;
import static jonathan.modern_design.others.TestResolution.NOT_DONE_TEST_ON_EXISTING_CASE;
import static jonathan.modern_design.others.TestResolution.NOT_DONE_TEST_WHEN_NO_CASE;
import static jonathan.modern_design.others.TestResolution.NOT_DONE_TEST_WITH_EXPIRED_CASE;
import static jonathan.modern_design.others.TestResolution.POSITIVE_TEST_ON_EXISTING_CASE;
import static jonathan.modern_design.others.TestResolution.POSITIVE_TEST_WHEN_NO_CASE;
import static jonathan.modern_design.others.TestResolution.POSITIVE_TEST_WITH_EXPIRED_CASE;
import static jonathan.modern_design.others.TestResolution.POSITIVE_VALID_TEST_WHEN_NO_CASE;


@Getter
@RequiredArgsConstructor
enum SampleRating {
    VALID("Valida"),
    NOT_VALID("noValida");

    private final String code;
}

enum TestResolution {
    POSITIVE_TEST_WHEN_NO_CASE,
    POSITIVE_VALID_TEST_WHEN_NO_CASE,
    NEGATIVE_TEST_WHEN_NO_CASE,
    NOT_DONE_TEST_WHEN_NO_CASE,

    POSITIVE_TEST_ON_EXISTING_CASE,
    NEGATIVE_TEST_ON_EXISTING_CASE,
    NOT_DONE_TEST_ON_EXISTING_CASE,

    POSITIVE_TEST_WITH_EXPIRED_CASE,
    POSITIVE_VALID_TEST_WITH_EXPIRED_CASE,
    NEGATIVE_TEST_WITH_EXPIRED_CASE,
    NOT_DONE_TEST_WITH_EXPIRED_CASE
}

@Getter
@RequiredArgsConstructor
enum SampleResultEnum {
    POSITIU("7f7600a7-45a2-459b-a4f1-9f97612d422e", "10828004"),
    NEGATIU("da4fd25e-7c47-4429-bc06-fef683f1532d", "260385009"),
    NO_REALITZAT("36982a55-a80c-4b28-a4c0-72d4e575dacd", "262008008"),
    ;

    private final String id;
    private final String code;

    public static SampleResultEnum fromId(String id) {
        for (SampleResultEnum result : SampleResultEnum.values()) {
            if (result.getId().equals(id)) {
                return result;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + id);
    }

    public static SampleResultEnum fromCode(String code) {
        for (SampleResultEnum result : SampleResultEnum.values()) {
            if (result.getCode().equals(code)) {
                return result;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}


record Patient(UUID id, String name) {
}

record Disease(UUID id, String name, int daysBetweenPeriods) {
}

@Data
class createTestCommand {
    private UUID id;
    private UUID patientId;
    private UUID microorganismId;
    private SampleRating sampleRating;
    private String labSampleTechniqueResultId;
    private UUID sampleTypeId;

    //100 more fields...
}

@Data
class AntibioticDto {
    private UUID id;
    private String name;
    private String dosage;
}

@ApplicationService
@Slf4j
@RequiredArgsConstructor
public class CreateTest {
    private final CaseFinder caseFinder;
    private final TestAlgorithmResolver testAlgorithmResolver;

    public createTestCommand create(createTestCommand request, List<AntibioticDto> antibiotics) {
        var patient = new Patient(UUID.randomUUID(), "Patient"); //patientWebClientService.getPatientData(request.getIdPatient()).orElseThrow();
        var disease = new Disease(UUID.randomUUID(), "Disease", 15); //findDiseaseFromClinicalEntityMicroSimple(request.getIdMicroorganism());

        var existsCase = caseFinder.existsCaseWithoutConsideringIfExpired(patient.id(), disease.name());
        Optional<String> caseId = existsCase ? fetchCaseIdIfExists(patient, disease) : empty();

        var principalDates = extractPrincipalDates(request);

        var fieldsForCalculation = new TestCalculationFields(
                isValidMicroorganismTechnique(request),
                isValidSample(request.getMicroorganismId(), request.getSampleTypeId()),
                request.getLabSampleTechniqueResultId(),
                existsCase,
                new TemporalFields(principalDates.getKeyDate(), ofNullable(disease.daysBetweenPeriods()).orElse(0), principalDates.getResultNotificationDate())
        );

        return processWithAlgorithm(new TestCreatorData(request, caseId, fieldsForCalculation, disease, patient, antibiotics));
    }

    //Meat and Potatoes here, function prepared to be unit-testable
    //Could be improved, returning the id instead of the mutated Dto
    private createTestCommand processWithAlgorithm(final TestCreatorData testCreatorData) {
        var testResolution = testAlgorithmResolver.calculateResult(testCreatorData.getFieldsForCalculation());

        return switch (testResolution) {
            case NEGATIVE_TEST_WITH_EXPIRED_CASE -> processWhenNegativeTest(testCreatorData.getLabTestDto());
            case POSITIVE_TEST_WHEN_NO_CASE, POSITIVE_TEST_WITH_EXPIRED_CASE, NEGATIVE_TEST_WHEN_NO_CASE, NOT_DONE_TEST_WITH_EXPIRED_CASE,
                 NOT_DONE_TEST_WHEN_NO_CASE, NOT_DONE_TEST_ON_EXISTING_CASE ->
                    createTestAndAssociateAntibiotics(testCreatorData.getLabTestDto(), testCreatorData.getAntibiotics());
            case POSITIVE_VALID_TEST_WHEN_NO_CASE -> processWhenValidPositiveTest(testCreatorData);
            case POSITIVE_TEST_ON_EXISTING_CASE, NEGATIVE_TEST_ON_EXISTING_CASE -> processWhenCaseAlreadyExists(testCreatorData);
            default -> throw new IllegalArgumentException("Invalid case option");
        };
    }

    private createTestCommand processWhenCaseAlreadyExists(final TestCreatorData testCreatorData) {
        return null;
    }

    private createTestCommand processWhenValidPositiveTest(final TestCreatorData testCreatorData) {
        return null;
    }

    private createTestCommand createTestAndAssociateAntibiotics(final createTestCommand labTestDto, final List<AntibioticDto> antibiotics) {
        return null;
    }

    private createTestCommand processWhenNegativeTest(final createTestCommand labTestDto) {
        return null;
    }

    private PrincipalDates extractPrincipalDates(final createTestCommand request) {
        var now = LocalDateTime.now();
        return new PrincipalDates(now, now, now, now, now);
    }

    private boolean isValidMicroorganismTechnique(final createTestCommand request) {
        return true;
    }

    private boolean isValidSample(final UUID idMicroorganism, final UUID idSampleType) {
        return true;
    }

    private Optional<String> fetchCaseIdIfExists(Patient patient, Disease disease) {
        return Optional.of("123");
    }

    //Move to domainService if it needs to be reused
    @Service
    @RequiredArgsConstructor
    static class TestAlgorithmResolver {
        private final DataInsideEpisodeChecker dataInsideEpisodeChecker;

        public TestResolution calculateResult(final TestCalculationFields testCalculationFields) {
            var sampleResult = SampleResultEnum.fromId(testCalculationFields.getSampleTechniqueId());

            var temporalFields = testCalculationFields.getTemporalFields();
            var caseExpired = testCalculationFields.isExistsCase() && !dataInsideEpisodeChecker.compareWithNotificationDate(temporalFields.getKeyDate(), temporalFields.getDaysBetweenPeriods(), temporalFields.getNotificationDate());

            if (caseExpired) {
                return resolveWhenCaseExistsButPeriodEnded(testCalculationFields.isValidMicroorganismTechnique(), testCalculationFields.isValidMicroSampleClinicalEntity(), sampleResult);
            } else if (testCalculationFields.isExistsCase()) {
                return resolveWhenCaseExists(sampleResult);
            } else if (SampleResultEnum.POSITIU == sampleResult) {
                return resolveWhenPositiveTest(testCalculationFields.isValidMicroorganismTechnique(), testCalculationFields.isValidMicroSampleClinicalEntity());
            } else if (SampleResultEnum.NEGATIU == sampleResult) {
                return NEGATIVE_TEST_WHEN_NO_CASE;
            } else if (SampleResultEnum.NO_REALITZAT == sampleResult) {
                return NOT_DONE_TEST_WHEN_NO_CASE;
            } else {
                throw new IllegalArgumentException("Cannot compute result of this test");
            }
        }

        private TestResolution resolveWhenCaseExists(final SampleResultEnum sampleResult) {
            return switch (sampleResult) {
                case SampleResultEnum.POSITIU -> POSITIVE_TEST_ON_EXISTING_CASE;
                case SampleResultEnum.NEGATIU -> NEGATIVE_TEST_ON_EXISTING_CASE;
                case SampleResultEnum.NO_REALITZAT -> NOT_DONE_TEST_ON_EXISTING_CASE;
            };
        }

        private TestResolution resolveWhenPositiveTest(final boolean validMicroorganismTechnique, final boolean validMicroSampleClinicalEntity) {
            if (validMicroorganismTechnique && validMicroSampleClinicalEntity) {
                return POSITIVE_VALID_TEST_WHEN_NO_CASE;
            } else {
                return POSITIVE_TEST_WHEN_NO_CASE;
            }
        }

        private TestResolution resolveWhenCaseExistsButPeriodEnded(final boolean validMicroorganismTechnique, final boolean validMicroSampleClinicalEntity, final SampleResultEnum sampleResult) {
            return switch (sampleResult) {
                case SampleResultEnum.POSITIU -> {
                    if (validMicroorganismTechnique && validMicroSampleClinicalEntity) {
                        yield TestResolution.POSITIVE_VALID_TEST_WITH_EXPIRED_CASE;
                    } else {
                        yield POSITIVE_TEST_WITH_EXPIRED_CASE;
                    }
                }
                case SampleResultEnum.NEGATIU -> NEGATIVE_TEST_WITH_EXPIRED_CASE;
                case SampleResultEnum.NO_REALITZAT -> NOT_DONE_TEST_WITH_EXPIRED_CASE;
            };
        }
    }

}

@Value
class TestCalculationFields {
    boolean validMicroorganismTechnique;
    boolean validMicroSampleClinicalEntity;
    String sampleTechniqueId;
    boolean existsCase;
    TemporalFields temporalFields;

    public TestCalculationFields(boolean validMicroorganismTechnique, boolean validMicroSampleClinicalEntity, String sampleTechniqueId, boolean existsCase, TemporalFields temporalFields) {
        this.validMicroorganismTechnique = validMicroorganismTechnique;
        this.validMicroSampleClinicalEntity = validMicroSampleClinicalEntity;
        this.sampleTechniqueId = requireNonNull(sampleTechniqueId, "El id de la tècnica no pot estar buit");
        this.existsCase = existsCase;
        this.temporalFields = requireNonNull(temporalFields, "Els camps temporals no poden estar buits");
    }
}

@Value
class TemporalFields {
    LocalDateTime keyDate;
    int daysBetweenPeriods;
    LocalDateTime notificationDate;

    public TemporalFields(LocalDateTime keyDate, int daysBetweenPeriods, LocalDateTime notificationDate) {
        this.keyDate = requireNonNull(keyDate, "Data clau no pot estar buida");
        this.daysBetweenPeriods = daysBetweenPeriods;
        this.notificationDate = requireNonNull(notificationDate, "Data notificacio no pot estar buida");
    }
}

@Value
class TestCreatorData {
    createTestCommand labTestDto;
    Optional<String> idCase;
    TestCalculationFields fieldsForCalculation;
    Disease disease;
    Patient patient;
    List<AntibioticDto> antibiotics;
}

@Value
class CreateCaseCommand {
    createTestCommand createTestCommandResponse;
    Disease disease;
    Patient patient;
}

@Value
class PrincipalDates {
    LocalDateTime dataRecollidaDeLaMostra;
    LocalDateTime resultNotificationDate;
    LocalDateTime caseNotificationDate;
    LocalDateTime diagnosisDate;
    LocalDateTime keyDate;
}
