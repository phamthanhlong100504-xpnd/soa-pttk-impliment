package com.vn.tech.booking_tour_service.dto.response.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkflowStatusResponse {
    private String workflowId;
    private String status;
    private String result;
}
