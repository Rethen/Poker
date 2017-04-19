package com.poseidon.pokers.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poseidon.pokers.R;

/**
 * Created by huangys on 2016/6/30.
 */
public class TitlebarView extends LinearLayout implements View.OnClickListener {
    private ImageView rightBtn;
    private TextView tvTitle;
    private OnClickListener backViewListener;
    private OnClickListener rightBtnListener;

    public TitlebarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 必须使用临时变量，避免放在Fragment中时直接调用findViewById失败的问题
        View view = LayoutInflater.from(context).inflate(R.layout.titlebar_view, this, true);

        rightBtn = (ImageView) view.findViewById(R.id.right_btn);
        rightBtn.setOnClickListener(this);

        tvTitle = (TextView) view.findViewById(R.id.back);
        tvTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.back) {
            if ( backViewListener != null) {
                backViewListener.onClick(v);
            }
        } else if (id == R.id.right_btn) {
            if ( rightBtnListener != null) {
                rightBtnListener.onClick(v);
            }
        }
    }

    public void setBackViewListener(OnClickListener listener) {
        backViewListener = listener;
    }

    public void setRightBtnListener(OnClickListener listener) {
        rightBtnListener = listener;
    }

    /**
     * 设置title
     *
     * @param resId
     */
    public void setTitle(int resId) {
        if (tvTitle != null) {
            tvTitle.setText(getResources().getString(resId));
        }
    }

    public void setText(String strText) {
        if (tvTitle != null) {
            tvTitle.setText(strText);
        }
    }

    public void setTitleWithRightBtn(int resId, int iconResId) {
        setTitle(resId); // set title

        if (rightBtn != null) {
            rightBtn.setVisibility(VISIBLE);
            rightBtn.setImageDrawable(getResources().getDrawable(iconResId));
        }
    }

    public void setTitleWithRightBtn(String strTittle, int iconResId) {
        setText(strTittle); // set title

        if (rightBtn != null) {
            rightBtn.setVisibility(VISIBLE);
            rightBtn.setImageDrawable(getResources().getDrawable(iconResId));
        }
    }
}
