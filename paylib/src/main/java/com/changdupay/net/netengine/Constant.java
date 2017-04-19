package com.changdupay.net.netengine;

public class Constant {
	
	protected final static String Version = "1.03";

	/**
	 * 已经存在文件，无需续传
	 */
	public final static int EXIST_FILE = 1;
	
	/**
	 * 正确
	 */
	public final static int CORRECT = 0;
	
	/**
	 * 错误
	 */
	public final static int ERROR = -1;
	
	/**
	 * 参数错误
	 */
	public final static int PARM_ERROR = -2;
	
	/**
	 * 内存错误
	 */
	public final static int MEMORY_ERROR = -3;
	
	/**
	 * 文件错误
	 */
	public final static int FILE_ERROR = -4;
	
	/**
	 * 没有网络连接
	 */
	public final static int CONNECT_ERROR = -5;
	
	/**
	 * 网络读取异常
	 */
	public final static int READ_ERROR = -6;
	
	/**
	 * 空间不足
	 */
	public final static int SPACE_INSUFFICIENT = -7;
	
	/**
	 * 内部未知错误
	 */
	public final static int INTERNAL_ERROR = -100;
	
	
//	protected final static String NULL_NET = "没有网络连接";
	
//	protected final static String NOT_NEED_DOWN = "本地已存在，不需续传";
	
	/*protected final static String ABNORMAL = "异常:";
	
	protected final static String NET_STATUS = "网络状态:";
	
	protected final static String DELETE_FILE = "删除已存在文件:";
	
	protected final static String CREATE_FILE = "创建文件:";
	
	protected final static String WRITEING = "开始写文件....";
	
	protected final static String WRITE_END = "写文件结束";
	
	protected final static String UPING = "开始上传文件......";
	
	protected final static String UP_END = "文件上传结束";*/
	
}
