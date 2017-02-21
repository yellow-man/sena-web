package yokohama.yellow_man.sena.api.components.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import play.cache.Cache;
import yokohama.yellow_man.sena.api.params.DataTablesParams;
import yokohama.yellow_man.sena.core.definitions.AppConsts;
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
		// キャッシュキー
// TODO yellow-man ClassUtils.getMethodName() がバグってる。
//		String cacheKey = StocksComponent.class.getName() + ":" + ClassUtils.getMethodName() + ":" + date;
		String cacheKey = StocksComponent.class.getName() + ":" + "getStocksTotalCountByDateCache" + ":" + date;

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (int) cache;
		}

		int count = getTotalCountByDate(date);

		// 取得データをキャッシュに保持
		Cache.set(cacheKey, count, AppConsts.CACHE_TIME_LONG);
		return count;
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
		// 検索文字列取得
		String searchValue = "";
		if (params.search != null && params.search.containsKey(DataTablesParams.MAP_KEY_SEARCH_VALUE)) {
			searchValue = params.search.get(DataTablesParams.MAP_KEY_SEARCH_VALUE);
		}

		// キャッシュキー
// TODO yellow-man ClassUtils.getMethodName() がバグってる。
//		String cacheKey = StocksComponent.class.getName() + ":" + ClassUtils.getMethodName() + ":" + date + ":" + encryptStr(searchValue);
		String cacheKey = StocksComponent.class.getName() + ":" + "getStocksFilterCountByDateCache" + ":" + date + ":" + encryptStr(searchValue);

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (int) cache;
		}

		searchValue = "%" + searchValue + "%";

		String sql = "SELECT count(*) as count FROM stocks WHERE delete_flg = false AND date = :date AND (stock_code LIKE :searchValue OR stock_name LIKE :searchValue)";
		SqlRow sqlRow = Ebean.createSqlQuery(sql)
				.setParameter("date", date)
				.setParameter("searchValue", searchValue)
				.findUnique();

		int count = sqlRow.getInteger("count");

		// 取得データをキャッシュに保持
		Cache.set(cacheKey, count, AppConsts.CACHE_TIME_LONG);
		return count;
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
		// 検索文字列取得
		String searchValue = "";
		if (params.search != null && params.search.containsKey(DataTablesParams.MAP_KEY_SEARCH_VALUE)) {
			searchValue = params.search.get(DataTablesParams.MAP_KEY_SEARCH_VALUE);
		}

		// 取得件数
		int limit = 10;
		if (params.length != null) {
			limit = params.length;
		}

		// ページ
		int page = 0;
		if (params.start != null) {
			page = params.start / limit;
		}

		// キャッシュキー
// TODO yellow-man ClassUtils.getMethodName() がバグってる。
//		String cacheKey = StocksComponent.class.getName() + ":" + ClassUtils.getMethodName() + ":" + date + ":" + encryptStr(searchValue);
		String cacheKey = StocksComponent.class.getName() + ":" + "getStocksListByDateCache" + ":" + date + ":" + encryptStr(searchValue) + ":" + limit + ":" + page;

		Object cache = null;
		if ((cache = Cache.get(cacheKey)) != null) {
			// キャッシュが存在する場合は、キャッシュからデータを取得する。
			return (List<Stocks>) cache;
		}

		searchValue = "%" + searchValue + "%";

		List<Stocks> retList =
				Ebean.find(Stocks.class)
					.where()
					.eq("delete_flg", false)
					.eq("date", date)
					.disjunction()
						.like("stock_code", searchValue)
						.like("stock_name", searchValue)
					.endJunction()
					.orderBy("stock_code ASC")
					.findPagingList(limit)
					.setFetchAhead(false)
					.getPage(page)
					.getList();

		// 取得データをキャッシュに保持
		if (retList != null) {
			Cache.set(cacheKey, retList, AppConsts.CACHE_TIME_LONG);
		}
		return retList;
	}

	/**
	 * TODO yellow-man とりあえず一旦privateメソッドで。common-tools 移動予定。
	* @param text ハッシュ化するテキスト。
	* @return ハッシュ化した計算値(16進数)。
	*/
	private static String encryptStr(String text) {
		// 変数初期化
		MessageDigest md = null;
		StringBuffer buffer = new StringBuffer();

		try {
			// メッセージダイジェストインスタンス取得
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// 例外発生時、エラーメッセージ出力
			System.out.println("指定された暗号化アルゴリズムがありません。");
		}

		// メッセージダイジェスト更新
		md.update(text.getBytes());

		// ハッシュ値を格納
		byte[] valueArray = md.digest();

		// ハッシュ値の配列をループ
		for(int i = 0; i < valueArray.length; i++){
			// 値の符号を反転させ、16進数に変換
			String tmpStr = Integer.toHexString(valueArray[i] & 0xff);

			if(tmpStr.length() == 1){
				// 値が一桁だった場合、先頭に0を追加し、バッファに追加
				buffer.append('0').append(tmpStr);
			} else {
				// その他の場合、バッファに追加
				buffer.append(tmpStr);
			}
		}

		// 完了したハッシュ計算値を返却
		return buffer.toString();
	}
}
