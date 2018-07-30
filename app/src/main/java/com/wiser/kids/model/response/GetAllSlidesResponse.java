package com.wiser.kids.model.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.wiser.kids.model.SlideItem;

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