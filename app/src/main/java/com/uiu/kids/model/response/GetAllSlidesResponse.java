package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.SlideItem;

import java.util.List;

public class GetAllSlidesResponse extends BaseResponse {

	@SerializedName("slides")
	private List<SlideItem> slide;

	public void setSlide(List<SlideItem> slide){
		this.slide = slide;
	}

	public List<SlideItem> getSlide(){
		return slide;
	}

	@Override
 	public String toString(){
		return 
			"GetAllSlidesResponse{" +
			"slide = '" + slide + '\'' + 
			"}";
		}
}