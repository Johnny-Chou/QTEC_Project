package com.im.qtec.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author zhouyanglei
 */
public class EmoParser {
	public static Pattern pattern = Pattern.compile("\\[smiley_(.*?)\\]");
	public static SpannableStringBuilder parseEmo(Context context, String content,int emoSize) {
		// [smiley_00]sss[smiley_00]xx[smiley_00][smiley_00]
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		Matcher matcher = pattern.matcher(content);
		String emo = null;
		int id = 0;
		//int emoSize = SupportMultipleScreensUtil.getScaleValue(20);
		Drawable drawable = null;
		while (matcher.find()) {
			emo = matcher.group();
			emo = emo.substring(1, emo.length() - 1);
			id = context.getResources().getIdentifier(emo, "mipmap", context.getPackageName());
			if(id != 0){
				drawable = context.getResources().getDrawable(id);
				drawable.setBounds(0, 0, SupportMultipleScreensUtil.getScaleValue(emoSize),  SupportMultipleScreensUtil.getScaleValue(emoSize));
				ImageSpan span = new ImageSpan(drawable);
				builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return builder;

	}
}
