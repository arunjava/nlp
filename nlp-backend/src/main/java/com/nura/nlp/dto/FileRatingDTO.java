package com.nura.nlp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileRatingDTO {

	private String fileName;
	private float fileRatings;
	private String sentiment;
}
