package com.im.qtec.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.im.qtec.R;
import com.im.qtec.utils.SupportMultipleScreensUtil;

/**
 * @author zhouyanglei
 */
public class EmoNormalGrid extends GridView implements OnItemClickListener {

	private OnEmoClickListener listener;
	private Context context;
	private int curPosition;
	private int startResId;
	// private int endResId;
	private float density;
	private int square;

	public EmoNormalGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context);
	}

	public EmoNormalGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context);
	}

	public EmoNormalGrid(Context context) {
		super(context);
		initializeView(context);
	}

	private void initializeView(Context context) {
		this.context = context;
		setNumColumns(7);
		//density = context.getResources().getDisplayMetrics().density;
		setVerticalSpacing(SupportMultipleScreensUtil.getScaleValue(30));
		square = SupportMultipleScreensUtil.getScaleValue(25);
		setGravity(Gravity.CENTER);
		setOnItemClickListener(this);
		setSelector(new BitmapDrawable());
	}

	public void initializeData(int position, OnEmoClickListener listener) {
		this.listener = listener;
		this.curPosition = position;
		startResId = position * 20 + R.mipmap.smiley_00;
		// endResId = (position+1)*20 + R.drawable.smiley_00;
		setAdapter(new EmoAdapter());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position != 20) {
			int res = position + curPosition * 20;
			listener.onNormalEmoClick("smiley_" + (res < 10 ? "0" + res : res), startResId + position);
		} else {
			listener.onDeleteButtonClick();
		}
	}

	class EmoAdapter extends BaseAdapter {

		private ViewHolder holder;

		@Override
		public int getCount() {
			return 21;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null || convertView.getTag() == null) {
				holder = new ViewHolder();
				LinearLayout layout = new LinearLayout(context);
				layout.setGravity(Gravity.CENTER);
				holder.emoticon = new ImageView(context);
				holder.emoticon.setLayoutParams(new LayoutParams(square, square));
				layout.addView(holder.emoticon);
				convertView = layout;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position != 20) {
				holder.emoticon.setImageResource(startResId + position);
			} else {
				holder.emoticon.setImageResource(R.drawable.selector_btn_delete);
			}
			return convertView;

		}

	}

	static class ViewHolder {
		public ImageView emoticon;
	}

	public  interface OnEmoClickListener {
		void onDeleteButtonClick();
		void onNormalEmoClick(String emo, int resId);
	}
}
