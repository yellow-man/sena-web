package yokohama.yellow_man.sena.api.components.db;

import java.util.Date;
import java.util.List;

import yokohama.yellow_man.sena.api.params.DataTablesParams;
import yokohama.yellow_man.sena.core.models.Stocks;

/**
 * 銘柄（stocks）モデルの操作を行うコンポーネントクラス。
 * <p>共通コンポーネント{@link yokohama.yellow_man.sena.components.db.StocksComponent}を拡張する。
 *
 * @author yellow-man
 * @since 1.1.0-1.1
 * @see yokohama.yellow_man.sena.components.db.StocksComponent
 */
public class StocksComponent extends yokohama.yellow_man.sena.components.db.StocksComponent {

	/**
	 * 検索条件に取得日（{@code date}）を指定し、
	 * 未削除の銘柄（stocks）情報一覧総数を返す。（※キャッシュ：1時間）
	 *
	 * @param date 取得日
	 * @return 未削除の銘柄（stocks）情報一覧総数
	 * @since 1.1.0-1.1
	 */
	public static int getStocksTotalCountByDateCache(Date date) {
		return 100;
	}

	/**
	 * 検索条件に取得日（{@code date}）とDataTablesパラメータ（{@code params}）を指定し、
	 * 未削除の銘柄（stocks）情報一覧総数を返す。（※キャッシュ：1時間）
	 *
	 * @param date 取得日
	 * @param params DataTablesパラメータ
	 * @return 未削除の銘柄（stocks）情報一覧フィルタアリング後総数
	 * @since 1.1.0-1.1
	 */
	public static int getStocksFilterCountByDateCache(Date date, DataTablesParams params) {
		return 100;
	}

	/**
	 * 検索条件に取得日（{@code date}）とDataTablesパラメータ（{@code params}）を指定し、
	 * 未削除の銘柄（stocks）情報一覧を返す。（※キャッシュ：1時間）
	 *
	 * @param date 取得日
	 * @param params DataTablesパラメータ
	 * @return 未削除の銘柄（stocks）情報一覧
	 * @since 1.1.0-1.1
	 */
	@SuppressWarnings("unchecked")
	public static List<Stocks> getStocksListByDateCache(Date date, DataTablesParams params) {
		return null;
	}
}
