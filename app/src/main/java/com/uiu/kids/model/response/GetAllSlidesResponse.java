package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.Slide;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetAllSlidesResponse extends BaseResponse {

	@SerializedName("slides")
	private List<Slide> slide;

	public void setSlide(List<Slide> slide){
		this.slide = slide;
	}

	public List<Slide> getSlide(){
		Collections.sort(slide,SlideSerialComparator);
		return slide;
	}

	@Override
 	public String toString(){
		return 
			"GetAllSlidesResponse{" +
			"slide = '" + slide + '\'' + 
			"}";
		}

	public static Comparator<Slide> SlideSerialComparator
			= (slide1, slide2) -> {

                int slideSerial1 = slide1.getSerial();
                int slideSerial2 = slide2.getSerial();

                int slideType1 = slide1.getType();
                int slideType2 = slide2.getType();
                int type = ((Integer)slideType1).compareTo(slideType2);
                if(type!=0){
                	return type;
				}

                //ascending order
                return ((Integer)slideSerial1).compareTo(slideSerial2);


            };
}