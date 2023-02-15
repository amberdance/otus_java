package ru.otus.solid.atm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ATMMeta {

    @NonNull String hardwareId;
    @NonNull String version;
    String contactCenter;
    @NonNull String corporation;
}
