package com.dreamflow.api.song.dto;
import org.springframework.core.io.Resource;

public record StreamResponse(Resource resource, long start, long end, long fileLength, boolean isParital) {
}
