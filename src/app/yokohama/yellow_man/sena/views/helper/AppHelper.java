package yokohama.yellow_man.sena.views.helper;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Viewで共通に利用するヘルパークラス。
 *
 * @author yellow-man
 * @since 1.0
 */
@SuppressWarnings("serial")
public class AppHelper implements Serializable {

	/** フォーマット：数値「{@code #,###;-#,###}」 */
	public static final String NUM_FORMAT_ASIGN_COMMA = "#,###";
	/** フォーマット：数値「{@code #,###.00;-#,###.00}」 */
	public static final String NUM_FORMAT_ASIGN_COMMA_POINT = "#,##0.00";

	/**
	 * パラメータ{@code number}を{@code format}で指定されたフォーマットに変換した値を返す。
	 * @param number 数値
	 * @param format フォーマット
	 * @return {@code format}に変換された値。
	 */
	public static String format(Number number, String format) {
		DecimalFormat df = new DecimalFormat(format);
		if (df.format(number).indexOf('.') == 0) {
			return "0" + df.format(number);
		}
		return df.format(number);
	}
}
