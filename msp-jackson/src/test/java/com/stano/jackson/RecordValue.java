package com.stano.jackson;

import java.time.LocalDate;
import java.util.UUID;

public record RecordValue(int id, String name, LocalDate birthDate, UUID uuid) {}
