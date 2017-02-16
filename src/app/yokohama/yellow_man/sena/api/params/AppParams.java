package yokohama.yellow_man.sena.api.params;

import yokohama.yellow_man.common_tools.FieldUtils;

/**
 * リクエストパラメータマッピング基底クラス。
 * @author yellow-man
 * @since 1.1.0-1.1
 */
public class AppParams {

	/**
	 * フィールドの値を文字列として取得する。
	 *
	 * @since 1.1.0-1.1
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return FieldUtils.toStringField(this);
	}
}
