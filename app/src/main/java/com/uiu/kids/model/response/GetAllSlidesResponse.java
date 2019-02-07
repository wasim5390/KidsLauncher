package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.Slide;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetAllSlidesResponse extends BaseResponse {

	@SerializedName("slides")
	private List<Slide> slides;

	public void setSlide(List<Slide> slide){
		this.slides = slide;
	}

	public List<Slide> getSlide(){
		for(Slide slide:slides){
			int count=1;
			for(Slide slide1:slides){
				if(slide.getType()==slide1.getType() && slide.getId()!=slide1.getId())
					count++;
			}
			slide.setCount(count);
		}
		Collections.sort(slides,SlideSerialComparator);
		return slides;
	}

	@Override
	public String toString(){
		return
				"GetAllSlidesResponse{" +
						"slide = '" + slides + '\'' +
						"}";
	}

	public static Comparator<Slide> SlideSerialComparator
			= (slide1, slide2) -> {



		Integer slideType1 = slide1.getType();
		Integer slideType2 = slide2.getType();
		int type = (slideType1).compareTo(slideType2);
		if(type!=0){
			return type;
		}
		int slideSerial1 = slide1.getSerial();
		int slideSerial2 = slide2.getSerial();
		//ascending order
		return ((Integer)slideSerial1).compareTo(slideSerial2);


	};
}