package cn.iocoder.yudao.module.childhealth.api.report.dto;
import jakarta.validation.constraints.NotNull; import lombok.Data; import java.time.LocalDate;
public final class ChildHealthReportDTO { private ChildHealthReportDTO() {} @Data public static class StatisticsRequest { @NotNull private LocalDate startDate; @NotNull private LocalDate endDate; private Long batchId; } @Data public static class ExportRequest extends StatisticsRequest { private String fileName; } }
