package com.dreamflow.api.media.dto;

import com.dreamflow.api.song.entity.UploadStatus;

public record UploadResponse(long jobId, String title, UploadStatus status){ }
