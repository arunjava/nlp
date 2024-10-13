package com.nura.nlp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SynonymDTO {

	private String word;
	private long score;

}
