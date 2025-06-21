package jonathan.modern_design._shared.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Schema(description = "Paginated response wrapper")
public record PaginatedResponse<T>(
        @Schema(description = "List of data items for the current page")
        List<T> data,

        @Schema(description = "Pagination metadata: total count, current page, page size, etc.")
        Pagination pagination,

        @Schema(description = "Additional metadata (optional)")
        Map<String, Object> metadata,

        @Schema(description = "HATEOAS links")
        List<Response.Link> links,

        @Schema(description = "Available client-side actions")
        List<Response.Action> actions
) {
    @Builder
    public PaginatedResponse {
    }

    @Schema(description = "Pagination info")
    public record Pagination(
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
    }
}

