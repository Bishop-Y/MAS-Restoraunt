package org.cafe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperationWrapper implements Serializable {
    private int bundleId;

    private Operation operation;
}
