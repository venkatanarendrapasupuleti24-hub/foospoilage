import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SpoilageService {

    public double calculateRisk(LocalDate entry, LocalDate expiry) {
        long totalDays = ChronoUnit.DAYS.between(entry, expiry);
        long usedDays = ChronoUnit.DAYS.between(entry, LocalDate.now());

        if (totalDays <= 0) return 1.0;

        return Math.round((double) usedDays / totalDays * 100.0) / 100.0;
    }
}

