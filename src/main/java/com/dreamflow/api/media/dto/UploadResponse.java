package com.dreamflow.api.media.dto;

import com.dreamflow.api.song.entity.UploadStatus;

public record UploadResponse(String jobId, String title, UploadStatus status){ }
