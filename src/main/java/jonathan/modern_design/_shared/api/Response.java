package jonathan.modern_design._shared.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Schema(description = "Generic response wrapper")
public record Response<T>(
        @Schema(description = "Main data payload")
        T data,

        @Schema(description = "Additional metadata (optional)")
        Map<String, Object> metadata,

        @Schema(description = "HATEOAS links")
        List<Link> links,

        @Schema(description = "Available client-side actions")
        List<Action> actions
) {
    public Response(T data) {
        this(data, Map.of(), List.of(), List.of());
    }

    @Schema(description = "Hypermedia link")
    public record Link(
            String linkId,
            String href,
            String method
    ) {
    }

    @Schema(description = "Client-side executable action")
    public record Action(
            String actionId,
            String method,
            String href
    ) {
    }

    public static class Builder<T> {
        private T data;
        private Map<String, Object> metadata = Map.of();
        private List<Link> links = List.of();
        private List<Action> actions = List.of();

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder<T> links(List<Link> links) {
            this.links = links;
            return this;
        }

        public Builder<T> actions(List<Action> actions) {
            this.actions = actions;
            return this;
        }

        //Wither
        public Response<T> withDefaultMetadataV1() {
            this.metadata = Map.of("version", "1.0.0", "retrievedAt", Instant.now().toString()); //(Object) java.time.LocalDateTime.now()
            return build();
        }

        public Response<T> withDefaultMetadataV2() {
            this.metadata = Map.of("version", "2.0.0", "retrievedAt", Instant.now().toString()); //(Object) java.time.LocalDateTime.now()
            return build();
        }

        public Response<T> build() {
            return new Response<>(data, metadata, links, actions);
        }
    }
}

