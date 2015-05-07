package com.global.component;

import java.util.ArrayList;
import java.util.List;

import com.global.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class GenericInputBox extends RelativeLayout{
	//内容输入框
	private EditText mInputContent;
	//清空按钮
	private ImageButton mClearButton;
	//最右侧功能按钮,如搜索
	private ImageButton mFuncButton;
	//输入内容名称
	private TextView mInputName;
	//额外功能按钮
	private ImageButton mLastImgBtn;
	
	//输入名称
	private String inputName;
	//名称大小
	private float inputNameTextSize;
	//输入内容字体大小
	private float inputTextSize;
	//输入内容字体颜色
	private int inputTextColor;
	//输入类型
	private int inputType;
	//id
	private int id;
	//功能按钮图片背景
	private Drawable funcBtnGg;
	//功能按钮可见性
	private boolean funcBtnVisiable;
	//输入内容最大长度
	private int maxlength;
	//提示信息
	private String hintString;
	//提示信息颜色
	private int hintTextColor;
	//是否是金额输入框
	private boolean isAlipayMoney;
	//额外图片按钮，如跳转到联系人
	private Drawable lastImgBtnBg;
	//是否需要显示清除按钮
	private boolean isNeedShowClearButton = true;
	//位数分隔表
	private String mSeparateList;
	private int bgType;
	private List<Integer> separateIntList;
	private boolean isBold;
	
	private OnClickListener mCleanButtonListener;
	private SpiliteTextWather mSpiliteTextWather;
	
	public enum BgType{
		TOP(0x1),CENTER(0x2),BOTTOM(0x3),NORMAL(0x4);
		
		private int bgType;
		
		BgType(int bgType){
			this.bgType = bgType;
		}

		public int getBgType() {
			return bgType;
		}

		public void setBgType(int bgType) {
			this.bgType = bgType;
		}
	}
	
	public GenericInputBox(Context context){
		super(context);
		
		afterInflate();
	}
	
	public GenericInputBox(Context context, AttributeSet set) {
		super(context, set);
		LayoutInflater.from(context).inflate(R.layout.generic_inputbox, this, true);
		float defaultSize = context.getResources().getDimension(R.dimen.defaultFontSize);
		TypedArray a = context.obtainStyledAttributes(set, R.styleable.genericInputBox);
		inputName = a.getString(R.styleable.genericInputBox_inputName);
		inputNameTextSize = a.getDimension(R.styleable.genericInputBox_inputNameTextSize, defaultSize);
		inputTextSize = a.getDimension(R.styleable.genericInputBox_inputTextSize, defaultSize);
		inputTextColor = a.getColor(R.styleable.genericInputBox_inputTextColor, Color.BLACK);
		inputType = a.getInt(R.styleable.genericInputBox_inputType, 0x1);
		maxlength = a.getInt(R.styleable.genericInputBox_maxLength, -1);
		id = a.getInt(R.styleable.genericInputBox_inputId,0);
		hintString = a.getString(R.styleable.genericInputBox_inputHint);
		hintTextColor = a.getColor(R.styleable.genericInputBox_inputHintTextColor, 
				getResources().getColor(R.color.colorGray));
		
		funcBtnGg = a.getDrawable(R.styleable.genericInputBox_funcBtnBg);
		funcBtnVisiable = a.getBoolean(R.styleable.genericInputBox_funcBtnVisiable, false);
		isAlipayMoney = a.getBoolean(R.styleable.genericInputBox_isAlipayMoney, false);
		
		lastImgBtnBg = a.getDrawable(R.styleable.genericInputBox_extraImgButtonBg);
		
		mSeparateList = a.getString(R.styleable.genericInputBox_separateList);
		isBold = a.getBoolean(R.styleable.genericInputBox_isBold,false);
		
		bgType = a.getInt(R.styleable.genericInputBox_bgType, 0x4);
		
		a.recycle();
		
		afterInflate();
	}
	
	private void afterInflate(){
		mInputContent  = (EditText) findViewById(R.id.content);
		mInputName = (TextView) findViewById(R.id.contentName);
		mClearButton= (ImageButton) findViewById(R.id.clearButton);
		mFuncButton = (ImageButton) findViewById(R.id.funcButton);
		mLastImgBtn = (ImageButton) findViewById(R.id.lastImgBtn);
		setInputName(inputName);
		setInputNameTextSize(inputNameTextSize);
		setInputTextSize(inputTextSize);
		setInputTextColor(inputTextColor);
		setInputId(id);
		setInputType(inputType);
		setHint(hintString);
		setHintTextColor(hintTextColor);
		setLength(maxlength);
//		addListener();
		
		mSpiliteTextWather = new SpiliteTextWather();
		addTextChangedListener(mSpiliteTextWather);
		
		addClearListener();
		setButtonVisiable(funcBtnVisiable);
		setFuncButtonBg(funcBtnGg);
		setLastImgBg(lastImgBtnBg);
		
		separateStr(mSeparateList);
		
		setApprerance(isBold);
		
		BgType bgTypeEnum = BgType.NORMAL;
		switch (bgType) {
		case 0x1:
			bgTypeEnum = BgType.TOP;
			break;
		case 0x2:
			bgTypeEnum = BgType.CENTER;
			break;
		case 0x3:
			bgTypeEnum = BgType.BOTTOM;
			break;
		case 0x4:
			bgTypeEnum = BgType.NORMAL;
			break;
		default:
			break;
		}
		
		setBgByBgType(bgTypeEnum);//根据bgType设置背景
	}
	
	private void addClearListener() {
		mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mInputContent.setText("");
				mClearButton.setVisibility(View.GONE);
				
				if(mCleanButtonListener != null){
					mCleanButtonListener.onClick(mClearButton);
				}
			}
		});
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
	}
	
	/**
	 * 设置控件背景
	 * @param bgType 背景类型
	 */
	public void setBgByBgType(BgType bgType) {
		switch (bgType.getBgType()) {
		case 0x1:
			setBackgroundResource(R.drawable.table_top);
			break;
		case 0x2:
			setBackgroundResource(R.drawable.table_center);
			break;
		case 0x3:
			setBackgroundResource(R.drawable.table_bottom);
			break;
		case 0x4:
			setBackgroundResource(R.drawable.generic_inputbox_selector);
			break;
		default:
			setBackgroundResource(R.drawable.generic_inputbox_selector);
			break;
		}
	}

	public void setApprerance(boolean isBold) {
		if(isBold){
			mInputContent.setTextAppearance(getContext(), R.style.boldStyle);
		}
	}

	/**
	 * 将显示的文字分隔开
	 */
	public void separateStr(String separateList) {
		mSeparateList = separateList;
		if(mSeparateList != null && mSeparateList.length() > 0){
			String[] separates =  mSeparateList.split(",");
			separateIntList = new ArrayList<Integer>();
			for(String curIndex : separates){
				separateIntList.add(Integer.parseInt(curIndex));
			}
		}
		
		if(separateIntList != null && separateIntList.size() > 0){
			setInputType(InputType.TYPE_CLASS_NUMBER);
		}
	}

	/**
	 * Set a special listener to be called when an action is performed on the
	 * text view. This will be called when the enter key is pressed, or when an
	 * action supplied to the IME is selected by the user. Setting this means
	 * that the normal hard key event will not insert a newline into the text
	 * view, even if it is multi-line; holding down the ALT modifier will,
	 * however, allow the user to insert a newline character.
	 * 
	 * @param l
	 */
    public void setOnEditorActionListener(OnEditorActionListener l) {
    	if (null != mInputContent) {
    		mInputContent.setOnEditorActionListener(l);
    	}
    }
    
    /**
	 * Adds a TextWatcher to the list of those whose methods are called whenever
	 * this TextView's text changes.
	 * 
	 * In 1.0, the TextWatcher.afterTextChanged method was erroneously not
	 * called after setText calls. Now, doing setText if there are any text
	 * changed listeners forces the buffer type to Editable if it would not
	 * otherwise be and does call this method.
	 * 
	 * @param watcher
	 */
    public void addTextChangedListener(TextWatcher watcher) {
    	if (null != mInputContent) {
    		mInputContent.addTextChangedListener(watcher);
    	}
    }
    
    /**
     * Register a callback to be invoked when focus of this view changed.
     */
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
    	if (null != mInputContent) {
    		mInputContent.setOnFocusChangeListener(l);
    	}
    }
    
    private final class SpiliteTextWather implements TextWatcher{
    	private String resultSpilitStr = "";
		private int start;
		private int count;
		private int before;
		private int mSelectionIndex;
		private int state;//状态记录 0 正常  ,1  清空 ,2 append 
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
				this.start = start;
				this.count = count;
				this.before = before;
		}

		@Override
		public void afterTextChanged(Editable inputedStr) {
			if (inputedStr.length() == 0) {
				setButtonVisiable(false);// 隐藏按钮
			} else {
				setButtonVisiable(true);// 显示按钮
			}

			if(isAlipayMoney){
				String temp = inputedStr.toString();
				int posDot = temp.indexOf(".");
				if (posDot == -1)
					return;

				if (temp.length() - posDot - 1 > 2) {
					inputedStr.delete(posDot + 3, posDot + 4);
				}
			}else{
				//分隔控制
				if(separateIntList != null && separateIntList.size() > 0){
					mInputContent.removeTextChangedListener(mSpiliteTextWather);
					
					String input = inputedStr.toString();
					
					//计算当前输入内容需要添加的空格数
					int blankCount = 0;
					for(int i = 0;i < input.length();i++){
						if(separateIntList.contains(i)){
							blankCount = blankCount + 1;
						}
					}
					
					String inputedString = input.replace(" ", "");
					int previousBlankCount = inputedStr.length() - inputedString.length();
					char[] newInputedChars = new char[inputedString.length() + blankCount];
					
					int blankInputed = 0;//添加了多少个空格
					for(int i = 0 ;i < newInputedChars.length ;i++){
						if(separateIntList.contains(i)){
							newInputedChars[i] = ' ';
							blankInputed++;
						}else{
							newInputedChars[i] =  inputedString.charAt(i - blankInputed);
						}
					}
					
					resultSpilitStr = new String(newInputedChars);
					setText(resultSpilitStr);
					if(blankInputed > 0){
						if(before > 0){
							mSelectionIndex = start;
						}else{
							mSelectionIndex = start - previousBlankCount + blankInputed + count;
						}
					}else{
						mSelectionIndex = mInputContent.getSelectionStart();
					}
					
					mInputContent.setSelection(mSelectionIndex);
					mInputContent.addTextChangedListener(mSpiliteTextWather);
				}
			}
		}
    	
    }
    
	public void setCleanButtonListener(OnClickListener listener){
		this.mCleanButtonListener = listener;
	}
	
	/**
	 * 清除按钮是否可见
	 * @param visiable
	 */
	public void setButtonVisiable(boolean visiable){
		if(visiable && isNeedShowClearButton){
			mClearButton.setVisibility(View.VISIBLE );
		}else
		{
			mClearButton.setVisibility(View.GONE);
		}
		if(funcBtnGg != null){
			mFuncButton.setVisibility(visiable ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * 设置文字内容
	 * @param inputContent
	 */
	public void setText(CharSequence inputContent){
		mInputContent.setText(inputContent);
		CharSequence text = mInputContent.getText();
		if (text instanceof Spannable) {
			 Spannable spanText = (Spannable)text;
			 Selection.setSelection(spanText, text.length());
		}
	}
	
	/**
	 * 获取文字内容
	 * @return
	 */
	public String getText(){
		String inputContent = mInputContent.getText().toString();
		return (mSeparateList != null && mSeparateList.length() > 0) ? inputContent.replace(" ", "") : inputContent;
	}
	
	/**
	 * 获取EditText控件
	 * @return
	 */
	public EditText getEtContent(){
		return mInputContent;
	}
	
	/**
	 * 输入内容名称
	 * @param inputName
	 */
	public void setInputName(String inputName){
		if(inputName != null && !"".equals(inputName)){
			mInputName.setText(inputName);
			mInputName.setVisibility(View.VISIBLE);
		}else{
			mInputName.setText("");
			mInputName.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 获取输入内容名称
	 * @return
	 */
	public TextView getInputName(){
		return mInputName;
	}
	
	/**
	 * 输入内容名称字体大小
	 * @param textSize
	 */
	public void setInputNameTextSize(float textSize){
		if(textSize > 0){
			mInputName.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
		}
	}
	
	
	public void setInputTextSize(float textSize){
		if(textSize > 0){
			mInputContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
		}
	}
	
	/**
	 * 输入内容文字颜色
	 * @param textColor
	 */
	public void setInputTextColor(int textColor){
		mInputContent.setTextColor(textColor);
	}
	
	/**
	 * 输入内容类型
	 * @param inputType
	 */
	public void setInputType(int inputType){
		mInputContent.setInputType(inputType);
	}
	
	/**
	 * 输入框id
	 * @param id
	 */
	public void setInputId(int id){
		if(id != 0)
			mInputContent.setId(id);
	}
	
	/**
	 * 提示信息
	 * @param hintString
	 */
	public void setHint(String hintString){
		if(hintString !=null && !"".equals(hintString))
			mInputContent.setHint(hintString);
	}
	
	/**
	 * 提示信息颜色
	 * @param textColor
	 */
	public void setHintTextColor(int textColor){
		mInputContent.setHintTextColor(textColor);
	}
	
	/**
	 * 文字长度
	 * @param maxlength
	 */
	public void setLength(int maxlength){
		if (maxlength >= 0) {
			mInputContent.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxlength) });
        } else {
        	mInputContent.setFilters(new InputFilter[0]);
        }
	}
	
	/**
	 * 功能按钮背景
	 * @param buttonBackground
	 */
	@SuppressWarnings("deprecation")
	public void setFuncButtonBg(Drawable buttonBackground) {
		if(buttonBackground != null){
			this.funcBtnGg = buttonBackground;
			setFuncButtonVisiable(true);
//			mFuncButton.setImageDrawable(buttonBackground);
			mFuncButton.setBackgroundDrawable(buttonBackground);
			mClearButton.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 功能按钮
	 * @return
	 */
	public ImageButton getFuncButton(){
		return mFuncButton;
	}
	
	/**
	 * 清除按钮
	 * @return
	 */
	public ImageButton getClearButton(){
		return mClearButton;
	}
	
	/**
	 * 功能按钮可见性
	 * @param visiable
	 */
	public void setFuncButtonVisiable(boolean visiable){
		if(funcBtnGg != null){
			mFuncButton.setVisibility(visiable ? View.VISIBLE : View.GONE);
		}
		if(isNeedShowClearButton && !visiable){
			mClearButton.setVisibility(View.VISIBLE);
		}else
		{
			mClearButton.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 设置按钮背景
	 * @param 
	 */
	@SuppressWarnings("deprecation")
	public void setLastImgBg(Drawable lastImgBtnBg) {
		if(lastImgBtnBg != null){
			this.lastImgBtnBg = lastImgBtnBg;
			setLastImgBtnVisiable(true);
			mLastImgBtn.setBackgroundDrawable(lastImgBtnBg);
		}
	}
	
	public void setLastImgBtnVisiable(boolean visiable){
		mLastImgBtn.setVisibility(visiable ? View.VISIBLE : View.GONE);
	}
	
	/**
	 * 获取最右功能按钮
	 * @return
	 */
	public ImageButton getLastImgButton(){
		return mLastImgBtn;
	}

	public boolean isNeedShowClearButton() {
		return isNeedShowClearButton;
	}

	public void setNeedShowClearButton(boolean isNeedShowClearButton) {
		this.isNeedShowClearButton = isNeedShowClearButton;
	}
}
