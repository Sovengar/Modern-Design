package jonathan.modern_design._shared.infra.db.delete_table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

import static lombok.AccessLevel.PACKAGE;

@Entity
@Table(name = "deleted_rows", schema = "shared")
@Getter
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor
public class DeletedRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "origin_table", nullable = false)
    private String originTable;

    @Column(name = "origin_id", nullable = false)
    private String originId;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "reason")
    private String reason;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String data;

    public static DeletedRow of(String originTable, String originId, String deletedBy, String reason, String data) {
        return new DeletedRow(null, originTable, originId, OffsetDateTime.now(), deletedBy, reason, data);
    }
}
