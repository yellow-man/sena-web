package yokohama.yellow_man.sena.pages;

import java.io.Serializable;
import java.util.Map;

import yokohama.yellow_man.common_tools.FieldUtils;

/**
 * ページ情報基底クラス。
 * <p>ページ情報保持する場合、このクラスを継承する。
 *
 * @author yellow-man
 * @since 1.0.0-1.0
 */
@SuppressWarnings("serial")
public class AppPage implements Serializable {

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return FieldUtils.toStringField(this);
	}

	/**
	 * このオブジェクトのプロパティ変数名をキーに、変数の値を保持するマップを取得する。
	 * @return Map＜変数名, 変数の値＞
	 * @since 1.0.0-1.0
	 */
	public Map<String, Object> toMap() {
		return FieldUtils.toMapField(this);
	}
}
