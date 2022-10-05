package org.acme.entity;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG;
import static org.mapstruct.ReportingPolicy.IGNORE;
import static org.mapstruct.ReportingPolicy.WARN;

@MapperConfig(
        componentModel = "cdi",
        implementationName = "<CLASS_NAME>Impl",
        unmappedSourcePolicy = IGNORE,
        unmappedTargetPolicy = WARN,
        mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface Mappable {
}
