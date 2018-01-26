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
	public static Pattern pattern = Pattern.compile("\\[.{1,2}\\]");
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
			int index = getIndex(emo);
			String name = null;
			if (index < 10){
				name = "smile_0" + index;
			}else {
				name = "smile_" + index;
			}
			id = context.getResources().getIdentifier(name, "mipmap", context.getPackageName());
			if(id != 0){
				drawable = context.getResources().getDrawable(id);
				drawable.setBounds(0, 0, SupportMultipleScreensUtil.getScaleValue(emoSize),  SupportMultipleScreensUtil.getScaleValue(emoSize));
				ImageSpan span = new ImageSpan(drawable);
				builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return builder;

	}

	public static int getIndex(String emo){
		for (int i = 0; i < emotions.length; i++) {
			if (emotions[i].contains(emo)){
				return i;
			}
		}
		return -1;
	}

	public static String[] emotions = {
			"[拥抱]",
			"[微笑]",
			"[憋嘴]",
			"[发呆]",
			"[色]",
			"[得意]",
			"[流泪]",
			"[害羞]",
			"[闭嘴]",
			"[睡]",
			"[大哭]",
			"[尴尬]",
			"[发怒]",
			"[调皮]",
			"[呲牙]",
			"[惊讶]",
			"[难过]",
			"[酷]",
			"[冷汗]",
			"[折磨]",
			"[偷笑]",
			"[愉快]",
			"[白眼]",
			"[傲慢]",
			"[饥饿]",
			"[困]",
			"[惊恐]",
			"[流汗]",
			"[憨笑]",
			"[悠闲]",
			"[奋斗]",
			"[咒骂]",
			"[疑问]",
			"[嘘]",
			"[亲亲]",
			"[晕]",
			"[抓狂]",
			"[衰]",
			"[骷髅]",
			"[敲打]",
			"[再见]",
			"[擦汗]",
			"[鼓掌]",
			"[抠鼻]",
			"[坏笑]",
			"[左哼哼]",
			"[右哼哼]",
			"[哈欠]",
			"[鄙视]",
			"[委屈]",
			"[快哭了]",
			"[阴险]",
			"[吓]",
			"[可怜]",
			"[糗大了]",
			"[吐]",
			"[菜刀]",
			"[啤酒]",
			"[篮球]",
			"[乒乓]",
			"[咖啡]",
			"[吃饭]",
			"[猪头]",
			"[西瓜]",
			"[鲜花]",
			"[凋谢]",
			"[飞吻]",
			"[爱心]",
			"[心碎]",
			"[生日]",
			"[闪电]",
			"[炸弹]",
			"[威胁]",
			"[NO]",
			"[OK]",
			"[强]",
			"[弱]",
			"[勾引]",
			"[胜利]",
			"[爱你]",
			"[拳头]",
			"[差劲]",
			"[握手]",
			"[抱拳]"};
}
