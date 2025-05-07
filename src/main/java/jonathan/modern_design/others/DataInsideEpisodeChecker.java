package jonathan.modern_design.others;

import jonathan.modern_design._common.tags.DomainService;

import java.time.LocalDateTime;

@DomainService
public class DataInsideEpisodeChecker {
    public boolean compareWithNotificationDate(LocalDateTime keyDateLDT, Integer durationOfEpisodeInDays, LocalDateTime notificationDateLDT) {
        var notificationDate = notificationDateLDT.toLocalDate();
        var startingEpisodeDate = keyDateLDT.toLocalDate();
        var lastValidDateOfCase = startingEpisodeDate.plusDays(durationOfEpisodeInDays);

        var isEqualsOrAfter = notificationDate.isAfter(startingEpisodeDate) || notificationDate.isEqual(startingEpisodeDate);
        var isEqualsOrBefore = notificationDate.isBefore(lastValidDateOfCase) || notificationDate.isEqual(lastValidDateOfCase);
        return isEqualsOrAfter && isEqualsOrBefore;
    }

    public boolean compareWithAnotherKeyDate(LocalDateTime keyDateOfCaseLDT, Integer durationOfEpisodeInDays, LocalDateTime arrivedKeyDateLDT) {
        var keyDateOfCase = keyDateOfCaseLDT.toLocalDate();
        var arrivedKeyDate = arrivedKeyDateLDT.toLocalDate();

        var lastValidDateOfCase = keyDateOfCase.plusDays(durationOfEpisodeInDays);
        var lastValidDateOfArrivedKeyDate = arrivedKeyDate.plusDays(durationOfEpisodeInDays);

        var isEqualsOrBefore = arrivedKeyDate.isBefore(lastValidDateOfCase) || arrivedKeyDate.isEqual(lastValidDateOfCase);
        var isEqualsOrAfter = lastValidDateOfArrivedKeyDate.isAfter(keyDateOfCase) || lastValidDateOfArrivedKeyDate.isEqual(keyDateOfCase);
        return isEqualsOrBefore && isEqualsOrAfter;
    }
}

