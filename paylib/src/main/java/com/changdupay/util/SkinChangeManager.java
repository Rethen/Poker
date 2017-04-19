package com.changdupay.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsSeekBar;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.changdupay.android.lib.R;
import com.changdupay.util.SkinSheet.Opt;
import com.changdupay.util.SkinSheet.SkinSite;

public class SkinChangeManager {
	private static SkinChangeManager sSkinChangeManager;
	
	private int mCommonRed;
	private float[] mColorMatrix;
	private ColorMatrixColorFilter mColorFilter;

	private SkinChangeManager() {
		mCommonRed = ContextUtil.getContext().getResources().getColor(MResource.getIdByName(((Activity)ContextUtil.getContext()).getApplication(), "color", "ipay_red"));
		mColorMatrix = new float[] { (float)-0.462661291695106,(float)1.85405438826175,(float)-0.391393096566648,0,25,(float)0.328567922656998,(float)0.36650017390965,(float)0.304931903433351,0,25,(float)0.863317922656998,(float)1.03180438826175,(float)-0.895122310918753,0,25,0,0,0,1,0 };
//		mColorMatrix = new float[] { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };
//		mColorMatrix = new float[] { (float)-0.194592577052952,(float)1.34844748741306,(float)-0.153854910360111,0,0,(float)0.33450640304692,(float)0.4610950068356,(float)0.20439859011748,0,0,(float)0.609630891440116,(float)0.925406607410623,(float)-0.535037498850739,0,0,0,0,0,1,0 };
//		mColorMatrix = new float[] { (float)0.920581200717975,(float)0.625997795891016,(float)-0.546578996608991,0,0,(float)-0.0245517898410415,(float)0.831635780886154,(float)0.192916008954888,0,0,(float)0.543351063317016,(float)-0.247229171868148,(float)0.703878108551132,0,0,0,0,0,1,0 };
		mColorFilter = new ColorMatrixColorFilter(new ColorMatrix(mColorMatrix));
	}
	
	public static SkinChangeManager getInstance() {
		if (sSkinChangeManager == null) {
			sSkinChangeManager = new SkinChangeManager();
		}

		return sSkinChangeManager;
	}
	
	public void setColorMatrix(float[] colorMatrix)
	{
		mColorMatrix = colorMatrix;
		if (mColorMatrix != null)
			mColorFilter = new ColorMatrixColorFilter(new ColorMatrix(mColorMatrix));
		else
			mColorFilter = null;
	}

	/**
	 * 5x4 matrix for transforming the color+alpha components of a Bitmap. The matrix is stored in a single array, and
	 * its treated as follows: [ a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t ]
	 * 
	 * When applied to a color [r, g, b, a], the resulting color is computed as (after clamping) R' = a*R + b*G + c*B +
	 * d*A + e; G' = f*R + g*G + h*B + i*A + j; B' = k*R + l*G + m*B + n*A + o; A' = p*R + q*G + r*B + s*A + t;
	 */
	public int getSkinColor(int color) {
		int desColor = color;

		if (mColorMatrix != null && mColorMatrix.length == 20) {
			int r = Color.red(color), g = Color.green(color);
			int b = Color.blue(color), a = (Integer.toHexString(color).length() == 6 ? 0xff : Color.alpha(color));
			int tmp[] = new int[4];
			for (int i = 0; i < 20; i += 5) {
				int p = i / 5;
				tmp[p] = (int) (mColorMatrix[i + 0] * r + mColorMatrix[i + 1] * g + mColorMatrix[i + 2] * b
						+ mColorMatrix[i + 3] * a + mColorMatrix[i + 4]);
				if (tmp[p] < 0) {
					tmp[p] = 0;
				}
			}

			desColor = Color.argb(tmp[3], tmp[0], tmp[1], tmp[2]);
		}

		return desColor;
	}
	
	public int getSkinColor() {
		return getSkinColor(mCommonRed);
	}
	
	private static int sFlagDraw;
	private static int getDrawFlag() {
		if (sFlagDraw == 0) {
			try {
				Field drawField = View.class.getDeclaredField("DRAWN");
				if (drawField != null) {
					drawField.setAccessible(true);

					sFlagDraw = drawField.getInt(null);
					if (sFlagDraw == 0) {
						sFlagDraw = 0x00000020;
					}
				}
			} catch (Exception e) {
				// do nothing
			}
		}

		return sFlagDraw;
	}
	
	public static void invalidate(View view) {
		if (view != null) {
			try {
				Field pfField = View.class.getDeclaredField("mPrivateFlags");
				if(pfField!=null){
					pfField.setAccessible(true);
					
					pfField.setInt(view, pfField.getInt(view) | getDrawFlag());
					view.invalidate();
				}
			} catch (Exception e) {
				//Log.e(e);
			}
		}
	}
	
	public static final boolean isRunningInMainThread(){
		return Thread.currentThread()==Looper.getMainLooper().getThread();
	}
	
	public void onChangeSkin(Activity activity, boolean isReset) {
		if (activity != null && isRunningInMainThread()) {
			try {
				Class<? extends SkinSheet> siteClazz = SkinSheet.getSheetClass().get(SkinSheet.getKey(activity.getClass()));
				if (siteClazz != null) {
					onChangeSkin(siteClazz.newInstance(), activity, isReset);
				}
			} catch (Throwable t) {
				//Log.e(t);
			}

		}
	}

	public <O> void onChangeSkin(String sheetCode, O o, boolean isReset) {
		if (!TextUtils.isEmpty(sheetCode) && o != null && isRunningInMainThread()) {
			try {
				Class<? extends SkinSheet> siteClazz = SkinSheet.getSheetClass().get(sheetCode);
				onChangeSkin(siteClazz.newInstance(), o, isReset);
			} catch (Throwable t) {
				//Log.e(t);
			}
		}
	}

	public <O> void onChangeSkin(Class<? extends SkinSheet> siteClazz, O o, boolean isReset) {
		if (siteClazz != null && o != null && isRunningInMainThread()) {
			try {
				onChangeSkin(siteClazz.newInstance(), o, isReset);
			} catch (Throwable t) {
				//Log.e(t);
			}
		}
	}

	private <O> void onChangeSkin(SkinSheet sheet, O o, boolean isReset) {
		if (sheet != null && o != null && isRunningInMainThread()) {
			ArrayList<SkinSite> sites = sheet.obtainSites();
			if (sites != null && !sites.isEmpty()) {
				for (SkinSite site : sites) {
					if (site != null) {
						if (site.opt == Opt.DRAWABLE) {
							changeDrawable(site.id, isReset);
						} else if (Opt.is(site.opt, Opt.VIEW_GROUP)) {
							changeViewGroupResource(findViewById(o, site), site.opt, isReset, sheet.getVPath(site.id),
									0);
						} else {
							changeViewResource(findViewById(o, site), site.opt, isReset);
						}
					}
				}
			}
		}
	}

	public void changeViewResource(View view, int opt, boolean isReset) {
		if (view != null) {
			if (Opt.is(opt, Opt.TEXT_COLOR)) {
				changeTextColor(view, isReset);
			} else if (Opt.is(opt, Opt.CHARACTER_STYLE)) {
				changeColorSpan(view);
			}

			if (Opt.is(opt, Opt.BG_DRAWABLE)) {
				changeBackgroundDrawable(view, isReset);

			} else if (Opt.is(opt, Opt.BG_COLOR)) {
				changeBackGroundColor(view, isReset);
			}

			if (Opt.is(opt, Opt.SRC_DRAWABLE)) {
				changeSrcDrawable(view, isReset);

			} else if (Opt.is(opt, Opt.SRC_COLOR)) {
				changeSrcColor(view, isReset);

			} else if (Opt.is(opt, Opt.COMPOUND_DRAWABLE)) {
				changeCompoundDrawables(view, isReset);

			} else if (Opt.is(opt, Opt.SPECIFY)) {
				//changeSpecifyDrawable(view, isReset);

			} else if (Opt.is(opt, Opt.COMPOUND_BUTTON)) {
				changeCompoundButtonDrawable(view, isReset);

			} else if (Opt.is(opt, Opt.PROGRESS)) {
				changeProgressResource(view, isReset);

			} else if (Opt.is(opt, Opt.LISTVIEW_SELECTOR)) {
				changeListViewSelector(view, isReset);
			}
		}
	}

	public void changeTextColor(View view, boolean isReset) {
		if (view != null && view instanceof TextView) {
			if (!changeColorSpan(view)) {
				setTextColorFilter((TextView) view, isReset);
			}

			invalidate(view);
		}
	}

	public void changeBackGroundColor(View view, boolean isReset) {
		if (view != null) {
			Drawable bgDrawable = view.getBackground();
			if (bgDrawable != null && bgDrawable instanceof ColorDrawable) {
				view.setBackgroundColor(getSkinColor());
			}

			invalidate(view);
		}
	}

	public void changeDrawable(int resId, boolean isReset) {
		if (resId > 0) {
			changeDrawable(ContextUtil.getContext().getResources().getDrawable(resId), isReset);
		}
	}

	public void changeDrawable(Drawable drawable, boolean isReset) {
		setDrawableColorFilter(drawable, isReset);
	}
	
	public boolean changeColorSpan(View view) {
		boolean result = false;
		if (view != null && view instanceof TextView) {
			CharSequence cs = ((TextView) view).getText();
			if (cs != null) {
				if (cs instanceof CharacterStyle) {
					changeColorSpan((CharacterStyle) cs);
					
					result = true;
				} else if (cs instanceof Spannable) {
					changeColorSpan((Spannable) cs);
					
					result = true;
				}
			}
		}

		return result;
	}

	public void changeColorSpan(Spannable span) {
		if (span != null) {
			if (span instanceof SpannableString) {
				SpannableString spString = (SpannableString) span;
				ForegroundColorSpan[] fcSpans = spString.getSpans(0, span.length(), ForegroundColorSpan.class);
				if (fcSpans != null && fcSpans.length > 0) {
					for (ForegroundColorSpan fc : fcSpans) {
						changeColorSpan(fc);
					}
				}

				BackgroundColorSpan[] bcSpans = spString.getSpans(0, span.length(), BackgroundColorSpan.class);
				if (bcSpans != null && bcSpans.length > 0) {
					for (BackgroundColorSpan bc : bcSpans) {
						changeColorSpan(bc);
					}
				}
			}
		}
	}
	
	public void changeColorSpan(CharacterStyle span) {
		if (span != null) {
			try {
				Class<? extends CharacterStyle> clazz = span.getClass();
				Field colorFiled = clazz.getDeclaredField("mColor");
				if (colorFiled != null) {
					colorFiled.setAccessible(true);

					colorFiled.setInt(span, getSkinColor());
				}
			} catch (Exception e) {
				//Log.e(e);
			}
		}
	}

	public void changeBackgroundDrawable(View view, boolean isReset) {
		if (view != null) {
			setDrawableColorFilter(view.getBackground(), isReset);

			invalidate(view);
		}
	}

	public void changeSrcDrawable(View view, boolean isReset) {
		if (view != null) {
			if (view instanceof ImageView) {
				setDrawableColorFilter(((ImageView) view).getDrawable(), isReset);
			}

			invalidate(view);
		}
	}

	public void changeSrcColor(View view, boolean isReset) {
		if (view != null) {
			if (view instanceof ImageView) {
				Drawable srcDrawable = ((ImageView) view).getDrawable();
				if (srcDrawable != null && srcDrawable instanceof ColorDrawable) {
					((ImageView) view).setImageDrawable(new ColorDrawable(getSkinColor()));
				}
			}

			invalidate(view);
		}
	}

	public void changeCompoundDrawables(View view, boolean isReset) {
		if (view != null && view instanceof TextView) {
			Drawable[] drawables = ((TextView) view).getCompoundDrawables();
			if (drawables != null) {
				for (Drawable draw : drawables) {
					setDrawableColorFilter(draw, isReset);
				}
			}

			invalidate(view);
		}
	}

//	public void changeSpecifyDrawable(View view, boolean isReset) {
//		if (view != null && view instanceof ViewSpecify) {
//			ViewSpecify specify = (ViewSpecify) view;
//			specify.setColorFilter(mColorFilter);
//			
//			ViewHelper.invalidate(view);
//		}
//	}

	public void changeListViewSelector(View view, boolean isReset) {
		if (view != null) {
			if (view instanceof AbsListView) {
				setDrawableColorFilter(((AbsListView) view).getSelector(), isReset);
			}
		}
	}

	public void changeCompoundButtonDrawable(View view, boolean isReset) {
		if (view != null) {
			try {
				Field drawFiled = null;
				if (view instanceof CompoundButton) {
					drawFiled = CompoundButton.class.getDeclaredField("mButtonDrawable");

				} else if (view instanceof CheckedTextView) {
					drawFiled = CheckedTextView.class.getDeclaredField("mCheckMarkDrawable");
				}

				if (drawFiled != null) {
					drawFiled.setAccessible(true);

					Drawable btnDraw = (Drawable) drawFiled.get(view);
					changeDrawable(btnDraw, isReset);
				}
			} catch (Exception e) {
				//Log.e(e);
			}

			invalidate(view);
		}
	}

	public void changeProgressResource(View view, boolean isReset) {
		if (view != null) {
			if (view instanceof ProgressBar) {
				changeDrawable(((ProgressBar) view).getProgressDrawable(), isReset);
			}

			if (view instanceof SeekBar) {
				try {
					Field thumbField = AbsSeekBar.class.getDeclaredField("mThumb");
					if (thumbField != null) {
						thumbField.setAccessible(true);

						changeDrawable((Drawable) thumbField.get(view), isReset);
					}
				} catch (Exception e) {
					//Log.e(e);
				}
			}

			invalidate(view);
		}
	}

	public void changeViewGroupResource(View view, int opt, boolean isReset, Class<? extends View>[] vPath, int index) {
		if (view != null && vPath != null && index < vPath.length) {
			if (index == vPath.length - 1) {
				if (view != null && vPath[index].isInstance(view)) {
					changeViewResource(view, opt, isReset);
				}
			} else {
				if (vPath[index].isInstance(view) && view instanceof ViewGroup) {
					int count = ((ViewGroup) view).getChildCount();
					if (count > 0) {
						++index;
						for (int i = 0; i < count; i++) {
							View cView = ((ViewGroup) view).getChildAt(i);
							if (cView != null && vPath[index].isInstance(cView)) {
								changeViewGroupResource(cView, opt, isReset, vPath, index);
							}
						}
					}
				}
			}
		}
	}

	private void setDrawableColorFilter(Drawable drawable, boolean isReset) {
		if (drawable != null) {
			ColorFilter colorFilter = isReset ? null : mColorFilter;

			drawable.setColorFilter(colorFilter);

			if (drawable instanceof DrawableContainer) {
				DrawableContainer dContainer = (DrawableContainer) drawable;
				DrawableContainerState dcState = (DrawableContainerState) dContainer.getConstantState();
				Drawable[] childDraws = dcState.getChildren();
				if (childDraws != null) {
					for (Drawable draw : childDraws) {
						if (draw != null) {
							draw.setColorFilter(colorFilter);
						}
					}
				}
			}
		}
	}

	private void setTextColorFilter(TextView textView, boolean isReset) {
		if (textView != null) {
			ColorFilter filter = isReset ? null : mColorFilter;
			textView.getPaint().setColorFilter(filter);

			if (textView instanceof EditText) {
				try {
					Field fHPaint = TextView.class.getDeclaredField("mHighlightPaint");
					if (fHPaint != null) {
						fHPaint.setAccessible(true);

						Paint hPaint = (Paint) fHPaint.get(textView);
						hPaint.setColorFilter(filter);
					}
				} catch (Exception e) {
					//Log.e(e);
				}
			}
		}
	}

	private int getColorDrawableColor(ColorDrawable colorDrawable) {
		int color = 0;
		if (colorDrawable != null) {
			ConstantState colorState = ((ColorDrawable) colorDrawable).getConstantState();
			if (colorState != null) {
				Class<? extends ConstantState> clazz = colorState.getClass();
				if (clazz != null) {
					try {
						Field colorField = clazz.getDeclaredField("mUseColor");
						if (colorField != null) {
							colorField.setAccessible(true);

							color = colorField.getInt(colorState);
						}
					} catch (Exception e) {
						//Log.e(e);

//						ThemeData themeData = ThemeConfig.getInstance().getThemeData(SkinLoader.getSkinName());
//						if (themeData != null && themeData.type == ThemeType.matrix) {
//							color = Color.parseColor(themeData.icon);
//						}
					}
				}
			}
		}

		return color;
	}

	private <O> View findViewById(O o, SkinSite site) {
		View view = null;
		if (o != null && site != null) {
			if (site.parent > 0) {
				View pView = findViewById(o, site.parent);
				if (pView != null) {
					view = pView.findViewById(site.id);
				}
			} else {
				view = findViewById(o, site.id);
			}
		}

		return view;
	}

	private <O> View findViewById(O o, int id) {
		View view = null;
		if (o != null && id > 0) {
			if (o instanceof Activity) {
				view = ((Activity) o).findViewById(id);
			} else if (o instanceof View) {
				view = ((View) o).findViewById(id);
			}
		}

		return view;
	}
}
