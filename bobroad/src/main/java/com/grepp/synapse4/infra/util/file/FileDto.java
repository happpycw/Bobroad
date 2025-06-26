package com.grepp.synapse4.infra.util.file;

public record FileDto(
    String originFileName,
    String renameFileName,
    String savePath
) {

}
